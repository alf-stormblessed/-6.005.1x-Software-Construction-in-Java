package twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {

        Instant start = Instant.MAX;
        Instant end = Instant.MIN;
        
        for (Tweet tweet : tweets){
           
                if ( start.compareTo(tweet.getTimestamp()) > 0){//if start > timestamp tweet actual
                      start = tweet.getTimestamp();
                      }
                
                if (end.compareTo(tweet.getTimestamp()) < 0){
                    end = tweet.getTimestamp();
                    }
        }
        
        if (start == Instant.MAX && end == Instant.MIN) //that means the initial boundaries haven't changed
            return new Timespan(Instant.MIN,Instant.MIN);
        else
            return new Timespan(start,end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
            
        Set<String> userMentionsSet = new HashSet<String>();
        
        boolean initCount = false;
        String username = new String();
        
        
        for (Tweet tweet : tweets){
            
            String temp = tweet.getText();

            if (temp.contains("@")){

               for (int i = 0; i<temp.length(); i++){

                   if (initCount){
                       if (((temp.charAt(i)>=48 && temp.charAt(i)<=57)     || //current char is a number
                               (temp.charAt(i)>=65 && temp.charAt(i)<=90)  || // or is a capital letter
                               (temp.charAt(i)>=97 && temp.charAt(i)<=122) || // or is a lower letter
                               (temp.charAt(i)==45 || temp.charAt(i)==95)  ))  //or it is a hyphen or underscore
                               
                       {   
                           
                           username += Character.toLowerCase(temp.charAt(i));
                           
                           if (i == temp.length()-1){
                               
                               if (!userMentionsSet.contains(username)){
                                   
                                   userMentionsSet.add(username.toLowerCase());


                               }
                               username = new String();
                               initCount = false;
                           }

                       }
                       
                       else{
                           
                           
                           initCount = false;
                           if (!username.isEmpty()){
                               
                               if (!userMentionsSet.contains(username)){
                                   
                                   userMentionsSet.add(username.toLowerCase());
                                   
                                   }
                               username = new String();
                               }
                               
                           }
                       }
                                   
                   if (temp.charAt(i) == '@'){
                       if (i > 0){
                           
                           if (!(temp.charAt(i-1)>=48 && temp.charAt(i-1)<=57) &&//previous char is not a number
                               !(temp.charAt(i-1)>=65 && temp.charAt(i-1)<=90) &&// and is not a capital letter
                               !(temp.charAt(i-1)>=97 && temp.charAt(i-1)<=122)  // and is not a lower letter
                               ){

                               initCount = true;
                               
                               }
                           
                           
                           }
                       
                       else if (i==0){
                           
                           initCount = true;
                           
                       }
                   }
                   
                   
                   }
              
                   
               
            }
                
        }
        return userMentionsSet;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
