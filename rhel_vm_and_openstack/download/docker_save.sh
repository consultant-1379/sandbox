pull_save_docker(){
   image_name=$1
   
   docker pull ${image_name}
   docker save -o ${image_name##*/}.tar ${image_name}
}

pull_save_docker armdocker.rnd.ericsson.se/aia/aia-sdk-paas-manager
pull_save_docker armdocker.rnd.ericsson.se/aia/base/flink-base/filnk:1.1.3-hadoop26-scala2.11
pull_save_docker armdocker.rnd.ericsson.se/aia/base/spark_base_1.6.0
