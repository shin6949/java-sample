package com.cocoblue.tetris.block;

import com.cocoblue.tetris.ui.Piece;
import com.cocoblue.tetris.ui.TetrisData;

public class O extends Piece {
	
	public O(TetrisData data) {
		super(data);
		c[0] = 1;	r[0] = 0;
		c[1] = 1;	r[1] = 1;
		c[2] = 0;	r[2] = 1;
		c[3] = 0;	r[3] = 0;
	}
	
	public int getType() {
		return 4;
	}
	
	public int roteType() {
		return 0;
	}
}