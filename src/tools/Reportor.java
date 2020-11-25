/**
 * 
 */
package tools;

import java.io.*;

/**
 * This tool can report the results of the experiment in a CSV file.
 * @author Liang Wang
 *
 */
public class Reportor {
	/**
	 * Report the results in a CSV file. <br>
	 * This method will print the entropy counting result.
	 * @param fileName the name of the CSV file.
	 * @param text the part of results in text.
	 */
	public static void report(String fileName, String text) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			File f = new File(fileName +".csv");
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();
			fw = new FileWriter(f, true);
			pw = new PrintWriter(fw);
			pw.println(text);
			String header = "Transmission Efficiency\n";
			header += "Timeline," + "Entropy," + "Average";
			pw.println(header);			
			for(int i=0; i < Parameters.entropy.size(); i++) {
				int avg = 0;
				for(int j = 0; j <= i; j ++) {
					avg += Parameters.entropy.get(j);
				}
				avg /= (i + 1);
				pw.println((i+1) + "," + Parameters.entropy.get(i) + "," + avg);
			}
			header = "Transmission Cost rate\n";
			header += "Information Content," + "Cost Rate";
			pw.println(header);	
//			System.out.println(TimerPoint.times.size());
//			System.out.println(Outbox.confirmedTxns.intValue());
			try {
				while(! TimerPoint.times.isEmpty()) {
					int ic = TimerPoint.times.take().intValue();
					pw.println(ic + "," + Math.round((ic - (Parameters.maxNodeNumber * 2 - 1)) * 100.00 / (Parameters.maxNodeNumber * 2 - 1)) / 100.00);
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pw.flush();
				fw.flush();
				pw.close();
				fw.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finished!");
		System.exit(0);
	}
	
//	/**
//	 * Report the results in a CSV file.
//	 * @param fileName the name of the CSV file.
//	 * @param text the part of results in text.
//	 */
//	public static void report(String fileName, String text) {
//		FileWriter fw = null;
//		PrintWriter pw = null;
//		try {
//			File f = new File(fileName +".csv");
//			if (f.exists()) {
//				f.delete();
//			}
//			f.createNewFile();
//			fw = new FileWriter(f, true);
//			pw = new PrintWriter(fw);
//			pw.println(text);
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				pw.flush();
//				fw.flush();
//				pw.close();
//				fw.close();
//			}catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("Finished!");
//		System.exit(0);
//	}
	
	/**
	 * Summary for reporting.
	 */
	public static void reportSummary() {
//		System.out.println(Outbox.confirmedTxns.intValue()+"::::"+Parameters.maxTxnNumber);

//		Check ending
		if(Outbox.confirmedTxns.intValue()== Parameters.maxTxnNumber) {
//			TimerPoint.setEndPoint();//The experiment is over.
			//Report the results of the experiment.
			long totalTime = TimerPoint.endPoint - TimerPoint.startPoint;//Total time
			long averageLatency = TimerPoint.average();//Average latency
			long averageThroughput = (long)Parameters.maxTxnNumber * 1000L / totalTime;//Average throughput
			String text = "";
			text = text + "Nodes: " + Parameters.maxNodeNumber + "\n";
			text = text + "Transactions: " + Parameters.maxTxnNumber + "\n";
			text = text + "Threads: " + Parameters.threadNumber + "\n";
			text = text + "Fault tolerance rate: " + Parameters.ftr + "%\n";
			text = text + "Total time: " + totalTime + " ms\n";
			text = text + "Average Latency: " + averageLatency + " ms/t\n";
			text = text + "Throughput: " + averageThroughput + " tps";
//			String header = "Time(ms),Time(s),concurrency";
			String fileName = "N_" + Parameters.maxNodeNumber +
					"_Tx_" + Parameters.maxTxnNumber + 
					"_Th_" + Parameters.threadNumber + 
					"_ftr_" + Parameters.ftr +
					"_Time_" + System.currentTimeMillis();
			System.out.println(text);
//			report(fileName, text);
			report(fileName, text);
		}
	}
	
//	/**
//	 * Report the results in a CSV file.
//	 * @param fileName the name of the CSV file.
//	 * @param text the part of results in text.
//	 * @param header the header for concurrencies.
//	 * @param interval the factor for computing the column <code>time</code>.
//	 * @param concurrencies the outcome of concurrencies.
//	 */
//	public static void report(String fileName, String text, String header, int interval, LinkedBlockingQueue<Integer> concurrencies) {
//		FileWriter fw = null;
//		PrintWriter pw = null;
//		try {
//			File f = new File(fileName +".csv");
//			if (f.exists()) {
//				f.delete();
//			}
//			f.createNewFile();
//			fw = new FileWriter(f, true);
//			pw = new PrintWriter(fw);
//			pw.println(text);
//			pw.println(header);			
//			for(int i=1; !concurrencies.isEmpty(); i++) {
//				pw.println((i * interval) + "," + (i * interval / 1000.000f) + "," + concurrencies.take().intValue());
//			}
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//		catch(InterruptedException e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				pw.flush();
//				fw.flush();
//				pw.close();
//				fw.close();
//			}catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("Finished!");
//		System.exit(0);
//	}
}
