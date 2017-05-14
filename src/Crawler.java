import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Indexes a given directory and its subdirectories used my the index method in
 * the manager class
 * 
 * @author Cezary
 *
 */
public class Crawler implements Runnable {

	String path;
	ArrayList<String> cacheAudio;
	ArrayList<String> cacheImage;
	ArrayList<String> cacheOther;

	classifier classifier = new classifier();

	Crawler(String path, ArrayList<ArrayList<String>> cache) {
		this.path = path;
		this.cacheAudio = cache.get(0);
		this.cacheImage = cache.get(1);
		this.cacheOther = cache.get(2);
	}

	@Override
	public void run() {
		listFiles(path);

	}

	public void listFiles(String path) {
		File[] listOfFiles = new File(path).listFiles();
		for(int i = 0; i<listOfFiles.length;i++){
			System.out.println(listOfFiles[i]);
		}
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i].getAbsolutePath());
			} else {
				// System.out.println(listOfFiles[i].getAbsolutePath());
				listFiles(listOfFiles[i].getAbsolutePath());
			}
		}
		for (int i = 0; i < cacheAudio.size(); i++) {
			System.out.println(cacheAudio.get(i));

		}

	}

	class classifier {
		HashMap<String, AudioAdder> audio = new HashMap<>();
		HashMap<String, ImageAdder> image = new HashMap<>();

		class AudioAdder {
			void addAudio(String path) {

			}
		}

		class ImageAdder {
			void addImage(String path) {

			}

		}

		AudioAdder audioAdder = new AudioAdder();
		ImageAdder imageAdder = new ImageAdder();

		classifier() {
			audio.put("mp3", audioAdder);

			image.put("jpg", imageAdder);
		}

		String fileType = null;

		void classify(String path) {

			
			fileType = getExtention(path);
			//System.out.println(path);
			// check if filetype has allready been seen
			if (audio.get(fileType) != null) {

				cacheAudio.add(path);
				return;
			} else {

				// check if filetype has allready been seen

				if (image.get(fileType) != null) {

					System.out.println(fileType);
					cacheImage.add(path);
					return;
				} else {

					cacheOther.add(path);
					return;
				}
			}
		}
	}

	String getExtention(String inn) {

		String tmp;
		for (int i = inn.length() - 1; i > 1; i--) {
			tmp = inn.substring(i - 1, i);
			if (tmp.equals(".")) {
				System.out.println(inn.substring(i, inn.length()));
				return inn.substring(i, inn.length());
			} else if (tmp.equals("\\")) {
				return null;
			}
		}

		return null;
	}
}
