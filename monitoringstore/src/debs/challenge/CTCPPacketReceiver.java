/**
Copyright (c) 2011, Zbigniew Jerzak

All rights reserved.
**/
package debs.challenge;

import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;


import com.google.protobuf.MessageLite;

/**
 * @author D053250
 *
 */
public class CTCPPacketReceiver {

	private Logger logger = Logger.getLogger(CTCPPacketReceiver.class.getCanonicalName());
	/**
	 * 
	 */
	public CTCPPacketReceiver(SocketAddress address, final ChannelHandler handler, final MessageLite message) {
        ChannelFactory factory =
            new NioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
		
		ChannelPipelineFactory cpf = new ChannelPipelineFactory() {			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline cp = Channels.pipeline();
				
				cp.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		  		cp.addLast("protobufDecoder", new ProtobufDecoder(message.getDefaultInstanceForType()));
				cp.addLast("application", handler);
				return cp;
			}
		};
		
        bootstrap.setPipelineFactory(cpf);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(address);
        logger.info("Bound to: " + address + " for message type: " + message.getClass().getSimpleName());
	}	
}
