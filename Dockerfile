FROM tomcat:10.1-jdk11

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Set environment variables
ENV DB_URL=jdbc:postgresql://postgres:5432/puskesmas_db
ENV DB_USER=postgres
ENV DB_PASSWORD=postgres

# Copy WAR file
COPY target/inventory.war /usr/local/tomcat/webapps/inventory.war

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
