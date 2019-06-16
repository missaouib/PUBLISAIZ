#!/bin/bash
# init
function pause(){
   read -p "$*"
}

function ask {
    read -n 1 -r
    if [[ $REPLY =~ ^[Yy]$ ]]
    then
            echo "1"
            return 1;
    else
            echo "0"
            return 0;
    fi
}

docker run -d -p 9000:9000 --name portainer -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer

clear
echo "================================================================================================"
echo "securing changes"
echo "================================================================================================"
pwd
git stash
git pull
git stash apply
echo "================================================================================================"
echo "building front end : perform build? [y/n]"
echo "================================================================================================"
a=$(ask);
if [ $a == 1 ];
then
   echo " ] =====================================building==============================================="
   cd frontend/angular
   ng build --prod
   cd ../..
   echo "=========================================done==============================================="
fi
echo "================================================================================================"
echo "building back end : perform build? [y/n]"
echo "================================================================================================"
a=$(ask);
if [ $a == 1 ];
then
   echo " ] =====================================building==============================================="
   docker run -it --rm --name PUBLISAIZ-maven-build -v "m2":/root/.m2  -v /root/publisaiz/backend:/usr/src/mymaven -w /usr/src/mymaven maven:3.6.1-jdk-11-slim mvn clean install
   echo "=========================================done==============================================="
fi
echo "================================================================================================"
echo "if anything failes concider ctrl+c"
echo "================================================================================================"
pause 'Press [Enter] key to continue...'
echo "removing old instance"
echo "================================================================================================"
docker-compose down
docker rm publisaiz_angular
docker rmi publisaiz_angular
echo "================================================================================================"
echo "deploing recent instance"
echo "================================================================================================"
docker-compose up -d
docker ps -a
echo "================================================================================================"
git status
echo "================================================================================================"
echo "tailing log file"
echo "================================================================================================"

tail -f resources/application.log
