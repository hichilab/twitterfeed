package tagGather;

import java.sql.SQLException;

import db.DB_Connect;

public class TestTagGather {
	public static void main(String[] args){
		
		try {
			DB_Connect.hashGather();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
