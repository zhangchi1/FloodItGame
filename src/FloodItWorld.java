import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//Assignment 9
//Wong Judy
//jwmwong
//Zhang Chi
//zhangchi
public class FloodItWorld extends World {
  // Fields:
  // size of this flood game
  int size;
  // width and height of this game screen
  int width;
  int height;
  // All the cells of the game
  ArrayList<Cell> board;
  // number of colors in the board
  int numOfColor;
  // the steps that player takes
  // initialize the steps to zero
  int steps = 0;
  // set the maxStep
  int maxSteps;
  // set the y offset be 80 on the top of the screen in order to display game
  // information
  int yOffSet = 80;
  // set the frame width
  int frameSize = 8;
  // the timer that player takes, initialize the time to zero.
  double time = 0.00;

  // the current clicked color of this game
  Color currColor;
  Color clickedColor;

  // remember the last update status
  ArrayList<Cell> previous;
  int undoTimes = 1;
  Color previousColor;

  /**
   * constructor of this FloodWorld game
   * 
   * @param size
   *          is the size of this flood game
   * @param numOfColor
   *          is the number of color use in this flood game
   */
  FloodItWorld(int size, int numOfColor) {
    super();
    if (size >= 1 && numOfColor >= 1 && numOfColor <= 8) {
      this.size = size;
      this.numOfColor = numOfColor;
      this.width = this.size * 30 + 2 * this.frameSize;
      this.height = this.size * 30 + 80 + 2 * this.frameSize;
      this.board = this.cellsGenerator(size, numOfColor);
      this.currColor = this.board.get(0).getColor();
      Double d = Math.ceil((this.size * 0.275 - 0.048) * (this.numOfColor * 1.1 - 0.048));
      this.maxSteps = d.intValue();
      this.previous = this.board;
    }
    else {
      throw new IllegalArgumentException("Please input valid size and number of colors, "
          + "size and number of colors must be greater than 0, colors must be less than 9!");
    }
  }

  /**
   * An empty constructor that use to initialize the flood it world and for
   * testing purpose
   */
  FloodItWorld() {
    super();
    this.size = 2;
    this.numOfColor = 2;
    this.width = this.size * 30 + 2 * this.frameSize;
    this.height = this.size * 30 + 80 + 2 * this.frameSize;
    ArrayList<Cell> board = new ArrayList<Cell>(
        Arrays.asList(new Cell(23, 103, Color.red), new Cell(53, 103, Color.blue),
            new Cell(23, 133, Color.red), new Cell(53, 133, Color.blue)));
    this.setAdjacentCells(board);
    this.board = board;
    this.currColor = this.board.get(0).getColor();
    Double d = Math.ceil((this.size * 0.275 - 0.048) * (this.numOfColor * 1.1 - 0.048));
    this.maxSteps = d.intValue();
    this.previous = this.board;

  }

  // reset the game to the beginning (initial) status
  public void resetGame() {
    this.steps = 0;
    this.board = this.cellsGenerator(this.size, this.numOfColor);
    this.time = 0.00;
    this.currColor = this.board.get(0).getColor();
    this.clickedColor = null;
    this.previous = this.board;
    this.undoTimes = 1;

  }

  // undo the game for one step
  public void undo() {
    if (this.steps > 0 && this.undoTimes == 1) {
      this.steps = this.steps - 1;
      this.board = this.previous;
      this.currColor = this.previousColor;
      this.clickedColor = null;
      this.undoTimes = this.undoTimes - 1;
    }

  }

