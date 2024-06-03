package com.mirlab.metric.southbound;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jboss.netty.channel.ChannelFuture;
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
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年7月20日 下午3:37:31 类说明
 */
public class ReactivePathProvisioningTime {
	public static boolean HAS_STARTED = false;

	public static DecimalFormat df = new DecimalFormat("#0.00000");

	public static Logger logger = LoggerFactory.getLogger(Node.class);

	public static void go() {
		version0();
		Log.exportLog(ReactivePathProvisioningTime.class.getSimpleName(), 5);
	}

	private static void version0() {
		// TODO Auto-generated method stub

		try {
			initi();

			/* TODO: 이 정도만 쉬면 TCP connection 전부 완료? */
			Thread.sleep(20000);

			start_task();
			result_check();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HAS_STARTED = false;
		}

	}

	private static void initi() {
		CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
		ct.go();
	}

	private static void start_task() {
		// TODO Auto-generated method stub

		try {

			ArrayList<OFMessage> srctemp = new ArrayList<>();
			ArrayList<OFMessage> dsttemp = new ArrayList<>();

			for (int i = 0; i < Global.NODE_SIZE; i++) {

				Host srcHost = Global.srcHosts[i];
				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, srcHost, srcHost);
				OFMessage srcofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket);
				srctemp.add(srcofpi);

				Host dstHost = Global.dstHosts[i];
				byte tempPacket1[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, dstHost,
						dstHost);
				OFMessage dstofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket1);
				dsttemp.add(dstofpi);

			}

			Global.srcHosts[0].getConnectedNode().sendPacket(srctemp);
			Thread.sleep(2000);
			Global.dstHosts[0].getConnectedNode().sendPacket(dsttemp);

			Thread.sleep(20000);
			
			Log.ADD_LOG_PANEL("Sending ARP(Gratuitous) Completed.", Initializer.class.toString());

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				
				ArrayList<OFMessage> temp1 = new ArrayList<OFMessage>();
				ArrayList<OFMessage> temp2 = new ArrayList<OFMessage>();
				
				for (int j = 0; j < Global.NODE_SIZE; j++) {
					for (int i = 0; i < Global.NODE_SIZE; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];

						byte tempPacket1[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket1);
						temp1.add(ofpi);
					}
				}
				Global.srcHosts[0].getConnectedNode().sendPacket_sleep(temp1, 0, Global.BUFF_SIZE);
				Log.ADD_LOG_PANEL("Sending ARP(Request) Completed.", Initializer.class.toString());

				for (int j = 0; j < Global.NODE_SIZE; j++) {
					for (int i = 0; i < Global.NODE_SIZE; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];
						byte tempPacket2[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket2);
						temp2.add(ofpi);
					}
				}

				Thread.sleep(5000);
				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				sendPacket(Global.dstHosts[0].getConnectedNode(), temp2);

			} else {
				// Southbound - tcp udp

				ArrayList<OFMessage> temp = new ArrayList<>();
				OFMessage ofpi = null;
				for (int i = 0; i < Global.NODE_SIZE; i++) {
					for (int j = 0; j < Global.NODE_SIZE; j++) {
						Host srcHost = Global.srcHosts[i];
						Host dstHost = Global.dstHosts[j];

						byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.PROVISINIONING_PACKET_TYPE,
								srcHost, dstHost, 1000, 1000);

						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.srcHosts[0].getBelong2Port(), tempPacket);
						temp.add(ofpi);
					}
				}

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				sendPacket(Global.srcHosts[0].getConnectedNode(), temp);
			}

			Thread.sleep(5000);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private static void result_check() {
		// TODO Auto-generated method stub
		Log.ADD_LOG_PANEL("" + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
				ReactivePathProvisioningTime.class.toString());

		if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() < 10
				|| Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size() < 10) {
			Result.ADD_RESULT("-", 5, 2);
			Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller!",
					ReactivePathProvisioningTime.class.getSimpleName());
			JOptionPane.showMessageDialog(null, "Please Check Reactive Module of SDN Controller or change packet type!",
					"Error!", JOptionPane.ERROR_MESSAGE);
		} else {
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Result.ADD_RESULT(df.format(
						BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN) / 2),
						5, 2);

				Log.ADD_LOG_PANEL("size of in: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
						ReactivePathProvisioningTime.class.getSimpleName());
				Log.ADD_LOG_PANEL("size of out: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
						ReactivePathProvisioningTime.class.getSimpleName());
				Log.ADD_LOG_PANEL(
						"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN) / 2) + " ms",
						ReactivePathProvisioningTime.class.getSimpleName());

			} else {
				Result.ADD_RESULT(df.format(
						BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN)),
						5, 2);

				Log.ADD_LOG_PANEL("size of in: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
						ReactivePathProvisioningTime.class.getSimpleName());
				Log.ADD_LOG_PANEL("size of out: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
						ReactivePathProvisioningTime.class.getSimpleName());
				Log.ADD_LOG_PANEL(
						"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
								Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN)) + " ms",
						ReactivePathProvisioningTime.class.getSimpleName());

			}
		}
		Log.ADD_LOG_PANEL("Test Completed!", ReactivePathProvisioningTime.class.getSimpleName());

		Main.gui.S_progressBarTotal.setValue(1);
		Initializer.INITIAL_CHANNEL_POOL();
	}

	private static void sendPacket(Node node, ArrayList<OFMessage> of) {
		ChannelFuture nodeChannelF = node.getChannelFuture();
		long iniTime = System.nanoTime();
		int count = 0;

		n: while (true) {

			while (count < of.size() - 3) {

				if (nodeChannelF.getChannel().isWritable()) {

					// record time
					if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
						// controller Flow_Mod 응답이 Packet_In과 1:1 대응되었을 때 다음 turn 진행
						if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD
								.size() == Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) {

							Global.count = count;
							System.out.println("### FLOW COUNT :: " + Global.count);

							// Packet_in 전송 + 시간 측정 시작
							nodeChannelF.getChannel().write((OFMessage) of.get(count++));
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(System.nanoTime());
						}
					} else {
						break n;
					}
				} else {
					logger.debug("Channel is not writable!");
				}
			}

		}
	}
}
