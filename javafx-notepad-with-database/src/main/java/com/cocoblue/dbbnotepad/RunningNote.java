package com.cocoblue.dbbnotepad;

public class RunningNote {
	String running_maketime;
	Boolean is_edit;
	
	public RunningNote(String running_maketime, Boolean is_edit) {
		if(running_maketime == null) { this.running_maketime = null; } 
		else { this.running_maketime = running_maketime; }
		
		this.is_edit = is_edit;
	}
}
