package sudoku;

/**
 * Solver.java
 * Sudoku Solver class based on Backtracking algorithm.
 */
public class Solver
{
	public static boolean solve(int[][] sudoku)
	{
		return solve(sudoku, 0);
	}
	
	private static boolean solve(int[][] sudoku, int index)
	{
		if (index == 81)
			return true;
		
		int r = index / 9;
		int c = index % 9;
		
		if (sudoku[r][c] != 0)
		{
			return solve(sudoku, index + 1);
		} else
		{
			for (int d = 1; d <= 9; d++)
			{
				if (isSuitable(sudoku, d, r, c))
				{
					sudoku[r][c] = d;
					if (solve(sudoku, index + 1))
						return true;
				}
			}
			sudoku[r][c] = 0;
			return false;
		}
	}
	
	private static boolean isSuitable(int[][] sudoku, int digit, int r, int c)
	{
		int bx = r / 3 * 3;
		int by = c / 3 * 3;
		
		for (int i = 0; i < 9; i++)
		{
			// check row
			if (sudoku[r][i] == digit) return false;
			// check column
			if (sudoku[i][c] == digit) return false;
			// check box
			if (sudoku[bx + i / 3][by + i % 3] == digit) return false;
		}
		return true;
	}
	
	public static boolean isValidPuzzle(int[][] sudoku)
	{
		for (int[] r : sudoku)
		{
			int[] count = new int[10];
			for (int c = 0; c < 9; c++)
			{
				count[r[c]]++;
			}
			
			for (int i = 1; i <= 9; i++)
				if (count[i] != 0 && count[i] != 1)
					return false;
		}
		
		for (int c = 0; c < 9; c++)
		{
			int[] count = new int[10];
			for (int r = 0; r < 9; r++)
			{
				count[sudoku[r][c]]++;
			}
			
			for (int i = 1; i <= 9; i++)
				if (count[i] != 0 && count[i] != 1)
					return false;
		}
		
		for (int bx = 0; bx < 9; bx += 3)
		{
			for (int by = 0; by < 9; by += 3)
			{
				int[] count = new int[10];
				for (int i = 0; i < 9; i++)
					count[sudoku[bx + i / 3][by + i % 3]]++;
				
				for (int i = 1; i <= 9; i++)
					if (count[i] != 0 && count[i] != 1)
						return false;
			}
		}
		
		return true;
	}
}
