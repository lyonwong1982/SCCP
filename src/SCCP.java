/**
 * Simulator for Cascading Consensus Protocol
 */

import java.util.HashMap;
import java.util.Iterator;

import entities.Transaction;
import tools.*;
/**
 * The Simulator for Cascading Consensus Protocol.
 * @author Liang Wang
 *
 */
public class SCCP {
	public static void main(String[] args) {
		//Read configuration from the keyboard
//		try(Scanner in = new Scanner(System.in)){
////			System.out.print("Enter the max number of nodes:");
////			Parameters.maxNodeNumber = Integer.parseInt(in.nextLine());
////			System.out.print("Enter the max number of transactions:");
////			Parameters.maxTxnNumber = Integer.parseInt(in.nextLine());
//			//Method one for setting threads.
////			Parameters.threadNumber = (Parameters.maxNodeNumber * 5 + 120) / 14;
//			//Method two for setting threads.
////			Parameters.threadNumber = Parameters.maxTxnNumber / Parameters.maxNodeNumber;
//			//Method three for setting threads.
//			System.out.print("Enter thread number:");
//			Parameters.threadNumber = Integer.parseInt(in.nextLine());
//		}
		//Read configuration from conf.txt
		HashMap<String, String> params = Parameters.readParameters();
		Parameters.interval = Integer.valueOf(params.get("interval"));
		Parameters.maxNodeNumber = Integer.valueOf(params.get("maxNodeNumber"));
		Parameters.maxTxnNumber = Integer.valueOf(params.get("maxTxnNumber"));
		Parameters.threadNumber = Integer.valueOf(params.get("threadNumber"));
		Parameters.ftr = Integer.valueOf(params.get("ftr"));
		
//		The simulation starts...
		System.out.println("Start...");
		
		//For reporting
		Runnable r1 = ()->{
			while(true) {
				try {
					Thread.sleep(Parameters.interval);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
//				Record entropy -- method 1 -- average
//				int entropy = 0;
//				for(int i = 0; i < Parameters.addressTable.size(); i++) {
//					entropy += Parameters.addressTable.get(i).getInboxSize();
//				}
//				entropy /= Parameters.addressTable.size();
				
//				Record entropy -- method 2 -- maximum
				int entropy = 0;
				for(int i = 0; i < Parameters.addressTable.size(); i++) {
					if(entropy < Parameters.addressTable.get(i).getInboxSize())
					entropy = Parameters.addressTable.get(i).getInboxSize();
				}
				Parameters.entropy.add(entropy);
				
//				Record entropy -- method 3 -- average on active transactions
//				Parameters.entropy.add(TimerPoint.activeTxn.intValue() / Parameters.maxNodeNumber);
				
//				Record entropy -- method 4 -- average on outbox
//				Parameters.entropy.add(Outbox.outbox.size() / Parameters.maxNodeNumber);
				
//				Record entropy -- method 5 -- average on waiting for transmission
//				int entropy = 0;
//				Iterator<Transaction> txns = Outbox.outbox.iterator();
//				while(txns.hasNext()) {
//					if(txns.next().onTransfer) {
//						entropy ++;
//					}
//				}
//				Parameters.entropy.add(entropy / Parameters.maxNodeNumber);
				
//				Show progress
				float percentage = Outbox.confirmedTxns.intValue()*100.00f/Parameters.maxTxnNumber;
				System.out.println(percentage + "%");
			}
		};

		//Initiate AddressTable
		Parameters.autoFillAT();
		//Initiate transaction pool
		TransactionPool.initiateAllTxns();
		//Start the experiment
		Parameters.startAllThings();
		//Start processing and reporting
		new Thread(r1).start();
		
		
		
//		The following code is reserved for an auxiliary.
		
//		Thread.currentThread().setName("main");
//		//Configuration
//		try(Scanner in = new Scanner(System.in)){
//			System.out.print("Select a mode, 0 for single mode and 1 for appending mode:");
//			Parameters.isAppended = Integer.parseInt(in.nextLine());
//			if(Parameters.isAppended == 0) {
//				System.out.print("Enter the max number of nodes:");
//				Parameters.maxNodeNumber = Integer.parseInt(in.nextLine());
//				Parameters.threadNumber = Math.round(Parameters.maxNodeNumber * 2.27f + 30.91f);
//				System.out.print("Enter the max number of transactions:");
//				Parameters.maxTxnNumber = Integer.parseInt(in.nextLine());
////				System.out.print("Enter thread number:");
////				Parameters.threadNumber = Integer.parseInt(in.nextLine());
////				System.out.print("Enter fault tolerance rate (%):");
////				Parameters.ftr = Integer.parseInt(in.nextLine());
//				System.out.print("Enter interval for reporting:");
//				Parameters.interval = Integer.parseInt(in.nextLine());
////				System.out.print("Enter 1 if you want to append the result into a single file, otherwise 0:");
////				Parameters.isAppended = Integer.parseInt(in.nextLine());
//			}
//			else {
//				System.out.println("Appending mode configuration:");
//				System.out.println("  Min number of nodes: " + Parameters.minNodeNumber);
//				System.out.println("  Max number of nodes: " + Parameters.maxNodeNumber);
//				System.out.println("  Min number of transactions: " + Parameters.minTxnNumber);
//				System.out.println("  Max number of transactions: " + Parameters.maxTxnNumber);
//				System.out.println("  Step size for increasing transactions: " + Parameters.txnStepSize);
//				System.out.println("  Fault tolerance rate: " + Parameters.ftr);
//			}
//			
//		}
//		System.out.println("Start...");
//		
//		//For reporting
//		Runnable r1 = ()->{
//			while(!Thread.currentThread().isInterrupted()) {
//				try {
//					Thread.sleep(Parameters.interval);
//				}catch(InterruptedException e) {
//					e.printStackTrace();
//				}
//				if(Parameters.isAppended == 0) {//Single mode
//					float percentage = Outbox.confirmedTxns.intValue()*100.00f/Parameters.maxTxnNumber;
//					System.out.println(percentage + "%");
//					Reportor.reportSummary();
//				}
//				else {//Appending mode
//					////////////////////////////////////////////////
//				}
//			}
//		};
//
//		if(Parameters.isAppended == 0) {//Start single mode
//			//Initiate AddressTable
//			Parameters.autoFillAT();
//			//Initiate transaction pool
//			TransactionPool.initiateAllTxns();
//			//Start reporting
//			new Thread(r1).start();
//		}
//		else {//Start appending mode
//			int maxTxnNumber = Parameters.maxTxnNumber;
//			int minTxnNumber = Parameters.minTxnNumber;
//			int maxNodeNumber = Parameters.maxNodeNumber;
//			int minNodeNumber = Parameters.minNodeNumber;
//			
////			for()
//		}
	}
}
