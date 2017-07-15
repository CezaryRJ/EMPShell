package general;

import java.util.List;

/**
 * Every module should implements this
 * 
 * @author Cezary
 *
 */
public abstract class runVoid {
	
/**
 *  This should be called to run a given module
 * @param list 
 * @throws Exception
 */
	public abstract Object run(List<Object> list) throws Exception;
	
	/**
	 * This should print instructions on how to use the module
	 */
	public void help() {
		System.out.println( this.getClass().getName() + " has not implemented the help method\n");
	}
}
