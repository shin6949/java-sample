package com.cocoblue.tetris.block;

import com.cocoblue.tetris.ui.Piece;
import com.cocoblue.tetris.ui.TetrisData;

public class Bar extends Piece {
	
	public Bar(TetrisData data) {
		super(data);
		c[0] = 0;	r[0] = 0;
		c[1] = -1;	r[1] = 0;
		c[2] = 1;	r[2] = 0;
		c[3] = 2;	r[3] = 0;
	}
	
	public int getType() {
		return 3;
	}
	
	public int roteType() {
		return 2;
	}
}
