/**
 * (C) Copyright LM Ericsson System Expertise AT/LMI, 2016
 *
 * The copyright to the computer program(s) herein is the property of Ericsson
 * System Expertise EEI, Sweden. The program(s) may be used and/or copied only
 * with the written permission from Ericsson System Expertise AT/LMI or in
 * accordance with the terms and conditions stipulated in the agreement/contract
 * under which the program(s) have been supplied.
 *
 * Entity: apex-apps.uservice-adapt:com.ericsson.apex.apps.uservice.TestKafkaSubscriber
 * File: TestKafkaSubscriber.java
 * Date: 7 Jul 2016
 */

package com.ericsson.nmlab.aiasupport.datasink;

import java.util.Arrays;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class KafkaEventSink {
	// The logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(KafkaEventSink.class);
	
	KafkaConsumer<String, GenericRecord> consumer = null;

	public KafkaEventSink(String kafkaServerAddress, String schemaRegistryAddress, String aiaTopicName) {
		logger.info("receiving events from AIA topic \"" + aiaTopicName + "\" as JSON");
		logger.info("using kafka server at \"" + kafkaServerAddress + "\"");
		logger.info("using schema registry at \"" + schemaRegistryAddress + "\"");
		
        System.setProperty("schemaRegistry.address", schemaRegistryAddress);

		Properties consumerProperties = new Properties();
		consumerProperties.put("bootstrap.servers", kafkaServerAddress);
		consumerProperties.put("group.id", "test");
		consumerProperties.put("enable.auto.commit", "true");
		consumerProperties.put("auto.commit.interval.ms", "1000");
		consumerProperties.put("session.timeout.ms", "30000");
		consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.put("value.deserializer", "com.ericsson.component.aia.common.avro.kafka.decoder.KafkaGenericRecordDecoder");
		
		consumer = new KafkaConsumer<String, GenericRecord>(consumerProperties);
		consumer.subscribe(Arrays.asList(aiaTopicName));

		Runtime.getRuntime().addShutdownHook(new KafkaAIAEventProxyShutdownHook());
		
		long recordCount = 0;
		while (!Thread.currentThread().isInterrupted()) {
			ConsumerRecords<String, GenericRecord> records = consumer.poll(100);
			for (ConsumerRecord<String, GenericRecord> record : records) {
				Object idObject = record.value().get("_ID");
				Integer id = -1;
				if (idObject != null && idObject instanceof Integer) {
					id = (Integer)idObject;
				}
				
				System.out.println("Record no:" + recordCount + ", AIA event id=" + id + ", value= " + record.value());
				recordCount++;
			}
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("usage: KafkaEventSink kafkaServerAddress schemaRegistryAddress KafkaTopic");
			System.err.println("  example : KafkaEventSink localhost:9092 http://localhost:8081 mytopic");
			return;
		}
		
		String kafkaServerAddress    = args[0];
		String schemaRegistryAddress = args[1];
		String aiaTopicName          = args[2];

		new KafkaEventSink(kafkaServerAddress, schemaRegistryAddress, aiaTopicName);
	}

	private class KafkaAIAEventProxyShutdownHook extends Thread {
		@Override
		public void run() {
			consumer.close();
		}
	}
}
