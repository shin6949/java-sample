package com.cocoblue.movingcar.ui;

import com.cocoblue.movingcar.object.Car;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class Screen extends Canvas implements KeyListener {

	private Car[] cars;

	public Screen() throws IOException {
		try {
			cars = new Car[]{new Car("image/car.png", 0, 0), new Car("image/car1.png", 0, 50)};
			addKeyListener(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(cars[0].getImage(), cars[0].getX(), cars[0].getY(), this);
		g.drawImage(cars[1].getImage(), cars[1].getX(), cars[1].getY(), this);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int gap = 10;
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			cars[0].setY(cars[0].getY() - gap);
			cars[1].setY(cars[1].getY() - gap);
			break;
		case KeyEvent.VK_DOWN:
			cars[0].setY(cars[0].getY() + gap);
			cars[1].setY(cars[1].getY() + gap);
			break;
		case KeyEvent.VK_LEFT:
			cars[0].setX(cars[0].getX() - gap);
			cars[1].setX(cars[1].getX() - gap);
			break;
		case KeyEvent.VK_RIGHT:
			cars[0].setX(cars[0].getX() + gap);
			cars[1].setX(cars[1].getX() + gap);
			break;
		}

		System.out.println("Car No.0: X=" + cars[0].getX() + " Y=" + cars[0].getY());
		System.out.println("Car No.1: X=" + cars[1].getX() + " Y=" + cars[1].getY());
		repaint();
	}

}
