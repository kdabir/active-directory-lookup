package io.github.kdabir.adl.api;

import org.junit.Test;

import javax.naming.NamingEnumeration;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchResultMapperTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldMapResultIntoListOfMaps() throws Exception {
        final BasicAttributes basicAttributes1 = createAttributes("sr1", 2);
        final BasicAttributes basicAttributes2 = createAttributes("sr2", 3);

        final SearchResult searchResult1 = mock(SearchResult.class);
        final SearchResult searchResult2 = mock(SearchResult.class);
        when(searchResult1.getAttributes()).thenReturn(basicAttributes1);
        when(searchResult2.getAttributes()).thenReturn(basicAttributes2);

        NamingEnumeration<SearchResult> namingEnumeration = mock(NamingEnumeration.class);
        when(namingEnumeration.hasMoreElements()).thenReturn(true, true, false);
        when(namingEnumeration.next()).thenReturn(searchResult1, searchResult2);

        SearchResultMapper mapper = new SearchResultMapper();

        final List<Map<String,String>> results = mapper.mapResult(namingEnumeration);

        assertEquals(2, results.size());
        assertEquals("sr1val1",results.get(0).get("sr1key1"));
        assertEquals("sr2val2", results.get(1).get("sr2key2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnEmptyListIfNoResults() throws Exception {
        NamingEnumeration<SearchResult> namingEnumeration = mock(NamingEnumeration.class);
        when(namingEnumeration.hasMoreElements()).thenReturn(false);

        SearchResultMapper mapper = new SearchResultMapper();

        final List<Map<String,String>> results = mapper.mapResult(namingEnumeration);

        assertEquals(0, results.size());
    }

    private BasicAttributes createAttributes(String prefix, int vals) {
        final BasicAttributes basicAttributes = new BasicAttributes();
        for (int i = 1; i <= vals; i++) {
            basicAttributes.put(prefix + "key" + i, prefix + "val" + i);
        }
        return basicAttributes;
    }
}
