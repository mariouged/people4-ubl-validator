# Ubl Validator - Schematron

Simply Compliance UBl 2.1 EN16931 Validator Schematron

## run

./gradlew bootRun

## test

curl http://localhost:8080/health

curl -X POST http://localhost:8080/schematron

> ubl eInvoicing-EN16931 OK
curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @ubl-tc434-example3.xml

> ubl OpenPEPPOL FAIL
curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @Norwegian-example-1.xml
