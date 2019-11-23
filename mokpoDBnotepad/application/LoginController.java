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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	
	@FXML private Button btn_login;
	@FXML private TextField input_id;
	@FXML private PasswordField input_password;
	@FXML private ProgressBar progressbar = new ProgressBar(0);

	private boolean Logined = false;
	private Stage dialogStage;
	
	public void login() {
		progressbar.setVisible(true);
		progressbar.setProgress(0.1);
		String input_id_string = input_id.getText();
		
		MainController.logined_id = null;
		progressbar.setProgress(0.2);	
		
		try {
			Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			progressbar.setProgress(0.3);	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM Users WHERE ID = '" + input_id_string + "' AND pw = '" + input_password.getText() + "'");
			progressbar.setProgress(0.4);
			
			if(rs.next()) {
				String ID = rs.getString("ID");
				MainController.logined_id = ID;
			}
			
			stmt.close();
			conn.close();
			rs.close();
			progressbar.setProgress(0.5);
			
			if(input_id_string.equals(MainController.logined_id)) {
				System.out.println("login Finished");
				Logined = true;
				progressbar.setProgress(1);
				dialogStage.close();
			} else {
				System.out.println("login Failed");
				
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("경고");
				alert.setHeaderText("일치하는 계정이 존재하지 않습니다.");
				alert.setContentText("입력한 ID 또는 비밀번호를 확인하세요.");
				
				progressbar.setVisible(false);
				progressbar.setProgress(1);
				
				alert.showAndWait();
			}
		
	} catch (Exception e1) {
		e1.printStackTrace();
	  }
	}
		
    public boolean isLogined() {
        return Logined;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
	
