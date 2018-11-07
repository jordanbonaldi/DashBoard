FROM php:7.2.1-apache-stretch

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -my wget gnupg \
    && echo 'deb http://ftp.debian.org/debian stretch-backports main' >> /etc/apt/sources.list \
    && curl -sS http://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - \
    && echo "deb http://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list \
    && apt-get update \
    && apt-get install -y sudo \
    && apt-get install -y apt-utils \
    && apt-get install -y build-essential patch \
    && a2enmod rewrite \
    && a2enmod ssl \
    && a2enmod headers \
    && a2enmod actions \
    && apt-get install -y libpq-dev \
    && apt-get install -y libpng-dev \
    && apt-get install -y libmcrypt-dev \
    && apt-get install -y libssl-dev \
    && apt-get install curl -y \
    && curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash - \
    && apt-get install -y nodejs \
    && npm install -g bower \
    && apt-get install -y yarn \
    && apt-get install -y openssl ssl-cert \
	 && mkdir -p /usr/share/man/man1 && \
    (echo "deb http://http.debian.net/debian jessie-backports main" > /etc/apt/sources.list.d/backports.list) && \
    apt-get update -y \
	 && apt-get install openjdk-8-jre -y && apt-get install openjdk-8-jdk -y\
    && apt-get install -t jessie-backports openjdk-8-jdk -y \
	 && apt-get install software-properties-common -y \
	 && apt-get install gradle -y

COPY ./prod/port.conf .

RUN mkdir /etc/apache2/ssl && /usr/sbin/make-ssl-cert /usr/share/ssl-cert/ssleay.cnf /etc/apache2/ssl/apache.pem \
	&& cat port.conf >> /etc/apache2/ports.conf

RUN a2enmod ssl
RUN a2ensite default-ssl

RUN a2enmod php7
RUN a2enmod rewrite

EXPOSE 80
