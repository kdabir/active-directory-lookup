Active Directory Lookup
=======================

Active Directory Lookup is an extremely simple Java API to access MS Active Directory for common tasks like user authentication and search. This lightweight library does not depend on any other library (No transitive dependencies) and is merely 17Kb in size. It also provides a minimal [CLI](https://github.com/kdabir/active-directory-lookup/blob/master/src/main/java/io/github/kdabir/adl/cli/ActiveDirectoryCLI.java) for quick operations.


## Quick Start

Assuming we know the values of these variables 

```java
String domain;              // e.g. acme.org
String url;                 // e.g. ldap://somehost.acme.org or ldap://someotherhost.com
String searchBase;          // e.g. dc=acme,dc=org
String username;            // e.g. johndoe
String password;            // e.g. password
```

### Authenticating with Active Directory

```java
authenticator = new ActiveDirectoryAuthenticator(domain, url); // check out other constructors
    
authenticator.authenticate(username, password);
```

### Searching in Active Directory

```java
searcher = new SimpleActiveDirectorySearcher(url, domain, username, password, searchBase);

searcher.searchByUsername("superman");
```
### Building LdapContext

```java
LdapContext ldapContext = ActiveDirectoryAuthenticator
                .getDefaultActiveDirectoryBinder()
                .getLdapContext(url, domain, username, password);
```

## Installation

The built library can be consumed directly from jitpack repo

[![Release](https://jitpack.io/v/com.kdabir/active-directory-lookup.svg)](https://jitpack.io/#com.kdabir/active-directory-lookup)


### Using Gradle

Add this at the top of build.gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```    
Add/merge the dependency in the `dependencies` section    

```groovy
dependencies {
    implementation "com.kdabir:active-directory-lookup:1.0.2" 
}
```

### Using Maven

Add this to the `pom.xml`

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add this to the `dependencies` section within `pom.xml`

```xml
<dependency>
    <groupId>com.kdabir</groupId>
    <artifactId>active-directory-lookup</artifactId>
    <version>1.0.2</version>
</dependency>
```

## API Documentation

Browse the [JavaDoc](https://jitpack.io/com/kdabir/active-directory-lookup/1.0.2/javadoc/) for 
details. The key methods to look out for are: 
  
- [`ActiveDirectoryAuthenticator::authenticate(..)`](https://jitpack.io/com/kdabir/active-directory-lookup/1.0.2/javadoc/io/github/kdabir/adl/api/ActiveDirectoryAuthenticator.html#authenticate-java.lang.String-java.lang.String-) 
- [`ActiveDirectoryAuthenticator::isValid(..)`](https://jitpack.io/com/kdabir/active-directory-lookup/1.0.2/javadoc/io/github/kdabir/adl/api/ActiveDirectoryAuthenticator.html#isValid-java.lang.String-java.lang.String-)  
- [`ActiveDirectorySearcher::search(..)`](https://jitpack.io/com/kdabir/active-directory-lookup/1.0.2/javadoc/io/github/kdabir/adl/api/ActiveDirectorySearcher.html#search-io.github.kdabir.adl.api.filters.SearchFilter-)
- [`SimpleActiveDirectorySearcher::searchByUsername(..)`](https://jitpack.io/com/kdabir/active-directory-lookup/1.0.2/javadoc/io/github/kdabir/adl/api/SimpleActiveDirectorySearcher.html#searchByUsername-java.lang.String-)

## Building Locally

The project is built and packaged using Gradle.

### Unit Testing

`gradle test`

Unit tests do not depend on any Active Directory instance and hence can be run without any configuration whatsoever.

### Integration Testing

`gradle integrationTests`

You need to set the `adl.properties` in the root of the project with the right configuration set in order to run Integration
Tests.

### Installing to local Maven repo

`gradle publishToMavenLocal`

This make's the jar available in you local maven repository for usage.


## References :

* http://docs.oracle.com/javase/8/docs/technotes/guides/jndi/jndi-ldap.html
* http://docs.oracle.com/javase/tutorial/jndi/ops/faq.html
* http://technet.microsoft.com/en-us/library/aa996205(v=exchg.65).aspx#BasicLDAPSyntax


## Finding configuration


`nslookup -type=srv _ldap._tcp.DOMAINNAME`
