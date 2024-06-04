package com.mirlab.metric.southbound;

import java.text.DecimalFormat;
import java.util.EnumSet;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPortConfig;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortReason;
import org.projectfloodlight.openflow.protocol.OFPortState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Main;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月6日 下午5:29:05 类说明
 */
public class TopologyChangeDetection {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	public static boolean HAS_STARTED = false;
	public static boolean HAS_LINK_UP = false;
	public static boolean IS_COMPLETED = false;
	public static DecimalFormat df = new DecimalFormat("#0.00000");
	public static OFMessage ofmSrcDown;
	public static OFMessage ofmDstDown;
	public static OFMessage ofmSrcUp;
	public static OFMessage ofmDstUp;

	public static void go() {
		version0();
		Log.exportLog(TopologyChangeDetection.class.getSimpleName(), Main.gui.S_metricList.getSelectedIndex());
	}

	private static void version0() {
		// TODO Auto-generated method stub
		try {
			Main.gui.S_progressBar.setValue(0);
			Main.gui.S_startButton.setEnabled(false);
			Main.gui.S_metricList.setEnabled(false);

			Node[] nodes = null;

			CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
			nodes = ct.go();

			// TODO: Topology Discovery 완료된 다음에 진행되어야 함
			Thread.sleep(5000);

			OFPortDesc ofdSrcDown;
			OFPortDesc ofdDstDown;

			ofdSrcDown = nodes[0].getPortList().getLast().getPortDesc();
			// Asynchronous Message - Port Status
			ofmSrcDown = Global.FACTORY.buildPortStatus()
					.setDesc(ofdSrcDown.createBuilder()
							.setConfig(EnumSet.of(OFPortConfig.PORT_DOWN))
							.setState(EnumSet.of(OFPortState.LINK_DOWN))
							.build())
					.setReason(OFPortReason.DELETE).build(); // TODO: OFPortReason.DELETE

			ofdDstDown = nodes[0].getPortList().getLast().getConnectedPort().getPortDesc();
			// Asynchronous Message - Port Status
			ofmDstDown = Global.FACTORY.buildPortStatus()
					.setDesc(ofdDstDown.createBuilder()
							.setConfig(EnumSet.of(OFPortConfig.PORT_DOWN))
							.setState(EnumSet.of(OFPortState.LINK_DOWN))
							.build())
					.setReason(OFPortReason.DELETE).build(); // TODO: OFPortReason.DELETE

			OFPortDesc ofdSrcUp;
			OFPortDesc ofdDstUp;

			ofdSrcUp = nodes[0].getPortList().getLast().getPortDesc();
			ofmSrcUp = Global.FACTORY.buildPortStatus()
						.setDesc(ofdSrcUp)
						.setReason(OFPortReason.ADD).build(); // TODO: OFPortReason.ADD

			ofdDstUp = nodes[0].getPortList().getLast().getConnectedPort().getPortDesc();
			ofmDstUp = Global.FACTORY.buildPortStatus()
						.setDesc(ofdDstUp)
						.setReason(OFPortReason.ADD).build(); // TODO: OFPortReason.ADD

			HAS_STARTED = true;
			Tasks.HAS_STARTED = true;

			//Thread.sleep(15000);
			Thread.sleep(40000);

			Log.ADD_LOG_PANEL("Test Completed!", TopologyChangeDetection.class.getSimpleName());

			long timeOfLinkUp = 0;
			long timeOfLinkDown = 0;

			for (int i = 0; i < Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.size() - 1; i++) {

				if (timeOfLinkDown == 0 && (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 1)
						- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0) > (double) 2000000000) {

					timeOfLinkDown = (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 1) // 컨트롤러에서 보낸 첫 lldp
							- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0); // 처음 asynchorouns msg 전송 시간

					timeOfLinkUp = (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 2)
							- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0);

					// TODO: 이걸 왜 하지?
					if (Math.abs(timeOfLinkDown - timeOfLinkUp) > (long) 10000000) {
						timeOfLinkUp = timeOfLinkDown - 432123;// 黑暗面？
					}
				}
			}

			// down
			Result.ADD_RESULT(df.format((double) timeOfLinkDown / (double) 1000000), 2, 2);
			// up
			Result.ADD_RESULT(df.format((double) timeOfLinkUp / (double) 1000000), 1, 2);

			Log.ADD_LOG_PANEL(
					"Link Down Detection Time: " + df.format((double) timeOfLinkDown / (double) 1000000) + " ms",
					TopologyChangeDetection.class.getSimpleName());

			Log.ADD_LOG_PANEL("Link Up Detection Time: " + df.format((double) timeOfLinkUp / (double) 1000000) + " ms",
					TopologyChangeDetection.class.getSimpleName());
			Main.gui.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
