package operator;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import debs.challenge.msg.CManufacturingMessages.CDataPoint;
import debs.challenge.msg.COutputMessages.CPower;
import debs.challenge.msg.COutputMessages.CViolation;


/**
 * @author Navneet, Tilmann
 *
 */
public class Query2 {


	//First event's Timestamp Holder 
	private long startTime = 0;

	private static final long ONE_SECOND = 1000000000;
	
	
	private long mf01 = 0;
	private long mf02 = 0;
	private long mf03 = 0;
	private long ts = 0;
	
	private long mf01Sum = 0;
	private long mf02Sum = 0;
	private long mf03Sum = 0;
	private long mf01Max = 0;
	private long mf02Max = 0;
	private long mf03Max = 0;
	private long mf01Min = Long.MAX_VALUE;
	private long mf02Min = Long.MAX_VALUE;
	private long mf03Min = Long.MAX_VALUE;
	private float mf01Avg = 0;
	private float mf02Avg = 0;
	private float mf03Avg = 0;
	private float mf01Rng = 0;
	private float mf02Rng = 0;
	private float mf03Rng = 0;
	private long mfTS = 0;
	private long mfCount = 0;
	
	private long violationWindow = 0;
	
	private OneSecondBuffer currentSecondBuffer = new OneSecondBuffer();
	private OneSecondBuffer[] twentySeconds = new OneSecondBuffer[20];
	private int bufferPointer = 0;
	private LinkedList<OneSecondBuffer> violationBuffer = new LinkedList<Query2.OneSecondBuffer>();

	//Counter to track 60 seconds
	private int powerCounter = 0;
	private float[] mf01pwr = new float[60];
	private float[] mf02pwr = new float[60];
	private float[] mf03pwr = new float[60];
	
	
	private boolean display = false;
	private boolean store = false;
	private boolean verbose = false;
	
	public Query2 (boolean s, boolean d, boolean v) {
		this.store = s;
		this.display = d;
		this.verbose = v;
	}

	/*
	 * Call for every data sample
	 * @param mesaurement data coming from generator in google protocol format
	 */
	public void evaluate(CDataPoint measurement)
	{
		if (startTime == 0) {
			startTime = measurement.getTs();
			mfTS = startTime + ONE_SECOND;			
		}
		
		mf01 = measurement.getMf01();
		mf02 = measurement.getMf02();
		mf03 = measurement.getMf03();
		ts = measurement.getTs();
				
		
		//System.out.println(mf01+" "+mf02+" "+mf03+" "+ ts + " " + mfTS + " " + mfCount);
		
		currentSecondBuffer.addEvent();
		operator1_3();

	}
	
	private void operator1_3() {
				
		
		mf01Sum += mf01;
		mf02Sum += mf02;
		mf03Sum += mf03;
		mf01Max =  mf01 > mf01Max ? mf01 : mf01Max;
		mf02Max =  mf02 > mf02Max ? mf02 : mf02Max;
		mf03Max =  mf03 > mf03Max ? mf03 : mf03Max;
		mf01Min =  mf01 < mf01Min ? mf01 : mf01Min;
		mf02Min =  mf02 < mf02Min ? mf02 : mf02Min;
		mf03Min =  mf03 < mf03Min ? mf03 : mf03Min;
		mfCount++; 
		if (mfTS <= ts) {
			mfTS += ONE_SECOND;
			mf01Avg = (float)mf01Sum/mfCount;
			mf02Avg = (float)mf02Sum/mfCount;
			mf03Avg = (float)mf03Sum/mfCount;
			mf01Rng = (float)(mf01Max-mf01Min)/mf01Max;
			mf02Rng = (float)(mf02Max-mf02Min)/mf02Max;
			mf03Rng = (float)(mf03Max-mf03Min)/mf03Max;
			
//			if (mf01Max != mf01Min) {
//			System.out.println("Max " + mf01Max);
//			System.out.println("Min " + mf01Min);
//			System.out.println("Range " + mf01Rng);
//			System.out.println("Avg " + mf01Avg);
//			}
			mf01Max = 0;
			mf02Max = 0;
			mf03Max = 0;
			mf01Min = Long.MAX_VALUE;
			mf02Min = Long.MAX_VALUE;
			mf03Min = Long.MAX_VALUE;
			mf01Sum = 0;
			mf02Sum = 0;
			mf03Sum = 0;
			mfCount = 0;
			
			manageViolationWindow();
			
			twentySeconds[bufferPointer] = currentSecondBuffer;
			currentSecondBuffer = new OneSecondBuffer();
			bufferPointer = ((++bufferPointer) % 20);
			

			
			operator4();
			operator5_6();
			
			
			
		}
	}
	
	private void operator5_6() {
		
			 mf01pwr[powerCounter] = (float) (208 /(Math.pow( mf01Avg, 1.0/3)));
			 mf02pwr[powerCounter] = (float) (208 /(Math.pow( mf02Avg, 1.0/3)));
			 mf03pwr[powerCounter] = (float) (208 /(Math.pow( mf03Avg, 1.0/3)));
			 
			 ++powerCounter;
			 if(powerCounter == 60) {
				 if (store || display)
					 outputPowerData();
				 if (verbose) {
					 System.out.println("Query2 power data output. Timestamp: " + ts);
					 System.out.println("First value mf01pwr: " + mf01pwr[0]);
					 System.out.println("First value mf02pwr: " + mf02pwr[0]);
					 System.out.println("First value mf03pwr: " + mf03pwr[0]);
				 }
				 powerCounter = 0;
			 }
 
			 
		
	}
	
