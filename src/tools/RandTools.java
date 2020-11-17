/**
 * Tools about Random affairs
 */
package tools;

import java.util.Random;

/**
 * @author Liang Wang
 *
 */
public class RandTools {
	/**
	 * Get a random integer that begins with start and end with (end - 1).
	 * @param start The beginning of the range.
	 * @param end The ending of the range.
	 * @return
	 */
	public static int randomInt(int start, int end) {
		try {
			if(start >= end) {
				throw new Exception("Wrong argument(s)!");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Random r = new Random();
		int rand = r.nextInt(end - start);
		return rand + start;
	}
}
