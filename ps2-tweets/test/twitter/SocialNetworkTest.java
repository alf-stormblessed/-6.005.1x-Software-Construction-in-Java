package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    private static final Tweet tweet3 = new Tweet(3, "alyssa", "is it reasonable @jorlto talk about rivest so much?", d1);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest talk in 30 minutes @candy #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "rivest talkfs in 30 minutes @carl #hype", d2);
    private static final Tweet tweet6 = new Tweet(1, "a_lyssp_", "RT @a_lyssp_: no friends :(", Instant.now());   
   
    private static final Tweet tweet7Caps = new Tweet(12, "BBITDIDDLE", "rivest talkfs in 30 minutes @CARL #hype", d2);
    private static final Tweet tweet7NoCaps = new Tweet(13, "bbitdiddle", "rivest talkfs in 30 minutes @carl #hype", d2); 

    private static final Tweet tweetBadUsername = new Tweet(11, ":alyssa", "RT @a_lyssp_ +  no friends :(", Instant.now());   
    
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    
    @Test
    public void testInfluencersMultipleUsersSomeSameFollowerCounts() {
        /*
         * 
         */
        Map<String, Set<String>> network = new HashMap<String, Set<String>>();
        network.put("a_lyssp_", new HashSet<String>());
        network.put("bbitdiddle", new HashSet<String>());
        network.put("alice", new HashSet<String>(Arrays.asList("bob", "a_lyssp_")));
        network.put("bob", new HashSet<String>(Arrays.asList("alice", "a_lyssp_")));
        
        List<String> canonicalUsers = Arrays.asList("a_lyssp_", "bbitdiddle", "alice", "bob");
        List<String> influencers = SocialNetwork.influencers(network);
        //System.out.println("influencers: " + influencers);
        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("list size", 4, influencers.size());
        assertTrue("incorrect user at index 0", influencers.get(0).toLowerCase().equals("a_lyssp_"));
        assertTrue(influencers.get(influencers.size() - 1).toLowerCase().equals("bbitdiddle"));
        for (String user: influencers) {
            assertTrue("missing expected user", canonicalUsers.contains(user.toLowerCase()));
        }
    }
    
    @Test
    public void testInfluencersMustBeCaseInsensitive() {
        /*
         * Usernames should be case insensitive.
         */
        Map<String, Set<String>> network = new HashMap<String, Set<String>>();
        network.put("bbitdiddle", new HashSet<String>(Arrays.asList("a_lyssp_")));
        network.put("A_LYSSP_", new HashSet<String>());
        
        List<String> influencers = SocialNetwork.influencers(network);
        //System.out.println(influencers);
        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("list size", 2, influencers.size());
        assertTrue("incorrect user at index 0", influencers.get(0).toLowerCase().equals("a_lyssp_"));
        assertTrue("incorrect user at index 1", influencers.get(1).toLowerCase().equals("bbitdiddle"));
    } 
    
    @Test
    public void testInfluencersTwoUsersOneFollowing() {
        /*
         * Single tweet with one mention: should be length one, with the person who has been
         * mentioned ranking higher than the other person.
         */
        Map<String, Set<String>> network = new HashMap<String, Set<String>>();
        network.put("bbitdiddle", new HashSet<String>(Arrays.asList("a_lyssp_")));

        //System.out.println(network);
            
        
        List<String> influencers = SocialNetwork.influencers(network);
        //System.out.println(influencers);
        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("list size", 2, influencers.size());
        assertTrue("incorrect user at index 0", influencers.get(0).toLowerCase().equals("a_lyssp_"));
        assertTrue("incorrect user at index 1", influencers.get(1).toLowerCase().equals("bbitdiddle"));
    }
    
    @Test
    public void testGuessFollowsGraphUserMentionsSelf() {
        /*
         * Users should not be inferred as following themselves.
         */
        Map<String, Set<String>> network = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
        //System.out.println("network " + network);
        if (!network.isEmpty()) {
            assertEquals("map size", 1, network.keySet().size());
            for (String user : network.keySet()) {
                assertFalse("unexpected user following self", network.get(user).contains(user.toLowerCase()));
            }
        }
    }

    @Test
    public void testGuessFollowsGraphCheckForNonExistingUsers() {
        /*
         * Users should not be inferred as following themselves.
         */
        Map<String, Set<String>> network = SocialNetwork.guessFollowsGraph(Arrays.asList(tweetBadUsername));
        System.out.println(network);

    }
    
    @Test
    public void testGuessFollowsGraphMustBeCaseInsensitive() {
        /*
         * Usernames should be case insensitive.
         */
        Map<String, Set<String>> networkCaps = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet7Caps));
        Map<String, Set<String>> networkNoCaps = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet7NoCaps));
        System.out.println(networkCaps);
        System.out.println(networkNoCaps);
        assertTrue("expected same network", networkCaps.equals(networkNoCaps));

    } 
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
