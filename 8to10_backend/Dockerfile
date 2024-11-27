FROM gkdldhdl/jdk:17

LABEL maintainer="siho-song"

ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} ${WORK_DIR}/build/8to10.jar