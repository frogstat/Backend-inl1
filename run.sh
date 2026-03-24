#!/bin/bash

echo "Starting program. Please wait..."
mvn -q package
mvn -q exec:java
