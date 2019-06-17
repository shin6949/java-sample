package mokpotetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JTextPane;

public class TetrisHoldview extends JPanel {
	
	public TetrisHoldview() {
		JTextPane txtNext = new JTextPane();
		txtNext.setFont(new Font("³ª´®¹Ù¸¥°íµñ", Font.PLAIN, 25));
		txtNext.setText("HOLD");
		txtNext.setDisabledTextColor(SystemColor.desktop);
		txtNext.setBackground(new Color(240, 240, 240));
		txtNext.setEditable(false);
		txtNext.setEnabled(false);
		add(txtNext, BorderLayout.WEST);
	}


}
