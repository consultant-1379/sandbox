/**
 *
 * (C) Copyright LM Ericsson System Expertise AT/LMI, 2016
 *
 * The copyright to the computer program(s) herein is the property of Ericsson  System Expertise EEI, Sweden.
 * The program(s) may be used and/or copied only with the written permission from Ericsson System Expertise
 * AT/LMI or in  * accordance with the terms and conditions stipulated in the agreement/contract under which
 * the program(s) have been supplied.
 *
 */
package com.ericsson.aia.bps.streaming.correlations;

import org.apache.avro.generic.GenericRecord;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;

import com.amazonaws.util.StringUtils;
import com.ericsson.component.aia.common.avro.GenericRecordWrapper;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryResponseItem;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryRestClient;
import com.ericsson.component.aia.model.registry.impl.rest.SchemaRegistryRestClientFactory;
import com.ericsson.component.aia.services.bps.core.service.configuration.datasink.BpsDataSinkAdapters;
import com.ericsson.component.aia.services.bps.core.service.configuration.datasource.BpsDataSourceAdapters;
import com.ericsson.component.aia.services.bps.spark.jobrunner.SparkStreamingJobRunner;

import nl.basjes.parse.core.Parser;
import nl.basjes.parse.httpdlog.ApacheHttpdLoglineParser;
import scala.Tuple2;

/**
 * <h1>SparkStreamingExampleApp</h1> The SparkStreamingExampleApp implements an
 * application that simply streams messages from Kafka topic and displays it in
 * the Spark console.
 * 
 *
 * @author
 * @version 1.0
 * @since 2016-06-07
 */
public class SparkStreamingExampleApp extends SparkStreamingJobRunner {

	private static final long serialVersionUID = 5914257776162053954L;
	private static long hashId;
	private static Schema schema;
	private static final String LOG_FORMAT = "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\"";
	private static Parser<AccessLogRecord> accessLogParser;

	@Override
	public void initialize(final BpsDataSourceAdapters inputAdapters, final BpsDataSinkAdapters outputAdapters,
			final Properties properties) {
		super.initialize(inputAdapters, outputAdapters, properties);
		SchemaRegistryRestClient instance = SchemaRegistryRestClientFactory
				.getInstance(properties.getProperty("schema.registry", "http://localhost:8081"));
		final SchemaRegistryResponseItem inputItem = instance
				.lookup(properties.getProperty("event.type", "com.ericsson.aia.example.model.access_log"));
		schema = new Schema.Parser().parse(inputItem.getSchema());
		hashId = inputItem.getId();
		accessLogParser = new ApacheHttpdLoglineParser<AccessLogRecord>(AccessLogRecord.class, LOG_FORMAT);
		System.out.println(properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() {

		// Gets Kafka Stream as defined in flow.xml
		final JavaPairDStream<String, String> stream = (JavaPairDStream<String, String>) inputStreams
				.getStreams("radio-stream").getStreamRef();
		stream.print();

		JavaDStream<GenericRecord> map = stream.map(new Function<Tuple2<String, String>, GenericRecord>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public GenericRecord call(Tuple2<String, String> record) throws Exception {

				AccessLogRecord accessLogRecord = accessLogParser.parse(record._2);
				Map<String, String> results = accessLogRecord.getResults();
				System.out.println(results);

				GenericRecordWrapper genericRecordWrapper = new GenericRecordWrapper(hashId, schema);

				genericRecordWrapper.put("remote_host", getNotNullValue(results.get("IP:connection.client.host")));
				genericRecordWrapper.put("time", getNotNullValue(results.get("TIME.STAMP:request.receive.time")));
				genericRecordWrapper.put("requester", getNotNullValue(results.get("STRING:connection.client.user")));
				genericRecordWrapper.put("request_uri", getNotNullValue(results.get("HTTP.URI:request.firstline.uri")));
				genericRecordWrapper.put("http_status", Integer.valueOf(results.get("STRING:request.status.last")));
				String byteSent = results.get("BYTES:response.body.bytesclf");
				if (byteSent != null && byteSent.matches("\\d+")) {
					genericRecordWrapper.put("bytes_sent", Integer.valueOf(byteSent));
				} else {
					genericRecordWrapper.put("bytes_sent", -1);
				}
				genericRecordWrapper.put("referrer", getNotNullValue(results.get("HTTP.URI:request.referer")));
				genericRecordWrapper.put("user_agent",
						getBrowser(getNotNullValue(results.get("HTTP.USERAGENT:request.user-agent"))));
				return genericRecordWrapper;
			}

			private String getBrowser(String userAgent) {
				if (userAgent.contains("Firefox")) {
					return "Firefox";
				} else if (userAgent.contains("Chrome")) {
					return "Chrome";
				} else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
					return "Safari";
				} else if (userAgent.contains("Opera")) {
					return "Opera";
				} else if (userAgent.contains("MSIE")) {
					return "MSIE";
				} else {
					return "UNKNOWN";
				}
			}

			private String getNotNullValue(String string) {
				if (string == null) {
					return "";
				}
				return string;
			}

		});

		/*
		 * To save output to Hdfs, file, Alluxio etc as defined in flow.xml use
		 * persistRDD or persistRDDSinglePartition methods
		 */
		persist(map);

	}
}
