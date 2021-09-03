package com.cocoblue.movingcar.ui;

import javax.swing.JFrame;
import java.io.IOException;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MainFrame() throws IOException {
		setTitle("Play Car");
		setSize(800, 600);
		setVisible(true);
		
		add(new Screen());
		
		validate();
	}
}
