package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainController {

	public static String enter = System.getProperty("line.separator");
	
	@FXML private MenuItem menuItem_save;
	@FXML private MenuItem menuItem_open;
	@FXML private MenuItem menuItem_new;
	@FXML private MenuItem menuItem_exit;
	
	@FXML private MenuItem menuItem_cut;
	@FXML private MenuItem menuItem_copy;
	@FXML private MenuItem menuItem_paste;
	
	@FXML private MenuItem menuItem_login;
	@FXML private MenuItem menuItem_register;
	@FXML private MenuItem menuItem_logout;
	@FXML private MenuItem menuItem_upload;
	@FXML private MenuItem menuItem_manage;
	
	@FXML public TextArea input_text;
	
	@FXML private Button toolBar_save;
	@FXML private Button toolBar_new;
	@FXML private Button toolBar_load;
	@FXML private Button toolBar_copy;
	@FXML private Button toolBar_paste;
	@FXML private Button btn_Test;
	
	@FXML private Text txt_status;
	
	public static String logined_id;
	public static String logined_name;
	public static Boolean Logined = false;
	
	private String not_login_mention = "비로그인 상태입니다.";
	
	public void btn_save(ActionEvent event) {
		String text = input_text.getText();
		System.out.println(text);
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT 파일 (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(menuItem_save.getParentPopup().getScene().getWindow());
		if (file != null) {
		     saveFile(file);
		}
	}
	
	 public void saveFile(File file){
		    try{
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

		    writer.write(input_text.getText().replaceAll("\n", enter));
		    writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }
	
	public void fileOpen (ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT 파일 (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(menuItem_open.getParentPopup().getScene().getWindow());
		
		try {
			StringBuffer data = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;

			while((line = reader.readLine()) != null) {
				data.append(line);
				data.append(enter);
			}
			
			reader.close();
			
			String info = data.toString();
			
			input_text.setText(info);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void make_new (ActionEvent event) {
		if(!input_text.getText().toString().equals("")) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("경고");
		alert.setHeaderText("텍스트 삭제 알림");
		alert.setContentText("작성하신 내용이 저장되지 않고 삭제됩니다. 계속하시겠습니까?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){ input_text.clear(); }
		else { }
		}
	}
	
	public void exit (ActionEvent event) {
		if(!input_text.getText().toString().equals("")) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("확인");
		alert.setHeaderText("작성한 내용이 있습니다.");
		alert.setContentText("옵션을 선택해주세요.");

		ButtonType buttonTypeSave = new ButtonType("저장 후 닫기");
		ButtonType buttonTypedontSave = new ButtonType("저장 안 함");
		ButtonType buttonTypeCancel = new ButtonType("취소", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypedontSave, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == buttonTypeSave){
			btn_save(null);
			System.exit(0);
		} else if (result.get() == buttonTypedontSave) { System.exit(0);} 
		else if (result.get() == buttonTypeCancel) { } } 
		else { System.exit(0); }		
	}
	
	public void btn_cut (ActionEvent event) { input_text.cut(); }
	public void btn_copy (ActionEvent event) { input_text.copy(); }
	public void btn_paste (ActionEvent event) { input_text.paste(); }
	
	public void btn_login(ActionEvent event) throws Exception { 
		Boolean logined = show_login_screen();
		if(logined) {
			txt_status.setText(logined_name + "님 환영합니다.");
			change_title("메모장");
			menuItem_login.setVisible(false);
			menuItem_register.setVisible(false);
			menuItem_logout.setVisible(true);
			menuItem_upload.setVisible(true);
			menuItem_manage.setVisible(true);
		}
	}
	
	public boolean show_login_screen() throws Exception { 
		try {
			Stage mainStage = (Stage)input_text.getScene().getWindow();
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/application/LoginScreen.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("로그인");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isLogined();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	 /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return (Stage)input_text.getScene().getWindow();
    }
	
	public void change_title(String Title) {
		 Stage primStage = (Stage) input_text.getScene().getWindow();
		 primStage.setTitle(Title);
	}
	
	public void set_exit_action() {
		Stage primStage = (Stage) input_text.getScene().getWindow();
		primStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	exit(null);
		    }
		}); 	
	}
	
	public void btn_register(ActionEvent event) {
		try {
			Stage mainStage = (Stage)input_text.getScene().getWindow();
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/application/RegisterScreen.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("회원가입");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(mainStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            // Set the person into the controller.
            RegisterController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void set_status(String mention) {
		txt_status.setText(mention);
	}
	
	public void logout(ActionEvent event) {
		logined_id = null;
		logined_name = null;
		Logined = false;
		txt_status.setText(not_login_mention);
		change_title("메모장 - 비로그인 상태");
		menuItem_login.setVisible(true);
		menuItem_register.setVisible(true);
		menuItem_logout.setVisible(false);
		menuItem_upload.setVisible(false);
		menuItem_manage.setVisible(false);
	}
	
	public void run_manageScreen() {
		try {
			//Stage mainStage = (Stage)input_text.getScene().getWindow();
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/application/NoteManageScreen.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("메모 관리");
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(mainStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            // Set the person into the controller.
            ManageController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void upload(ActionEvent event) {
		SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String test2 = test.format(new Date());
		java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
		
		TextInputDialog dialog = new TextInputDialog(test2);
		dialog.setTitle("제목 입력");
		dialog.setHeaderText("저장할 제목을 입력하세요.");
		dialog.setContentText(null);
		
		ConnectDB id = new ConnectDB();
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String title = result.get();
			
			if(id.insert_Note(title, logined_id, now_date, input_text.getText())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("알림");
				alert.setHeaderText(null);
				alert.setContentText("업로드가 완료되었습니다.");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("알림");
				alert.setHeaderText(null);
				alert.setContentText("업로드에 실패하였습니다.");

				alert.showAndWait();
			}
		}
	}
}
	
