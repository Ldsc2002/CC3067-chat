<!-- Write a readme for this project -->

# XMPP Client

## Description
This is a simple XMPP client that can be used to send and receive messages from an XMPP server.

## Requirements
* Java
* Maven
* Smack API
* Openfire XMPP server

## Installation

###  cd into xmpp-client
```bash
cd xmpp-client
```

### Install dependencies
```bash
mvn clean package
```

### Compile project
```bash
mvn compile
```

### Run
```bash
mvn exec:java -Dexec.mainClass="client.App"
```

### Build
```bash
mvn package
```

### Run .jar 
```bash
java -cp target/xmpp-client-1.0.jar client.App
```

## Features
* Register new user
* Login
* Close connection
* Delete user
* Show all users and their status
* Add user to roster
* Show contact details
* Send message to user
* Send message to group
* Change status
* Change presence
* File transfer
* Notifications
