package com.mirlab.component;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.global.Global;
import com.mirlab.lib.OpenFlow13_PacketMaker;
import com.mirlab.lib.Result;
import com.mirlab.openflow.OpenFlowClient;

public class Node {
	public static Logger logger = LoggerFactory.getLogger(Node.class);

	private OpenFlowClient client;

	private long dpid;
	private int nodeId;

	private LinkedList<Port> portList = new LinkedList<Port>();

	private ChannelFuture channelFuture;
	private Table[] tables;

	private Node nextNode = null;
	private long iniTime = 0;
	private boolean hasReceivedHello = false;

	private int numberOflldpPacket = 0;
	private int numberOfFlowEntry = 0;
	private ArrayList<Long> LLDP_OUT = new ArrayList<Long>(); // currentTimeMillis() 추가
	private ArrayList<Long> LLDP_IN = new ArrayList<Long>(); // currentTimeMillis() 추가
	private ClientBootstrap bootstrap;
	long tempTime = 0;

	public Node(long dpid, int nodeId) {
		this.dpid = dpid;
		this.nodeId = nodeId;

	}

	public void start_OpenFlowClient() {
		client = new OpenFlowClient(this, Global.SDN_CONTROLLER_IP[0], Global.SDN_CONTROLLER_PORT);
		client.start();
	}

	// bug
	public void close_OpenFlowClient() {
		client.close();
	}

