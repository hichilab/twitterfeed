package db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import org.h2.jdbc.JdbcSQLException;

import export.CSV;
import export.ConvoGraph;
import export.GeoExport;
import export.Gephi;
import export.GephiHash;
import export.TimeGraph;

import authentication.OAuth;

import tagGather.TagGather;
import twitter4j.*;
import twitterFeed.PublicTimeline;
import twitterFeed.Search;

/**
 * 
 * @author Baohuy Ung
 * @version 1.0
 * 
 * This is a intermediary class that handles connections with the database and executes commands from the outer classes.
 * It contains a list of all current databases (which could possible be removed when unifying data into a single database)
 * 
 * There are two different packages that this class unifies. The first step is to query the twitter severs for the data.
 * This utilizes twitterFeed classes, then the returned data is then sent to the DB classes which prepare the raw data for
 * insertion into the DB and inserts it.
 */

public class DB_Connect {

	//default username and pass for the admin
	final static String user = "sa";
	final static String pass = "";
	
	//list of all current databases
	final static String DbUrl = "jdbc:h2:tcp://localhost/~/twitterDB";
	final static String test = "jdbc:h2:tcp://localhost/~/test";
	final static String SenC = "jdbc:h2:tcp://localhost/~/SenateCandidates";
	final static String natCon = "jdbc:h2:tcp://localhost/~/NationalConventions";
	final static String PreDeb1 = "jdbc:h2:tcp://localhost/~/PresidentialDebate1";
	final static String HITsunami = "jdbc:h2:tcp://localhost/~/HITsunami";
	final static String DB12_11_08 = "jdbc:h2:tcp://localhost/~/DB12_11_08";
	final static String HISenOG = "jdbc:h2:tcp://localhost/~/HISen_OnGoing";
	
	//constant to switch databases
	final static String DB = HISenOG;

	/**
	 * User timeline scraper.
	 * 
	 * Scraps a user's lineline for the most recent 3200 tweets and also inserts their profile into the database.
	 * 
	 * @param u is a userID parameter that is passed in when adding a new user, it will create a timeline based on the user's handle
	 * @throws TwitterException thrown when there is no valid userID passed in u
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 */
	public static void addUser(String u) throws TwitterException, ClassNotFoundException, SQLException {

		//create a timeline from the user's name
		PublicTimeline pt = new PublicTimeline(u);
		pt.getTimeline();

		//connect to the database for insertion
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		
		//call the user/status building classes that will transform and insert the data into the DB
		DB_Interface.insertUser(conn, pt.status.get(0).getUser());
		DB_Interface.insertStatus(conn, pt);
		
		//close all connections when a transaction is completed
		conn.close();

	}

