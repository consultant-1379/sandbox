# **********************************************************************
# Ericsson Radio Systems AB                                     SCRIPT
# **********************************************************************
#
# (c) Ericsson Radio Systems AB 2016 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property of
# Ericsson Radio Systems AB, Sweden. The programs may be used and/or
# copied only with the written permission from Ericsson Radio Systems AB
# or in accordance with the terms and conditions stipulated in the
# agreement/contract under which the program(s) have been supplied.
#
# **********************************************************************
#   Command Section
#
# **********************************************************************
#
#   Main body of program
#
# ********************************************************************
#
#Pull Spark Base image from Docker Repository
FROM armdocker.rnd.ericsson.se/aia/base/spark_base_1.6.0

#Created Spark Batch folder so that all Spark Batch application dependencies goes into this
CMD mkdir -p  /spark-streaming

#overwrite JavaHome
ENV JAVA_HOME /usr/java/latest

#Copy the default_cmd,flow.xml,config.properties and spark-streaming-asr-rab-all-in-one.jar in docker
ADD /src/main/resources/default_cmd /spark-streaming/
ADD /src/main/resources/flow.xml /spark-streaming/
ADD /src/main/resources/config.properties /spark-streaming/
ADD /src/main/resources/META-INF   /spark-streaming/
ADD target/spark-streaming-example-app-all-in-one.jar /spark-streaming/

#Run this script when docker images gets initialized
ENTRYPOINT ["sh","/spark-streaming/default_cmd"]