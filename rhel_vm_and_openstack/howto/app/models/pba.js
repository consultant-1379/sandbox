(function() {
    bps.service('pba', function() {
        this.get = function() {
            return {
                "pba": {
                    "name": "appname",
                    "version": "0.0.1",
                    "description": "AFAF",
                    "template": {
                        "name": "aia-batch",
                        "version": "1.0.4"
                    },
                    "extensionPoints": [
                        //{
                        //         "type": "application",
                        //         "applicationName": "asr",
                        //         "applicationVersion": "0.1.2",
                        //         "flow": "LTE BEARER SESSION",
                        //         "schema": "file",
                        //         "uri": "file:///tmp",
                        //         "attributes": {
                        //             "data.format": "json"
                        //         }

                        //     },
                        //     {
                        //         "type": "template",
                        //         "schema": "kafka",
                        //         "uri": "kafka:///topic_name?format=avro",
                        //         "attributes": {
                        //             "data.format": "json",
                        //             "group.id": "live.ericsson.se"
                        //         }

                        //     }
                    ]
                }
            };
            //end object

        };
    });
})();