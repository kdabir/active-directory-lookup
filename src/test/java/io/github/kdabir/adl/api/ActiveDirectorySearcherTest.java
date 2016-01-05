package io.github.kdabir.adl.api;

import io.github.kdabir.adl.api.filters.SearchFilter;
import io.github.kdabir.adl.api.mapper.DefaultSearchResultMapper;
import io.github.kdabir.adl.api.mapper.SearchResultMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActiveDirectorySearcherTest {


    final SearchFilter fakeSearchFilter = new SearchFilter() {
        @Override
        public String getFilter() {
            return "fakefilter";
        }
    };

    final ArrayList<Map<String, String>> expectedResult = new ArrayList<Map<String, String>>();
    final HashMap<String, String> user1 = new HashMap<String, String>();
    final HashMap<String, String> user2 = new HashMap<String, String>();


    @Before
    public void setUp() throws Exception {
        user1.put("name", "user1");
        user2.put("name", "user2");
        expectedResult.add(user1);
        expectedResult.add(user2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void searchShouldDelegateToLdapContextSearch() throws Exception {
        final LdapContext mockLdapContext = mock(LdapContext.class);
        final SearchResultMapper<Map<String,String>> mockSearchResultMapper = mock(SearchResultMapper.class);
        final String fakeTestBase = "dc=testbase";
        final ActiveDirectorySearcher<Map<String,String>> activeDirectorySearcher =
                new ActiveDirectorySearcher<Map<String,String>>(mockLdapContext, fakeTestBase, mockSearchResultMapper);
        final NamingEnumeration namingEnumeration = mock(NamingEnumeration.class);

        when(mockLdapContext.search(eq(fakeTestBase), eq("fakefilter"), any(SearchControls.class))).thenReturn(namingEnumeration);
        when(mockSearchResultMapper.mapResult(namingEnumeration)).thenReturn(expectedResult);

        final List<Map<String, String>> result = activeDirectorySearcher.search(fakeSearchFilter);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testClose() throws Exception {

    }

    @Test
    public void shouldTakeDefaultSearchResultMapper() throws Exception {
        assertThat(new SimpleActiveDirectorySearcher(null, null).getSearchResultMapper(), instanceOf(DefaultSearchResultMapper.class));
    }

    @Test
    public void testWithReturnedAttrs() throws Exception {

    }

    @Test
    public void testGetDefaultSearchResultMapper() throws Exception {

    }

    @Test
    public void testGetReturnedAttrs() throws Exception {

    }
}
