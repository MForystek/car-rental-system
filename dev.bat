@echo off
cd car-rental-system
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
cd ..
@echo on