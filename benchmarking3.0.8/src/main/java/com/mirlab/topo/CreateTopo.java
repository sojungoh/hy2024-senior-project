package com.mirlab.topo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.DistributedPort;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.component.Port;
import com.mirlab.global.Global;
import com.mirlab.lib.IP;
import com.mirlab.lib.MacAddr;

public class CreateTopo {

	private long dpid = 1;

	public CreateTopo() {
	}

	public CreateTopo(long startDpid) {
		this.dpid = startDpid;
	}

	public Node[] go() {
		Node[] nodes = null;

		if (Global.IS_ENABLE_DISTRIBUTED) {

//			switch (Global.TOPO_TYPE) {
//			case Global.TOPO_LINEAR:
//				nodes = create_linearTopo_Dis();
//				break;
//			case Global.TOPO_RING:
//				nodes = create_ringTopo_Dis();
//				break;
//			}

			nodes = create_ringTopo_Dis();

		} else {
			switch (Global.topoType) {
			case LINEAR:
				nodes = create_linearTopo();
				break;
			case RING:
				nodes = create_ringTopo();
				break;
			case MININET:
				nodes = create_mininetTopo();
				break;
			}
		}

		return nodes;
	}

	private Node[] create_ringTopo_Dis() {

		// TODO Auto-generated method stub
		Node[] nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].createPort(2);
		}

		// create srchosts Global.HOST_IP[2]=0 mac5=0
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host srcH;

			String ip = IP.generateIPAddr(Global.HOST_IP[0], Global.HOST_IP[1], 0, n);
			String mac = MacAddr.generateMacAddr(Integer.valueOf(Global.HOST_MAC, 16) + Global.MY_ID, 0, n);

			srcH = new Host(ip, mac);

			srcHosts[n - 1] = srcH;
		}

		// create dstHosts Global.HOST_IP[2]=1 mac5=1
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host dstH;

			String ip = IP.generateIPAddr(Global.HOST_IP[0], Global.HOST_IP[1], 1, n);
			String mac = MacAddr.generateMacAddr(Integer.valueOf(Global.HOST_MAC, 16) + Global.MY_ID, 1, n);

			dstH = new Host(ip, mac);

			dstHosts[n - 1] = dstH;

		}

		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		// create link between s and h
		nodes[0].addPort();
		for (int n = 0; n < srcHosts.length; n++) {
			nodes[0].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
		}
		nodes[nodes.length / 2].addPort();
		for (int n = 0; n < dstHosts.length; n++) {
			nodes[nodes.length / 2].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
		}

		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1).setConnectedPort(nodes[0].getPortList().get(0));
		nodes[0].getPortList().get(0).setConnectedPort(nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1));
		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].setNextNode(nodes[0]);

		// **********************************************
		Port port1 = nodes[0].addPort();

		DistributedPort distributedPort1 = Global.distributedPortList.get(0);

		port1.setDistributedPort(distributedPort1);
		distributedPort1.setConnectedPort(port1);

		Port port2 = nodes[Global.NUMBER_OF_TEST_SWITCH - 1].addPort();

		DistributedPort distributedPort2 = Global.distributedPortList.get(1);

		port2.setDistributedPort(distributedPort2);
		distributedPort2.setConnectedPort(port2);

		distributedPort1.startClient();
		distributedPort2.startClient();

		// **********************************************

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].start_OpenFlowClient();
		}

		return nodes;
	}

	private Node[] create_linearTopo_Dis() {
		// TODO Auto-generated method stub
		return null;
	}

	private Node[] create_mininetTopo() {
		// the mininet topology's hosts should be 2 or it will make an error
		// TODO Auto-generated method stub

		Node[] nodes = null;
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];
		JSONObject JSONMSG = null;
		int portToHost_count = 2;

		JSONMSG = Global.JSONMSG;

		JSONArray mn_links = JSONMSG.getJSONArray("links");
		JSONArray mn_switches = JSONMSG.getJSONArray("switches");

		int link_count = mn_links.length();
		int switch_count = mn_switches.length();

		nodes = new Node[switch_count];

		// create nodes
		for (int i = 0; i < switch_count; i++) {
			nodes[i] = new Node((long) dpid++, i);
		}

		// create srchosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h1;
			if (n < 16) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(n));

			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(n));
			}

			srcHosts[n - 1] = h1;
		}

		// create dstHosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h5;
			if (n < 16) {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + "0" + Integer.toHexString(n));

			} else {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + Integer.toHexString(n));
			}

			dstHosts[n - 1] = h5;

		}

		// create links
		for (int i = 0; i < link_count; i++) {
			String src = mn_links.getJSONObject(i).getString("src");
			String dest = mn_links.getJSONObject(i).getString("dest");
			String src_type = src.replaceAll("[^(A-Za-z)]", "");
			String dest_type = dest.replaceAll("[^(A-Za-z)]", "");
			int src_num = Integer.parseInt(src.replaceAll("[^(0-9)]", "")) - 1;
			int dest_num = Integer.parseInt(dest.replaceAll("[^(0-9)]", "")) - 1;

			if (src_type.equals(dest_type)) {// between switch and switch

				nodes[src_num].addPort();
				nodes[dest_num].addPort();

				nodes[src_num].getPortList().getLast().setConnectedPort(nodes[dest_num].getPortList().getLast());
				nodes[dest_num].getPortList().getLast().setConnectedPort(nodes[src_num].getPortList().getLast());

			} else {// between switch and host

				if (src_type.equals("s")) {

					nodes[src_num].addPort();

					if (portToHost_count == 2) {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[src_num].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
						}
						portToHost_count--;
					} else {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[src_num].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
						}
					}

				} else {

					nodes[dest_num].addPort();

					if (portToHost_count == 2) {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[dest_num].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
						}
						portToHost_count--;
					} else {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[dest_num].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
						}
					}
				}

			}
		}

		// global 변수에 처음과 마지막 노드 설정
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < switch_count; i++) {
			nodes[i].start_OpenFlowClient();
		}
		return nodes;
	}

	private Node[] create_linearTopo() {

		Node[] nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].createPort(2);
		}

		// create srchosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h1;
			if (n < 16) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(n));

			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(n));
			}

			srcHosts[n - 1] = h1;
		}

		// create dstHosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h5;
			if (n < 16) {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + "0" + Integer.toHexString(n));

			} else {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + Integer.toHexString(n));
			}

			dstHosts[n - 1] = h5;

		}

		// create link between switch and switch
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		// create link between switch and host
		for (int n = 0; n < srcHosts.length; n++) {
			nodes[0].getPortList().get(0).addConnectedHostToList(srcHosts[n]);
		}
		for (int n = 0; n < dstHosts.length; n++) {
			nodes[nodes.length - 1].getPortList().get(1).addConnectedHostToList(dstHosts[n]);
		}

		// global 변수에 처음과 마지막 노드 설정
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].start_OpenFlowClient();
		}

		return nodes;
	}

	private Node[] create_ringTopo() {
		// no host
		Node[] nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].createPort(2);
		}

		// create srchosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h1;
			if (n < 16) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(n));

			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(n));
			}

			srcHosts[n - 1] = h1;
		}

		// create dstHosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h5;
			if (n < 16) {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + "0" + Integer.toHexString(n));

			} else {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + Integer.toHexString(n));
			}

			dstHosts[n - 1] = h5;

		}

		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1).setConnectedPort(nodes[0].getPortList().get(0));
		nodes[0].getPortList().get(0).setConnectedPort(nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1));
		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].setNextNode(nodes[0]);

		// create link between switch and host
		nodes[0].addPort();
		for (int n = 0; n < srcHosts.length; n++) {
			nodes[0].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
		}
		nodes[nodes.length / 2].addPort();
		for (int n = 0; n < dstHosts.length; n++) {
			nodes[nodes.length / 2].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
		}

		// global 변수에 처음과 마지막 노드 설정
		Global.ROOTNODE = nodes[0];
		// TODO: LEAFNODE는 nodes[nodes.length / 2] 아닌가?
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].start_OpenFlowClient();
		}

		return nodes;
	}

}
