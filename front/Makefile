dev:
	sudo docker run -d -it -v `pwd`:/var/www/html/ -p 443:443 frontjs_dev /bin/bash
	sudo docker exec -it `docker ps -l -q` /bin/bash

join_dev:
	sudo docker exec -it `docker ps -l -q` /bin/bash

clean:
	rm -rf node_modules/

build_dev:
	sudo docker build -t frontjs_dev .conf/dev/.

back:
	sudo docker run -d -it -p 8080:8080 back /bin/bash

install:
	sudo docker build -t configured-debian .conf/.
	sudo docker build -t frontjs_prod .
	sudo docker build -t frontjs_dev .conf/dev/.

.PHONY: dev prod
