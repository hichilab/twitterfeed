package export;

import java.io.IOException;
import java.sql.SQLException;

import db.DB_Connect;

public class TestTimeGraph {
	public static void main(String[] args) throws IOException{
		try {
			DB_Connect.timeExport();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
