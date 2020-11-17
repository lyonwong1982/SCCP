/**
 * Entities in the CCP.
 */
package entities;

import java.util.Date;
import java.util.concurrent.*;

import tools.Outbox;
import tools.Parameters;
import tools.Reportor;
import tools.TimerPoint;
import tools.TransactionPool;

/**
 * The <code>Node</code> class emulates the behavior of the nodes in the CCP.
 * @author Liang Wang
 *
 */
public class Node implements Runnable{
	private String id;
	//The inbox is used to receive transactions about to be processed.
	private PriorityBlockingQueue<Transaction> inbox;
	//The ownUnconfirmedTxn holds the node's own transactions about to be confirmed.
	private LinkedBlockingQueue<Transaction> ownUnconfirmedTxn;
	//The otherUnconfirmedTxn holds others' transactions about to be confirmed.
//	private LinkedBlockingQueue<Transaction> othersUnconfirmedTxn;
	//The thrd is a daemon thread that help other threads process transactions.
	private Thread thrd;
	
	public Node(String id) {
		this.id = id;
		inbox = new PriorityBlockingQueue<Transaction>();
		ownUnconfirmedTxn = new LinkedBlockingQueue<Transaction>();
//		othersUnconfirmedTxn = new LinkedBlockingQueue<Transaction>();
		thrd = new Thread(this);
	}
	
	public void startWork() {
		thrd.start();
		for(int i=0; i<Parameters.threadNumber; i++) {
			new Thread(this).start();
		}
	}
	
	/**
	 * Add an transaction to the inbox, i.e. receive a transaction to be processed.
	 * @param txn the transaction to be added.
	 * @param sender the sender node.
	 */
	public void addToInbox(Transaction txn, Node sender) {
		txn.onTransfer = true;
//		TimerPoint.activeTxn.incrementAndGet();
//		System.out.println(txn.getFrom().getId() + "->" + txn.getTo().getId() + " was put into the inbox of " + this.getId() + " by " + sender.getId());
		try {//Simulate the network delay of transferring one transaction.
			//((372l^2+524428l-524544)*1000/(3l-1)/31457280-(3l-1)
//			Thread.sleep(Math.round((((long)Math.pow(Parameters.maxNodeNumber,2)*372L+524428*Parameters.maxNodeNumber-524544L)*1000.0d)/((3*Parameters.maxNodeNumber-1)*31457280.0d)));
			Thread.sleep((((long)Math.pow(Parameters.maxNodeNumber,2)*372L+524428*Parameters.maxNodeNumber-524544L)*1000L)/((3*Parameters.maxNodeNumber-1)*31457280L));
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
//		TimerPoint.activeTxn.decrementAndGet();
		txn.times.incrementAndGet();
		inbox.put(txn);
	}
	
	/**
	 * Get the Id of this node.
	 * @return the Id of this node.
	 */
	public String getId() {return id;}
	/**
	 * Get the size of the Inbox of this node.
	 * @return the size of the inbox.
	 */
	public int getInboxSize() {
		return inbox.size();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				if(Thread.currentThread() == thrd) {//Main thread is used to initiate transactions.
					if(!TransactionPool.txnPool.isEmpty()) {
						Transaction txn = TransactionPool.txnPool.take();
						txn.timestamp = new Date().getTime();
						txn.setFrom(this);
						txn.setTo(Parameters.getRandomAddress(this));
						ownUnconfirmedTxn.put(txn);
						Outbox.addTxn(txn);
						txn.getTo().addToInbox(txn, this);
					}
				}
				else {
					Transaction txn = inbox.take();
					txn.onTransfer = false;
					int sigBalance = txn.verifySL(Parameters.maxNodeNumber, Parameters.ftr);
					if(sigBalance == -Parameters.maxNodeNumber) {
						throw new Exception("The fault tolerance rate was set wrong!");
					}
					if(this == txn.getFrom()) {//Received the transaction A->B
						if(txn.getC1()) {//Stage 2
//							if(sigBalance >= 0) {
//								if(ownUnconfirmedTxn.remove(txn)) {
//									if(!Outbox.confirmTxn(txn)) {//Confirm the transaction.
//										throw new Exception("No such transactions to be confirmed: " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());
//									}
//									Reportor.reportSummary();
//								}
//							}
						}
						else {//Stage 1
							if(sigBalance < 0) {
								txn.takeAddress().addToInbox(txn, this);
							}
							else {//Start Stage 2
								txn.addDataFrag();
								txn.resetAddressTable();
								txn.clearSL();
								txn.takeAddress().addToInbox(txn, this);
							}
						}
					}
					else if(this == txn.getTo()){//Received the transaction B->A
						if(txn.getC1()) {//Stage 2
							if(sigBalance >= 0) {
								if(ownUnconfirmedTxn.remove(txn)) {
									if(!Outbox.confirmTxn(txn)) {//Confirm the transaction.
										throw new Exception("No such transactions to be confirmed: " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());
									}
									Reportor.reportSummary();
								}
							}
							else {
//								Node n = txn.takeAddress();
//								if(n == null) {System.out.println("Receiver+P2+" + this.getId() + ": The error txn is " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());}
//								else {n.addToInbox(txn, this);}
								txn.takeAddress().addToInbox(txn, this);
							}
						}
						else {//Stage 1
							if(!ownUnconfirmedTxn.contains(txn)) {
								ownUnconfirmedTxn.put(txn);
								txn.takeAddress().addToInbox(txn, this);
							}
						}
					}
					else {//Received the transaction C->D
						if(txn.getC1()) {//Stage 2
							if(!txn.checkSL(this.getId())) {
								txn.appendS(this.getId());
							}
//							othersUnconfirmedTxn.remove(txn);
//							txn.getFrom().addToInbox(txn, this);???
//							System.out.println(txn.getAddressNumber());//////////////////////
//							System.out.println(txn.getSigsSize());//////////////////////////
//							Node n = txn.takeAddress();
//							if(n == null) {System.out.println("Signer+P2+" + this.getId() + ": The error txn is " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());}
//							else {n.addToInbox(txn, this);}
							if(sigBalance >= 0) {
								if(txn.getFrom().ownUnconfirmedTxn.remove(txn)) {
									if(!Outbox.confirmTxn(txn)) {//Confirm the transaction.
										throw new Exception("No such transactions to be confirmed: " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());
									}
									Reportor.reportSummary();
								}
							}
							else {
								txn.takeAddress().addToInbox(txn, this);
							}
						}
						else {//Stage 1
							if(!txn.checkSL(this.getId())) {
								txn.appendS(this.getId());
							}
//							if(!othersUnconfirmedTxn.contains(txn)) {
//								othersUnconfirmedTxn.add(txn);
//							}
//							txn.getTo().addToInbox(txn, this);
//							System.out.println(txn.getAddressNumber());///////////////////////
//							System.out.println(txn.getSigsSize());///////////////////////////
//							Node n = txn.takeAddress();
//							if(n == null) {System.out.println("Signer+P1+" + this.getId() + ": The error txn is " + txn.getId() + " ~ " + txn.getFrom().getId() + "->" + txn.getTo().getId());}
//							else {n.addToInbox(txn, this);}
							txn.takeAddress().addToInbox(txn, this);
						}
					}
				}
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
