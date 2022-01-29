FROM centos:8 AS base

RUN useradd -ms /bin/bash tester

RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-Linux-* \
    && sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-Linux-* \
    && yum -y install java-1.8.0-openjdk-devel wget unzip xorg-x11-server-Xvfb xdpyinfo \
    && mkdir /driver

ENV JAVA_HOME="/usr/lib/jvm/java-1.8.0" \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF8" \
    DISPLAY=":99" 
COPY src/test/resources/docker/initscript /tmp
RUN cat /tmp/initscript >> /home/tester/.bashrc && rm /tmp/initscript
WORKDIR /tests
ENTRYPOINT ["bash", "-i"]


FROM base AS chrome83
RUN wget http://dist.control.lth.se/public/CentOS-7/x86_64/google.x86_64/google-chrome-stable-83.0.4103.97-1.x86_64.rpm \
        && yum -y install epel-release && yum -y install ./google-chrome-stable-83.0.4103.97-1.x86_64.rpm \
        && wget https://chromedriver.storage.googleapis.com/83.0.4103.39/chromedriver_linux64.zip \
        && unzip chromedriver_linux64.zip -d /driver/ \
        && rm chromedriver_linux64.zip google-chrome-stable-83.0.4103.97-1.x86_64.rpm
USER tester
ENV PATH="/driver/:$PATH"

FROM base AS chromelast
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm \
        && yum -y install ./google-chrome-stable_current_x86_64.rpm \
        && wget https://chromedriver.storage.googleapis.com/97.0.4692.71/chromedriver_linux64.zip \
        && unzip chromedriver_linux64.zip -d /driver/ \
        && rm chromedriver_linux64.zip google-chrome-stable_current_x86_64.rpm  
USER tester
ENV PATH="/driver/:$PATH"
