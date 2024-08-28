(function () {
    bps.service('schema', function () {
    this.get = function () {
        return {
  "schema": {
    "name": "spark-batch",
    "inputs": [
      {
        "name": "hive",
        "properties": {
          "uri": "string"
        }
      },
      {
        "name": "hdfs",
        "properties": {
          "uri": "string",
          "header": "boolean",
          "inferSchema": "boolean",
          "drop-malformed": "boolean",
          "data-format": [
            "csv",
            "json"
          ],
          "skip-comments": "boolean",
          "quote": "string",
          "table-name": "string"
        }
      },
      {
        "name": "jdbc",
        "properties": {
          "uri": "string",
          "driver": "string",
          "user": "string",
          "password": "string",
          "table-name": "string"
        }
      },
      {
        "name": "kafka",
        "properties": {
          "uri": {"type": "string","default": "kafka//loremipsum"},
          "metadata.broker.list": {"type": "string","default": ""},
          "group.id": {"type": "string","default": ""},
          "kafka.keyClass": {"type": "string","default": ""},
          "kafka.valueClass": {"type": "string","default": "dsds"},
          "kafka.keyDecoder.class": {"type": "string","default": ""},
          "kafka.valueDecoder.class": {"type": "string","default": ""},
          "slide.window.length":{"type": "string","default": ""},
          "window.length": {"type": "boolean","default": ["Yes","No"]},
          "fildArrayExample": {"type": "array","default": ["Red","Yellow","blue"]}
        }
      }
    ],
    "outputs": [
      {
        "name": "hive",
        "properties": {
          "uri": "string",
          "data.format": "string",
          "partition.columns": "string"
        }
      },
      {
        "name": "hdfs",
        "properties": {
          "uri": "string",
          "data.format": "string"
        }
      },
      {
        "name": "jdbc",
        "properties": {
          "uri": "string",
          "driver": "string",
          "user": "string",
          "password": "string",
          "table-name": "string"
        }
      },
      {
        "name": "kafka",
        "properties": {
          "uri": "string",
          "metadata.broker.list": "string",
          "group.id": "string",
          "eventType": "string",
          "format": "string"
        }
      }
    ]
  }
} ;
//end object

    };
});
})();