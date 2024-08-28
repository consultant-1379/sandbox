package com.ericsson.nmlab.aiasupport.csvdatasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ericsson.nmlab.aiasupport.csvdatasource.CSVInputKafkaParameters;
import com.ericsson.nmlab.aiasupport.csvdatasource.CSVInputKafkaSource;

public class SampleDataInput {

	private static Map<String, String> csvFile2EventMap = new HashMap<String, String>();

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("usage:   SampleDataInput ZookeeperAddress SchemaRegistryAddress");
			System.err.println("example: SampleDataInput localhost:9092 http://localhost:8081");
			return;
		}
		
		csvFile2EventMap.put("ItemInput.csv"     , "ITEM_INPUT");
		csvFile2EventMap.put("AssistantInput.csv", "ASSISTANT_INPUT");
		csvFile2EventMap.put("BranchInput.csv"   , "BRANCH_INPUT");
		csvFile2EventMap.put("SalesInput.csv"    , "SALES_INPUT");
		
		new SampleDataInput(args[0], args[1]);
	}

	public SampleDataInput(final String zookeeperAddress, final String schemaRegistryAddress) {

		for (Entry<String, String> csvFile2Event : csvFile2EventMap.entrySet()) {
			CSVInputKafkaParameters parameters = new CSVInputKafkaParameters(
					"src/main/resources/SampleData/" + csvFile2Event.getKey(),
					"correlation",
					csvFile2Event.getValue(),
					zookeeperAddress,
					schemaRegistryAddress,
					true);
			
			try {
				new CSVInputKafkaSource(parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
