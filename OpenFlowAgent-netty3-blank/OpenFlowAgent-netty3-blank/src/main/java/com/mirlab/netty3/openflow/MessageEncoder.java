package com.mirlab.netty3.openflow;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.projectfloodlight.openflow.protocol.OFMessage;

public class MessageEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

		if (msg instanceof OFMessage) {
			OFMessage ofMessage = (OFMessage) msg;
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
			ofMessage.writeTo(buffer);
			return buffer;
		} else if (msg instanceof List) {
			List<?> messageList = (List<?>) msg;
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
			for (Object ofMessage : messageList) {
				if (!(ofMessage instanceof OFMessage)) {
					continue;
				}
				((OFMessage) ofMessage).writeTo(buffer);
			}
			return buffer;
		} else {
			return msg;
		}

	}

}
