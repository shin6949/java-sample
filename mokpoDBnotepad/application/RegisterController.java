package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements DialogScreen {
	
	@FXML private Button btn_check_id;
	@FXML private Button btn_register;
	
	@FXML private TextField input_ID;
	@FXML private TextField name;
	@FXML private PasswordField input_pw;
	@FXML private PasswordField input_pwre;

	private Stage dialogStage;
	
	public Boolean id_length_check() {
		if(input_ID.getText().length() >= 4 && input_ID.getText().length() <= 10) {
			return true; 
		}
		else { return false; }
	}
	
	public Boolean id_DB_check() {
		try {
			Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ID FROM Users Where ID = '" + input_ID.getText() + "'");
			int count = 0;
			
			if(rs.next()) { count++; }
			
			if(count == 0 ) { rs.close(); stmt.close(); conn.close(); return true; } 
			else {	rs.close(); stmt.close(); conn.close(); return false;	}
	   
		} 
		catch (Exception e1) { e1.printStackTrace(); return false;}
	}
	
	public Boolean check_id() {
		if(id_length_check() && id_DB_check()) { return true; } 
		else { return false; }
	}
		
	public Boolean check_pw() {
		if(input_pw.getText().length() < 4 || input_pw.getText().length() >= 20) { 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("알림");
			alert.setHeaderText("사용할 수 없는 비밀번호");
			alert.setContentText("비밀번호는 4 ~ 20글자로 구성되어야합니다.");

			alert.showAndWait();
			return false;
		}
		else if (input_pw.getText().equals(input_pwre.getText())) { 
			return true; 
			} 
		else { 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("알림");
			alert.setHeaderText("사용할 수 없는 비밀번호");
			alert.setContentText("비밀번호를 2칸 모두 정확하게 입력하세요.");

			alert.showAndWait();
			return false;		
		}
	}
	
	public Boolean check_vaild() {
		if(check_id() && check_pw()) { return true; }
		else { return false; }
	}
	
	public Boolean DB_insert() {
		try {
			Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
			Statement stmt = conn.createStatement();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users VALUES (?, ?, ?)");
			
			pstmt.setString(1, input_ID.getText());
			pstmt.setString(2, name.getText());
			pstmt.setString(3, input_pw.getText());
			pstmt.executeUpdate();

			stmt.close();
			conn.close();
			pstmt.close();
			
			return true;
		} 
		catch (Exception e1) { e1.printStackTrace(); return false; }
	}
	
	public void register(ActionEvent event) {
		if(check_vaild()) {
			if(DB_insert()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("알림");
				alert.setHeaderText(null);
				alert.setContentText("회원 가입이 완료되었습니다!");

				alert.showAndWait();
				dialogStage.close();
			}
		}
	}
	
	public void btn_checkID(ActionEvent event) {
		if(check_id()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("알림");
			alert.setHeaderText(null);
			alert.setContentText("사용할 수 있는 ID입니다. 현재 입력한 ID를 사용하시겠습니까?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				input_ID.setDisable(true);
			} else {}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("알림");
			alert.setHeaderText("사용할 수 없는 ID");
			alert.setContentText("사용할 수 없는 ID 입니다.");

			alert.showAndWait();
		}
	}
	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
}
