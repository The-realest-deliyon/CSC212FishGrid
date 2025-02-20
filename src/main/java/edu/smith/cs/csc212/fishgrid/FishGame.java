package edu.smith.cs.csc212.fishgrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class manages our model of gameplay: missing and found fish, etc.
 * @author jfoley
 *
 */
public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends.
	 */
	Fish player;
	/**
	 * The home location.
	 */
	FishHome home;
	/**
	 * These are the missing fish!
	 */
	List<Fish> missing;
	
	/**
	 * These are fish we've found!
	 */
	List<Fish> found;
	
	List<Fish> home2;
	
	
	/**
	 * Number of steps!
	 */
	int stepsTaken;
	
	/**
	 * Score!
	 */
	int score;
	
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	public FishGame(int w, int h) {
		world = new World(w, h);
		
		missing = new ArrayList<Fish>();
		found = new ArrayList<Fish>();
		
		// Add a home!
		home = world.insertFishHome();
		
		final int num_rock = 5;
		for (int i=0; i<num_rock; i++) {
			world.insertRockRandomly();
		}
		
		world.insertSnailRandomly();
		world.insertSnailRandomly();
		
		
		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at "home".
		player.setPosition(home.getX(), home.getY());
		player.markAsPlayer();
		world.register(player);
		
		// Generate fish of all the colors but the first into the "missing" List.
		for (int ft = 1; ft < Fish.COLORS.length; ft++) {
			Fish friend = world.insertFishRandomly(ft);
			missing.add(friend);
		}
	}
	
	
	/**
	 * How we tell if the game is over: if missingFishLeft() == 0.
	 * @return the size of the missing list.
	 */
	public int missingFishLeft() {
		return missing.size();
	}
	
	/**
	 * This method is how the Main app tells whether we're done.
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOver() {
		// TODO(FishGrid) We want to bring the fish home before we win!
		return missing.isEmpty();
	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 */
	public void step() {
		// Keep track of how long the game has run.
		this.stepsTaken += 1;
		
		wanderMissingFish();
				
		// These are all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);
		
		// If we find a fish, remove it from missing.
		for (WorldObject thing : overlap) {
			// It is missing if it's in our missing list.
			if (missing.contains(thing)) {
				// Remove this fish from the missing list.
				missing.remove(thing);
				
				found.add((Fish) thing);
				
				// Increase score when you find a fish!
				score += 10;
			}
			
			if (((Fish)thing).color == 0) {
				score += 10;
			}
			if (((Fish)thing).color == 1) {
				score += 20;
			}
			if (((Fish)thing).color == 2) {
				score += 15;
			}
			if (((Fish)thing).color == 3) {
				score += 10;
			}
			if (((Fish)thing).color == 4) {
				score += 30;
			}
			if (((Fish)thing).color == 5) {
				score += 40;
			}
			if (((Fish)thing).color == 6) {
				score += 10;
			}
			if (((Fish)thing).color == 7) {
				score += 15;
			}
			if (((Fish)thing).color == 8) {
				score += 10;
			}
			if (((Fish)thing).color == 9) {
				score += 20;
			}
		}
		
		if (home2 instanceof FishHome) {
			for (Fish fish: found) {
				home2.add(fish);
				world.remove(fish);
			}
			found.clear();
		}
		
		// Make sure missing fish *do* something.
		wanderMissingFish();
		// When fish get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();
	}
	
	/**
	 * Call moveRandomly() on all of the missing fish to make them seem alive.
	 */
	private void wanderMissingFish() {
		Random rand = ThreadLocalRandom.current();
		for (Fish lost : missing) {
			// 30% of the time, lost fish move randomly.
			if (rand.nextDouble() < 0.3) {
				lost.moveRandomly();
			}
		}
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the game.
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {
		System.out.println("Clicked on: "+x+","+y+ " world.canSwim(player,...)="+world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);
		for(WorldObject wo: atPoint) {
			if(wo.Rock()) {
				wo.remove();
			}
		}
		// TODO(FishGrid) allow the user to click and remove rocks.

	}
	
}
