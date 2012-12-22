package export;

import java.io.IOException;
import java.sql.SQLException;

import db.DB_Connect;

public class TestGephiHash {
	
	public static void main(String[] args){
		long timer;
		try {
			timer = System.nanoTime();
			DB_Connect.gHash();
			timer = System.nanoTime() - timer;
			System.out.println("Completion Time in ns: "+timer);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
  