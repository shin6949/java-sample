package mokpotetris;

public class TetrisData {
	public static final int ROW = 20;
	public static final int COL = 10;
	
	private int data[][]; //ROW x COL의 배열
	private int line; //지운 줄 수
	
	public TetrisData() {
		data = new int[ROW][COL];
	}
	
	public int getAt(int x, int y) {
		if(x < 0 || x >= ROW || y < 0 || y >= COL)
			return 0;
		return data[x][y];
	}
	
	public void setAt(int x, int y, int v) {
		data[x][y] = v;
	}
	
	public int getLine() {
		return line;
	}
	
	public synchronized void removeLines() {
		NEXT:
		for(int i = ROW-1; i >= 0; i--) {
			boolean done = true;
			for(int k = 0; k < COL; k++) {
				if(data[i][k] == 0) {
					done = false;
					continue NEXT; } }
			
			if(done) {
				line++;
				for(int x = i; x > 0; x--) {
					for(int y = 0; y < COL; y++) {
						data[x][y] = data[x-1][y]; } }
				
				if(i != 0) {
					for(int y = 0; y < COL; y++) {
						data[0][y] = 0; } }
			}
		}

	}
	
	public void clear() { //data 배열 초기화
		for(int i = 0; i < ROW; i++) {
			for(int k = 0; k < COL; k++) {
				data[i][k] = 0;
			}
		}
	}
	
	public void dump() { //data 배열 내용 출력
		for(int i = 0; i < ROW; i++) {
			for(int k = 0; k < COL; k++) {
				System.out.print(data[i][k] + " ");
			}
			System.out.println();
		}
	}
}
