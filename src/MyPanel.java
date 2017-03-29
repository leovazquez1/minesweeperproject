import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int innerCellSize = 29;
	private static final int totalColums = 9;
	private static final int totalRows = 9;   
	final int bombColums = 9;
	final int bombRows = 9;
	final int totalBombs = 10;
	public int selectedSquares = (totalColums * totalRows) - totalBombs;  //Counts the gray squares
	private boolean endGame = false;

	private boolean[][] Blank = new boolean[bombColums][bombRows];
	Random generator = new Random();
	public boolean[][] HiddenNumber = new boolean[bombColums][bombRows];
	private Integer[][] mineNumbers = new Integer[bombColums][bombRows];
	public boolean[][] mines = new boolean[bombColums][bombRows]; 
	public Integer[][] minesPosition = new Integer[totalBombs][2];
	public Color[][] minesArray = new Color[bombColums][bombRows];
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[totalColums][totalRows];


	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (innerCellSize + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (totalColums + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (totalRows + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

		for (int x = 0; x < totalColums; x++) {   //Paint cells on grid
			for (int y = 0; y < totalRows; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}

		mineGenerator();
		numberGenerator();

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right;
		int y2 = getHeight() - myInsets.bottom;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);


		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= totalRows; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (innerCellSize + 1)), x1 + GRID_X + ((innerCellSize + 1) * totalColums), y1 + GRID_Y + (y * (innerCellSize + 1)));
		}
		for (int x = 0; x <= totalColums; x++) {
			g.drawLine(x1 + GRID_X + (x * (innerCellSize + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (innerCellSize + 1)), y1 + GRID_Y + ((innerCellSize + 1) * (totalRows )));
		}

		if(endGame){ //when game ends all mines appear
			for(int i = 0; i < totalBombs; i++){
				colorArray[minesPosition[i][0]][minesPosition[i][1]] = Color.black;

			}




		}
		//Paint cell colors
		for (int x = 0; x < totalColums; x++) {
			for (int y = 0; y < totalRows; y++) {
				if ((x == 0) || (y != totalRows)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (innerCellSize + 1)) + 1, y1 + GRID_Y + (y * (innerCellSize + 1)) + 1, innerCellSize, innerCellSize);

					if(HiddenNumber[x][y]){ //paints numbers on non-mined squares

						g.setColor(Color.BLACK);

						if(mineNumbers[x][y]>0){ //If mineNumbers > 0 draw numbers of mine around square

							g.drawString(Integer.toString(mineNumbers[x][y]),x1 + GRID_X + (x * (innerCellSize + 1)) + 1 +10  , y1 + GRID_Y + (y * (innerCellSize + 1)) + 1 + 20);

						}
					}

				}
			}



		}


	}


	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (innerCellSize + 1) == 0) || (y % (innerCellSize + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (innerCellSize + 1);
		y = y / (innerCellSize + 1);

		if (x < 0 || x > totalColums  || y < 0 || y > totalRows ) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (innerCellSize + 1) == 0) || (y % (innerCellSize + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (innerCellSize + 1);
		y = y / (innerCellSize + 1);

		if (x < 0 || x > totalColums || y < 0 || y > totalRows ) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}

	private void mineGenerator(){ //generates mine positions


		int x = 0;
		int y = 0;

		for(int i = 0; i<totalBombs; ){
			x = generator.nextInt(9);
			y = generator.nextInt(9);

			if(!mines[x][y]){

				minesPosition[i][0] = x;
				minesPosition[i][1] = y;
				mines[x][y] = true;
				i++;
			}

		}

	}
	public boolean endGame(){

		return endGame;


	}
	public boolean winGame(){ //Compares if maximum gray squares have been discovered
		int counter = 0;
		boolean win = false;
		for (int i = 0; i < totalColums; i++){
			for (int j = 0; j < totalRows; j++){

				if (colorArray[i][j].equals(Color.LIGHT_GRAY)){
					counter++;
				}

			}
		}
		if (counter == selectedSquares){
			win = true;
		}
		else{
			counter = 0;
		}
		return win;
	}
	public boolean mineCompare(int x, int y){ //compares clicked position to mine position
		for(int i = 0; i < totalBombs; i++){
			if(x == minesPosition[i][0] && y == minesPosition[i][1]){
				endGame = true;
				repaint();
				return true;
			}


		}

		return false;

	}
	private boolean mineCompareNumber(int x, int y){ //compares all adjacent squares with clicked square
		for(int i = 0; i < totalBombs; i++){
			if(x == minesPosition[i][0] && y == minesPosition[i][1]){
				return true;
			}
		}
		return false;
	}
	private void numberGenerator(){ //Generates the number of mines adjacent to the clicked square
		int mineCount = 0;

		for(int m = 0; m < bombColums; m++){
			for(int n =0; n < bombRows; n++){

				if(mineCompareNumber(m+1, n)){
					mineCount++;			
				}
				if(mineCompareNumber(m+1, n+1)){
					mineCount++;			
				}
				if(mineCompareNumber(m, n+1)){
					mineCount++;			
				}
				if(mineCompareNumber(m-1, n+1)){
					mineCount++;			
				}
				if(mineCompareNumber(m-1, n)){
					mineCount++;			
				}
				if(mineCompareNumber(m-1, n-1)){
					mineCount++;			
				}
				if(mineCompareNumber(m, n-1)){
					mineCount++;			
				}
				if(mineCompareNumber(m+1, n-1)){
					mineCount++;			
				}
				mineNumbers[m][n] = mineCount;

				mineCount = 0;
			}

		}

	}
	public void blankFinder(int a, int b){ //calculates the amount of 0 adjacent to a square where isBlank = true
		if((a >= 0) && (b >= 0) && (a < 9) && (b < 9) && (!Blank[a][b])){

			if(mineNumbers[a][b] == 0 ){
				colorArray[a][b] = Color.LIGHT_GRAY;
				HiddenNumber[a][b] = true;
				Blank[a][b] = true;
				//if((a+1 < 9 && n+1 < 9) && (a-1 >=0 && n-1 >= 0)){


				if(!mineCompareNumber(a+1, b) && (a+1 < 9 )){
					colorArray[a+1][b] = Color.LIGHT_GRAY;
					HiddenNumber[a+1][b] = true;
					blankFinder(a+1,b);

				}
				if(!mineCompareNumber(a+1, b+1) && (a+1 < 9 && b+1 < 9) ){
					colorArray[a+1][b+1] = Color.LIGHT_GRAY;
					HiddenNumber[a+1][b+1] = true;
					blankFinder(a+1,b+1);
				}if(!mineCompareNumber(a, b+1) && ( b+1 < 9)){
					colorArray[a][b+1] = Color.LIGHT_GRAY;
					HiddenNumber[a][b+1] = true;
					blankFinder(a,b+1);
				}if(!mineCompareNumber(a-1, b+1) && (b+1 < 9) && (a-1 >=0)){
					colorArray[a-1][b+1] = Color.LIGHT_GRAY;
					HiddenNumber[a-1][b+1] = true;
					blankFinder(a-1,b+1);
				}if(!mineCompareNumber(a-1, b) && (a-1 >=0)){
					colorArray[a-1][b] = Color.LIGHT_GRAY;
					HiddenNumber[a-1][b] = true;
					blankFinder(a-1,b);
				}if(!mineCompareNumber(a-1, b-1)&& (a-1 >=0 && b-1 >= 0)){
					colorArray[a-1][b-1] = Color.LIGHT_GRAY;
					HiddenNumber[a-1][b-1] = true;
					blankFinder(a-1,b-1);
				}if(!mineCompareNumber(a, b-1)&& ( b-1 >= 0)){
					colorArray[a][b-1] = Color.LIGHT_GRAY;
					HiddenNumber[a][b-1] = true;
					blankFinder(a,b-1);
				}if(!mineCompareNumber(a+1, b-1)&& (a+1 < 9) && ( b-1 >= 0)){
					colorArray[a+1][b-1] = Color.LIGHT_GRAY;
					HiddenNumber[a+1][b-1] = true;
					blankFinder(a+1,b-1);
				}

			}

		}

		repaint();
	}
	public void isBlank(int x, int y){ //boolean that determines if clicked square is 0
		if(mineNumbers[x][y] == 0){
			blankFinder(x,y);

		}

	}


}