  // generate a list of random cells based on the size of the grid and the
  // number of colors
  public ArrayList<Cell> cellsGenerator(int size, int numOfColor) {
    ArrayList<Cell> result = new ArrayList<Cell>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        result.add(new Cell(15 + j * 30 + this.frameSize,
            this.yOffSet + 15 + i * 30 + this.frameSize, this.colorGenerator(numOfColor)));
      }
    }
    this.setAdjacentCells(result);
    return result;
  }

  // given a list of cells in the board set adjacent cells for each cell
  public void setAdjacentCells(ArrayList<Cell> result) {
    // set adjacent cells for each cell
    for (int k = 0; k < result.size(); k++) {
      if (k - 1 >= 0) {
        result.get(k).left = result.get(k - 1);
      }

      if (k + 1 < result.size()) {
        result.get(k).right = result.get(k + 1);
      }

      if (k - this.size >= 0) {
        result.get(k).top = result.get(k - this.size);
      }

      if (k + this.size < result.size()) {
        result.get(k).bottom = result.get(k + this.size);
      }

    }
    // set the left boundary cells' left cells be empty
    for (int i = 0; i < result.size(); i = i + this.size) {
      result.get(i).left = null;
    }
    // set the right boundary cells' right cells be empty
    for (int j = this.size - 1; j < result.size(); j = j + this.size) {
      result.get(j).right = null;
    }
  }

  // generate a random color based on the color range
  // where the maximum range is 8
  public Color colorGenerator(int range) {
    Integer rand = new Random().nextInt(range);
    if (rand == 0) {
      return Color.green;
    }
    else if (rand == 1) {
      return Color.red;
    }
    else if (rand == 2) {
      return Color.orange;
    }
    else if (rand == 3) {
      return Color.yellow;
    }
    else if (rand == 4) {
      return Color.cyan;
    }
    else if (rand == 5) {
      return Color.pink;
    }
    else if (rand == 6) {
      return new Color(75, 0, 130);
    }
    else {
      return Color.gray;
    }

  }

  // get the clicked Cell based on the given position
  // set default to be the first cell
  public Cell getCell(Posn posn) {
    Cell temp = this.board.get(0);
    for (int i = 0; i < this.board.size(); i++) {
      if (this.board.get(i).x - 15 < posn.x && posn.x < this.board.get(i).x + 15
          && this.board.get(i).y - 15 < posn.y && posn.y < this.board.get(i).y + 15) {
        temp = this.board.get(i);
      }
    }
    return temp;
  }

  // flood the adjacent cells in the board based on the given clicked cell
  public void floodIt(Cell cell) {
    this.currColor = cell.getColor();
    // change flooded cells color
    for (int i = 0; i < this.board.size(); i++) {
      if (this.board.get(i).isFlooded()) {
        this.board.get(i).setColor(currColor);
        this.board.get(i).setAdjFlood();
      }

    }
  }

  // check whether all cells are flooded with the same color
  public boolean allFlooded() {
    boolean temp = this.board.get(0).isFlooded();
    for (int i = 0; i < this.board.size(); i++) {
      temp = temp && this.board.get(i).isFlooded();
    }
    return temp;
  }

  // the mouse clicked method that allows player to check on
  public void onMousePressed(Posn mouse) {
    Color saveColor = this.currColor;
    this.previousColor = saveColor;
    ArrayList<Cell> saveBoard = new ArrayList<Cell>(this.board.size());
    for (Cell c : this.board) {
      Cell copy = c;
      saveBoard.add(new Cell(copy.x, copy.y, copy.getColor()));
    }
    this.setAdjacentCells(saveBoard);
    this.previous = saveBoard;

    this.board.get(0).setFlood();
    this.board.get(0).setAdjFlood();
    this.clickedColor = this.getCell(mouse).getColor();

    // add steps by one for player's each click on unflooded, not same color
    // cell
    if (!this.getCell(mouse).isFlooded() && !this.currColor.equals(this.clickedColor)) {
      this.floodIt(this.getCell(mouse));
      this.steps = this.steps + 1;
    }
  }

  // the on key event method use to allows players to click on keys and do
  // certain functions
  // press "r" key to reset the game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.resetGame();
    }
    else if (key.equals("u") && !this.allFlooded()) {
      this.undo();
    }

  }

  /**
   * On tick method that update the game for each tick if player doesn't win the
   * game, runs the timer
   */
  public void onTick() {
    if (!this.allFlooded()) {
      this.time += 0.5;
      this.makeScene();

    }
  }

  // produce the image of this world by adding:
  // 1. the cells with board. 2. the grid size text. 3. the text of steps that
  // play used .
  // 4. the time text
  public WorldScene makeScene() {
    WorldScene temp = this.getEmptyScene();
    for (int i = 0; i < this.board.size(); i++) {
      temp.placeImageXY(this.board.get(i).cellImage(), this.board.get(i).x, this.board.get(i).y);
    }
    temp.placeImageXY(new LineImage(new Posn(this.width * 2, 0), Color.BLACK), 0, this.yOffSet);
    // add frames
    temp.placeImageXY(
        new RectangleImage(this.width, this.frameSize, OutlineMode.SOLID, Color.BLACK),
        this.width / 2, this.yOffSet + this.frameSize / 2);
    temp.placeImageXY(
        new RectangleImage(this.width, this.frameSize, OutlineMode.SOLID, Color.BLACK),
        this.width / 2, this.height - this.frameSize / 2);
    temp.placeImageXY(new RectangleImage(this.frameSize, this.height - this.yOffSet,
        OutlineMode.SOLID, Color.BLACK), this.frameSize / 2, this.height / 2 + this.yOffSet / 2);
    temp.placeImageXY(new RectangleImage(this.frameSize, this.height - this.yOffSet,
        OutlineMode.SOLID, Color.BLACK), this.width - this.frameSize / 2,
        this.height / 2 + this.yOffSet / 2);
    // add steps text
    temp.placeImageXY(
        new TextImage("Steps: " + this.steps + "/" + this.maxSteps, 14, FontStyle.BOLD, Color.RED),
        50, 24);
    // add timer text
    temp.placeImageXY(new TextImage("Time: " + this.time, 14, FontStyle.BOLD, Color.RED), 42, 44);
    temp.placeImageXY(new TextImage("Flood-It", 14, FontStyle.BOLD, Color.BLACK), this.width / 2,
        10);

    // add lose and win texts
    if (this.steps <= this.maxSteps && this.allFlooded()) {
      temp.placeImageXY(new TextImage("You Win!", 16, FontStyle.BOLD, new Color(255, 20, 147)),
          this.width / 2, 64);
    }
    else if (this.steps > this.maxSteps) {
      temp.placeImageXY(new TextImage("You Lose!", 16, FontStyle.BOLD, new Color(142, 56, 142)),
          this.width / 2, 64);
    }
    return temp;
  }

} // End FloodItWorld class

