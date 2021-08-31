package com.cocoblue.tetris.ui;

import com.cocoblue.tetris.block.*;

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
	protected static TetrisData data;
	protected int margin = 20;
	protected boolean stop, makeNew;
	protected Piece current;
	protected static int interval = 2000;
	protected static int level = 2;
	protected static int block_stack[] = {8,8,8};
	protected static int now_block = 8;
	
	public TetrisCanvas() {
		setSize(getPreferredSize());
		data = new TetrisData();
	 
		addKeyListener(this);
		
		colors = new Color[8]; // ��Ʈ���� ��� �� ���� ��
		colors[0] = new Color(80, 80, 80); // ����(����ȸ��)
		colors[1] = new Color(255, 0, 0); //������
		colors[2] = new Color(0, 255, 0); //���
		colors[3] = new Color(0, 200, 255); //�ϴû�
		colors[4] = new Color(255, 255, 0); //�����
		colors[5] = new Color(255, 150, 0); //Ȳ���
		colors[6] = new Color(210, 0, 240); //�����
		colors[7] = new Color(40, 0, 240); //�Ķ���
	}
	 
	public void start() { // ���� ����
		data.clear();
		worker = new Thread(this);
		worker.start();
		makeNew = true;
		stop = false;
		requestFocus();
		repaint();
	}
	 
	public void stop() { //���� ������ ���� ��� �ߴ�
		Stop_Sound();
		stop = true;
		current = null;
	}
	 
	public void paint(Graphics g) {
		super.paint(g);
	 
		for(int i = 0; i < TetrisData.ROW; i++) { //���� ������ �׸���

			for(int k = 0; k < TetrisData.COL; k++) {
				if(data.getAt(i, k) == 0) {
					g.setColor(colors[0]);
					g.draw3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true); 
					} 
				else { 
					g.setColor(colors[0]);
					g.fill3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true);
					}
				}
			}
		
		if(current != null){ // ���� �������� �ִ� ��Ʈ���� ���� �׸���
			for(int i = 0; i < 4; i++) {
				g.setColor(colors[current.getType()]);
				g.fill3DRect(margin/2 + w * (current.getX()+current.c[i]), margin/2 + w * (current.getY()+current.r[i]), w, w, true);
				}
			}
		}
	
	public Dimension getPreferredSize(){ // ��Ʈ���� ���� ũ�� ����
		int tw = w * TetrisData.COL + margin;
		int th = w * TetrisData.ROW + margin;
		return new Dimension(tw, th);
	}
	
	public void run() {

		while(!stop) {
			int now_score = data.getLine() * 175 * level;
			TetrisView.refresh_now_score(now_score);
			TetrisView.refresh_delete_line(data.getLine());
						
			try {
				if(makeNew){ // ���ο� ��Ʈ���� ���� �����
					if(block_stack[0] == 8 && block_stack[1] == 8 && block_stack[2] == 8) {
						block_stack[0] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[1] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
					}
					TetrisPreview.input_next_blocks(block_stack[1], block_stack[2]);
					now_block = block_stack[0];
					
					
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
					
				} else { // ���� ������� ��Ʈ���� ���� �Ʒ��� �̵�
					if(current.moveDown()){
						
						makeNew = true;
						
						if(current.copy()){
							stop();
							int score = data.getLine() * 175 * level;
							JOptionPane.showMessageDialog(this, "���� ��\n���� : " + score);
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
	
	  // Ű���带 �̿��ؼ� ��Ʈ���� ���� ����
	public void keyPressed(KeyEvent e) {
		if(current == null) return;
	 
		switch(e.getKeyCode()) {
		case 37: // ���� ȭ��ǥ
			current.moveLeft();
			repaint();
			break;
		case 39: // ������ ȭ��ǥ
			current.moveRight();
			repaint();
			break;
		case 38: // ���� ȭ��ǥ
			current.rotate();
			repaint();
			break;
		case 82: // rŰ
	         Hold_block();
	         repaint();
	         break;
		case 32: //�����̽���
			current.moveFullDown();
			boolean temp = current.moveDown();
			if(temp) {
				makeNew = true;
				if(current.copy()) {
					stop();
					int score = data.getLine() * 175 * level;
					JOptionPane.showMessageDialog(this, "���� ��\n���� : " + score);
				}
			}
			data.removeLines();
			repaint();
			break;
		case 107: // +Ű
			Level_UP();
			repaint();
			break;
		case 109: // -Ű
			Level_DOWN();
			repaint();
			break;
		case 40: // �Ʒ��� ȭ��ǥ
			boolean temp2 = current.moveDown();
			if(temp2) {
				makeNew = true;
				if(current.copy()) {
					stop();
					int score = data.getLine() * 175 * level;
					JOptionPane.showMessageDialog(this, "���� ��\n���� : " + score);
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
		if(level <= 50) { 
		level++;
		TetrisView.refresh_now_level(level);
		}
	}
	
	public static void Level_DOWN() {
		if(level > 1) { 
		level--;
		TetrisView.refresh_now_level(level); }
	}
	
	public void Hold_block() {
		if(TetrisHoldview.holding_num == 8) { 
			TetrisHoldview.input_hold_num(now_block);
			int tmp[] = {block_stack[0], block_stack[1], block_stack[2]};
			for(int i = 0; i < 3; i++) {
				block_stack[0] = tmp[1];
				block_stack[1] = tmp[2];
				block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
			}
		} else {
			
		}
	}
	
	public void keyReleased(KeyEvent e) { }
	 public void keyTyped(KeyEvent e) { }
}