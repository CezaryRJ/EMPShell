
import java.util.ArrayList;

/**
 * Every module should implements this
 * 
 * @author Cezary
 *
 */
public interface runVoid {
	
/**
 *  This should be called to run a given module
 * @param inn 
 * @throws Exception
 */
	public void run(ArrayList<String> inn) throws Exception;
	
	/**
	 * This should print instructions on how to use the module
	 */
	public void help();
}
