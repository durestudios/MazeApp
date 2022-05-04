package edu.wm.cs.cs301.game2048;

import java.util.Arrays;
import java.lang.reflect.Member;
import java.util.*;

import javax.security.auth.callback.ConfirmationCallback;

import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Peter Kemper
 * Derived work
 * @author Jason Yoo
 */
public class State implements GameState {
	
	private Random rand = new Random();
	
	int[][] boardArray = {
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
	};
	
	/**
	 *	Constructor (critical)
	 *	This constructor is used for the tmp class object
	 *	used to compare values on tiles in the Game2048 class.
	 *	Iterates through our 2D array and copies values into the
	 *	object for comparison.
	 *	input & ouput: constructor
	 */
	public State(GameState original) {
		
		/* Create an instance of the State class to insert our array values */
		State compare = (State) original;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.boardArray[i][j] = compare.boardArray[i][j]; 
			}
		}
		
	}
	
	
	/** 
	 *	Constructor (supplementary)
	 *	We don't put anything in our constructor
	 *	since we initialize our instance variable
	 *	above.
	 */
	public State() {

	}
	
	
	/**
	 *	Critical
	 *	Override equals method since Java's equals method
	 *	points to memory address. Our version compares each
	 *	board array value to see if two boards are the same.
	 */
	@Override
	public boolean equals(Object obj) {
		
		boolean result = true;
		
	    if (obj == null) {
	        result = false;
	    }
		
		State compare = (State) obj;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.boardArray[i][j] != compare.boardArray[i][j]) {
					result = false;
				}
			}
		}
		
		return result;
		
	}
	
	
	/**
	 *	Supplementary
	 *	we need to implement a hash code method as we have overridden the 
	 *	equals method.
	 */
	public int hashCode() {
		int hash = 0;
		return hash;
	}
	
	
	//////////// Methods to Get/Set Values on the Board /////////////
	/**
	 *	Supplementary
	 *	Gets the value at a certain x-y coordinate on the board and returns it.
	 *	input: x-y coordinates
	 *	output: return the value at coordinate
	 */
	@Override
	public int getValue(int xCoordinate, int yCoordinate) {
		
		/* Flip to match X-Y coordinates to [row][column] format of arrays */
		int value = boardArray[yCoordinate][xCoordinate];

		return value;
		
	}

	
	/**
	 *	Supplementary
	 *	Changes the value of a certain x-y coordinate on the board to a
	 *	new input value.
	 *  input: x-y coordinates, value to insert
	 *  output: none
	 */
	@Override
	public void setValue(int xCoordinate, int yCoordinate, int value) {
		
		/* Flip to match X-Y coordinates to [row][column] format of arrays */
		boardArray[yCoordinate][xCoordinate] = value;
		
	}
	
	
	/**
	 * 	Supplementary
	 *	Resets every value on the board to 0. Traverse through array and set
	 *	all values to 0. Needs access to array containing board.
	 *	input: none
	 *	output: none
	 */
	@Override
	public void setEmptyBoard() {
				
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				boardArray[i][j] = 0;
			}
		}
		
	}

	
	/**
	 * 	Critical
	 *	Check the board for empty spots (value of 0). 
	 *	If there are any, choose one randomly to fill
	 *	with value 2 or 4, also chosen randomly.
	 *	Return true if it works (=there is an empty spot)
	 *	input: none
	 *	output: boolean value
	 */
	@Override
	public boolean addTile() {
		
		List<Integer> emptyIndexList = new ArrayList<Integer>();
		boolean isEmpty = false;
		int length = boardArray.length;
		
		/* Find and add all array indexes with value 0 to the list */
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (boardArray[i][j] == 0) {
					isEmpty = true;
					emptyIndexList.add(i);
					emptyIndexList.add(j);
				}
			}
		}
		
		/*  If we spotted a empty spot in the array, randomly fill one with 2 or 4 */
		if (isEmpty == true) {
			int chosenTile = rand.nextInt(emptyIndexList.size());
			
			/**	
			 *	Since the row indexes of the 2D array are stored in even number 
			 *	index positions, if the random number chosen is odd, we make it even
			 *	to position ourselves to get the correct corresponding column index 
			 *	by simply adding one
			 */
			if (chosenTile % 2 == 1) { 
				chosenTile -= 1;
			}
			int chosenRowIndex = emptyIndexList.get(chosenTile);
			int chosenColumnIndex = emptyIndexList.get(chosenTile + 1);
			
			/* Inserts 2 or 4 based on random number into chosen index */
			boardArray[chosenRowIndex][chosenColumnIndex] = (rand.nextInt(2)+1)*2;
		}
		// debugging
