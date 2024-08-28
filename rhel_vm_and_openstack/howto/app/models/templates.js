(function () {
    bps.service('templates', function () {
    this.get = function () {
        return {
  "items": [
    {
      "name": "aia-bps-batch",
      "version": "0.1.97",
      "title": "Batch Processing (Spark based)",
      "description": "This template demonstrates how to process a batch of events coming on Kafka or any other File sources(SQL and NoSQL).",
      "downloads": [
        {
          "title": "0.1.97",
          "link": "/paas/v1/repositories/applications/aia-bps-batch/0.1.97/pba.zip"
        }
      ],
      "service": [
        {
          "name": "test-service",
          "container_port": 80,
          "service_port": 0
        }
      ],
      "extensions": [
        {
          "name": "asr-base-service-extension-point",
          "version": "1.0.0",
          "description": "ASR implementation Spark Service extension",
          "platform": [
            {
              "type": "marathon",
              "version": "latest"
            }
          ],
          "envars": [
            {
              "name": "mainClass",
              "value": "com.ericsson.component.aia.services.bps.engine.service.BPSPipeLineExecuter"
            },
            {
              "name": "masterUrl",
              "value": "spark://10.45.16.201:7077"
            },
            {
              "name": "applicationJar",
              "value": "/spark_batch/bps-engine-deployable.jar"
            },
            {
              "name": "appArguments",
              "value": ""
            }
          ],
          "structure": {
            "instance_configuration": {
              "cpu": "4",
              "memory": {
                "size": 4,
                "unit": "Gb"
              }
            }
          },
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
          "dependencies": [
            {
              "type": "parent",
              "reference": "urn:uuid:8cd66ae6-b1a3-4020-aca1-1b94a6ef8c6c"
            },
            {
              "type": "extend",
              "reference": "foundation",
              "qualifier": ""
            }
          ],
          "integration_points": [
            {
              "description": "Kafka messaging service",
              "name": "kafka-service",
              "version": "1.0.0"
            }
          ],
          "service": [
            {}
          ],
          "URN": "aia-bps-batch:0.1.97:asr-base-service-extension-point:1.0.0",
          "id": "aia-bps-batch:0.1.97:asr-base-service-extension-point:1.0.0"
        }
      ],
      "deployments": {
        "zookeeper": {
          "id": "/bps-deployable-aia-bps-batch",
          "uris": [
            "file:///root/docker.tar.gz"
          ],
          "instances": 1,
          "cpus": 1,
          "mem": 512,
          "container": {
            "type": "DOCKER",
            "volumes": [
              {
                "containerPath": "/usr/java",
                "hostPath": "/usr/java",
                "mode": "RW"
              },
              {
                "containerPath": "/usr/lib",
                "hostPath": "/usr/lib",
                "mode": "RW"
              },
              {
                "containerPath": "/etc",
                "hostPath": "/etc",
                "mode": "RW"
              },
              {
                "containerPath": "/home",
                "hostPath": "/home",
                "mode": "RW"
              }
            ],
            "docker": {
              "image": "armdocker.rnd.ericsson.se/aia/bps-aia-bps-batch:0.1.97",
              "network": "HOST",
              "forcePullImage": true,
              "privileged": true
            }
          }
        }
      },
      "URN": "aia-bps-batch:0.1.97",
      "id": "aia-bps-batch:0.1.97"
    },
    {
      "name": "aia-bps-mediation",
      "version": "0.1.4",
      "title": "Mediation Application",
      "description": "A template for creating the mediation applications",
      "downloads": [
        {
          "title": "0.1.4",
          "link": "/paas/v1/repositories/applications/aia-bps-mediation/0.1.4/pba.zip"
        }
      ],
      "service": [
        {
          "name": "test-service",
          "container_port": 80,
          "service_port": 0
        }
      ],
      "extensions": [],
      "URN": "aia-bps-mediation:0.1.4",
      "id": "aia-bps-mediation:0.1.4"
    },
    {
      "name": "aia-bps-streaming",
      "version": "0.1.42",
      "title": "Streaming Processing (Spark based)",
      "description": "This template demonstrates how to process a stream of events, using Apache Spark. The template covers different cases on how to integrate with inbound streams, for example Apache Kafka.",
      "downloads": [
        {
          "title": "0.1.42",
          "link": "/paas/v1/repositories/applications/aia-bps-streaming/0.1.42/pba.zip"
        }
      ],
      "service": [
        {
          "name": "test-service",
          "container_port": 80,
          "service_port": 0
        }
      ],
      "extensions": [],
      "deployments": {
        "zookeeper": {
          "id": "/bps-deployable-aia-bps-streaming",
          "uris": [
            "file:///root/docker.tar.gz"
          ],
          "instances": 1,
          "cpus": 1,
          "mem": 512,
          "container": {
            "type": "DOCKER",
            "docker": {
              "network": "HOST",
              "image": "armdocker.rnd.ericsson.se/aia/bps-aia-bps-streaming:0.1.42",
              "forcePullImage": true,
              "privileged": true,
              "parameters": [
                {
                  "key": "env",
                  "value": "mainClass=com.ericsson.component.aia.services.bps.engine.service.BPSPipeLineExecuter"
                },
                {
                  "key": "env",
                  "value": "deployMode=cluster"
                },
                {
                  "key": "env",
                  "value": "schemaRegistry=-DschemaRegistry.address=http://ieatrcxb3651.athtem.eei.ericsson.se:8081/"
                },
                {
                  "key": "env",
                  "value": "masterUrl=spark://10.0.2.15:7077"
                },
                {
                  "key": "env",
                  "value": "bpsstreamningJar=hdfs:////user/vagrant/aia-bps-streaming-all-in-one.jar"
                },
                {
                  "key": "env",
                  "value": "flowPath=hdfs:////user/vagrant/flow.xml"
                },
                {
                  "key": "env",
                  "value": "jobArguments="
                }
              ]
            },
            "volumes": [
              {
                "containerPath": "/usr/java",
                "hostPath": "/usr/java",
                "mode": "RW"
              },
              {
                "containerPath": "/usr/lib",
                "hostPath": "/usr/lib",
                "mode": "RW"
              },
              {
                "containerPath": "/etc",
                "hostPath": "/etc",
                "mode": "RW"
              },
              {
                "containerPath": "/home",
                "hostPath": "/home",
                "mode": "RW"
              }
            ]
          }
        }
      },
      "URN": "aia-bps-streaming:0.1.42",
      "id": "aia-bps-streaming:0.1.42"
    }
  ]
};
//end object

    };
});
})();