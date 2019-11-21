package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private Button btn_login;
	@FXML private TextField input_id;
	@FXML private PasswordField input_password;
	@FXML private ProgressBar progressbar = new ProgressBar(0);
	
	public void login() {
		try {
			progressbar.setVisible(true);
			progressbar.setProgress(0.1);
			Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Users");

		if(rs.next()) {
			String ID = rs.getString("ID");
			String PW = rs.getString("PW");
			String name = rs.getString("name");
			
			System.out.println(ID + "\t" + PW + "\t" + name + "\n");
	   }

	   stmt.close();
	   conn.close();
	   rs.close();


	} catch (Exception e1) {
		e1.printStackTrace();
	  }
	}
}
