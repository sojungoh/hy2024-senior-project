package com.mir.ui;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.Main;
import com.mir.distributed.message.DistributedMessage;
import com.mir.distributed.server.DistributedServer;
import com.mirlab.component.DistributedPort;
import com.mirlab.enumType.MetricType;
import com.mirlab.global.Global;
import com.mirlab.lib.IP;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Tasks;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingRate;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingTime;
import com.mirlab.metric.southbound.ForwardingTableCapacityNrp;
import com.mirlab.metric.southbound.ReactivePathProvisioningRate;
import com.mirlab.metric.southbound.ReactivePathProvisioningTime;
import com.mirlab.metric.southbound.TestTotal;
import com.mirlab.metric.southbound.TopologyChangeDetection;
import com.mirlab.metric.southbound.TopologyDiscovery;

import io.netty.channel.Channel;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月7日 下午12:48:57 类说明
 *
 * @author 오소정 E-mail: sojung3514@hanyang.ac.kr
 * @version 확장일: 2024-05-17
 */
public class S_RunButton {

	private static DistributedMessage distributedMessage = DistributedMessage.INSTANCE;

	public static void go() {

		boolean controller_isOpen = IP.isHostConnectable(Global.SDN_CONTROLLER_IP[0], Global.SDN_CONTROLLER_PORT, 1000);

		if (controller_isOpen) {// controller is open

			checkDistributed();

			startTasks();

		} else {// controller offline

			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Please check if controller is running.", "Disconnected",
					JOptionPane.ERROR_MESSAGE);// 새 창 건너뛰기

		}

	}

	private static void checkDistributed() {// 모든 변수를 JSON 형식으로

		DistributedServer distributedServer = DistributedServer.getInstance();

		if (Global.IS_ENABLE_DISTRIBUTED && Global.IS_MASTER) {

			JSONObject msgType1 = distributedMessage.msgType1();

			for (Channel channel : distributedServer.channelMap.values()) {
				channel.writeAndFlush(msgType1.toString());
			}

			// ******************************

			int listSize = Global.distribudtedTool_IP_List.size();
			int distributedPortCount = Global.distribudtedTool_PortList.length;

			String IP1 = Global.distribudtedTool_IP_List.get((listSize - 1) % listSize);

			Global.distribudtedTool_IP.add(IP1);

			int tool_port1 = Global.distribudtedTool_PortList[1 % distributedPortCount];
			Global.distribudtedTool_Port.put(IP1, tool_port1);

			int server_port1 = Global.distribudtedTool_PortList[0 % distributedPortCount];
			Global.distribudtedToolSever_Port.put(IP1, server_port1);

			DistributedPort distributedPort1 = new DistributedPort(IP1, tool_port1, server_port1);

			// *********************************************
			String IP2 = Global.distribudtedTool_IP_List.get(1 % listSize);

			Global.distribudtedTool_IP.add(IP2);

			int tool_port2 = Global.distribudtedTool_PortList[0 % distributedPortCount];
			Global.distribudtedTool_Port.put(IP2, tool_port2);

			int server_port2 = Global.distribudtedTool_PortList[1 % distributedPortCount];

			DistributedPort distributedPort2 = new DistributedPort(IP2, tool_port2, server_port2);

			Global.distribudtedToolSever_Port.put(IP2, server_port2);

			Global.distributedPortList = new ArrayList<>();

			Global.distributedPortList.add(distributedPort1);
			Global.distributedPortList.add(distributedPort2);

			// **********************************************

			for (int i = 1; i < listSize; i++) {
				System.out.println();

				String IP = Global.distribudtedTool_IP_List.get(i);

				JSONObject message = distributedMessage.msgType2(
						Global.distribudtedTool_IP_List.get((i + listSize - 1) % listSize),
						Global.distribudtedTool_PortList[1 % distributedPortCount],
						Global.distribudtedTool_PortList[0 % distributedPortCount],
						Global.distribudtedTool_IP_List.get((i + 1) % listSize),
						Global.distribudtedTool_PortList[0 % distributedPortCount],
						Global.distribudtedTool_PortList[1 % distributedPortCount]);

				System.out.println("IP:" + IP);
				System.out.println("channelMap Size : " + distributedServer.channelMap.size());

				distributedServer.channelMap.get(IP).writeAndFlush(message.toString());
				System.out.println(IP);
				System.out.println("msgType=2 send!");
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Channel channel : distributedServer.channelMap.values()) {
				channel.writeAndFlush(distributedMessage.MsgTypeRun(MetricType.SOUTHBOUND).toString());
			}
		}

	}

	public static void startTasks() {// run 버튼 클릭 시 주요 실행 코드
		// start Metric

		Main.gui.S_LOG_TEXT.setText("");
		Main.gui.S_TOPO_TEXT.setText("");

		Log.ADD_LOG("***************SDN Controller Information***************\n");
		Log.ADD_LOG("Controller Mode: " + Global.conMode + "\n");
		Log.ADD_LOG("Controller Type: " + Global.conTroller + "\n");
		Log.ADD_LOG("Ip/Port: " + Global.SDN_CONTROLLER_IP[0] + ":" + Global.SDN_CONTROLLER_PORT + "\n");

		Log.ADD_LOG("*****************Benchmark  Information*****************\n");
		Log.ADD_LOG("OpenFlow Version: "
				+ (Global.ofVersion == org.projectfloodlight.openflow.protocol.OFVersion.OF_13 ? "Openflow 1.3"
						: "please select 1.3 version.")
				+ " \n");

		switch (Global.topoType) {
		case LINEAR:
			Log.ADD_LOG("Topology Type: Linear\n");
			break;
		case RING:
			Log.ADD_LOG("Topology Type: Ring\n");
			break;
		case MININET:
			Log.ADD_LOG("Topology Type: Mininet\n");
		}

		Log.ADD_LOG("Number of Switch: " + Global.NUMBER_OF_TEST_SWITCH + " \n");
		Log.ADD_LOG("Number of Host per Switch: " + Global.NODE_SIZE + " \n");
		Log.ADD_LOG("Test Time: " + Global.TEST_TIME + "s \n");
		Log.ADD_LOG("Bulk size start from: " + Global.CURRENT_BULK_SIZE + "\n");
		Log.ADD_LOG("The packet loss threshold : " + Global.PACKET_LOSS_RATE * 100 + "%\n");

		// unknowns type
		if (Global.UNKNOWN_PACKET_TYPE == Global.TCP_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: TCP");
		} else if (Global.UNKNOWN_PACKET_TYPE == Global.UDP_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: UDP");
		} else if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: ECHO Request");
		} else {
			Log.ADD_LOG("Asynchronous Message Type: ARP Request");
		}

		// provisioning unknow type
		if (Global.PROVISINIONING_PACKET_TYPE == Global.TCP_PACKET) {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: TCP");
		} else if (Global.PROVISINIONING_PACKET_TYPE == Global.UDP_PACKET) {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: UDP");
		} else {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: ARP");
		}

		Log.ADD_LOG("\n*******************************************************\n");

		Log.ADD_LOG_PANEL("\"" + Global.southboundMetric + "\" Test Start... \n", Main.class.toString());

		Main.gui.S_TOPO_TEXT.append("Topology Type:" + Global.topoType + "\n");

		Main.gui.S_TOPO_TEXT.append("Number Of Node: " + Global.NUMBER_OF_TEST_SWITCH);

		Main.gui.S_progressBarTotal.setMaximum(1);// 진척도
		Main.gui.S_progressBarTotal.setValue(0);

		switch (Global.southboundMetric) {
		case TOPOLOGY_DISCOVERY_TIME:

			Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID = Global.NUMBER_OF_TEST_SWITCH + 1;
			Global.PROGRESS_MAX_VALUE = 26;
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);

			// 현재 시간 가져와 global 변수에 할당
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			ProgressUpdate pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			TopologyDiscovery.go();

			break;
		case TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP:

			Global.PROGRESS_MAX_VALUE = 26;
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			TopologyChangeDetection.go();

			break;

		case ASYNCHRONOUS_MESSAGE_PROCESSING_TIME:

			Global.PROGRESS_MAX_VALUE = 4 + Global.TEST_TIME;

			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			AsynchronousMessageProcessingTime.go();
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Main.gui.S_progressBarTotal.setValue(Main.gui.S_progressBarTotal.getMaximum());

			break;

		case ASYNCHRONOUS_MESSAGE_PROCESSING_RATE:

			Global.PROGRESS_MAX_VALUE = 2;

			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			AsynchronousMessageProcessingRate.go();

			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Main.gui.S_progressBarTotal.setValue(Main.gui.S_progressBarTotal.getMaximum());

			break;
		case REACTIVE_PATH_PROVISIONING_TIME:

			Global.PROGRESS_MAX_VALUE = 32 + Global.TEST_TIME; // init: 32s + 5s = 37s
			
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Global.PROGRESS_MAX_VALUE = 27 + Global.TEST_TIME;
			}
			
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			ReactivePathProvisioningTime.go();

			break;

		case REACTIVE_PATH_PROVISIONING_RATE:

			Global.PROGRESS_MAX_VALUE = 2;

			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			Log.ADD_LOG_PANEL(
					"Start to find optimizational Bulk Size. Bulk Size Start from:" + Global.CURRENT_BULK_SIZE,
					ReactivePathProvisioningRate.class.toString());

			ReactivePathProvisioningRate.go();

			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Main.gui.S_progressBarTotal.setValue(Main.gui.S_progressBarTotal.getMaximum());

			break;

		case PROACTIVE_PATH_PROVISIONING_TIME:
			Initializer.INITIAL_CHANNEL_POOL();
			// 미구현
			break;
		case PROACTIVE_PATH_PROVISIONING_RATE:
			Initializer.INITIAL_CHANNEL_POOL();
			// 미구현
			break;
		case CONTROL_SESSION_CAPACITY_CCD:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();// 1970.1.1시작 밀리초

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Tasks.START_TASK_8_NEW();

			break;
		case NETWORK_DISCOVERY_SIZE_NS:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Tasks.START_TASK_9_NEW();

			break;

		case FORWARDING_TABLE_CAPACITY_NRP:

			Global.NODE_SIZE = 1000;
			Global.BUFF_SIZE = 1000000;
			Global.PROGRESS_MAX_VALUE = 2;

			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);

			Global.TEST_INIT_TIME = System.currentTimeMillis();

			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);
			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			ForwardingTableCapacityNrp.go();

			break;

		case EXCEPTION_HANDLING_SECURITY:
		case DOS_ATTACKS_SECURITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		case CONTROLLER_FAILOVER_TIME_RELIABILITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case TEST_TOTAL:

			Global.TEST_INIT_TIME = System.currentTimeMillis();
			Main.gui.S_progressBarTotal.setMaximum(10);
			Main.gui.S_progressBarTotal.setValue(0);
			TestTotal.go();
			break;
		default:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		}

		Initializer.INITIAL_CHANNEL_POOL();

	}

}
