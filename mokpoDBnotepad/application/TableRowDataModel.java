package application;

import javafx.beans.property.StringProperty;

public class TableRowDataModel {
    private StringProperty title;
    private StringProperty make_time;
    private StringProperty modified_time;
    private StringProperty context;
 
    public TableRowDataModel(StringProperty title, StringProperty make_time, StringProperty modified_time, StringProperty context) {
        this.title = title;
        this.make_time = make_time;
        this.modified_time = modified_time;
        this.context = context;
    }
 
    public StringProperty nameProperty() {
        return title;
    }
    public StringProperty addressProperty() {
        return make_time;
    }
    public StringProperty genderProperty() {
        return modified_time;
    }
    public StringProperty classNumProperty() {
        return context;
    }
}