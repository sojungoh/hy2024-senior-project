package com.mirlab.openflow;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.component.Node;
import com.mirlab.lib.PacketHandler;

public class BaseHandler extends IdleStateAwareChannelHandler {
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		super.handleUpstream(ctx, e);

	}

	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

	private Node node;

	public BaseHandler(Node node) {
		super();
		this.node = node;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel connected.");
		PacketHandler.HANDEL_HELLO_IN_PACKET(ctx);// 和controller连接之后发送hello
													// message
		// Timer.ADD_CURRENT_TIME(Global.HELLO_TIME_LIST);

	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel closed.");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		OFMessage ofMessage = (OFMessage) event.getMessage();
		switch (ofMessage.getType()) {
		case HELLO:
			// 收到来自controller的hello mmessage之后
			logger.debug("Hello message Received.");
			PacketHandler.HANDEL_HELLO_OUT_PACKET(ctx, ofMessage, node);
			break;
		case ECHO_REQUEST:
			logger.debug("Echo Request message Received.");
			PacketHandler.HANDEL_ECHO_REQUEST(ctx, ofMessage);
			break;
		case ECHO_REPLY:
			logger.debug("Echo reply message Received.");
			PacketHandler.ECHO_REPLY(ctx, ofMessage);
			break;
		case FEATURES_REQUEST:
			logger.debug("Features Request message Received.");
			PacketHandler.HANDEL_FEATURES_REQUEST(ctx, ofMessage, node);
			break;
		case STATS_REQUEST:
			logger.debug("Multipart Request message Received. ");
			logger.debug(ofMessage.toString());
			PacketHandler.HANDEL_STATES_REQUEST(ctx, ofMessage, node);
			break;
		case ROLE_REQUEST:
			logger.debug("Flowmod message Received.");
			PacketHandler.HANDEL_ROLE_REQUEST(ctx, ofMessage);
			break;
		case FLOW_MOD:
			logger.debug("Flowmod message Received.");
			PacketHandler.HANDEL_FLOW_MOD(ctx, ofMessage, node);
			// proceedFlowmod(ofMessage);
			break;
		case BARRIER_REQUEST:
			logger.debug("BARRIER_REQUEST message Received.");
			PacketHandler.HANDEL_BARRIER_REQUEST(ctx, ofMessage);
			break;
		case GET_CONFIG_REQUEST:
			logger.debug("GET_CONFIG_REQUEST message Received.");
			PacketHandler.HANDEL_GET_CONFIG_REQUEST(ctx, ofMessage);
			break;
		case PACKET_OUT:

			logger.debug("PACKET_OUT message Received.");
			logger.debug(ofMessage.toString());
			PacketHandler.HANDEL_PACKET_OUT(ofMessage, node);
			break;

		default:
			break;
		}
	}

}
