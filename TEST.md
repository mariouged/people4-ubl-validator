# TEST

./gradlew test

./gradlew test --console=plain

##

**OK**
```bash
curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @ubl-tc434-example3.xml
```
response
```json
{"ok":true,"messages":[]}
```
**OK**
```bash
curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @Vat-category-S.xml
```

**FAIL**
```bash
curl -s -w "\nHTTP:%{http_code}\n" -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary '<Invoice><unclosed></Invoice>'
```

**FAIL**
```bash
curl -s -X POST http://localhost:8080/schematron -H "Content-Type: application/xml" --data-binary @Norwegian-example-1.xml
```
response
```json
{"ok":false,"messages":["[UBL-CR-679]-A UBL invoice should not include the ClassifiedTaxCategory/ID schemeID"]}
```
