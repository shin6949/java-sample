package com.cocoblue.tetris.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JTextPane;

public class TetrisSideview extends JPanel {
	
	public TetrisSideview() {
		JTextPane txtNext = new JTextPane();
		txtNext.setFont(new Font("�����ٸ����", Font.PLAIN, 25));
		txtNext.setText("HOLD");
		txtNext.setDisabledTextColor(SystemColor.desktop);
		txtNext.setBackground(new Color(240, 240, 240));
		txtNext.setEditable(false);
		txtNext.setEnabled(false);
		add(txtNext, BorderLayout.WEST);
	}


}
