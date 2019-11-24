package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectDB {
	Connection conn;
	
	public ConnectDB() {
		try { conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW); } 
		catch (Exception e1) { e1.printStackTrace(); }
	}
	
	public Boolean insert_Note(String title, String author, java.sql.Timestamp make_time, String context) {
		try {
			//Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			Statement stmt = conn.createStatement();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO note VALUES (?, ?, ?, ?, ?)");
			
			pstmt.setString(1, title);
			pstmt.setString(2, author);
			pstmt.setTimestamp(3, make_time);
			pstmt.setTimestamp(4, make_time);
			pstmt.setString(5, context);
			pstmt.executeUpdate();

			stmt.close();
			conn.close();
			pstmt.close();
			
			return true;
		} 
		catch (Exception e1) { e1.printStackTrace(); return false; }
	}
	
	public Boolean load_note(String id) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT title, make_time, modified_time, context FROM note WHERE author = '" + id + "'");
			
			while(rs.next()) {
				String title = rs.getString("title");
				
			}

			stmt.close();
			conn.close();
			rs.close();
			
			return true;
		} 
		catch (Exception e1) { e1.printStackTrace(); return false; }
	}
}
