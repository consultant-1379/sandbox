CREATE USER hiveuser WITH PASSWORD 'hiveuser';
CREATE DATABASE metastore;
\c metastore;
\i /usr/lib/hive/scripts/metastore/upgrade/postgres/hive-schema-1.1.0.postgres.sql
SET
