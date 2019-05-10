package models;

import java.util.ArrayList;
import java.util.Random;

public class Table {
    private int baseNumber;
    private int numberOfRows;
    private int numberOfColumns;
    private Cell[][] cells = new Cell[numberOfRows][numberOfColumns];

    private int score = 0;

    public Table(int baseNumber, int numberOfRows, int numberOfColumns) {
        this.baseNumber = baseNumber;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        initCells(cells);
    }

    private void initCells(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[j].length; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private int makeNewNumber() {
        int zeroOrOne = Math.abs(new Random().nextInt()) % 2;
        return (zeroOrOne + 1) * baseNumber;
    }

    private void clockWiseQuarterRotation(int rotationCount) {
        for (int rotationNumber = 0; rotationNumber < rotationCount; rotationNumber++) {
            Cell[][] newCells = new Cell[numberOfColumns][numberOfRows];
            initCells(newCells);
            for (int i = 0; i < numberOfColumns; i++) {
                for (int j = 0; j < numberOfRows; j++) {
                    newCells[i][j].setValue(cells[numberOfRows - 1 - j][i].getValue());
                }
            }

            cells = newCells;
            int temp = numberOfRows;
            numberOfRows = numberOfColumns;
            numberOfColumns = temp;
        }
    }

    public void move(Directon directon) {
        int rotationCount = 4;
        switch (directon) {
            case LEFT:
                rotationCount = 3;
                break;
            case UP:
                rotationCount = 2;
                break;
            case RIGHT:
                rotationCount = 1;
                break;
            case DOWN:
                rotationCount = 0;
                break;
        }
        clockWiseQuarterRotation(rotationCount);
        dropDown();
        clockWiseQuarterRotation((4 - rotationCount) % 4);
    }

    private void dropDown() {
        if (!canDropDown()) {
            return;
        }
        for (int j = 0; j < numberOfColumns; j++) {
            if (canDropDownColumn(j)) {
                dropDownColumn(j);
            }
        }
    }

    private void dropDownColumn(int columnNumber) {
        ArrayList<Cell> newColumn = new ArrayList<>();

        outer:
        for (int i = numberOfRows - 1; i >= 0; i--) {
            Cell cell1 = cells[i][columnNumber];
            if (cell1.getValue() == 0) {
                continue;
            }

            if (i == 0) {
                newColumn.add(0, new Cell(cell1));
                continue;
            }

            for (int j = i - 1; j >= 0; j--) {
                Cell cell2 = cells[j][columnNumber];
                if (cell2.getValue() == 0) {
                    if (j == 0) {
                        newColumn.add(0, new Cell(cell1));
                        break outer;
                    } else {
                        continue;
                    }
                }

                if (cell1.getValue() == cell2.getValue()) {
                    Cell mergedCell = new Cell(cell1, cell2);
                    score += mergedCell.getValue();
                    newColumn.add(0, mergedCell);
                    i = j;
                    continue outer;
                } else {
                    newColumn.add(0, new Cell(cell1));
                    continue outer;
                }
            }
        }

        replaceColumn(columnNumber, newColumn);
    }

    private void replaceColumn(int columnNumber, ArrayList<Cell> newColumn) {
        for (int i = 0; i < numberOfRows; i++) {
            cells[i][columnNumber] = new Cell();
        }
        for (int i = 0; i < newColumn.size(); i++) {
            cells[i + numberOfRows - newColumn.size()][columnNumber] = new Cell(newColumn.get(i));
        }
    }

    private boolean canDropDown() {
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                if (canDropDownColumn(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDropDownColumn(int columnNumber) {
        if (!columnIsFull(columnNumber)) {
            return true;
        }
        for (int i = 0; i < numberOfRows - 1; i++) {
            if (cells[i][columnNumber].getValue() == cells[i + 1][columnNumber].getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean columnIsFull(int columnNumber) {
        for (int i = 0; i < numberOfRows; i++) {
            if (cells[i][columnNumber].getValue() == 0) {
                return false;
            }
        }
        return true;
    }
}
