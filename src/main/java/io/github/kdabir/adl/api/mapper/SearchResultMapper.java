package io.github.kdabir.adl.api.mapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface SearchResultMapper<T> {
    List<T> mapResult(NamingEnumeration<SearchResult> namingEnumeration) throws NamingException;
}
