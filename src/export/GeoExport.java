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
 * Based on the Gephi class, exports the all nodes from a database with geo-tags enabled
 */
public class GeoExport {

		//set the file name and change sql statement if nodes are to be filtered
		private Connection conn;
		private String fileName = "GephiTest";
		private String sql ="SELECT * FROM TWEETS WHERE GEOLOCATION IS NOT NULL";

		private int nodeCount = 1;
		private String d = ",";
		
		/**
		 * constructor to setup the files and sql statement
		 * @param conn Connection to the DB
		 * @param fileName path to the file to write out to
		 * @param statement the sql statement you want to execute
		 */
		public GeoExport(Connection conn, String fileName, String statement){
			this.conn = conn;
			this.fileName = fileName;
			sql = statement;
			
		}
		
		/**
		 * Queries that database and writes nodes to the file.
		 * 
		 * @throws SQLException
		 * @throws IOException
		 */
		public void geoMake() throws SQLException, IOException{
			System.out.println("creating statement...");
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			writeA("id"+ d + "created" + d + "T_id" + d + "text"+ d + "in_reply"+ d + "reply_user"+ d + "reply_sn"+d +"geo_late"+ d + "geo_long"+ d +"place"+ d +"rtCount"+ d +"rtByMe"+ d +"contrib"+ d +"rtStatus"+ d +"mentions"+ d +"urls"+ d +"hash"+ d +"userID"+d+"UserSN"+"\n");
			ResultSet rs = stmt.executeQuery(sql);
			rs.beforeFirst();
			boolean eof = true;
			Timestamp created;
			long id, in_reply, reply_user, rtCount, rtStatus, userID;
			String text, source, reply_sn, place, contrib, annot, mentions, urls, hash, userSN;
			boolean is_trunc, fav, rtByMe;
			double geoLate, geoLong;
			
			rs.first();
			
			//refer to the columns on the DB
			while(eof){
				created = rs.getTimestamp(1);
				id = rs.getLong(2);
				text = textConvert(rs.getString(3));
				in_reply = rs.getLong(6);
				reply_user = rs.getLong(7);
				reply_sn = rs.getString(9);
				geoLate = lateGet(rs.getString(10));
				geoLong = longGet(rs.getString(10));
				place = rs.getString(11);
				rtCount = rs.getLong(12);
				rtByMe = rs.getBoolean(13);
				contrib = rs.getString(14);
				rtStatus = rs.getLong(16);
				mentions = rs.getString(17);
				urls = rs.getString(18);
				hash = rs.getString(19);
				userID = rs.getLong(20);
				userSN = rs.getString(21);
				
				System.out.println("node: "+ nodeCount);
				String line = nodeCount + d + created.toString() + d + id + d + text+ d + in_reply
						+ d + reply_user+ d + reply_sn+d +geoLate+ d+ geoLong+ d +place+ d +rtCount+ d +rtByMe+ d +contrib
						+ d +rtStatus+ d +mentions+ d +urls+ d +hash+ d +userID+d+userSN+"\n";
				
				nodeCount++;
				//long u_ID = rs.getLong(1);
				//String u_SN = rs.getString(2);
				//String line = u_ID + "|" + u_SN + "\n";
				
				System.out.println("Inserting Line: \n "+ line);
				writeA(line);
				rs.next();
				if(rs.isAfterLast()){
					eof = false;
				}

			}
			rs.close();
			stmt.close();
			conn.close();
		}
		
		/**
		 * Helper method to write each line to the file
		 * 
		 * @param a
		 * @throws IOException
		 */
		public void writeA(String a ) throws IOException{
			File file = new File(fileName);
			file.setWritable(true);
			FileWriter fw = new FileWriter(file, true);
			fw.append(a);
			fw.close();
		}
		
		/**
		 * Text in the database may contain line breaks and commons that will not play well with CSV files.
		 * Convert them into spaces for better compatibility.
		 * 
		 * @param t the text to convert
		 * @return a string of the coverted text safe for CSV files
		 */
		public String textConvert(String t){
				String temp = t;
				while(temp.contains("\n")){
					System.out.println("Return characters found! Replacing with space....");
					temp = temp.replace("\n", " ");
							
				}
				while(temp.contains(",")){
					temp = temp.replace(",", " ");
				}
			return temp;
		}
		
		/**
		 * Helper method to extract the latitude of a geo-coordinate string.
		 * @param geo coordinate string
		 * @return latitude
		 */
		public double lateGet(String geo){
			String temp = geo.substring(geo.indexOf("=")+1, geo.indexOf(","));
			Double d = new Double(temp);
			//changes the granularity of the coordinate to 2 decimal places
			int tempInt = (int)(d*100);
			d = tempInt/100.0;
			return d;
			
		}
		
		/**
		 * Helper method to extract the longitude of a geo-coordinate string.
		 * 
		 * @param geo coordinate string
		 * @return longitude
		 */
		public double longGet(String geo){
			String temp = geo.substring(geo.lastIndexOf("=")+1, geo.length()-1);
			Double d = new Double(temp);
			int tempInt = (int)(d*100);
			d = tempInt/100.0;
			return d;
			
					
		}
		
	}
