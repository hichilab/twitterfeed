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
 * 
 * @author Baohuy Ung
 * @version 1.0
 * 
 * Raw export of the data from a database, you can add filters by chaning the sql statement and must change the file location/name
 */
public class CSV {
	
	//file names and default sql string
	private final String fileName = "HISEN_OG";
	private final String sql ="SELECT * FROM TWEETS";
	private String d = ",";
	
	public CSV(){
		//empty constructor
	}
	
	/**
	 * Exports each entity to a line into a CSV file specified.
	 * @param conn Connection to the DB
	 * @throws SQLException
	 * @throws IOException
	 */
	public void make(Connection conn) throws SQLException, IOException{
		System.out.println("creating statement...");
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		//Path is hard coded because this is an example, should be changed later
		writeA("/Users/Brian/Desktop/"+fileName+".csv", sql+"\n");
		writeA("/Users/Brian/Desktop/"+fileName+".csv","created" + d + "id" + d + "text"+ d + "source" + d + "is_trunc"+ d + "in_reply"+ d + "reply_user"+ d + "fav"+ d + "reply_sn"+d +"geo"+ d +"place"+ d +"rtCount"+ d +"rtByMe"+ d +"contrib"+ d +"annot"+ d +"rtStatus"+ d +"mentions"+ d +"urls"+ d +"hash"+ d +"userID"+"\n");
		ResultSet rs = stmt.executeQuery(sql);
		rs.beforeFirst();
		boolean eof = true;
		Timestamp created;
		long id, in_reply, reply_user, rtCount, rtStatus, userID;
		String text, source, reply_sn, geo, place, contrib, annot, mentions, urls, hash, userSN;
		boolean is_trunc, fav, rtByMe;
		
		rs.first();
		while(eof){
			created = rs.getTimestamp(1);
			id = rs.getLong(2);
			text = textConvert(rs.getString(3));
			source = rs.getString(4);
			is_trunc = rs.getBoolean(5);
			in_reply = rs.getLong(6);
			reply_user = rs.getLong(7);
			fav = rs.getBoolean(8);
			reply_sn = rs.getString(9);
			
			String geoTemp = rs.getString(10);
			if(geoTemp!=null){
				geo = textConvert(geoTemp);
			}
			else geo = geoTemp;
			
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
			userSN = rs.getString(21);
			
			String line = created + d + id + d + text+ d + source + d + is_trunc+ d + in_reply
					+ d + reply_user+ d + fav+ d + reply_sn+d +geo+ d +place+ d +rtCount+ d +rtByMe+ d +contrib
					+ d +annot+ d +rtStatus+ d +mentions+ d +urls+ d +hash+ d +userID+d+userSN+"\n";
			
			//long u_ID = rs.getLong(1);
			//String u_SN = rs.getString(2);
			//String line = u_ID + d + u_SN + "\n";
			
			System.out.println("Inserting Line: \n "+ line);
			writeA("/Users/Brian/Desktop/"+fileName+".csv", line);
			rs.next();
			if(rs.isAfterLast()){
				eof = false;
			}
		}
		
	}
	
	/**
	 * Helper method to write each line into the file as it comes out of the DB
	 * @param fileName name of the file
	 * @param a the string you want to write to the file
	 * @throws IOException
	 */
	public void writeA(String fileName, String a ) throws IOException{
		File file = new File(fileName);
		file.setWritable(true);
		FileWriter fw = new FileWriter(file, true);
		fw.append(a);
		fw.close();
	}
	
	/**
	 * Converts the text from the DB which is not CSV friendly into a single string.
	 * @param the string of text from the DB
	 * @return CSV friendly string
	 */
	public String textConvert(String t){
			String temp = t;
			System.out.println(t);
			if (temp.contains("\n")){
				System.out.println("Return characters found! Replacing with space....");
				temp = temp.replace("\n", " ");
						
			}
			if (temp.contains(d)){
				temp = temp.replace(d, " ");
			}
		return temp;
	}
}
