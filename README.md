Active Directory Lookup
=======================

Active Directory Lookup is a simple Java API to access MS Active Directory for doing very simple things like Authentication
and Search.

## Building

The project is built and packaged using Gradle.

### Unit Testing

`gradle test`

Unit tests do not depend on any Active Directory instance and hence can be run without any configuration whatsoever.

### Integration Testing

`gradle integrationTests`

You need to set the `adl.properties` in the root of the project with the right configuration set in order to run Integration
Tests.


## References :

* http://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-ldap.html
* http://docs.oracle.com/javase/tutorial/jndi/ops/faq.html
* http://technet.microsoft.com/en-us/library/aa996205(v=exchg.65).aspx#BasicLDAPSyntax