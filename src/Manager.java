import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Manager {
	
	public void credits(){
		System.out.println("Created by Cezary Radoslaw Jaksula\nhttps://github.com/CezaryRJ");
		
	}
	
	public void openFolder(String currentPath) throws IOException{
		Desktop.getDesktop().open(new File(currentPath));
		
	}
	
	public void delete(String path,ArrayList<String> args){
		
		File tmp;
		for(int i = 1;i<args.size();i++){
			
			tmp = new File(path + "\\" + args.get(i));
			if(tmp.delete()){
				System.out.println("File " + tmp.getName() + " has been deleted");
			}
			else {
				System.out.println("File " + tmp.getName() + " could not be deleted");
			}
		}
	}

	public void indexer(String currentPath) throws Exception {

		Timer timer = new Timer();
		timer.start();
		PrintWriter writer = new PrintWriter(currentPath + ".txt", "UTF-8");
		File[] listOfFiles = new File(currentPath).listFiles();

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

	public String goToc() {

		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			System.out.println("No such directory");
		}
		return null;
	}

	public void help() {
		System.out.println("cd -foldername  |enter folder \ncd  " + "| exit folder\nls "
				+ "|lists all files and subfolder in current folder\nexe -argument "
				+ "|opens or executes the requested argument");
	}

	public void open(String path, ArrayList<String> args) {

		for (int i = 1; i < args.size(); i++) {

			try {
				if (new File(path + "\\" + args.get(i)).exists()) {
					Desktop.getDesktop().open(new File(path + "\\" + args.get(i)));
				} else {
					System.out.println("Failed to open file " + path + "\\" + args.get(i) + " file does not exist");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public void listFiles(String path) {
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

	public String enterFolder(String oldPath, String fileName) {

		System.out.println(oldPath + "\\" + fileName);
		if (new File(oldPath + "\\" + fileName).exists()) {
			return oldPath + "\\" + fileName;
		} else {
			System.out.println("Invalid path name");
			return oldPath;
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

}
