package general;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import search.ResultSet;
import java.util.Random;

/**
 * Makes a playlist consisting of the requested amount of tracks from the
 * 
 * WARNING, IT IS HIGLY RECCOMENDED TO USE VLC TO LAUNCH THE PLAYLIST, AS
 * WINDWOS MEDIA PLAYERS DOSNT ALWAYS WORK if you find a solution to this
 * problem, feel free to contact me on github
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

		if (inn.size() < 1) {
			System.out.println("Empty result set");
			return null;
		}

		if (inn.get(0) instanceof ResultSet) {
			ResultSet tmp = ((ResultSet) inn.get(0));
			int mengde = 0;
			
			
			if (inn.size() > 1) {
				if (inn.get(1) instanceof String) {
					mengde = Integer.parseInt((String) inn.get(1));
				}
			}else {
				System.out.println("Specify the amount of random elements to select ");
				return null;
			}
			

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(Manager.openDir + "\\EMPplaylist.wpl"), Charset.forName("UTF-8").newEncoder());
			writer.write("<?wpl version=" + "1.0" + "?> \n<smil>" + "\n <body>" + "\n  <seq>");

			ArrayList<Integer> keys = new ArrayList<>(tmp.results.keySet());
			Random random = new Random();
			int randomKey;
			for (int i = 0; i < mengde; i++) {
				randomKey = keys.get(random.nextInt(keys.size()-1));

				writer.write(
						"\n    <media src=\"" + manager.database.files.get(randomKey).getPath() + "\"/>");

			}
			writer.write("\n   </seq>" + "\n  </body>" + "\n </smil>");
			writer.close();
			Desktop.getDesktop().open(new File("EMPplaylist.wpl"));
			System.out.println(mengde);
			return null;
		}

		System.out.println("Wrong input argument type, expected " + ResultSet.class.getName() + " got "
				+ inn.get(0).getClass().getName());
		return null;
	}

	@Override
	public void help() {
		System.out.println(
				"mix (number) - This method will generate a playlist consisting of X requested sound files in the database");

	}

}
