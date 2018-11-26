package walkingland;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame {

	// Setup variables
	public static final String gamename = "Walking Land!";
	public static final int menu = 0;
	public static final int play = 1;
	
	// Constructor
	public Game(String gamename) {
		super(gamename);			// Call constructor of StateBasedGame and sets title of display box
		this.addState(new Menu(menu));
		this.addState(new Play(play));}
	
	// Required from StateBasedGame
	// Gives a list of states so the game knows what screens will exist
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.enterState(menu);}
	
	// Main
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new Game(gamename));
			app.setDisplayMode(640, 360, false);
			app.start();} 
		catch (SlickException e) {
			e.printStackTrace();}
	}

	
}
