package general;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import search.ResultSet;

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

		if (inn.get(0) instanceof ResultSet) {
			ResultSet tmp = ((ResultSet) inn.get(0));
			

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(Manager.openDir + "\\EMPplaylist.wpl"), Charset.forName("UTF-8").newEncoder());
			writer.write("<?wpl version=" + "1.0" + "?> \n<smil>" + "\n <body>" + "\n  <seq>");

			System.out.println("Mixer playing the follwing : ");
			for (Integer docId : tmp.results.keySet()) {
				writer.write("\n    <media src=\"" + manager.database.files.get(docId).getPath() + "\"/>");
				

				/*
				ftmp = new File(manager.database.files.get(docId).getPath());
				System.out.println(ftmp.getName());
				Desktop.getDesktop().open(ftmp);
				*/

			}
			writer.write("\n   </seq>" + "\n  </body>" + "\n </smil>");
			writer.close();
			Desktop.getDesktop().open(new File("EMPplaylist.wpl"));
			
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
