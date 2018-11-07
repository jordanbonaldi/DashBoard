FROM ubuntu:bionic

ADD . /code
WORKDIR /code

RUN apt-get update && apt-get install -y gradle

EXPOSE 8080

CMD ["gradle", "run", "-q"]
