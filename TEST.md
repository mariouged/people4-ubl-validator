# TEST

./gradlew test

./gradlew test --console=plain

##

curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @ubl-tc434-example3.xml

curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @/tmp/broken.xml

curl -s -w "\nHTTP:%{http_code}\n" -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary '<Invoice><unclosed></Invoice>'