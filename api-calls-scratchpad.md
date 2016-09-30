$ curl "https://rest.interfax.net/outbound/faxes?faxNumber=+442084978672" -u devexp-java:ZAQ\!zaq1 --data-binary "@/Users/anuragkapur/Downloads/test.pdf" -H "Content-Type: application/pdf"
$ curl "https://rest.interfax.net/outbound/faxes/664598111" -u devexp-java:ZAQ\!zaq1
$ curl "https://rest.interfax.net/inbound/faxes" -u devexp-java:ZAQ\!zaq1
$ curl "https://rest-pilot.interfax.net/inbound/faxes/292112264/image" -u devexp-java:ZAQ\!zaq1 --output test-received.pdf