// FloodIt world examples and tests
class FloodItExamples {

  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;

  public void initWorld() {
    cell1 = new Cell(1, 20, Color.GREEN);
    cell2 = new Cell(2, 20, Color.CYAN);
    cell3 = new Cell(1, 19, Color.CYAN);
    cell4 = new Cell(1, 18, Color.GREEN);
  }

  /*
   * Tests for Cell class
   */

  // test cellImage()
  public void testCellImage(Tester t) {
    this.initWorld();
    t.checkExpect(this.cell1.cellImage(),
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.GREEN));
    t.checkExpect(this.cell2.cellImage(),
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN));
    t.checkExpect(this.cell3.cellImage(),
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN));
    t.checkExpect(this.cell4.cellImage(),
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.GREEN));
  }

  // test setAdjacentFlood()
  public void testSetAdjFlood(Tester t) {
    this.initWorld();
    FloodItWorld w = new FloodItWorld();
    ArrayList<Cell> l1 = new ArrayList<Cell>(
        Arrays.asList(new Cell(23, 103, Color.red), new Cell(53, 103, Color.blue),
            new Cell(23, 133, Color.red), new Cell(53, 133, Color.blue)));
    w.setAdjacentCells(l1);
    t.checkExpect(l1.get(0).isFlooded(), false);
    t.checkExpect(l1.get(2).isFlooded(), false);
    t.checkExpect(l1.get(1).isFlooded(), false);
    t.checkExpect(l1.get(3).isFlooded(), false);

    l1.get(0).setFlood();
    t.checkExpect(l1.get(0).isFlooded(), true);
    l1.get(0).setAdjFlood();
    t.checkExpect(l1.get(2).isFlooded(), true);
    t.checkExpect(l1.get(1).isFlooded(), false);
    l1.get(1).setFlood();
    t.checkExpect(l1.get(1).isFlooded(), true);
    t.checkExpect(l1.get(3).isFlooded(), false);
    l1.get(1).setAdjFlood();
    t.checkExpect(l1.get(3).isFlooded(), true);
    t.checkExpect(l1.get(1).isFlooded(), true);

  }

  // test setFlood()
  public void testSetFlood(Tester t) {
    this.initWorld();
    t.checkExpect(this.cell1.flooded, false);
    this.cell1.setFlood();
    t.checkExpect(this.cell1.flooded, true);
    t.checkExpect(this.cell2.flooded, false);

    t.checkExpect(this.cell3.flooded, false);
    this.cell3.setFlood();
    t.checkExpect(this.cell3.flooded, true);

  }

  // test getColor()
  public void testGetColor(Tester t) {
    this.initWorld();
    t.checkExpect(this.cell1.getColor(), Color.GREEN);
    t.checkExpect(this.cell2.getColor(), Color.CYAN);
    t.checkExpect(this.cell3.getColor(), Color.CYAN);
    t.checkExpect(this.cell4.getColor(), Color.GREEN);
  }

  // test setColor()
  public void testSetColor(Tester t) {
    this.initWorld();
    t.checkExpect(this.cell1.getColor(), Color.GREEN);
    this.cell1.setColor(Color.YELLOW);
    t.checkExpect(this.cell1.getColor(), Color.YELLOW);
    t.checkExpect(this.cell2.getColor(), Color.CYAN);
    this.cell2.setColor(Color.red);
    t.checkExpect(this.cell2.getColor(), Color.red);

  }

  // test isFlooded()
  public void testIsFlooded(Tester t) {
    this.initWorld();
    t.checkExpect(this.cell1.isFlooded(), false);
    this.cell1.setFlood();
    t.checkExpect(this.cell1.isFlooded(), true);
    t.checkExpect(this.cell2.isFlooded(), false);
    t.checkExpect(this.cell3.isFlooded(), false);
    t.checkExpect(this.cell4.isFlooded(), false);
    this.cell4.setFlood();
    t.checkExpect(this.cell4.isFlooded(), true);
  }

  /*
   * Tests for FloodItWorld class
   */

  // tests the myBigBang and run the game
  public void testGame(Tester t) {
    FloodItWorld w = new FloodItWorld(20, 8);
    w.bigBang(w.width, w.height, 0.5);
  }

  // tests for resetGame method
  public void testresetGame(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.steps, 0);
    t.checkExpect(w.time, 0.00);
    w.onTick();
    t.checkExpect(w.time, 0.50);
    w.onMousePressed(new Posn(53, 103));
    t.checkExpect(w.steps, 1);
    w.resetGame();
    t.checkExpect(w.steps, 0);
    t.checkExpect(w.time, 0.00);
    t.checkExpect(w.clickedColor, null);

    FloodItWorld w1 = new FloodItWorld(1, 2);
    ArrayList<Cell> l1 = w1.board;
    t.checkExpect(w1.board.get(0), l1.get(0));
    w1.resetGame();
    ArrayList<Cell> l2 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.green)));
    ArrayList<Cell> l3 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.red)));
    t.checkOneOf(w1.board, l2, l3);
  }

  // tests for undo method
  public void testUndo(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.steps, 0);
    t.checkExpect(w.time, 0.00);
    w.onTick();
    t.checkExpect(w.time, 0.50);
    t.checkExpect(w.currColor, Color.red);

    w.onMousePressed(new Posn(53, 103));
    t.checkExpect(w.currColor, Color.blue);
    t.checkExpect(w.steps, 1);
    t.checkExpect(w.undoTimes, 1);
    w.undo();
    t.checkExpect(w.steps, 0);
    t.checkExpect(w.time, 0.50);
    t.checkExpect(w.undoTimes, 0);
    t.checkExpect(w.clickedColor, null);
    t.checkExpect(w.currColor, Color.red);

  }

  public void testIllegalInput(Tester t) {
    t.checkConstructorException(new IllegalArgumentException(
        "Please input valid size and number of colors, size and number of colors "
            + "must be greater than 0, colors must be less than 9!"),
        "FloodItWorld", 0, 2);
    t.checkConstructorException(new IllegalArgumentException(
        "Please input valid size and number of colors, size and number of colors "
            + "must be greater than 0, colors must be less than 9!"),
        "FloodItWorld", 0, 0);
    t.checkConstructorException(new IllegalArgumentException(
        "Please input valid size and number of colors, size and number of colors "
            + "must be greater than 0, colors must be less than 9!"),
        "FloodItWorld", 0, -2);
    t.checkConstructorException(new IllegalArgumentException(
        "Please input valid size and number of colors, size and number of colors "
            + "must be greater than 0, colors must be less than 9!"),
        "FloodItWorld", -1, 2);

  }

  // tests for cellsGenerator method
  public void testcellsGenerator(Tester t) {
    FloodItWorld w = new FloodItWorld();
    ArrayList<Cell> l1 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.green)));
    ArrayList<Cell> l2 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.red)));
    ArrayList<Cell> l3 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.orange)));
    ArrayList<Cell> l5 = new ArrayList<Cell>(Arrays.asList(new Cell(23, 103, Color.yellow)));

    ArrayList<Cell> l4 = new ArrayList<Cell>(
        Arrays.asList(new Cell(23, 103, Color.green), new Cell(53, 103, Color.green),
            new Cell(23, 133, Color.green), new Cell(53, 133, Color.green)));
    w.setAdjacentCells(l4);
    t.checkOneOf(w.cellsGenerator(1, 1), l1);
    t.checkOneOf(w.cellsGenerator(1, 3), l1, l2, l3);
    t.checkOneOf(w.cellsGenerator(1, 4), l1, l2, l3, l5);
    t.checkOneOf(w.cellsGenerator(0, 1), new ArrayList<Cell>());
    t.checkOneOf(w.cellsGenerator(2, 1), l4);
  }

  // tests for SetAdjacentCells method
  public void testSetAdjacentCells(Tester t) {
    FloodItWorld w1 = new FloodItWorld(2, 8);
    t.checkExpect(w1.board.get(1).left, w1.board.get(0));
    t.checkExpect(w1.board.get(3).left, w1.board.get(2));
    t.checkExpect(w1.board.get(3).top, w1.board.get(1));

    FloodItWorld w2 = new FloodItWorld();
    ArrayList<Cell> l3 = new ArrayList<Cell>(
        Arrays.asList(new Cell(23, 103, Color.green), new Cell(53, 103, Color.green),
            new Cell(23, 133, Color.green), new Cell(53, 133, Color.green)));
    w2.setAdjacentCells(l3);
    t.checkExpect(l3.get(0).right, l3.get(1));
    t.checkExpect(l3.get(0).bottom, l3.get(2));
    t.checkExpect(l3.get(1).left, l3.get(0));
    t.checkExpect(l3.get(1).bottom, l3.get(3));
    t.checkExpect(l3.get(2).top, l3.get(0));
    t.checkExpect(l3.get(2).right, l3.get(3));
    t.checkExpect(l3.get(3).left, l3.get(2));
    t.checkExpect(l3.get(3).top, l3.get(1));
  }

  // tests for colorGenerator method
  public void testColorGenerator(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkOneOf(w.colorGenerator(1), Color.green);
    t.checkOneOf(w.colorGenerator(2), Color.green, Color.red);
    t.checkOneOf(w.colorGenerator(3), Color.green, Color.red, Color.orange);
    t.checkOneOf(w.colorGenerator(4), Color.green, Color.red, Color.orange, Color.yellow);
    t.checkOneOf(w.colorGenerator(5), Color.green, Color.red, Color.orange, Color.yellow,
        Color.cyan);
    t.checkOneOf(w.colorGenerator(6), Color.green, Color.red, Color.orange, Color.yellow,
        Color.cyan, Color.pink);
    t.checkOneOf(w.colorGenerator(7), Color.green, Color.red, Color.orange, Color.yellow,
        Color.cyan, Color.pink, new Color(75, 0, 130));
    t.checkOneOf(w.colorGenerator(8), Color.green, Color.red, Color.orange, Color.yellow,
        Color.cyan, Color.pink, new Color(75, 0, 130), Color.gray);
  }

  // tests for getCell method
  public void testgetCell(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.getCell(new Posn(0, 1)), w.board.get(0));
    t.checkExpect(w.getCell(new Posn(23, 133)), w.board.get(2));
    t.checkExpect(w.getCell(new Posn(53, 103)), w.board.get(1));
    t.checkExpect(w.getCell(new Posn(53, 113)), w.board.get(1));
    t.checkExpect(w.getCell(new Posn(53, 133)), w.board.get(3));
    t.checkExpect(w.getCell(new Posn(w.width, w.height)), w.board.get(0));

  }

  // tests for floodIt method
  public void testFloodIt(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.board.get(0).isFlooded(), false);
    t.checkExpect(w.board.get(2).isFlooded(), false);
    w.floodIt(w.board.get(1));
    t.checkExpect(w.board.get(0).isFlooded(), false);
    t.checkExpect(w.board.get(1).isFlooded(), false);
    t.checkExpect(w.board.get(2).isFlooded(), false);
    t.checkExpect(w.currColor, w.board.get(1).getColor());

    FloodItWorld w1 = new FloodItWorld();
    t.checkExpect(w1.board.get(0).isFlooded(), false);
    t.checkExpect(w1.board.get(2).isFlooded(), false);
    w1.floodIt(w1.board.get(0));
    t.checkExpect(w1.board.get(0).isFlooded(), false);
    t.checkExpect(w1.board.get(1).isFlooded(), false);
    t.checkExpect(w1.board.get(2).isFlooded(), false);

    t.checkExpect(w1.currColor, w1.board.get(0).getColor());
  }

  // tests for allFlooded method
  public void testAllFlooded(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.allFlooded(), false);
    w.floodIt(w.board.get(0));
    t.checkExpect(w.allFlooded(), false);
    t.checkExpect(w.board.get(1).isFlooded(), false);
    w.floodIt(w.board.get(1));
    t.checkExpect(w.board.get(1).isFlooded(), false);
    t.checkExpect(w.allFlooded(), false);

    FloodItWorld w1 = new FloodItWorld();
    w1.onMousePressed(new Posn(53, 105));
    t.checkExpect(w1.allFlooded(), true);

  }

  // tests for onMousePressed method
  public void testOnMousePressed(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.steps, 0);
    t.checkExpect(w.board.get(1).isFlooded(), false);
    w.onMousePressed(new Posn(53, 103));
    t.checkExpect(w.steps, 1);
    t.checkExpect(w.board.get(0).getColor(), Color.blue);
    t.checkExpect(w.board.get(1).isFlooded(), true);
    t.checkExpect(w.board.get(3).isFlooded(), true);
    FloodItWorld w1 = new FloodItWorld(2, 3);
    t.checkExpect(w1.steps, 0);

    ArrayList<Cell> temp1 = w1.board;
    w1.onMousePressed(new Posn(3, 3));
    t.checkExpect(w1.steps, 0);
    t.checkExpect(w1.board.get(0).getColor(), temp1.get(0).getColor());
    t.checkExpect(w1.board.get(0).isFlooded(), true);
  }

  // tests for onKeyEvent method
  public void testOnKeyEvent(Tester t) {
    FloodItWorld w = new FloodItWorld();
    w.onTick();
    w.onKeyEvent("x");
    t.checkExpect(w.time, 0.5);
    w.onKeyEvent("r");
    t.checkExpect(w.time, 0.00);
    FloodItWorld w2 = new FloodItWorld(2, 1);
    w2.onTick();
    w2.onKeyEvent("x");
    t.checkExpect(w2.time, 0.5);
    w2.onKeyEvent("q");
    t.checkExpect(w2.time, 0.5);
    w2.onKeyEvent("r");
    t.checkExpect(w2.time, 0.00);
    ArrayList<Cell> l3 = new ArrayList<Cell>(
        Arrays.asList(new Cell(23, 103, Color.green), new Cell(53, 103, Color.green),
            new Cell(23, 133, Color.green), new Cell(53, 133, Color.green)));
    w2.setAdjacentCells(l3);
    t.checkExpect(w2.board, l3);
    FloodItWorld w3 = new FloodItWorld();
    w3.onMousePressed(new Posn(53, 103));
    t.checkExpect(w3.steps, 1);
    w3.onKeyEvent("r");
    t.checkExpect(w3.steps, 0);
    FloodItWorld w4 = new FloodItWorld();
    t.checkExpect(w4.steps, 0);
    w4.onTick();
    w4.onKeyEvent("u");
    t.checkExpect(w4.time, 0.5);
    w4.onMousePressed(new Posn(53, 103));
    t.checkExpect(w4.steps, 1);
    w4.onKeyEvent("u");
    t.checkExpect(w4.steps, 1);

  }

  // tests for onTick method
  public void testOnTick(Tester t) {
    FloodItWorld w = new FloodItWorld();
    t.checkExpect(w.time, 0.00);
    w.onTick();
    t.checkExpect(w.time, 0.50);
    w.onTick();
    t.checkExpect(w.time, 1.00);
    w.onTick();
    t.checkExpect(w.time, 1.50);
    w.floodIt(w.board.get(1));
    t.checkExpect(w.time, 1.50);
    w.onTick();
    t.checkExpect(w.allFlooded(), false);
    t.checkExpect(w.time, 2.0);

    FloodItWorld w1 = new FloodItWorld();
    t.checkExpect(w1.time, 0.00);
    w1.onTick();
    t.checkExpect(w1.time, 0.50);
    w1.onMousePressed(new Posn(53, 103));
    t.checkExpect(w1.allFlooded(), true);
    w1.onTick();
    t.checkExpect(w1.time, 0.50);

    FloodItWorld w2 = new FloodItWorld(2, 2);
    t.checkExpect(w2.time, 0.00);
    w2.onTick();
    t.checkExpect(w2.time, 0.50);
    w2.onTick();
    t.checkExpect(w2.time, 1.00);
    w2.onTick();
    t.checkExpect(w2.time, 1.50);
  }

  // tests for makeScene method
  public void testMakeScene(Tester t) {
    FloodItWorld w = new FloodItWorld();
    WorldScene temp = w.getEmptyScene();
    temp.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 103);
    temp.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 103);
    temp.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 133);
    temp.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 133);
    temp.placeImageXY(new LineImage(new Posn(152, 0), new Color(0, 0, 0)), 0, 80);
    temp.placeImageXY(new RectangleImage(w.width, w.frameSize, OutlineMode.SOLID, Color.BLACK),
        w.width / 2, w.yOffSet + w.frameSize / 2);
    temp.placeImageXY(new RectangleImage(w.width, w.frameSize, OutlineMode.SOLID, Color.BLACK),
        w.width / 2, w.height - w.frameSize / 2);
    temp.placeImageXY(
        new RectangleImage(w.frameSize, w.height - w.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w.frameSize / 2, w.height / 2 + w.yOffSet / 2);
    temp.placeImageXY(
        new RectangleImage(w.frameSize, w.height - w.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w.width - w.frameSize / 2, w.height / 2 + w.yOffSet / 2);
    temp.placeImageXY(
        new TextImage("Steps: " + w.steps + "/" + w.maxSteps, 14, FontStyle.BOLD, Color.RED), 50,
        24);
    temp.placeImageXY(new TextImage("Time: " + w.time, 14, FontStyle.BOLD, Color.RED), 42, 44);
    temp.placeImageXY(new TextImage("Flood-It", 14, FontStyle.BOLD, Color.BLACK), w.width / 2, 10);
    t.checkExpect(w.makeScene(), temp);

    FloodItWorld w1 = new FloodItWorld();
    WorldScene temp1 = w1.getEmptyScene();
    w1.floodIt(w1.board.get(1));
    t.checkExpect(w1.allFlooded(), false);
    t.checkExpect(w1.steps, 0);
    temp1.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 103);
    temp1.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 103);
    temp1.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 133);
    temp1.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 133);
    temp1.placeImageXY(new LineImage(new Posn(152, 0), new Color(0, 0, 0)), 0, 80);
    temp1.placeImageXY(new RectangleImage(w1.width, w1.frameSize, OutlineMode.SOLID, Color.BLACK),
        w1.width / 2, w1.yOffSet + w1.frameSize / 2);
    temp1.placeImageXY(new RectangleImage(w1.width, w1.frameSize, OutlineMode.SOLID, Color.BLACK),
        w1.width / 2, w1.height - w1.frameSize / 2);
    temp1.placeImageXY(
        new RectangleImage(w1.frameSize, w1.height - w1.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w1.frameSize / 2, w1.height / 2 + w1.yOffSet / 2);
    temp1.placeImageXY(
        new RectangleImage(w1.frameSize, w1.height - w1.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w1.width - w1.frameSize / 2, w1.height / 2 + w1.yOffSet / 2);
    temp1.placeImageXY(
        new TextImage("Steps: " + w1.steps + "/" + w1.maxSteps, 14, FontStyle.BOLD, Color.RED), 50,
        24);
    temp1.placeImageXY(new TextImage("Time: " + w1.time, 14, FontStyle.BOLD, Color.RED), 42, 44);
    temp1.placeImageXY(new TextImage("Flood-It", 14, FontStyle.BOLD, Color.BLACK), w1.width / 2,
        10);
    temp1.placeImageXY(new TextImage("You Win!", 16, FontStyle.BOLD, new Color(255, 20, 147)),
        w1.width / 2, 64);
    t.checkExpect(w1.makeScene(), temp1);

    FloodItWorld w2 = new FloodItWorld();
    WorldScene temp2 = w2.getEmptyScene();
    t.checkExpect(w2.allFlooded(), false);
    t.checkExpect(w2.steps, 0);
    w2.maxSteps = 0;
    t.checkExpect(w2.allFlooded(), false);

    temp2.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 103);
    temp2.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 103);
    temp2.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.red), 23, 133);
    temp2.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), 53, 133);
    temp2.placeImageXY(new LineImage(new Posn(152, 0), new Color(0, 0, 0)), 0, 80);
    temp2.placeImageXY(new RectangleImage(w2.width, w2.frameSize, OutlineMode.SOLID, Color.BLACK),
        w2.width / 2, w2.yOffSet + w2.frameSize / 2);
    temp2.placeImageXY(new RectangleImage(w2.width, w2.frameSize, OutlineMode.SOLID, Color.BLACK),
        w2.width / 2, w2.height - w2.frameSize / 2);
    temp2.placeImageXY(
        new RectangleImage(w2.frameSize, w2.height - w2.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w2.frameSize / 2, w2.height / 2 + w2.yOffSet / 2);
    temp2.placeImageXY(
        new RectangleImage(w2.frameSize, w2.height - w2.yOffSet, OutlineMode.SOLID, Color.BLACK),
        w2.width - w2.frameSize / 2, w2.height / 2 + w2.yOffSet / 2);
    temp2.placeImageXY(
        new TextImage("Steps: " + w2.steps + "/" + w2.maxSteps, 14, FontStyle.BOLD, Color.RED), 50,
        24);
    temp2.placeImageXY(new TextImage("Time: " + w2.time, 14, FontStyle.BOLD, Color.RED), 42, 44);
    temp2.placeImageXY(new TextImage("Flood-It", 14, FontStyle.BOLD, Color.BLACK), w2.width / 2,
        10);
    temp2.placeImageXY(new TextImage("You Lose!", 16, FontStyle.BOLD, new Color(142, 56, 142)),
        w2.width / 2, 64);
    t.checkExpect(w2.makeScene(), temp2);

  }

}
