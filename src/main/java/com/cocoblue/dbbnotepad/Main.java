package com.cocoblue.dbbnotepad;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MainScreen.fxml"));
		    Parent root = loader.load();
			Scene scene = new Scene(root, 402, 450);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			//primaryStage.setResizable(false);
	
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {
			    	
			    }
			}); 	
			
			primaryStage.setTitle("�޸��� - ��α��� ����");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
}
