#!/bin/bash

echo "Starting program. Please wait..."
mvn -q compile
mvn -q exec:java
