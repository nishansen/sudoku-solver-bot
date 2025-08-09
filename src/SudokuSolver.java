public class SudokuSolver {

	static int backtracks = 0;

	public static int[] findNextCellToFill(int[][]grid) {
		for (int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(grid[x][y] == 0) {
					return new int[] {x, y};
				}
			}
		}
		return new int[] {-1, -1};
	}
	
	public static boolean isValid(int[][] grid, int i, int j, int e) {
		boolean rowOk = allI(grid, i, e);
		if(rowOk) {
			boolean columnOk = allJ(grid, j, e);
			if(columnOk) {
				int secTopX = 3 * (i/3), secTopY = 3 * (j/3);
				for(int x=secTopX; x<secTopX+3; x++) {
					for(int y=secTopY; y<secTopY+3; y++) {
						if(grid[x][y] == e) {
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean solveSudoku(int[][] grid, int i, int j) {
		
		int[] array = findNextCellToFill(grid);
		i = array[0];
		j = array[1];
		
		if(i == -1) {
			return true;
		}
		
		for (int e=1; e<10; e++) {
			if(isValid(grid, i, j, e)) {
				grid[i][j] = e;
				if (solveSudoku(grid, i, j)) {
					return true;
				}
				backtracks += 1;
				grid[i][j] = 0;
			}
		}
		return false;
	}
	
	public static void printSudoku(int[][] grid) {
		for (int i=1;i<=grid.length;i++)
		{
	       System.out.print("[");
		   for (int j=1;j<=grid.length;j++)
		   {
			   if(j%3==0) {
				   if(j==grid.length)
					   System.out.print(grid[i-1][j-1] + "]");
				   else
					   System.out.print(grid[i-1][j-1] + "]  [");
			   }else 
			   System.out.print(grid[i-1][j-1] + ", ");
		   }
		   System.out.println();
		   if(i%3==0)
			   System.out.println();
		}
	}
	
	public static boolean allI(int[][] grid, int i, int e) {
		for (int x=0; x<9; x++) {
			if(grid[i][x] == e) return false;
		}
		return true;
	}
	
	public static boolean allJ(int[][] grid, int j, int e) {
		for (int x=0; x<9; x++) {
			if(grid[x][j] == e) return false;
		}
		return true;
	}
}
