package io.github.kdabir.adl.api;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class ActiveDirectoryBinder {
    private final ActiveDirectoryEnvironmentBuilder activeDirectoryEnvironmentBuilder;

    public ActiveDirectoryBinder(ActiveDirectoryEnvironmentBuilder activeDirectoryEnvironmentBuilder) {
        this.activeDirectoryEnvironmentBuilder = activeDirectoryEnvironmentBuilder;
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
        Hashtable environment = activeDirectoryEnvironmentBuilder.getActiveDirectoryEnvironment(url, domain, username, password);

        return new InitialLdapContext(environment, null);
    }
}