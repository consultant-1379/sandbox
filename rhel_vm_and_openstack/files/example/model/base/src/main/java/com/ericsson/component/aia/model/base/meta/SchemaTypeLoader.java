/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.component.aia.model.base.meta;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.*;

import java.io.InputStream;
import java.util.*;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.base.resource.loading.ResourceFileFinder;
import com.ericsson.component.aia.model.base.util.xml.XMLDocumentHandler;

/**
 * This class instantiates a SchemaTypeLoader instance to handle schema types, such as cell trace and EBM.
 *
 */

public class SchemaTypeLoader {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaTypeLoader.class);

    private static final String SCHEMA_TYPE_NAMESPACE_PREFIX = "st";

    private static final String SCHEMA_TYPE_XPATH_EXPRESSION = "/st:schemaTypes/st:schemaType";

    private final Map<SchemaEnum, SchemaTypeHandler> schemaTypeMap = new TreeMap<SchemaEnum, SchemaTypeHandler>();

    /**
     * @throws SchemaException
     * @throws ResourceNotFoundException
     */
    public SchemaTypeLoader() throws SchemaException, ResourceNotFoundException {
        this(DEFAULT_SCHEMA_TYPE_XML_FILE, DEFAULT_SCHEMA_TYPE_SCHEMA_FILE);
    }

    
    /**
     * @param document 
     * @throws SchemaException
     * @throws ResourceNotFoundException
     * @throws JaxenException
     */
    public SchemaTypeLoader(final Document document) throws SchemaException, ResourceNotFoundException, JaxenException {
        buildSchemaTypesMap(document);
    }
    
    /**
     * @param schemaTypeXmlFile location of SchemaType xml 
     * @param schemaTypeXsdFile location of SchemaType xsd 
     * @throws SchemaException
     * @throws ResourceNotFoundException
     */
    public SchemaTypeLoader(final String schemaTypeXmlFile, final String schemaTypeXsdFile) throws SchemaException, ResourceNotFoundException {
        LOGGER.debug("loading schemas . . .");
        final ResourceFileFinder resourceFileFinder = new ResourceFileFinder();
        
        final String schemaTypeSchemaFile = resourceFileFinder.getFileResourcePath(schemaTypeXsdFile);
        if (schemaTypeSchemaFile == null) {
            throw new SchemaException("failed to find schema type schema file configuration property: " + schemaTypeXsdFile);
        }

        final String xmlFile = resourceFileFinder.getFileResourcePath(schemaTypeXmlFile);
        if (xmlFile == null) {
            throw new SchemaException("failed to find schema type xml file configuration property: " + schemaTypeXmlFile);
        }

        LOGGER.debug("Schema type schema is at: {}", schemaTypeSchemaFile);
        LOGGER.debug("Schema type xml file is at: {}", schemaTypeXmlFile);

        try {
            final Map<String, String> schemaTypeSchemaMap = new HashMap<String, String>();
            schemaTypeSchemaMap.put(SCHEMA_TYPE_NAMESPACE, schemaTypeSchemaFile);

            final XMLDocumentHandler documentHandler = new XMLDocumentHandler(schemaTypeSchemaMap);

            final InputStream inputStream = resourceFileFinder.getFileResourceAsStream(schemaTypeXmlFile);

            final Document schemaTypeDocument = documentHandler.loadAndValidate(inputStream);

            buildSchemaTypesMap(schemaTypeDocument);

        } catch (final Exception e) {
            LOGGER.error("schemaType loading failed", e);
            throw new SchemaException("schema loading failed");
        }

        LOGGER.debug("schema loading completed");
    }

    private void buildSchemaTypesMap(final Document schemaTypeDocument) throws JaxenException, SchemaException {
        final Map<String, String> nameSpaceMap = new HashMap<String, String>();
        nameSpaceMap.put(SCHEMA_TYPE_NAMESPACE_PREFIX, SCHEMA_TYPE_NAMESPACE);
        
        final JDOMXPath xPathSchemaType = new JDOMXPath(SCHEMA_TYPE_XPATH_EXPRESSION);
        xPathSchemaType.setNamespaceContext(new SimpleNamespaceContext(nameSpaceMap));

        @SuppressWarnings("unchecked")
        final List<Element> schemaTypeNodeList = xPathSchemaType.selectNodes(schemaTypeDocument);
        
        for (final Element schemaTypeElement : schemaTypeNodeList) {
            final SchemaTypeHandler schemaTypeHandler = new SchemaTypeHandler(schemaTypeElement);
            schemaTypeMap.put(schemaTypeHandler.getName(), schemaTypeHandler);
        }
    }

    /**
     * Get the schema map for all schemas
     *
     * @return The schema NeTypes map
     */
    public Map<SchemaEnum, SchemaTypeHandler> getSchemaTypeMap() {
        return schemaTypeMap;
    }

}
