FROM openjdk:8-jre-alpine
ARG version

RUN mkdir -p /opt/lib/geoq
COPY ./build/distributions/geo-discovery-server-$version.tar /opt/lib/geoq
COPY ./build/distributions/entrypoint.sh /opt/lib/geoq

RUN tar -xf /opt/lib/geoq/geo-discovery-server-$version.tar -C /opt/lib/geoq/ \
&& mv /opt/lib/geoq/geo-discovery-server-$version /opt/lib/geoq/geo-discovery-server/ \
&& rm /opt/lib/geoq/geo-discovery-server-$version.tar \
&& chmod +x /opt/lib/geoq/entrypoint.sh \
&& history -c; history -w

EXPOSE 12822

ENTRYPOINT /opt/lib/geoq/entrypoint.sh