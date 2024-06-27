package com.mirlab.lib;

import java.util.ArrayList;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.oxm.OFOxmInPort;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;

import com.mirlab.component.Host;

import com.mirlab.component.Port;
import com.mirlab.global.Global;
import com.mirlab.protocol.ARP;
import com.mirlab.protocol.EthernetHeader;
import com.mirlab.protocol.Ip;
import com.mirlab.protocol.Tcp;
import com.mirlab.protocol.Udp;

public class PacketMaker_NEW {

	public static ArrayList<OFMessage> MAKE_ARP_REPLY_GRATUITOUS_PACKET_NEW() {
		// 모든 host 정보를 controller에게 전송
		ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
		OFMessage ofpi = null;

		for (int i = 0; i < Global.NODE_SIZE; i++) {
			Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(i);
			byte tempPacket[] = MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, srcHost, srcHost);
			ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0), tempPacket);
			temp.add(ofpi);

			srcHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(i);
			byte tempPacket1[] = MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, srcHost, srcHost);
			ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(1), tempPacket1);
			temp.add(ofpi);

		}

		return temp;
	}

	public static ArrayList<OFMessage> MAKE_ARP_REQUEST() {
		ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
		OFMessage ofpi = null;

		for (int i = 0; i < Global.NODE_SIZE; i++) {
			for (int j = 0; j < Global.NODE_SIZE; j++) {
				Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(i);
				Host dstHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(j);

				byte tempPacket[] = MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0), tempPacket);
				temp.add(ofpi);

			}
		}

		return temp;
	}

	public static ArrayList<OFMessage> MAKE_ARP_REQUEST_REPLY() {
		ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
		OFMessage ofpi = null;

		for (int i = 0; i < Global.NODE_SIZE; i++) {
			for (int j = 0; j < Global.NODE_SIZE; j++) {
				Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(i);
				Host dstHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(j);

				byte tempPacket[] = MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0), tempPacket);
				temp.add(ofpi);

				byte tempPacket1[] = MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY, srcHost, dstHost);
				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(1), tempPacket1);
				temp.add(ofpi);

			}
		}

		return temp;
	}

	public static ArrayList<OFMessage> MAKE_TCP_UDP_PACKET(int protocolType, int numberOfHost) {
		ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
		OFMessage ofpi = null;

		for (int i = 0; i < numberOfHost; i++) {
			for (int j = 0; j < numberOfHost; j++) {

				Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(i);
				Host dstHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(j);

				byte tempPacket[] = MAKE_TCP_UDP_PACKET_BYTE(protocolType, srcHost, dstHost, 1000, 1000);

				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0), tempPacket);
				temp.add(ofpi);

				byte tempPacket1[] = MAKE_TCP_UDP_PACKET_BYTE(protocolType, dstHost, srcHost, 1000, 1000);

				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(1), tempPacket1);

				temp.add(ofpi);

			}
		}

		return temp;

	}

	public static ArrayList<OFMessage> MAKE_IP_PACKET() {
		ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
		OFMessage ofpi = null;

		for (int i = 0; i < 1012; i++) {
			for (int j = 0; j < 1012; j++) {
				Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(i);
				Host dstHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(j);

				byte tempPacket[] = MAKE_IP_PACKET_BYTE(srcHost, dstHost);
				ofpi = MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0), tempPacket);
				temp.add(ofpi);
			}
		}
		return temp;

	}

	public static OFMessage MAKE_OPENFLOW_PACKET_IN(Port port, byte[] tempPacket) {
		// openflow
		OFOxmInPort oxe = Global.FACTORY.oxms().inPort(OFPort.of(port.getPortNum()));

		OFOxmList oxmList = OFOxmList.of(oxe);

		Match m = Global.FACTORY.buildMatchV3().setOxmList(oxmList).build();

		OFPacketIn ofpi = Global.FACTORY.buildPacketIn()
				.setMatch(m).setBufferId(OFBufferId.NO_BUFFER)
				.setTotalLen(tempPacket.length).setXid((long) 0).setReason(OFPacketInReason.ACTION)
				.setTableId(TableId.of(0)).setData(tempPacket).build();
		return ofpi;
	}

	public static byte[] MAKE_ARP_PACKET_BYTE(int op, Host src, Host dst) {

		byte tempPacket[] = new byte[60];
		EthernetHeader e = new EthernetHeader();
		ARP arp = new ARP();
		byte[] tempHeader;
		byte[] tempArp;
		switch (op) {
		// request
		case Global.ARP_REQUEST:
			e.setSourceMac(src.getMac());
			e.setDestinationMac("ff:ff:ff:ff:ff:ff"); // broadcast
			e.setEthernetType("arp");
			tempHeader = e.toByte();

			arp.setSendIp(src.getIp());
			arp.setSendMac(src.getMac());
			arp.setTargetIp(dst.getIp());
			arp.setTargetMac("00:00:00:00:00:00");
			arp.setOpCode(1);

			tempArp = arp.toByte();

			// ethernet header
			for (int i = 0; i < 14; i++) {
				tempPacket[i] = tempHeader[i];
			}

			// Arp payload
			for (int i = 0; i < 28; i++) {
				tempPacket[i + 14] = tempArp[i];
			}

			// ethernet trailer
			for (int i = 42; i < 60; i++) {
				tempPacket[i] = 0x00;
			}
			break;

		// reply
		case Global.ARP_REPLY:
			e.setSourceMac(dst.getMac());
			e.setDestinationMac(src.getMac());
			e.setEthernetType("arp");
			tempHeader = e.toByte();

			arp.setSendIp(dst.getIp());
			arp.setSendMac(dst.getMac());
			arp.setTargetIp(src.getIp());
			arp.setTargetMac(src.getMac());
			arp.setOpCode(2);

			tempArp = arp.toByte();

			// ethernet header
			for (int i = 0; i < 14; i++) {
				tempPacket[i] = tempHeader[i];
			}

			// Arp payload
			for (int i = 0; i < 28; i++) {
				tempPacket[i + 14] = tempArp[i];
			}

			// ethernet trailer
			for (int i = 42; i < 60; i++) {
				tempPacket[i] = 0x00;
			}
			break;

		// gratuitous
		case Global.ARP_REPLY_GRATUITOUS:
			e.setSourceMac(src.getMac());
			e.setDestinationMac("ff:ff:ff:ff:ff:ff");
			e.setEthernetType("arp");
			tempHeader = e.toByte();

			arp.setSendIp(src.getIp());
			arp.setSendMac(src.getMac());
			arp.setTargetIp(src.getIp());
			arp.setTargetMac(src.getMac());
			arp.setOpCode(2);

			tempArp = arp.toByte();

			// ethernet header
			for (int i = 0; i < 14; i++) {
				tempPacket[i] = tempHeader[i];
			}

			// Arp payload
			for (int i = 0; i < 28; i++) {
				tempPacket[i + 14] = tempArp[i];
			}

			// ethernet trailer
			for (int i = 42; i < 60; i++) {
				tempPacket[i] = (byte)0x00;
			}
			break;
		}

		return tempPacket;
	}

	public static byte[] MAKE_IP_PACKET_BYTE(Host srcHost, Host dstHost) {
		byte tempPacket[] = new byte[64];
		EthernetHeader e = new EthernetHeader();
		byte[] tempHeader;
		byte[] tempIp;

		e.setSourceMac(srcHost.getMac());
		e.setDestinationMac(dstHost.getMac());
		e.setEthernetType("ip");
		tempHeader = e.toByte();

		Ip ip = new Ip();
		ip.setSrcIp(srcHost.getIp());
		ip.setDstIp(dstHost.getIp());
		tempIp = ip.toByte();

		// ethernet header
		for (int i = 0; i < 14; i++) {
			tempPacket[i] = tempHeader[i];
		}

		for (int i = 0; i < 50; i++) {
			tempPacket[i + 14] = tempIp[i];
		}

		return tempPacket;
	}

	public static byte[] MAKE_TCP_UDP_PACKET_BYTE(int protocolType, Host srcHost, Host dstHost, int srcPort,
			int dstPort) {
		byte tempPacket[] = new byte[60];
		EthernetHeader e = new EthernetHeader();
		byte[] tempHeader;
		byte[] tempIp;
		byte[] tempPayload;

		e.setSourceMac(srcHost.getMac());
		e.setDestinationMac(dstHost.getMac());
		e.setEthernetType("ip");
		tempHeader = e.toByte();

		Ip ip = new Ip();
		ip.setSrcIp(srcHost.getIp());
		ip.setDstIp(dstHost.getIp());
		ip.setProtocol(protocolType);
		tempIp = ip.toByte();

		// ethernet header
		for (int i = 0; i < 14; i++) {
			tempPacket[i] = tempHeader[i];
		}

		// ip header
		for (int i = 0; i < 20; i++) {
			tempPacket[i + 14] = tempIp[i];
		}

		if (protocolType == Global.TCP_PACKET) {
			Tcp tcp = new Tcp();
			tcp.setSrcPort(srcPort);
			tcp.setDstPort(dstPort);
			tempPayload = tcp.toByte();

			for (int i = 0; i < 20; i++) {
				tempPacket[i + 34] = tempPayload[i];
			}

			tempPacket[54] = (byte) 0x00;
			tempPacket[55] = (byte) 0x00;

			for (int i = 0; i < 4; i++) {

				tempPacket[56 + i] = (byte) 0x20;
			}

		} else if (protocolType == Global.UDP_PACKET) {

			Udp udp = new Udp();
			udp.setSrcPort(srcPort);
			udp.setDstPort(dstPort);
			tempPayload = udp.toByte();

			for (int i = 0; i < 20; i++) {
				tempPacket[i + 34] = tempPayload[i];
			}

			tempPacket[54] = (byte) 0x00;
			tempPacket[55] = (byte) 0x00;

			for (int i = 0; i < 4; i++) {

				tempPacket[56 + i] = (byte) 0x20;
			}

		}

		return tempPacket;
	}

}
