package export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
/**
 * This is an example gephi export class. It can also be used as a RAW CSV data export class.
 * @author Baohuy Ung
 * @version 1.0
 */
public class Gephi {
	
	private Connection conn;
	private String fileName = "GephiTest";
	private String sql ="SELECT * FROM TWEETS";
	private String path;
	private int nodeCount = 0;
	
	/**
	 * Genertic constructor
	 * @param conn Conection to the DB
	 */
	public Gephi(Connection conn){
		this.conn = conn;
	}
	
	/**
	 * Contructor with a sql query and full path.
	 * @param conn connection to the DB.
	 * @param path Full path to where the export the file
	 * @param fileName file name to export as
	 * @param statement any sql filter statements
	 */
	public Gephi(Connection conn, String path, String fileName, String statement){
		this.conn = conn;
		this.path = path;
		this.fileName = fileName;
		sql = statement;
		
	}
	

	public void gpMake() throws SQLException, IOException{
		System.out.println("creating statement...");
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		writeA(path+fileName+".csv","id"+ ";" + "created" + ";" + "T_id" + ";" + "text"+ ";" + "source" + ";" + "is_trunc"+ ";" + "in_reply"+ ";" + "reply_user"+ ";" + "fav"+ ";" + "reply_sn"+";" +"geo"+ ";" +"place"+ ";" +"rtCount"+ ";" +"rtByMe"+ ";" +"contrib"+ ";" +"annot"+ ";" +"rtStatus"+ ";" +"mentions"+ ";" +"urls"+ ";" +"hash"+ ";" +"userID"+"\n");
		ResultSet rs = stmt.executeQuery(sql);
		rs.beforeFirst();
		//list of all the columns by type
		boolean eof = true;
		Timestamp created;
		long id, in_reply, reply_user, rtCount, rtStatus, userID;
		String text, source, reply_sn, geo, place, contrib, annot, mentions, urls, hash;
		boolean is_trunc, fav, rtByMe;
		
		//set the result set to the first item in the db
		rs.first();
		while(eof){
			//store each item by column
			created = rs.getTimestamp(1);
			id = rs.getLong(2);
			text = textConvert(rs.getString(3));
			source = rs.getString(4);
			is_trunc = rs.getBoolean(5);
			in_reply = rs.getLong(6);
			reply_user = rs.getLong(7);
			fav = rs.getBoolean(8);
			reply_sn = rs.getString(9);
			geo = rs.getString(10);
			place = rs.getString(11);
			rtCount = rs.getLong(12);
			rtByMe = rs.getBoolean(13);
			contrib = rs.getString(14);
			annot = rs.getString(15);
			rtStatus = rs.getLong(16);
			mentions = rs.getString(17);
			urls = rs.getString(18);
			hash = rs.getString(19);
			userID = rs.getLong(20);
			
			//structure the line to be written
			String line = nodeCount + ";" + created + ";" + id + ";" + text+ ";" + source + ";" + is_trunc+ ";" + in_reply
					+ ";" + reply_user+ ";" + fav+ ";" + reply_sn+";" +geo+ ";" +place+ ";" +rtCount+ ";" +rtByMe+ ";" +contrib
					+ ";" +annot+ ";" +rtStatus+ ";" +mentions+ ";" +urls+ ";" +hash+ ";" +userID+"\n";
			
			nodeCount++;
			
			System.out.println("Inserting Line: \n "+ line);
			//write it to the file
			writeA(path+fileName+".csv", line);
			rs.next();
			if(rs.isAfterLast()){
				eof = false;
			}
		}
		
	}
	
	/**
	 * Helper method to write a line to the specified CSV file.
	 * @param fileName name of the file to be written to
	 * @param a the line to be written
	 * @throws IOException invalid file path
	 */
	public void writeA(String fileName, String a ) throws IOException{
		File file = new File(fileName);
		file.setWritable(true);
		FileWriter fw = new FileWriter(file, true);
		fw.append(a);
		fw.close();
	}
	
	/**
	 * Converts the text of a status that contains line breaks and commons into CSV friend text.
	 * @param t text to be converted
	 * @return CSV friend text
	 */
	public String textConvert(String t){
			String temp = t;
			if (temp.contains("\n")){
				System.out.println("Return characters found! Replacing with space....");
				temp = temp.replace("\n", " ");
						
			}
			if (temp.contains("|")){
				temp = temp.replace("|", "--");
			}
		return temp;
	}
	
}
