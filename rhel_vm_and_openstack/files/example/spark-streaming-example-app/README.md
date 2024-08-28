# Introduction
spark-streaming-example-app this is base streaming template project generated from the AIA Portal.

# Prerequisites:

- Maven 3.0.0+
- Java 1.8
- Docker
- Vagrant

# Installation:

If using Vagrant as standalone instead of Docker set the below parameter
```
export schemaRegistry.address=http://IP:8081
```
## Authenticating with Private Registries(Maven setting for Docker):

To push to a private Docker image registry that requires authentication, you can put your
credentials in your Maven's global `settings.xml` as below.
```
<server>
	<id>armdocker.rnd.ericsson.se</id>
	<username>id</username>
	<password>Password</password>
	<configuration>
		<email>email@ericsson.com</email>
	</configuration>
</server>
```
#### Docker Setting parameters for Window:
* export DOCKER_CERT_PATH='PATH'
* export DOCKER_HOST=tcp://IP:2376
* export DOCKER_MACHINE_NAME=default
* export DOCKER_TLS_VERIFY=1
* export DOCKER_TOOLBOX_INSTALL_PATH='PATH'


## Using Docker config file for authentication

Another option to authenticate with private repositories is using dockers ~/.docker/config.json.
This makes it also possible to use in cooperation with cloud providers like AWS or Google Cloud which store the user's
credentials in this file, too. **<useConfigFile>true</useConfigFile>**

    <plugin>
      <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>VERSION GOES HERE</version>
        <configuration>
          [...]
          <useConfigFile>true</useConfigFile>
        </configuration>
      </plugin>
    </plugins>

**Hint:** The build will fail, if the config file doesn't exist.

## Quick Start:

List of Extension points -- input adapters
| EP Adapters  	|   Support	|   Usuage		|
|---			|---		|---			|
|  Active MQ 	|   NA		|  	amq 		|
|  DRILL 		|   NA		|   drill		|
|  FILE System 	|   Beta	|   file		|
|   HIVE		|   NA		|   hive		|
|   Spark Sql	|   NA		|   spark-sql	|
|   Spark Batch	|   NA		|   spark-batch	|
|   Zero MQ		|   NA		|   zmq			|
|   HDFS		|   Beta	|   hdfs		|
|   KAFKA		|   Beta	|   kafka		|
|Spark Streaming|   Beta	|   spark-streaming	|
|   Alluxio		|   Beta	|   alluxio		|


List of Extension points -- output adapters
| EP Adapters  	|   Support	|   Usuage		|
|---			|---		|---			|
|  Active MQ 	|   NA		|  	amq 		|
|  DRILL 		|   NA		|   drill		|
|  FILE System 	|   Beta	|   file		|
|   HIVE		|   Beta	|   hive		|
|   Spark Sql	|   NA		|   spark-sql	|
|   Spark Batch	|   NA		|   spark-batch	|
|   Zero MQ		|   NA		|   zmq			|
|   HDFS		|   Beta	|   hdfs		|
|   KAFKA		|   NA		|   kafka		|
|Spark Streaming|   Beta	|   spark-streaming	|
|   Alluxio		|   Beta	|   alluxio		|

### Flow.xml

Kafka Input Attributes Usage:
:   **radio-stream** stream to be accessed in code
:   **kafka://** repressnts Kafka Input Adapter
:   **radio_up** represents Kafka Topic name
:   **win.length** represents window interval
:   **slide.win.length** represents sliding window interval
:   **group.id** represents Kafka Topic group id

As mentioned below:
```
<input name="radio-stream">
	<attribute name="uri" value="kafka://radio_up"/>
	<attribute name ="metadata.broker.list" value="localhost:9092(kakfa server url)"/>
	<attribute name="win.length" value="300000"/>
	<attribute name="slide.win.length" value="300000"/>	
	<attribute name="group.id" value="radio_session"/>
</input>
```

Spark Streaming Attributes Usage:
:   **ups-spark-streaming** represents Spark streaming
:   **master.url** represents Spark master
:   **driver-class** represents Spark driver class

Optional Attributes Usage:
:   **spark.externalBlockStore.url** represents Spark external blockstore url
:   **spark.externalBlockStore.baseDir** represents Spark external blockstore uri
:   **streaming.checkpoint** represents Spark streaming checkpoint url

As mentioned below:
```
<step name="ups-spark-streaming">
	<attribute name="uri" value="spark-streaming://SparkStreamingExampleApp"/>
	<attribute name="master.url" value="spark://localhost:7077"/>
	<attribute name="spark.externalBlockStore.url" value="alluxio://localhost:19998"/>
	<attribute name="spark.externalBlockStore.baseDir" value="alluxio://localhost:19998/staging/ups/"/>
	<attribute name="streaming.checkpoint" value="alluxio://localhost:19998/ups-checkpoint/kafka"/>
	<attribute name="driver-class" value="com.ericsson.aia.bps.streaming.correlations.SparkStreamingExampleApp" />
</step>
```