	/**
	 * Search API Connector
	 * 
	 * This connector builds a search query and submits a query to the twitter API. It should be noted that this uses a 1-to-1 
	 * rate limit use at the moment. The limit of a search query is 1500 tweets or 7 days. But it takes 1 request for each insert
	 * due to the differences in the data structure of a status and a tweet returned through a search.
	 * 
	 * @param q search query to be passed into the Search API
	 * @throws TwitterException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void query(String q) throws TwitterException, SQLException, ClassNotFoundException {

		//the search object returns a list of all the tweets from the query string q
		Search newQuery = new Search(q);
		
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		
		//authentication allows a user to have up to 350 requests per hour over the normal 150
		Twitter a = OAuth.authenticate();

		try {
			ArrayList<Long> sid = new ArrayList<Long>();
			for (Tweet tweets : newQuery.statusList) {
				long statusID = tweets.getId();
				//this checks for redudant statuses then inserts new ones into the DB
				if (!(DB_Interface.exist_Status(statusID, conn))) {
					System.out.println("Preparin new insert of status "
							+ statusID);
					sid.add(statusID);
					
					/* a.showStatus transforms a query tweet into a normal status this takes up a rate limit
					 * this is a difficult problem to address because scraped tweets and streamed tweets are
					 * formatted as a normal status, but only queried tweets are structured this way
					 */
					DB_Interface.insertTweet(conn, a.showStatus(statusID));
				} else {
					System.out.println("Status Already exists!!!!");
				}
			}
		} catch (JdbcSQLException e) {
			e.printStackTrace();
		}
		
		conn.close();

	}

	/**
	 * Connection between the streaming API to add a status into the DB.
	 * 
	 * @param s the status to be inserted
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void trackerAdd(Status s) throws ClassNotFoundException, SQLException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		
		DB_Interface.insertStatus(conn, s);
		conn.close();
	}
	
	/**
	 * Connection between the streaming API to add a user into the DB.
	 * 
	 * A user is added into the DB when they are added to the tracking list
	 * 
	 * @param id the userID of the user to be added into the DB
	 * @throws SQLException invalid inserts of data
	 * @throws ClassNotFoundException H2 not started
	 * @throws TwitterException there is insufficient requests left
	 */
	public static void trackerUserAdd(long id) throws TwitterException, ClassNotFoundException, SQLException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		
		if(!(DB_Interface.exist_User(id, conn))){
			DB_Interface.insertUser(conn, OAuth.authenticate().showUser(id));
		}
		else{
			System.out.println("User already exists in database");
		}
		
	}
	
	/**
	 * Queries the database for all tweets. This is to view it from the UI.
	 * 
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 */
	public static String view() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		String displayText = Display.run(conn);
		conn.close();
		return displayText;
	}
	
	/**
	 * Exports all the data in the tweets table of the data base. (Raw Export of Data)
	 * 
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 * @throws IOException when the specified output file is not found
	 */
	public static void csvMake() throws ClassNotFoundException, SQLException, IOException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		CSV newFile = new CSV();
		newFile.make(conn);
		conn.close();
	}
	
	
	
	/**
	 * Gathers co-related tags based on a given tag.
	 * 
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 */
	public static void hashGather() throws SQLException, ClassNotFoundException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		TagGather tg = new TagGather(conn, "usa");
		conn.close();
		
	}

	/**
	 * Gephi test export tool, exports each entitie in the table as a node.
	 * 
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 * @throws IOExpcetion
	 */
	public static void gExport() throws SQLException, ClassNotFoundException, IOException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		Gephi g = new Gephi(conn);
		g.gpMake();
		conn.close();
	}
	
	/**
	 * Gephi hashtag co-occurrence export.
	 * 
	 * @throws ClassNotFoundException when connecting to the DB if H2 is not running
	 * @throws SQLException for invalid SQL queries
	 * @throws IOExcetion when specified write location cannot be found
	 */
	public static void gHash() throws SQLException, ClassNotFoundException, IOException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		GephiHash tg = new GephiHash(conn);
		System.out.println("Making initial list");
		tg.init();
		System.out.println("initiliazation finished... Makeing vertex list");
		tg.writeVertecies("/Users/Brian/Desktop/DB12_11_08_Vertex.csv");
		System.out.println("vertextList finished... creating edge list");
		tg.edgeMake("/Users/Brian/Desktop/DB12_11_08_Edge.csv");
		System.out.println("edge list finished");
		tg.close();
		conn.close();
		
	}
	
	/**
	 * timeLapse raw export for data with timestamp consideration. Needs to be expanded as the granularity is too small.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void timeExport() throws ClassNotFoundException, SQLException, IOException{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		TimeGraph tg = new TimeGraph(conn);
		tg.pointList();
		tg.export("/Users/Brian/Desktop/PresDeb1_time.csv");
	}
	
	/**
	 * Exported based on geo tags, tags are converted from string into long and lat.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void geoExport() throws ClassNotFoundException, SQLException, IOException{
		
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		GeoExport ge = new GeoExport(conn, "/Users/Brian/Desktop/PreDeb1_Geo.csv", "SELECT * FROM TWEETS WHERE GEOLOCATION IS NOT NULL");
		ge.geoMake();
		conn.close();
		
	}
	
	/**
	 * Two types of expoets for conversations. Retweets and Replies.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void convoExport() throws ClassNotFoundException, SQLException, IOException{
		
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(DB, user, pass);
		ConvoGraph rt = new ConvoGraph(conn, "HISenOG", "/Users/Brian/Desktop/");
		rt.createNodesRT();
		rt.createRT();
		rt.nodeWrite("RT");
		ConvoGraph rp = new ConvoGraph(conn, "HISenOG", "/Users/Brian/Desktop/");
		rp.createNodesRP();
		rp.createReply();
		rp.nodeWrite("RP");
	}
	
}
