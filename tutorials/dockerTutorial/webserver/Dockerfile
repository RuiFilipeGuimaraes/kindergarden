FROM ubuntu 

#disable user input interactions when installing stuff
ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update 
RUN apt-get install -y apache2 
RUN apt-get install -y apache2-utils 
RUN apt-get clean 
EXPOSE 80 

RUN which apache2ctl
ENTRYPOINT ["apache2ctl","-DFOREGROUND"]


