package operator;

import java.util.ArrayList;

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

	boolean m_sbm05 = false ;
	boolean m_sbm06 = false ;
	boolean m_sbm07 = false ;
	boolean m_sbm08 = false ;
	boolean m_sbm09 = false ;
	boolean m_sbm10 = false ;

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


	public void evaluate(CDataPoint i_measurement)
	{

		//operator 1
		firstLevelOperator(i_measurement,m_sbm05,s05Edge,OPERATOR1);
		m_sbm05 = i_measurement.getBm05();

		//operator 2
		firstLevelOperator(i_measurement,m_sbm06,s06Edge,OPERATOR2);
		m_sbm06 = i_measurement.getBm06();

		//operator 3
		firstLevelOperator(i_measurement,m_sbm07,s07Edge,OPERATOR3);
		m_sbm07 = i_measurement.getBm07();

		//operator 4
		firstLevelOperator(i_measurement,m_sbm08,s08Edge,OPERATOR4);
		m_sbm08 = i_measurement.getBm08();

		//operator 5
		firstLevelOperator(i_measurement,m_sbm09,s09Edge,OPERATOR5);
		m_sbm09 = i_measurement.getBm09();


		//operator 6
		firstLevelOperator(i_measurement,m_sbm10,s10Edge,OPERATOR6);
		m_sbm10 = i_measurement.getBm10();

		//operator 7
		secondLevelOperator(s05Edge,s08Edge,OPERATOR7);

		//operator 8
		secondLevelOperator(s06Edge,s09Edge,OPERATOR8);

		//operator 9
		secondLevelOperator(s07Edge,s10Edge,OPERATOR9);


		//operator 10
		decreaseThresholdByOnePercent(s58dt, OPERATOR10);
		//operator 12
		decreaseThresholdByOnePercent(s69dt, OPERATOR12);
		//operator 14
		decreaseThresholdByOnePercent(s710dt, OPERATOR14);


		//operator 11
		s58dtBuffer.add(s58dt);
		s58tsBuffer.add(s58ts);

		//operator 13
		s69dtBuffer.add(s69dt);
		s69tsBuffer.add(s69ts);

		//operator 15
		s710dtBuffer.add(s710dt);
		s710tsBuffer.add(s710ts);

	}


	/*
	 * For operator 1-6
	 */
	private void firstLevelOperator(CDataPoint i_measurement, boolean oldVal, boolean previous_edge, int operator)
	{
		boolean newEdge = findEdgeSequence(i_measurement.getBm05(),oldVal,previous_edge);
		if(previous_edge != newEdge)
		{
			switch(operator) 
			{
			case OPERATOR1 : s05Edge = newEdge;
			s05ts = i_measurement.getTs();
			break;
			case OPERATOR2 : s06Edge = newEdge;
			s06ts = i_measurement.getTs();
			break;
			case OPERATOR3 : s07Edge = newEdge;
			s07ts = i_measurement.getTs();
			break;
			case OPERATOR4 : s08Edge = newEdge;
			s08ts = i_measurement.getTs();
			break;
			case OPERATOR5 : s09Edge = newEdge;
			s09ts = i_measurement.getTs();
			break;
			case OPERATOR6 : s10Edge = newEdge;
			s10ts = i_measurement.getTs();
			break;

			}

		}

	}

	private void secondLevelOperator( boolean firstEdge, boolean secondEdge, int operator)
	{
		if( (firstEdge && secondEdge) || (!firstEdge && !secondEdge))
		{
			switch(operator) 
			{
			case OPERATOR7 : s58dt = s05ts - s08ts;
			s58ts = s08ts;
			break;
			case OPERATOR8 : s69dt = s06ts - s09ts;
			s69ts = s09ts;
			break;
			case OPERATOR9 : s710dt = s07ts - s10ts;
			s710ts = s10ts;
			break;
			}
		}

	}
	/*
	 * This should be called in every 24 hour by data generator
	 */

	private void thirdLevelOperator_DayBeep()
	{

		plotTrend(s58dtBuffer,s58tsBuffer) ;
		plotTrend(s69dtBuffer,s69tsBuffer) ;
		plotTrend(s710dtBuffer,s710tsBuffer) ;

	}

	/*
	 * To find the edge between two value
	 */

	private boolean findEdgeSequence(boolean newVal, boolean oldVal, boolean previous_edge)
	{
		if ( !oldVal && newVal)
			return true;
		else if ( oldVal && !newVal )
			return false;
		else
			return previous_edge;
	}


	private void decreaseThresholdByOnePercent(long newDiff, int operator) {
		switch(operator)
		{
		case OPERATOR10 : 
			
			//TODO: is it right to just minus two time stamp
			if(((newDiff - last24hMinS58)/last24hMinS58)> 0.01)
			{
				sendAlarm(s58ts);
			}
			if(newDiff<last24hMinS58)
				last24hMinS58 = newDiff;
			break;
		case OPERATOR12 : 
			if(((newDiff - last24hMinS69)/last24hMinS69)> 0.01)
			{
				sendAlarm(s69ts);
			}
			if(newDiff<last24hMinS69)
				last24hMinS69 = newDiff;
			break;
		case OPERATOR14 : 
			if(((newDiff - last24hMinS710)/last24hMinS710)> 0.01)
			{
				sendAlarm(s710ts);
			}
			if(newDiff<last24hMinS710)
				last24hMinS710 = newDiff;
			break;
		}

	}

	private void plotTrend(ArrayList<Long> diff, ArrayList<Long> timeStamp) {
		//Da vinci code

	}


	private void sendAlarm(long timestamp) {
		System.out.println("Snake" + timestamp);
		//Bingo!!! Congratulation you win. :P
		//Buzzer buzzer buzzer , mushroom mushroom
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
