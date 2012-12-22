TwitterFeed
Author - Baohuy Ung
Version - 1.0
-------------------------------
SQL Statements
View the SQL Statements to look at the structure of a status and User profile that is stored in the DB. 

Packages:

* authentication : contains class for user authentication
* db : contains database write classes
* export : contains export modules for graphic representation of database
* tagGather: example tool for hashtag popularity ranking
* twitterFeed: Twitter classes that using Twiiter4J for information retrieval from servers
* ui: user interface package

Startup
------------------------------
To collect data the H2 library must be running before the TwitterFeed system can being collecting data. Run the h2.jar file located in the "h2/bin". You can now start the user interface for TwitterFeed.

Notes: I suggest setting up a test database to figure out how the data retrieval works, you can create one by using the H2 browser and typing in a new name on connect, if no existing DB is found then a new one is created automatically.

Authentication Keys
-----------------------------
Authentication is a two step process, it requires a registered app which provides a two keys. The Consumer secret and Consumer key. 

Consumer key:	 0kNReK9NrqfFVobwrVYSxQ
Consumer secret: MUShAkXosqxQxpzQ3k08MXEnXYVpLtGTd9JBFOoNg

These two keys are required to authenticate the application, you must also authenticate a user by requesting an Access token and token secret. This allows the application to make requests (search, post, delete, etc) on the behalf of the authenticated user. I currently have my own account's token hardcoded for you to use (don't worry about posted random stuff because access level is set to read-only). 

You may want to update the system to allow users to authenticate themselves. I believe this will allow multiple streaming connections (one for each user).

This app is registered under my person account so if you need me to change anything regarding the authentication settings (probably won't have to), then feel free to email me.