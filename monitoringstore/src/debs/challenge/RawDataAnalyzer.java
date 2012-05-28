package debs.challenge;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.zip.GZIPInputStream;

import org.apache.regexp.recompile;

public class RawDataAnalyzer {
	
	BufferedReader in;
	
	private boolean pp04 = false;
	private boolean pp05 = false;
	private boolean pp06 = false;
	private boolean pp04new = false;
	private boolean pp05new = false;
	private boolean pp06new = false;
	
	private int pp04RL = 0;
	private int pp05RL = 0;
	private int pp06RL = 0;
	
	private int pp04MinRL = Integer.MAX_VALUE;
	private int pp04MaxRL = 0;
	private int pp04NumRL = 0;
	private int pp04SumRL = 0;
	private int pp05MinRL = Integer.MAX_VALUE;
	private int pp05MaxRL = 0;
	private int pp05NumRL = 0;
	private int pp05SumRL = 0;
	private int pp06MinRL = Integer.MAX_VALUE;
	private int pp06MaxRL = 0;
	private int pp06NumRL = 0;
	private int pp06SumRL = 0;
	
	private long mf01 = 0;
	private long mf02 = 0;
	private long mf03 = 0;
	private long mf01Sum = 0;
	private long mf02Sum = 0;
	private long mf03Sum = 0;
	private long mf01Max = 0;
	private long mf02Max = 0;
	private long mf03Max = 0;
	private long mf01Min = Long.MAX_VALUE;
	private long mf02Min = Long.MAX_VALUE;
	private long mf03Min = Long.MAX_VALUE;
	private long mfcount = 0;
	
	
	
	private void initialize(String file) throws IOException {
		in  = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		RawDataAnalyzer rda = new RawDataAnalyzer();
		
		
		try {
			if (args.length < 1) {
				System.out
						.println("Missing parameter DEBS-Challenge data file.");
			}
			rda.initialize(args[0]);
			
			long counter = 0;
			while(true) {
				rda.readLine();
				counter++;
				if (counter %  60000 == 0)
					rda.printResults();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			rda.printResults();
			
		}
		
	}

	
	private void readLine() throws IOException {
		
		String line = in.readLine();
		String[] values = line.split("\t");
		
		pp04new = values[21].equals("1") ? true: false;
		pp05new = values[22].equals("1") ? true: false;
		pp06new = values[23].equals("1") ? true: false;
		
		if (pp04new == pp04) {
			pp04RL++;
		} else {
			pp04SumRL += pp04RL;
			pp04NumRL++;
			if (pp04RL > pp04MaxRL)
				pp04MaxRL = pp04RL;
			if (pp04RL < pp04MinRL)
				pp04MinRL = pp04RL;
			pp04RL = 1;
			pp04 = pp04new;
		}
		
		if (pp05new == pp05) {
			pp05RL++;
		} else {
			pp05SumRL += pp05RL;
			pp05NumRL++;
			if (pp05RL > pp05MaxRL)
				pp05MaxRL = pp05RL;
			if (pp05RL < pp05MinRL)
				pp05MinRL = pp05RL;
			pp05RL = 1;
			pp05 = pp05new;
		}
		
		if (pp06new == pp06) {
			pp06RL++;
		} else {
			pp06SumRL += pp06RL;
			pp06NumRL++;
			if (pp06RL > pp06MaxRL)
				pp06MaxRL = pp06RL;
			if (pp06RL < pp06MinRL)
				pp06MinRL = pp06RL;
			pp06RL = 1;
			pp06 = pp06new;
		}
		
		mf01 = Long.parseLong(values[2]);
		mf02 = Long.parseLong(values[3]);
		mf03 = Long.parseLong(values[4]);
		
		mfcount++;
		
		mf01Max = mf01 > mf01Max? mf01: mf01Max;
		mf01Min = mf01 < mf01Min? mf01: mf01Min;
		mf01Sum += mf01;
		
		mf02Max = mf02 > mf02Max? mf02: mf02Max;
		mf02Min = mf02 < mf02Min? mf02: mf02Min;
		mf02Sum += mf02;
		
		mf03Max = mf03 > mf03Max? mf03: mf03Max;
		mf03Min = mf03 < mf03Min? mf03: mf03Min;
		mf03Sum += mf03;
		
		
		
	}
	
	private void printResults() {
		
		System.out.println("Evaluation Results:");
		System.out.println("---pp04---");
		System.out.println("Avg Run Length:	" + (float)pp04SumRL/pp04NumRL);
		System.out.println("Max Run Length:	" + pp04MaxRL);
		System.out.println("Min Run Length:	" + pp04MinRL);
		System.out.println("---pp05---");
		System.out.println("Avg Run Length:	" + (float)pp05SumRL/pp05NumRL);
		System.out.println("Max Run Length:	" + pp05MaxRL);
		System.out.println("Min Run Length:	" + pp05MinRL);
		System.out.println("---pp06---");
		System.out.println("Avg Run Length:	" + (float)pp06SumRL/pp06NumRL);
		System.out.println("Max Run Length:	" + pp06MaxRL);
		System.out.println("Min Run Length:	" + pp06MinRL);
		System.out.println("---mf01---");
		System.out.println("Avg Value:		" + (float)mf01Sum/mfcount);
		System.out.println("Max Value:		" + mf01Max);
		System.out.println("Min Value:		" + mf01Min);
		System.out.println("---mf02---");
		System.out.println("Avg Value:		" + (float)mf02Sum/mfcount);
		System.out.println("Max Value:		" + mf02Max);
		System.out.println("Min Value:		" + mf02Min);
		System.out.println("---mf03---");
		System.out.println("Avg Value:		" + (float)mf03Sum/mfcount);
		System.out.println("Max Value:		" + mf03Max);
		System.out.println("Min Value:		" + mf03Min);
		
	}
}
