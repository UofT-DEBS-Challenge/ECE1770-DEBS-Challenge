package operator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Pair;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;
import debs.challenge.msg.COutputMessages.CAlarm;
import debs.challenge.msg.COutputMessages.CPlot;

public class Query1 {

	private static final long ONE_SECOND = 1000000000;
	private static final long ONE_DAY = 3600 * 24 * ONE_SECOND;

	private boolean bm05 = false;
	private boolean bm06 = false;
	private boolean bm07 = false;
	private boolean bm08 = false;
	private boolean bm09 = false;
	private boolean bm10 = false;

	private boolean op1_init = false;
	private boolean op2_init = false;
	private boolean op3_init = false;

	private long op1Ts = 0;
	private long op2Ts = 0;
	private long op3Ts = 0;
	private long op1Ds = 0;
	private long op2Ds = 0;
	private long op3Ds = 0;

	private Queue<Pair<Long, Long>> twentyFourHoursOp1 = new LinkedList<Pair<Long, Long>>();
	private Queue<Pair<Long, Long>> twentyFourHoursOp2 = new LinkedList<Pair<Long, Long>>();
	private Queue<Pair<Long, Long>> twentyFourHoursOp3 = new LinkedList<Pair<Long, Long>>();

	private long ts = 0;
	private long starttime = 0;
	
	private boolean display = false;
	private boolean store = false;
	private boolean verbose = false;

	public Query1 (boolean s, boolean d, boolean v) {
		this.store = s;
		this.display = d;
		this.verbose = v;
	}
	
	public void evaluate(CDataPoint i_measurement) {

		if (!op1_init) {
			op1_init = (i_measurement.getPp04() == i_measurement.getPp05());
			starttime = starttime == 0 ? i_measurement.getTs() : starttime;
		}
		if (!op2_init) {
			op2_init = (i_measurement.getPp05() == i_measurement.getPp06());
			starttime = starttime == 0 ? i_measurement.getTs() : starttime;
		}
		if (!op3_init) {
			op3_init = (i_measurement.getPp04() == i_measurement.getPp06());
			starttime = starttime == 0 ? i_measurement.getTs() : starttime;
		}

		ts = i_measurement.getTs();

		if (op1_init) {
			if (bm05 != i_measurement.getPp04()) {
				op1Ts = ts;
				bm05 = !bm05;
			}

			if (bm08 != i_measurement.getPp05()) {
				op1Ds = ts - op1Ts;
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
		}

		if (op2_init) {
			if (bm06 != i_measurement.getPp05()) {
				op2Ts = ts;
				bm06 = !bm06;
			}

			if (bm09 != i_measurement.getPp06()) {
				op2Ds = ts - op2Ts;
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
		}
		if (op3_init) {
			if (bm07 != i_measurement.getPp04()) {
				op3Ts = ts;
				bm07 = !bm07;

			}
			if (bm10 != i_measurement.getPp06()) {
				op3Ds = ts - op3Ts;
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

	}

	private void plot(int opCode) {

		Queue<Pair<Long, Long>> oneDay = new LinkedList<Pair<Long, Long>>();
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
		Pair<Long, Long> tmpPair = null;
		SimpleRegression sr = new SimpleRegression();

		Iterator<Pair<Long, Long>> events = oneDay.iterator();
		if (oneDay.size() > 1) {
			while (events.hasNext()) {
				tmpPair = events.next();
				tmpDelta = tmpPair.getValue();
				tmpTs = tmpPair.getKey();

				min = tmpDelta < min ? tmpDelta : min;
				sr.addData(tmpTs - starttime, tmpDelta);
			}
		}
		if (tmpDelta > min * 1.01) {
			if (store || display)
				outputAlarm(opCode);
			if (verbose)
				System.out.println("Query1 Violation. Timestamp: " + ts);
		}

		if (store || display)
			outputPlot(sr.getSlope(), sr.getIntercept(), opCode);
		if (verbose)
			System.out.println("Query1 plotting. Timestamp: " + ts + " slope: " + sr.getSlope() + " intercept: " + sr.getIntercept());
	}

	private void outputAlarm(int opCode) {
		// TODO :- Write in GPB
		try {
			// TODO file name ??
			FileOutputStream outputFile = new FileOutputStream("tempAlarm",true);
			CAlarm.Builder oAlarm = CAlarm.newBuilder();
			oAlarm.setTs(ts);
			oAlarm.setOpCode(opCode);
			oAlarm.build().writeDelimitedTo(outputFile);
			outputFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void outputPlot(double slope, double intercept, int opCode) {
		// TODO Write in GPB
		// TODO :- Write in GPB
		try {
			// TODO file name ??
			FileOutputStream outputFile = new FileOutputStream("tempPlot",true);
			CPlot.Builder oPlot = CPlot.newBuilder();
			oPlot.setTs(ts);
			oPlot.setOpCode(opCode);
			oPlot.setIntercept(intercept);
			oPlot.setSlope(slope);
			oPlot.build().writeDelimitedTo(outputFile);
			outputFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
