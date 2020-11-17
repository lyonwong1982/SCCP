/**
 * 
 */
package tools;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a timer Class that is used to set timer points.
 * @author Liang Wang
 *
 */
public class TimerPoint {
	/**
	 * Use concurrencies to record the concurrency of each time point.
	 */
	public static LinkedBlockingQueue<Integer> concurrencies = new LinkedBlockingQueue<Integer>(); 
	/**
	 * Use startPoint to mark the beginning of an event.
	 */
	public static long startPoint = 0L;
	/**
	 * Use endPoint to mark the end of an event.
	 */
	public static long endPoint = 0L;
	/**
	 * Use latencies to collect the time of confirming each transaction.
	 */
	public static LinkedBlockingQueue<Long> latencies = new LinkedBlockingQueue<Long>();
	/**
	 * Record the number of times that each transaction has been transferred.
	 */
	public static LinkedBlockingQueue<Integer> times = new LinkedBlockingQueue<Integer>();
	/**
	 * activeTxn is used to count the number of transactions that are running on channels.
	 */
	public static AtomicInteger activeTxn = new AtomicInteger();
	
	/**
	 * Add a latency.
	 * @param latency the value to be added
	 */
	public static void addLatency(long latency) {
		try {
			latencies.put(Long.valueOf(latency));
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculate the average latency of one transaction.
	 * @return the average latency
	 */
	public static long average() {
		if(!latencies.isEmpty()) {
			long temp = 0L;
			Iterator<Long> itr = latencies.iterator();
			while(itr.hasNext()) {
				temp = temp + itr.next().longValue();
			}
			return temp/(long)latencies.size();
		}
		else {
			return 0L;
		}
	}
	
	/**
	 * Set a startPoint.
	 */
	public static void setStartPoint() {
		startPoint = new Date().getTime();
	}
	/**
	 * Set an endPoint.
	 */
	public static void setEndPoint() {
		endPoint = new Date().getTime();
	}
}
