package io.github.kdabir.adl.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActiveDirectoryBinderTest {

    ActiveDirectoryBinder binder; // Subject under test

    @Mock
    LdapContextFactory ldapContextFactoryMock;

    @Mock
    ActiveDirectoryEnvironmentBuilder activeDirectoryEnvironmentBuilderMock;

    @Before
    public void setUp() throws Exception {
        binder = new ActiveDirectoryBinder(activeDirectoryEnvironmentBuilderMock, ldapContextFactoryMock);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testShouldBindToActiveDirectoryWithCorrectEnvironment() throws Exception {
        final LdapContext expectedLdapContext = mock(LdapContext.class);
        final Hashtable<String, String> env = (Hashtable<String, String>) mock(Hashtable.class);
        when(activeDirectoryEnvironmentBuilderMock.getActiveDirectoryEnvironment("a", "b", "c", "d")).thenReturn(env);
        when(ldapContextFactoryMock.getLdapContext(env)).thenReturn(expectedLdapContext);

        final LdapContext returnedLdapContext = binder.getLdapContext("a", "b", "c", "d");

        assertEquals(expectedLdapContext, returnedLdapContext);
    }

    @Test(expected = NamingException.class)
    public void shouldPropagateException() throws Exception {
        when(ldapContextFactoryMock.getLdapContext(Matchers.<Hashtable<String, String>>any())).thenThrow(new NamingException());

        binder.getLdapContext("a", "b", "c", "d");
    }
}
