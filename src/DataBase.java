import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import indexing.InvertedIndex;
import indexing.Lexicon;
import indexing.Posting;
import indexing.PostingList;

public class DataBase implements runVoid {

	Lexicon lexicon = new Lexicon();
	ArrayList<PostingList> PostingLists = new ArrayList<>();
	ArrayList<FileInfo> files = new ArrayList<>();
	//database database woah.
	//stuff things
	public void findFile(String name) {
		PostingList tmp = index.getInvertedIndex().get(index.getLexicon().lookup(name));
		for (int i = 0; i < tmp.getPostings().size(); i++) {
			System.out.println(tmp.getPostings().get(i).getPath());

		}
	}

	public void readData(String file) throws Exception {

		try {

			for (int i = 0; i < classes; i++) {
				// create more classes if needed
				if (dataBase.get(i) == null) {
					dataBase.put(i, new HashMap<String, FileInfo>());
				}

			}

			// read file database
			// Scanner scanner = new Scanner(new File(file),"UTF-8");

			BufferedReader scanner = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(file)), "UTF8"));

			classes = Integer.parseInt(scanner.readLine());
			for (int x = 0; x < classes; x++) {
				int classMembers = Integer.parseInt(scanner.readLine());
				for (int i = 0; i < classMembers; i++) {
					dataBase.get(x).put(scanner.readLine(), new FileInfo());
				}

			}

			System.out.println("Files in databse : " + scanner.read());
			scanner.close();

			System.out.println("Database file has been sucesfully read");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot, or if db file is not found
			System.out.println("No databse file found, performing first time scan");
			index(Manager.path);
		}

	}

	public void index(String path) throws Exception {

		Timer timer = new Timer();
		timer.start();

		File[] listOfFiles = new File(path).listFiles();

		ArrayList<FileInfo> cache = new ArrayList<>();
		
		Crawler thisFolder = new Crawler(path, cache);

		ArrayList<Thread> crawler = new ArrayList<>();
		File tmp;
		int threads = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
			tmp = new File(listOfFiles[i].getPath());

			if (tmp.isDirectory()) {

				cache.add(new ArrayList<ArrayList<String>>());
				for (int y = 0; y < classes; y++) {
					cache.get(cache.size() - 1).add(new ArrayList<String>());
				}
				crawler.add(new Thread(new Crawler(tmp.getAbsolutePath(), cache.get(cache.size() - 1))));

				threads++;

			} else { // denne sjekke filene i root mappen
				thisFolder.classifier.classify(tmp.getAbsolutePath());
			}

		}
		int maxThreads = 0;
		if (Runtime.getRuntime().availableProcessors() * 2 < crawler.size()) {
			maxThreads = Runtime.getRuntime().availableProcessors();
		} else {
			maxThreads = crawler.size();
		}

		int threadIndex = 0;
		for (int i = 0; i < maxThreads; i++) {
			// start first threads
			crawler.get(threadIndex).start();
			threadIndex++;
		}

		for (int i = 0; i < crawler.size(); i++) {
			// start first threads and wait so start new
			crawler.get(i).join();
			try {
				crawler.get(threadIndex).start();
				threadIndex++;
			} catch (Exception e) {

			}
		}

		for (int i = 0; i < classes; i++) {
			// create more classes if needed
			if (dataBase.get(i) == null) {
				dataBase.put(i, new HashMap<String, FileInfo>());
			}

		}

		// classes will be names 0,1,2,3 etc. 0 - music, 1- images and so on
		for (int b = 0; b < cache.size(); b++) {
			for (int c = 0; c < cache.get(b).size(); c++) {
				for (int d = 0; d < cache.get(b).get(c).size(); d++) {

					dataBase.get(c).put(cache.get(b).get(c).get(d), new FileInfo());

				}
			}

		}

		int fileCounter = 0;
		for (int i = 0; i < classes; i++) {

			System.out.println("Class " + labels.get(i) + " contains " + dataBase.get(i).size() + " files");
			fileCounter += dataBase.get(i).size();
		}
		System.out.print(fileCounter + " files gathered in ");
		timer.stop();
		System.out.println("\n" + threads + " threads have been used for this task");

	}

	public void writeToFile() throws IOException {
		System.out.println("Writing database to file");
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("empdb.txt"),
				Charset.forName("UTF-8").newEncoder());
		int counter = 0;
		writer.write(classes + "\n");
		for (int i = 0; i < dataBase.size(); i++) {
			writer.write(dataBase.get(i).size() + "\n");
			counter += dataBase.get(i).size();
			for (String entry : dataBase.get(i).keySet()) {
				writer.write(entry + "\n");
			}
		}
		writer.write(counter);
		writer.close();
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

	public HashMap<String, FileInfo> getGroup(int i) {

		return dataBase.get(i);
	}

	@Override
	public void run(ArrayList<String> inn) throws Exception {
		index(Manager.path);

	}

	public HashMap<String, FileInfo> getClass(String name) {
		return dataBase.get(labels.get(name));
	}

	@Override
	public void help() {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> getTags() {

		return null;
	}

	public ArrayList<String> readMeta() {

		return null;
	}

}
