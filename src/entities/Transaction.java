/**
 * Entities in the CCP.
 */
package entities;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import tools.Parameters;
import tools.RandTools;

/**
 * The <code>Transaction</code> represents a data delivery record on a blockchain.
 * @author Liang Wang
 *
 */
public class Transaction implements Comparable<Transaction>{
	private String id;
	private Node from;
	private Node to;
	private LinkedBlockingQueue<String> sigs;
	private boolean c1;
	private LinkedBlockingQueue<Node> addressTable;
	public long timestamp;
	public AtomicInteger times;// The number of times that the current transaction has been transferred.
	public boolean onTransfer;
	
	public Transaction(String id, Node from, Node to){
//		try {
//			Thread.sleep(1);
//		}catch(InterruptedException e) {
//			e.printStackTrace();
//		}
		this.id = id;
		this.from = from;
		this.to = to;
		this.sigs = new LinkedBlockingQueue<String>();
		c1 = false;
		addressTable = Parameters.getAddressTable();
		timestamp = 0;
		times = new AtomicInteger();
		onTransfer = false;
	}
	
	/**
	 * Take away an address from the AddressTable.</br>
	 * We use the control on AddressTable to simulate the timeout of every node.
	 * @return the node that has been taken away from the AddressTable. Or otherwise, null.
	 */
	public Node takeAddress() {
		if(addressTable.isEmpty()) {
			return null;
		}
		Node n = null;
		try {
			//Delete unexpected nodes.
			if(c1) {addressTable.remove(from);}
			else {addressTable.remove(to);}
			
//			Randomly taking an address
			int rand = RandTools.randomInt(0, addressTable.size());
			for(int i = 0; i < rand; i ++) {
				n = addressTable.take();
				addressTable.put(n);
			}
			n = addressTable.take();
			
			if(c1) {//Stage 2
				if(n == to) {//In Stage 2, if the transaction was sent to the receiver in advance, it should be sent to it again.
//					System.out.println(addressTable.size()+"P2");
					if(!addressTable.isEmpty()) {
						addressTable.put(n);
//						System.out.println(addressTable.size()+":::Stage 2");
					}
				}
			}
			else {//Stage 1
				if(n == from) {//In Stage 1, if the transaction was sent to the sender in advance, it should be sent to it again.
//					System.out.println(addressTable.size()+"P1");
					if(!addressTable.isEmpty()) {
						addressTable.put(n);
//						System.out.println(addressTable.size()+":::Stage 1");
					}
				}
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		return n;
	}
	
	/**
	 * Reset the AddressTable.
	 */
	public void resetAddressTable() {
		addressTable = Parameters.getAddressTable();
	}
	
	/**
	 * This method is just used for test.
	 * @return the size of the AddressTable
	 */
	public int getAddressNumber() {
		return addressTable.size();
	}
	
	/**
	 * 
	 * @return the Id of the current transaction.
	 */
	public String getId() {return id;}
	
	/**
	 * Note: Modify the return value will causing modification of the original one in the Transaction.
	 * @return the Node that sent the data to be delivered.
	 */
	public Node getFrom() {return from;}
	
	/**
	 * Set the from node.
	 * @param n the from node
	 */
	public void setFrom(Node from) {this.from = from;}
	
	/**
	 * Note: Modify the return value will causing modification of the original one in the Transaction.
	 * @return the Node that received the data to be delivered.
	 */
	public Node getTo() {return to;}
	
	/**
	 * Set the to node.
	 * @param n the to node
	 */
	public void setTo(Node to) {this.to = to;}
	
	/**
	 * 
	 * @return Check that if C1 exists.
	 */
	public boolean getC1() {return c1;}
	
	/**
	 * This method returns the size of sigs only for test.
	 * @return the size of sigs
	 */
	public int getSigsSize() {return sigs.size();}
	
	/**
	 * Append a signature onto the transaction.
	 * @param sig the signature with a String format.
	 */
	public void appendS(String sig) {
		try {
			this.sigs.put(sig); 
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add C1 into the transaction.
	 */
	public void addDataFrag() {
		this.c1 = true;
	}
	
	/**
	 * Verify the signature list.
	 * @param n the total number of nodes.
	 * @param e the fault tolerance rate in percentage. e should be neither more than 50 nor less than 200/n. 
	 * @return 
	 * <0 represents the number of signatures that still needs to collect;</br>
	 * >=0 represents enough signatures have been collected;</br>
	 * -n indicates that the input e is invilid.
	 */
	public int verifySL(int n, int e) {
		if(e>50 || e*n/100.00<2) return -n;
		int nt = n/2 + Math.round(e * n / 100.00f) - 2;
		return sigs.size() - nt;
	}
	
	/**
	 * Check the signature list to see if the given nodeId has been signed in.
	 * @param nodeId the Id of the given node.
	 * @return true if the nodeId is in the signature list, otherwise false.
	 */
	public boolean checkSL(String nodeId) {
		return sigs.contains(nodeId);
	}
	
	/**
	 * Clear the signature list of the transaction.
	 */
	public void clearSL() {
		sigs.clear();
	}
	
	/**
	 * Sort by ASC
	 */
	@Override
	public int compareTo(Transaction txn) {
		long tag = this.timestamp - txn.timestamp;
		if(tag > 0L) return 1;
		else if(tag < 0L) return -1;
		else return 0;
	}
}
