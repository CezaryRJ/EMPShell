import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

	String path;

	HashMap<String, runVoid> runVoid = new HashMap<>();

	Manager(String path) {
		this.path = path;

		runVoid.put("ls", new ls());
		runVoid.put("credits", new credits());
		runVoid.put("opendir", new openFolder());
		runVoid.put("del", new delete());
		runVoid.put("index", new indexer());
		runVoid.put("help", new help());
		runVoid.put("exit", new exit());

		runVoid.put("goto", new goToc());
		runVoid.put("cd", new enterFolder());

	}

	public ArrayList<String> tokenize(String inn, String limiter) {

		ArrayList<String> out = new ArrayList<>();
		int counter = 0;

		for (int i = 0; i < inn.length() - 1; i++) {

			if (inn.substring(i, (i + 1)).equals(limiter)) {

				out.add(inn.substring(counter, i));
				counter = (i + 1);
			}
		}
		out.add(inn.substring(counter, inn.length()));

		return out;
	}

	class ls implements runVoid {

		public void run(ArrayList<String> inn) {
			File[] listOfFiles = new File(path).listFiles();
			ArrayList<String> files = new ArrayList<>();
			ArrayList<String> folders = new ArrayList<>();
			Path tmp;
			for (int i = 0; i < listOfFiles.length; i++) {
				tmp = Paths.get(listOfFiles[i].getAbsolutePath());
				if (listOfFiles[i].isDirectory()) {
					folders.add(tmp.getFileName().toString());
				} else {
					files.add(tmp.getFileName().toString());
				}

			}
			for (int i = 0; i < folders.size(); i++) {
				System.out.println("\\" + folders.get(i));

			}
			for (int i = 0; i < files.size(); i++) {
				System.out.println(files.get(i));

			}

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}
	}

	class credits implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {
			System.out.println("Created by Cezary Radoslaw Jaksula\nhttps://github.com/CezaryRJ");

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class openFolder implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class delete implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {

			File tmp;
			for (int i = 2; i < inn.size(); i++) {

				tmp = new File(inn.get(1) + "\\" + inn.get(i));
				if (tmp.delete()) {
					System.out.println("File " + tmp.getName() + " has been deleted");
				} else {
					System.out.println("File " + tmp.getName() + " could not be deleted");
				}
			}
		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class indexer implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {

			Timer timer = new Timer();
			timer.start();
			PrintWriter writer = new PrintWriter(path + ".txt", "UTF-8");

			File[] listOfFiles = new File(path).listFiles();

			ArrayList<ArrayList<String>> cache = new ArrayList<>();
			cache.add(new ArrayList<String>());

			Thread[] crawler = new Thread[Runtime.getRuntime().availableProcessors()];

			File tmp;
			int threads = 0;

			for (int i = 0; i < listOfFiles.length; i++) {
				tmp = new File(listOfFiles[i].getPath());

				if (tmp.isDirectory()) {
					for (int x = 0; x < crawler.length; x++) {
						if (crawler[x] == null || !crawler[x].isAlive()) {
							cache.add(new ArrayList<String>());
							crawler[x] = new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1)));
							crawler[x].start();
							threads++;
							break;
						}
					}

				} else {
					cache.get(0).add(tmp.getAbsolutePath());
				}

			}
			for (int i = 0; i < crawler.length; i++) {
				if (crawler[i] != null) {

					crawler[i].join();

				}

			}
			int counter = 0;
			for (int i = 0; i < cache.size(); i++) {
				ArrayList<String> tmpArr = cache.get(i);
				for (int x = 0; x < cache.get(i).size(); x++) {
					writer.println(tmpArr.get(x));
					counter++;

				}

			}
			writer.close();
			System.out.print(counter + " files gathered in ");
			timer.stop();
			System.out.println("\n" + threads + " threads have been used for this task");

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class goToc implements runVoid {

		public void run(ArrayList<String> inn) {

			try {
				inn = new ArrayList<>();
				inn.add((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
				runVoid.get("cd").run(inn);

			} catch (Exception e) {
				System.out.println("No such directory");
			}

		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class help implements runVoid {

		
		public void run(ArrayList<String> inn) throws Exception {

			runVoid.get(inn.get(0)).help();
		}

		
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class open implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {
			for (int i = 2; i < inn.size(); i++) {

				try {
					if (new File(inn.get(0) + "\\" + inn.get(i)).exists()) {
						Desktop.getDesktop().open(new File(inn.get(0) + "\\" + inn.get(i)));
					} else {
						System.out.println(
								"Failed to open file " + inn.get(0) + "\\" + inn.get(i) + " file does not exist");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}

	}

	class exit implements runVoid {

		@Override
		public void run(ArrayList<String> inn) throws Exception {
			System.exit(0);

		}

		@Override
		public void help() {
			System.out.println("Closes the shell");

		}

	}

	class enterFolder implements runVoid {

		@Override
		public void run(ArrayList<String> inn) {

			if (inn.size() > 0) {

				String tmp = path + "\\" + inn.get(0);
				File tmpf = new File(tmp);
				System.out.println(tmp);
				if (tmpf.exists() && tmpf.isDirectory()) {

					path = tmp;
				} else {
					System.out.println("Invalid path name");

				}
			} else {
				path = exitFolder(path);
			}
		}

		public String exitFolder(String path) {

			ArrayList<String> tmp = tokenize(path, "\\");

			String out = tmp.get(0);
			for (int i = 1; i < tmp.size() - 1; i++) {
				out = out + "\\" + tmp.get(i);
			}
			System.out.println(out);
			return out;
		}

		public String getPath() {
			return path;
		}

		@Override
		public void help() {
			// TODO Auto-generated method stub

		}
	}

}
