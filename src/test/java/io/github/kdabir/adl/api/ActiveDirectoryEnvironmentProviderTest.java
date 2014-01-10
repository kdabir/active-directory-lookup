package io.github.kdabir.adl.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;

import static javax.naming.Context.*;
import static org.junit.Assert.assertEquals;

public class ActiveDirectoryEnvironmentProviderTest {
    Hashtable environment;

    @Before
    public void setUp() throws Exception {
        environment = new ActiveDirectoryEnvironmentProvider()
                .getActiveDirectoryEnvironment("ldap://example.com", "example.com", "user", "password");
    }

    @Test
    public void testInitialContextFactory() {
        assertEquals("com.sun.jndi.ldap.LdapCtxFactory", environment.get(INITIAL_CONTEXT_FACTORY));
    }

    @Test
    public void testSecurityAuth() throws Exception {
        assertEquals("simple", environment.get(SECURITY_AUTHENTICATION));
    }

    @Test
    public void testProviderUrl() throws Exception {
        assertEquals("ldap://example.com", environment.get(PROVIDER_URL));
    }

    @Test
    public void testSecurityPrincipal() throws Exception {
        assertEquals("user@example.com", environment.get(SECURITY_PRINCIPAL));
    }

    @Test
    public void testSecurityCredentials() throws Exception {
        assertEquals("password", environment.get(SECURITY_CREDENTIALS));
    }
}
