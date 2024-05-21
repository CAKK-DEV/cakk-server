FROM nginx:1.19.6-alpine
COPY ./config/nginx.conf /etc/nginx/conf.d/nginx.conf
ENTRYPOINT ["nginx", "-g", "daemon off;"]
