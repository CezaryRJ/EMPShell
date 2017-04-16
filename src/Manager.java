import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Manager {

	public ArrayList<String> tokenize(String inn, String limiter) {
	
		ArrayList<String> out = new ArrayList<>();
		int counter = 0;
	
		for (int i = 0; i < inn.length()-1; i++) {

			if (inn.substring(i, (i + 1)).equals(limiter)) {

				out.add(inn.substring(counter, i));
				counter = (i+1);
			}
		}
		out.add(inn.substring(counter , inn.length()));
		
		
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
		return  oldPath + "\\" + fileName;

	}
	public String exitFolder(String path){
		
	ArrayList<String> tmp = tokenize(path,"\\");
		
	String out = tmp.get(0);
		for(int i = 1; i<tmp.size()-1;i++){
			out = out +"\\" + tmp.get(i);
		}
		System.out.println(out);
		return out;
	}
	
}
