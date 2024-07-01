package com.mirlab.metric.southbound;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import org.jboss.netty.channel.ChannelFuture;
import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Main;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.BenchmarkTimer;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月19日 上午11:16:45 类说明
 */
public class AsynchronousMessageProcessingTime {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	public static boolean HAS_STARTED = false;
	public static DecimalFormat df = new DecimalFormat("#0.00000");

	public static void go() {
		version0();
		Log.exportLog(AsynchronousMessageProcessingTime.class.getSimpleName(), 3);
	}

	private static void version0() {
		try {
			CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
			ct.go();

			Thread.sleep(5000);

			Host src = Global.srcHosts[0];
			Host dst = Global.dstHosts[0];

			if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {

				OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				sendPacket(src.getConnectedNode(), OFEchoRequest);

			} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {

				byte srcTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
				OFMessage src_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), srcTempPacket);

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				sendPacket(src.getConnectedNode(), src_ofpi);
			} else {// tcp udp

				byte srcTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, src, dst);
				OFMessage src_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), srcTempPacket);

				byte dstTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, dst, src);
				OFMessage dst_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dst.getBelong2Port(), dstTempPacket);

				src.getConnectedNode().sendPacketIn(src_ofpi);
				dst.getConnectedNode().sendPacketIn(dst_ofpi);

				Thread.sleep(5000);

				/******************************************************************************************************/

				byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, src, dst, 1000,
						1000);// tcp port

				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), tempPacket);
				// return packet in message

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;
				
				sendPacket(src.getConnectedNode(), ofpi);

			}

			Thread.sleep(1000);

			Result.ADD_RESULT(df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
					Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
					Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)), 3, 2);
			Log.ADD_LOG_PANEL("size of in: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
					Tasks.class.toString());
			Log.ADD_LOG_PANEL("size of out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
					Tasks.class.toString());
			Log.ADD_LOG_PANEL(
					"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
							Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
							Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)) + " ms",
					Tasks.class.toString());

			Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());

//			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size() < 5
//					|| Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() < 5) {
//
//				Result.ADD_RESULT("-", 3, 2);
//				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller!", Tasks.class.toString());
//				JOptionPane.showMessageDialog(null,
//						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
//						JOptionPane.ERROR_MESSAGE);
//			} else {
//
//			}
			Main.gui.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			HAS_STARTED = false;
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			HAS_STARTED = false;
		}
	}

	private static void sendPacket(Node node, OFMessage of) {
		ChannelFuture nodeChannelF = node.getChannelFuture();
		long iniTime = System.nanoTime();

		while (true) {

			if (nodeChannelF.getChannel().isWritable()) {

				// record time
				if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
					if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN
							.size() == Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()) {

						nodeChannelF.getChannel().write((OFMessage) of);
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.add(System.nanoTime());

					}
				} else {

					break;
				}

			} else {
				logger.debug("Channel is not writable!");
			}

		}
	}
}
