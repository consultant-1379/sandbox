package com.ericsson.nmlab.aiasupport.samplecorrelation;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.generated.analytics.events.correlation.ASSISTANT_INPUT;
import com.ericsson.component.aia.model.generated.analytics.events.correlation.BRANCH_INPUT;
import com.ericsson.component.aia.model.generated.analytics.events.correlation.ITEM_INPUT;
import com.ericsson.component.aia.model.generated.analytics.events.correlation.SALES_EVENT;
import com.ericsson.component.aia.model.generated.analytics.events.correlation.SALES_INPUT;
import com.ericsson.component.aia.model.registry.client.SchemaRegistryClient;
import com.ericsson.component.aia.services.bps.core.service.configuration.datasink.BpsDataSinkAdapters;
import com.ericsson.component.aia.services.bps.core.service.configuration.datasource.BpsDataSourceAdapters;
import com.ericsson.component.aia.services.bps.spark.jobrunner.SparkStreamingJobRunner;

import scala.Tuple2;

public class SampleCorrelation extends SparkStreamingJobRunner {
	private static final String EVENT_PACKAGE_PREFIX = "com.ericsson.component.aia.model.generated.analytics.events";

	private static final long serialVersionUID = -7569198685638843800L;
	private static Logger logger = LoggerFactory.getLogger(SampleCorrelation.class);

	@Override
	public void initialize(final BpsDataSourceAdapters inputAdapters, final BpsDataSinkAdapters outputAdapters, final Properties properties) {

		logger.info("###########################INITIALIZE###################");
		super.initialize(inputAdapters, outputAdapters, properties);

		final String schemaURL = (String) properties.get("schemaRegistry.address");
		if (schemaURL != null && !schemaURL.isEmpty()) {
			final String cacheSize = (String) properties.get("schemaRegistry.cacheMaximumSize");
			final Properties schemaProp = new Properties();
			schemaProp.put("schemaRegistry.address", schemaURL);
			schemaProp.put("schemaRegistry.cacheMaximumSize", cacheSize);
			logger.info("Reinitializing SchemaRegistryClient with schemaRegistry.address ={} and  schemaRegistry.cacheMaximumSize ={} ", schemaURL, cacheSize);
			SchemaRegistryClient.INSTANCE.configureSchemaRegistryClient(schemaProp);
		}
		logger.info("###########################END INITIALIZE###################");
	}

	private long getLong(String longString) {
		if (longString == null || longString.trim().length() == 0) {
			return 0;
		}
		else {
			return Long.parseLong(longString);
		}
	}

	private float getFloat(String floatString) {
		if (floatString == null || floatString.trim().length() == 0) {
			return 0;
		}
		else {
			return Float.parseFloat(floatString);
		}
	}

	private Function<Tuple2<String, GenericRecord>, GenericRecord> filterRecordsOnly = new Function<Tuple2<String, GenericRecord>, GenericRecord>() {
		private static final long serialVersionUID = 1L;

		@Override
		public GenericRecord call(final Tuple2<String, GenericRecord> v1) throws Exception {
			return v1._2;
		}
	};

	private Function<GenericRecord, Object> genericRecord2Object = new Function<GenericRecord, Object>() {
		private static final long serialVersionUID = -1;

		@Override
		public Object call(final GenericRecord genericRecord) throws Exception {
			Class<?> genericRecordClass = Class.forName(EVENT_PACKAGE_PREFIX + "." + genericRecord.getSchema().getFullName());
			Method createMethod = genericRecordClass.getDeclaredMethod("create", GenericRecord.class);
			return createMethod.invoke(null, genericRecord);
		}
	};

	private JavaDStream<Object> getInputRecords(final String inputName) {
		final Properties inputProperties = inputStreams.getStreams(inputName).getProperties();
		final long inputWindowLength = getLong(inputProperties.getProperty("window.length"));
		final long inputSlideWindow  = getLong(inputProperties.getProperty("slide.window.length"));

		@SuppressWarnings("unchecked")
		final JavaPairInputDStream<String, GenericRecord> rawInputRecords = (JavaPairInputDStream<String, GenericRecord>) inputStreams.getStreams(inputName).getStreamRef();
		return rawInputRecords.window(Durations.seconds(inputWindowLength), Durations.seconds(inputSlideWindow))
				.map(filterRecordsOnly)
				.map(genericRecord2Object);
	}

