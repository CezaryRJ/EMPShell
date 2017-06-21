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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import indexing.InvertedIndex;
import indexing.Lexicon;
import indexing.Posting;
import indexing.PostingList;

public class DataBase implements runVoid {

	Lexicon lexicon = new Lexicon();
	
	ArrayList<PostingList> postingLists = new ArrayList<>();

	List<FileInfo> files = Collections.synchronizedList(new ArrayList<FileInfo>());



	public void findFile(String name) {
		PostingList tmp = postingLists.get(lexicon.lookup(name));
		for (int i = 0; i < tmp.getPostings().size(); i++) {
			System.out.println(tmp.getPostings().get(i).getDocID());

		}
	}

	public void readData(String file) throws Exception {

		try {

			// read file database
			// Scanner scanner = new Scanner(new File(file),"UTF-8");

			BufferedReader scanner = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(file)), "UTF8"));

			int size = Integer.parseInt(scanner.readLine());
			for (int x = 0; x < size; x++) {

				files.add(new FileInfo(scanner.readLine()));
				// System.out.println(files.get(files.size()-1).path);

			}

			System.out.println("Files in databse : " + size);
			scanner.close();

			System.out.println("Database file has been sucesfully read");

		} catch (FileNotFoundException e) {
			// scan all subfolders on first boot, or if db file is not found
			System.out.println("No databse file found, performing first time scan");
			index(Manager.path);
		}

	}

	public void index(String path) throws Exception {

		lexicon = new Lexicon();
		postingLists = new ArrayList<PostingList>();
		files = Collections.synchronizedList(new ArrayList<FileInfo>());

		Timer timer = new Timer();
		timer.start();

		File[] listOfFiles = new File(path).listFiles();

		ArrayList<Thread> crawler = new ArrayList<>();
		File tmp;
		int threads = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
			tmp = new File(listOfFiles[i].getPath());

			if (tmp.isDirectory()) {

				crawler.add(new Thread(new Crawler(tmp.getAbsolutePath(), files)));

				threads++;

			} else {

				files.add(new FileInfo(tmp.getAbsolutePath()));
			}

		}

		int maxThreads;
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

		for (int i = 0; i < files.size(); i++) {
			addFile(files.get(i).path, i);
		}

		
		timer.stop();
		System.out.println("\n" + files.size() + " files found\n" + threads + " threads have been used for this task");

	}

	public void writeToFile() throws IOException {
		System.out.println("Writing database to file");
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("empdb.txt"),
				Charset.forName("UTF-8").newEncoder());

		writer.write(files.size() + "\n");

		for (int i = 0; i < files.size(); i++) {
			writer.write(files.get(i).path + "\n");

		}

		writer.close();
	}

	@Override
	public void run(ArrayList<String> inn) throws Exception {
		index(Manager.path);

	}

	@Override
	public void help() {
		// TODO Auto-generated method stub

	}

	public void addFile(String path, int id) {

		String[] tags = getTags(path);
		for (int i = 0; i < tags.length; i++) {
		
			if (lexicon.lookup(tags[i]) == -1) {
				
				lexicon.addValue(tags[i]);
				postingLists.add(new PostingList());
				postingLists.get(lexicon.lookup(tags[i])).addPosting(new Posting(id));
			} else {
				postingLists.get(lexicon.lookup(tags[i])).addPosting(new Posting(id));
			}

		}
	}

	public String[] getTags(String inn) {

		return inn.split("\\\\");
	}

	public ArrayList<String> readMeta() {

		return null;
	}

}