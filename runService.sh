#!/bin/bash
function printSuccessInfo() {
	tput setaf 2
	echo $1
	tput setaf 7
}

timeout=300
local_host="`hostname --fqdn`"
local_ip=`ip addr | grep 'state UP' -A2 | tail -n1 | awk '{print $2}' | cut -f1 -d '/'`

#Use local ip from template and generate the docker-compose yml file
for filename in `ls *template.yml` ;
do
    newFilename=${filename/-template/}
    sed "s/127.0.0.1/$local_ip/g"  $filename > $newFilename
done

#Start ElasticSearch, RabbitMQ and redis
echo "Starting ElasticSearch, RabbitMQ and redis..."
docker-compose -f docker-compose-env.yml up -d
./wait-for-it.sh -t ${timeout} $local_ip:15672
printSuccessInfo "RabbitMQ is up!"

#Start Eureka server
echo "Starting Eureka server..."
docker-compose -f docker-compose-eureka.yml up -d
./wait-for-it.sh -t ${timeout} $local_ip:8761
printSuccessInfo "Eureka server is up!"

#Start user, consumer service
echo "Starting user, consumer service..."
docker-compose -f docker-compose-service.yml up -d
./wait-for-it.sh -t ${timeout} $local_ip:8080
./wait-for-it.sh -t ${timeout} $local_ip:8001
./wait-for-it.sh -t ${timeout} $local_ip:8002
./wait-for-it.sh -t ${timeout} $local_ip:8003
printSuccessInfo "User service is up!"

#Start notification service
echo "Starting notification service..."
docker-compose -f docker-compose-notification.yml up -d
./wait-for-it.sh -t ${timeout} $local_ip:8090
printSuccessInfo "User notification is up!"



