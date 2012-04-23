/**
Copyright (c) 2011, Zbigniew Jerzak

All rights reserved.
 **/

package debs.challenge;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import java.util.Timer;
import java.util.TimerTask;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class CSimpleServerManufacturing extends SimpleChannelHandler {
	public class EachMinute extends TimerTask {

		@Override
		public void run() {
			// Methods to be executed each minute

		}

	}

	public class EachDay extends TimerTask {

		@Override
		public void run() {
			// Methods to be executed each 24 hours

		}

	}

	public class EachHour extends TimerTask {

		@Override
		public void run() {
			// Methods to be executed each hour

		}

	}


	private long i = 0;
	private long modulo = 1;

	private Logger logger = Logger.getLogger(CSimpleServerManufacturing.class
			.getCanonicalName());

	public CSimpleServerManufacturing(final InetSocketAddress sa, final long mod) {
		modulo = mod;
		new CTCPPacketReceiver(sa, this, CDataPoint.getDefaultInstance());
		logger.info("Started server @ "+sa.getAddress()+":" + sa.getPort() + ". Printing every " + modulo + " message." );
		
		class EachSecond extends TimerTask {
		    public void run() {
		        //Methods to be executed each second//
			System.out.println("");      
		    } 
		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CSimpleServerManufacturing(new InetSocketAddress(args[0], Integer.parseInt(args[1])), Long.parseLong(args[2]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelBound(org.jboss.netty
	 * .channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Channel bound: "
				+ ((InetSocketAddress) e.getValue()).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Client connected: " + ctx.getChannel().getRemoteAddress()
				+ " :> " + ctx.getChannel().getLocalAddress());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.
	 * jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		logger.info("Client disconnected: "
				+ ctx.getChannel().getRemoteAddress() + " :> "
				+ ctx.getChannel().getLocalAddress());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		
		Timer timersec = new Timer();
		timersec.schedule(new CSimpleServerManufacturing.EachMinute(),60*1000);
		
		Timer timermin = new Timer();
		timermin.schedule(new CSimpleServerManufacturing.EachHour(), 3600*1000);
		
		Timer timerhour = new Timer();
		timerhour.schedule(new CSimpleServerManufacturing.EachDay(),(3600*24*1000));	
		
		if (++i % modulo == 0) {
			CDataPoint measurement = (CDataPoint) e.getMessage();
			
			/*Navneets custom defined method here */
			
			/*logger.info("CFullMeasurement: "
					+ measurement.toString().replaceAll("\n", "; ")); */
			/*		logger.info("CFullMeasurement: "
			+ measurement.getMf01());*/    			
			
						}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("Exception caught: " + e.toString());
		ctx.getChannel().close();
	}
}