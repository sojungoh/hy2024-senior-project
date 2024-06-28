package com.mirlab.lib;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.projectfloodlight.openflow.protocol.OFBadRequestCode;
import org.projectfloodlight.openflow.protocol.OFBarrierReply;
import org.projectfloodlight.openflow.protocol.OFCapabilities;
import org.projectfloodlight.openflow.protocol.OFControllerRole;
import org.projectfloodlight.openflow.protocol.OFEchoReply;
import org.projectfloodlight.openflow.protocol.OFFeaturesReply;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFGetConfigReply;
import org.projectfloodlight.openflow.protocol.OFHello;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortDescStatsReply;
import org.projectfloodlight.openflow.protocol.OFPortStatsEntry;
import org.projectfloodlight.openflow.protocol.OFPortStatsReply;
import org.projectfloodlight.openflow.protocol.OFRoleReply;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.protocol.match.MatchFields;
import org.projectfloodlight.openflow.protocol.oxm.OFOxm;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFErrorCauseData;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.U64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.mirlab.component.Node;
import com.mirlab.enumType.Controller;
import com.mirlab.enumType.SNboundMetric;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;
import com.mirlab.global.Global;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingRate;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingTime;
import com.mirlab.metric.southbound.ForwardingTableCapacityNrp;
import com.mirlab.metric.southbound.ReactivePathProvisioningRate;
import com.mirlab.metric.southbound.ReactivePathProvisioningTime;
import com.mirlab.metric.southbound.TopologyChangeDetection;
import com.mirlab.metric.wraparound.AsyncClientProactiveProTimeThread_WR;
import com.mirlab.metric.wraparound.AsyncProactiveProvisioningTimeWR;

import ch.qos.logback.core.net.SyslogOutputStream;

public class PacketHandler {
	public static Logger logger = LoggerFactory.getLogger(PacketHandler.class);
	public static int COUNT = 0;
	public static int flowCount = 0;
	public static int wrflowCount = 0;
	public static int FTCconunt = 0;

	public static void HANDEL_HELLO_IN_PACKET(ChannelHandlerContext ctx) {

		OFHello hello = Global.FACTORY.buildHello().build();
		SEND_MSG(ctx, hello);
		// ctx.getChannel().write(hello);

	}

	public static void HANDEL_HELLO_OUT_PACKET(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		if (Global.southboundMetric == SouthboundMetric.CONTROL_SESSION_CAPACITY_CCD) {
			node.setHasReceivedHello(true);
		}

	}

	public static void HANDEL_ECHO_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {

		OFEchoReply echoReply = Global.FACTORY.buildEchoReply().setXid(ofMessage.getXid()).build();
		SEND_MSG(ctx, echoReply);
		// ctx.getChannel().write(echoReply);
	}

	public static void HANDEL_FEATURES_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		OFFeaturesReply featuresReply = Global.FACTORY.buildFeaturesReply().setXid(ofMessage.getXid())
				.setDatapathId(DatapathId.of(node.getDpid())).setNBuffers((long) 256).setNTables((short) 254)
				.setCapabilities(EnumSet.<OFCapabilities>of(OFCapabilities.FLOW_STATS, OFCapabilities.TABLE_STATS,
						OFCapabilities.PORT_STATS, OFCapabilities.QUEUE_STATS))
				.build();

