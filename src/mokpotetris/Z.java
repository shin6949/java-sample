package mokpotetris;

public class Z extends Piece {
	public Z(TetrisData data) {
		super(data);
		c[0] = 0;	r[0] = 0;
		c[1] = -1;	r[1] = 0;
		c[2] = -1;	r[2] = 1;
		c[3] = 0;	r[3] = -1;
	}
	
	public int getType() {
		return 1;
	}
	
	public int roteType() {
		return 4;
	}
}
