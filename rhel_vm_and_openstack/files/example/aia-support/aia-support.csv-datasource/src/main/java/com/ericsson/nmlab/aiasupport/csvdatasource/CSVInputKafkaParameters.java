package com.ericsson.nmlab.aiasupport.csvdatasource;

public class CSVInputKafkaParameters {
	private String  fileName;
	private String  schemaName;
	private String  inputName;
	private String  kafkaServerAddress;
	private String  schemaRegistryAddress;
	private boolean verbose;

	public CSVInputKafkaParameters(String fileName, String schemaName, String inputName, String kafkaServerAddress,	String schemaRegistryAddress, boolean verbose) {
		super();
		this.fileName = fileName;
		this.schemaName = schemaName;
		this.inputName = inputName;
		this.kafkaServerAddress = kafkaServerAddress;
		this.schemaRegistryAddress = schemaRegistryAddress;
		this.verbose = verbose;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	public String getKafkaServerAddress() {
		return kafkaServerAddress;
	}
	public void setKafkaServerAddress(String kafkaServerAddress) {
		this.kafkaServerAddress = kafkaServerAddress;
	}
	public String getSchemaRegistryAddress() {
		return schemaRegistryAddress;
	}
	public void setSchemaRegistryAddress(String schemaRegistryAddress) {
		this.schemaRegistryAddress = schemaRegistryAddress;
	}
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getEventName() {
		return schemaName + "." + inputName;
	}

	@Override
	public String toString() {
		return "CSVInputKafkaParameters [fileName=" + fileName + ", schemaName=" + schemaName + ", inputName="
				+ inputName + ", kafkaServerAddress=" + kafkaServerAddress + ", schemaRegistryAddress="
				+ schemaRegistryAddress + ", verbose=" + verbose + "]";
	}
}