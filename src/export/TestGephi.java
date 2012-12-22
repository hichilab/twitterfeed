package export;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import db.DB_Connect;

import tagGather.TagGather;

public class TestGephi {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException{

		DB_Connect.gExport();
	}
}
