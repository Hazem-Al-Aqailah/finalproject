#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ADD target/finalproject-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#
FROM almalinux
ENV JAVA_VERSION 8u31
ENV BUILD_VERSION b13
# Upgrading system
RUN yum -y upgrade
RUN yum -y install wget
# Downloading & Config Java 8
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm
RUN rpm -ivh jdk-17_linux-x64_bin.rpm
#RUN alternatives – install /usr/bin/java jar /usr/java/latest/bin/java 200000
#RUN alternatives – install /usr/bin/javaws javaws /usr/java/latest/bin/javaws 200000
#RUN alternatives – install /usr/bin/javac javac /usr/java/latest/bin/javac 200000
EXPOSE 8080
#install Spring Boot artifact
VOLUME /tmp
#ADD /maven/sfg-thymeleaf-course-0.0.1-SNAPSHOT.jar sfg-thymeleaf-course.jar
COPY DataBaseSchema.txt /
ADD target/finalproject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#ADD target/finalproject-0.0.1-SNAPSHOT.jar app.jar
#RUN sh -c 'touch /finalproject-0.0.1-SNAPSHOT.jar'
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/finalproject-0.0.1-SNAPSHOT.jar"]

#FROM openjdk:8-jdk-alpine
#MAINTAINER atypon.com
#COPY target/finalproject-0.0.1-SNAPSHOT.jar finalproject-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/finalproject-0.0.1-SNAPSHOT.jar"]

#FROM openjdk:12
#COPY target/finalproject-0.0.1-SNAPSHOT.jar finalproject-0.0.1-SNAPSHOT.jar
#version: '3'
#services:
#  finalproject:
#    build: finalproject/
#    ports:
#      - "8050:8080"
#  node:
#    build: node/
#    ports:
#      - "8060:8080"