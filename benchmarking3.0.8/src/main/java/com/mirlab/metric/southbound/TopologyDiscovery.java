package com.mirlab.metric.southbound;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Main;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.BenchmarkTimer;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.topo.CreateTopo;

public class TopologyDiscovery {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	//public static boolean HAS_STARTED = false;
	//public static boolean IS_COMPLETED = false;
	private static DecimalFormat df = new DecimalFormat("#0.00000");

	public static void go() {
		version0();
		Log.exportLog(TopologyDiscovery.class.getSimpleName(), Main.gui.S_metricList.getSelectedIndex());
	}

	private static void version0() {
		try {
			// UI 속성 수정
			Main.gui.S_progressBar.setValue(0); // 진척도
			Main.gui.S_startButton.setEnabled(false);// run 버튼
			Main.gui.S_metricList.setEnabled(false); // metric 옵션

			Node[] nodes = null;

			CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
			nodes = ct.go();

			Tasks.HAS_STARTED = true;

			Thread.sleep(10000); // 10s

			try { // 휴식 15s - Trial Duration

				Thread.sleep(15000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
				for (int j = 0; j < nodes[i].getLLDP_OUT().size(); j++) {
					Result.TOPOLOGY_DISCOVERY_LLDP_OUT.add(nodes[i].getLLDP_OUT().get(j));
				}
				for (int j = 0; j < nodes[i].getLLDP_IN().size(); j++) {
					Result.TOPOLOGY_DISCOVERY_LLDP_IN.add(nodes[i].getLLDP_IN().get(j));
				}
			}

			Log.ADD_LOG_PANEL("Tasks 0 Completed. Discovery Time: "
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_OUT)
					+ "ms" + "\nSize of LLDP IN:" + Result.TOPOLOGY_DISCOVERY_LLDP_IN.size() + "\nSize of LLDP OUT:"
					+ Result.TOPOLOGY_DISCOVERY_LLDP_OUT.size() + "\n F-L IN:"
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_IN)
					+ "\n F-L OUT:"
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_OUT, Result.TOPOLOGY_DISCOVERY_LLDP_OUT),
					TopologyDiscovery.class.getSimpleName());

			// GUI 화면에 결과 출력
			Result.ADD_RESULT(df.format(
					BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_OUT)), 0,
					2);

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
