# Start mit Eclipse Temurin JDK 18 auf Ubuntu 22.04 (Jammy, glibc ≥ 2.35 !!!! für scala)
FROM eclipse-temurin:18-jdk-jammy
# oder FROM mcr.microsoft.com/devcontainers/base:ubuntu + apt-get .. openjdk-17-jdk
VOLUME /scalacliTry
# System-Tools installieren und aufräumen  Xvom devContX
RUN apt-get update \
 && apt-get -y install --no-install-recommends \
      curl \
      gzip \
      unzip \
      bash \
      git \
#      openjdk-17-jdk \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*


RUN curl -fL https://github.com/coursier/coursier/releases/latest/download/cs-x86_64-pc-linux.gz | gzip -d > /usr/local/bin/cs  \
    && chmod +x /usr/local/bin/cs && \
	cs install scala-cli && \
#	cs setup --yes --apps scala-cli,scala && \    ./cs nötig??
	echo 'export PATH="/root/.local/share/coursier/bin:${PATH}"' >> /etc/profile

ENV PATH=/root/.local/share/coursier/bin:${PATH}

USER root

WORKDIR /scalacliTry
