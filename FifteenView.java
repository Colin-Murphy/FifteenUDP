import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
/**
 * Class FifteenView provides the user interface for the Fifteen network game.
 *
 * @author  Alan Kaminsky
 * @author  Colin L Murphy
 * @version 16-Mar-2014
 */
public class FifteenView
	extends JFrame
	implements ModelListener
	{

	private static final int GAP = 10;
	private static final int COLS = 12;
	

	/**
	 * Class DigitButton provides a button labeled with a digit.
	 */
	private class DigitButton
		extends JButton
		{
		private int digit;
		private boolean enabled = true;
		private boolean available = true;

		/**
		 * Construct a new digit button.
		 *
		 * @param  digit  Digit for the button label.
		 */
		public DigitButton
			(int digit)
			{
			super ("" + digit);
			this.digit = digit;
			addActionListener (new ActionListener()
				{
				public void actionPerformed (ActionEvent e)
					{
					onDigitButton (DigitButton.this.digit);
					}
				});
			}

		/**
		 * Make this digit button available or unavailable. When available, the
		 * button displays its digit. When not available, the button is blank.
		 *
		 * @param  available  True if available, false if not.
		 */
		public void available
			(boolean available)
			{
			this.available = available;
			setText (available ? "" + digit : " ");
			updateButton();
			}

		/**
		 * Enable or disable this digit button. When enabled and available,
		 * clicking the button performs the appropriate action. Otherwise,
		 * clicking the button has no effect.
		 *
		 * @param  enabled  True if enabled, false if not.
		 */
		public void setEnabled
			(boolean enabled)
			{
			this.enabled = enabled;
			updateButton();
			}

		/**
		 * Update this digit button's label and enabled state.
		 */
		private void updateButton()
			{
			super.setEnabled (available && enabled);
			}
		}

	/**
	 * User interface widgets.
	 */
	private String myName;
	private DigitButton[] digitButton;
	private JTextField myScoreField;
	private JTextField theirScoreField;
	private JTextField winnerField;
	private JButton newGameButton;
	

	//My id
	private int myID;
	
	//Their id
	private int theirID;
	//Their name
	private String theirName;
	
	//My View Listener
	private ViewListener viewListener;

	/**
	 * Construct a new FifteenView object.
	 *
	 * @param  myName  Player's name.
	 */
	public FifteenView
		(String myName)
		{

		super ("Fifteen -- " + myName);
		this.myName = myName;
		JPanel panel = new JPanel();
		add (panel);
		panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		JPanel panel_a = new JPanel();
		panel.add (panel_a);
		panel_a.setLayout (new BoxLayout (panel_a, BoxLayout.Y_AXIS));
		digitButton = new DigitButton [9];
		for (int i = 0; i < 9; ++ i)
			{
			panel_a.add (digitButton[i] = new DigitButton (i + 1));
			digitButton[i].setAlignmentX (0.5f);
			digitButton[i].setEnabled (false);
			digitButton[i].setMinimumSize (digitButton[i].getPreferredSize());
			digitButton[i].setMaximumSize (digitButton[i].getPreferredSize());
			digitButton[i].setSize (digitButton[i].getPreferredSize());
			}
		panel.add (Box.createHorizontalStrut (GAP));
		JPanel panel_b = new JPanel();
		panel.add (panel_b);
		panel_b.setLayout (new BoxLayout (panel_b, BoxLayout.Y_AXIS));
		panel_b.add (myScoreField = new JTextField (COLS));
		myScoreField.setAlignmentX (0.5f);
		myScoreField.setEditable (false);
		myScoreField.setMaximumSize (myScoreField.getPreferredSize());
		panel_b.add (Box.createRigidArea (new Dimension (0, GAP)));
		panel_b.add (theirScoreField = new JTextField (COLS));
		theirScoreField.setAlignmentX (0.5f);
		theirScoreField.setEditable (false);
		theirScoreField.setMaximumSize (theirScoreField.getPreferredSize());
		panel_b.add (Box.createRigidArea (new Dimension (0, GAP)));
		panel_b.add (winnerField = new JTextField (COLS));
		winnerField.setAlignmentX (0.5f);
		winnerField.setEditable (false);
		winnerField.setMaximumSize (winnerField.getPreferredSize());
		panel_b.add (Box.createVerticalGlue());
		panel_b.add (newGameButton = new JButton ("New Game"));
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setMaximumSize (newGameButton.getPreferredSize());
		newGameButton.setEnabled (false);
		newGameButton.addActionListener (new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				onNewGameButton();
				}
			});
		addWindowListener (new WindowAdapter()
			{
			public void windowClosing (WindowEvent e)
				{
				onClose();
				}
			});
		pack();
		setVisible (true);
		}

	/**
	 * Take action when a digit button is clicked.
	 *
	 * @param  digit  Digit that was clicked.
	 */
	private void onDigitButton
		(int digit)
		{
		viewListener.digit(digit);
		}

	/**
	 * Take action when the New Game button is clicked.
	 */
	private void onNewGameButton()
		{
			viewListener.newGame();
		}

	/**
	 * Take action when the Fifteen window is closing.
	 */
	private void onClose()
		{
			viewListener.quit();
			System.exit (0);
		}
		


//Methods requred by the interface
	
	/**
		Inform the client it has joined the game, set their id
		@param id The clients id
	*/
	public void joined(int id) {
		myID = id;

		myScoreField.setText(myName);
		//Set the waiting text, this goes away when told about another player
		theirScoreField.setText("Waiting for partner");
		
	}
	
	/**
		Store the information of the other player
		@param id The players id
		@param name The players name
	*/
	public void playerInfo(int id, String name) {
		if (id !=myID) {
			theirName = name;
			theirID = id;
			//No score yet, just set their name
			theirScoreField.setText(name);
			newGameButton.setEnabled(true);
		}
		
	}
	
	/**
		Set the available digits
		@param digits An array of booleans telling if that digit is available
	*/
	public void availableDigits(Boolean[] digits) {
		for (int i=0; i<digitButton.length; i++) {
			digitButton[i].available(digits[i]);
		}
	}
		
	
	/**
		Set the score of a player
		@param id the id of the player
		@param score the score of the player
	*/
	public void playerScore(int id, int score) {
		
		if (id == myID) {
			myScoreField.setText(myName + " = " + score);
		}
		
		else {
			theirScoreField.setText(theirName + " = " + score);
		}
		
		//If someones score is 0 nobody has won, clear the win field
		//This is done because the players have no way to know when someone hits new game
		if (score == 0) {
			winnerField.setText("");
		}
	}
	
	/**
		Inform the view of whos turn it is
		@param i The id of the players whos turn it is
	*/
	public void playerTurn(int id) {
		boolean myTurn = false;
		if (id == myID) {
			myTurn = true;
		}
		
		//Enable all the buttons if its your turn, otherwise disable them
		for (int i=0;i<digitButton.length; i++) {
			digitButton[i].setEnabled(myTurn);
		}
	}
	
	/**
		Display the winner of the game (or say its a draw)
		@param winner the id of the winner (0 for draw)
	*/
	public void winner(int winner) {
	
		if (winner == myID) {
			winnerField.setText(myName + " wins!");
		}
		
		else if (winner == theirID) {
			winnerField.setText(theirName + " wins!");
		}
		
		else {
			winnerField.setText("Draw!");
		}
	
	}
	
	/**
		Quit the game
	*/
	public void quit() {
	
		System.exit(0);

	
	}
	
	/**
		Set the view listener
		@param viewListener the view listener
	*/
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
		

		viewListener.joined(myName);
	
	}
			
	
	
		
		

}
