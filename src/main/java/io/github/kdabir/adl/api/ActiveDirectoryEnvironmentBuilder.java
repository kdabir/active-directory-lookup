package io.github.kdabir.adl.api;

import javax.naming.Context;
import java.util.Hashtable;

public class ActiveDirectoryEnvironmentBuilder {

    Hashtable getActiveDirectoryEnvironment(String url, String domain, String username, String password) {
        Hashtable environment = new Hashtable();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// can make it variable, not needed so far
        environment.put(Context.SECURITY_AUTHENTICATION, "simple"); // can make it variable, not needed so far
        environment.put(Context.PROVIDER_URL, url);
        environment.put(Context.SECURITY_PRINCIPAL, username + "@" + domain); // This is specific to AD
        environment.put(Context.SECURITY_CREDENTIALS, password);
        return environment;
    }
}