package io.github.kdabir.adl.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchBaseGuesserTest {

    @Test
    public void testGuess() throws Exception {
        assertEquals("DC=EXAMPLE,DC=COM", new SearchBaseGuesser().guessFrom("example.com"));
    }
}