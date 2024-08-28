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
package com.ericsson.component.aia.model.base.util;

import java.sql.Types;
import java.util.*;

/**
 * This class handles conversion from String to SQL Type
 * 
 * 
 */
public class TypeConversionStringToSql {
	private static final Map<String, Integer> MAPSTRINGTOSQL;
	static {
        final Map<String, Integer> typeMap = new HashMap<String, Integer>();
        typeMap.put("ARRAY", Types.ARRAY);
		typeMap.put("BIGINT", Types.BIGINT);
		typeMap.put("BINARY", Types.BINARY);
		typeMap.put("BYTEA", Types.BINARY);
		typeMap.put("BIT", Types.BIT);
		typeMap.put("BOOLEAN", Types.BOOLEAN);
		typeMap.put("CHAR", Types.CHAR);
		typeMap.put("CLOB", Types.CLOB);
		typeMap.put("TEXT", Types.CLOB);
		typeMap.put("BLOB", Types.BLOB);
		typeMap.put("DATALINK", Types.DATALINK);
		typeMap.put("DATE", Types.DATE);
		typeMap.put("DECIMAL", Types.DECIMAL);
		typeMap.put("DISTINCT", Types.DISTINCT);
		typeMap.put("DOUBLE", Types.DOUBLE);
		typeMap.put("FLOAT", Types.FLOAT);
		typeMap.put("INTEGER", Types.BIGINT);
		typeMap.put("JAVA_OBJECT", Types.JAVA_OBJECT);
		typeMap.put("LONGNVARCHAR", Types.LONGNVARCHAR);
		typeMap.put("OTHER", Types.OTHER);
		typeMap.put("LONGVARBINARY", Types.LONGVARBINARY);
		typeMap.put("LONGVARCHAR", Types.LONGVARCHAR);
		typeMap.put("NCHAR", Types.NCHAR);
		typeMap.put("NCLOB", Types.NCLOB);
		typeMap.put("NULL", Types.NULL);
		typeMap.put("NUMERIC", Types.NUMERIC);
		typeMap.put("NVARCHAR", Types.NVARCHAR);
		typeMap.put("REAL", Types.REAL);
		typeMap.put("REF", Types.REF);
		typeMap.put("ROWID", Types.ROWID);
		typeMap.put("SMALLINT", Types.INTEGER);
		typeMap.put("SQLXML", Types.SQLXML);
		typeMap.put("STRUCT", Types.STRUCT);
		typeMap.put("TIME", Types.TIME);
		typeMap.put("TIMESTAMP", Types.TIMESTAMP);
		typeMap.put("TINYINT", Types.SMALLINT);
		typeMap.put("VARBINARY", Types.VARBINARY);
		typeMap.put("VARCHAR", Types.ARRAY);
		MAPSTRINGTOSQL = Collections.unmodifiableMap(typeMap);
    }
	
	/**
     * Convert a standard SQL type string to a SQL type
     * 
     * @param sqlTypeString
     */
	public static int typeStringToSql(final String sqlTypeString) {
		final Integer returnType;
		returnType = MAPSTRINGTOSQL.get(sqlTypeString);
		if (returnType == null) {
			return Types.OTHER;
		}
		else {
			return returnType;
		}
	}
}