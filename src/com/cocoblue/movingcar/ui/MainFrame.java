package com.cocoblue.movingcar.ui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public MainFrame() {
		setTitle("Play Car");
		setSize(800, 600);
		setVisible(true);
		
		add(new Screen());
		
		validate();
	}
}
