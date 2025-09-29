# Use a Gradle image with JDK 21
FROM gradle:8.14.2-jdk21 AS builder

WORKDIR /srv/src

# Copy all project files
COPY . /srv/src

# Build the project without running tests
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# Runtime image
FROM amazoncorretto:21 AS app

ARG workdir=/opt/backend
ENV WORKDIR ${workdir}

# Create working directory
RUN mkdir -p ${workdir}

# Copy entrypoint script and jar from build stage
COPY --from=builder /srv/src/docker/ ${workdir}
COPY --from=builder /srv/src/build/libs/forex-0.0.1-SNAPSHOT.jar ${workdir}/app-exec.jar

# Ensure the entrypoint script is executable
RUN chmod +x ${workdir}/entrypoint.sh

WORKDIR ${workdir}

EXPOSE 9971

# Start the application
ENTRYPOINT ["/bin/bash", "entrypoint.sh"]
