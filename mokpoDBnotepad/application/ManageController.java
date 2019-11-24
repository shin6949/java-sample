package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ManageController implements DialogScreen {
	private Stage dialogStage;
	
	@FXML TableView<TableRowDataModel> table;
	@FXML TableColumn<TableRowDataModel, String> titleColumn;
	@FXML TableColumn<TableRowDataModel, String> maketimeColumn;
	@FXML TableColumn<TableRowDataModel, String> modifiedColumn;
	@FXML TableColumn<TableRowDataModel, String> contextColumn;

	ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList(
           
    );

	public void initialize() {
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		maketimeColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
		modifiedColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
		contextColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
		table.setItems(myList);
	}
	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
}