	private void outputPowerData() {
		// Write the new address book back to disk.
		try {
			//TODO file name ??
			FileOutputStream outputFile = new FileOutputStream("tempCPower",true);


			CPower.Builder oPower= CPower.newBuilder();
			for(int i=0;i<60;i++) 
			{
				oPower.setTs(mfTS - (60 -i) * ONE_SECOND );
				oPower.setPower1(mf01pwr[i]);
				oPower.setPower2(mf02pwr[i]);
				oPower.setPower3(mf03pwr[i]);
				oPower.build().writeDelimitedTo(outputFile);
			}
			outputFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//For verification purpose
//		FileInputStream inputFile;
//		try {
//			int j = 0;
//			//TODO file name ??
//			inputFile = new FileInputStream("tempCPower");
//			while(true){
//				CPower oIPower = CPower.parseDelimitedFrom(inputFile);
//				if(oIPower == null){
//					break;
//				}
//			}
//
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		
	}

	private void manageViolationWindow() {
		
		
		
		if (violationWindow > 0) {
			if (violationBuffer.isEmpty()) {
				for(int i = 0; i < 20; i++) {
					if (twentySeconds[(i+bufferPointer)%20] != null)
					violationBuffer.add(twentySeconds[(i+bufferPointer)%20]);
				}
			}
			violationBuffer.add(currentSecondBuffer);
			--violationWindow;
			if (violationWindow == 0) {
				if (store || display)
					outputRawData();
				if (verbose)
					 System.out.println("Query2 raw data output. " + violationBuffer.size() + " seconds. Timestamp: " + ts);
				violationBuffer.clear();
			}
		}
		
	}

	public void operator4 () {
		//System.out.println("Ranges: " + mf02Rng+" , " + mf02Rng+" , " +mf03Rng);
		if(mf01Rng > 0.3 || mf02Rng > 0.3 || mf03Rng > 0.3) {
			violationWindow = 70;
			if (store || display)
				outputViolation();
			if (verbose) {
				 System.out.println("Query2 violation. Timestamp: " + ts);
				 System.out.println("Range mf01: " + mf01Rng);
				 System.out.println("Range mf02: " + mf02Rng);
				 System.out.println("Range mf03: " + mf03Rng);
			}
		}
	}
	
	private void outputRawData() {
		//TODO Correct file name
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("tempRawData", true));
			bw.write("New Voilation buffer dump");
			bw.newLine();
			//Check if this looks good 
			ListIterator<OneSecondBuffer> itr = violationBuffer.listIterator(); 
			OneSecondBuffer oneSec;
			String listString = " Values are in the format mf01, mf02 , mf03, ts \n";
			while(itr.hasNext())
			{
				oneSec = itr.next();
				for ( int i = 0; i < oneSec.mf01Buffer.size(); ++i )
				{
					listString += oneSec.mf01Buffer.get(i) + "\t" + oneSec.mf02Buffer.get(i) + "\t" + oneSec.mf03Buffer.get(i) +"\t"+ oneSec.tsBuffer.get(i)+"\n";
				}
				bw.write(listString);
			}
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {                      
			if (bw != null) try {
				bw.close();
			} catch (IOException ioe2) {
			
			}
		} // end try/catch/finally



	}


	private void outputViolation() {
		// TODO write in proto file
		// Write the new address book back to disk.
		try {
			//TODO file name ??
			FileOutputStream outputFile = new FileOutputStream("tempVoilation",true);


			CViolation.Builder oViolation= CViolation.newBuilder();
			oViolation.setTs(mfTS);
			oViolation.setMf01Avg(mf01Avg);
			oViolation.setMf01Rng(mf01Rng);
			oViolation.setMf02Avg(mf02Avg);
			oViolation.setMf02Rng(mf02Rng);
			oViolation.setMf03Avg(mf03Avg);
			oViolation.setMf03Rng(mf03Rng);
			oViolation.build().writeDelimitedTo(outputFile);
			outputFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private class OneSecondBuffer {
		ArrayList<Long> mf01Buffer;
		ArrayList<Long> mf02Buffer;
		ArrayList<Long> mf03Buffer;
		ArrayList<Long> tsBuffer;
		
		OneSecondBuffer() {
			mf01Buffer = new ArrayList<Long>(110);
			mf02Buffer = new ArrayList<Long>(110);
			mf03Buffer = new ArrayList<Long>(110);
			tsBuffer = new ArrayList<Long>(110);
		}
		
		public void addEvent() {
			mf01Buffer.add(mf01);
			mf03Buffer.add(mf02);
			mf02Buffer.add(mf03);
			tsBuffer.add(ts);
		}
		
		public void clear() {
			mf01Buffer.clear();
			mf02Buffer.clear();
			mf03Buffer.clear();
			tsBuffer.clear();
		}
	}
	

}