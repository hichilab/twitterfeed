package export;

import java.io.IOException;
import java.sql.SQLException;

import db.DB_Connect;

public class TestConvoGraph {
	public static void main(String[] args){
		
		try {
			DB_Connect.convoExport();
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
}
