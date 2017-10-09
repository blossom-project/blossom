# Blossom

Blossom is a Java framework which enables you to realize your projects quickly and smoothly.
It comes with several modules and tools made to ease and speed up your development process.

## Quick start

Sadly, there is no quick start yet :'-(

You must clone the repository, build, install it manually, then you can use the project generator to start your first project with Blossom.

1. git clone https://github.com/mgargadennec/blossom.git
2. cd blossom
3. mvn clean install
4. java -jar blossom-tools/blossom-tools-initializr/target/blossom-tools-initializr-0.0.1-SNAPSHOT.jar
5. Go to localhost:8090
6. Choose your modules and generate your project !


## Features

### Minimal features

### Back-office
Blossom comes with a back-office interface (module : blossom-ui-web) which can be accessed on http://localhost:8080/blossom.
You can then connect with the default user and password : system / system.

This default user can be configured with the following properties :

    blossom.security.default.account.enabled=true
    blossom.security.default.account.identifier=system
    blossom.security.default.account.password=system

### Security

### Caching

### Application Events

### Plugin system

### Indexation and Search

### Bean Validation

### Exception Handling

### Action tokens

### Rendering

### Liquibase

### Scheduling

### I18n

### Mails


