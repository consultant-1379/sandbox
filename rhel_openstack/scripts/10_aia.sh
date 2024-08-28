. /tmp/helper_function.sh

wget --no-proxy https://arm101-eiffel004.lmera.ericsson.se:8443/nexus/service/local/repositories/aia-releases/content/com/ericsson/aia/model/avro/1.0.9/avro-1.0.9.jar

jar xvf avro-1.0.9.jar

mkdir -p /schema/
cp -R avro /schema/