	public ClientBootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(ClientBootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public ArrayList<Long> getLLDP_OUT() {
		return LLDP_OUT;
	}

	public void setLLDP_OUT(ArrayList<Long> lLDP_OUT) {
		LLDP_OUT = lLDP_OUT;
	}

	public ArrayList<Long> getLLDP_IN() {
		return LLDP_IN;
	}

	public void setLLDP_IN(ArrayList<Long> lLDP_IN) {
		LLDP_IN = lLDP_IN;
	}

	public int getNumberOfFlowEntry() {
		return numberOfFlowEntry;
	}

	public void numberOfFlowEntryPlusOne() {
		this.numberOfFlowEntry = this.numberOfFlowEntry + 1;
	}

	public int getNumberOflldpPacket() {
		return numberOflldpPacket;
	}

	public void numberOflldpPacketPlusOne() {
		this.numberOflldpPacket = this.numberOflldpPacket + 1;
	}

	public boolean isHasReceivedHello() {
		return hasReceivedHello;
	}

	public void setHasReceivedHello(boolean hasReceivedHello) {
		this.hasReceivedHello = hasReceivedHello;
	}

	public long getIniTime() {
		return iniTime;
	}

	public void setIniTime(long iniTime) {
		this.iniTime = iniTime;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
		for (int i = 0; i < portList.size(); i++) {
			portList.get(i).setChannelFuture(channelFuture);
		}
	}

	public Table[] getTables() {
		return tables;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public void setTables(Table[] tables) {
		this.tables = tables;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public void createPort(int numberOfPort) {// create port
		for (int i = 0; i < numberOfPort; i++) {
			String name = "Eth" + (i + 1);
			OFPortDesc temp = OpenFlow13_PacketMaker.PORT_DESC(name, i + 1);
			Port port = new Port(temp, i + 1, this); // Port class，component 디렉토리 아래
			portList.add(port);
		}
	}

	public Port addPort() {// 포트 추가
		String name = "Eth" + (this.portList.size() + 1);
		OFPortDesc temp = OpenFlow13_PacketMaker.PORT_DESC(name, this.portList.size() + 1);

		Port port = new Port(temp, (this.portList.size() + 1), this);
		// TODO: 왜 여기서 0번 포트의 컨트롤러와의 tcp channel을 추가 포트에 할당하지?
		if ((this.portList.size() + 1) >= 2) {
			port.setChannelFuture(this.portList.get(0).getChannelFuture());
		}
		portList.add(port);

		return port;
	}

	public LinkedList<Port> getPortList() {
		return portList;
	}

	public void setPortList(LinkedList<Port> portList) {
		this.portList = portList;
	}

	public long getDpid() {
		return dpid;
	}

	public void setDpid(long dpid) {
		this.dpid = dpid;
	}

	public void sendPacketIn(OFMessage ofpi) {
		ChannelFuture nodeChannelF = this.getChannelFuture();

		if (nodeChannelF.getChannel().isWritable()) {

			nodeChannelF.getChannel().write(ofpi);

		} else {
			logger.debug("Channel is not writable!");
		}

	}

	public void sendPacket(OFMessage ofpi) {
		ChannelFuture nodeChannelF = this.getChannelFuture();

		if (nodeChannelF.getChannel().isWritable() && nodeChannelF.getChannel().isOpen()) {

			nodeChannelF.getChannel().write(ofpi);

		} else {
			logger.debug("Channel is not writable!");
		}

	}

	public void sendPacket(ArrayList<OFMessage> buff) {

		for (int i = 0; i < buff.size(); i++) {

			ChannelFuture nodeChannelF = this.getChannelFuture();

			if (nodeChannelF.getChannel().isWritable()) {

				nodeChannelF.getChannel().write(buff.get(i));
			} else {
				logger.debug("Channel is not writable!");
				i = i - 1;

			}
		}

	}

	public void sendUnknownPacketThroughput_fms(ArrayList<OFMessage> buff) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		int times = 0;
		int numberOfFlowMod = 0;
		int timeOutTime = Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP;
		n: while (true) {
			for (int i = 0; i < Global.BUFF_SIZE; i++) {

				if (numberOfFlowMod != Global.ROOTNODE.getNumberOfFlowEntry()) {
					if (times >= 4) {
						break n;
					}
					timeOutTime = timeOutTime * Global.MULTIPLE;
					times++;
					try {
						Thread.sleep(timeOutTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					if (nodeChannelF.getChannel().isWritable()) {

						nodeChannelF.getChannel().write(buff.get(i));
						numberOfFlowMod++;

						if (times == 0) {
							Global.COUNT_METRIC_10[0]++;
						} else if (times == 1) {
							Global.COUNT_METRIC_10[1]++;
						} else if (times == 2) {
							Global.COUNT_METRIC_10[2]++;
						} else if (times == 3) {
							Global.COUNT_METRIC_10[3]++;
						}

						times = 0;

						timeOutTime = Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP;
						try {
							Thread.sleep(timeOutTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						logger.debug("Channel is not writable!");
					}

				}

			}
		}
	}

	public void sendPacket_sleep(ArrayList<OFMessage> buff, int start, int end) {

		for (; start < buff.size() - 2;) {
			ChannelFuture nodeChannelF = this.getChannelFuture();

			if (nodeChannelF.getChannel().isWritable()) {

				nodeChannelF.getChannel().write(buff.get(start++));

			} else {
				logger.debug("Channel is not writable!");
			}

		}

	}

	public void sendUnknownPacketThroughput_fms_new_arp(ArrayList<OFMessage> buf) {
		int dialogResult = JOptionPane.CANCEL_OPTION;
		Object[] options = { "Continue", "Return result", "Remind me later" };
		long iniTime = System.currentTimeMillis();

		ChannelFuture nodeChannelF = this.getChannelFuture();
		try {

			int timeoutTime = 0;
			int k = 0;
			n: while (true) {

				while (k <= (Global.BUFF_SIZE * 2 - 10000)) {
					if (System.currentTimeMillis() - iniTime > (long) 1800000
							&& dialogResult == JOptionPane.CANCEL_OPTION) {
						dialogResult = JOptionPane.showOptionDialog(null,
								"Would you like to continue the test? \n Cost Time: "
										+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000)
										+ " s\n Current Flow Number: " + Result.TOTAL_NUMBER_OF_FLOW_MOD,
								"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
								options, options[2]);

						if (dialogResult == JOptionPane.YES_OPTION) {

						} else if (dialogResult == JOptionPane.NO_OPTION) {
							break n;
						} else {
							iniTime = System.currentTimeMillis();
						}

					}

					for (int i = 0; i < 10000; i++) {
						if (nodeChannelF.getChannel().isWritable()) {

							nodeChannelF.getChannel().write(buf.get(k++));
							Thread.sleep(3);
							nodeChannelF.getChannel().write(buf.get(k++));

						} else {
							logger.debug("Channel is not writable!");
						}

					}

					while (true) {

						logger.info(Result.TEMP_NUMBER_OF_FLOW_MOD + "");
						if (Result.TEMP_NUMBER_OF_FLOW_MOD >= 8000) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							Result.TEMP_NUMBER_OF_FLOW_MOD = 0;
							timeoutTime = 0;
							break;

						} else if (timeoutTime == 2) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							break n;
						} else if (timeoutTime == 1) {
							timeoutTime = 2;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP * 10);
						} else if (timeoutTime == 0) {
							timeoutTime = 1;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP);
						}

					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUnknownPacketThroughput_fms_new(ArrayList<OFMessage> buf) {
		int dialogResult = JOptionPane.CANCEL_OPTION;
		Object[] options = { "Continue", "Return result", "Remind me later" };
		long iniTime = System.currentTimeMillis();

		ChannelFuture nodeChannelF = this.getChannelFuture();
		try {

			int timeoutTime = 0;
			int k = 0;
			n: while (true) {

				while (k <= (Global.BUFF_SIZE - 10000)) {
					if (System.currentTimeMillis() - iniTime > (long) 1800000
							&& dialogResult == JOptionPane.CANCEL_OPTION) {
						dialogResult = JOptionPane.showOptionDialog(null,
								"Would you like to continue the test? \n Cost Time: "
										+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000)
										+ " s\n Current Flow Number: " + Result.TOTAL_NUMBER_OF_FLOW_MOD,
								"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
								options, options[2]);

						if (dialogResult == JOptionPane.YES_OPTION) {

						} else if (dialogResult == JOptionPane.NO_OPTION) {
							break n;
						} else {
							iniTime = System.currentTimeMillis();
						}

					}

					for (int i = 0; i < 10000; i++) {
						if (nodeChannelF.getChannel().isWritable()) {

							nodeChannelF.getChannel().write(buf.get(k++));
							Thread.sleep(0, 333333);

						} else {
							logger.debug("Channel is not writable!");
						}

					}

					while (true) {
						System.out.println("##### K CHECK ::: " + k);
						System.out.println("##### time out check  ::: "+timeoutTime);
						logger.info(Result.TEMP_NUMBER_OF_FLOW_MOD + "");
						if (Result.TEMP_NUMBER_OF_FLOW_MOD >= 8000) {
							System.out.println("##### FLOW MOD count CHECK  ::: "+Result.TOTAL_NUMBER_OF_FLOW_MOD);

							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							Result.TEMP_NUMBER_OF_FLOW_MOD = 0;
							timeoutTime = 0;
							break;

						} else if (timeoutTime == 2) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							break n;
						} else if (timeoutTime == 1) {
							timeoutTime = 2;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP * 10);
						} else if (timeoutTime == 0) {
							timeoutTime = 1;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP);
						}

					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
