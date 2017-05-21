import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

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

public class PartyMixer implements runVoid {

	Manager manager;

	PartyMixer(Manager manager) {
		this.manager = manager;
	}

	public void run(ArrayList<String> inn) throws Exception {

		ArrayList<String> tokens;
		int filesWanted;
		try {
			filesWanted = Integer.parseInt(inn.get(0));
		} catch (Exception e) {
			System.out.println("Input the number of wanted audio files");
			return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(Manager.openDir + "\\EMPplaylist.wpl"),
				Charset.forName("UTF-8").newEncoder());
		Random random = new Random();
		ArrayList<String> keys = new ArrayList<String>(manager.database.getGroup(0).keySet());
		String randomKey;

		writer.write("<?wpl version=\"1.0\"?>\n<smil>\n" + "    <body>\n" + "        <seq>\n");
		for (int i = 0; i < filesWanted; i++) {
			randomKey = keys.get(random.nextInt(keys.size()));
			tokens = Manager.tokenize(randomKey, ".");
			if (tokens.get(tokens.size() - 1).equals("mp3")) {
				writer.write("            <media src=\"" + randomKey + "\"/>\n");

			}

		}
		writer.write("        </seq>\n    </body>\n</smil>");
		writer.close();
		inn.clear();
		inn.add("EMPplaylist.wpl");
		Desktop.getDesktop().open(new File(Manager.openDir + "\\EMPplaylist.wpl"));

	}

	@Override
	public void help() {
		System.out.println(
				"mix (number) - This method will generate a playlist consisting of X requested sound files in the database");

	}

}
