package twitterFeed;

import java.util.List;

import authentication.OAuth;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * 
 * @author Baohuy Ung
 * @version 1.0
 * 
 * Crawls through a public timeline and gathers up to 3200 of the most recent tweets.
 * A single call and only return up to 200 statuses so it may take up to 16 requests.
 *
 */
public class PublicTimeline {
		//start at the first page
		int pageIndex = 1;
		//paging set to 200 per page to get maximum possible
		Paging page = new Paging(1,200);
		//initialize a list to hold all statuses
		public List<Status> status = null;
		int tweetTotal = 0;
		String userID;
		boolean statusFlag = true;
		
		/**
		 * Constructor to set the User ID of the public timeline
		 * @param userID to gather from
		 */
		public PublicTimeline(String userID){
			this.userID = userID;
		}
		
		/**
		 * Beings collection of up to 3200 most recent statuses
		 * @return a list of all statuses
		 * @throws TwitterException UserID mismatch or rate limit exceeded
		 */
		public List<Status> getTimeline() throws TwitterException{
		Twitter newTimeLine = OAuth.authenticate();
		ResponseList<Status> temp = null;
		
		//page through each page and add all statues to the list
			while(statusFlag){			
				temp = newTimeLine.getUserTimeline(userID, page);
				if(temp.size() == 0){
					System.out.println("Maximum number of tweets gathered");
					statusFlag = false;
				}
				
				if(pageIndex==1 && statusFlag){
					status = temp;
					page.setPage(pageIndex++);
				}
				else if(statusFlag){
					status.addAll(temp);
					page.setPage(pageIndex++);
				}

				tweetTotal = status.size();
				System.out.println("Number of Tweets gathered: "+ tweetTotal);			
			}                                                
			return status;
		
		}
}
