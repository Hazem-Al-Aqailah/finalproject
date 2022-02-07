#FROM almalinux
FROM almalinux
ENV JAVA_VERSION 8u31
ENV BUILD_VERSION b13
# Upgrading system
RUN yum -y upgrade
RUN yum -y install wget
# Downloading & Config Java
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm
RUN rpm -ivh jdk-17_linux-x64_bin.rpm
EXPOSE 8080
#install Spring Boot artifact
VOLUME /tmp
COPY DataBaseSchema.txt /
ADD target/finalproject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
