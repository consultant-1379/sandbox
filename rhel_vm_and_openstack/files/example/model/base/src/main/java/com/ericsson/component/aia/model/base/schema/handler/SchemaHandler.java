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

import java.util.NavigableMap;
import java.util.TreeMap;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.meta.SchemaTypeHandler;
import com.ericsson.component.aia.model.base.meta.SchemaTypeLoader;
import com.ericsson.component.aia.model.base.meta.schema.Schema;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SchemaHandler {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SchemaHandler.class);

    protected final NavigableMap<FfvFivKey, Schema> schemaMap;

    private SchemaTypeHandler schemaTypeHandler;

    private final SchemaEnum neType;
    
    public SchemaHandler(final SchemaEnum neType) {
        this.neType = neType;
        this.schemaMap = new TreeMap<FfvFivKey, Schema>();
    }

    private SchemaTypeHandler getSchemaTypeHandler(final SchemaEnum neType) throws ResourceNotFoundException, SchemaException {
        final SchemaTypeLoader schemaTypeLoader = new SchemaTypeLoader();
        return schemaTypeLoader.getSchemaTypeMap().get(neType);
    }
    
    /**
     * @return load Schema Type Xml and Schema xml files
     * @throws ResourceNotFoundException
     * @throws SchemaException
     */
    public SchemaHandler load() throws ResourceNotFoundException, SchemaException {
        this.schemaTypeHandler = getSchemaTypeHandler(neType);
        return null;
    }

    
    /**
     * @param docNo Document no 
     * @param ffv File Format Version
     * @param fiv File Information Version
     * @return matched Schema
     */
    public abstract Schema getSchema(final String docNo, final String ffv, final String fiv);
    
    /**
     * @param ffvFivKey
     * @return matched Schema
     */
    public abstract Schema getTreatAsSchema(final FfvFivKey ffvFivKey);
    
    /**
     * @param docNo Document no 
     * @param ffv File Format Version
     * @param fiv File Information Version
     * @return closest Schema
     */
    public abstract Schema getTreatAsSchema(final String docNo, final String ffv, final String fiv);
    
    /**
     * @param ffvFivKey
     * @return closest Schema
     */
    public abstract Schema getSchema(final FfvFivKey ffvFivKey);
    

    /**
     * @return the schemaTypeHandler
     */
    public SchemaTypeHandler getSchemaType() {
        return schemaTypeHandler;
    }

    /**
     * @return the neType
     */
    public SchemaEnum getNeType() {
        return neType;
    }
}
