FROM lumue/jdkydl:alpine
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar /app.jar


CMD chmod a+rwx /app.jar &&   mkdir /kodi-api-service && chmod a+rwx /kodi-api-service

EXPOSE 8003

VOLUME ["/kodi-api-service"]

ENTRYPOINT ["java","-jar","-Dkrp.path.home=/kodi-api-service","/app.jar","--spring.config.location=file:/kodi-api-service/config/krp.properties"]