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

echo " ########## would you like to run new portainer on port 9000? [y/n]  ########## "

a=$(ask);
if [[ $a == 1 ]];
then
   echo " ] =====================================building============================="
   docker run -d -p 9000:9000 --name portainer -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer
   echo "=========================================done================================"
   pause 'Press [Enter] key to continue...'
fi

echo "================================================================================"
echo "securing changes"
echo "================================================================================"
pwd
git stash
git pull
git stash apply
echo "================================================================================"
echo "building front end : perform build? [y/n]"
echo "================================================================================"
b=$(ask);
echo "  ]"
if [[ $b  == 1 ]];
then
   echo "===================================building================================="
      echo "building with host npm and angular... "
	   cd frontend/angular
	   npm install
	   npm run build
	   cd ../..
      docker rm brzezinski/publisaiz
      docker rmi brzezinski/publisaiz
      docker build frontend -t brzezinski/publisaiz
      docker login -p $DOCKER_PASS -u brzezinski
      docker push brzezinski/publisaiz:latest
   echo " ========================================= done ================================ "
fi
echo "================================================================================"
echo "building back end : perform build? [y/n]"
echo "================================================================================"
c=$(ask);
if [[ $c == 1 ]];
then
   echo " ] =====================================building=============================="
   docker run -it --rm --name PUBLISAIZ-maven-build -v "m2":/root/.m2  -v /root/publisaiz/backend:/usr/src/mymaven -w /usr/src/mymaven maven:3.6.1-jdk-11-slim mvn clean install
   echo "=========================================done================================="
fi
echo "================================================================================"
echo "if anything failed just use: [ctrl+c] "
echo "================================================================================"
pause 'Press [Enter] key to continue...'
echo "removing old instance"
echo "================================================================================"
docker-compose down --remove-orphans
echo "================================================================================"
echo "deploying recent instance"
echo "================================================================================"
docker-compose up -d
docker ps -a
echo "================================================================================"
git status
echo "================================================================================"
echo "tailing log file"
echo "================================================================================"


docker-compose logs -f -t

#tail -f resources/application.log
