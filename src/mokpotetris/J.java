package mokpotetris;

public class J extends Piece {
	
	public J(TetrisData data) {
		super(data);
		c[0] = 0;	r[0] = 0;
		c[1] = 0;	r[1] = 0;
		c[2] = 1;	r[2] = 0;
		c[3] = 2;	r[3] = 0;
	}
	
	public int getType() {
		return 1;
	}
	
	public int roteType() {
		return 2;
	}
}

