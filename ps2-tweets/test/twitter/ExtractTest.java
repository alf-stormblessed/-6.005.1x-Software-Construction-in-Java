package twitter;

import static org.junit.Assert.*;

import java.awt.List;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet = new Tweet(15, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    
    private static final Instant maxInstant = Instant.MAX;
    private static final Instant minInstant = Instant.MIN;
    
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "is it reasonable @jorlto talk about rivest so much?", maxInstant);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest talk in 30 minutes #hype", minInstant);
    
    private static final Tweet oneMentionedUser = new Tweet(5, "alyssa", "is it reasonable @jorlto talk about rivers so much?", d1);
    private static final Tweet twoMentionedUser = new Tweet(6, "alyssa", "is it @anakin reasonable @jorlto talk about rivest so much?", d1);
    private static final Tweet oneMentionedUserWithCaps = new Tweet(15, "alyssa", "is it reasonable @jORLTO talk about rivers so much?", d1);
    
    private static final Tweet sameUserTwiceWithCaps = new Tweet(7, "alyssa", "is it @anakin reasonable @ANAKIN talk about rivest so much?", d1);
    
    private static final Tweet invalidUsername = new Tweet(8, "alyssa", "this is the string @@.so much?", d1);
    
    private static final Tweet invalidUsername2 = new Tweet(9, "alyssa", "this is the123@ mark string @@.so much?", d1);
    
    private static final Tweet invalidUsername3 = new Tweet(10, "alyssa", "this is the123@mark string @@.so much?", d1);
    
    private static final Tweet validUsername = new Tweet(11, "alyssa", "this is the123 @mark. string @@.so much?", d1);
    
    private static final Tweet startingWithUsername = new Tweet(12, "alyssa", "@anakin reasonable talk about rivest much?", d1);
    
    private static final Tweet OneMentionedUserPrime = new Tweet(13, "alyssa", "following text will be @mention", d1);
    
    private static final Tweet OneMentionedUserHyphenated = new Tweet(22, "alyssa", "following text will be @d-mention", d1);
    
    private static final Tweet InvalidCharactersFollowing1 = new Tweet(23, "alyssa", "@\u2026" , d1);

    private static final Tweet InvalidCharactersFollowing2 = new Tweet(24, "alyssa", "@!!!" , d1);
    
    private static final Tweet MultipleMentionsMultipleTweets = new Tweet(25, "alyssa", "@mention @othermention" , d1);
    
    private static final Tweet MultipleMentionsMultipleTweets2 = new Tweet(26, "alyssa", "@thirdMention" , d1);

    private static final Tweet SingleMentionsMultipleTweets = new Tweet(28, "alyssa", ". @mention" , d1);
    
    private static final Tweet SingleMentionsMultipleTweets2 = new Tweet(29, "alyssa", "@mention ." , d1);
  
    private static final Tweet SingleMentionsMultipleTweets3 = new Tweet(27, "alyssa", "@mention" , d1);
    
    //private static final Tweet IOBoundsMIN = new Tweet(14, "alyssa", "@anakin reasonable talk about rivest much?", minInstant.minusSeconds(1));
        
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
    public void testEmptylist() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertTrue ((timespan.getEnd().getEpochSecond() - timespan.getStart().getEpochSecond() == 0));

    } 
    
    @Test
    public void testOneTweetsSameTimespan() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet));
        assertTrue ((timespan.getEnd().getEpochSecond() - timespan.getStart().getEpochSecond() == 0));

    }       
    @Test
    public void testMultipleTweetsSameTimespan() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet,tweet1));
        assertTrue ((timespan.getEnd().getEpochSecond() - timespan.getStart().getEpochSecond() == 0));

    }    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetOneMentionedUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(oneMentionedUser));
        Set<String> mentionedUsersLowerCase = new HashSet<>();
        for (String s: mentionedUsers){
            mentionedUsersLowerCase.add(s.toLowerCase());
            }
        assertFalse("expected non empty", mentionedUsersLowerCase.isEmpty());
        //System.out.println(mentionedUsers);
        assertTrue("expected mentioned jorlto", mentionedUsersLowerCase.contains("jorlto"));
    }

    
    @Test
    public void testGetTwoMentionedUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(twoMentionedUser));
        Set<String> mentionedUsersLowerCase = new HashSet<>();
        for (String s: mentionedUsers){
            mentionedUsersLowerCase.add(s.toLowerCase());
            }
        assertTrue("expected two users, jorlto not there", mentionedUsersLowerCase.contains("jorlto"));
        assertTrue("expected two users, anakin not there", mentionedUsersLowerCase.contains("anakin"));
    }
    
    @Test
    public void testgetMentionedUserCaseInsensitive() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(oneMentionedUser));
        Set<String> mentionedUsers2 = Extract.getMentionedUsers(Arrays.asList(oneMentionedUserWithCaps));
        
        assertTrue(mentionedUsers.equals(mentionedUsers2));
        
   }    
    
    @Test
    public void testgetMentionedUserSingleMentionSingleTweet() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(OneMentionedUserPrime));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        for (String users: mentionedUsers){
        assertEquals("Incorrect user","mention",users.toLowerCase());}
        
   }   
    
    @Test
    public void testgetMentionedUserHyphenated() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(OneMentionedUserHyphenated));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        for (String users: mentionedUsers){
            assertEquals("Incorrect user","d-mention",users.toLowerCase());}
        
   }
    @Test
    public void testGetMentionedUsersMultipleMentionsMultipleTweets() {
        /*
         * Testing multiple mentions across multiple tweets.
         */
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(
                MultipleMentionsMultipleTweets,
                MultipleMentionsMultipleTweets2));
        Set<String> canonicalUsers = new HashSet<String>(Arrays.asList(
                "mention", "othermention", "thirdmention"));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        assertEquals("set size", 3, mentionedUsers.size());
        for (String user : mentionedUsers) {
            assertTrue("unexpected user in set", canonicalUsers.contains(user.toLowerCase()));
        }
    }
    
    @Test
    public void testGetMentionedUsersSingleMentionsMultipleTweets() {
        /*
         * Testing multiple mentions across multiple tweets.
         */
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(
                SingleMentionsMultipleTweets,
                SingleMentionsMultipleTweets2,
                SingleMentionsMultipleTweets3));
        Set<String> canonicalUsers = new HashSet<String>(
                Arrays.asList("mention"));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        for (String user : mentionedUsers) {
            assertTrue("unexpected user in set", canonicalUsers.contains(user.toLowerCase()));
        }

    }
    @Test
    public void testGetSameUserMentionedTwiceWithCaps() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(sameUserTwiceWithCaps));
        Set<String> mentionedUsersLowerCase = new HashSet<>();
        for (String s: mentionedUsers){
            mentionedUsersLowerCase.add(s.toLowerCase());
            }
        assertTrue("expected one user, anakin there", mentionedUsersLowerCase.contains("anakin"));
        assertEquals("expected one user", 1 , mentionedUsersLowerCase.size());

    }
    
    @Test
    public void testGetSameUserMentionedInvalidCharactersFollowing() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(InvalidCharactersFollowing1));
        Set<String> mentionedUsers2 = Extract.getMentionedUsers(Arrays.asList(InvalidCharactersFollowing2));
        assertTrue("expected empty list", mentionedUsers.isEmpty());
        assertTrue("expected empty list", mentionedUsers2.isEmpty());

    }   
    @Test
    public void testGetSameUserMentionedInvalidUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername));

        assertTrue("expected no users", mentionedUsers.isEmpty());


    }
    
    @Test
    public void testGetSameUserMentionedInvalidUsername2() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername2));
        //System.out.println("testGetSameUserMentionedInvalidUsername2 " + mentionedUsers);
        assertTrue("expected no users", mentionedUsers.isEmpty());


    }  
    
    @Test
    public void testGetSameUserMentionedInvalidUsername3() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidUsername3));
        //System.out.println("testGetSameUserMentionedInvalidUsername2 " + mentionedUsers);
        assertTrue("expected no users", mentionedUsers.isEmpty());


    }   
    
    @Test
    public void testGetSameUserMentionedValidUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(validUsername));
        Set<String> mentionedUsersLowerCase = new HashSet<>();
        for (String s: mentionedUsers){
            mentionedUsersLowerCase.add(s.toLowerCase());
            }
        assertFalse("expected 1 user", mentionedUsersLowerCase.isEmpty());
        assertTrue("expected mark", mentionedUsersLowerCase.contains("mark"));


    }  
    
    @Test
    public void testGetSameUserMentionedstartingWithUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(startingWithUsername));
        Set<String> mentionedUsersLowerCase = new HashSet<>();
        for (String s: mentionedUsers){
            mentionedUsersLowerCase.add(s.toLowerCase());
            }
        assertTrue("expected anakin", mentionedUsersLowerCase.contains("anakin"));
        assertEquals("expected size one", 1, mentionedUsersLowerCase.size());


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
