FROM configured-debian

COPY ./apache.conf /etc/apache2/sites-available/000-default.conf

COPY ./default-ssl.conf /etc/apache2/sites-available/

CMD /usr/sbin/apache2ctl -D FOREGROUND

EXPOSE 80
