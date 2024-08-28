package com.ericsson.component.aia.model.generation.avro.json;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.eventbean.generation.GenerateUtils;
import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import com.ericsson.component.aia.model.base.meta.schema.*;
import com.ericsson.component.aia.model.base.schema.handler.FileBasedSchemaHandler;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.util.MappedEvent;
import com.google.gson.*;

/**
 * This class generates JSON file for Avro schema generation
 *
 * @author echchik
 *
 */
public class AvroSchemaGenerator {

    private final static String avroFileExtension = ".avsc";

    private final static Logger LOGGER = LoggerFactory.getLogger(AvroSchemaGenerator.class);

    public void genarateAvroSchemas(final String outputDir) throws SchemaException, IOException {
        for (final SchemaEnum schemaType : SchemaEnum.values()) {
            final JsonGenSchemaHandler schemaHandler = new JsonGenSchemaHandler(schemaType).load();
            if (schemaHandler.getSchemaType() != null) {
                genarateBaseAvroSchemasForSchemaType(outputDir, schemaType, schemaHandler);
                genarateAvroSchemasForSchemaTypeAndFfvFiv(outputDir, schemaType, schemaHandler);
            }
        }
    }

    private void genarateBaseAvroSchemasForSchemaType(final String outputDir, final SchemaEnum schemaType, final JsonGenSchemaHandler schemaHandler)
            throws IOException {
        LOGGER.info("Conversion proccess for base Avro schema for schema type {} starting.", schemaType.value());
        final Map<Event, MappedEvent> eventToMappedEvents = schemaHandler.getEventMap();
        if (eventToMappedEvents != null && !eventToMappedEvents.isEmpty()) {
            genarateBaseAvroSchemas(outputDir, schemaType, eventToMappedEvents.values());
        }
        LOGGER.info("Conversion proccess for base Avro schema for schema type {} completed.", schemaType.value());
    }

    private void genarateBaseAvroSchemas(final String outputDir, final SchemaEnum schemaType, final Collection<MappedEvent> mappedEvents)
            throws IOException {
        final String featureDir = getFeatureDirectoryName(outputDir, schemaType.value());
        createDirectory(featureDir);
        for (final MappedEvent mappedEvent : mappedEvents) {
            createBaseAvroSchemaJsonFile(featureDir, mappedEvent);
        }
    }

    private void genarateAvroSchemasForSchemaTypeAndFfvFiv(final String outputDir, final SchemaEnum schemaType,
                                                           final JsonGenSchemaHandler schemaHandler)
            throws IOException {
        LOGGER.info("Conversion proccess for {} starting.", schemaType.value());

        final Map<FfvFivKey, Schema> ffvFivKeyToschemaMap = schemaHandler.getSchemaMap();
        if (ffvFivKeyToschemaMap != null) {
            genarateAvroSchemasForFFvFiv(outputDir, schemaType, ffvFivKeyToschemaMap);
        }
        LOGGER.info("Conversion proccess for {} completed.", schemaType.value());
    }

    private void genarateAvroSchemasForFFvFiv(final String outputDir, final SchemaEnum schemaType, final Map<FfvFivKey, Schema> ffvFivKeyToschemaMap)
            throws IOException {
        for (final Entry<FfvFivKey, Schema> entry : ffvFivKeyToschemaMap.entrySet()) {
            final String dirName = getDirectoryName(outputDir, entry.getValue().getAvroPackageName());
            createDirectory(dirName);
            createJsonFiles(dirName, entry.getValue());
        }
    }

    protected void addDirectoryToClasspath(final String metadataDirName) {
        GenerateUtils.addDirectoryToClasspath(new File(metadataDirName));
    }

    private String getDirectoryName(final String userDefinedOutputDir, final String avroDirName) {

        String dirName = "src" + File.separator + "main" + File.separator + "resources" + File.separator + avroDirName;

        if (userDefinedOutputDir != null && !userDefinedOutputDir.isEmpty()) {
            dirName = userDefinedOutputDir + File.separator + avroDirName;
        }

        return dirName;
    }

    private String getFeatureDirectoryName(final String userDefinedOutputDir, final String feature) {

        String dirName = "src" + File.separator + "main" + File.separator + "resources" + File.separator + feature;

        if (userDefinedOutputDir != null && !userDefinedOutputDir.isEmpty()) {
            dirName = userDefinedOutputDir + File.separator + feature;
        }

        return dirName;
    }

    private void createDirectory(final String dirName) throws IOException {

        final File dir = new File(dirName);
        FileUtils.deleteDirectory(dir);
        if (!dir.mkdirs()) {
            throw new IOException("Unable to create " + dirName);
        }
        LOGGER.info("Output Directory created: {}", dirName);
    }

    private void createJsonFiles(final String dirName, final Schema schema) throws FileNotFoundException {
        for (final Event event : schema.getEventHandler().getMap().values()) {
            createAvroSchemaJsonFile(dirName, event);
        }
    }

