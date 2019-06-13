package mokpotetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JTextPane;

public class TetrisPreview extends JPanel {
	
	static int block[] = new int[2];
	protected TetrisData data; //테트리스 내부 데이터
	protected Piece current;
	protected Color colors[];
	
	public TetrisPreview() {
				
		JTextPane txtNext = new JTextPane();
		txtNext.setFont(new Font("나눔바른고딕", Font.PLAIN, 25));
		txtNext.setText("NEXT");
		txtNext.setDisabledTextColor(SystemColor.desktop);
		txtNext.setBackground(new Color(240, 240, 240));
		txtNext.setEditable(false);
		txtNext.setEnabled(false);
		add(txtNext, BorderLayout.EAST);

	}
	
	public void paint(Graphics g) {
		super.paint(g);

		if(current != null){
			for(int i = 0; i < 4; i++) {
				g.setColor(colors[current.getType()]);
				g.fill3DRect(20/2 + 25 * (current.getX()+current.c[i]), 20/2 + 25 * (current.getY()+current.r[i]), 25, 25, true);
				}
			}
		} 
	
	public static void input_next_blocks(int next1, int next2) {
		next1 = block[0];
		next2 = block[1];
	}

}
