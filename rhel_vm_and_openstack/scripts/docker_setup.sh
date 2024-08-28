#
# This script configures docker & downloads base docker images.
#

. /tmp/installer/functions.sh

df -h

cecho "\n*** Setting up AIA docker containers ***\n" $GREEN
cd ${HOME_DIRECTORY}
tar cfzT docker.tar.gz /dev/null

cp docker.tar.gz /root/docker.tar.gz
chown ${USER_NAME}:${GROUP_NAME} docker.tar.gz

docker pull armdocker.rnd.ericsson.se/aia/aia-sdk-paas-manager

systemctl enable paas-manager

#if [[ ! "${PACKER_BUILDER_TYPE}" == "openstack" ]];then
#echo "Getting the host docker interface address..."
#docker_address=`ifconfig enp0s3 | grep "inet" |grep -v inet6 | awk -F' ' '{print $2}'`

# Run it once.
#docker run -d -p 8888:8888 --restart=always --name aia-ui -v /data:/data armdocker.rnd.ericsson.se/aia/aia-ui:1.0.8 --web.cdsv3.url=http://localhost:8889 --app.base.url=http://localhost:8888 --paas.store.maven.dir=/data/paas/maven-store --paas.application.generator.dir=/data/paas/generated-applications --marathon.url=http://${docker_address}:8080/v2
#fi

echo "Installing local docker registry service..."
docker pull registry:2
systemctl enable docker-registry

echo "Pulling some of the images..."
docker pull armdocker.rnd.ericsson.se/aia/base/flink-base/filnk:1.1.3-hadoop26-scala2.11
docker pull armdocker.rnd.ericsson.se/aia/base/spark_base_1.6.0
