/**
 * 
 */
package operator;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Float;
import debs.challenge.msg.CManufacturingMessages.CDataPoint;
import debs.challenge.msg.COutputMessages.CPower;


/**
 * @author Navneet
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
	
	
	public Query2 () {
		
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
				
		if (mfTS > ts) {
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
		} else {
			mfTS += ONE_SECOND;
			mf01Avg = mf01Sum/mfCount;
			mf02Avg = mf02Sum/mfCount;
			mf03Avg = mf03Sum/mfCount;
			mf01Rng = (mf01Max-mf01Min)/mf01Max;
			mf02Rng = (mf02Max-mf02Min)/mf02Max;
			mf03Rng = (mf03Max-mf03Min)/mf03Max;
			
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
				 outputPowerData();
				 powerCounter = 0;
			 }
 
			 
		
	}
	private void outputPowerData() {
		// Write the new address book back to disk.
		try {
			//TODO file name ??
			FileOutputStream outputFile = new FileOutputStream("tempCPower");


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
					violationBuffer.add(twentySeconds[(i+bufferPointer)%20]);
				}
			}
			violationBuffer.add(currentSecondBuffer);
			--violationWindow;
			if (violationWindow == 0) {
				outputRawData();
				violationBuffer.clear();
			}
		}
		
	}

	public void operator4 () {
		if(mf01Rng > 0.3 || mf02Rng > 0.3 || mf03Rng > 0.3) {
			violationWindow = 70;
			outputViolation();
		}
	}
	
	private void outputRawData() {
		//TODO Where to write??
		System.out.println("Raw Data");
	}


	private void outputViolation() {
		// TODO write in proto file
		System.out.println("Violation! at " + ts);
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
