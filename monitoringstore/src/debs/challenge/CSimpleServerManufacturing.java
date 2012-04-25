/**
Copyright (c) 2011, Zbigniew Jerzak

All rights reserved.
 **/

package debs.challenge;

import java.net.InetSocketAddress;

import operator.Query1;
import operator.Query2;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class CSimpleServerManufacturing extends SimpleChannelHandler {

//	private long i = 0;
//	private long modulo = û1;
	private Query1 q1;
	private Query2 q2;
	
	private Logger logger = Logger.getLogger(CSimpleServerManufacturing.class
			.getCanonicalName());

	public CSimpleServerManufacturing(final InetSocketAddress sa, final long mod) {
		q1 = new Query1();
		q2 = new Query2();
//		modulo = mod;
		new CTCPPacketReceiver(sa, this, CDataPoint.getDefaultInstance());
		logger.info("Started server @ "+sa.getAddress()+":" + sa.getPort());
//		logger.info("Started server @ "+sa.getAddress()+":" + sa.getPort() + ". Printing every " + modulo + " message." );	
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
		CDataPoint measurement = (CDataPoint) e.getMessage();
		if(measurement.getBm05())
			System.out.println("Found a 1 in bm05");
		if(measurement.getBm08())
			System.out.println("Found a 1 in bm08");
//		try {
//			q1.evaluate(measurement);
//			q2.evaluate(measurement);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		if (++i % modulo == 0) {
//			
//			CDataPoint measurement = (CDataPoint) e.getMessage();
//			/*Navneets custom defined method here */
//			
//			
//			/*logger.info("CFullMeasurement: "
//					+ measurement.toString().replaceAll("\n", "; ")); */
//			/*		logger.info("CFullMeasurement: "
//			+ measurement.getMf01());*/    			
//			
//						}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("Exception caught: " + e.toString());
		ctx.getChannel().close();
	}
}