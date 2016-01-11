 JavaScript (Angular JS) part of the Gateway (GAS) that holds the following components:

- form for voucher activation
- form for registering username with password
- login page that operates an Angular JS controller to activate the registration (not finished), and to access basic user data for the logged in user with no further activation needed

The Angular JS part takes adventage on the server status codes, in general.

- hmac is used as hashed message authentication to validate specific requests with the server (the key components may vary as per the state of the workflow, but the concept is to use one that is both accessible, independently on client and server side, however another implementations of symetric encryptions are available for mobiles (native apps), because the key components can be safely stored in the native mobile app, unlike in a web app or web view where it may be openly exposed
- all crypthography implementations are accomplished using Google Crypto JS, that is also available for use in Swift iOS through JavaScript virtual machine and there is server side counterpart for it (as per this system it is Java, but check the possible ways of interoperability in other languages at this site: http://www.jokecamp.com/blog/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#js)
- Angular JS http request/response providers will help retrieve, transform and manipulate headers during the request/response
- an Angular JS login or register controller will follow redirects as per specific server response and/or header value


