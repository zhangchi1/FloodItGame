
import java.awt.Color;
import javalib.worldimages.*;

//Assignment 9
//Wong Judy
//jwmwong
//Zhang Chi
//zhangchi

//Represents a single square of the game area
public class Cell {
  // Fields:
  // In logical coordinates, with the origin at the top-left corner of the
  // screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  /**
   * constructor of this Cell
   * 
   * @param x
   *          is the x position
   * @param y
   *          is the y position
   * @param color
   *          is the color
   */
  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = false;
  }

  // draw this Cell as a square
  public WorldImage cellImage() {
    return new RectangleImage(30, 30, OutlineMode.SOLID, this.color);
  }

  // change this cell and all its adjacent cells with same color to flooded
  public void setAdjFlood() {
    if (this.bottom != null && this.getColor().equals(this.bottom.getColor())
        && !this.bottom.flooded) {
      this.bottom.setFlood();
      this.bottom.setAdjFlood();
    }

    if (this.right != null && this.getColor().equals(this.right.getColor())
        && !this.right.flooded) {
      this.right.setFlood();
      this.right.setAdjFlood();
    }

    if (this.top != null && this.getColor().equals(this.top.getColor()) && !this.top.flooded) {
      this.top.setFlood();
      this.top.setAdjFlood();
    }

    if (this.left != null && this.getColor().equals(this.left.getColor()) && !this.left.flooded) {
      this.left.setFlood();
      this.left.setAdjFlood();
    }

  }

  // set the this Cell be flooded
  public void setFlood() {
    this.flooded = true;
  }

  // get the color of this Cell
  public Color getColor() {
    return this.color;
  }

  // set the color of this Cell to the given color
  public void setColor(Color color) {
    this.color = color;
  }

  // check whether this Cell is flooded
  public boolean isFlooded() {
    return this.flooded;
  }
 

}
