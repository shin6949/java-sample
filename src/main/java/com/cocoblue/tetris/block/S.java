package com.cocoblue.tetris.block;

import com.cocoblue.tetris.ui.Piece;
import com.cocoblue.tetris.ui.TetrisData;

public class S extends Piece {
	
	public S(TetrisData data) {
		super(data);
		c[0] = 0;	r[0] = 0;
		c[1] = 1;	r[1] = 0;
		c[2] = 0;	r[2] = 1;
		c[3] = -1;	r[3] = 1;
	}
	
	public int getType() {
		return 2;
	}
	
	public int roteType() {
		return 2;
	}
}