    private void createAvroSchemaJsonFile(final String dirName, final Event event) throws FileNotFoundException {
        final JsonObject schemaJson = createHeader(dirName, event);
        final JsonArray fields = createFields(event.getParameterList());
        schemaJson.add("fields", fields);
        final String fileName = getFileName(dirName, event.getName());
        createAvroSchemaJsonFile(schemaJson, fileName);
    }

    private void createBaseAvroSchemaJsonFile(final String dirName, final MappedEvent mappedEvent) throws FileNotFoundException {
        final JsonObject schemaJson = createHeader(dirName, mappedEvent.getEvent());
        final JsonArray fields = createFields(mappedEvent.getParameterSet());
        schemaJson.add("fields", fields);
        final String fileName = getFileName(dirName, mappedEvent.getEvent().getName());
        createAvroSchemaJsonFile(schemaJson, fileName);
    }

    private void createAvroSchemaJsonFile(final JsonObject jsonObject, final String fileName) throws FileNotFoundException {
        final File file = new File(fileName);
        final PrintWriter out = new PrintWriter(file);

        LOGGER.info("Creating avro schema file for: {}", fileName);
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String prettyJsonString = gson.toJson(jsonObject);
        out.println(prettyJsonString);
        out.flush();
        out.close();
    }

    private String getFileName(final String dirName, final String eventName) {
        final StringBuilder fileName = new StringBuilder();
        fileName.append(dirName);
        fileName.append(File.separator);
        fileName.append(eventName);
        fileName.append(avroFileExtension);
        return fileName.toString();
    }

    private JsonObject createHeader(final String dirName, final Event event) {
        final JsonObject schemaJson = new JsonObject();
        schemaJson.addProperty("type", "record");
        schemaJson.addProperty("name", event.getName());
        schemaJson.addProperty("namespace", getNameSpace(dirName));
        return schemaJson;
    }

    private String getNameSpace(final String dirName) {
        final String nameSpace = dirName.substring(dirName.lastIndexOf(File.separator) + 1, dirName.length());
        return nameSpace;
    }

    private JsonArray createFields(final Collection<EventParameter> eventParameters) {
        final JsonArray fields = new JsonArray();
        updateDefaultFields(fields);
        for (final EventParameter element : eventParameters) {
            final JsonObject field = new JsonObject();
            field.addProperty("name", element.getName());
            if (element.isStructArray()) {
                final JsonObject fieldType = new JsonObject();
                fieldType.addProperty("type", "array");
                fieldType.addProperty("items", TypeConversionParserTypesToJson.typeSqlToJava(element.getParserType()));
                field.add("type", fieldType);
            } else {
                field.addProperty("type", TypeConversionParserTypesToJson.typeSqlToJava(element.getParserType()));
            }
            fields.add(field);
        }
        return fields;
    }

    private void updateDefaultFields(final JsonArray fields) {
        final JsonObject neField = new JsonObject();
        neField.addProperty("name", "_NE");
        neField.addProperty("type", "string");
        fields.add(neField);

        final JsonObject timeStampField = new JsonObject();
        timeStampField.addProperty("name", "_TIMESTAMP");
        timeStampField.addProperty("type", "long");
        fields.add(timeStampField);

        final JsonObject idField = new JsonObject();
        idField.addProperty("name", "_ID");
        idField.addProperty("type", "int");
        fields.add(idField);

        final JsonObject idVersion = new JsonObject();
        idVersion.addProperty("name", "_VERSION");
        idVersion.addProperty("type", "string");
        fields.add(idVersion);

    }

    class JsonGenSchemaHandler extends FileBasedSchemaHandler {

        private final Map<Event, MappedEvent> eventMap = new TreeMap<Event, MappedEvent>();

        public JsonGenSchemaHandler(final SchemaEnum neType) {
            super(neType);
        }

        @Override
        public JsonGenSchemaHandler load() throws ResourceNotFoundException, SchemaException {
            super.load();
            buildEventParameterMap();
            return this;
        }

        /**
         * Build the event parameter map using the rules described in the comment at the top of this class
         */
        private void buildEventParameterMap() {
            for (final Schema schema : schemaMap.descendingMap().values()) {
                for (final Event event : schema.getEventHandler().getMap().values()) {

                    MappedEvent mappedEvent = null;

                    if (eventMap.containsKey(event)) {
                        mappedEvent = eventMap.get(event);

                    } else {
                        mappedEvent = new MappedEvent(event);
                        eventMap.put(event, mappedEvent);
                    }

                    mappedEvent.setParameters(event);
                }
            }
        }

        public Map<Event, MappedEvent> getEventMap() {
            return eventMap;
        }

        public Map<FfvFivKey, Schema> getSchemaMap() {
            return schemaMap;
        }
    }

    public static void main(final String args[]) throws SchemaException, IOException {

        LOGGER.info("starting Avro schema generation: [{} , {}]", args[0], args[1]);
        final AvroSchemaGenerator jsonGenerator = new AvroSchemaGenerator();
        jsonGenerator.addDirectoryToClasspath(args[0]);
        jsonGenerator.genarateAvroSchemas(args[1]);

    }

}