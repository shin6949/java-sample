package com.cocoblue.dbbnotepad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
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
	
	@FXML Button btn_modify_title;
	@FXML Button btn_modify;
	@FXML Button btn_delete;
	
	ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList();
	RunningNote runningnote = new RunningNote(null, false);
	String new_title = "test";

	public void initialize() {
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				progressBar.setVisible(true);
				
				if(isCancelled()) { progressBar.setVisible(false); }

				try {
					int i = 0;
					Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
					updateProgress(20, 100);
					Statement stmt = conn.createStatement();
					updateProgress(30, 100);
					ResultSet rs;
					rs = stmt.executeQuery("SELECT title, make_time, modified_time, context FROM note WHERE author = '" + MainController.logined_id + "'");

					while(i < 10 && rs.next()) {
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
						i++;
					}
					
					stmt.close();
					conn.close();
					rs.close();
					updateProgress(100, 100);
					progressBar.setVisible(false);
				
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
	 
	 public RunningNote return_currentnote() { return runningnote; }

	 public void btn_edit() {
		 TableRowDataModel List;
		 if(table.getSelectionModel().getSelectedItem() != null) {
		 List = table.getSelectionModel().getSelectedItem();
		 
		 runningnote.running_maketime = List.make_time().get();
		 runningnote.is_edit = true;
		 
		 dialogStage.close();
		 } else {
        	 Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�˸�");
				alert.setHeaderText(null);
				alert.setContentText("������ ������ �����ϼ���.");
				
				alert.showAndWait();
		 }
	 }
	 
	 public void btn_delete() {
		 task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					progressBar.setVisible(true);
					String make_time_string = null;
					TableRowDataModel List;
					
					if(isCancelled()) { progressBar.setVisible(false); }
					
					if(table.getSelectionModel().getSelectedItem() != null) {
					List = table.getSelectionModel().getSelectedItem();
					System.out.println(List.make_time().get()); 
					make_time_string = List.make_time().get();
					
					System.out.println(make_time_string);
					
					try {
						Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
						Statement stmt = conn.createStatement();
						PreparedStatement pstmt = conn.prepareStatement("DELETE FROM note WHERE make_time = ?");
						pstmt.setString(1, make_time_string);
						pstmt.executeUpdate();

						stmt.close();
						conn.close();
						pstmt.close();
						
						Platform.runLater(new Runnable() {
			                 @Override public void run() {
			                	 Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("�˸�");
									alert.setHeaderText(null);
									alert.setContentText("���� �Ϸ�");
									
									alert.showAndWait();
			                 }
			             });
						
						table.getItems().remove(List);
						progressBar.setVisible(false);
					}  catch (Exception e1) { e1.printStackTrace(); } 
					}

					return null;
				}
				
			};
			progressBar.progressProperty().bind(task.progressProperty());

			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();
	 }
	 
	 
	 public void btn_modify_title() {
		TableRowDataModel List;
		List = table.getSelectionModel().getSelectedItem();
		
		
		if(table.getSelectionModel().getSelectedItem() != null) {
			 runningnote.running_maketime = List.make_time().get();
			 runningnote.is_edit = false;
			 
			 TextInputDialog dialog = new TextInputDialog(null);
			 dialog.setTitle("���� ����");
			 dialog.setHeaderText("���� �� ������ �Է��ϼ���.");
			 dialog.setContentText(null);

			 // Traditional way to get the response value.
			 Optional<String> result = dialog.showAndWait();
			 if (result.isPresent()){
				 progressBar.progressProperty().bind(task.progressProperty());
				 	
				 new_title = result.get();
				 
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();
			 }
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�˸�");
				alert.setHeaderText(null);
				alert.setContentText("������ ������ �����ϼ���.");
				
				alert.showAndWait();
			}
		 
		 task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					progressBar.setVisible(true);
					String make_time_string = null;
										
					if(isCancelled()) { progressBar.setVisible(false); }
										
					System.out.println(make_time_string);
					
					try {
						Connection conn = DriverManager.getConnection(LoginManager.DB_URL, LoginManager.DB_ID, LoginManager.DB_PW);
						Statement stmt = conn.createStatement();
						PreparedStatement pstmt = conn.prepareStatement("UPDATE note SET title = ? WHERE make_time = ?");
						
						pstmt.setString(1, new_title);
						pstmt.setString(2, runningnote.running_maketime);
						pstmt.executeUpdate();

						stmt.close();
						conn.close();
						pstmt.close();
						
						Platform.runLater(new Runnable() {
			                 @Override public void run() {
			                	 Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("�˸�");
									alert.setHeaderText(null);
									alert.setContentText("���� ���� �Ϸ�");
									
									alert.showAndWait();
			                 }
			             });
						
						for ( int i = 0; i < table.getItems().size(); i++) {
							table.getItems().clear();
						}
						
						initialize();
					}  catch (Exception e1) { e1.printStackTrace(); } 

					return null;
				}
				
			};			
	 }
}
