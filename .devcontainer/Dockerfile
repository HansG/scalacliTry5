# Start mit Eclipse Temurin JDK 18 auf Ubuntu 22.04 (Jammy, glibc ≥ 2.35 !!!! für scala)
FROM eclipse-temurin:18-jdk-jammy

USER root
ARG MHOME=root
#ARG USERNAME=vscode
#RUN usermod -aG root ${USERNAME}

VOLUME /scalacliTry

# System-Tools installieren und aufräumen
RUN apt-get update \
 && apt-get install -y \
	  curl \
	  gzip \
	  bash \
 && rm -rf /var/lib/apt/lists/*

# Netztest
RUN apt-get update \
 && apt-get install -y iputils-ping dnsutils \
 && ping -c 4 github.com \
 && nslookup github.com \
 && curl -v https://github.com/

#WORKDIR /scalacliTry
RUN curl -fsSL \
    https://github.com/coursier/coursier/releases/latest/download/cs-x86_64-pc-linux.gz \
  | gzip -d > /usr/local/bin/cs \
 && chmod +x /usr/local/bin/cs  
# Scala-CLI installieren
RUN cs install scala-cli --yes && \
	echo 'export PATH="${MHOME}/.local/share/coursier/bin:${PATH}"' >> /etc/profile

ENV PATH=${MHOME}/.local/share/coursier/bin:${PATH}



#FROM ubuntu:22.04
#
#RUN apt-get update && \
#	apt-get install -y curl unzip openjdk-17-jdk && \
#	curl -fLo scala-cli.zip https://github.com/VirtusLab/scala-cli/releases/latest/download/scala-cli-x86_64-pc-linux.zip && \
#	unzip scala-cli.zip -d /usr/local/bin && \
#	chmod +x /usr/local/bin/scala-cli
#
#ENV PATH="/usr/local/bin:${PATH}"

