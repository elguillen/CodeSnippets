class Solution {
    
    public void solveSudoku(char[][] board) {
        //boolean result = partialSudokuSolver(board);
        final int NUM_ROWS = board.length;
        final int NUM_COLS = board[0].length;
        final int RADIX_DECIMAL = 10;
        
        if (NUM_ROWS != NUM_COLS)
            return;
        
        Character[] digitsArr = IntStream.rangeClosed(1, NUM_ROWS)
                                .mapToObj(x -> Character.forDigit(x, RADIX_DECIMAL))
                                .toArray(Character[]::new);
        
        List<Character> digitsList = Arrays.asList(digitsArr);        
        Set<Character> digitsSet = new HashSet<Character>(digitsList);
        
        if (partialSudokuSolver(board))
            return;
        
        solveSudoku(digitsSet, board, NUM_ROWS);
    }
    
    public boolean isEmptyCell(char[][] board, int row, int column,
                                    Set<Character> digits) {  
        // cell considered empty if it is filled with a character
        // that does not correspond to one of the possible digits
        return !digits.contains(board[row][column]);      
    }
    
    public boolean passRowConstraint(char[][] board, int N, int row, int num) {
        
        for (int col = 0; col < N; col++)
            if (board[row][col] == Character.forDigit(num, 10))
                return false;
        
        return true;
    }
    
    public boolean passColConstraint(char[][]board, int N, int col, int num) {
        
        for (int row = 0; row < N; row++) 
            if (board[row][col] == Character.forDigit(num, 10))
                return false;
        
        return true;
    }
    
    public boolean passBoxConstraint(char[][]board, int N, int row, int col, int num) {
        
        int boxLength = (int) Math.sqrt((double) N);
        int rowStart = row - (row % boxLength);
        int rowEnd = rowStart + boxLength;
        int colStart = col - (col % boxLength);
        int colEnd = colStart + boxLength;
        
        for (int r = rowStart; r < rowEnd; r++)
            for (int c = colStart; c < colEnd; c++) 
                if (board[r][c] == Character.forDigit(num, 10))
                    return false;
        
        return true;
    }
    
    public boolean passConstraints(char[][] board, int N, int row, int col, int num) {
        
        return passRowConstraint(board, N, row, num) &&
                passColConstraint(board, N, col, num) &&
                passBoxConstraint(board, N, row, col, num);
    }
    
    public boolean solveSudoku(Set<Character> digits, char[][] board, int N) {
        
        int row = -1;
        int col = -1;
        
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (isEmptyCell(board, r, c, digits)) {
                    row = r;
                    col = c;
                }
            }
        }
        
        // all cell values have been filled so sudoku
        // has been solved
        if (row == -1 || col == -1)
            return true;
        
        for (int num = 1; num <= N; num++) {
            
            if (passConstraints(board, N, row, col, num)) {
                
                board[row][col] = Character.forDigit(num, 10);
                
                if (solveSudoku(digits, board, N))
                    return true;
                else
                    board[row][col] = '.';
            }
        }
        
        return false;
    }
    
    public boolean partialSudokuSolver(char[][] board) {      
        final int NUM_ROWS = board.length;
        final int NUM_COLS = board[0].length;
        final int RADIX_DECIMAL = 10;
        
        // sum of the first n numbers (excludes zero) is:
        //      (n * (n + 1)) / 2
        final int MAX_SUM = (NUM_ROWS * (NUM_ROWS + 1)) / 2;
        
        final int ROWS_BOX = (int) Math.sqrt((double) NUM_ROWS);
        final int COLS_BOX = (int) Math.sqrt((double) NUM_COLS);
        
        //for 9x9 puzzle: {'1', '2', '3', '4', '5', '6', '7', '8', '9'}
        Character[] digitsArr = IntStream.rangeClosed(1, NUM_ROWS)
                                .mapToObj(x -> Character.forDigit(x, RADIX_DECIMAL))
                                .toArray(Character[]::new);
        
        List<Character> digitsList = Arrays.asList(digitsArr);        
        Set<Character> digitsSet = new HashSet<Character>(digitsList);        

        // if a cell has been filled then we want to iterate through
        // entire array and keep filling empty cells with values until
        // no longer possible with this strategy
        boolean cellFilled;
        
        do {
            
            cellFilled = false;
            Set<Character> foundSoFar = new HashSet<Character>();
            Set<Character> notFound = new HashSet<Character>(digitsList);
          
            // Try to find if we can eliminate all but one
            // digit from the possible digits
            for (int row = 0; row < NUM_ROWS; row++) {
                for (int col = 0; col < NUM_COLS; col++) {
                    
                    boolean emptyCell = isEmptyCell(board, row, col, digitsSet);
                    
                    if (emptyCell) {
                        
                        for (int c = 0; c < NUM_COLS; c++) {
                            Character cellVal = board[row][c];
                            if (digitsSet.contains(cellVal)) {
                                if (foundSoFar.contains(cellVal)) {
                                    // violates row constraint (digit occurs twice in row)
                                    return false;
                                } else {
                                    foundSoFar.add(cellVal);
                                    notFound.remove(cellVal);
                                }
                            }
                        }
                        
                        // clear digits found to check for column constraint
                        foundSoFar = new HashSet<Character>();
                        
                        for (int r = 0; r < NUM_ROWS; r++) {
                            Character cellVal = board[r][col];
                            if (digitsSet.contains(cellVal)) {
                                if (foundSoFar.contains(cellVal)) {
                                    // violates column constraint (digit occurs twice in column)
                                    return false;
                                } else {
                                    foundSoFar.add(cellVal);
                                    notFound.remove(cellVal);
                                }
                            }
                        }
                        
                        // clear digits found to check for box constraint
                        foundSoFar = new HashSet<Character>();
                        
                        int rowStart = row - (row % ROWS_BOX);
                        int rowEnd = rowStart + ROWS_BOX;
                        int colStart = col - (col % COLS_BOX);
                        int colEnd = colStart + COLS_BOX;
                        
                        for (int r = rowStart; r < rowEnd; r++) {
                            for (int c = colStart; c < colEnd; c++) {
                                Character cellVal = board[r][c];
                                if (digitsSet.contains(cellVal)) {
                                    if (foundSoFar.contains(cellVal)) {
                                        // violates box constaint (digit occurs twice in same box)
                                        return false;
                                    } else {
                                        foundSoFar.add(cellVal);
                                        notFound.remove(cellVal);
                                    }
                                }
                            }
                        }
                        
                        // clear digits found for next iteration
                        foundSoFar = new HashSet<Character>();
                      
                        // remaining value is valid digit in cell
                        if (notFound.size() == 1) {
                            Character cellDigit = notFound.toArray(new Character[1])[0];
                            board[row][col] = cellDigit;
                            cellFilled = true;
                        }
                        
                        // repopulate all possible digits for next iteration
                        notFound = new HashSet<Character>(digitsList);
                    }
                }
            }
        } while (cellFilled);
        
        for (int row = 0; row < NUM_ROWS; row++) 
            for (int col = 0; col < NUM_COLS; col++)
                if (board[row][col] == '.')
                    return false;
            
        return true;
    }

}