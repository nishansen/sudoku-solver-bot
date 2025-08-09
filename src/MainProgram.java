import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MainProgram {
	
	public static void main(String[] args){
		
		//create object and run()
		MainProgram mainProgram = new MainProgram();
		mainProgram.run();
    	}
    
	public void run() {
		
		//two boards, one solved for the answers, one unsolved for the coordinate
		int[][] unsolved = ocrToArray();
		int[][] solved = solve(unsolved);
		
		/** robot class, care when using **/
		goBaymax(solved, unsolved); 

	}
	
	public int[][] solve(int[][] sudoku) {
    	
		int[][] solved = new int[9][9];

		//make copy of unsolved board, probably better way
		for (int i = 0; i < solved.length; i++) {
			for (int j = 0; j < solved.length; j++) {
				solved[i][j] = sudoku[i][j];
			}
		}
    	
    		//simple output
		System.out.println("Unsolved:");
		printSudokuBoard(sudoku);
		
		//solve algorithm
		SudokuSolver.solveSudoku(solved, 0, 0);

    		System.out.println("Solved:");		
		printSudokuBoard(solved);
		
		System.out.println("Backtracks = " + SudokuSolver.backtracks);
		
		return solved;
    	}
    
	private void goBaymax(int[][] solved, int[][] unsolved) {
    		try {
    		
    			//initialise stuff
			int x = 1015;
			int y = 295;
			int jump = 65;
			int miniJump = 6;

			int[] buttons = {1025, 1085, 1150, 1215, 1280, 1345, 1410, 1475, 1535};
			int buttonsY = 1110;

			Robot baymax = new Robot();

			//alt-tab
			baymax.keyPress(KeyEvent.VK_ALT);
			baymax.keyPress(KeyEvent.VK_TAB);
			baymax.keyRelease(KeyEvent.VK_ALT);
			baymax.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(1000);

			for(int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if(unsolved[i][j] == 0) {
						int digit = solved[i][j]; //get digit at exact cell

						//move to the cell
						baymax.mouseMove(x+jump*j+(j/3*miniJump), y+jump*i+(i/3*miniJump));
						baymax.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						baymax.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

						//press the number
						baymax.mouseMove(buttons[digit-1], buttonsY);
						baymax.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						baymax.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

						//sleep
						Thread.sleep(50);
					}
				}
			}

			//minimize button
			baymax.mouseMove(2445, 10);
			baymax.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			baymax.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		} 	
    	}
    
	//get name of file
	private String getFileName() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("File Name\n> ");
		String fileName = scanner.nextLine();

		//automatically adds .jpg because I'm lazy sometimes
		if(!fileName.contains(".")) fileName += ".jpg";
		fileName = "screenshots/" + fileName;
		scanner.close();
		return fileName;
	}
    
	//return list of images (File)
	public File[] getListOfImages(String fileName) {
		File[] arrayOfImages = new File[81];
    		int xOffset = 14, yOffset = 352;
    		int width, height;
    		int x, y, pos;
    	
    		width = height = 112;
    		x = y = pos = 0;
 
    		try {
    			//get screenshot, same resolution
			BufferedImage image = ImageIO.read(new File(fileName));
			for(int i = 0; i < 9; i++) {
				
				//manual cropping and jumping
				y = (i%3==0) ? y+7 : y+2;
				if(i%3==1) y+=1;
				
				for (int j = 0; j < 9; j++) {
					
					x = (j%3==0) ? x+7 : x+2;
					if(j%3==1) x+=1;
					
					int xPosition = (x+xOffset)+width*j;
					int yPosition = (y+yOffset)+height*i;
					
					//Get each tile
					BufferedImage sImage = image.getSubimage(xPosition, yPosition, width, height);
					
					//Create directory
					String directoryName = "temp";
					File directory = new File(directoryName);
					directory.mkdir();
					
					//Create files
					ImageIO.write(sImage, "jpg", new File(directoryName+"/"+pos+".jpg"));
					arrayOfImages[pos] = (new File(directoryName+"/"+pos+".jpg"));
					
					pos++;
				}
				
				x=0;
			}

			return arrayOfImages;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    		return null;
	}

	public int[][] ocrToArray() {

		//initialise stuff
		File[] listOfNumImages = getListOfImages(getFileName());
		int[][] sudokuBoard = new int[9][9]; //actual board

		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath(".../Tess4J"); // path to Tess4J directory

		//OCR to Board
		int position = 0;
		for(int i=0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				try {
					String number = tesseract.doOCR(listOfNumImages[position]).replaceAll("\n", "");

					if(number.matches("[0-9]+")) {
						sudokuBoard[i][j] = Integer.parseInt(number); 
					}else {
						sudokuBoard[i][j] = 0;
					}
					position++;

				} catch (NumberFormatException | TesseractException e) {
					e.printStackTrace();
				}
			}
    		}
    	
    		//Delete `temp` folder
		try {
			FileUtils.deleteDirectory(new File("temp"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sudokuBoard; //return board
	}
    
	private void printSudokuBoard(int[][] sudokuBoard) {

		//print any 2D board
		for (int i = 0; i < sudokuBoard.length; i++) {
			System.out.println(Arrays.toString(sudokuBoard[i]));
		}
	}
	
}
