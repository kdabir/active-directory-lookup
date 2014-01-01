package io.github.kdabir.adl.api;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapContextFactory {
    LdapContext getLdapContext(Hashtable<String, String> environment) throws NamingException {
        return new InitialLdapContext(environment, null);
    }
}