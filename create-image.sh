./gradlew bootBuildImage

docker run -p 8080:8080 172.17.0.1:5000/api-ecommerce:0.0.1-SNAPSHOT

docker push 172.17.0.1:5000/api-ecommerce:0.0.1-SNAPSHOT