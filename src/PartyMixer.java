import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

/**
 * Makes a playlist consisting of the requested amount of tracks from the
 * databse For some reason it does not work with not ascii filenames
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

		// inn 0 = number of tracs

	//	File playlist = new File("EMPplaylist.wpl");

		ArrayList<String> tokens;

		int filesWanted = Integer.parseInt(inn.get(0));

		//PrintWriter writer = new PrintWriter(playlist);
		 OutputStreamWriter writer = new OutputStreamWriter(
			     new FileOutputStream("EMPplaylist.wpl"),
			     Charset.forName("UTF-8").newEncoder() 
			 );
		Random random = new Random();
		ArrayList<String> keys = new ArrayList<String>(manager.database.getGroup(0).keySet());
		String randomKey;

		writer.write("<?wpl version=\"1.0\"?>\n<smil>\n" + "    <head>\n"
				+ "        <meta name=\"Generator\" content=\"Microsoft Windows Media Player -- 12.0.9600.17031\"/>\n"
				+ "        <meta name=\"ItemCount\" content=\"1\"/>\n" + "        <title>test</title>\n" + "    </head>\n"
				+ "    <body>\n" +"        <seq>\n");
		for (int i = 0; i < filesWanted; i++) {
			randomKey = keys.get(random.nextInt(keys.size()));
			tokens = Manager.tokenize(randomKey, ".");
			if (tokens.get(tokens.size() - 1).equals("mp3")) {
			writer.write("            <media src=\"" + randomKey +"\"/>\n");
			
				// writer.println(randomKey + "\n");

			}

		}
		writer.write("        </seq>\n    </body>\n</smil>");
		writer.close();
		inn.clear();
		inn.add("EMPplaylist.wpl");
		manager.runVoid.get("start").run(inn);

	}

	@Override
	public void help() {
		System.out.println(
				"mix (number) - This method will generate a playlist consisting of X requested sound files in the database");

	}

}
