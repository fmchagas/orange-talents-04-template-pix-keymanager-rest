FROM adoptopenjdk/openjdk11:jre11u-alpine-nightly
RUN addgroup micronaut
RUN adduser -DH micronaut -G micronaut
#roda app no contexto do user e group micronaut no container
USER micronaut:micronaut

ARG JAR_FILE=/build/libs/*all.jar
#COPY ${JAR_FILE} /usr/app/app.jar
ADD ${JAR_FILE} /usr/app/app.jar

#ENV APP_NAME keymanager-rest

ENTRYPOINT ["java","-Xmx512m","-Dfile.encoding=UTF-8","-jar","/usr/app/app.jar"]