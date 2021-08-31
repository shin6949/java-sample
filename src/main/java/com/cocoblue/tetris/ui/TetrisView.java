package com.cocoblue.tetris.ui;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.Box;
import java.awt.Font;
import java.awt.FlowLayout;

public class TetrisView extends JPanel {
	
	public static TetrisPreview tetrisPre = new TetrisPreview(); 
	static JTextPane nowScore = new JTextPane();
	static JTextPane nowlevel = new JTextPane();
	static JTextPane deleteline = new JTextPane();
	
	public TetrisView() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel tetrisBlock = new JPanel();
		add(tetrisBlock, BorderLayout.CENTER);
		tetrisBlock.add(MyTetris.tetrisCanvas, BorderLayout.CENTER);
		
		JPanel tetrisImf = new JPanel();
		add(tetrisImf, BorderLayout.EAST);
		tetrisImf.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		tetrisImf.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
			
			Box verticalBox = Box.createVerticalBox();
			panel.add(verticalBox, BorderLayout.NORTH);
			
			JTextPane txtpnTesxt = new JTextPane();
			txtpnTesxt.setEnabled(false);
			txtpnTesxt.setEditable(false);
			txtpnTesxt.setFont(new Font("�����ٸ����", Font.PLAIN, 25));
			verticalBox.add(txtpnTesxt);
			txtpnTesxt.setText("NEXT");
			txtpnTesxt.setDisabledTextColor(SystemColor.desktop);
			txtpnTesxt.setBackground(new Color(240, 240, 240));
			
			JPanel preview = new JPanel();
			verticalBox.add(preview);
			preview.add(tetrisPre);
			tetrisPre.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			
			verticalBox.add(nowScore);
			
			nowScore.setText("\uD604\uC7AC \uC810\uC218: 0");
			nowScore.setDisabledTextColor(SystemColor.desktop);
			nowScore.setBackground(new Color(240, 240, 240));
			nowScore.setEnabled(false);
			nowScore.setEditable(false);
			
				
				verticalBox.add(nowlevel);
				nowlevel.setEditable(false);
				nowlevel.setDisabledTextColor(SystemColor.desktop);
				nowlevel.setBackground(new Color(240, 240, 240));
				nowlevel.setText("���� ����: " + TetrisCanvas.level); //���� ���� ǥ��
				nowlevel.setEnabled(false);
				

				deleteline.setText("\uC0AD\uC81C\uD55C \uC904 \uC218: 0");
				deleteline.setEnabled(false);
				deleteline.setEditable(false);
				deleteline.setDisabledTextColor(Color.BLACK);
				deleteline.setBackground(SystemColor.menu);
				verticalBox.add(deleteline);
				
		/* JPanel sideView = new JPanel();
		add(sideView, BorderLayout.WEST);
		
		Box verticalBox_1 = Box.createVerticalBox();
		sideView.add(verticalBox_1);
		
		TetrisSideview tetrisSide = new TetrisSideview(); 
		verticalBox_1.add(tetrisSide);
		
		JPanel tetrisHold = new JPanel();
		verticalBox_1.add(tetrisHold);
		
		TetrisHoldview tetrisHoldview = new TetrisHoldview();
		tetrisHold.add(tetrisHoldview); */
	}

	public static void refresh_now_score(int score) {
		nowScore.setText("\uD604\uC7AC \uC810\uC218: " + score);
	}
	
	public static void refresh_now_level(int level) {
		nowlevel.setText("���� ����: " + level);
	}
	
	public static void refresh_delete_line(int line) {
		deleteline.setText("������ �� ��: " + line);
	}
}
