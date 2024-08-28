package com.ericsson.nmlab.aiasupport.csvdatasource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.component.aia.common.avro.GenericRecordWrapper;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryResponseItem;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryRestClient;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryRestClientFactory;

public class CSVInputKafkaSource {
	
	// The logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(CSVInputKafkaSource.class);

	private KafkaProducer<String, GenericRecordWrapper> producer = null;
	
	private CSVParser csvParser = null;

	public CSVInputKafkaSource(final CSVInputKafkaParameters parameters) throws IOException {
		System.setProperty("schemaRegistry.address", parameters.getSchemaRegistryAddress());

		Properties producerProperties = new Properties();
		producerProperties.put("bootstrap.servers", parameters.getKafkaServerAddress());
		producerProperties.put("acks", "all");
		producerProperties.put("retries", 0);
		producerProperties.put("batch.size", 16384);
		producerProperties.put("linger.ms", 1);
		producerProperties.put("buffer.memory", 33554432);
		producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerProperties.put("value.serializer", "com.ericsson.component.aia.common.avro.kafka.encoder.KafkaGenericRecordEncoder");

		producer = new KafkaProducer<String, GenericRecordWrapper>(producerProperties);

		Runtime.getRuntime().addShutdownHook(new KafkaAIAEventProxyShutdownHook());

		SchemaRegistryRestClient schemaRegistryClient = SchemaRegistryRestClientFactory.getInstance(parameters.getSchemaRegistryAddress());
		SchemaRegistryResponseItem inputRegistryItem = schemaRegistryClient.lookup(parameters.getEventName());
		Schema iaflbInputAvroSchema = new Schema.Parser().parse(inputRegistryItem.getSchema());

		final Reader csvReader = new FileReader(parameters.getFileName());
		csvParser = new CSVParser(csvReader, CSVFormat.RFC4180.withHeader());

		for (final CSVRecord csvRecord: csvParser) {
			// Create the event for serialization to JSON
			final GenericRecordWrapper genericRecordWrapper = new GenericRecordWrapper(inputRegistryItem.getId(), iaflbInputAvroSchema);

			// Set the event data
			genericRecordWrapper.put("_TIMESTAMP",            System.currentTimeMillis());
			genericRecordWrapper.put("_NE",                   parameters.getInputName());
			genericRecordWrapper.put("_ID",                   genericRecordWrapper.getSchemaId());
			genericRecordWrapper.put("_VERSION",              "0.0.1");
			
			for (String csvColumn : csvParser.getHeaderMap().keySet()) {
				genericRecordWrapper.put(csvColumn2EventFieldName(csvColumn), csvRecord.get(csvColumn).toString());
			}

			if (parameters.isVerbose() || logger.isTraceEnabled()) {
				logger.info(genericRecordWrapper.toString());
			}
			
			producer.send(
					new ProducerRecord<String, GenericRecordWrapper>(
							parameters.getInputName(),
							Long.toString(genericRecordWrapper.getSchemaId()),
							genericRecordWrapper)
					);
		}
	}

	private String csvColumn2EventFieldName(String csvColumn) {
		return csvColumn
				.replaceAll("\\(.*\\)", "")
				.replaceAll("-", " ")
				.replaceAll("\\.", " ")
				.trim()
				.replaceAll("\\s+", "_")
				.toUpperCase();
	}

	/**
	 * The Class KafkaAIAEventProxyShutdownHook.
	 */
	private class KafkaAIAEventProxyShutdownHook extends Thread {
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			if (producer != null) {
				producer.close();
			}
			if (csvParser != null) {
				try {
					csvParser.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
