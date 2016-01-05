package io.github.kdabir.adl.api;

import io.github.kdabir.adl.api.filters.UsernameFilter;
import io.github.kdabir.adl.api.mapper.DefaultSearchResultMapper;
import io.github.kdabir.adl.util.ActiveDirectoryEnvironmentProvider;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.List;
import java.util.Map;

public class SimpleActiveDirectorySearcher extends ActiveDirectorySearcher<Map<String, String>> {

    public static final ActiveDirectoryEnvironmentProvider ACTIVE_DIRECTORY_ENVIRONMENT_PROVIDER = new ActiveDirectoryEnvironmentProvider();
    public static final LdapContextFactory LDAP_CONTEXT_FACTORY = new LdapContextFactory();
    public static final DefaultSearchResultMapper DEFAULT_SEARCH_RESULT_MAPPER = new DefaultSearchResultMapper();

    /**
     * Constructs the Searcher.
     * <p>
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     *
     * @param ldapContext
     * @param searchBase
     */
    public SimpleActiveDirectorySearcher(LdapContext ldapContext, String searchBase) {
        super(ldapContext, searchBase, DEFAULT_SEARCH_RESULT_MAPPER);
    }

    public SimpleActiveDirectorySearcher(String url, String domain, String username, String password, String searchBase) throws NamingException {
        this(getLdapContext(url, domain, username, password), searchBase);
    }

    /**
     * Search by username
     *
     * @param username to be searched
     * @return List of results
     */
    public List<Map<String,String>> searchByUsername(String username) {
        return this.search(new UsernameFilter(username));
    }

    private static LdapContext getLdapContext(String url, String domain, String username, String password) throws NamingException {
        return LDAP_CONTEXT_FACTORY.getLdapContext(
                ACTIVE_DIRECTORY_ENVIRONMENT_PROVIDER
                        .getActiveDirectoryEnvironment(url, domain, username, password)
        );
    }

}
