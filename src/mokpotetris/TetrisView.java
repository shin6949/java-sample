package mokpotetris;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TetrisView extends JPanel {

	public static TetrisPreview tetrisPre = new TetrisPreview(); 
	public static TetrisHoldview tetrisHold = new TetrisHoldview(); 
	public static JTextPane nowScore = new JTextPane();
	
	public TetrisView() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel tetrisBlock = new JPanel();
		add(tetrisBlock, BorderLayout.CENTER);
		tetrisBlock.add(MyTetris.tetrisCanvas, BorderLayout.CENTER);
		
		JPanel tetrisImf = new JPanel();
		add(tetrisImf, BorderLayout.EAST);
		tetrisImf.setLayout(new BorderLayout(0, 0));
		
		tetrisImf.add(tetrisPre, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		tetrisImf.add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{72, 0};
		gbl_panel.rowHeights = new int[]{21, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		GridBagConstraints gbc_nowlevel = new GridBagConstraints();
		gbc_nowlevel.insets = new Insets(0, 0, 5, 0);
		gbc_nowlevel.anchor = GridBagConstraints.NORTHWEST;
		gbc_nowlevel.gridx = 0;
		gbc_nowlevel.gridy = 0;
		
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 1;

		JTextPane nowlevel = new JTextPane();
		nowlevel.setEditable(false);
		nowlevel.setDisabledTextColor(SystemColor.desktop);
		nowlevel.setBackground(new Color(240, 240, 240));
		nowlevel.setText("현재 레벨: " + TetrisCanvas.level); //현재 레벨 표시
		nowlevel.setEnabled(false);
		panel.add(nowlevel, gbc_nowlevel);
		
		nowScore.setText("");
		nowScore.setDisabledTextColor(SystemColor.desktop);
		nowScore.setBackground(new Color(240, 240, 240));
		nowScore.setEnabled(false);
		nowScore.setEditable(false);
		panel.add(nowScore, gbc_textPane);
		
		JPanel holdView = new JPanel();
		add(holdView, BorderLayout.WEST);
		holdView.add(tetrisHold);
	}

	public static void refresh_now_score(int score) {
		nowScore.setText("\uD604\uC7AC \uC810\uC218: " + score);
	}
	
}
