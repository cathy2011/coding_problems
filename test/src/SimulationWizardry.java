import java.util.Scanner;

/**
 * 114 - Simulation Wizardry
 * 
 * @author QQ
 *
 */
public class SimulationWizardry {

  private static enum Direction {
    RIGHT(0, 1, 0), UP(1, 0, 1), LEFT(2, -1, 0), DOWN(3, 0, -1);

    public static Direction toDirection(int d) {
      Direction direction = null;
      switch (d) {
        case 0:
          direction = RIGHT;
          break;
        case 1:
          direction = UP;
          break;
        case 2:
          direction = LEFT;
          break;
        case 3:
          direction = DOWN;
          break;
        default:
          direction = null;
      }
      return direction;
    }

    private int direction;
    private int changeX;
    private int changeY;

    private Direction(int direction, int changeX, int changeY) {
      this.direction = direction;
      this.changeX = changeX;
      this.changeY = changeY;
    }

    public int getChangeX() {
      return changeX;
    }

    public int getChangeY() {
      return changeY;
    }

    public Direction turn() {
      return toDirection((this.direction - 1 + 4) % 4);
    }

  }

  private static class Playboard {

    private int m;
    private int n;
    private boolean[][] hasBump;
    private int[][] credits;
    private int[][] costs;
    private int wallHitCost;

    public Playboard(Scanner in) {
      m = in.nextInt();
      n = in.nextInt();
      wallHitCost = in.nextInt();
      credits = new int[m][n];
      costs = new int[m][n];
      hasBump = new boolean[m][n];
      fill(hasBump, false);
      int numOfBumps = in.nextInt();
      for (int i = 0; i < numOfBumps; ++i) {
        int x = in.nextInt() - 1;
        int y = in.nextInt() - 1;
        credits[x][y] = in.nextInt();
        costs[x][y] = in.nextInt();
        hasBump[x][y] = true;
      }
    }

    public int getM() {
      return m;
    }

    public int getN() {
      return n;
    }

    public int[][] getCredits() {
      return credits;
    }

    public int[][] getCosts() {
      return costs;
    }

    public int getWallHitCost() {
      return wallHitCost;
    }

    public boolean[][] getHasBump() {
      return hasBump;
    }

    private void fill(boolean[][] arr, boolean initialValue) {
      for (int i = 0; i < arr.length; ++i) {
        for (int j = 0; j < arr[i].length; ++j) {
          arr[i][j] = initialValue;
        }
      }
    }
  }

  private static class Ball {

    private Playboard playboard;

    // Where the ball currently is.
    private int posX;
    private int posY;
    // Current direction of the ball.
    private Direction currentDirection;
    // Remaining lifetime.
    private int remainingLifetime;
    // Credits accumulated so far.
    private int credits;

    public Ball(Playboard playboard, int posX, int posY, Direction currentDirection,
        int remainingLifetime) {
      this.playboard = playboard;
      this.posX = posX;
      this.posY = posY;
      this.currentDirection = currentDirection;
      this.remainingLifetime = remainingLifetime;
      this.credits = 0;
    }

    public int getCredits() {
      return credits;
    }

    // Whether this ball is still alive.
    public boolean alive() {
      return this.remainingLifetime > 0;
    }

    public void roll() {
      if (--remainingLifetime <= 0) {
        // Dead already. Cannot proceed to hit anything.
        return;
      }

      int nextX = posX + currentDirection.getChangeX();
      int nextY = posY + currentDirection.getChangeY();
      boolean hitWall = nextX == 0 || nextX == playboard.getM() - 1 || nextY == 0
          || nextY == playboard.getN() - 1;
      if (hitWall) { // Hit the wall.
        remainingLifetime -= playboard.getWallHitCost();
        currentDirection = currentDirection.turn();
        return;
      }
      boolean hitBump = playboard.getHasBump()[nextX][nextY];
      if (hitBump) { // Hit the bump.
        remainingLifetime -= playboard.getCosts()[nextX][nextY];
        credits += playboard.getCredits()[nextX][nextY];
        currentDirection = currentDirection.turn();
        return;
      }
      posX = nextX;
      posY = nextY;
    }

  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      Playboard playboard = new Playboard(in);
      int totalCredits = 0;
      while (in.hasNextInt()) {
        Ball ball = new Ball(playboard, in.nextInt() - 1, in.nextInt() - 1,
            Direction.toDirection(in.nextInt()), in.nextInt());
        while (ball.alive()) {
          ball.roll();
        }
        int creditsEarned = ball.getCredits();
        System.out.println(creditsEarned);
        totalCredits += creditsEarned;
      }
      System.out.println(totalCredits);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
