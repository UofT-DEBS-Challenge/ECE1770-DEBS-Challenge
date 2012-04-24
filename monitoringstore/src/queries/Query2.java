/**
 * 
 */
package operator;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Float;
import debs.challenge.msg.CManufacturingMessages.CDataPoint;


/**
 * @author Navneet
 *
 */
public class Query2 {


	private int max_s_mf01 = 0;
	private int min_s_mf01 = 0;

	private int max_s_mf02 = 0;
	private int min_s_mf02 = 0;

	private int max_s_mf03 = 0;
	private int min_s_mf03 = 0;

	//to store partial results
	private float partial_s1_avg_mf01 =0;
	private float partial_s2_avg_mf02 =0;
	private float partial_s3_avg_mf03 =0;
	
	//to store time stamp;
	private long s1ts = 0;
	private long s2ts = 0;
	private long s3ts = 0;
	private long s5ts = 0;
	private long s6ts = 0;
	private long s7ts = 0;
	

	private Queue<Float> s5_pwrBuffer = new LinkedList<Float>();
	private Queue<Float> s6_pwrBuffer = new LinkedList<Float>();
	private Queue<Float> s7_pwrBuffer = new LinkedList<Float>();

	
	private Queue<Long> s5tsBuffer = new LinkedList<Long>();
	private Queue<Long> s6tsBuffer = new LinkedList<Long>();
	private Queue<Long> s7tsBuffer = new LinkedList<Long>();


	private Queue<Integer> smf01Buffer = new LinkedList<Integer>();
	private Queue<Integer> smf02Buffer = new LinkedList<Integer>();
	private Queue<Integer> smf03Buffer = new LinkedList<Integer>();
	private Queue<Long> stsBuffer = new LinkedList<Long>();

	//keep track of no of samples per second ( to maintain 20 second slide window, we need no of record the 20th second)
	private Queue<Integer> noOf_samples_InThisSecond = new LinkedList<Integer>();
	private int noOf_dataSamples = 0;

	private long currentTS;
	
	//private Queue<Long> s1tsBuffer = new LinkedList<Long>();
	
	//this is counter for operator 4 to track no of times to send output ( next 70 second);
	private int alarmCounter = 0;
	
	//this is counter for counting the buffer size for last 20
	private int secondsCounter = 0;





	/*
	 * Call for every data sample
	 */
	public void evaluate(CDataPoint measurement)
	{
		//measurement = i_measurement;
		operator1(measurement.getMf01(), measurement.getTs());
		operator2(measurement.getMf02(), measurement.getTs());
		operator3(measurement.getMf03(), measurement.getTs());
		//buffer timestamp
		stsBuffer.add(measurement.getTs());
		//counter for number of samples
		++noOf_dataSamples;


	}


	/*
	 * First level operator ( operator 1,2,3) and Operator 4
	 */
	public void beepEverySecond()
	{
		float s1_rng_mf01 = (max_s_mf01 - min_s_mf01)/max_s_mf01;
		float s2_rng_mf02 = (max_s_mf02 - min_s_mf02) /max_s_mf02;
		float s3_rng_mf03 = (max_s_mf03 - min_s_mf03) /max_s_mf03;

		
		//operator 4
		if( s1_rng_mf01 > 0.3 || s2_rng_mf02 > 0.3 || s3_rng_mf03 > 0.3)
		{
			//if there condition again match, reset alarm for next 70 second
			alarmCounter = 70;
			
			//TODO:- to avoid repeat , check if already published
			outputLast20Buffer();
		}

		if(alarmCounter > 0)
		{
			outputCurrentData(s1_rng_mf01, s2_rng_mf02, s3_rng_mf03,partial_s1_avg_mf01,partial_s2_avg_mf02,partial_s3_avg_mf03, currentTS );
			--alarmCounter;
		}
		
		
		// For operator 5,6,7
		//Operator 5
		float s5_pwr = (float) (208 / Math.pow( partial_s1_avg_mf01 , 1.0/3));
		//Operator 6
		float s6_pwr = (float) (208 / Math.pow( partial_s2_avg_mf02 , 1.0/3));
		//Operator 7
		float s7_pwr = (float) (208 / Math.pow( partial_s3_avg_mf03 , 1.0/3));

		s5_pwrBuffer.add(s5_pwr);
		s6_pwrBuffer.add(s6_pwr);
		s7_pwrBuffer.add(s7_pwr);
		//TODO wha is s5ts = max (s1.ts)
		//assuming its same as s1ts
		s5ts =s1ts;
		s5tsBuffer.add(s5ts);
		s6ts =s2ts;
		s6tsBuffer.add(s6ts);
		s7ts =s3ts;
		s7tsBuffer.add(s7ts);
		
		
		//track the no of dataSamples in this second ( to remove those many record while sliding)
		noOf_samples_InThisSecond.add(noOf_dataSamples);
		
		//track no of seconds to track last 20 seconds
		++secondsCounter;
		
		resetEverySecond();

	}



