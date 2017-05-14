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
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i].getAbsolutePath());
			} else {
				// System.out.println(listOfFiles[i].getAbsolutePath());
				classifier.classify(listOfFiles[i].getAbsolutePath());
			}
		}
		for (int i = 0; i < cacheAudio.size(); i++) {
			System.out.println(cacheAudio.get(i));

		}

	}

	class classifier {
		HashMap<String, AudioAdder> audio = new HashMap<>();
		HashMap<String, ImageAdder> image = new HashMap<>();
		HashMap<String, OtherAdder> other = new HashMap<>();

		class AudioAdder {
			void addAudio(String path) {

			}
		}

		class ImageAdder {
			void addImage(String path) {

			}

		}

		class OtherAdder {
			void addOther(String path) {

			}
		}

		AudioAdder audioAdder = new AudioAdder();
		ImageAdder imageAdder = new ImageAdder();
		OtherAdder otherAdder = new OtherAdder();

		String fileType = null;

		void classify(String path) {

			// check if filetype has allready been seen
			if (audio.get(getExtention(path)) != null) {

				cacheAudio.add(path);
				return;
			} else {

				// check if filetype has allready been seen

				if (image.get(getExtention(path)) != null) {

					System.out.println(getExtention(path));
					cacheImage.add(path);
					return;
				} else {

					if (other.get(getExtention(path)) != null) {

						cacheOther.add(path);
						return;
					} else {
						// if we get here it means we have found a new filetype
					
						try {
						
							AudioFileFormat format = AudioSystem.getAudioFileFormat(new File("2.mp3"));
							System.out.println("hei");
							fileType = getExtention(path);
							audio.put(fileType, audioAdder);
							audio.get(fileType).addAudio(path);

							return;

						} catch (UnsupportedAudioFileException a) {
							// now we know its not an audio file
							
						
								try {
									if (ImageIO.read(new File(path)) == null) {
										// is not image
										
										fileType = getExtention(path);
										other.put(fileType, otherAdder);
										other.get(fileType).addOther(path);
										return;
									} else {
										//is image
										
										fileType = getExtention(path);
										image.put(fileType, imageAdder);
										image.get(fileType).addImage(path);
										return;
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
						} catch (Exception b) {

						}

					}
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

}
