#!/usr/bin/env bash

unset username
unset password
echo -n "username:"
read username
prompt="password:"
while IFS= read -p "$prompt" -r -s -n 1 char
do
    if [[ $char == $'\0' ]]
    then
         break
    fi
    prompt='*'
    password+="$char"
done

BOXNAME="aia-sandbox"
BOXPROVIDER="virtualbox"
BOXVERSION="1.1.1"
BOXLOCATION="output-aia-sandbox-1-0-1-virtualbox.box"
CURLSTR="curl -i -u${username}:${password} -T ${BOXLOCATION} \"https://arm.epk.ericsson.se/artifactory/proj-aia-vagrant-local/${BOXNAME}.box;box_name=${BOXNAME};box_provider=${BOXPROVIDER};box_version=${BOXVERSION}\" > /tmp/upload.log"
#echo $CURLSTR
eval $CURLSTR
