FROM ubuntu:18.04
MAINTAINER student
USER root

RUN apt-get update &&  apt-get install -y rpm
RUN apt-get clean all;\
    rpm --rebuilddb;\
    apt-get install -y curl openssh-server openssh-client rsync
RUN apt-get install -y libselinux-dev

RUN apt-get install -y sudo
RUN apt-get install -y nano

RUN ssh-keygen -q -N "" -t dsa -f /etc/ssh/ssh_host_dsa_key
#RUN ssh-keygen -q -N "" -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN ssh-keygen -q -N "" -t rsa -f /root/.ssh/id_rsa
RUN cp /root/.ssh/id_rsa.pub /root/.ssh/authorized_keys

RUN chmod 0600 ~/.ssh/authorized_keys

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

RUN wget http://mirrors.advancedhosters.com/apache/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz
RUN tar -zxf hadoop-3.2.1.tar.gz
RUN ln -s hadoop-3.2.1 hadoop

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV PATH $PATH:$JAVA_HOME/bin
RUN rm /usr/bin/java && ln -s $JAVA_HOME/bin/java /usr/bin/java

CMD ["/etc/bootstrap.sh","-d"]

EXPOSE 50010 50020 50070 50075 50090 8020 9000
EXPOSE 10020 19888
EXPOSE 8030 8031 8032 8033 8040 8042 8088
EXPOSE 49707 2122