@echo off
cd car-rental-system
mvn clean install jib:dockerBuild
cd ..
@echo on