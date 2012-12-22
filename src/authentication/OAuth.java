package authentication;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * 
 * @author Baohuy Ung
 * 
 * The authentication package allows the app to verify a user. This is similar to logging in.
 * Being authenticated provides access to a lot of options, but currently all authentication is 
 * through my own account. 
 *
 *Future Plans: Allow a user to login with their own ID, I believe this will allow multiple users to gather information using different streams
 */
public class OAuth {
	
	static Twitter tw = new TwitterFactory().getInstance();
	static TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	
	/**
	 * Gives users an authenticated instance of the Twitter class to access the API
	 * @return an authenticated twitter instance use to call parts of the api
	 * @throws TwitterException
	 */
	public static Twitter authenticate() throws TwitterException {
		
		/* This is the access token for a TwitterID created for this project
		 * one suggestion is to create a way for users to authenticate through their own ID and store that into the DB
		 * like making a DB ID that you can use when gathering data 
		 */
		
		AccessToken at = new AccessToken(
				"349607669-Fx3F1RJ0f2c7AjxuLojGETTyXuvR3V2CXKkC1owP",
				"KPMKDFP7QZyqcflyKxUGnVcIbPpgbfVquS69eeA7Qw");

		//this is the a secret token that is part of the app registered to twitter
		try{
			tw.setOAuthConsumer("0kNReK9NrqfFVobwrVYSxQ",
					"MUShAkXosqxQxpzQ3k08MXEnXYVpLtGTd9JBFOoNg");
			tw.setOAuthAccessToken(at);
		}
		catch (IllegalStateException e) {
			//token already set
			return tw;
		}
		
			return tw;
	}
	/**
	 * Gives users an authenticated instance of the streaming class to access the API
	 * @return an authenticated instance of the streaming class API
	 * @throws TwitterException
	 */
	public static TwitterStream streamAuthenticate() throws TwitterException {
		AccessToken at = new AccessToken(
				"349607669-Fx3F1RJ0f2c7AjxuLojGETTyXuvR3V2CXKkC1owP",
				"KPMKDFP7QZyqcflyKxUGnVcIbPpgbfVquS69eeA7Qw");


		try{
			twitterStream.setOAuthConsumer("0kNReK9NrqfFVobwrVYSxQ",
					"MUShAkXosqxQxpzQ3k08MXEnXYVpLtGTd9JBFOoNg");
			twitterStream.setOAuthAccessToken(at);
		}
		catch (IllegalStateException e) {
			//token already set
			return twitterStream;
		}
		return twitterStream;
	}
}
