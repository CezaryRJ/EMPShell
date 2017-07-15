package general;
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
import java.util.List;

import indexing.InvertedIndex;
import indexing.PostingList;


public class DataBase extends runVoid {

	InvertedIndex index = new InvertedIndex();
	public List<FileInfo> files = Collections.synchronizedList(new ArrayList<FileInfo>());



	

	public void readData(String file) throws Exception {

		try {

			// read file database

			BufferedReader scanner = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(file)), "UTF8"));

			int size = Integer.parseInt(scanner.readLine());
			for (int x = 0; x < size; x++) {

				files.add(new FileInfo(scanner.readLine()));
				index.addFile(files.get(files.size()-1).getPath(), x);

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

		index.clear();
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
		index.addFile(files.get(i).getPath(), i);
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
			writer.write(files.get(i).getPath() + "\n");

		}

		writer.close();
	}

	@Override
	public Object run(List<Object> inn) throws Exception {
		index(Manager.path);
		return null;
	}

	
	

	

	
	public PostingList lookup(String inn){
		return index.lookup(inn);
	}
}