Alluxio Output Attributes Usage:
:   **alluxio-store** represents Alluxio output adapter
:   **RADIO_USER_SESSION** represents Alluxio output directory
:   **master-url** represents Alluxio server url

As mentioned below:

```
<output name="alluxio-store">
	<attribute name="uri" value="alluxio://RADIO_USER_SESSION"/>
	<attribute name="master-url" value="localhost:19998"/>        
</output>
```

### This section(<Path>) signifies flow:

#### Default flow is like this:
1. Kafka producers will be publishing messages to Kafka server(Default:radio_up )
2. Spark streaming application will be streaming Kafka topic(Default:radio_up )
3. Spark Streaming application writes outputs to Alluxio server periodically for every 30 minutes(Default: ``<attribute name="win.length" value="300000"/>``) 

```
<path>
	<from uri="radio-stream" />
	<to uri="spark-streaming" />
	<to uri="alluxio-store" />
</path>
```
### Build the application

You can build an image with the above configurations by running this command.
Run this Docker Pull before running maven build

# Start the Docker Quickstart Terminal
Start the Docker Quickstart Terminal that you just installed.
Other shell or dos based terminals can be used but they require extra environment variables to be setup.

## Prepare input data
When deployed, the application starts automatically, so the inputs (data and flow config file) first need to be in place

### Flow.xml
Copy to the VM under /home/vagrant
The password for the vagrant user is `vagrant`
```
scp -P 2222 ./config/flow.xml vagrant@localhost:/home/vagrant
```

### Test data
The flow.xml file uploaded in the previous step points to the input data.

Let's say it expects data in "hdfs:///tmp/SalesJan2009.csv", then the steps to upload the data would be
```
scp -P 2222 config/SalesJan2009.csv vagrant@localhost:/home/vagrant
ssh -p 2222 vagrant@localhost
hadoop fs -copyFromLocal SalesJan2009.csv /tmp
exit
```

Go to the sample project folder
```
cd <Sample-project-location>/

    mvn clean install
```

# Run docker pull to get the base image
```
docker pull armdocker.rnd.ericsson.se/aia/base/spark_base_1.6.0
```


## Containerize the application
```
docker login -u <signum> http://armdocker.rnd.ericsson.se
docker build -t armdocker.rnd.ericsson.se/aia/bps-spark-streaming-example-app:1.0.1 .
```
## To run Docker image with schema registry (ctrl+c to stop)
```
    docker run -it --env mainClass=com.ericsson.component.aia.services.bps.engine.service.BPSPipeLineExecuter --env deployMode=cluster --env schemaRegistry=-DschemaRegistry.address=http://IP:8081/ --env masterUrl=spark://ip:7077  --env bpsstreamningJar=hdfs:///tmp/spark-streaming-example-app-all-in-one.jar  --env flowPath=hdfs:///tmp/flow.xml --env jobArguments="" armdocker.rnd.ericsson.se/aia/aia-spark-streaming-example-app
```
### To run Docker image without schema registry (ctrl+c to stop)
```
    docker run -it --env mainClass=com.ericsson.component.aia.services.bps.engine.service.BPSPipeLineExecuter --env deployMode=cluster --env masterUrl=spark://ip:7077  --env bpsstreamningJar=hdfs:///tmp/spark-streaming-example-app-all-in-one.jar  --env flowPath=hdfs:///tmp/flow.xml --env jobArguments="" armdocker.rnd.ericsson.se/aia/aia-spark-streaming-example-app
```

## Submit to AIA Paas Manager To deploy

### Pepare input data
When deployed, the application starts automatically, so the inputs (data and flow config file) first need to be in place.
Also make sure the url for the spark master and schema registry are correct, Right now its hard coded so please change according to your environment
### Copy input data into hdfs from virtual Machine

```
   hadoop fs -copyFromLocal <from localtion to flow.xml> <To location on hdfs>
   hadoop fs -copyFromLocal <from location of spark-streaming-example-app-all-in-one.jar> <To location on hdfs>
```
## To Deploy
```
<project-location>/bin/deploy_app.sh http://localhost:8888
```
## To undeploy
```
<project-location>/bin/undeploy_app.sh http://localhost:8888
```


### To Publish to Docker repository
Simple push
```
docker push armdocker.rnd.ericsson.se/aia/bps-spark-streaming-example-app:1.0.1
```

### Publish back to app manager
* After publish, you may find your application listed at:
* http://atrcxb2994.athtem.eei.ericsson.se//#aia/ecosystem

```bash
./bin/publish_app.sh
```


### Unpublish from app manager
```bash
./bin/unpublish_app.sh
```


# Wait for results
Check the deployment status on the Paas Manager web interface. It should be in status "Running":
http://localhost:8080
Give it a bit of time to crunch the data, then check the output results.
If the output is in hdfs format, you can browse the file system using the hadoop web interface:
http://localhost:50070/explorer.html#


## Additional Information:

BPS service consists of three module:
* Core
* Adapters
* Engine

Engine uses--> Adapters uses--> Core