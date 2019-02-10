FROM lumue/jdkydl:alpine
MAINTAINER lm "mueller.lutz@gmail.com"

RUN ln -s `which youtube-dl` /usr/local/bin/youtube-dl
RUN ln -s `which ffmpeg` /usr/local/bin/ffmpeg
RUN ln -s `which ffprobe` /usr/local/bin/ffprobe

ADD *.jar /app.jar
CMD mkdir -p /woelkchen/download && mkdir /downloads

EXPOSE 8001

VOLUME ["/woelkchen/download"]
VOLUME ["/downloads"]

ENTRYPOINT ["java","-Xms20M","-Xmx100M","-Dwoelkchen.download.path.home=/woelkchen/download","-Dwoelkchen.download.path.download=/downloads","-jar","/app.jar","--spring.config.location=file:/woelkchen/download/download.properties"]