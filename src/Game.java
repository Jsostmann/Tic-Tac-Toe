import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
/**
 * Class that creates a Tic-Tac-Toe game.
 *
 * @author jsostmann@aggies.ncat.edu
 */
public class Game extends JFrame implements ActionListener {

  /** ImageIcon with picture of "X" for Player 1. */
  private static ImageIcon playerOne = new ImageIcon("src/media/xImage.png");

  /** ImageIcon with picture of "O" for Player 2. */
  private static ImageIcon playerTwo = new ImageIcon("src/media/oImage.png");

  /** ImageIcon to hold currentPlayers ImageIcon either "X" or "O". */
  private static ImageIcon currentPlayer = playerOne;

  /** Array to hold both players number identifiers. */
  private static final int[] playerNums = {1, 2};

  /** Integer to hold the current players number either 1 or 2. */
  private static int currentPlayerNum;

  /** 2D array to hold 9 gameStates for each button. */
  private static int[][] gameStates = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

  /** Layout to hold the Buttons of the board array in the Container. */
  private static GridBagLayout layout = new GridBagLayout();

  /** Constraints to apply to each button in the board array. */
  private static GridBagConstraints gbc = new GridBagConstraints();

  /** 2D array to hold 9 buttons for the game. */
  private static JButton[][] board = new JButton[3][3];

  /** Basic Constructor for the Tic-Tac-Toe Game. */
  public Game() {

    super("Tic-Tac-Toe");
    Container pane = this.getContentPane();
    setLayout(layout);
    currentPlayerNum = playerNums[0];

    for (int y = 0; y < board.length; y++) {

      for (int x = 0; x < board.length; x++) {

        JButton temp = new JButton();
        temp.setBackground(Color.BLUE);
        temp.setPreferredSize(new Dimension(50, 50));
        temp.setFocusPainted(false);
        temp.addActionListener(this);

        gbc.gridy = y;
        gbc.gridx = x;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        pane.add(temp, gbc);
        board[y][x] = temp;
      }
    }

    setVisible(true);
    setSize(500, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /** ChangePlayer method which changes the current player state. */
  public static void changePlayer() {

    switch (currentPlayerNum) {
      case 1:
        currentPlayer = playerTwo;
        currentPlayerNum = playerNums[1];
        break;

      case 2:
        currentPlayer = playerOne;
        currentPlayerNum = playerNums[0];
        break;
    }
  }

  /**
   * Gets the location of the button which was clicked.
   *
   * @param button The button that was clicked on the board.
   * @return the location object of the clicked button.
   */
  public static Dimension getLocation(JButton button) {

    Dimension answer = new Dimension();

    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board.length; x++) {

        if (board[y][x] == button) {

          answer.width = x;
          answer.height = y;
        }
      }
    }
    return answer;
  }

  /**
   * Handles the main actions of the game.
   *
   * @param event The Current button which fired the click event.
   */
  public void actionPerformed(ActionEvent event) {

    JButton temp = (JButton) event.getSource();
    Dimension location = getLocation(temp);

    if (gameStates[location.height][location.width] == -1) {

      playSound();
      temp.setIcon(currentPlayer);
      gameStates[location.height][location.width] = currentPlayerNum;
      showGameStates();
      checkForWinner(this);
    }
  }

  /**
   * Prints out the game states for each button as 1 for playerOne , 2 for playerTwo, and -1 if not
   * clicked yet.
   */
  public static void showGameStates() {

    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board.length; x++) {

        System.out.print(gameStates[y][x] + " ");
      }

      System.out.println("");
    }

    System.out.println("\n\n");
  }

  /**
   * Checks if there is a winner and if it is vertical, horizontal, or diagonal.
   *
   * @param root this Classes inherited JFrame.
   */
  public static void checkForWinner(JFrame root) {

    // check for vertical //
    boolean wonVertical =
        (gameStates[0][0] == currentPlayerNum
                && gameStates[1][0] == currentPlayerNum
                && gameStates[2][0] == currentPlayerNum)
            || (gameStates[0][1] == currentPlayerNum
                && gameStates[1][1] == currentPlayerNum
                && gameStates[2][1] == currentPlayerNum)
            || (gameStates[0][2] == currentPlayerNum
                && gameStates[1][2] == currentPlayerNum
                && gameStates[2][2] == currentPlayerNum);

    // check for horizonal //
    boolean wonHorizonal =
        (gameStates[0][0] == currentPlayerNum
                && gameStates[0][1] == currentPlayerNum
                && gameStates[0][2] == currentPlayerNum)
            || (gameStates[1][0] == currentPlayerNum
                && gameStates[1][1] == currentPlayerNum
                && gameStates[1][2] == currentPlayerNum)
            || (gameStates[2][0] == currentPlayerNum
                && gameStates[2][1] == currentPlayerNum
                && gameStates[2][2] == currentPlayerNum);

    // check for diagonal //
    boolean wonDiagonal =
        (gameStates[0][0] == currentPlayerNum
                && gameStates[1][1] == currentPlayerNum
                && gameStates[2][2] == currentPlayerNum)
            || (gameStates[2][0] == currentPlayerNum
                && gameStates[1][1] == currentPlayerNum
                && gameStates[0][2] == currentPlayerNum);

    if (wonVertical) {

      JOptionPane.showMessageDialog(root, "Player " + currentPlayerNum + " Vertical");
      reset();

    } else if (wonHorizonal) {

      JOptionPane.showMessageDialog(root, "Player " + currentPlayerNum + " Won Horizontal");
      reset();

    } else if (wonDiagonal) {

      JOptionPane.showMessageDialog(root, "Player " + currentPlayerNum + " Won Diagonal");
      reset();

    } else if (staleMate()) {

      JOptionPane.showMessageDialog(root, "No Winner StaleMate");
      reset();

    } else {

      changePlayer();
    }
  }

  /**
   * Resets all of the gameStates for the buttons and sets images to null. Also sets currentPlayer
   * back to playerOne.
   */
  public static void reset() {

    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board.length; x++) {

        board[y][x].setIcon(null);
        gameStates[y][x] = -1;
      }
    }

    currentPlayerNum = 1;
    currentPlayer = playerOne;
  }

  /** Plays a cork sound when a "X" or an "O" is placed on the board. */
  public static void playSound() {

    try {

      InputStream in = new FileInputStream("src/media/cork_pop_x.wav");
      AudioStream as = new AudioStream(in);
      AudioPlayer.player.start(as);

    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  /**
   * This checks if game is stalemate.
   *
   * @return true if all gameStates are not = 1. returns false if 1 or more gamesSates are = -1;
   */
  public static boolean staleMate() {
    boolean answer = true;

    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board.length; x++) {
        if (gameStates[y][x] == -1) {

          answer = false;
          break;
        }
      }
    }

    return answer;
  }
}
