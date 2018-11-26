package walkingland;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {

	Animation player, movingUp, movingDown, movingLeft, movingRight;
	Image worldMap;
	boolean quit = false;
	int[] duration = {200, 200};
	float playerPosX = 0;
	float playerPosY = 0;
	
	// Trick to keep player in middle of screen, moves map instead.
	float shiftX = playerPosX + 320;		
	float shiftY = playerPosY + 160;
	
	public Play(int state) {}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		worldMap = new Image("res/world.png");
		
		Image[] walkUp 		= {new Image("res/playersBack.png"), new Image("res/playersBack.png")};
		Image[] walkDown 	= {new Image("res/playersFront.png"), new Image("res/playersFront.png")};
		Image[] walkLeft 	= {new Image("res/playersLeft.png"), new Image("res/playersLeft.png")};
		Image[] walkRight 	= {new Image("res/playersRight.png"), new Image("res/playersRight.png")};
		
		movingUp 			= new Animation(walkUp, duration, false);
		movingDown 			= new Animation(walkDown, duration, false);
		movingLeft 			= new Animation(walkLeft, duration, false);
		movingRight	 		= new Animation(walkRight, duration, false);
		
		player = movingDown;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		worldMap.draw(playerPosX, playerPosY);
		player.draw(shiftX, shiftY);
		g.drawString("Player's X: " + playerPosX + "\nPlayer's Y: " + playerPosY, 400, 20);

		// Pulls up a quit menu when Escape is pressed
		if(quit == true) {
			g.drawString("Resume (R)", 250, 100);
			g.drawString("Main Menu (M)", 250, 150);
			g.drawString("Quit (Q)", 250, 200);
			if(quit == false) {g.clear();}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		// Up
		if(input.isKeyDown(Input.KEY_UP)) {
			player = movingUp;
			playerPosY += delta * .1f;
			if(playerPosY > 162) {playerPosY -= delta*.1f;}
		}

		// Down
		if(input.isKeyDown(Input.KEY_DOWN)) {
			player = movingDown;
			playerPosY -= delta * .1f;
			if(playerPosY < -600) {playerPosY += delta*.1f;}
		}

		// Left
		if(input.isKeyDown(Input.KEY_LEFT)) {
			player = movingLeft;
			playerPosX += delta * .1f;
			if(playerPosX > 324) {playerPosX -= delta*.1f;}
		}

		// Right
		if(input.isKeyDown(Input.KEY_RIGHT)) {
			player = movingRight;
			playerPosX -= delta * .1f;
			if(playerPosX < -840) {playerPosX += delta*.1f;}
		}
		
		// Escape Key Menu
		if(input.isKeyDown(Input.KEY_ESCAPE)) {
			quit = true;
		}

		if(quit == true) {
			if(input.isKeyDown(Input.KEY_R)) {quit = false;}
			if(input.isKeyDown(Input.KEY_M)) {sbg.enterState(0);}
			if(input.isKeyDown(Input.KEY_Q)) {System.exit(0);}
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}
