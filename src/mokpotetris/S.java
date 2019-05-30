package mokpotetris;

public class S extends Piece {
	
	public S(TetrisData data) {
		super(data);
		c[0] = 1;	r[0] = 1;
		c[1] = 1;	r[1] = 1;
		c[2] = 1;	r[2] = 1;
		c[3] = 1;	r[3] = 1;
	}
	
	public int getType() {
		return 4;
	}
	
	public int roteType() {
		return 0;
	}
}