//		for (int i = 0; i < length; i++) {
//			System.out.print("//");
//			for (int j = 0; j < length; j++) {
//				System.out.print(boardArray[i][j]);
//			}
//		}
//		System.out.println("^^^ array");
		return isEmpty;
		
	}

	
	/**
	 *	Supplementary
	 *	Checks if board is full (no index has a value of 0).
	 *	Returns true if full, False otherwise
	 *	input: none
	 *	output: boolean value
	 */
	@Override
	public boolean isFull() {
		
		boolean hasZero = true;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (boardArray[i][j] == 0) {
					hasZero = false;
					break;
				}
			}
		}
		
		return hasZero;
		
	}
	
	
	/**
	 *	Supplementary
	 *	Check if any tile can be merged using a move operation,
	 *	a.k.a. check if there is a pair of tiles on a column or row
	 *	adjacent to each other with the same value. Returns True if 
	 *	there is a pair, False otherwise.
	 *	input: none
	 *	output: boolean value 
	 */
	@Override
	public boolean canMerge() {
		
		boolean foundPair = false;
		
		// check pairs on columns by comparing values on every spot on
		// rows 1,2,3 to the spot below it. 
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				// makes sure a pair of value 0 is not recognized as a pair
				if (boardArray[i][j]!= 0 && (boardArray[i][j] == boardArray[i][j+1])) {
					foundPair = true;
					break;
				}
			}
		}
		
		// check pairs on rows by comparing values on every spot on
		// columns 1,2,3 to the spot on its right.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				// makes sure a pair of value 0 is not recognized as a pair
				if (boardArray[i][j] != 0 && (boardArray[i][j] == boardArray[i+1][j])) {
					foundPair = true;
					// debug
					//System.out.println("pair found on row" + i + "column" + j);
				}
			}
		}
		
		return foundPair;
		
	}

	
	/**
	 *	Supplementary
	 *	Check if any tile on the board has reached the value
	 *	2048 or greater, and return true if so. Signifies the
	 *	end of the game. Returns True if tile >= 2048, 
	 *	False otherwise.
	 *	input: none
	 *	output: boolean value
	 */
	@Override
	public boolean reachedThreshold() {
		
		boolean gameOver = false;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (boardArray[i][j] >= 2048) {
					gameOver = true;
					break;
				}
			}
		}
		
		return gameOver;
	}

	
	//// Move Operations /////////////////
	
	/**
	 *	Critical
	 *	Move all tiles left, and merge any adjacent pairs.
	 *	Any merges are returned as points accrued during this move.
	 *	We first shift each row left to fill any empty spaces,
	 *	then merge adjacent tiles. We place the new merged tile on the left
	 *	index, then shift again to account for any empty spaces.
	 *	input: none
	 *	output: total points accrued by doing this move
	 */
	@Override
	public int left() {
		
		int points = 0;
		
		shiftLeft();
		
		/* merge tiles */
		for (int i = 0; i < 4; i++) {
			int n = 0;	// start from leftmost index
			while ( n < 3 ) {
				if (boardArray[i][n] == boardArray[i][n+1]) {	
					boardArray[i][n] = (boardArray[i][n]) * 2;	
					points += (boardArray[i][n]);
					boardArray[i][n+1] = 0;
					// if we merged a tile, then skip to the next index
					// since we don't need to merge again
					n += 1;
				}
				n += 1;
			}
		}
		
		shiftLeft();
		
		return points;
		
	}
	
	
	/*
	 *	Supplementary
	 *	Private function to shift values on the board
	 *	to the left. Based on insertion sort algorithm,
	 *	takes all non-zero values as shifts them left,
	 *	then places any zero values on the right.
	 *	input: none
	 *	output: none
	 */
	private void shiftLeft() {
		
		for (int i = 0; i < 4; i++) {
			int count = 0;
			
			for (int j = 0; j < 4; j++) {
				if(boardArray[i][j] != 0) {
					boardArray[i][count++] = boardArray[i][j];
				}
			}
			while (count < 4) {
				boardArray[i][count++] = 0;
			}
		}
		
	}

	
	/**
	 *	Critical
	 *	Move all tiles right, and merges any adjacent pairs.
	 *	Any merges are returned as points accrued during this move.
	 *	We first shift each row right to fill any empty spaces,
	 *	then merge adjacent tiles. We place the new merged tile on the right
	 *	index, then shift again to account for any empty spaces.
	 *	input: none
	 *	output: total points accrued by doing this move
	 */
	@Override
	public int right() {
		
		int points = 0;
		
		shiftRight();
		
		/* merge tiles */
		for (int i = 0; i < 4; i++) {
			int n = 3;		// start from the rightmost index 
			while ( n > 0 ) {	
				if (boardArray[i][n] == boardArray[i][n-1]) {	
					boardArray[i][n] = (boardArray[i][n]) * 2;	
					points += (boardArray[i][n]);
					boardArray[i][n-1] = 0;
					n -= 1;
					// debug: needs to stop here.... 
					// System.out.println("outside loop" + i + " inside loop: " + n );
				}
				n -= 1;
			}
		}
		
		shiftRight();
		
		return points;
		
	}
	
	
	/*
	 *	Supplementary
	 *	Private function to shift values on the board
	 *	to the right. Based on insertion sort algorithm,
	 *	takes all non-zero values as shifts them right,
	 *	then places any zero values on the left.
	 *	input: none
	 *	output: none
	 */
	private void shiftRight() {
		
		for (int i = 0; i < 4; i++) {
			int count = 3;
			
			for (int j = 3; j >= 0; j--) {
				if(boardArray[i][j] != 0) {
					boardArray[i][count--] = boardArray[i][j];
				}
			}
			while (count >= 0) {
				boardArray[i][count--] = 0;
			}
		}
		
	}

	
	/**
	 *	Critical
	 *	Move all tiles down, and merge any adjacent column pairs.
	 *	Any merges are returned as points accrued during this move.
	 *	We first shift each column down to fill any empty spaces,
	 *	then merge adjacent tiles. We place the new merged tile on
	 *	the index below, then shift down again to account for any empty 
	 *	spaces.
	 *	input: none
	 *	output: total points accrued by doing this move
	 */
	@Override
	public int down() {
		
		int points = 0;
		
		shiftDown();
		
		for (int i = 0; i < 4; i++) {
			
			int n = 3;
			
			while ( n > 0 ) {
				if (boardArray[n][i] == boardArray[n-1][i]) {
					boardArray[n-1][i] = (boardArray[n][i]) * 2;
					points += (boardArray[n][i]) * 2;
					boardArray[n][i] = 0;
					n -= 1;	
				}
				n -= 1;
			}
		}
		
		shiftDown();
		
		return points;
	}

	
	/*
	 *	Critical
	 *	Private function to shift values on the board
	 *	downwards. Based on insertion sort algorithm,
	 *	takes all non-zero values as shifts them down,
	 *	then places any zero values above.
	 *	input: none
	 *	output: none
	 */
	private void shiftDown() {
		
		for (int i = 0; i < 4; i++) {
			int count = 3;
			
			for (int j = 3; j >= 0; j--) {
				if(boardArray[j][i] != 0) {
					boardArray[count--][i] = boardArray[j][i];
				}
			}
			while (count >= 0) {
				boardArray[count--][i] = 0;
			}
		}
		
	}
		
	
	/**
	 *	Critical
	 *	Move all tiles up, and merges any adjacent column pairs.
	 *	Any merges are returned as points accrued during this move.
	 *	We first shift each column up to fill any empty spaces,
	 *	then merge adjacent tiles. We place the new merged tile on
	 *	the index above, then shift up again to account for any empty 
	 *	spaces.
	 *	input: none
	 *	output: total points accrued by doing this move
	 */
	@Override
	public int up() {
		
		int points = 0;
		
		shiftUp();
		
		// merge adjacent tiles
		for (int i = 0; i < 4; i++) {
			int n = 0;
			while ( n < 3 ) {
				if (boardArray[n][i] == boardArray[n+1][i]) {
					boardArray[n+1][i] = (boardArray[n][i]) * 2;
					points += (boardArray[n][i]) * 2;
					boardArray[n][i] = 0;
					n += 1; 
				}
				n +=1 ;
			}
		}
		
		shiftUp();
		
		return points;
	}
	
	
	/*
	 *	Supplementary
	 *	Private function to shift values on the board
	 *	upwards. Based on insertion sort algorithm,
	 *	takes all non-zero values as shifts them up,
	 *	then places any zero values down.
	 *	input: none
	 *	output: none
	 */
	private void shiftUp() {
		
		for (int i = 0; i < 4; i++) {
			int count = 0;
			
			for (int j = 0; j < 4; j++) {
				if(boardArray[j][i] != 0) {
					boardArray[count++][i] = boardArray[j][i];
				}
			}
			while (count < 4) {
				boardArray[count++][i] = 0;
			}
		}
		
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
