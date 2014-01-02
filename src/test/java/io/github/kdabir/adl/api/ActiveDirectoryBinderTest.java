package io.github.kdabir.adl.api;

import io.github.kdabir.adl.exceptions.ActiveDirectoryException;
import io.github.kdabir.adl.exceptions.BadCredentialsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
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

    @Test
    public void shouldWrapNamingException() throws Exception {
        final NamingException namingException = new NamingException();
        when(ldapContextFactoryMock.getLdapContext(Matchers.<Hashtable<String, String>>any())).thenThrow(namingException);

        try {
            binder.getLdapContext("a", "b", "c", "d");
        } catch (ActiveDirectoryException actual) {
            assertEquals(namingException, actual.getCause());
        }
    }

    @Test
    public void shouldWrapCommunicationException() throws Exception {
        final CommunicationException communicationException = new CommunicationException();
        when(ldapContextFactoryMock.getLdapContext(Matchers.<Hashtable<String, String>>any())).thenThrow(communicationException);

        try {
            binder.getLdapContext("a", "b", "c", "d");
        } catch (ActiveDirectoryException actual) {
            assertEquals(communicationException, actual.getCause());
        }
    }

    @Test
    public void shouldThrowBadCredentialsExceptionWithWrappedAuthenticationException() throws Exception {
        final AuthenticationException authenticationException = new AuthenticationException();
        when(ldapContextFactoryMock.getLdapContext(Matchers.<Hashtable<String, String>>any())).thenThrow(authenticationException);

        try {
            binder.getLdapContext("a", "b", "c", "d");
        } catch (BadCredentialsException actual) {
            assertEquals(authenticationException, actual.getCause());
        }
    }
}
