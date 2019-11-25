package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements DialogScreen, Runnable, Initializable {
	
	@FXML private Button btn_login;
	@FXML private TextField input_id;
	@FXML private TextField input_name;
	@FXML private PasswordField input_password;
	@FXML private ProgressBar progressbar = new ProgressBar(0);

	private Task<Void> task;
	
	private boolean Logined = false;
	private Stage dialogStage;
	
	public void login() {
		String input_id_string = input_id.getText();
		
		MainController.logined_id = null;
		
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				progressbar.setVisible(true);
				
				if(isCancelled()) { progressbar.setVisible(false); }
								
				try {
					Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
					updateProgress(30, 100);
					Statement stmt = conn.createStatement();
					ResultSet rs;
					rs = stmt.executeQuery("SELECT id, name FROM Users WHERE ID = '" + input_id_string + "' AND pw = '" + input_password.getText() + "'");
					updateProgress(40, 100);
					
					if(rs.next()) {
						String ID = rs.getString("ID");
						String name = rs.getString("name");
						MainController.logined_id = ID;
						MainController.logined_name = name;
						updateProgress(70, 100);
					}
					
					stmt.close();
					conn.close();
					rs.close();
					updateProgress(80, 100);
					
					if(input_id_string.equals(MainController.logined_id)) {
						System.out.println("login Finished");
						Logined = true;
						updateProgress(100, 100);
						
						Platform.runLater(new Runnable() {
			                 @Override public void run() {
			                	 dialogStage.close();
			                 }
			             });
						
						
					} else {
						System.out.println("login Failed");
						Platform.runLater(new Runnable() {
			                 @Override public void run() {
			                	 Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("경고");
									alert.setHeaderText("일치하는 계정이 존재하지 않습니다.");
									alert.setContentText("입력한 ID 또는 비밀번호를 확인하세요.");
									
									alert.showAndWait();
			                 }
			             });

						updateProgress(100, 100);
						progressbar.setVisible(false);
					}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			  }
				
				return null;
			}
			
		};
		
		progressbar.progressProperty().bind(task.progressProperty());
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
	}
		
    public boolean isLogined() {
        return Logined;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	@Override
	public void run() {
			
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
}
	
