package mokpotetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TetrisPreview extends JPanel {
	
	protected TetrisData data; //테트리스 내부 데이터
	protected Piece current;
	protected Color colors[];
	static int block[] = {8,8};
	protected int w = 25;
	protected int margin = 20;
	
	public Dimension getPreferredSize(){ // 테트리스 판의 크기 지정
		int tw = 110;
		int th = 100;
		return new Dimension(110, 150);
	}
	
	public TetrisPreview() {
		
		colors = new Color[8]; // 테트리스 배경 및 조각 색
		colors[0] = new Color(80, 80, 80); // 배경색(검은회색)
		colors[1] = new Color(255, 0, 0); //빨간색
		colors[2] = new Color(0, 255, 0); //녹색
		colors[3] = new Color(0, 200, 255); //하늘색
		colors[4] = new Color(255, 255, 0); //노란색
		colors[5] = new Color(255, 150, 0); //황토색
		colors[6] = new Color(210, 0, 240); //보라색
		colors[7] = new Color(40, 0, 240); //파란색
		
		System.out.println("TetrisPreView");
		repaint();
	} 
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		System.out.println("TetrisPreView.paint");
		System.out.println(block[0]);
		System.out.println(current);
		
		switch(block[0]){
		case 0:
			current = new Bar(data);
			break;
		case 1:
			current = new Tee(data);
			break;
		case 2:
			current = new El(data);
			break;
		case 3:
			current = new Z(data);
			break;
		case 4:
			current = new O(data);
			break;
		case 5:
			current = new S(data);
			break;
		case 6:
			current = new J(data);
			break;
		default:
			if(block[0] % 2 == 0)
				current = new Tee(data);
			else current = new El(data);
		}
		if(block[1] != 8) {
		for(int i = 0; i < 4; i++) {
			g.setColor(colors[current.getType()]);
			g.fill3DRect(margin/2 + w * (1+current.c[i]), margin/2 + w * (current.getY()+current.r[i]), w, w, true);
			g.setColor(colors[current.getType()]);
			g.fill3DRect(margin/2 + w * (1+current.c[i]), margin/2 + w * (3+current.r[i]), w, w, true);
			repaint();
			}
		}
	}
		
		public static void input_next_blocks(int next1, int next2) {
			block[0] = next1;
			block[1] = next2;
		}
}
