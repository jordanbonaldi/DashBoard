FROM configured-debian

COPY .conf/prod/apache.conf /usr/local/apache2/sites-enabled/000-default.conf

COPY .conf/prod/default-ssl.conf /etc/apache2/sites-available/

CMD /usr/sbin/apache2ctl -D FOREGROUND

WORKDIR /var/www/project

COPY . .

RUN rm -rf node_modules/

RUN npm install -g pngquant-bin --allow-root --unsafe-perm=true

RUN npm i && npm run prod

RUN cp -R public/* ../html/.

RUN ls -l /var/www/html/
