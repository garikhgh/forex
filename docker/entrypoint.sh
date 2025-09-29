#!/bin/bash

java ${JAVA_ARGS} ${CUSTOM_OPTS} -Dspring.profiles.active=${PROFILE} -jar app-exec.jar