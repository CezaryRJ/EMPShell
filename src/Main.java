import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		final int coreCount = Runtime.getRuntime().availableProcessors();

		try {
			Scanner inn = new Scanner(new File("settings.txt"));

		} catch (FileNotFoundException e) {

			PrintWriter writer = new PrintWriter("settings.txt", "UTF-8");
			File[] listOfFiles = new File(System.getProperty("user.dir")).listFiles();

			ArrayList<ArrayList<String>> cache = new ArrayList<>();
			cache.add(new ArrayList<String>());

			Thread[] crawler = new Thread[coreCount];

			File tmp;

			for (int i = 0; i < listOfFiles.length; i++) {
				tmp = new File(listOfFiles[i].getPath());
				if (tmp.isDirectory()) {
					for (int x = 0; x < crawler.length; x++) {
						if (crawler[x] == null || !crawler[x].isAlive()) {
							cache.add(new ArrayList<String>());
							crawler[x] = new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1)));
							crawler[x].start();
							x = crawler.length;
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
			for (int i = 0; i < cache.size(); i++) {
				ArrayList<String> tmpArr = cache.get(i);
				for (int x = 0; x < cache.get(i).size(); x++) {
					writer.println(tmpArr.get(x));

				}

			}
			writer.close();
		}

	}

}
