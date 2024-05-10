docker build --platform linux/amd64 -t mysql .
docker run --restart always -e TZ=Asia/Seoul --name mysql -d -p 3306:3306 mysql
