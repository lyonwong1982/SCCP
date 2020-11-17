/**
 * 
 */
package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import entities.Node;

/**
 * This Class offers basic parameters used for configure the system.
 * @author Liang Wang
 *
 */
public class Parameters {
//	/**
//	 * 0 indicates that the output will be separate files, while others appended into a single file.
//	 */
//	public static int isAppended = 1;
	/**
	 * The interval is set for reading percentage of processing in fixed cycle (in millisecond).
	 */
	public static int interval = 1000;
	/**
	 * The maximum number of transactions.
	 */
	public static int maxTxnNumber = 40000;
	/**
	 * The minimum number of nodes. It will be used in appending mode.
	 */
	public static int minTxnNumber = 20000;
	/**
	 * The step size for transactions in appending mode.
	 */
	public static int txnStepSize = 1;
	/**
	 * The maximum number of nodes.
	 */
	public static int maxNodeNumber = 30;
	/**
	 * The minimum number of nodes. It will be used in appending mode.
	 */
	public static int minNodeNumber = 4;
//	/**
//	 * The container for creating a spreadsheet.
//	 */
//	public static long[][] spreadsheet = new long[maxNodeNumber-minNodeNumber+1][maxTxnNumber/minTxnNumber/txnStepSize];
	/**
	 * The number of threads that one node will use.
	 */
	public static int threadNumber = 30;
	/**
	 * The AddressTable that every node will download.
	 */
	public static ArrayList<Node> addressTable = new ArrayList<Node>();
	
	/**
	 * The fault tolerance rate in percentage.
	 */
	public static int ftr = 50;
	/**
	 * entropy is used to calculate transmission efficiency and channel capacity
	 * The index of entropy indicate the time interval of the observation,
	 * and the content of it is the entropy of that moment with the unit of bit.
	 */
	public static ArrayList<Integer> entropy = new ArrayList<Integer>();
	
	/**
	 * Get an full clone of the AddressTable.
	 * @return
	 */
	public static LinkedBlockingQueue<Node> getAddressTable(){
		LinkedBlockingQueue<Node> at = new LinkedBlockingQueue<Node>();
		try {
			for(int i=0; i<addressTable.size(); i++) {
				at.put(addressTable.get(i));
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		return at;
	}
	
	/**
	 * Add a new node into the AddressTable.
	 * @param n the node to be added.
	 * @return true if succeed, otherwise false.
	 * @deprecated
	 */
	public static boolean addAddressToAT(Node n) {
		return addressTable.add(n);
	}
	
	/**
	 * Automatically fill up the AddressTable with the amount of the <code>maxNodeNumber</code>.
	 * @param nodeNumber the number of nodes in the network.
	 */
	public static void autoFillAT() {
		for(int i=0; i<maxNodeNumber; i++) {
			addressTable.add(new Node("n" + i));
		}
		
//		TimerPoint.setStartPoint();//Set a start timer point.
//		
//		for(int i=0; i<addressTable.size(); i++) {
//			addressTable.get(i).startWork();
//		}
	}
	
	/**
	 * Start all things.
	 */
	public static void startAllThings() {
		TimerPoint.setStartPoint();//Set a start timer point.
		for(int i=0; i<addressTable.size(); i++) {
			addressTable.get(i).startWork();
		}
	}
	
	/**
	 * Get a random address from the AddressTable except the node exception.
	 * @param exception a Node or null
	 * @return a random address
	 */
	public static Node getRandomAddress(Node exception) {
		Node ra = null;
		do {
			ra = addressTable.get(ThreadLocalRandom.current().nextInt(maxNodeNumber));
		}while(ra == exception);
		return ra;
	}
	
	/**
	 * Clear the AddressTable.
	 * @deprecated
	 */
	public static void clearAddressTable() {
		addressTable.clear();
	}
	
	/**
	 * Get the total number of nodes in the network (AddressTable).
	 * @return the total number of nodes in the network (AddressTable).
	 * @deprecated
	 */
	public static int getNodeNumber() {
		return addressTable.size();
	}
	/**
	 * Read parameters from the configuration file.
	 * @return a hashmap holds all parameters and their values.
	 */
	public static HashMap<String, String> readParameters() {
		BufferedReader reader = null;
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			reader = new BufferedReader(new FileReader("conf.txt"));
			String str = null;
			while((str = reader.readLine()) != null) {
				params.put(str.substring(0, str.indexOf("=")).trim(),
						str.substring(str.indexOf("=") + 1).trim());
			}
			reader.close();
			return params;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}
}
