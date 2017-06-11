import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Indexes a given directory and its subdirectories used my the index method in
 * the manager class
 * 
 * @author Cezary
 *
 */
public class Crawler implements Runnable {

	String path;
	ArrayList<FileInfo> Files;

	Crawler(String path, ArrayList<FileInfo> cache) {
		this.path = path;
	}

	@Override
	public void run() {
		listFiles(path);

	}

	public void listFiles(String path) {
		File[] listOfFiles = new File(path).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i].getAbsolutePath());
			} else {

				Files.add(new FileInfo(listOfFiles[i].getAbsolutePath()));
			}
		}

	}

	String getExtention(String inn) {

		String tmp;
		for (int i = inn.length() - 1; i > 1; i--) {
			tmp = inn.substring(i - 1, i);
			if (tmp.equals(".")) {
				return inn.substring(i, inn.length());
			} else if (tmp.equals("\\")) {
				return null;
			}
		}

		return null;
	}
}
