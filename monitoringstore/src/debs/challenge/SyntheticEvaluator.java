package debs.challenge;

import operator.Query1;
import operator.Query2;
import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class SyntheticEvaluator {
	

	private long currenttime = System.nanoTime();
	private static final long TIME_INCREMENT = 10000000;
	private static final int MF01 = 14000;
	private static final int MF02 = 15000;
	private static final int MF03 = 9000;
	
	private static final int MF01Error = 8000;
	private static final int MF02Error = 22000;

	private static final long MF01ErrorFrequency = 300000000000L/TIME_INCREMENT;
	private static final long MF02ErrorFrequency = 700000000000L/TIME_INCREMENT;
	private static final long MF02ErrorDuration = 2500000000L/TIME_INCREMENT;
	
	private static final long QUERY1OFFSET = 1500000000L/TIME_INCREMENT; // 1.5 sec
	private static final long QUERY1PERIOD = 130000000000L/TIME_INCREMENT; //20 min + 10 sec
	
	private boolean pp04 = false;
	private boolean pp05 = false;
	private boolean pp06 = false;
	
	
	private long counter = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	
		if (args.length == 1) {
			
			

			SyntheticEvaluator se = new SyntheticEvaluator();
		
			int runtime = Integer.parseInt(args[0]);
			//Query2 q2 = new Query2(true, false, true);
			Query1 q1 = new Query1(true, false, true);
			for (int i = 0; i < runtime; i++) { 
				//System.out.println("Frequencies: " + MF01ErrorFrequency);
				
				//Thread.sleep(10);
				CDataPoint measurement = se.generateMeasurement();
				q1.evaluate(measurement);
			}
		} else {
			System.out.println("Necessary parameters: number of events in test.");
		}
	}

	private CDataPoint generateMeasurement() {
		CDataPoint.Builder measurement = CDataPoint.newBuilder();
		
		currenttime += TIME_INCREMENT;
		measurement.setTs(currenttime);
		if (counter % MF01ErrorFrequency == 0) {
			measurement.setMf01(MF01Error);
			//System.out.println("Generating Error");
		}
		else 
			measurement.setMf01(MF01);
		
		if (counter % MF02ErrorFrequency < MF02ErrorDuration)
			measurement.setMf02(MF02Error);
		else 
			measurement.setMf02(MF02);
		
		measurement.setMf03(MF03);
		
		long mode = (counter - QUERY1OFFSET) % QUERY1PERIOD;
		boolean exception = (counter % (QUERY1PERIOD * 5)) > 4*QUERY1PERIOD ;
		
		
		if (mode == 0)
			pp04 = true;
		if (mode == 150)
			pp05 = true;
		if (mode == 300)
			pp06 = true;
		if (mode == 800)
			pp04 = false;
		if (mode == 950)
			pp05 = false;
		if ((mode == 1100 && !exception) || (mode == 1200 && exception))
			pp06 = false;
		
		measurement.setPp04(pp04);
		measurement.setPp05(pp05);
		measurement.setPp06(pp06);
		counter+=1;
		
		return measurement.buildPartial();
		
		
	}
	
}