	private PairFunction<Object, Long, ITEM_INPUT> itemKeyFunction = new PairFunction<Object, Long, ITEM_INPUT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<Long, ITEM_INPUT> call(final Object itemInputObject) throws Exception {
			ITEM_INPUT itemInput = (ITEM_INPUT)itemInputObject;
			return new Tuple2<Long, ITEM_INPUT>(getLong(itemInput.getITEM_ID()), itemInput);
		}
	};

	private PairFunction<Object, Long, ASSISTANT_INPUT> assistantKeyFunction = new PairFunction<Object, Long, ASSISTANT_INPUT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<Long, ASSISTANT_INPUT> call(final Object assistantInputObject) throws Exception {
			ASSISTANT_INPUT assistantInput = (ASSISTANT_INPUT)assistantInputObject;
			return new Tuple2<Long, ASSISTANT_INPUT>(getLong(assistantInput.getASSISTANT_ID()), assistantInput);
		}
	};

	private PairFunction<Object, String, BRANCH_INPUT> branchKeyFunction = new PairFunction<Object, String, BRANCH_INPUT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<String, BRANCH_INPUT> call(final Object branchInputObject) throws Exception {
			BRANCH_INPUT branchInput = (BRANCH_INPUT)branchInputObject;
			return new Tuple2<String, BRANCH_INPUT>(branchInput.getBRANCH_NAME(), branchInput);
		}
	};

	private Function<Object, SALES_INPUT> salesInputCastFunction = new Function<Object, SALES_INPUT>() {
		private static final long serialVersionUID = -1;

		@Override
		public SALES_INPUT call(final Object salesInputRecord) throws Exception {
			return (SALES_INPUT)salesInputRecord;
		}
	};

	private Function<SALES_INPUT, SALES_EVENT> salesInputMapFunction = new Function<SALES_INPUT, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public SALES_EVENT call(final SALES_INPUT salesInput) throws Exception {
			final SALES_EVENT salesEvent = new SALES_EVENT();
			salesEvent.setTIMESTAMP(salesInput.getTIMESTAMP());
			salesEvent.setNE       (salesInput.getNE());
			salesEvent.setID       (62340);
			
			salesEvent.setSALES_TRANSACTION_AMOUNT      (getFloat(salesInput.getAMOUNT()));
			salesEvent.setSALES_TRANSACTION_ASSISTANT_ID(getLong (salesInput.getASSISTANT_ID()));
			salesEvent.setSALES_TRANSACTION_BRANCH_NAME (         salesInput.getBRANCH_NAME());
			salesEvent.setSALES_TRANSACTION_ITEM_ID     (getLong (salesInput.getITEM_ID()));
			salesEvent.setSALES_TRANSACTION_NOTES       (         salesInput.getNOTES());
			salesEvent.setSALES_TRANSACTION_QUANTITY    (getLong (salesInput.getQUANTITY()));
			salesEvent.setSALES_TRANSACTION_SALE_ID     (getLong(salesInput.getSALE_ID()));

			return salesEvent;
		}
	};

	private PairFunction<SALES_EVENT, Long, SALES_EVENT> saleItemKeyFunction = new PairFunction<SALES_EVENT, Long, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<Long, SALES_EVENT> call(final SALES_EVENT salesEvent) throws Exception {
			return new Tuple2<Long, SALES_EVENT>(salesEvent.getSALES_TRANSACTION_ITEM_ID(), salesEvent);
		}
	};

	private Function<Tuple2<Long, Tuple2<SALES_EVENT, ITEM_INPUT>>, SALES_EVENT> saleItemMapFunction = new Function<Tuple2<Long, Tuple2<SALES_EVENT, ITEM_INPUT>>, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public SALES_EVENT call(final Tuple2<Long, Tuple2<SALES_EVENT, ITEM_INPUT>> joinedTuple) throws Exception {
			SALES_EVENT salesEvent  = joinedTuple._2._1;
			ITEM_INPUT  item        = joinedTuple._2._2;
			
			salesEvent.setITEM_DETAILS_BARCODE    (getLong (item.getBARCODE()));
			salesEvent.setITEM_DETAILS_CATEGORY   (         item.getCATEGORY());
			salesEvent.setITEM_DETAILS_COST_PRICE (getFloat(item.getCOST_PRICE()));
			salesEvent.setITEM_DETAILS_DESCRIPTION(         item.getDESCRIPTION());
			salesEvent.setITEM_DETAILS_ITEM_ID    (getLong (item.getITEM_ID()));
			salesEvent.setITEM_DETAILS_SUPPLIER_ID(getLong (item.getSUPPLIER_ID()));

			return salesEvent;
		}

	};

	private PairFunction<SALES_EVENT, Long, SALES_EVENT> saleAssistantKeyFunction = new PairFunction<SALES_EVENT, Long, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<Long, SALES_EVENT> call(final SALES_EVENT salesEvent) throws Exception {
			return new Tuple2<Long, SALES_EVENT>(salesEvent.getSALES_TRANSACTION_ASSISTANT_ID(), salesEvent);
		}
	};

	private Function<Tuple2<Long, Tuple2<SALES_EVENT, ASSISTANT_INPUT>>, SALES_EVENT> saleAssistantMapFunction = new Function<Tuple2<Long,Tuple2<SALES_EVENT, ASSISTANT_INPUT>>, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public SALES_EVENT call(final Tuple2<Long, Tuple2<SALES_EVENT, ASSISTANT_INPUT>> joinedTuple) throws Exception {
			SALES_EVENT     salesEvent = joinedTuple._2._1;
			ASSISTANT_INPUT assistant  = joinedTuple._2._2;
			
			salesEvent.setASSISTANT_DETAILS_AGE         (getLong(assistant.getAGE()));
			salesEvent.setASSISTANT_DETAILS_ASSISTANT_ID(getLong(assistant.getASSISTANT_ID()));
			salesEvent.setASSISTANT_DETAILS_FIRSTNAME   (        assistant.getFIRSTNAME());
			salesEvent.setASSISTANT_DETAILS_GRADE       (getLong(assistant.getGRADE()));
			salesEvent.setASSISTANT_DETAILS_MIDDLENAME  (        assistant.getMIDDLENAME());
			salesEvent.setASSISTANT_DETAILS_PHONE_NO    (getLong(assistant.getPHONE_NO()));
			salesEvent.setASSISTANT_DETAILS_SURNAME     (        assistant.getSURNAME());
			   
			return salesEvent;
		}

	};

	private PairFunction<SALES_EVENT, String, SALES_EVENT> saleBranchKeyFunction = new PairFunction<SALES_EVENT, String, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public Tuple2<String, SALES_EVENT> call(final SALES_EVENT salesEvent) throws Exception {
			return new Tuple2<String, SALES_EVENT>(salesEvent.getSALES_TRANSACTION_BRANCH_NAME(), salesEvent);
		}
	};

	private Function<Tuple2<String, Tuple2<SALES_EVENT, BRANCH_INPUT>>, SALES_EVENT> saleBranchMMapFunction = new Function<Tuple2<String, Tuple2<SALES_EVENT, BRANCH_INPUT>>, SALES_EVENT>() {
		private static final long serialVersionUID = -1;

		@Override
		public SALES_EVENT call(final Tuple2<String, Tuple2<SALES_EVENT, BRANCH_INPUT>> joinedTuple) throws Exception {
			SALES_EVENT  salesEvent  = joinedTuple._2._1;
			BRANCH_INPUT branch = joinedTuple._2._2;
			
			salesEvent.setBRANCH_DETAILS_BRANCH_CATEGORY(        branch.getBRANCH_CATEGORY());
			salesEvent.setBRANCH_DETAILS_BRANCH_ID      (getLong(branch.getBRANCH_ID()));
			salesEvent.setBRANCH_DETAILS_BRANCH_NAME    (        branch.getBRANCH_NAME());
			salesEvent.setBRANCH_DETAILS_CITY           (        branch.getCITY());
			salesEvent.setBRANCH_DETAILS_COUNTRY        (        branch.getCOUNTRY());
			salesEvent.setBRANCH_DETAILS_POSTCODE       (        branch.getPOSTCODE());
			salesEvent.setBRANCH_DETAILS_STREET         (        branch.getSTREET());

			return salesEvent;
		}
	};

	@Override
	public void executeJob() {
		logger.info("###########################EXEC###################");
		jssc.sparkContext().getConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		jssc.sparkContext().getConf().registerKryoClasses(new Class<?>[] { org.apache.avro.generic.GenericData.class });

		final JavaDStream<Object> itemInputRecords = getInputRecords("item-input");
		final JavaPairDStream<Long, ITEM_INPUT> itemRecordCache = itemInputRecords
				.mapToPair(itemKeyFunction)
				.cache();

		final JavaDStream<Object> assistantInputRecords = getInputRecords("assistant-input");
		final JavaPairDStream<Long, ASSISTANT_INPUT> assistantRecordCache = assistantInputRecords
				.mapToPair(assistantKeyFunction)
				.cache();

		final JavaDStream<Object> branchInputRecords = getInputRecords("branch-input");
		final JavaPairDStream<String, BRANCH_INPUT> branchRecordCache = branchInputRecords
				.mapToPair(branchKeyFunction)
				.cache();

		final JavaDStream<SALES_EVENT> salesEvents =
				getInputRecords("sales-input").
				map(salesInputCastFunction).
				map(salesInputMapFunction).
				mapToPair(saleItemKeyFunction).
				join(itemRecordCache).
				map(saleItemMapFunction).
				mapToPair(saleAssistantKeyFunction).
				join(assistantRecordCache).
				map(saleAssistantMapFunction).
				mapToPair(saleBranchKeyFunction).
				join(branchRecordCache).
				map(saleBranchMMapFunction);

		salesEvents.foreachRDD(new VoidFunction<JavaRDD<SALES_EVENT>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(final JavaRDD<SALES_EVENT> v1) throws Exception {
				v1.foreach(new VoidFunction<SALES_EVENT>() {
					private static final long serialVersionUID = -766351075170666899L;

					@Override
					public void call(SALES_EVENT arg0) throws Exception {
						logger.info(arg0.getCSVString());
					}
				});
			}
		});

		salesEvents.foreachRDD(new VoidFunction<JavaRDD<SALES_EVENT>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(final JavaRDD<SALES_EVENT> fuzzyEvent) throws Exception {
				if (!fuzzyEvent.isEmpty()) {
					final DataFrame genericRecordDF = getHiveContext().createDataFrame(fuzzyEvent, SALES_EVENT.class);
					outGoingStreams.write(genericRecordDF);
				}
			}
		});

		logger.info("###########################END EXEC###################");
	}
}
