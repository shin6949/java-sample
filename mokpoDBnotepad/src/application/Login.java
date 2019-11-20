package application;
	
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login {
    public void start(Stage window) throws Exception {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/LoginScreen.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("·Î±×ÀÎ");
        stage.show();
        
    }
}