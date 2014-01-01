package io.github.kdabir.adl.api;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class ActiveDirectoryBinder {
    public ActiveDirectoryBinder() {
    }

    /**
     * Method tries to get LdapContext by connecting to the Active Directory.
     *
     * @param url
     * @param domain
     * @param username
     * @param password
     * @return LdapContext
     * @throws javax.naming.NamingException - if bind is unsuccessful
     */
    public LdapContext getLdapContext(String url, String domain, String username, String password)
            throws NamingException {
        Hashtable environment = new Hashtable();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// can make it variable, not needed so far
        environment.put(Context.SECURITY_AUTHENTICATION, "simple"); // can make it variable, not needed so far
        environment.put(Context.PROVIDER_URL, url);
        environment.put(Context.SECURITY_PRINCIPAL, username + "@" + domain); // This is specific to AD
        environment.put(Context.SECURITY_CREDENTIALS, password);

        return new InitialLdapContext(environment, null);
    }
}