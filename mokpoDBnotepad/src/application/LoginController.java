package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private Button btn_login;
	@FXML private TextField input_id;
	@FXML private PasswordField input_password;
	@FXML private ProgressBar progressbar = new ProgressBar(0);

	public Account login() {
		Account logined_account = new Account();
		String input_id_string = input_id.getText();
				
		try {
			Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM Users WHERE ID = '" + input_id_string + "' AND pw = '" + input_password.getText() + "'");
			
			if(rs.next()) {
				String ID = rs.getString("ID");
				logined_account.logined_id = ID;
			}
			
			stmt.close();
			conn.close();
			rs.close();

		if(logined_account.logined_id.equals(input_id_string)) { 
			logined_account.Logined = true;  } 
		else { 
			logined_account.Logined = false; 
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("경고");
			alert.setHeaderText("일치하는 ID가 없습니다.");
			alert.setContentText("ID와 비밀번호를 확인하고 다시 시도해주세요.");
	
	   stmt.close();
	   conn.close();
	   rs.close();
		}
		
		return null;
	}
}
