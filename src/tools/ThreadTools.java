/**
 * 
 */
package tools;

/**
 * This Class contains some useful tools for controlling threads.
 * @author Liang Wang
 *
 */
public class ThreadTools {
	/**
	 * Close all threads of the system, except for ex
	 * @param ex the exceptive thread is normally the main threads
	 */
	public static void closeAllThreads(String ex) {
		ThreadGroup group = Thread.currentThread().getThreadGroup();  
		ThreadGroup topGroup = group;  
		while (group != null) {  
		    topGroup = group;  
		    group = group.getParent();  
		}  
		int estimatedSize = topGroup.activeCount() * 2;  
		Thread[] slackList = new Thread[estimatedSize];  
		int actualSize = topGroup.enumerate(slackList);  
		Thread[] list = new Thread[actualSize];  
		System.arraycopy(slackList, 0, list, 0, actualSize);
		for (Thread thread : list) {  
			if(thread.getName() == ex) {
				continue;
			}
			thread.interrupt();
		}
	}

}
