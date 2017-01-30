package twitter;

import static org.junit.Assert.*;

import java.awt.List;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    
    private static final Instant maxInstant = Instant.MAX;
    private static final Instant minInstant = Instant.MIN;
    
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "is it reasonable @jorlto talk about rivest so much?", maxInstant);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest talk in 30 minutes #hype", minInstant);
    
    private static final Tweet oneMentionedUser = new Tweet(5, "alyssa", "is it reasonable @jorlto talk about rivest so much?", maxInstant);
    private static final Tweet twoMentionedUser = new Tweet(6, "alyssa", "is it @anakin reasonable @jorlto talk about rivest so much?", maxInstant);
    
    private static final Tweet sameUserTwiceWithCaps = new Tweet(7, "alyssa", "is it @anakin reasonable @ANAKIN talk about rivest so much?", maxInstant);
    
    private static final Tweet invalidUsername = new Tweet(8, "alyssa", "this is the string @@.so much?", d1);
    
    private static final Tweet invalidUsername2 = new Tweet(9, "alyssa", "this is the123@ mark string @@.so much?", d1);
    
    private static final Tweet invalidUsername3 = new Tweet(10, "alyssa", "this is the123@mark string @@.so much?", d1);
    
    private static final Tweet validUsername = new Tweet(11, "alyssa", "this is the123 @mark. string @@.so much?", d1);
    
    private static final Tweet startingWithUsername = new Tweet(12, "alyssa", "@anakin reasonable talk about rivest much?", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testBoundaryInstants() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet4));

                
        assertEquals("expected start", minInstant, timespan.getStart());
        assertEquals("expected end", maxInstant, timespan.getEnd());
    }   
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetOneMentionedUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(oneMentionedUser));
        
        assertTrue("expected one user", mentionedUsers.contains("jorlto"));
    }

    
    @Test
    public void testGetTwoMentionedUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(twoMentionedUser));

        assertTrue("expected two users", mentionedUsers.contains("jorlto"));
        assertTrue("expected two users", mentionedUsers.contains("anakin"));
    }
    
    @Test
    public void testGetSameUserMentionedTwiceWithCaps() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(sameUserTwiceWithCaps));

        assertTrue("expected one user", mentionedUsers.contains("anakin"));
        assertTrue("expected one user", !mentionedUsers.contains("ANAKIN"));

    }
    
    @Test
    public void testGetSameUserMentionedInvalidUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername));

        assertTrue("expected no users", mentionedUsers.isEmpty());


    }
    
    @Test
    public void testGetSameUserMentionedInvalidUsername2() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername2));
        System.out.println("testGetSameUserMentionedInvalidUsername2 " + mentionedUsers);
        assertTrue("expected no users", mentionedUsers.isEmpty());


    }  
    
    @Test
    public void testGetSameUserMentionedInvalidUsername3() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername3));
        System.out.println("testGetSameUserMentionedInvalidUsername2 " + mentionedUsers);
        assertTrue("expected no users", mentionedUsers.isEmpty());


    }   
    
    @Test
    public void testGetSameUserMentionedValidUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(validUsername));

        assertFalse("expected 1 user", mentionedUsers.isEmpty());
        assertTrue("expected anakin", mentionedUsers.contains("mark"));


    }  
    
    @Test
    public void testGetSameUserMentionedstartingWithUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(startingWithUsername));

        assertTrue("expected anakin", mentionedUsers.contains("anakin"));
        assertEquals("expected size one", 1, mentionedUsers.size());


    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
