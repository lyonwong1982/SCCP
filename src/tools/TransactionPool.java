/**
 * 
 */
package tools;

import java.util.concurrent.PriorityBlockingQueue;

import entities.Transaction;

/**
 * This is a transaction pool that is used to initiate all transactions.</br>
 * Nodes will fetch transactions from this pool rather than create ones own.
 * @author Liang Wang
 *
 */
public class TransactionPool {
	public static PriorityBlockingQueue<Transaction> txnPool = new PriorityBlockingQueue<Transaction>();
	
	/**
	 * Initiate all transactions and put them into the pool for future fetching.</br>
	 * This method can only run after create an AddressTable.
	 * Run this method will create <code>maxTxnNumber</code> transactions.
	 */
	public static void initiateAllTxns() {
		for(int i=0; i<Parameters.maxTxnNumber; i++) {
			txnPool.put(new Transaction("t"+i, null, null));
		}
	}
}
