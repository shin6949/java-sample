package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ManageController implements DialogScreen {
	private Stage dialogStage;
	
	private Task<Void> task;
	
	@FXML TableView<TableRowDataModel> table;
	@FXML TableColumn<TableRowDataModel, String> titleColumn;
	@FXML TableColumn<TableRowDataModel, String> maketimeColumn;
	@FXML TableColumn<TableRowDataModel, String> modifiedColumn;
	@FXML TableColumn<TableRowDataModel, String> contextColumn;
	@FXML ProgressBar progressBar;
	
	ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList();

	public void initialize() {
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				progressBar.setVisible(true);
				
				if(isCancelled()) { progressBar.setVisible(false); }
								
				try {
					Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
					updateProgress(20, 100);
					Statement stmt = conn.createStatement();
					updateProgress(30, 100);
					ResultSet rs;
					rs = stmt.executeQuery("SELECT title, make_time, modified_time, context FROM note WHERE author = '" + MainController.logined_id + "'");
					
					while(rs.next()) {
						String title  = rs.getString("title");
						
						updateProgress(40, 100);
						
						Timestamp make_time = rs.getTimestamp("make_time");
						Date make_date = new Date();
						make_date.setTime(make_time.getTime());
						String make_time_string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(make_date);
						
						updateProgress(50, 100);
						
						Timestamp modified_time = rs.getTimestamp("modified_time");
						Date mod_time = new Date();
						mod_time.setTime(modified_time.getTime());
						String modified_time_string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mod_time);
						
						String context = rs.getString("context");
						
						table.getItems().add(new TableRowDataModel(new SimpleStringProperty(title), new SimpleStringProperty(make_time_string),
								new SimpleStringProperty(modified_time_string), new SimpleStringProperty(context)));
						
						updateProgress(100, 100);
						progressBar.setVisible(false);
					}
					
					stmt.close();
					conn.close();
					rs.close();
				
			} catch (Exception e1) {
				e1.printStackTrace();
			  }	
				
				return null;
			}
			
		};
		progressBar.progressProperty().bind(task.progressProperty());

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().title());
		maketimeColumn.setCellValueFactory(cellData -> cellData.getValue().make_time());
		modifiedColumn.setCellValueFactory(cellData -> cellData.getValue().modified_time());
		contextColumn.setCellValueFactory(cellData -> cellData.getValue().context());
		table.setItems(myList);
	}

	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
}
