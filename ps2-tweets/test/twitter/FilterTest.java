package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T10:30:00Z");
    private static final Instant d3 = Instant.parse("2015-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest  in 30 minutes #hype", d2);

    private static final Tweet tweet3 = new Tweet(3, "alyssa", "rivest talk in 3dsfdsfdsfdsf0 minutes #hype", d3);
    
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "rivest talk in AriZoNa minutes #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "Arizona minutes #hype", d2);
    
    private static final Tweet tweet6 = new Tweet(6, "alyssa", "Obama minutes #hype", d3);
    private static final Tweet tweet7 = new Tweet(7, "Alyssa", "obama minutes #hype", d3);   
    
    private static final Tweet multipleSearchWords1 = new Tweet(0, "alyssa",
            "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet multipleSearchWords2 = new Tweet(1, "alyssa",
            "rivest talk in 32123 seconds #hype", d2);
    private static final Tweet multipleSearchWords3 = new Tweet(2, "BBitdiddle",
            "every day i'm hackin' it", d3);
    private static final Tweet multipleSearchWords4 = new Tweet(3, "_reAsOnEr",
            "i know Rivest is an author of CLRS, but who else??", d1);
    private static final Tweet multipleSearchWords5 = new Tweet(4, "reAsOnEr",
            "giving an optimization talk in 32-123 tomorrow", d2); 
    
    private static final List<Tweet> tweetsMultipleSearchWords = Collections.unmodifiableList(
            Arrays.asList(multipleSearchWords1, multipleSearchWords2, multipleSearchWords3, multipleSearchWords4, multipleSearchWords5));
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByEmpty() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");
        //System.out.println(Arrays.asList());
        assertTrue("expected empty", writtenBy.isEmpty());
        assertFalse("expected list not to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testWrittenByMultipleTweetsMultipleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        //System.out.println(writtenBy);
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1) && writtenBy.contains(tweet3));

    }
    @Test
    public void testWrittenByCaseSensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet6, tweet7), "alyssa");
        //System.out.println(writtenBy);
        assertEquals("expected 2 elements in the list", 2, writtenBy.size());
        assertTrue("expected list to contain tweets", writtenBy.contains(tweet6) && writtenBy.contains(tweet7));

    }   
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    
    @Test
    public void testInTimespanNotInTimespan() {
        Instant testStart = Instant.parse("2016-02-18T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-18T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());

    }
    
    @Test
    public void testInTimespanReturnsFullList() {
        Instant testStart = Instant.MIN;
        Instant testEnd = Instant.MAX;
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3,tweet2,tweet1), new Timespan(testStart, testEnd));
        //System.out.println(inTimespan);
        assertFalse("expected non empty list", inTimespan.isEmpty());
        assertEquals("expected 3 tweets", 3 , inTimespan.size());

    }    
    
    @Test
    public void testInTimespanExclusiveTimespan() {
        Instant testStart = d1;
        Instant testEnd = d2;
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3,tweet2,tweet1), new Timespan(testStart, testEnd));
        //System.out.println(inTimespan);
        assertFalse("expected non empty list", inTimespan.isEmpty());
        assertEquals("expected 2 tweets", 2 , inTimespan.size());

    }   
    @Test
    public void testInTimespanReturnsSomeNotAll() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T13:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3,tweet2,tweet1), new Timespan(testStart, testEnd));
        //System.out.println(inTimespan);
        assertFalse("expected non empty list", inTimespan.isEmpty());
        assertEquals("expected 2 tweets", 2 , inTimespan.size());
        assertTrue("expected tweet1", inTimespan.contains(tweet1));
        assertTrue("expected tweet2", inTimespan.contains(tweet2));
    }  
    

    @Test
    public void testInTimespanMultipleTweetsNoResults() {
        Instant testStart = Instant.parse("2015-02-18T09:00:00Z");
        Instant testEnd = Instant.parse("2015-02-18T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
        //assertTrue("expected list to contain no tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        //assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1)));
        //assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    @Test
    public void testContainingMultipleSearchWords() {
        /*
         * Testing multiple terms that occur in order. We should not see any
         * terms that match only a single term.
         */
        List<Tweet> filteredTweets = Filter.containing(tweetsMultipleSearchWords,
                Arrays.asList("optimization", "talk"));
        //System.out.println(filteredTweets);
        System.out.println(filteredTweets);
        assertFalse("expected non-empty list", filteredTweets.isEmpty());
        assertEquals("list size", 3, filteredTweets.size());

        assertTrue("missing expected tweet", filteredTweets.contains(multipleSearchWords1));
        assertTrue("missing expected tweet", filteredTweets.contains(multipleSearchWords2));
        assertTrue("missing expected tweet", filteredTweets.contains(multipleSearchWords5));
    }
    
    @Test
    public void testContainingAnyOfTwoWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk","rivest"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }    
    
    @Test
    public void testNotContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("arizona"));
        
        assertTrue("expected empty list", containing.isEmpty());
        //assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        //assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    @Test
    public void testContainingCaps() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet4, tweet5), Arrays.asList("arizona"));
        
        
        assertFalse("expected not to be empty", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet4, tweet5)));
        assertEquals("expected same order", 0, containing.indexOf(tweet4));
    }
    
    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet6, tweet7), Arrays.asList("obama"));
        List<Tweet> containing2 = Filter.containing(Arrays.asList(tweet6, tweet7), Arrays.asList("Obama"));        
        
        assertFalse("expected not to be empty", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.contains(tweet6));
        assertTrue("expected list to contain tweets", containing.contains(tweet7));
        assertEquals("expected same order", 0, containing.indexOf(tweet6));
        assertEquals("expected same order", 1, containing.indexOf(tweet7));
        
        assertFalse("expected not to be empty", containing2.isEmpty());
        assertTrue("expected list to contain tweets", containing2.contains(tweet6));
        assertTrue("expected list to contain tweets", containing2.contains(tweet7));
        assertEquals("expected same order", 0, containing2.indexOf(tweet6));
        assertEquals("expected same order", 1, containing2.indexOf(tweet7));
    }
    
    @Test
    public void testContainingReturnsWholeTweet() {

        
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet6), Arrays.asList("oba"));
        //System.out.println(inTimespan);
        assertTrue("expected empty list", containing.isEmpty());
        //assertEquals("expected 1 tweet", 1 , containing.size());
        //assertTrue("expected tweet1", containing.contains(tweet6));
        
        //assertEquals("expected same tweet",tweet6,containing.get(0));
        //assertTrue("expected same text",tweet6.getText().equals(containing.get(0).getText()));
    }  
    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
