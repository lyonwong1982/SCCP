/**
 * Tools for maintaining a CCP consensus.
 */
package tools;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import entities.Transaction;

/**
 * The <code>OutBox</code> is a Class for maintaining the incomplete transactions.
 * @author Liang Wang
 *
 */
public class Outbox {
	/**
	 * Count the number of finished transactions.
	 */
	public static AtomicInteger confirmedTxns = new AtomicInteger();
	
	/**
	 * The <code>outbox</code> holds all unconfirmed transactions.
	 */
	public static LinkedBlockingQueue<Transaction> outbox = new LinkedBlockingQueue<Transaction>();
	
	/**
	 * Add an unconfirmed transaction into outbox.
	 * @param txn the transaction to be added.
	 * @return ture if succeed, otherwise false.
	 */
	public static void addTxn(Transaction txn) {
		try {
			outbox.put(txn);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Confirm a transaction and remove it from outbox.
	 * @param txn the transaction to be confirmed.
	 * @return true if succeed, otherwise false.
	 */
	public static boolean confirmTxn(Transaction txn) {
		if(outbox.contains(txn)) {
//			int l = Parameters.maxNodeNumber;
			//Preserve this code
//			try {//Simulate the network delay of one transaction.
//				Thread.sleep(4*(int)Math.pow(l, 2)/1000+17*l-17);
//			}
//			catch(InterruptedException e) {
//				e.printStackTrace();
//			}

			long latency = System.currentTimeMillis() - txn.timestamp;
//			System.out.println(latency+txn.getFrom().getId());
			TimerPoint.addLatency(latency);

//			Record the number of times that this transaction has been transferred.
			try {
				TimerPoint.times.put(txn.times.intValue());
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			confirmedTxns.incrementAndGet();
			
			if(confirmedTxns.intValue() == Parameters.maxTxnNumber) {
				TimerPoint.setEndPoint();//The experiment is over.
			}
			
			return outbox.remove(txn);
		}
		return false;
	}
	
	/**
	 * Record concurrency of each time point (approximately).</br>
	 * This method should be run right after autoFillAT().
	 * @deprecated
	 */
	public static void recordConcurrency() {
		try {
			TimerPoint.concurrencies.put(Integer.valueOf(concurrency()));
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the total number of unconfirmed transactions, which is the concurrency of the CCP.
	 * @return the total number of unconfirmed transactions.
	 */
	public static int concurrency() {
		return outbox.size();
	}
	
//	public static void main(String[] args) {
//		ArrayList<Integer> k = new ArrayList<Integer>();
//		for(int i=0; i <20000; i ++) {
//			k.add(i);
//		}
//		System.out.println(k.size());
//	}
}
