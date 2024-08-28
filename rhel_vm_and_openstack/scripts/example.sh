#
# 
#
. /tmp/installer/functions.sh

mkdir -p /aia
chown -R ${USER_NAME}:${GROUP_NAME} /aia

docker pull armdocker.rnd.ericsson.se/aia/bps-demo:1.0.0
docker pull armdocker.rnd.ericsson.se/aia/aia-development-work-flow-demo:0.11.0

echo "Copying all the examples to home directory..."
cp -R /tmp/installer/files/example ${HOME_DIRECTORY}/
chown -R ${USER_NAME}:${GROUP_NAME} ${HOME_DIRECTORY}/example

echo "Replacing the path in aia-support example..."
find ${HOME_DIRECTORY}/example/aia-support -type f -name "BatchFlow.xml" | xargs -I {} sed -i -e "s/\/home\/liam\/git/\/\${HOME_DIRECTORY}\/example/g" {}

echo "Installing the zeppelin notebook..."
find /tmp/installer/files/note/ -type f -name "*json" | xargs -I {} curl -X POST http://localhost:9090/api/notebook/import -T {}

echo "Getting the IPv4 addr..."
source /etc/network-environment

echo "Installing the jupyter notebook..."
sudo -u cloud-user cp -R /tmp/installer/files/jupyter_note/* /home/cloud-user/notebooks
