package general;

import java.util.List;

import search.ResultSet;

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

	public Object run(List<Object> inn) throws Exception {
		System.out.println("hei");
	
			((ResultSet) inn.get(0)).print();
			
		System.out.println("hei");
		

		return null;
	}

	@Override
	public void help() {
		System.out.println(
				"mix (number) - This method will generate a playlist consisting of X requested sound files in the database");

	}

}
