FROM eclipse-temurin:17-jdk-jammy

ARG CMDLINE_TOOLS_VERSION=13114758
ARG ANDROID_PLATFORM=android-36
ARG ANDROID_BUILD_TOOLS=36.0.0
ARG GRADLE_PROFILER_VERSION=0.26.0
ARG REMOTING_VERSION=3391.va_37fa_a_305d6d

ENV ANDROID_HOME=/opt/android-sdk \
    ANDROID_SDK_ROOT=/opt/android-sdk \
    LANG=C.UTF-8
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:/opt/gradle-profiler/bin:${PATH}"

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl git procps unzip wget \
    && rm -rf /var/lib/apt/lists/*

RUN wget -q "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip" -O /tmp/cmdline-tools.zip \
    && mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && unzip -q /tmp/cmdline-tools.zip -d "${ANDROID_HOME}/cmdline-tools" \
    && mv "${ANDROID_HOME}/cmdline-tools/cmdline-tools" "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm /tmp/cmdline-tools.zip \
    && yes | sdkmanager --licenses > /dev/null || true \
    && sdkmanager --install \
        "platform-tools" \
        "platforms;${ANDROID_PLATFORM}" \
        "build-tools;${ANDROID_BUILD_TOOLS}" \
    && chmod -R a+rX "${ANDROID_HOME}"

RUN wget -q "https://github.com/gradle/gradle-profiler/releases/download/v${GRADLE_PROFILER_VERSION}/gradle-profiler-${GRADLE_PROFILER_VERSION}.zip" -O /tmp/gradle-profiler.zip \
    && unzip -q /tmp/gradle-profiler.zip -d /opt \
    && mv "/opt/gradle-profiler-${GRADLE_PROFILER_VERSION}" /opt/gradle-profiler \
    && chmod +x /opt/gradle-profiler/bin/gradle-profiler \
    && rm /tmp/gradle-profiler.zip \
    && gradle-profiler --version

RUN mkdir -p /opt/jenkins \
    && wget -q "https://repo.jenkins-ci.org/public/org/jenkins-ci/main/remoting/${REMOTING_VERSION}/remoting-${REMOTING_VERSION}.jar" \
        -O /opt/jenkins/agent.jar

RUN useradd --create-home --home-dir /home/jenkins --shell /bin/bash jenkins \
    && mkdir -p /home/jenkins/.gradle \
    && chown -R jenkins:jenkins /home/jenkins
USER jenkins
WORKDIR /home/jenkins

COPY --chown=jenkins:jenkins --chmod=755 jenkins-agent.sh /usr/local/bin/jenkins-agent

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s CMD pgrep -f agent.jar > /dev/null || exit 1

ENTRYPOINT ["jenkins-agent"]
