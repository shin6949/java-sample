package mokpotetris;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.Box;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TetrisView extends JPanel {
	public static TetrisHoldview tetrisHold = new TetrisHoldview(); 
	public static TetrisPreview tetrisPre = new TetrisPreview(); 
	static JTextPane nowScore = new JTextPane();
	static JTextPane nowlevel = new JTextPane();
	
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
			txtpnTesxt.setFont(new Font("나눔바른고딕", Font.PLAIN, 25));
			verticalBox.add(txtpnTesxt);
			txtpnTesxt.setText("NEXT");
			txtpnTesxt.setDisabledTextColor(SystemColor.desktop);
			txtpnTesxt.setBackground(new Color(240, 240, 240));
			
			JPanel preview = new JPanel();
			verticalBox.add(preview);
			preview.add(tetrisPre);
			
			verticalBox.add(nowScore);
			
			nowScore.setText("\uD604\uC7AC \uC810\uC218: ");
			nowScore.setDisabledTextColor(SystemColor.desktop);
			nowScore.setBackground(new Color(240, 240, 240));
			nowScore.setEnabled(false);
			nowScore.setEditable(false);
			
				
				verticalBox.add(nowlevel);
				nowlevel.setEditable(false);
				nowlevel.setDisabledTextColor(SystemColor.desktop);
				nowlevel.setBackground(new Color(240, 240, 240));
				nowlevel.setText("현재 레벨: " + TetrisCanvas.level); //현재 레벨 표시
				nowlevel.setEnabled(false);
				
		JPanel holdView = new JPanel();
		add(holdView, BorderLayout.WEST);
		holdView.add(tetrisHold);
	}

	public static void refresh_now_score(int score) {
		nowScore.setText("\uD604\uC7AC \uC810\uC218: " + score);
	}
	
	public static void refresh_now_level(int level) {
		nowlevel.setText("현재 레벨: " + level);
	}
}
