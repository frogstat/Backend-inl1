#!/bin/bash

echo "Starting program. Please wait..."
mvn -q compile exec:java -Dexec.mainClass="se.yrgo.main.Main"
