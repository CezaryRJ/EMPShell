import java.io.File;
import java.util.ArrayList;

public class Crawler implements Runnable {

	String path;
	ArrayList<String> cache;

	Crawler(String path, ArrayList<String> cache) {
		this.path = path;
		this.cache = cache;
	}

	@Override
	public void run() {
	listFiles(path,cache);

	}

	public void listFiles(String path,ArrayList<String> cache) {
		File[] listOfFiles = new File(path).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i].getAbsolutePath(),cache);
			}
			else{
				cache.add(listOfFiles[i].getAbsolutePath());
			}
		}

	}

}
