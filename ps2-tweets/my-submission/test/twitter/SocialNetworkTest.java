package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraph2followers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3,tweet4));
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertEquals("expected size 2", 2, followsGraph.size());
        assertTrue("expected alyssa key", followsGraph.containsKey("alyssa"));
        assertTrue("expected bbitdiddle key", followsGraph.containsKey("bbitdiddle"));
        assertTrue("alyssa follows jorlto", followsGraph.get("alyssa").contains("jorlto"));
        assertTrue("bbitdiddle follows candy", followsGraph.get("bbitdiddle").contains("candy")); 
        
        System.out.println("test followsgraph " + followsGraph);
        
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    
    @Test
    public void testInfluencersNotEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3,tweet4,tweet5));
        //System.out.println("test influencers " + followsGraph);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse("expected non-empty list", influencers.isEmpty());
        
        int count = 0;
        for (String key : followsGraph.keySet()){
            //System.out.println("size: " + followsGraph.get(key).size());
            if (count!=0 && (count - followsGraph.get(key).size() <0)){
                
                assert(false);
                
                }
            else{
                
                count = 0;
            }
                
               count += followsGraph.get(key).size();

            
        }
    }
    
    @Test
    public void testInfluencersDecreasingOrder() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3,tweet4,tweet5));
        //System.out.println("test influencers " + followsGraph);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        int count = 0;
        for (String key : followsGraph.keySet()){
            //System.out.println("size: " + followsGraph.get(key).size());
            if (count!=0 && (count - followsGraph.get(key).size() <0)){
                
                assert(false);
                
                }
            else{
                
                count = 0;
            }
                
               count += followsGraph.get(key).size();

            
        } 
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
