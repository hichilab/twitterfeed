package twitterFeed;

import twitter4j.HashtagEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import authentication.OAuth;

/**
 * This is a test class that finds a specific status to verify the collection from the DB and twitter
 * Doesn't do much, but I will leave it in here as an example
 * This example shows that data collected may be malformed but matches twitter because users
 * may by pass the 140 character limit when retweeting and replying to a chain as it appends @RT and @user 
 * 
 * @author Baohuy
 *
 */
public class tweetFinder {
	public static void main(String[] args) throws TwitterException{
		
		Twitter t = OAuth.authenticate();
		//status ID
		long temp = 265522995454414848L;
		//in this example I want to print out all the hashtags from that specific tag
	    for(HashtagEntity a : t.showStatus(temp).getHashtagEntities()){
	    	System.out.println(a.getText());
	    }
	
		
	}
}
