package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ManageController implements DialogScreen, Runnable {
	private Stage dialogStage;
	
	@FXML TableView<TableRowDataModel> table;
	@FXML TableColumn<TableRowDataModel, String> titleColumn;
	@FXML TableColumn<TableRowDataModel, String> maketimeColumn;
	@FXML TableColumn<TableRowDataModel, String> modifiedColumn;
	@FXML TableColumn<TableRowDataModel, String> contextColumn;

	ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList(
           
    );

	public void initialize() {
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().title());
		maketimeColumn.setCellValueFactory(cellData -> cellData.getValue().make_time());
		modifiedColumn.setCellValueFactory(cellData -> cellData.getValue().modified_time());
		contextColumn.setCellValueFactory(cellData -> cellData.getValue().context());
		table.setItems(myList);
	}
	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
