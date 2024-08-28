/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.schema.handler;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.EVENT_SCHEMA_XSD_FILE_LOCATION;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;

import com.ericsson.component.aia.model.base.meta.schema.Schema;

/**
 * {@link SchemaHandler} implementation that reads schemas from files. <br>
 * Comparing to {@link FileBasedSchemaHandler} this implementation does not load all schemas into memory at initialization but only when they are
 * actually needed, i.e. when {@code getSchema} or {@code getTreatAsSchema} methods are called. It gives better performance for short living
 * applications as only needed 101/ files are parsed.
 * 
 * @author eshuand
 *
 */
public class FileBasedLazyLoadingSchemaHandler extends SchemaHandler {

    protected NavigableSet<FfvFivKey> keysMap = new TreeSet<FfvFivKey>();

    private static final Pattern FILES_NAME_PATTERN = Pattern.compile("([a-z0-9]*)_([a-z0-9]*)_*?([0-9]*)*?.xml", Pattern.CASE_INSENSITIVE);

    public FileBasedLazyLoadingSchemaHandler(final SchemaEnum neType) {
        super(neType);
    }

    @Override
    public SchemaHandler load() throws ResourceNotFoundException, SchemaException {
        super.load();
        buildKeysMap();
        return this;
    }

    @Override
    public Schema getSchema(final FfvFivKey ffvFivKey) {
        if (schemaMap.containsKey(ffvFivKey)) {
            return schemaMap.get(ffvFivKey);
        }

        final String filePath = generateFileName(ffvFivKey);
        return createAndGetSchema(filePath);
    }

    @Override
    public Schema getSchema(final String docNo, final String ffv, final String fiv) {
        final FfvFivKey ffvFivKey = new FfvFivKey(docNo, ffv, fiv);
        return getSchema(ffvFivKey);
    }

    @Override
    public Schema getTreatAsSchema(final FfvFivKey ffvFivKey) {
        return getClosestSchema(ffvFivKey);
    }

    @Override
    public Schema getTreatAsSchema(final String docNo, final String ffv, final String fiv) {
        final FfvFivKey ffvFivKey = new FfvFivKey(docNo, ffv, fiv);
        return getTreatAsSchema(ffvFivKey);
    }

    private String generateFileName(final FfvFivKey ffvFivKey) {
        final StringBuilder builder = new StringBuilder("xml").append("/").append(getNeType().value()).append("/");
        builder.append(ffvFivKey.getFileFormatVersion()).append("_").append(ffvFivKey.getFileInformationVersion());
        if (ffvFivKey.getIteration() != null) {
            builder.append("_").append(ffvFivKey.getIteration());
        }
        return builder.append(".xml").toString();
    }

    void buildKeysMap() {
        for (final String filePath : getSchemaType().getXmlFilesSet()) {
            final Path path = Paths.get(filePath);
            final Matcher matcher = FILES_NAME_PATTERN.matcher(path.getFileName().toString());
            if (matcher.matches()) {
                final FfvFivKey key = new FfvFivKey(matcher.group(3), matcher.group(1), matcher.group(2));
                keysMap.add(key);
            } else {
                LOGGER.error("The file {} has invalid name pattern, it can not be used as a schema definition file.", filePath);
            }
        }
    }

    Schema createAndGetSchema(final String filePath) {
        if (getSchemaType().getXmlFilesSet().contains(filePath)) {
            try {
                final Schema schema = createSchema(filePath);
                schemaMap.put(schema.getGeneralHandler().getGeneralInfo().getFfvFivKey(), schema);
                return schema;
            } catch (JaxenException | SchemaException | JDOMException | IOException exception) {
                LOGGER.error("loading of schema of type {} from file {} failed ", getSchemaType().getName(), filePath, exception);
            }
        }

        return null;
    }

    Schema createSchema(final String filePath) throws SchemaException, JDOMException, IOException, JaxenException {
        final Schema schema = new Schema(getSchemaType().getName(), filePath, EVENT_SCHEMA_XSD_FILE_LOCATION, getSchemaType().getParamPreamble(),
                getSchemaType().getValuePreamble());
        return schema;

    }

    private Schema getClosestSchema(final FfvFivKey fileVersionKey) {
        Schema closestSchema = null;
        final FfvFivKey floorVersionKey = keysMap.floor(fileVersionKey);
        if (floorVersionKey != null && fileVersionKey != null) {
            if (!fileVersionKey.getFileFormatVersion().equals(floorVersionKey.getFileFormatVersion())) {
                LOGGER.info("The closest match found for : ({}), but its ffv : '{}' does not match ffv :'{}'", floorVersionKey.toString(),
                        floorVersionKey.getFileFormatVersion(), fileVersionKey.getFileFormatVersion());
                return null;
            } else {
                closestSchema = getSchema(floorVersionKey);
            }
        }
        LOGGER.info("Will treat fiv/ffv ({})  as ({})", fileVersionKey.toString(), floorVersionKey.toString());
        return closestSchema;
    }

}
