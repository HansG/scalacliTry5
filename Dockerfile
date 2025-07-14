# Start mit Eclipse Temurin JDK 18 auf Ubuntu 22.04 (Jammy, glibc ≥ 2.35 !!!! für scala)
FROM eclipse-temurin:18-jdk-jammy

VOLUME /scalacliTry
# System-Tools installieren und aufräumen
RUN apt-get update \
 && apt-get install -y \
      curl \
      gzip \
      bash \
 && rm -rf /var/lib/apt/lists/*


RUN curl -fL https://github.com/coursier/coursier/releases/latest/download/cs-x86_64-pc-linux.gz | gzip -d > cs && chmod +x cs && \
	./cs install scala-cli && \
	echo 'export PATH="/root/.local/share/coursier/bin:${PATH}"' >> /etc/profile

ENV PATH=/root/.local/share/coursier/bin:${PATH}

USER root

WORKDIR /scalacliTry
