if [[ ! -f jdk-8u66-linux-x64.rpm ]];then
    wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-linux-x64.rpm"
fi

getFile(){
    download_link=$1
    extra_param=$2
    echo "Downloading file ${download_link}"
    command="wget ${extra_param} ${download_link}"
    file_name=${download_link##*/}
    echo "Checking file name ${file_name}"
    if [[ -f ${file_name} ]];then
      echo "${file_name} already exist, skip it"
    else
      echo `${command}`
    fi
    

}

getFile http://alluxio.org/downloads/files/1.0.1/alluxio-1.0.1-bin.tar.gz
getFile http://alluxio.org/downloads/files/1.0.1/alluxio-core-client-spark-1.0.1-jar-with-dependencies.jar
getFile http://alluxio.org/downloads/files/1.3.0/alluxio-1.3.0-hadoop2.7-bin.tar.gz
getFile https://github.com/kelseyhightower/setup-network-environment/releases/download/v1.0.0/setup-network-environment
getFile http://ftp.heanet.ie/mirrors/www.apache.org/dist/zeppelin/zeppelin-0.7.0/zeppelin-0.7.0-bin-all.tgz
getFile http://ftp.heanet.ie/mirrors/www.apache.org/dist/flink/flink-1.1.3/flink-1.1.3-bin-hadoop26-scala_2.11.tgz
getFile https://arm101-eiffel004.lmera.ericsson.se:8443/nexus/service/local/repositories/aia-releases/content/com/ericsson/component/aia/model/avro-json/2.0.20/avro-json-2.0.20.jar
getFile https://download2.rstudio.org/rstudio-server-rhel-1.0.136-x86_64.rpm
getFile https://repo.continuum.io/archive/Anaconda2-4.3.0-Linux-x86_64.sh
