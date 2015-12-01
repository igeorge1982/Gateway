Login sample in Angular JS

(found in the Login folder. the Registering part is yet to come, but it will work similarly)

- hmac will be used as hashed message authentication to validate requests with the server (JSESSIONID will be used as the key component, as the same one is both accessible, independently on client and server side) -> the same technic is meant to be used system-wide, because JSESSIONID is stored in the dB and in the headers of the browsers, as well, however another implementations of symetric encryption seems to be available for mobiles, because the key components can be safely stored in the mobile app, unlike in a web app where it may be openly exposed
- hashings can be accomplished using Google Crypto JS, that is also available for Swift iOS
- Angular JS http request/response providers will help on desktop browsers to retrieve, transform and manipulate headers during the request/response which will also help direct the user to the appropiate site depending on the user-agent of the platform


