package com.cocoblue.dbbnotepad;

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
 
    public StringProperty title() {
        return title;
    }
    public StringProperty make_time() {
        return make_time;
    }
    public StringProperty modified_time() {
        return modified_time;
    }
    public StringProperty context() {
        return context;
    }
}