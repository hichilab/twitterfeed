package twitterFeed;

import java.util.List;

import authentication.OAuth;


import twitter4j.*;
/**
 * 
 * @author Baohuy Ung
 * @version 1.0
 * 
 * Uses the search API to query twitter with search terms. Similar to using the search bar in twitter.
 * Twitter imposes a limit of 1500 most recent tweets or up to 1 week back.
 * A single search returns up to 100-200 results per page but requires each to be converted into a status.
 * 
 */
public class Search {

	public List<Tweet> statusList;
	private boolean searchFlag = true;
	
	/**
	 * Sets the query for the search
	 * @param query search query
	 * @throws TwitterException there's an error in the twitter connection, no internet connect or rate limit exceeded
	 */
	public Search(String query) throws TwitterException {
		statusList = getResults(query);
	}

	/**
	 * Gets all results of a search in a list of tweets, this is raw tweets and must be converted into a status
	 * @param q the query string
	 * @return a list of all tweets returned
	 * @throws TwitterException
	 */
	public List<Tweet> getResults(String q) throws TwitterException {
		Twitter searchQuery = OAuth.authenticate();
		Query newQuery = new Query(q);
		int pageIndex = 1;
		//this sets the paging to return the maximum number of tweets search query
		//you may get 1500 results and have to go through each page to get all tweets
		newQuery.setRpp(200);
		List<Tweet> temp = null;
		QueryResult result = null;
		try {
			while (searchFlag) {
				newQuery.setPage(pageIndex);
				result = searchQuery.search(newQuery);
				if (!(result.getTweets().isEmpty()) && pageIndex == 1) {
					temp = result.getTweets();
					pageIndex++;
				} else if (!(result.getTweets().isEmpty()) && pageIndex > 1) {
					temp.addAll(result.getTweets());
					pageIndex++; //advances through each page to get all tweets
				} else if (result.getTweets().isEmpty()) {
					searchFlag = false; //checks if there are no more tweets to be gathered
				}
				System.out.println("Tweets Gathered: "+ temp.size());
			}
		} catch (TwitterException e) {
			//rate limit will be exhuast very fast using this, you may want to create a timer to track again 
			System.out.println("Rate Limit Exceeded");
			e.printStackTrace();
		}
		System.out.println("Total Search Results: "+temp.size());
		return temp;
	}

}
