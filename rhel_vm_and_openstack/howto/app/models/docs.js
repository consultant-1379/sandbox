(function() {
    bps.service('docsModel', function() {
        this.get = function() {
            return [{
                    "name": "BPS",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "bps.png",
                    "color": "#0093be",
                    "docs": "#/Documentation/BPS documentation",
                    "api": "#/Documentation/BPS documentation/api"
                },
                {
                    "name": "EPS",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "eps_logo.png",
                    "color": "#abd53b",
                    "docs": "http://analytics.ericsson.se/assets/eps/index.html",
                    "api": "http://analytics.ericsson.se/assets/eps/apidocs/index.html"
                },
                {
                    "name": "Kafka",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "kafka.png",
                    "color": "#0093be",
                    "docs": "http://kafka.apache.org/documentation.html",
                },
                {
                    "name": "Alluxio",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "alluxio.png",
                    "color": "#0093be",
                    "docs": "http://www.alluxio.org/documentation/en/File-System-API.html",
                },
                {
                    "name": "Mapr",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "mapr.png",
                    "color": "#0093be",
                    "docs": "http://maprdocs.mapr.com/51/#MapROverview/c_overview_intro.html",
                },
                {
                    "name": "HBASE",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "hbase.png",
                    "color": "#0093be",
                    "docs": "http://doc.mapr.com/display/MapR/HBase",
                },
                {
                    "name": "Redis",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "redis.png",
                    "color": "#d92b21",
                    "docs": "http://redis.io/documentation",
                },
                {
                    "name": "Spark",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "spark.png",
                    "color": "#ccc",
                    "docs": "http://spark.apache.org/docs/latest/",
                },
                {
                    "name": "Hive",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "hive.png",
                    "color": "#0093be",
                    "docs": "https://cwiki.apache.org/confluence/display/Hive/LanguageManual",
                    "api": "https://hive.apache.org/javadoc.html"
                },
                {
                    "name": "Avro",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "avro.png",
                    "color": "#0093be",
                    "docs": "https://avro.apache.org/docs/1.8.1/",
                    "api": "https://avro.apache.org/docs/1.8.1/api/java/index.html"
                },
                {
                    "name": "Flink",
                    "summary": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus harum...",
                    "image": "flink.png",
                    "color": "#0093be",
                    "docs": "https://ci.apache.org/projects/flink/flink-docs-release-1.2/",
                },
                {
                    "name": "Parquet",
                    "summary": "",
                    "image": "parquet.png",
                    "color": "#0093be",
                    "docs": "https://parquet.apache.org/documentation/latest/",
                }
            ];
        };
    });
})();