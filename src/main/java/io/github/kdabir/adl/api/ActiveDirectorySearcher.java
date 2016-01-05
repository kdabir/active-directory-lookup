package io.github.kdabir.adl.api;

import io.github.kdabir.adl.api.filters.SearchFilter;
import io.github.kdabir.adl.api.mapper.DefaultSearchResultMapper;
import io.github.kdabir.adl.api.mapper.SearchResultMapper;
import io.github.kdabir.adl.exceptions.ActiveDirectoryException;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kunal Dabir
 */
public class ActiveDirectorySearcher<T> {

    /**
     * To support of multi-search mode, stores the context so that bind is not 
     * required every time
     */
    protected LdapContext ldapContext = null;

    /**
     * searches will be performed based on this search if search base is not provided in the call
     */
    protected String searchBase = null;  //eg dc=test,dc=com

    /**
     * only these attributes will be loaded
     */
    protected List<String> returnedAttrs = null;

    /**
     * Mapper
     */
    protected SearchResultMapper<T> searchResultMapper = null;

    /**
     * Constructs the Searcher.  
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * 
     * @param searchBase
     */
    public ActiveDirectorySearcher(LdapContext ldapContext, String searchBase, SearchResultMapper<T> searchResultMapper) {
        this.ldapContext = ldapContext;
        this.searchBase = searchBase;
        this.searchResultMapper = searchResultMapper;
    }
    
    /**
     * Perform serach by filter in the search base.
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param searchFilter
     * @return
     * @throws io.github.kdabir.adl.exceptions.ActiveDirectoryException is something goes wrong
     */
    public List<T> search(SearchFilter searchFilter) {
        try {
            return searchResultMapper.mapResult(
                            ldapContext.search(searchBase, searchFilter.getFilter(), getSearchControls()));
        } catch (NamingException ex) {
            throw new ActiveDirectoryException("Exception while searching Active Directory", ex);
        }
    }

    private SearchControls getSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        if (returnedAttrs != null && returnedAttrs.size() > 0) {
            searchControls.setReturningAttributes(returnedAttrs.toArray(new String[0]));
        }
        return searchControls;
    }


    /**
     * to clean up
     */
    public void close() {
        if (ldapContext != null) {
            try {
                ldapContext.close();
            } catch (NamingException ex) {
                throw new ActiveDirectoryException("Exception while closing the context", ex);
            }
        }
    }


    public ActiveDirectorySearcher<T> withReturnedAttrs(List<String> returnedAttrs) {
        this.returnedAttrs = returnedAttrs;
        return this;
    }

    public SearchResultMapper getSearchResultMapper() {
        return searchResultMapper;
    }

    public List<String> getReturnedAttrs() {
        return returnedAttrs;
    }

}
