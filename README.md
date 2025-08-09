# Sudoku Solver Bot
Automatic Sudoku Solver Bot written in Java which uses OCR and the Robot class to automatically complete a sudoku puzzle on a phone. Also uses some backtracking algorithms and experiments with files and folders. 

- OCR was from [Tesseract](https://github.com/tesseract-ocr/tesseract)
- Android Phone Mirroring was from [Vysor](https://www.vysor.io/)

## What it does
Using **Vysor**, it takes a screenshot of the current sudoku game. It then manipulates the image to get the puzzle only, uses **Tesseract** to do OCR and uses the output to solve the sudoku using *backtracking algorithms*. The **Robot** class is then used to automatically solve the sudoku by pressing the correct number for each cell. (This only works for specific devices as the coordinates are mapped manually and are **NOT** universal for any device).
## Example
https://github.com/user-attachments/assets/e2ee7371-2993-4831-ba3c-98273d39d56e

## Possible Future Plans
- Make it completely automatic:
  - Take screenshot
  - Download screenshot
  - Load image
  - Navigate to puzzle
  - Complete the puzzle
  - Load another puzzle
