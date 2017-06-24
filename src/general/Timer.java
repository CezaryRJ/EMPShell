package general;

/**
 * Its a timer , what did you expect ?
 * 
 * @author Cezary
 *
 */
public class Timer {

	long start = 0;

	public void start() {
		start = System.nanoTime();
	}

	public void stop() {

		System.out.print((double) (System.nanoTime() - start) / 1000000 + " miliseconds");
	
	}

}
