package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private Button btn_login;
	@FXML private TextField input_id;
	@FXML private PasswordField input_password;
	
	public void login() {
		ProgressBar.setVisible(true);
		Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT name, status, id, isadmin FROM Users WHERE id = '" + id + "' AND pw = '" + PW + "'");
		ProgressBar.setValue(50);
		
		if(rs.next()) {
			String name = rs.getString("name");
			Boolean bool_status = rs.getBoolean("status");
			String login_id = rs.getString("id");
			Boolean bool_admin = rs.getBoolean("isadmin");
		   
			logined_user.id = login_id;
			logined_user.name = name;
			logined_user.status = bool_status;
			logined_user.isadmin = bool_admin;
	   }
		
		if(id.equals(logined_user.id)) { 
			logined = true; 
			SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String test2 = test.format(new Date());
			java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO log VALUES (null, ?, ?, ?, null)");
			pstmt.setTimestamp(1, now_date);
			pstmt.setString(2, id);
			pstmt.setString(3, "Login_Success");
			pstmt.executeUpdate();
			ProgressBar.setValue(80);
			pstmt.close();
			} 
		else {
			logined = false;
			JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 확인하세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String test2 = test.format(new Date());
			java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO log VALUES (null, ?, ?, ?, null)");
			pstmt.setTimestamp(1, now_date);
			pstmt.setString(2, id);
			pstmt.setString(3, "Login_Fail");
			pstmt.executeUpdate();
			ProgressBar.setValue(80);
			pstmt.close();
		}

	   ProgressBar.setValue(90);
	   
	   stmt.close();
	   conn.close();
	   rs.close();
	   
	   ProgressBar.setValue(100);
	   ProgressBar.setVisible(false);

	} catch (Exception e1) {
		ProgressBar.setValue(0);
		e1.printStackTrace();
	  }
	}
}
