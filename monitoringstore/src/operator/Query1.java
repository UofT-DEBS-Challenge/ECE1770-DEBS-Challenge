package operator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Pair;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class Query1 {

	/**
	 * @param args
	 */
	public static final int OPERATOR1 = 1;
	public static final int OPERATOR2 = 2;
	public static final int OPERATOR3 = 3;
	public static final int OPERATOR4 = 4;
	public static final int OPERATOR5 = 5;
	public static final int OPERATOR6 = 6;
	public static final int OPERATOR7 = 7;
	public static final int OPERATOR8 = 8;
	public static final int OPERATOR9 = 9;
	public static final int OPERATOR10 = 10;
	public static final int OPERATOR11 = 11;
	public static final int OPERATOR12 = 12;
	public static final int OPERATOR13 = 13;
	public static final int OPERATOR14 = 14;
	public static final int OPERATOR15 = 15;



	boolean s05Edge = false;
	boolean s06Edge = false;
	boolean s07Edge = false;
	boolean s08Edge = false;
	boolean s09Edge = false;
	boolean s10Edge = false;

	long s05ts = 0;
	long s06ts = 0;
	long s07ts = 0;
	long s08ts = 0;
	long s09ts = 0;
	long s10ts = 0;

	long s58dt = 0;
	long s69dt = 0;
	long s710dt = 0;
	long s58ts = 0;
	long s69ts = 0;
	long s710ts = 0;

	long last24hMinS58 = 0;
	long last24hMinS69 =0;
	long last24hMinS710 = 0;

	ArrayList<Long> s58dtBuffer = new ArrayList<Long>();
	ArrayList<Long> s58tsBuffer = new ArrayList<Long>();

	ArrayList<Long> s69dtBuffer = new ArrayList<Long>();
	ArrayList<Long> s69tsBuffer = new ArrayList<Long>();

	ArrayList<Long> s710dtBuffer = new ArrayList<Long>();
	ArrayList<Long> s710tsBuffer = new ArrayList<Long>();

	private static final long ONE_SECOND = 1000000000;
	private static final long ONE_DAY = 3600 * 24 * ONE_SECOND;

	private boolean bm05 = false ;
	private boolean bm06 = false ;
	private boolean bm07 = false ;
	private boolean bm08 = false ;
	private boolean bm09 = false ;
	private boolean bm10 = false ;
	
	private boolean op1_init = false;
	private boolean op2_init = false;
	private boolean op3_init = false;
	
	private long op1Ts = 0;
	private long op2Ts = 0;
	private long op3Ts = 0;
	private long op1Ds = 0;
	private long op2Ds = 0;
	private long op3Ds = 0;
	
	private Queue<Pair<Long,Long>> twentyFourHoursOp1 = new LinkedList<Pair<Long,Long>>();
	private Queue<Pair<Long,Long>> twentyFourHoursOp2 = new LinkedList<Pair<Long,Long>>();
	private Queue<Pair<Long,Long>> twentyFourHoursOp3 = new LinkedList<Pair<Long,Long>>();
	
	private long ts = 0;
	
	public void evaluate(CDataPoint i_measurement)
	{
		
		if (op1_init) {		
			//TODO Implement the Input's stability check 
		}
		
		ts = i_measurement.getTs();
		
		if (bm05 != i_measurement.getBm05()) {
			op1Ts = ts;
			bm05 = !bm05;
		}
		if (bm06 != i_measurement.getBm06()) {
			op2Ts = ts;
			bm06 = !bm06;
		}
		if (bm07 != i_measurement.getBm07()) {
			op3Ts = ts;
			bm07 = !bm07;
		}
		if (bm08 != i_measurement.getBm08()) {
			op1Ds = ts-op1Ts;
			bm08 = !bm08;
			twentyFourHoursOp1.add(new Pair<Long, Long>(ts, op1Ds));
 
			while (true) {
				if (twentyFourHoursOp1.peek().getKey() + ONE_DAY < ts)
					twentyFourHoursOp1.remove();
				else 
					break;
			}
			plot(11);
		}
		if (bm09 != i_measurement.getBm09()) {
			op2Ds = ts-op2Ts;
			bm09 = !bm09;
			twentyFourHoursOp2.add(new Pair<Long, Long>(ts, op2Ds));
			while (true) {
				if (twentyFourHoursOp2.peek().getKey() + ONE_DAY < ts)
					twentyFourHoursOp2.remove();
				else 
					break;
			}
			plot(13);
		}
		if (bm10 != i_measurement.getBm10()) {
			op3Ds = ts-op3Ts;
			bm10 = !bm10;
			twentyFourHoursOp3.add(new Pair<Long, Long>(ts, op3Ds));

			while (true) {
				if (twentyFourHoursOp3.peek().getKey() + ONE_DAY < ts)
					twentyFourHoursOp3.remove();
				else 
					break;
			}
			plot(15);
		}

	}


	private void plot(int opCode) {
		
		Queue<Pair<Long,Long>> oneDay = new LinkedList<Pair<Long,Long>>();
		if (opCode == 11) {
			oneDay = twentyFourHoursOp1;
		} else if (opCode == 13) {
			oneDay = twentyFourHoursOp2;
		} else if (opCode == 15) {
			oneDay = twentyFourHoursOp3;
		}
		
		long min = Long.MAX_VALUE;
		long tmpTs = 0;
		long tmpDelta = 0;
		SimpleRegression sr = new SimpleRegression();
		
		Iterator<Pair<Long, Long>> events = oneDay.iterator();
		while (events.hasNext()) {
			tmpDelta = events.next().getValue();
			tmpTs = events.next().getKey();
			events.remove();
			
			min = tmpDelta < min ? tmpDelta : min;
			sr.addData(tmpTs, tmpDelta);
		}
		if (tmpDelta > min * 1.01 )
			outputAlarm(opCode);
		outputPlot(sr.getSlope(),sr.getIntercept(), opCode);
	}


	private void outputAlarm(int opCode) {
		System.out.println(" Aakash where are you");
		
	}


	private void outputPlot(double slope, double intercept, int opCode) {
		System.out.println(" Aakash where are you");
		
	}


}
