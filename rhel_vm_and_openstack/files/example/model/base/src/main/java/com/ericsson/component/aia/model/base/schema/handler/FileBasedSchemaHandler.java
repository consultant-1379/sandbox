/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.schema.handler;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.*;

import java.io.IOException;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.meta.schema.Schema;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;

public class FileBasedSchemaHandler extends SchemaHandler {
    
    /**
     * @throws ResourceNotFoundException
     * @throws SchemaException
     */
    public FileBasedSchemaHandler(final SchemaEnum neType) {
        super(neType);
    }

    @Override
    public SchemaHandler load() throws ResourceNotFoundException, SchemaException {
        super.load();
        for (final String filePath : getSchemaType().getXmlFilesSet()) {
            try {
                final Schema schema = new Schema(getSchemaType().getName(), filePath, EVENT_SCHEMA_XSD_FILE_LOCATION, getSchemaType().getParamPreamble(), getSchemaType().getValuePreamble());
                schemaMap.put(schema.getGeneralHandler().getGeneralInfo().getFfvFivKey(), schema);
            } catch (JaxenException | SchemaException | JDOMException | IOException exception) {
                LOGGER.error("loading of schema of type {} from file {} failed ", getSchemaType().getName(), filePath, exception);
            }
        }
        return this;
    }
    

    @Override
    public Schema getSchema(final String docNo, final String ffv, final String fiv) {
        final FfvFivKey ffvFivKey = new FfvFivKey(docNo, ffv, fiv);
        return getSchema(ffvFivKey);
    }

    @Override
    public Schema getSchema(final FfvFivKey ffvFivKey) {
        return schemaMap.get(ffvFivKey);
    }
    
    @Override
    public Schema getTreatAsSchema(final String docNo, final String ffv, final String fiv) {
        final FfvFivKey ffvFivKey = new FfvFivKey(docNo, ffv, fiv);
        return getTreatAsSchema(ffvFivKey);
    }

    @Override
    public Schema getTreatAsSchema(final FfvFivKey ffvFivKey) {
        return getClosestSchema(ffvFivKey);
    }
    
    private Schema getClosestSchema(final FfvFivKey fileVersionKey) {
        Schema closestSchema = null;
        final FfvFivKey floorVersionKey = schemaMap.floorKey(fileVersionKey);
        if (floorVersionKey != null && fileVersionKey != null) {
            closestSchema = schemaMap.get(floorVersionKey);
            if (!fileVersionKey.getFileFormatVersion().equals(floorVersionKey.getFileFormatVersion())) {
                LOGGER.info("The closest match found for : ({}), but its ffv : '{}' does not match ffv :'{}'",floorVersionKey.toString(), floorVersionKey.getFileFormatVersion(), fileVersionKey.getFileFormatVersion());
                return null;
            }
        }
        LOGGER.info("Will treat fiv/ffv ({})  as ({})", fileVersionKey.toString(), floorVersionKey.toString());
        return closestSchema;
    }
}


