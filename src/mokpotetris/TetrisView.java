package mokpotetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Component;

public class TetrisView extends JPanel {
	public static TetrisSideview tetrisSide = new TetrisSideview(); 
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
			txtpnTesxt.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			txtpnTesxt.setEnabled(false);
			txtpnTesxt.setEditable(false);
			txtpnTesxt.setFont(new Font("나눔바른고딕", Font.PLAIN, 25));
			verticalBox.add(txtpnTesxt);
			txtpnTesxt.setText("NEXT");
			txtpnTesxt.setDisabledTextColor(SystemColor.desktop);
			txtpnTesxt.setBackground(new Color(240, 240, 240));
			StyledDocument doc = txtpnTesxt.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
			
			JPanel preview = new JPanel();
			verticalBox.add(preview);
			
			TetrisPreview tetrisPreview = new TetrisPreview();
			preview.add(tetrisPreview);
			
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
		
		Box verticalBox_1 = Box.createVerticalBox();
		holdView.add(verticalBox_1);
		verticalBox_1.add(tetrisSide);
		
		JPanel panel_1 = new JPanel();
		verticalBox_1.add(panel_1);
	}

	public static void refresh_now_score(int score) {
		nowScore.setText("\uD604\uC7AC \uC810\uC218: " + score);
	}
	
	public static void refresh_now_level(int level) {
		nowlevel.setText("현재 레벨: " + level);
	}
}
