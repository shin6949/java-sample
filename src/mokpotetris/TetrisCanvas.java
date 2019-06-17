package mokpotetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TetrisCanvas extends JPanel implements Runnable, KeyListener {
	static Clip clip;
	protected Thread worker;
	protected Color colors[];
	protected int w = 25;
	protected TetrisData data;
	protected int margin = 20;
	protected boolean stop, makeNew;
	protected Piece current;
	protected static int interval = 2000;
	protected static int level = 2;
	protected static int block_stack[] = {8,8,8}; //한번만 작동하는 코드임. 
	
	public TetrisCanvas() {
		setSize(getPreferredSize());
		data = new TetrisData();
	 
		addKeyListener(this);
		
		colors = new Color[8]; // 테트리스 배경 및 조각 색
		colors[0] = new Color(80, 80, 80); // 배경색(검은회색)
		colors[1] = new Color(255, 0, 0); //빨간색
		colors[2] = new Color(0, 255, 0); //녹색
		colors[3] = new Color(0, 200, 255); //하늘색
		colors[4] = new Color(255, 255, 0); //노란색
		colors[5] = new Color(255, 150, 0); //황토색
		colors[6] = new Color(210, 0, 240); //보라색
		colors[7] = new Color(40, 0, 240); //파란색
	}
	 
	public void start() { // 게임 시작
		data.clear();
		worker = new Thread(this);
		worker.start();
		makeNew = true;
		stop = false;
		requestFocus();
		repaint();
	}
	 
	public void stop() { //게임 끝나면 음악 재생 중단
		Stop_Sound();
		stop = true;
		current = null;
	}
	 
	public void paint(Graphics g) {
		super.paint(g);
	 
		for(int i = 0; i < TetrisData.ROW; i++) { //쌓인 조각들 그리기
		  
			for(int k = 0; k < TetrisData.COL; k++) {
				if(data.getAt(i, k) == 0) {
					g.setColor(colors[data.getAt(i, k)]);
					g.draw3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true); 
					} 
				else { 
					g.setColor(colors[data.getAt(i, k)]);
					g.fill3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true);
					}
				}
			}
		
		if(current != null){ // 현재 내려오고 있는 테트리스 조각 그리기
			for(int i = 0; i < 4; i++) {
				g.setColor(colors[current.getType()]);
				g.fill3DRect(margin/2 + w * (current.getX()+current.c[i]), margin/2 + w * (current.getY()+current.r[i]), w, w, true);
				}
			}
		}
	
	public Dimension getPreferredSize(){ // 테트리스 판의 크기 지정
		int tw = w * TetrisData.COL + margin;
		int th = w * TetrisData.ROW + margin;
		return new Dimension(tw, th);
	}
	
	public void run() {

		while(!stop) {
			int now_score = data.getLine() * 175 * level;
			TetrisView.refresh_now_score(now_score);
			
			try {
				if(makeNew){ // 새로운 테트리스 조각 만들기
					if(block_stack[0] == 8 && block_stack[1] == 8 && block_stack[2] == 8) {
						block_stack[0] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[1] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
					}
					TetrisPreview.input_next_blocks(block_stack[1], block_stack[2]);
					
					switch(block_stack[0]){
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
						if(block_stack[0] % 2 == 0) { current = new Tee(data);}
						else { current = new El(data); }
					}
					
					int tmp[] = {block_stack[0], block_stack[1], block_stack[2]};
					
					for(int i = 0; i < 3; i++) {
						block_stack[0] = tmp[1];
						block_stack[1] = tmp[2];
						block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
					}
					
					makeNew = false;
					
				} else { // 현재 만들어진 테트리스 조각 아래로 이동
					if(current.moveDown()){
						
						makeNew = true;
						
						if(current.copy()){
							stop();
							int score = data.getLine() * 175 * level;
							JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score);
						}
						current = null;
					}
								
					data.removeLines();
				}
				repaint();
				Thread.currentThread();
				Thread.sleep(interval/level);
				} catch(Exception e){ }
			}
		}
	
	public static void Boom() {
		try {
			interval = 0;
			Thread.currentThread();
			Thread.sleep(1000);
			interval = 2000;
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	  // 키보드를 이용해서 테트리스 조각 제어
	public void keyPressed(KeyEvent e) {
		if(current == null) return;
	 
		switch(e.getKeyCode()) {
		case 37: // 왼쪽 화살표
			current.moveLeft();
			repaint();
			break;
		case 39: // 오른쪽 화살표
			current.moveRight();
			repaint();
			break;
		case 38: // 윗쪽 화살표
			current.rotate();
			repaint();
			break;
		case 32: //스페이스바
			current.moveFullDown();
			boolean temp = current.moveDown();
			if(temp) {
				makeNew = true;
				if(current.copy()) {
					stop();
					int score = data.getLine() * 175 * level;
					JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score);
				}
			}
			data.removeLines();
			repaint();
			break;
		case 107: // +키
			Level_UP();
			repaint();
			break;
		case 109: // -키
			Level_DOWN();
			repaint();
			break;
		case 40: // 아랫쪽 화살표
			boolean temp2 = current.moveDown();
			if(temp2) {
				makeNew = true;
				if(current.copy()) {
					stop();
					int score = data.getLine() * 175 * level;
					JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score);
				}
			}
			data.removeLines();
			repaint();
			}
		}
	
	public static void Play(String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(fileName)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		}	catch(Exception ex) { }		
	}

	public static void Stop_Sound() {
		clip.stop();
		clip.close();
	}
	
	public static void Level_UP() {
		level++;
		TetrisView.refresh_now_level(level);
	}
	
	public static void Level_DOWN() {
		level--;
		TetrisView.refresh_now_level(level);
	}
	
	public void keyReleased(KeyEvent e) { }
	 public void keyTyped(KeyEvent e) { }
}