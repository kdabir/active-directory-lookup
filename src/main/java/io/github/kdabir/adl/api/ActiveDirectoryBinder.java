package io.github.kdabir.adl.api;

import io.github.kdabir.adl.exceptions.ActiveDirectoryException;
import io.github.kdabir.adl.exceptions.BadCredentialsException;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class ActiveDirectoryBinder {

    private final ActiveDirectoryEnvironmentBuilder activeDirectoryEnvironmentBuilder;
    private final LdapContextFactory ldapContextFactory;

    public ActiveDirectoryBinder(ActiveDirectoryEnvironmentBuilder activeDirectoryEnvironmentBuilder, LdapContextFactory ldapContextFactory) {
        this.activeDirectoryEnvironmentBuilder = activeDirectoryEnvironmentBuilder;
        this.ldapContextFactory = ldapContextFactory;
    }

    /**
     * Method tries to get LdapContext by connecting (bind operation) to the Active Directory.
     *
     * @param url
     * @param domain
     * @param username
     * @param password
     * @return LdapContext
     * @throws javax.naming.NamingException - if bind is unsuccessful
     */
    public LdapContext getLdapContext(String url, String domain, String username, String password) {
        Hashtable<String, String> environment = activeDirectoryEnvironmentBuilder.getActiveDirectoryEnvironment(url, domain, username, password);
        try {
            return ldapContextFactory.getLdapContext(environment);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException(ex);
        } catch (CommunicationException ex) {
            throw new ActiveDirectoryException(ex);
        } catch (NamingException ex) {
            throw new ActiveDirectoryException(ex);
        }


    }
}