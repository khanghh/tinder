FROM node:alpine

RUN apk --update add imagemagick && \
    rm -rf /var/cache/apk/*

RUN npm install pm2 -g

WORKDIR /app
COPY package.json .
RUN npm install --quite

COPY ./src .
