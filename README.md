Active Directory Lookup
=======================

Active Directory Lookup is a simple Java API to access MS Active Directory for doing very simple things like Authentication
and Search.

## Using

The built library can be consumed using from jitpack repo  
[![](https://jitpack.io/v/kdabir/active-directory-lookup.svg)](https://jitpack.io/#kdabir/active-directory-lookup)

### Gradle


Add this at the top of build.gradle

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
    
Add/merge the dependency in the `dependencies` section    

    dependencies {
	    compile 'com.github.kdabir:active-directory-lookup:1.0.0'
    }


### Maven


Add this in the pom.xml

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>


Add this to the `dependencies` section within pom.xml

	<dependency>
	    <groupId>com.github.kdabir</groupId>
	    <artifactId>active-directory-lookup</artifactId>
	    <version>1.0.0</version>
	</dependency>


## Building

[![Build Status](https://travis-ci.org/kdabir/active-directory-lookup.svg)](https://travis-ci.org/kdabir/active-directory-lookup)

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

* http://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-ldap.html
* http://docs.oracle.com/javase/tutorial/jndi/ops/faq.html
* http://technet.microsoft.com/en-us/library/aa996205(v=exchg.65).aspx#BasicLDAPSyntax


## Finding configuration


`nslookup -type=srv _ldap._tcp.DOMAINNAME`
