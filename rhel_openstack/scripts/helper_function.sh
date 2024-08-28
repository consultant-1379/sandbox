#!/usr/bin/bash

echo "proxy required: $(printenv PROXY_REQUIRED)"
echo "mapr repo self: $(printenv MAPR_REPO_SELF)"
echo "mapr repo host: $(printenv MAPR_REPO_HOST)"
PROXY_REQUIRED=$(printenv PROXY_REQUIRED);
MAPR_REPO_SELF=$(printenv MAPR_REPO_SELF);
MAPR_REPO_HOST=$(printenv MAPR_REPO_HOST);


get_internal_address(){
   internal_address="$(ifconfig eth0 | grep inet | grep -v inet6 | awk -F' ' {'print $2'})"
   echo $internal_address
}

get_public_address(){
   public_address="$(ifconfig eth0 | grep "inet" |grep -v inet6 | awk -F' ' '{print $2}')"
   echo $public_address
}

is_master(){
    hostname=$(hostname | awk -F'.' {'print $1'})
 #   server_mode=$(cat 0_serverconfig | grep "${hostname}" | awk -F' ' {'print $3'})
 #   if [ "$server_mode" == "master" ];then
 #      echo 1
 #   else 
 #      echo 0
 #   fi
    echo 1
}

get_hostname(){
   # hostname=`hostname | awk -F'.' {'print $1'}`
    echo $(hostname)
}

get_master_name(){
  # master_node=`cat 0_serverconfig | grep master | awk -F' ' {'print $2'}`
  # echo ${master_node}
   echo $(hostname)
}

get_cluster(){
  # del=$1
  # list_node=`cat 0_serverconfig | awk -F' ' {'print $2'} | tr '\n' "${del}"`
  # echo ${list_node::-1}
  echo $(hostname)
}

