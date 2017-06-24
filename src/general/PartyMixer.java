package general;

import java.util.ArrayList;

/**
 * Makes a playlist consisting of the requested amount of tracks from the
 * 
 * WARNING, IT IS HIGLY RECCOMENDED TO USE VLC TO LAUNCH THE PLAYLIST, AS WIDNWO
 * MEDIA PLAYERS DOSNT ALWAYS WORK if you find a solution to this problem, feel
 * free to contact me on github
 * 
 * @author Cezary
 *
 */

public class PartyMixer extends runVoid {

	Manager manager;

	PartyMixer(Manager manager) {
		this.manager = manager;
	}

	public void run(ArrayList<String> inn) throws Exception {

	
	}

	@Override
	public void help() {
		System.out.println(
				"mix (number) - This method will generate a playlist consisting of X requested sound files in the database");

	}

}
