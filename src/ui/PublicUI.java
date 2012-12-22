package ui;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.applet.*;
import java.io.IOException;
import java.sql.SQLException;
import db.DB_Connect;
import authentication.OAuth;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterFeed.PublicStream;

/**
 * User Interface Class
 * @author Baohuy Ung
 * @version 1.1
 * Changes include restructuring the different views into their own classes. 
 * There is one main view, and you can open more on top of it.
 *
 */
public class PublicUI extends Applet {

	private static final long serialVersionUID = 1L;
	//variables that are used throughout the interface
	private static Label rateLimit = null;
	private static PublicStream stream;
	public static String queryString = "";
	public static Twitter aTwitter = null;
	
	
	public static void main(String[] args) throws TwitterException {
		//creates the main view
		f1Init();
	}
	
	/**
	 * Initializes the main view along with the buttons associateed. 
	 * DB view and help open on top of this view.
	 */
	public static void f1Init(){
		
		//start an instance of the public stream
		try {
			stream = new PublicStream(OAuth.streamAuthenticate());
			stream.startStream();
		} catch (TwitterException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		final Frame f = new Frame("Twitter Queries");
		f.setLayout(new GridBagLayout());

		// allows the panel buttons from a brower to be used
		f.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		try {
			rateLimit = new Label("Rate Limit: "+ OAuth.authenticate().getRateLimitStatus().getRemainingHits());
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//text field used for user entry of IDs, search terms and tags
		final TextField text = new TextField(30);
		GridBagConstraints textF = new GridBagConstraints();
		textF.gridx = 0;
		textF.gridy = 0;
		textF.gridwidth = 2;
		textF.fill = GridBagConstraints.HORIZONTAL;
		textF.weightx = 0.5;
		f.add(text, textF);
		final TextArea display = new TextArea(
				"Enter a user ID above and click \"add user.\" \n You may also enter a sql query.");
		GridBagConstraints textA = new GridBagConstraints();
		textA.fill = GridBagConstraints.HORIZONTAL;
		textA.gridx = 0;
		textA.gridy = 1;
		textA.gridheight = 2;
		textA.gridwidth = 2;
		display.setEditable(false);
		f.add(display, textA);

		//panel to separate the submit buttons
		Panel submitButtons = new Panel();
		submitButtons.setLayout(new GridBagLayout());
		GridBagConstraints submitButton = new GridBagConstraints();
		submitButton.gridx = 3;
		submitButton.gridy = 0;
		submitButton.fill = GridBagConstraints.HORIZONTAL;
		f.add(submitButtons, submitButton);

		
		//add user button allows to scraping of a public timeline
		Button submit = new Button("Add User");
		GridBagConstraints sub = new GridBagConstraints();
		sub.gridx = 0;
		sub.gridy = 0;
		sub.fill = GridBagConstraints.HORIZONTAL;
		submit.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				queryString = text.getText();
				display.setText("querying new user: " + queryString);
				try {
					DB_Connect.addUser(queryString);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					display.setText(queryString
							+ " does not exist, please enter a valid user.");
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					// display.append("\n"+(new
					// TwitterFactory().getInstance().getRateLimitStatus().getRemainingHits()));
					rateLimit.setText(("Rate Limit: " + OAuth.authenticate().getRateLimitStatus()
							.getRemainingHits()));
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				display.append("\n update completed.");

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		submitButtons.add(submit, sub);

		
		//allows the use of the search API
		Button query = new Button("Search");
		GridBagConstraints queryButton = new GridBagConstraints();
		queryButton.gridx = 0;
		queryButton.gridy = 1;
		queryButton.fill = GridBagConstraints.HORIZONTAL;
		query.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				queryString = text.getText();
				display.setText("querying for keywords: " + queryString);
				try {
					DB_Connect.query(queryString);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					display.setText(queryString + " \n Error!");
					e.printStackTrace();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				display.append("\n update completed.");

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		submitButtons.add(query, queryButton);

		//opens up the DB view ontop of the current view
		final Button dbView = new Button("DB View");
		GridBagConstraints dbv = new GridBagConstraints();
		dbv.gridx = 3;
		dbv.gridy = 2;
		dbv.weightx = 0.5;
		dbv.fill = GridBagConstraints.HORIZONTAL;
		f.add(dbView, dbv);
		dbView.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//f.setVisible(false);
				f2Init();
			

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		//this panel is for the streaming API
		final Panel tracker = new Panel();
		tracker.setLayout(new GridBagLayout());
		GridBagConstraints trackerConstraints = new GridBagConstraints();
		trackerConstraints.gridx = 3;
		trackerConstraints.gridy = 1;
		trackerConstraints.fill = GridBagConstraints.HORIZONTAL;
		f.add(tracker, trackerConstraints);

		
		// adds a user to the track list
		final Button trackUser = new Button("Track User");
		GridBagConstraints userTracker = new GridBagConstraints();
		userTracker.gridx = 0;
		userTracker.gridy = 0;
		userTracker.fill = GridBagConstraints.HORIZONTAL;
		tracker.add(trackUser, userTracker);
		trackUser.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				try {
					//add user by ID
					stream.addUser(new Long(text.getText()));
				} catch (NumberFormatException e) {
					
					try {
						//if not an long ID then add by screen name
						stream.addUser(OAuth.authenticate().showUser(text.getText()).getId());
					} catch (TwitterException e1) {
						System.out.println("User not found");
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				display.setText(stream.userToString());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		//adds a keyword to the track list, replacing all new line and return characters
		final Button trackKeyword = new Button("Track Keyword");
		GridBagConstraints keywordTracker = new GridBagConstraints();
		keywordTracker.gridx = 0;
		keywordTracker.gridy = 1;
		keywordTracker.fill = GridBagConstraints.HORIZONTAL;
		tracker.add(trackKeyword, keywordTracker);
		trackKeyword.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				String temp = text.getText().replace("\n", "").replace("\r", "");
				System.out.println("Adding to track list: "+ temp);
				stream.addSearch(text.getText());
				display.setText(stream.searchToString());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
			
		// add the rate limit label
		GridBagConstraints rateLabel = new GridBagConstraints();
		rateLabel.gridx = 0;
		rateLabel.gridy = 4;
		tracker.add(rateLimit, rateLabel);

		//Updates the tracker by refreshing the stream
		Button rateRefresh = new Button("Update Tracker");
		GridBagConstraints rr = new GridBagConstraints();
		rr.gridx = 0;
		rr.gridy = 3;
		tracker.add(rateRefresh, rr);
		rateRefresh.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					stream.refresh();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		//displays the help menu to see what the buttons do
		final Button helpButton = new Button ("help");
		GridBagConstraints hb = new GridBagConstraints();
		hb.gridx = 0;
		hb.gridy = 5;
		helpButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				helpPanel();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		tracker.add(helpButton, hb);
		// display first frame
		f.pack();
		f.setSize(700, 255);
		f.setVisible(true);
		
	}
	
	
	/**
	 * Initializes the second fram "DB view". Close by clicking the close or "default view"
	 */
	public static void f2Init(){
		//creates the frame layout
		final Frame f2 = new Frame("Database View");
		f2.setLayout(new GridBagLayout());
		f2.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	/* ---------------------------------Datebase view ----------------------------------*/
		
		//text area for display
		final TextArea queryDisplay = new TextArea();
		
		//closes the current DB view
		final Button defaultView = new Button("Default View");
		GridBagConstraints dv = new GridBagConstraints();
		dv.gridx = 3;
		dv.gridy = 2;
		dv.weightx = 0.5;
		defaultView.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				f2.setVisible(false);
				//f1Init();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		f2.add(defaultView, dv);

		
		//button to view the tweets table
		final Button displayTable = new Button("View Table");
		GridBagConstraints dt = new GridBagConstraints();
		dt.gridx = 3;
		dt.gridy = 0;
		dt.weightx = 0.5;
		displayTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					queryDisplay.setText(DB_Connect.view());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		f2.add(displayTable, dt);


		//QueryDisplay
		GridBagConstraints qd = new GridBagConstraints();
		qd.fill = GridBagConstraints.HORIZONTAL;
		qd.gridheight = 2;
		qd.gridx = 0;
		qd.gridy = 2;
		qd.gridwidth = 2;
		queryDisplay.setEditable(false);
		f2.add(queryDisplay, qd);
		
		//this isn't used as there aren't enough tables, only tweets and users
		final Choice dBox1 = new Choice();
		GridBagConstraints db1 = new GridBagConstraints();
		db1.gridx = 0;
		db1.gridy = 0;
		db1.weightx = 0.5;
		db1.fill = GridBagConstraints.HORIZONTAL;
		dBox1.addItem("Tweets");
		dBox1.addItem("User");
		dBox1.addItem("Hashtag");
		f2.add(dBox1, db1);
		
		//used to enter in sql queries to filter table, isn't used
		final TextField sqlText = new TextField();
		GridBagConstraints st = new GridBagConstraints();
		st.gridx = 0;
		st.gridy = 1;
		//st.gridwidth = 2;
		st.fill = GridBagConstraints.HORIZONTAL;
		f2.add(sqlText, st);

		//Export raw data only
		final Button exportButton = new Button("Export");
		GridBagConstraints export = new GridBagConstraints();
		export.gridx = 3;
		export.gridy = 3;
		export.fill = GridBagConstraints.HORIZONTAL;
		f2.add(exportButton, export);
		exportButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					//calls the main DB class to use the export modules
					DB_Connect.csvMake();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		//the query button should let you view the table with the sql query
		//isn't used
		final Button DBQuery = new Button("Query");
		GridBagConstraints queryConstraints = new GridBagConstraints();
		queryConstraints.gridx = 3;
		queryConstraints.gridy = 1;
		queryConstraints.fill = GridBagConstraints.HORIZONTAL;
		f2.add(DBQuery, queryConstraints);
		DBQuery.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		f2.pack();
		f2.setSize(700, 255);
		f2.setVisible(true);
	}
	
	/**
	 * This method display the help menu explaining the collection buttons
	 */
	public static void helpPanel(){
		final Frame help = new Frame ("Help");
		help.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				help.setVisible(false);
			}
		});
		TextArea ta = new TextArea();
		
		
		String helpText = "Add User: Gather tweets for a specific user name once. \n\n" +
				"Search: Gather tweets based on a twitter search. \n\n" +
				"Track User: Schedule a recurring gathering of user tweets based on a set interval \n\n"+
				"Track Search: Schedule a recurring search for keywords set on an interval \n\n" +
				"Rate Limit: Twitter is queried multiple times for every action and is limited to a set number per hour,\n" +
				"		 you may not get any immediate responses if the rate is too low. \n\n" +
				"DB View: Switching to this view will allow the display of current tables in the database";
		
		
		ta.setText(helpText);
		help.add(ta);
		
		help.pack();
		help.setSize(655,235);
		help.setVisible(true);
	}


}