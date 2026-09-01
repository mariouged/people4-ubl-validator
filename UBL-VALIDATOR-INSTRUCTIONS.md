# UBL VALIDATOR INSTRUCCTIONS TO IA AGENT

Generate code to schematron will be an ubl validator

Environment 
- openjdk 17.0.20
- Spring Boot Framework 'org.springframework.boot:spring-boot-starter-web'
- testImplementation 'org.springframework.boot:spring-boot-starter-test'

Endpoint `/schematron` it's an UBL 2.1 EN16931 Euro Invoice validator 


## Feature - Whats
- Given the xml file `ubl-tc434-example3.xml` requested to an endpoint `/schematron` in the request body method POST
- The schematron Service validate xml, ubl, namespace and schemas using [eInvoicing-EN16931 Repository](https://github.com/ConnectingEurope/eInvoicing-EN16931/blob/master/ubl/schematron/EN16931-UBL-validation.sch)
- Return the ok true or false and the messages error list or empty messages error list in case ok 
- Each message error begin within tag `[AA-99]-message error` where `[AA-99]` will be the rule from [eInvoicing-EN16931 Model](https://github.com/ConnectingEurope/eInvoicing-EN16931/blob/master/ubl/schematron/abstract/EN16931-model.sch) and [eInvoicing-EN16931 Syntax](https://github.com/ConnectingEurope/eInvoicing-EN16931/blob/master/ubl/schematron/abstract/EN16931-syntax.sch)
- The service return bool and messages and the endpoint `/schematron` response the following JSON content:
```json
{
  "ok": false,
  "messages": [
    "[BR-13]-An Invoice shall have the Invoice total amount without VAT (BT-109)."
  ]
}
```

## Test
Generate the test implementation only in the Schematron Service
The Schematron Service received the xml and response the ok and messages
Test the service, the endpoint will be tested with curl o postman
