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
package com.ericsson.component.aia.model.base.eventbean.generation;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import com.ericsson.component.aia.model.base.meta.schema.Event;
import com.ericsson.component.aia.model.base.meta.schema.Schema;
import com.ericsson.component.aia.model.base.schema.handler.FileBasedSchemaHandler;
import com.ericsson.component.aia.model.base.schema.handler.SchemaHandler;
import com.ericsson.component.aia.model.base.util.*;

/**
 * This class generates a java package for each schema and a java class for each event.
 *
 */
public class GenerateEventBeans {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateEventBeans.class);

    protected void generate(final String[] args) {
        checkArguments(args);

        final File metadataDir = new File(args[0] + File.separator + RESOURCES_PARENT_DIRECTORY);
        final File fileDir = new File(args[1]);

        try {
            new DirectoryUtil(metadataDir, fileDir).initializeDirectoryStructure();
        } catch (final IOException e1) {
            e1.printStackTrace();
            throw new GenerationException(e1);
        }

        addDirectoryToClasspath(args[0]);

        if (isEventBeanGenerationRequired(metadataDir, fileDir)) {
            try {
                generateEvents(args);
            } catch (SchemaException | IOException e) {
                e.printStackTrace();
                throw new GenerationException(e);
            }
        }
    }

    /**
     * This method generates the schema packages and event classes usage: GenerateEventBeans config_dir output_dir
     *
     * @param Command
     *            line arguments (args[0] should be Resource directory and args[1] should be output package directory)
     */
    public static void main(final String[] args) {
        new GenerateEventBeans().generate(args);
    }

    /**
     * @param args
     * @throws SchemaException
     * @throws IOException
     */
    protected void generateEvents(final String[] args) throws SchemaException, IOException {
        final String outputDir = args[1];
        final String eventBeanPackage = DEFAULT_EVENT_CLASS_PACKAGE;
        final File ebPackageDir = new File(outputDir + '/' + eventBeanPackage.replaceAll("\\.", "/"));
        defineDirectoryForEventClassPackage(ebPackageDir);
        File beanPackage = new File(outputDir + '/' + DEFAULT_BEAN_EVENT_CLASS_PACKAGE.replaceAll("\\.", "/"));
        defineDirectoryForEventClassPackage(beanPackage);
        for (final SchemaEnum schemaType : SchemaEnum.values()) {
            final PojoGenSchemaHandler schemaHandler = getSchemaHandler(schemaType);
            if (schemaHandler.getSchemaType() != null) {
                generateBaseClasses(ebPackageDir, schemaType, schemaHandler.getEventMap(), eventBeanPackage);
                generateSubClasses(ebPackageDir, schemaType, schemaHandler.getSchemaMap(), eventBeanPackage);
                generateBeanClasses(beanPackage, schemaType, schemaHandler.getEventMap(), DEFAULT_BEAN_EVENT_CLASS_PACKAGE);
            }
        }
    }

    protected PojoGenSchemaHandler getSchemaHandler(final SchemaEnum schemaType) throws ResourceNotFoundException, SchemaException {
        return (PojoGenSchemaHandler) new PojoGenSchemaHandler(schemaType).load();
    }

    /**
     * @param ebPackageDir
     * @param schemaType
     * @param schemaMap
     * @param eventBeanPackage
     * @throws IOException
     */
    void generateSubClasses(final File ebPackageDir, final SchemaEnum schemaType, final Map<FfvFivKey, Schema> schemaMap,
                            final String eventBeanPackage)
            throws IOException {
        for (final FfvFivKey schemaKey : schemaMap.keySet()) {

            final Schema schema = schemaMap.get(schemaKey);
            final String schemaName = schema.getPackageName();

            final File schemaPackageDir = new DirectoryUtil().createFolder(ebPackageDir, schemaName);

            for (final Event event : schema.getEventHandler().getMap().values()) {
                final JavaClassGenerator generator = getJavaClassGenerator();
                generator.generateSubJavaClass(schemaPackageDir, schemaType, event, eventBeanPackage);
            }

            LOG.info("event classes generated for schema {}", schemaName);
        }
    }

    /**
     * @return
     */
    protected JavaClassGenerator getJavaClassGenerator() {
        return new JavaClassGenerator();
    }

    /**
     * Pure base bean classes for analytics purpose
     * 
     * @param ebPackageDir
     * @param schemaType
     * @param evebt
     * @param eventBeanPackage
     * @throws IOException
     */
    private void generateBeanClasses(final File ebPackageDir, final SchemaEnum schemaType, final Map<Event, MappedEvent> eventMap,
                                     final String eventBeanPackage)
            throws IOException {
        final File basePackageDir = new DirectoryUtil().createFolder(ebPackageDir, schemaType.value());

        for (final MappedEvent mappedEvent : eventMap.values()) {
            final JavaClassGenerator generator = getJavaClassGenerator();
            generator.generateBeanJavaClass(basePackageDir, mappedEvent, eventBeanPackage);
        }
    }

    /**
     * @param ebPackageDir
     * @param schemaType
     * @param evebt
     * @param eventBeanPackage
     * @throws IOException
     */
    private void generateBaseClasses(final File ebPackageDir, final SchemaEnum schemaType, final Map<Event, MappedEvent> eventMap,
                                     final String eventBeanPackage)
            throws IOException {
        final File basePackageDir = new DirectoryUtil().createFolder(ebPackageDir, schemaType.value());

        for (final MappedEvent mappedEvent : eventMap.values()) {
            final JavaClassGenerator generator = getJavaClassGenerator();
            generator.generateBaseJavaClass(basePackageDir, mappedEvent, eventBeanPackage);
        }
    }

    /**
     * @param ebPackageDir
     */
    private void defineDirectoryForEventClassPackage(final File ebPackageDir) {
        try {
            new DirectoryUtil().createDirectoryIfNotExists(ebPackageDir);
        } catch (final IOException e) {
            LOG.warn("{} file already exists will be used / failed to create directory", ebPackageDir.getAbsoluteFile());
        }
    }

    /**
     * @param metadataDirName
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    protected void addDirectoryToClasspath(final String metadataDirName) {
        GenerateUtils.addDirectoryToClasspath(new File(metadataDirName));
    }

    /**
     * @param fileDirName
     * @param metadataDir
     * @param fileDir
     */
    protected boolean isEventBeanGenerationRequired(final File metadataDir, final File fileDir) {
        if (GenerateUtils.generateRequired(metadataDir, fileDir)) {
            LOG.info("generation required: files in {} newer than some files in {}", metadataDir.getAbsolutePath(), fileDir.getName());
            return true;
        }
        LOG.info("generation not required: all files in {} older than each file in {}", metadataDir.getAbsolutePath(), fileDir.getName());
        return false;

    }

    /**
     * @param args
     * @return
     */
    public void checkArguments(final String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("usage: GenerateEventBeans config_dir output_dir schema_xml_file schema_xsd_file");
        }

        LOG.debug("GenerateEventBeans arguments: {}, {}, {}, {}, {}", args[0], args[1], args[2], args[3]);
    }

    class PojoGenSchemaHandler extends FileBasedSchemaHandler {

        private final Map<Event, MappedEvent> eventMap = new TreeMap<Event, MappedEvent>();

        /**
         * @param neType
         * @throws ResourceNotFoundException
         * @throws SchemaException
         */
        public PojoGenSchemaHandler(final SchemaEnum neType) {
            super(neType);
        }

        @Override
        public SchemaHandler load() throws ResourceNotFoundException, SchemaException {
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

        /**
         * @return the eventMap
         */
        public Map<Event, MappedEvent> getEventMap() {
            return eventMap;
        }

        public Map<FfvFivKey, Schema> getSchemaMap() {
            return schemaMap;
        }
    }

}