		// ctx.getChannel().write(featuresReply);
		SEND_MSG(ctx, featuresReply);
	}

	public static void HANDEL_ROLE_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFRoleReply roleReply = Global.FACTORY.buildRoleReply().setXid(ofMessage.getXid())
				.setRole(OFControllerRole.ROLE_MASTER).setGenerationId(U64.ZERO).build();
		// ctx.getChannel().write(roleReply);
		SEND_MSG(ctx, roleReply);

	}

	public static void HANDEL_BARRIER_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFBarrierReply barrierReply = Global.FACTORY.buildBarrierReply().setXid(ofMessage.getXid()).build();
		// ctx.getChannel().write(barrierReply);
		SEND_MSG(ctx, barrierReply);
	}

	public static void HANDEL_GET_CONFIG_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFGetConfigReply getConfigReply = Global.FACTORY.buildGetConfigReply().setXid(ofMessage.getXid())
				.setMissSendLen(65535).build();
		SEND_MSG(ctx, getConfigReply);
		// ctx.getChannel().write(getConfigReply);
	}
	
	public static void setFTCcount() {
		
		FTCconunt = 0; 
		
	}

	public static void HANDEL_PACKET_OUT(OFMessage ofMessage, final Node node) {

		OFPacketOut packetOut = (OFPacketOut) ofMessage;
		byte[] data = packetOut.getData();
		// OFPacktOut.getActions(): List<OFAction> getActions()
		String action = packetOut.getActions().get(0).toString();

		int actionPortNum = -1;

		try {

			if (action.contains("flood")) {
				/*
				 *	FLOOD: Represents flooding using the normal pipeline of the switch. Can be
				 *	used only as an output port, in general will send the packet out all standard ports, but not to the
				 *	ingress port, or ports that are in OFPPS_BLOCKED state. The switch may also use the packet VLAN ID
				 *	to select which ports to flood.
				 */
				logger.debug("flood msg: " + packetOut);
			} else {

				actionPortNum = Integer.parseInt(action.substring(action.indexOf("=") + 1, action.indexOf(","))) - 1;

				if (node.getPortList().get(actionPortNum).getConnectedPort() != null) {
					// this port connects with another node's port
					if (node.getPortList().get(actionPortNum).getPortState() == 0 // 0:up 1:down
							&& node.getPortList().get(actionPortNum).getConnectedPort().getPortState() == 0
							&& node.getPortList().get(actionPortNum).getLinkState() == 0
							&& node.getPortList().get(actionPortNum).getConnectedPort().getLinkState() == 0) {

						OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(
								node.getPortList().get(actionPortNum).getConnectedPort(), data);

						node.getPortList().get(actionPortNum).getConnectedPort().getBelong2Node().sendPacketIn(ofpi);
					}

				} else if (node.getPortList().get(actionPortNum).getConnectedHostList() != null) {
					// this port connects with host or hosts

					if (Global.UNKNOWN_PACKET_TYPE != Global.ECHO_PACKET) {

						if (AsynchronousMessageProcessingTime.HAS_STARTED
								|| AsynchronousMessageProcessingRate.HAS_STARTED) {

							Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.add(System.nanoTime());

						}
					}

				} else if (node.getPortList().get(actionPortNum).getDistributedPort() != null) {
					node.getPortList().get(actionPortNum).getDistributedPort().sendByteMessage(data);
				} else {
					// this port connects nothing--error
					logger.debug("error");
				}
			}

			/*
			* if data 12-13bytes are 0x88cc,
			* it means that data type is a 802.1 Link Layer Discovery Protocol (LLDP)
			* if data 12-13bytes are 0x8942,
			* it means that data type is unknown, and it actually represents broadcast message
			*/
			if(Byte.toUnsignedInt(data[12]) == 0x88 && Byte.toUnsignedInt(data[13]) == 0xcc) {

				logger.debug("LLDP Received..Action Port is " + actionPortNum);

				if (Global.southboundMetric == SouthboundMetric.TOPOLOGY_DISCOVERY_TIME) {

					if(Tasks.HAS_STARTED) {
						// Packet Out 시간 측정
						if(node.getLLDP_OUT().size() < node.getPortList().size())
							BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_OUT()); // getLLDP_OUT: ArrayList<Long>에 현재 시간 추가

						// Packet In 시간 측정
						if(Global.topoType == TopologyType.LINEAR) {
							if(node == Global.ROOTNODE) {
								if (actionPortNum == 0 && node.getLLDP_IN().size() < node.getPortList().size() - 1)
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
							}
							else if(node == Global.LEAFNODE) {
								if(actionPortNum == 1 && node.getLLDP_IN().size() < node.getPortList().size() - 1)
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
							}
							else {
								if(node.getLLDP_IN().size() < node.getPortList().size())
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
							}
						}else if(Global.topoType == TopologyType.RING) {
							if(node == Global.ROOTNODE || node == Global.LEAFNODE) {
								if(actionPortNum == node.getPortList().getLast().getPortNum()
										&& node.getLLDP_IN().size() < node.getPortList().size() - 1) {
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
								}
							}
							else {
								if (node.getLLDP_IN().size() < node.getPortList().size())
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
							}
						}else {
							// TODO: mininet type일 때 토폴로지별 고려
							if (node.getLLDP_IN().size() < node.getPortList().size()) {
								BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
							}
						}
					}
				}

				// made by haojun 20170407
				if (Global.southboundMetric == SouthboundMetric.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP
						/*&& (Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)*/) {
					if (TopologyChangeDetection.HAS_STARTED) {

						BenchmarkTimer.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP);

						if (node == Global.ROOTNODE) {
							new Thread(() -> {
								node.sendPacket(TopologyChangeDetection.ofmSrcDown);
								node.getPortList().getLast().getConnectedPort().getBelong2Node()
										.sendPacket(TopologyChangeDetection.ofmDstDown);
								node.getPortList().getLast().setLinkState(1); // 1: down
								node.getPortList().getLast().setPortState(1); // 1: down
								node.getPortList().getLast().getConnectedPort().setLinkState(1); // 1: down
								node.getPortList().getLast().getConnectedPort().setPortState(1); // 1: down
								BenchmarkTimer
										.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS);

								try {
									if (Global.conTroller == Controller.ONOS) {
										Thread.sleep(3000);
									} else if (Global.conTroller == Controller.OPENDAYLIGHT) {
										Thread.sleep(5000);
									} else {

										Thread.sleep(15000);

									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								node.sendPacket(TopologyChangeDetection.ofmSrcUp);
								node.getPortList().getLast().getConnectedPort().getBelong2Node()
										.sendPacket(TopologyChangeDetection.ofmDstUp);
								node.getPortList().getLast().setLinkState(0);
								node.getPortList().getLast().setPortState(0);
								node.getPortList().getLast().getConnectedPort().setLinkState(0);
								node.getPortList().getLast().getConnectedPort().setPortState(0);

								BenchmarkTimer
										.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS);

								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// TopologyChangeDetection.IS_COMPLETED = true;
								TopologyChangeDetection.HAS_STARTED = false;
							}).start();
						}
					}

				} else if (Global.southboundMetric == SouthboundMetric.NETWORK_DISCOVERY_SIZE_NS
						&& (Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)) {
					if (node.getNumberOflldpPacket() < 2) {
						node.numberOflldpPacketPlusOne();
					}
				}

			} else {// asynchronous metric - 알 수 없는 lldp message
				if(Byte.toUnsignedInt(data[12]) == 0x89 && Byte.toUnsignedInt(data[13]) == 0x42)
					logger.debug("unknown type msg: " + packetOut);
				else
					logger.debug("Received Packet_out(unknown packet reply)");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\n not writeable! Nodeid: " + node.getNodeId() + "; Port Number: " + actionPortNum + 1);
		}
	}

	public static void HANDEL_STATES_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		if (ofMessage.toString().contains("OFPortDescStats")) {
			// Node Discovery end
			// Timer.ADD_CURRENT_TIME(Global.MULTIPART_REPLY_OFPMP_PORT_DESC_TIME_LIST);

			org.projectfloodlight.openflow.protocol.OFPortDescStatsReply.Builder buildPortDescStats = Global.FACTORY
					.buildPortDescStatsReply();
			List<OFPortDesc> list = new ArrayList<OFPortDesc>();

			for (int i = 0; i < node.getPortList().size(); i++) {
				list.add(node.getPortList().get(i).getPortDesc());
			}

			buildPortDescStats.setEntries(ImmutableList.<OFPortDesc>copyOf(list)).setXid(ofMessage.getXid());
			OFPortDescStatsReply portDescStatsReply = buildPortDescStats.build();

			// ctx.getChannel().write(portDescStatsReply);
			SEND_MSG(ctx, portDescStatsReply);
		} else if (ofMessage.toString().contains("OFDescStats")) {
			SEND_MSG(ctx, Global.FACTORY.buildDescStatsReply().setXid(ofMessage.getXid()).setMfrDesc("mirlab-vswitch")
					.setHwDesc("open vSwitch").setSwDesc("2.0.2").setSerialNum("1").setDpDesc("1").build());
			/*
			 * ctx.getChannel().write( Global.FACTORY.buildDescStatsReply()
			 * .setXid(ofMessage.getXid()) .setMfrDesc("mirlab-vswitch")
			 * .setHwDesc("open vSwitch").setSwDesc("2.0.2")
			 * .setSerialNum("1").setDpDesc("1").build());
			 */

		} else if (ofMessage.toString().contains("OFPortStats")) {
			org.projectfloodlight.openflow.protocol.OFPortStatsReply.Builder buildPortStats = Global.FACTORY
					.buildPortStatsReply();

			List<OFPortStatsEntry> list = new ArrayList<OFPortStatsEntry>();

			for (int i = 0; i < node.getPortList().size(); i++) {
				OFPortStatsEntry pse = Global.FACTORY.buildPortStatsEntry().setPortNo(OFPort.of(i))
						.setDurationSec((long) 153).setDurationNsec((long) 211000000).build();
				list.add(pse);
			}

			buildPortStats.setXid(ofMessage.getXid()).setEntries(ImmutableList.<OFPortStatsEntry>copyOf(list));
			OFPortStatsReply portStatsReply = buildPortStats.build();
			SEND_MSG(ctx, portStatsReply);
			// ctx.getChannel().write(portStatsReply);
		} else if (ofMessage.toString().contains("OFMeterStatsRequest")) {

			SEND_MSG(ctx, Global.FACTORY.buildMeterStatsReply().setXid(ofMessage.getXid()).build());
			// ctx.getChannel().write(Global.FACTORY.buildMeterStatsReply().setXid(ofMessage.getXid()).build());
		} else if (ofMessage.toString().contains("OFGroupDescStatsRequest")) {
			SEND_MSG(ctx, Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
					.setXid(ofMessage.getXid())
					.setData(OFErrorCauseData.of("0007000000000000".getBytes(), Global.FACTORY.getVersion())).build());
			/*
			 * ctx.getChannel().write( Global.FACTORY .errorMsgs()
			 * .buildBadRequestErrorMsg() .setCode(OFBadRequestCode.BAD_TYPE)
			 * .setXid(ofMessage.getXid()) .setData( OFErrorCauseData.of(
			 * "0007000000000000".getBytes(), Global.FACTORY.getVersion())) .build());
			 */
		} else if (ofMessage.toString().contains("OFGroupStatsRequest")) {
			SEND_MSG(ctx,
					Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
							.setXid(ofMessage.getXid()).setData(OFErrorCauseData
									.of("0006000000000000fffffffc00000000".getBytes(), Global.FACTORY.getVersion()))
							.build());
			/*
			 * ctx.getChannel().write( Global.FACTORY .errorMsgs()
			 * .buildBadRequestErrorMsg() .setCode(OFBadRequestCode.BAD_TYPE)
			 * .setXid(ofMessage.getXid()) .setData( OFErrorCauseData.of(
			 * "0006000000000000fffffffc00000000" .getBytes(), Global.FACTORY
			 * .getVersion())).build());
			 */
		} else if (ofMessage.toString().contains("OFTableFeaturesStats")) {
			SEND_MSG(ctx, Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
					.setXid(ofMessage.getXid())
					.setData(OFErrorCauseData.of("0007000000000000".getBytes(), Global.FACTORY.getVersion())).build());
		}

	}

	public static void HANDEL_FLOW_MOD(ChannelHandlerContext ctx, OFMessage ofMessage, final Node node) {
		OFFlowMod flowMod = (OFFlowMod) ofMessage;
		String action = flowMod.getActions().get(0).toString();
		if (Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_TIME
				|| Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_RATE) {

			System.out.println("###!!!!");

			if (action.contains("controller")) {

			} else {
				int actionPortNum = Integer.parseInt(action.substring(action.indexOf("=") + 1, action.indexOf(",")))
						- 1;

				if (node.getPortList().get(actionPortNum).getConnectedPort() != null) {
					// this port connects with another node's port

				} else if (node.getPortList().get(actionPortNum).getConnectedHostList() != null) {
					// this port connects with host or hosts

					if (ReactivePathProvisioningRate.HAS_STARTED || ReactivePathProvisioningTime.HAS_STARTED) {

						if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
							if (COUNT == 1) {
								BenchmarkTimer.ADD_CURRENT_TIME(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD);
								COUNT = 0;
							}
							COUNT++;

						} else if (Global.PROVISINIONING_PACKET_TYPE == Global.UDP_PACKET
								|| Global.PROVISINIONING_PACKET_TYPE == Global.TCP_PACKET) {
							// Southbound - udp tcp

							BenchmarkTimer.ADD_CURRENT_TIME(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD);

						} else {
							System.out.println("unknown packet type");
						}
					}

				} else {
					// this port connects nothing--error
					logger.debug("error");
				}

			}

		} else if (Global.southboundMetric == SouthboundMetric.FORWARDING_TABLE_CAPACITY_NRP) {
			if (action.contains("controller")) {

			} else {
				if (ForwardingTableCapacityNrp.HAS_STARTED) {
					Result.TEMP_NUMBER_OF_FLOW_MOD++;
				}
			}
		}

		// wrap around made by gilho 190101  
		int flowmodcount = 0; 
		
		if (Global.snboundMetric == SNboundMetric.PROACTIVE_PATH_PROVISIONING) {

			// time measure
			List<Iterable<MatchField<?>>> ofMatchFields = Arrays.asList(flowMod.getMatch().getMatchFields());
			
//			flowmodcount++;
//			System.out.println("###  check FLOW MOD COUNT : " +flowmodcount );
			
				
			String matchS = flowMod.getMatch().toString();
			System.out.println("### check match fields :  " + matchS);
			if (matchS.length() > 70) {
				

				String dstKey = String.valueOf(matchS.charAt(44))+String.valueOf(matchS.charAt(46))+String.valueOf(matchS.charAt(47))+String.valueOf(matchS.charAt(49))+String.valueOf(matchS.charAt(50));
				long lastTime = System.nanoTime()/1000000;
				
				AsyncClientProactiveProTimeThread_WR.setGlobalLastMap(dstKey,lastTime);

//				System.out.println("### (Packet Handler)  key     : "   + dstKey);
				
				
			}

		} 
		
		//**FORWARDING_TABLE_CAPACITY
		if(Global.snboundMetric == SNboundMetric.FORWARDING_TABLE_CAPACITY) {
			FTCconunt++;
			
		}
		
	}

	public static void SEND_MSG(ChannelHandlerContext ctx, OFMessage ofMessage) {
		if (ctx.getChannel().isOpen()) {
			ctx.getChannel().write(ofMessage);

		} else {
			logger.debug("Channel is not writable!");
		}
	}

	public static void ECHO_REPLY(ChannelHandlerContext ctx, OFMessage ofMessage) {
		if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET
				&& (Global.southboundMetric == SouthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME
						|| Global.southboundMetric == SouthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE)) {
			if (AsynchronousMessageProcessingRate.HAS_STARTED || AsynchronousMessageProcessingTime.HAS_STARTED) {
				Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.add(System.nanoTime());
			}

		} else {

		}

	}
}