	/*
	 * second level operator (operator 4, 5, 6)
	 */
	public void beepEveryMinute()
	{

		sendOp5ToGUI(s5_pwrBuffer,s5tsBuffer);
		sendOp5ToGUI(s6_pwrBuffer,s6tsBuffer);
		sendOp5ToGUI(s7_pwrBuffer,s7tsBuffer);
		
		resetEveryMinute();
	}

	
	private void outputLast20Buffer() {
		 sendOp4ToGUILast20(smf01Buffer,smf02Buffer,smf02Buffer, stsBuffer);

		//resetEveryMinute();
	}

	private void resetEverySecond() {
		max_s_mf01 = 0;
		min_s_mf01 = 0;

		max_s_mf02 = 0;
		min_s_mf02 = 0;

		max_s_mf03 = 0;
		min_s_mf03 = 0;

		partial_s1_avg_mf01 =0;
		partial_s2_avg_mf02 =0;
		partial_s3_avg_mf03 =0;
		
		noOf_dataSamples =0;
		 
		
		
		// to ensure 20 second buffer,  and clear the last 20th data
		if(secondsCounter > 20)
		{

			
			int size = noOf_samples_InThisSecond.remove();
			while ( size > 0 )
			{
				smf01Buffer.remove();
				smf02Buffer.remove();
				smf03Buffer.remove();
				stsBuffer.remove();
				--size; 
			}
			--secondsCounter;
		}


	}

	private  void resetEveryMinute() {
		

		
		s5_pwrBuffer.remove();
		s6_pwrBuffer.remove();
		s7_pwrBuffer.remove();

		s5tsBuffer.clear();
		s6tsBuffer.clear();
		s7tsBuffer.clear();
	}

	private void operator1(int val, long ts) {

		partial_s1_avg_mf01 = (partial_s1_avg_mf01 + val)/2; 

		max_s_mf01 = max_s_mf01 < val ? max_s_mf01 : val ;
		min_s_mf01 = (min_s_mf01 > val) ? val :min_s_mf01;

		//TODO:- is it correct to compare timestamp in this function
		//store max timestamp
		s1ts = s1ts < ts ? ts : s1ts;
		//buffer each value ( in the beepEvery20Second sliding window be maintained) 
		smf01Buffer.add(val);

	}

	private void operator2(int val, long ts) {

		partial_s2_avg_mf02 = (partial_s2_avg_mf02 + val)/2; 

		max_s_mf02 = max_s_mf02 < val ? max_s_mf02 : val ;
		min_s_mf02 = (min_s_mf02 > val) ? val :min_s_mf02;

		//TODO:- is it correct to compare timestamp in this function
		//store max timestamp
		s2ts = s2ts < ts ? ts : s2ts;
		//buffer each value ( in the beepEvery20Second sliding window be maintained) 
		smf02Buffer.add(val);

	}
	private void operator3(int val, long ts) {

		partial_s3_avg_mf03 = (partial_s3_avg_mf03 + val)/2; 

		max_s_mf03 = max_s_mf03 < val ? max_s_mf03 : val ;
		min_s_mf03 = (min_s_mf03 > val) ? val :min_s_mf03;

		//TODO:- is it correct to compare timestamp in this function
		//store max timestamp
		s3ts = s3ts < ts ? ts : s3ts;

		//buffer each value ( in the beepEvery20Second sliding window be maintained) 
		smf03Buffer.add(val);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
