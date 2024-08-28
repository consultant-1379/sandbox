(function () {
    bps.service('applications', function () {
    this.get = function () {
        return {
  "items": [
    {
      "name": "aia-development-work-flow-demo",
      "title": "App - Development Work Flow Demo",
      "description": "Hello world app to run on AIA platform",
      "releases": [
        {
          "version": "1.0.1",
          "status": "0",
          "maturity": 0,
          "downloads": [
            {
              "link": "/paas/v1/repositories/applications/aia-development-work-flow-demo/1.11.1/pba.zip"
            }
          ],
          "extensions": {
            "asr-base-service-extension-point": {
              "name": "asr-base-service-extension-point",
              "version": "1.0.0",
              "description": "ASR implementation Spark Service extension",
              "platform": [],
              "envars": [],
              "structure": {},
              "pba_policies": [],
              "pba_lifecycle": {
                "min_instances": 1,
                "max_instances": 4,
                "scale_triggers": [
                  {
                    "kpi": "response_time",
                    "value": 60
                  },
                  {
                    "kpi": "cpu",
                    "value": "90"
                  }
                ]
              },
              "service": []
            }
          },
          "integration_points": [
            {
              "name": "lte-call-drop",
              "title": "LTE CALL DROP",
              "description": "LTE Call session drop",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-call-drop-session?avo&"
                },
                {
                  "title": "hive",
                  "link": "hive://lte-call-drop-session?avo&"
                },
                {
                  "title": "jdbc",
                  "link": "jdbc://lte-call-drop-session?avo&"
                },
                {
                  "title": "hdfs",
                  "link": "hdfs://lte-call-drop-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://CONTEXT_RELEASE",
                  "link": "/download/avro_schemas/correlation.v1.v1/CONTEXT_RELEASE.avsc"
                }
              ]
            },
            {
              "name": "lte-initial-attach",
              "title": "LTE INITIAL ATTACH",
              "description": "UE initial registration with network",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://INITIAL_ATTACH",
                  "link": "/download/avro_schemas/correlation.v1.v1/INITIAL_ATTACH.avsc"
                }
              ]
            },
            {
              "name": "lte-bearer-session",
              "title": "LTE BEARER SESSION",
              "description": "LTE default and dedicated brearer session",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://LTE_ERAB_SESSION",
                  "link": "/download/avro_schemas/correlation.v1.v1/LTE_ERAB_SESSION.avsc"
                }
              ]
            },
            {
              "name": "lte-radio-user-session",
              "title": "LTE RADIO USER SESSION",
              "description": "LTE UE session performance information",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                [
                  {
                    "title": "tachyon://LTE_USER_SESSION",
                    "link": "/download/avro_schemas/correlation.v1.v1/LTE_USER_SESSION.avsc"
                  }
                ]
              ]
            },
            {
              "name": "lte-paging",
              "version": "0.1.0",
              "title": "LTE PAGING",
              "description": "LTE Paging Procedure tracking",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://NET_TG_SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/NET_TG_SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-service-request",
              "version": "0.1.0",
              "title": "LTE SERVICE REQUEST",
              "description": "LTE UE Service Request Procedure correlation",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-s1-handover",
              "version": "0.1.0",
              "title": "LTE S1 HANDOVER",
              "description": "LTE S1 Handover Procedure Correlation",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://S1_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/S1_HANDOVER.avsc"
                }
              ]
            },
            {
              "name": "lte-x2-handover",
              "version": "0.1.0",
              "title": "LTE X2 HANDOVER",
              "description": "LTE X2/INTRA-FREQ Handover Procedure Correlation",
              "inputs": [
                {
                  "title": "kafka",
                  "link": "kafka://lte-radio-user-session?avo&"
                }
              ],
              "availableInAndSchema": [
                {
                  "title": "tachyon://X2_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/X2_HANDOVER.avsc"
                }
              ]
            }
          ],
          "dependencies": {
            "zookeeper": {
              "name": "platform-service-zookeeper",
              "version": "latest"
            },
            "kafka": {
              "name": "platform-service-kafka",
              "version": "latest"
            }
          },
          "deployments": {
            "asr": {
              "id": "/aia-asr",
              "uris": [
                "file:///root/docker.tar.gz"
              ],
              "instances": 1,
              "cpus": 1,
              "mem": 512,
              "env": {
                "SPRING_PROFILES_ACTIVE": "eventgenerator",
                "ZOOKEEPER_ENDPOINT": "${pba.dependencies.zookeeper.apps.zookeeper.endpoints[0]}",
                "KAFKA_ENDPOINT": "${pba.dependencies.kafka.apps.kafka.endpoints[0]}"
              },
              "container": {
                "type": "DOCKER",
                "docker": {
                  "image": "armdocker.rnd.ericsson.se/aia/aia-kafka-sample-app:0.0.1",
                  "network": "BRIDGE",
                  "forcePullImage": true,
                  "privileged": true
                }
              }
            }
          }
        },
        {
          "version": "1.1.1",
          "status": 1,
          "maturity": 1,
          "downloads": [
            {
              "link": "/paas/v1/repositories/applications/aia-development-work-flow-demo/1.11.1/pba.zip"
            }
          ],
          "extensions": {
            "asr-base-service-extension-point": {
              "name": "asr-base-service-extension-point",
              "version": "1.0.0",
              "description": "ASR implementation Spark Service extension",
              "platform": [],
              "envars": [],
              "structure": {},
              "pba_policies": [],
              "pba_lifecycle": {
                "min_instances": 1,
                "max_instances": 4,
                "scale_triggers": [
                  {
                    "kpi": "response_time",
                    "value": 60
                  },
                  {
                    "kpi": "cpu",
                    "value": "90"
                  }
                ]
              },
              "service": []
            }
          },
          "dependencies": {
            "zookeeper": {
              "name": "platform-service-zookeeper",
              "version": "latest"
            },
            "kafka": {
              "name": "platform-service-kafka",
              "version": "latest"
            }
          },
          "deployments": {
            "asr": {
              "id": "/aia-asr",
              "uris": [
                "file:///root/docker.tar.gz"
              ],
              "instances": 1,
              "cpus": 1,
              "mem": 512,
              "env": {
                "SPRING_PROFILES_ACTIVE": "eventgenerator",
                "ZOOKEEPER_ENDPOINT": "${pba.dependencies.zookeeper.apps.zookeeper.endpoints[0]}",
                "KAFKA_ENDPOINT": "${pba.dependencies.kafka.apps.kafka.endpoints[0]}"
              },
              "container": {
                "type": "DOCKER",
                "docker": {
                  "image": "armdocker.rnd.ericsson.se/aia/aia-kafka-sample-app:0.0.1",
                  "network": "BRIDGE",
                  "forcePullImage": true,
                  "privileged": true
                }
              }
            }
          }
        },
        {
          "version": "1.11.1",
          "status": 2,
          "maturity": 2,
          "downloads": [
            {
              "link": "/paas/v1/repositories/applications/aia-development-work-flow-demo/1.11.1/pba.zip"
            }
          ],
          "extensions": {
            "asr-base-service-extension-point": {
              "name": "asr-base-service-extension-point",
              "version": "1.0.0",
              "description": "ASR implementation Spark Service extension",
              "platform": [],
              "envars": [],
              "structure": {},
              "pba_policies": [],
              "pba_lifecycle": {
                "min_instances": 1,
                "max_instances": 4,
                "scale_triggers": [
                  {
                    "kpi": "response_time",
                    "value": 60
                  },
                  {
                    "kpi": "cpu",
                    "value": "90"
                  }
                ]
              },
              "service": []
            }
          },
          "integration_points": [
            {
              "name": "lte-call-drop",
              "version": "0.1.0",
              "title": "LTE CALL DROP",
              "description": "LTE Call session drop",
              "availableInAndSchema": [
                {
                  "title": "tachyon://CONTEXT_RELEASE",
                  "link": "/download/avro_schemas/correlation.v1.v1/CONTEXT_RELEASE.avsc"
                }
              ]
            },
            {
              "name": "lte-initial-attach",
              "version": "0.1.0",
              "title": "LTE INITIAL ATTACH",
              "description": "UE initial registration with network",
              "availableInAndSchema": [
                {
                  "title": "tachyon://INITIAL_ATTACH",
                  "link": "/download/avro_schemas/correlation.v1.v1/INITIAL_ATTACH.avsc"
                }
              ]
            },
            {
              "name": "lte-bearer-session",
              "version": "0.1.0",
              "title": "LTE BEARER SESSION",
              "description": "LTE default and dedicated brearer session",
              "availableInAndSchema": [
                {
                  "title": "tachyon://LTE_ERAB_SESSION",
                  "link": "/download/avro_schemas/correlation.v1.v1/LTE_ERAB_SESSION.avsc"
                }
              ]
            },
            {
              "name": "lte-radio-user-session",
              "title": "LTE RADIO USER SESSION",
              "description": "LTE UE session performance information",
              "availableInAndSchema": [
                {
                  "title": "tachyon://LTE_USER_SESSION",
                  "link": "/download/avro_schemas/correlation.v1.v1/LTE_USER_SESSION.avsc"
                }
              ]
            },
            {
              "name": "lte-paging",
              "version": "0.1.0",
              "title": "LTE PAGING",
              "description": "LTE Paging Procedure tracking",
              "availableInAndSchema": [
                {
                  "title": "tachyon://NET_TG_SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/NET_TG_SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-service-request",
              "version": "0.1.0",
              "title": "LTE SERVICE REQUEST",
              "description": "LTE UE Service Request Procedure correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-s1-handover",
              "version": "0.1.0",
              "title": "LTE S1 HANDOVER",
              "description": "LTE S1 Handover Procedure Correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://S1_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/S1_HANDOVER.avsc"
                }
              ]
            },
            {
              "name": "lte-x2-handover",
              "version": "0.1.0",
              "title": "LTE X2 HANDOVER",
              "description": "LTE X2/INTRA-FREQ Handover Procedure Correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://X2_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/X2_HANDOVER.avsc"
                }
              ]
            }
          ],
          "dependencies": {
            "zookeeper": {
              "name": "platform-service-zookeeper",
              "version": "latest"
            },
            "kafka": {
              "name": "platform-service-kafka",
              "version": "latest"
            }
          },
          "deployments": {
            "asr": {
              "id": "/aia-asr",
              "uris": [
                "file:///root/docker.tar.gz"
              ],
              "instances": 1,
              "cpus": 1,
              "mem": 512,
              "env": {
                "SPRING_PROFILES_ACTIVE": "eventgenerator",
                "ZOOKEEPER_ENDPOINT": "${pba.dependencies.zookeeper.apps.zookeeper.endpoints[0]}",
                "KAFKA_ENDPOINT": "${pba.dependencies.kafka.apps.kafka.endpoints[0]}"
              },
              "container": {
                "type": "DOCKER",
                "docker": {
                  "image": "armdocker.rnd.ericsson.se/aia/aia-kafka-sample-app:0.0.1",
                  "network": "BRIDGE",
                  "forcePullImage": true,
                  "privileged": true
                }
              }
            }
          }
        }
      ]
    },
    {
      "name": "asr",
      "title": "Network Analytics Session Record (ASR)",
      "description": "Status of Radio/Core correlations",
      "releases": [
        {
          "version": "0.1.0",
          "downloads": [
            {
              "link": "/portal/resources/portal/widgets/wow-banner/asr.zip"
            }
          ],
          "extensions": {},
          "integration_points": [
            {
              "name": "lte-call-drop",
              "title": "LTE CALL DROP",
              "description": "LTE Call session drop",
              "availableInAndSchema": [
                {
                  "title": "tachyon://CONTEXT_RELEASE",
                  "link": "/download/avro_schemas/correlation.v1.v1/CONTEXT_RELEASE.avsc"
                }
              ]
            },
            {
              "name": "lte-initial-attach",
              "title": "LTE INITIAL ATTACH",
              "description": "UE initial registration with network",
              "availableInAndSchema": [
                {
                  "title": "tachyon://INITIAL_ATTACH",
                  "link": "/download/avro_schemas/correlation.v1.v1/INITIAL_ATTACH.avsc"
                }
              ]
            },
            {
              "name": "lte-bearer-session",
              "title": "LTE BEARER SESSION",
              "description": "LTE default and dedicated brearer session",
              "availableInAndSchema": [
                {
                  "title": "tachyon://LTE_ERAB_SESSION",
                  "link": "/download/avro_schemas/correlation.v1.v1/LTE_ERAB_SESSION.avsc"
                }
              ]
            },
            {
              "name": "lte-radio-user-session",
              "title": "LTE RADIO USER SESSION",
              "description": "LTE UE session performance information",
              "availableInAndSchema": [
                {
                  "title": "tachyon://LTE_USER_SESSION",
                  "link": "/download/avro_schemas/correlation.v1.v1/LTE_USER_SESSION.avsc"
                }
              ]
            },
            {
              "name": "lte-paging",
              "title": "LTE PAGING",
              "description": "LTE Paging Procedure tracking",
              "availableInAndSchema": [
                {
                  "title": "tachyon://NET_TG_SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/NET_TG_SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-service-request",
              "title": "LTE SERVICE REQUEST",
              "description": "LTE UE Service Request Procedure correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://SERVICE_REQUEST_CORR",
                  "link": "/download/avro_schemas/correlation.v1.v1/SERVICE_REQUEST_CORR.avsc"
                }
              ]
            },
            {
              "name": "lte-s1-handover",
              "title": "LTE S1 HANDOVER",
              "description": "LTE S1 Handover Procedure Correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://S1_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/S1_HANDOVER.avsc"
                }
              ]
            },
            {
              "name": "lte-x2-handover",
              "title": "LTE X2 HANDOVER",
              "description": "LTE X2/INTRA-FREQ Handover Procedure Correlation",
              "availableInAndSchema": [
                {
                  "title": "tachyon://X2_HANDOVER",
                  "link": "/download/avro_schemas/correlation.v1.v1/X2_HANDOVER.avsc"
                }
              ]
            }
          ],
          "dependencies": {
            "zookeeper": {
              "name": "platform-service-zookeeper",
              "version": "latest"
            },
            "kafka": {
              "name": "platform-service-kafka",
              "version": "latest"
            }
          },
          "deployments": {
            "asr": {
              "id": "/aia-asr",
              "uris": [
                "file:///root/docker.tar.gz"
              ],
              "instances": 1,
              "cpus": 1,
              "mem": 512,
              "env": {
                "SPRING_PROFILES_ACTIVE": "eventgenerator",
                "ZOOKEEPER_ENDPOINT": "${pba.dependencies.zookeeper.apps.zookeeper.endpoints[0]}",
                "KAFKA_ENDPOINT": "${pba.dependencies.kafka.apps.kafka.endpoints[0]}"
              },
              "container": {
                "type": "DOCKER",
                "docker": {
                  "image": "armdocker.rnd.ericsson.se/aia/aia-kafka-sample-app:0.0.1",
                  "network": "BRIDGE",
                  "forcePullImage": true,
                  "privileged": true
                }
              }
            }
          }
        }
      ]
    }
  ]
};
//end object

    };
});
})();