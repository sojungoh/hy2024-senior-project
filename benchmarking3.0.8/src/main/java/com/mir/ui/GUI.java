package com.mir.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.http.impl.cookie.BrowserCompatSpecFactory.SecurityLevel;

import com.mir.ui.distributedGUI.DistributedFrame;
import com.mirlab.enumType.Controller;
import com.mirlab.enumType.ControllerMode;
import com.mirlab.enumType.NorthboundMetric;
import com.mirlab.enumType.SNboundMetric;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;

public class GUI {

	public JFrame frmMirlabBenchmark;

	public JTextArea S_LOG_TEXT;
	public JTextArea S_TOPO_TEXT;
	public JTextArea S_TOPOLOGY_TEXT;

	public JComboBox S_comboBoxTestType = new JComboBox();

	private JTextField S_controller2IpPort;
	private JTextField S_controller1IpPort;
	public JList S_metricList;
	private JScrollPane S_scrollPane_2;
	private JTabbedPane S_tabbedPane;
	public JProgressBar S_progressBar;
	private JTextField S_controller3IpPort;
	private JComboBox S_comboBoxControllerMode;
	private JComboBox S_comboBoxControllerType;
	public JTable S_RESULT_TABLE;
	public JButton S_startButton;
	public JLabel S_benchmarkCondition;
	public JProgressBar S_progressBarTotal;
	private JTextField S_numberOfS;
	private JTextField S_hostIP_1;
	private JTextField S_hostIP_2;
	private JTextField S_textField_3;
	private JTextField S_textField_4;
	private JComboBox S_comboBoxOFProtocolType;
	public JComboBox S_comboBoxTopoType;
	private JComboBox S_packetComboBox;
	private JComboBox S_provisioningComboBox;

	/*************************************************/

	public JTextArea N_LOG_TEXT;
	public JTextArea N_TOPO_TEXT;
	public JTextArea N_TOPOLOGY_TEXT;

	public JComboBox N_comboBoxTestType = new JComboBox();

	private JTextField N_controller2IpPort;
	private JTextField N_controller1IpPort;
	public JList N_metricList;
	private JScrollPane N_scrollPane_2;
	private JTabbedPane N_tabbedPane;
	public static JProgressBar N_progressBar;
	private JTextField N_controller3IpPort;
	private JComboBox N_comboBoxControllerMode;
	private JComboBox N_comboBoxControllerType;
	public JTable N_RESULT_TABLE;
	public static JButton N_startButton;
	public JLabel N_benchmarkCondition;
	public static JProgressBar N_progressBarTotal;
	private JTextField N_numberOfS;
	private JComboBox N_comboBoxOFProtocolType;
	private JComboBox N_comboBoxTopoType;
	private JComboBox N_packetComboBox;
	private JComboBox N_provisioningComboBox;
	private JComboBox N_sendType; 

	/*************************************************/

	public DistributedFrame dr;
	public ConfigurationFrame cf;
	public FileReader reader; // to read files
	private JTextField N_Flow_Number;

	/************************************************* wrap around/ */
	private JTextField SN_Flow_Number;

	public JTextArea SN_LOG_TEXT;
	public JTextArea SN_TOPO_TEXT;
	public JTextArea SN_TOPOLOGY_TEXT;

	public JComboBox SN_comboBoxTestType = new JComboBox();

	private JTextField SN_controller2IpPort;
	private JTextField SN_controller1IpPort;
	public JList SN_metricList;
	private JScrollPane SN_scrollPane_2;
	private JTabbedPane SN_tabbedPane;
	public JProgressBar SN_progressBar;
	private JTextField SN_controller3IpPort;
	private JComboBox SN_comboBoxControllerMode;
	private JComboBox SN_comboBoxControllerType;
	public JTable SN_RESULT_TABLE;
	public JButton SN_startButton;
	public JLabel SN_benchmarkCondition;
	public JProgressBar SN_progressBarTotal;
	private JTextField SN_numberOfS;
	private JTextField SN_hostIP_1;
	private JTextField SN_hostIP_2;
	private JTextField SN_textField_3;
	private JTextField SN_textField_4;
	private JComboBox SN_comboBoxOFProtocolType;
	public JComboBox SN_comboBoxTopoType;
	private JComboBox SN_packetComboBox;
	private JComboBox SN_provisioningComboBox;
	private JTextField SN_GuardTime;
	private JTextField N_GuardTime;
	
	/**
	 * Create the application.
	 */
	public GUI() {
		cf = new ConfigurationFrame();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMirlabBenchmark = new JFrame();
		frmMirlabBenchmark.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmMirlabBenchmark.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmMirlabBenchmark.setTitle("Mir-Lab SDN Benchmark v3.0.8");
		frmMirlabBenchmark.setResizable(false);
		frmMirlabBenchmark.setBounds(new Rectangle(50, 50, 50, 50));
		frmMirlabBenchmark.setBounds(100, 100, 1080, 768);
		frmMirlabBenchmark.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMirlabBenchmark.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1074, 31);
		frmMirlabBenchmark.getContentPane().add(menuBar);
		menuBar.setToolTipText("");

		JMenu fileMB = new JMenu("File");
		//fileMB.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/filechooser/icons/folder.png")));
		menuBar.add(fileMB);

		JMenuItem newMI = new JMenuItem("New");
		//newMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/filechooser/icons/file_icon.png")));
		fileMB.add(newMI);

		JMenuItem openMI = new JMenuItem("Open");
		//openMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/ninepatch/icons/open.png")));
		fileMB.add(openMI);

		JMenuItem saveMI = new JMenuItem("Save As...");
		//saveMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/ninepatch/icons/saveas.png")));
		fileMB.add(saveMI);

		JMenu windowMB = new JMenu("Configuration");
		//windowMB.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/window/icons/window.png")));
		menuBar.add(windowMB);

		JMenuItem reportWindowMI = new JMenuItem("Distributed Benchmark");
		reportWindowMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				dr = new DistributedFrame();
				dr.frmDistrubutedBenchmarkReport.setVisible(true);

			}
		});

		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				cf.frmConfiguration.setVisible(true);

			}
		});
		//mntmConfiguration.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/utils/icons/selection/gripper.png")));
		windowMB.add(mntmConfiguration);
		//reportWindowMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/table/icons/table.png")));
		windowMB.add(reportWindowMI);

		// by Joon .. judged in line 145

		JMenuItem OpenTopology = new JMenuItem("OpenTopology");
		OpenTopology.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(frmMirlabBenchmark) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						reader = new FileReader(file.getAbsolutePath());

						System.out.println(file.getName() + " is opened");

						if (file.getName().contains("mn") == true) {
							Topo_import topology = new Topo_import(reader);
							Global.JSONMSG = topology.JSONMSG;
							S_numberOfS.setText(Integer.toString(topology.JSONMSG.getJSONArray("switches").length()));
							N_numberOfS.setText(Integer.toString(topology.JSONMSG.getJSONArray("switches").length()));
							topology.JSONMSG.getJSONArray("switches").length();
							S_comboBoxTopoType.setSelectedIndex(2);
							N_comboBoxTopoType.setSelectedIndex(2);
						} else {
							System.out.println("wrong file");
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		//OpenTopology.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/utils/icons/selection/gripper.png")));
		windowMB.add(OpenTopology);
		//reportWindowMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/table/icons/table.png")));
		windowMB.add(OpenTopology);

		// this is for opening the saved mininet topology

		JMenu helpMB = new JMenu("Help");
		//helpMB.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/transition/icons/toolbar/2.png")));
		menuBar.add(helpMB);

		JMenuItem aboutMI = new JMenuItem("About");
		aboutMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Hanyang MIR LAB.\nSDN Benchmark v3.0.8", "MIR LAB.",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		//aboutMI.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/ninepatch/icons/icon.png")));
		helpMB.add(aboutMI);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 37, 1054, 695);
		frmMirlabBenchmark.getContentPane().add(tabbedPane_1);

		JPanel southbound = new JPanel();
		southbound.setLayout(null);
		tabbedPane_1.addTab("southbound", null, southbound, null);

		JPanel northbound = new JPanel();
		northbound.setLayout(null);
		tabbedPane_1.addTab("northbound", null, northbound, null);

		JPanel snbound = new JPanel();
		snbound.setLayout(null);
		tabbedPane_1.addTab("SNbound", null, snbound, null);

		southbound(southbound);
		northbound(northbound);
		snbound(snbound);

	}

	private void southbound(JPanel southbound) {
		JScrollPane metricsPane = new JScrollPane();
		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		metricsPane.setAutoscrolls(true);
		metricsPane.setBounds(41, 32, 242, 299);
		// frmMirlabBenchmark.getContentPane().add(scrollPane);
		southbound.add(metricsPane);

		S_metricList = new JList();
		S_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
		S_metricList.setAutoscrolls(false);
		metricsPane.setViewportView(S_metricList);
		S_metricList.setModel(new AbstractListModel() {
			String[] values = new String[] { "1.TOPOLOGY_DISCOVERY_TIME",
					"2.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP", "3.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME",
					"4.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE", "5.REACTIVE_PATH_PROVISIONING_TIME",
					"6.REACTIVE_PATH_PROVISIONING_RATE", "7.*PROACTIVE_PATH_PROVISIONING_TIME",
					"8.*PROACTIVE_PATH_PROVISIONING_RATE", "9. Test (Metric 1~6)",
					"---------------SDN controller should be reboot to test metric 10~16",
					"10.CONTROL_SESSION_CAPACITY_CCD", "11.NETWORK_DISCOVERY_SIZE_NS",
					"12.FORWARDING_TABLE_CAPACITY_NRP", "13.*EXCEPTION_HANDLING_SECURITY", "14.*DOS_ATTACKS_SECURITY",
					"15.*CONTROLLER_FAILOVER_TIME_RELIABILITY",
					"16.*NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE","TEST" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		S_metricList.setSelectedIndex(0);

		S_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));

		// run 버튼에 관한 코드
		S_startButton = new JButton("Run");
		S_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 버튼을 누른 후 다음 코드 시행
				S_startButton.setEnabled(false);// 버튼 비활성화
				S_metricList.setEnabled(false);// metric 비활성화

				// metric index，metric의 선택값을 얻고 global 변수에 값 부여
//				Global.TEST_METRIC = S_metriclist.
//						S_metricList.getSelectedIndex();
//				String metric = S_metricList.getSelectedValue().toString()

				String selec_metric = S_metricList.getSelectedValue().toString().split("\\.")[1];

				Global.southboundMetric = SouthboundMetric.valueOf(selec_metric);

				// --Benchmark configuration 1
				// OF Version
//				Global.OPENFLOW_VERSION = Global.OF_V_3;
				Global.ofVersion = org.projectfloodlight.openflow.protocol.OFVersion.OF_13;
				// -- Benchmark configuration 2
//			    OF_10(1), OF_11(2), OF_12(3), OF_13(4), OF_14(5);

				// topology type RING or LINEAR
//				Global.TOPO_TYPE = S_comboBoxTopoType.getSelectedIndex();
				Global.topoType = TopologyType.valueOf(S_comboBoxTopoType.getSelectedItem().toString().toUpperCase());
				// --Benchmark configuration 3
				// node # node 수
				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) S_numberOfS.getText().toString());

				// test time
				Global.TEST_TIME = 5;

				// Benchmark configuration 4
				// Host Ip
				Global.HOST_IP[0] = Integer.parseInt((String) S_hostIP_1.getText().toString());
				Global.HOST_IP[1] = Integer.parseInt((String) S_hostIP_2.getText().toString());

				// SDN controller configuration 1
				// Controller Mode （Standalone， Cluster）
//				Global.CONTROLLER_MODE = S_comboBoxControllerMode.getSelectedIndex();
				Global.conMode = ControllerMode
						.valueOf(S_comboBoxControllerMode.getSelectedItem().toString().toUpperCase());
				if (Global.conMode == ControllerMode.STANDALONE) {
					// standalone mode
					// SDN controller configuration 3
					StringTokenizer st = new StringTokenizer(S_controller1IpPort.getText().toString(), ":");// ip와 port 분리
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 1000;
					// Global.BUFF_SIZE = 1000000;
				} else {
					// cluster mode
					// SDN controller configuration 3
					StringTokenizer st = new StringTokenizer(S_controller1IpPort.getText().toString(), ":");
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 200;
					// Global.BUFF_SIZE = 40000;
				}

				// packet loss rate 미완성
				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());

				// SDN controller configuration 2
				// Controller Type
				// CONTROLLER_TYPE_ONOS = 0;
				// CONTROLLER_TYPE_FLOODLIGHT = 1;
				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
//				Global.CONTROLLER_TYPE = S_comboBoxControllerType.getSelectedIndex();
				Global.conTroller = Controller
						.valueOf(S_comboBoxControllerType.getSelectedItem().toString().toUpperCase());

				// host size
				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());

				// Benchmark configuration 5
				// Asynchronous message type
				// unknowns type asynchronous message type
				// unkowns enum
				if (S_packetComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (S_packetComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
				} else if (S_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
				} else {
					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
				}

				// Benchmark configuration 6
				// Unknown packet type （TCP， UDP）
				// provisioning unknow type
				if (S_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (S_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
				} else {
					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
				}

				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());

				// bulk size
				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());

				new Thread(new Runnable() {
					public void run() {
						S_RunButton.go();
					}
				}).start();

			}
		}

		);
		S_startButton.setBounds(460, 244, 93, 23);
		southbound.add(S_startButton);

		JButton btnStop = new JButton("Force Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Initializer.INITIAL_CHANNEL_POOL();
				S_progressBarTotal.setMaximum(0);
				S_progressBarTotal.setValue(0);

				S_progressBar.setMaximum(0);
				S_progressBar.setValue(0);

			}
		});
		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStop.setBounds(804, 244, 93, 23);
		southbound.add(btnStop);

		S_tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		S_tabbedPane.setBounds(41, 344, 992, 310);
		southbound.add(S_tabbedPane);
		JScrollPane scrollPane_3 = new JScrollPane();
		S_tabbedPane.addTab("Result", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
				scrollPane_3, null);

		S_RESULT_TABLE = new JTable();
		S_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		S_RESULT_TABLE.setRowHeight(26);
		S_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		S_RESULT_TABLE.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
						{ "Proactive Path Provisioning Time", "ms", "N/A", null, null, null },
						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", null, null, null },
						{ "Control Session Capacity", "Sessions", null, null, null, null }, },
				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
		S_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
		S_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
		scrollPane_3.setViewportView(S_RESULT_TABLE);

		S_LOG_TEXT = new JTextArea();
		S_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_LOG_TEXT.setLineWrap(true);
		S_TOPO_TEXT = new JTextArea();
		S_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_TOPO_TEXT.setLineWrap(true);

		S_tabbedPane.addTab("Log", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
				new JScrollPane(S_LOG_TEXT), null);

		S_TOPOLOGY_TEXT = new JTextArea();
		S_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_TOPOLOGY_TEXT.setLineWrap(true);

		S_tabbedPane.addTab("Topology", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/content/icons/presentation.png")),
				new JScrollPane(S_TOPO_TEXT), null);

		JLabel lblNewLabel = new JLabel("Metrics");
		//lblNewLabel.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel.setBounds(41, 10, 93, 15);
		southbound.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		//lblNewLabel_1.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblNewLabel_1.setBounds(309, 10, 170, 15);
		southbound.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(309, 167, 724, 67);
		southbound.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Controller Mode");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(48, 10, 88, 20);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("Controller IP");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(48, 40, 88, 18);
		panel.add(lblNewLabel_3);

		S_controller2IpPort = new JTextField();
		S_controller2IpPort.setEditable(false);
		S_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller2IpPort.setAutoscrolls(false);
		S_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller2IpPort.setText("166.104.28.130:6633");
		S_controller2IpPort.setBounds(316, 40, 160, 18);
		panel.add(S_controller2IpPort);

		// first IP
		S_controller1IpPort = new JTextField();
		S_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller1IpPort.setAutoscrolls(false);
		S_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller1IpPort.setText("192.168.1.114:6633");
		S_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller1IpPort.setBounds(142, 40, 160, 18);
		panel.add(S_controller1IpPort);

		S_controller3IpPort = new JTextField();
		S_controller3IpPort.setEditable(false);
		S_controller3IpPort.setText("166.104.28.131:6633");
		S_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller3IpPort.setAutoscrolls(false);
		S_controller3IpPort.setBounds(486, 40, 160, 18);
		panel.add(S_controller3IpPort);

		S_comboBoxControllerMode = new JComboBox();
		S_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
		S_comboBoxControllerMode.setSelectedIndex(0);
		S_comboBoxControllerMode.setBounds(146, 10, 126, 20);
		panel.add(S_comboBoxControllerMode);

		JLabel lblControllerMode = new JLabel("Controller Mode");
		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblControllerMode.setBounds(48, 10, 88, 20);
		// panel.add(lblControllerMode);

		S_comboBoxControllerType = new JComboBox();
		S_comboBoxControllerType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				S_packetComboBox.removeAllItems();
				S_provisioningComboBox.removeAllItems();

				Controller conValue = Controller
						.valueOf(S_comboBoxControllerType.getSelectedItem().toString().toUpperCase());

				switch (conValue) {
				case ONOS:

					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
					break;
				case FLOODLIGHT:
					S_packetComboBox.removeAllItems();
					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
					break;
				case OPENDAYLIGHT:

					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP", "ECHO" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP" }));
					break;

				}

			}
		});

		S_comboBoxControllerType.setBounds(520, 10, 126, 20);
		panel.add(S_comboBoxControllerType);
		S_comboBoxControllerType
				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
		S_comboBoxControllerType.setSelectedIndex(0);
		S_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblControllerType = new JLabel("Controller Type");
		lblControllerType.setBounds(422, 10, 88, 20);
		panel.add(lblControllerType);
		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
		//lblNewLabel_2.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_2.setBounds(309, 142, 204, 15);
		southbound.add(lblNewLabel_2);

		S_progressBar = new JProgressBar();
		S_progressBar.setBackground(Color.WHITE);
		S_progressBar.setStringPainted(true);
		S_progressBar.setBounds(460, 287, 573, 13);
		southbound.add(S_progressBar);

		S_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
		S_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
		S_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
		S_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
		S_benchmarkCondition.setBounds(293, 344, 771, 15);
		southbound.add(S_benchmarkCondition);

		S_progressBarTotal = new JProgressBar();
		S_progressBarTotal.setStringPainted(true);
		S_progressBarTotal.setBackground(Color.WHITE);
		S_progressBarTotal.setBounds(460, 321, 573, 13);
		southbound.add(S_progressBarTotal);

		JLabel lblCurrentProgress = new JLabel("Current Progress");
		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblCurrentProgress.setBounds(309, 285, 141, 15);
		southbound.add(lblCurrentProgress);

		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalProgress.setBounds(309, 319, 141, 15);
		southbound.add(lblTotalProgress);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_1.setBounds(309, 32, 724, 101);
		southbound.add(panel_1);

		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
		panel_1.add(lblNumberOfSwitch);

		S_numberOfS = new JTextField();
		S_numberOfS.setText("11");
		S_numberOfS.setMargin(new Insets(3, 3, 3, 3));
		S_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
		S_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_numberOfS.setAutoscrolls(false);
		S_numberOfS.setBounds(191, 40, 79, 18);
		panel_1.add(S_numberOfS);

		S_comboBoxOFProtocolType = new JComboBox();
		S_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
		S_comboBoxOFProtocolType.setSelectedIndex(3);
		S_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
		panel_1.add(S_comboBoxOFProtocolType);

		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblOfProtocolVer.setBounds(10, 10, 100, 20);
		panel_1.add(lblOfProtocolVer);

		S_hostIP_1 = new JTextField();
		S_hostIP_1.setText("10");
		S_hostIP_1.setMargin(new Insets(3, 3, 3, 3));
		S_hostIP_1.setHorizontalAlignment(SwingConstants.CENTER);
		S_hostIP_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_hostIP_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_hostIP_1.setAutoscrolls(false);
		S_hostIP_1.setBounds(479, 40, 28, 18);
		panel_1.add(S_hostIP_1);

		JLabel lblHostIp = new JLabel("Host IP start from");
		lblHostIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblHostIp.setBounds(370, 40, 99, 18);
		panel_1.add(lblHostIp);

		S_hostIP_2 = new JTextField();
		S_hostIP_2.setText("0");
		S_hostIP_2.setMargin(new Insets(3, 3, 3, 3));
		S_hostIP_2.setHorizontalAlignment(SwingConstants.CENTER);
		S_hostIP_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_hostIP_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_hostIP_2.setAutoscrolls(false);
		S_hostIP_2.setBounds(530, 40, 28, 18);
		panel_1.add(S_hostIP_2);

		S_textField_3 = new JTextField();
		S_textField_3.setEditable(false);
		S_textField_3.setText("0");
		S_textField_3.setMargin(new Insets(3, 3, 3, 3));
		S_textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		S_textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_textField_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_textField_3.setAutoscrolls(false);
		S_textField_3.setBounds(584, 40, 28, 18);
		panel_1.add(S_textField_3);

		S_textField_4 = new JTextField();
		S_textField_4.setEditable(false);
		S_textField_4.setText("2");
		S_textField_4.setMargin(new Insets(3, 3, 3, 3));
		S_textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		S_textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_textField_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_textField_4.setAutoscrolls(false);
		S_textField_4.setBounds(638, 40, 28, 18);
		panel_1.add(S_textField_4);

		JLabel label_1 = new JLabel(".");
		label_1.setBounds(517, 41, 6, 15);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel(".");
		label_2.setBounds(568, 41, 6, 15);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel(".");
		label_3.setBounds(622, 41, 6, 15);
		panel_1.add(label_3);

		S_comboBoxTopoType = new JComboBox();
		S_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
		S_comboBoxTopoType.setSelectedIndex(0);
		S_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxTopoType.setBounds(517, 10, 79, 20);
		panel_1.add(S_comboBoxTopoType);

		JLabel label_4 = new JLabel("Topology Type");
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_4.setBounds(370, 10, 88, 20);
		panel_1.add(label_4);

		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketType.setBounds(10, 69, 171, 20);
		panel_1.add(lblPacketType);

		S_packetComboBox = new JComboBox();
		S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
		S_packetComboBox.setSelectedIndex(0);
		S_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_packetComboBox.setBounds(191, 68, 79, 20);
		panel_1.add(S_packetComboBox);

		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUnknownPacketType.setBounds(370, 68, 144, 20);
		panel_1.add(lblUnknownPacketType);

		S_provisioningComboBox = new JComboBox();
		S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
		S_provisioningComboBox.setSelectedIndex(0);
		S_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_provisioningComboBox.setBounds(518, 69, 78, 20);
		panel_1.add(S_provisioningComboBox);

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void northbound(JPanel northbound) {
		JScrollPane metricsPane = new JScrollPane();
		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		metricsPane.setAutoscrolls(true);
		metricsPane.setBounds(41, 32, 242, 299);
		// frmMirlabBenchmark.getContentPane().add(scrollPane);
		northbound.add(metricsPane);

		N_metricList = new JList();
		N_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
		N_metricList.setAutoscrolls(false);
		metricsPane.setViewportView(N_metricList);
		N_metricList.setModel(new AbstractListModel() {
			String[] values = new String[] { "1.PROACTIVE_PATH_PROVISIONING",
					"2.FORWARDING_TABLE_CAPACITY_NRP"
					};

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
//		N_metricList.setModel(new AbstractListModel() {
//			String[] values = new String[] { "17.PROACTIVE_PATH_PROVISIONING",
//					"18.SYNC_PROACTIVE_PATH_PROVISIONING_TIME","1.TOPOLOGY_DISCOVERY_TIME",
//					"2.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP", "3.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME",
//					"4.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE", "5.REACTIVE_PATH_PROVISIONING_TIME",
//					"6.REACTIVE_PATH_PROVISIONING_RATE", "7.PROACTIVE_PATH_PROVISIONING_TIME",
//					"8.PROACTIVE_PATH_PROVISIONING_RATE", "9. Test (Metric 1~6)",
//					"---------------SDN controller should be reboot to test metric 10~16",
//					"10.CONTROL_SESSION_CAPACITY_CCD", "11.NETWORK_DISCOVERY_SIZE_NS",
//					"12.FORWARDING_TABLE_CAPACITY_NRP", "13.*EXCEPTION_HANDLING_SECURITY", "14.*DOS_ATTACKS_SECURITY",
//					"15.*CONTROLLER_FAILOVER_TIME_RELIABILITY",
//					"16.*NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE",
//					};
//
//			public int getSize() {
//				return values.length;
//			}
//
//			public Object getElementAt(int index) {
//				return values[index];
//			}
//		});
		N_metricList.setSelectedIndex(0);

		N_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));

		// 关于run按钮的代码
		N_startButton = new JButton("Run");
		N_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 按键之后开始實行下列代碼

				N_startButton.setEnabled(false);// run按钮不可按
				N_metricList.setEnabled(false);// metric 栏不可选择

				// metric index，获得metric的选择值并赋值给global变数
//				Global.TEST_METRIC = N_metricList.getSelectedIndex();

				String selec_metric = N_metricList.getSelectedValue().toString().split("\\.")[1];
				System.out.println("#### notrhbound metric test   :   " + selec_metric);

				Global.northboundMetric = NorthboundMetric.valueOf(selec_metric);

				// --Benchmark configuration 1
				// OF Version
//				Global.OPENFLOW_VERSION = Global.OF_V_3;
				Global.ofVersion = org.projectfloodlight.openflow.protocol.OFVersion.OF_13;

				// -- Benchmark configuration 2
				// topology type: RING or LINEAR
				Global.topoType = TopologyType.valueOf(N_comboBoxTopoType.getSelectedItem().toString().toUpperCase());
				// --Benchmark configuration 3
				// node 수
				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) N_numberOfS.getText().toString());
				Global.NUMBER_OF_TEST_FLOW = Integer.parseInt((String) N_Flow_Number.getText().toString());
				Global.GUARD_TIME = Integer.parseInt((String)N_GuardTime.getText().toString())+1;
				
				Global.sendType = NorthboundMetric.valueOf(N_sendType.getSelectedItem().toString());
				
				// test time
				Global.TEST_TIME = 5;

				// Benchmark configuration 4
				// Host Ip
//				Global.HOST_IP[0] = Integer.parseInt((String) N_hostIP_1.getText().toString());
//				Global.HOST_IP[1] = Integer.parseInt((String) N_hostIP_2.getText().toString());

				// SDN controller configuration 一
				// Controller Mode （Standalone， Cluster）
//				Global.CONTROLLER_MODE = N_comboBoxControllerMode.getSelectedIndex();
				Global.conMode = ControllerMode
						.valueOf(N_comboBoxControllerMode.getSelectedItem().toString().toUpperCase());
				if (Global.conMode == ControllerMode.STANDALONE) {
					// standalone mode 完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(N_controller1IpPort.getText().toString(), ":");// 分词器，分离ip和port
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 1000;
					// Global.BUFF_SIZE = 1000000;
				} else {
					// cluster mode 未完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(N_controller1IpPort.getText().toString(), ":");
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 200;
					// Global.BUFF_SIZE = 40000;
				}

				// packet loss rate 未完成
				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());

				// SDN controller configuration 二
				// Controller Type
				// CONTROLLER_TYPE_ONOS = 0;
				// CONTROLLER_TYPE_FLOODLIGHT = 1;
				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
//				Global.CONTROLLER_TYPE = N_comboBoxControllerType.getSelectedIndex();
				Global.conTroller = Controller
						.valueOf(N_comboBoxControllerType.getSelectedItem().toString().toUpperCase());

				// host size
				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());

				// Benchmark configuration 5
				// Asynchronous message type
				// unknowns type asynchronous message type
				if (N_packetComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (N_packetComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
				} else if (N_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
				} else {
					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
				}

				// Benchmark configuration 6
				// Unknown packet type （TCP， UDP）
				// provisioning unknow type
				if (N_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (N_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
				} else {
					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
				}

				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());

				// bulk size
				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());

				new Thread(new Runnable() {
					public void run() {
						N_RunButton.go();
					}
				}).start();

			}
		}

		);
		N_startButton.setBounds(457, 258, 93, 23);
		northbound.add(N_startButton);

		JButton btnStop = new JButton("Force Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Initializer.INITIAL_CHANNEL_POOL();
				N_progressBarTotal.setMaximum(0);
				N_progressBarTotal.setValue(0);

				N_progressBar.setMaximum(0);
				N_progressBar.setValue(0);

			}
		});
		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStop.setBounds(801, 258, 93, 23);
		northbound.add(btnStop);

		N_tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		N_tabbedPane.setBounds(41, 344, 992, 310);
		northbound.add(N_tabbedPane);
		JScrollPane scrollPane_3 = new JScrollPane();
		N_tabbedPane.addTab("Result", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
				scrollPane_3, null);

		N_RESULT_TABLE = new JTable();
		N_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		N_RESULT_TABLE.setRowHeight(26);
		N_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		N_RESULT_TABLE.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
						{ "(Pipeline)Proactive Path Provisioning Time", "ms", "", null, null, null},
						{ "Proactive Path Provisioning Rate", "Flows/s", "", null, null, null },
						{ "(Serialize)Proactive Path Provisioning Time", "ms", null, null, null },
						{ "Control Session Capacity", "Sessions", null, null, null, null }, },
						
				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
		N_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
		N_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
		scrollPane_3.setViewportView(N_RESULT_TABLE);

		N_LOG_TEXT = new JTextArea();
		N_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_LOG_TEXT.setLineWrap(true);
		N_TOPO_TEXT = new JTextArea();
		N_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_TOPO_TEXT.setLineWrap(true);

		N_tabbedPane.addTab("Log", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
				new JScrollPane(N_LOG_TEXT), null);

		N_TOPOLOGY_TEXT = new JTextArea();
		N_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_TOPOLOGY_TEXT.setLineWrap(true);

		N_tabbedPane.addTab("Topology", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/content/icons/presentation.png")),
				new JScrollPane(N_TOPO_TEXT), null);

		JLabel lblNewLabel = new JLabel("Metrics");
		//lblNewLabel.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel.setBounds(41, 10, 93, 15);
		northbound.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		//lblNewLabel_1.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblNewLabel_1.setBounds(309, 10, 170, 15);
		northbound.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(309, 186, 724, 67);
		northbound.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Controller Mode");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(48, 10, 88, 20);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("Controller IP");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(48, 40, 88, 18);
		panel.add(lblNewLabel_3);

		N_controller2IpPort = new JTextField();
		N_controller2IpPort.setEditable(false);
		N_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller2IpPort.setAutoscrolls(false);
		N_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller2IpPort.setText("166.104.28.130:6633");
		N_controller2IpPort.setBounds(316, 40, 160, 18);
		panel.add(N_controller2IpPort);

		// first IP
		N_controller1IpPort = new JTextField();
		N_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller1IpPort.setAutoscrolls(false);
		N_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller1IpPort.setText("192.168.1.11:6633");
		N_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller1IpPort.setBounds(146, 40, 160, 18);
		panel.add(N_controller1IpPort);

		N_controller3IpPort = new JTextField();
		N_controller3IpPort.setEditable(false);
		N_controller3IpPort.setText("166.104.28.131:6633");
		N_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller3IpPort.setAutoscrolls(false);
		N_controller3IpPort.setBounds(486, 40, 160, 18);
		panel.add(N_controller3IpPort);

		N_comboBoxControllerMode = new JComboBox();
		N_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
		N_comboBoxControllerMode.setSelectedIndex(0);
		N_comboBoxControllerMode.setBounds(146, 10, 126, 20);
		panel.add(N_comboBoxControllerMode);

		JLabel lblControllerMode = new JLabel("Controller Mode");
		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblControllerMode.setBounds(48, 10, 88, 20);
		// panel.add(lblControllerMode);

		N_comboBoxControllerType = new JComboBox();
		N_comboBoxControllerType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});

		N_comboBoxControllerType.setBounds(520, 10, 126, 20);
		panel.add(N_comboBoxControllerType);
		N_comboBoxControllerType
				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
		N_comboBoxControllerType.setSelectedIndex(0);
		N_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblControllerType = new JLabel("Controller Type");
		lblControllerType.setBounds(422, 10, 88, 20);
		panel.add(lblControllerType);
		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
		//lblNewLabel_2.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_2.setBounds(309, 169, 204, 15);
		northbound.add(lblNewLabel_2);

		N_progressBar = new JProgressBar();
		N_progressBar.setBackground(Color.WHITE);
		N_progressBar.setStringPainted(true);
		N_progressBar.setBounds(460, 287, 573, 13);
		northbound.add(N_progressBar);

		N_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
		N_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
		N_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
		N_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
		N_benchmarkCondition.setBounds(293, 344, 771, 15);
		northbound.add(N_benchmarkCondition);

		N_progressBarTotal = new JProgressBar();
		N_progressBarTotal.setStringPainted(true);
		N_progressBarTotal.setBackground(Color.WHITE);
		N_progressBarTotal.setBounds(460, 321, 573, 13);
		northbound.add(N_progressBarTotal);

		JLabel lblCurrentProgress = new JLabel("Current Progress");
		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblCurrentProgress.setBounds(309, 285, 141, 15);
		northbound.add(lblCurrentProgress);

		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalProgress.setBounds(309, 319, 141, 15);
		northbound.add(lblTotalProgress);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_1.setBounds(309, 32, 724, 125);
		northbound.add(panel_1);

		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
		panel_1.add(lblNumberOfSwitch);

		N_numberOfS = new JTextField();
		N_numberOfS.setText("1");
		N_numberOfS.setMargin(new Insets(3, 3, 3, 3));
		N_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
		N_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_numberOfS.setAutoscrolls(false);
		N_numberOfS.setBounds(191, 40, 79, 18);
		panel_1.add(N_numberOfS);

		N_comboBoxOFProtocolType = new JComboBox();
		N_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
		N_comboBoxOFProtocolType.setSelectedIndex(3);
		N_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
		panel_1.add(N_comboBoxOFProtocolType);

		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblOfProtocolVer.setBounds(10, 10, 100, 20);
		panel_1.add(lblOfProtocolVer);

		N_comboBoxTopoType = new JComboBox();
		N_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
		N_comboBoxTopoType.setSelectedIndex(0);
		N_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxTopoType.setBounds(517, 10, 79, 20);
		panel_1.add(N_comboBoxTopoType);

		JLabel label_4 = new JLabel("Topology Type");
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_4.setBounds(370, 10, 88, 20);
		panel_1.add(label_4);

		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketType.setBounds(10, 69, 171, 20);
		panel_1.add(lblPacketType);

		N_packetComboBox = new JComboBox();
		N_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
		N_packetComboBox.setSelectedIndex(0);
		N_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_packetComboBox.setBounds(191, 68, 79, 20);
		panel_1.add(N_packetComboBox);

		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUnknownPacketType.setBounds(370, 68, 144, 20);
		panel_1.add(lblUnknownPacketType);

		N_provisioningComboBox = new JComboBox();
		N_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
		N_provisioningComboBox.setSelectedIndex(0);
		N_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_provisioningComboBox.setBounds(518, 69, 78, 20);
		panel_1.add(N_provisioningComboBox);
		
				JLabel lblNumberOfFlow = new JLabel("Number of Flows");
				lblNumberOfFlow.setBounds(10, 101, 88, 18);
				panel_1.add(lblNumberOfFlow);
				lblNumberOfFlow.setFont(new Font("Times New Roman", Font.PLAIN, 12));
				
						N_Flow_Number = new JTextField(); // number of flow
						N_Flow_Number.setBounds(191, 101, 79, 18);
						panel_1.add(N_Flow_Number);
						N_Flow_Number.setText("50000");
						N_Flow_Number.setMargin(new Insets(3, 3, 3, 3));
						N_Flow_Number.setHorizontalAlignment(SwingConstants.CENTER);
						N_Flow_Number.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						N_Flow_Number.setBorder(new LineBorder(new Color(0, 0, 0)));
						N_Flow_Number.setAutoscrolls(false);
						
						JLabel lblGuardTime_1 = new JLabel("Guard Time");
						lblGuardTime_1.setForeground(Color.RED);
						lblGuardTime_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						lblGuardTime_1.setBounds(368, 100, 88, 18);
						panel_1.add(lblGuardTime_1);
						
						N_GuardTime = new JTextField();
						N_GuardTime.setText("0");
						N_GuardTime.setMargin(new Insets(3, 3, 3, 3));
						N_GuardTime.setHorizontalAlignment(SwingConstants.CENTER);
						N_GuardTime.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						N_GuardTime.setBorder(new LineBorder(new Color(0, 0, 0)));
						N_GuardTime.setAutoscrolls(false);
						N_GuardTime.setBounds(517, 101, 79, 18);
						panel_1.add(N_GuardTime);
						
						JLabel lblSerializepipeline = new JLabel("Serialize/Pipeline");
						lblSerializepipeline.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						lblSerializepipeline.setBounds(370, 39, 88, 20);
						panel_1.add(lblSerializepipeline);
						
						N_sendType = new JComboBox();
						N_sendType.setModel(new DefaultComboBoxModel(new String[] {"SERIALIZE", "PIPELINE"}));
						N_sendType.setSelectedIndex(0);
						N_sendType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						N_sendType.setBounds(517, 39, 79, 20);
						panel_1.add(N_sendType);

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void snbound(JPanel snbound) {
		
		JScrollPane metricsPane = new JScrollPane();
		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		metricsPane.setAutoscrolls(true);
		metricsPane.setBounds(41, 32, 242, 299);
		// frmMirlabBenchmark.getContentPane().add(scrollPane);
		snbound.add(metricsPane);

		SN_metricList = new JList();
		SN_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
		SN_metricList.setAutoscrolls(false);
		metricsPane.setViewportView(SN_metricList);
		SN_metricList.setModel(new AbstractListModel() {
			String[] values = new String[] { "1.PROACTIVE_PATH_PROVISIONING" ,
					"2.FORWARDING_TABLE_CAPACITY","-----FutureWork-----","1.TOPOLOGY_DISCOVERY_TIME",
					"2.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP", "3.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME",
					"4.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE", "5.REACTIVE_PATH_PROVISIONING_TIME",
					"6.REACTIVE_PATH_PROVISIONING_RATE", "7.PROACTIVE_PATH_PROVISIONING_TIME",
					"8.PROACTIVE_PATH_PROVISIONING_RATE", "9. Test (Metric 1~6)",
					"---------------SDN controller should be reboot to test metric 10~16",
					"10.CONTROL_SESSION_CAPACITY_CCD", "11.NETWORK_DISCOVERY_SIZE_NS",
					"12.FORWARDING_TABLE_CAPACITY_NRP", "13.*EXCEPTION_HANDLING_SECURITY", "14.*DOS_ATTACKS_SECURITY",
					"15.*CONTROLLER_FAILOVER_TIME_RELIABILITY",
					"16.*NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE"};

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		SN_metricList.setSelectedIndex(0);

		SN_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));

		// 关于run按钮的代码
		SN_startButton = new JButton("Run");
		SN_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 按键之后开始實行下列代碼
				SN_startButton.setEnabled(false);// run按钮不可按
				SN_metricList.setEnabled(false);// metric 栏不可选择

				// metric index，获得metric的选择值并赋值给global变数
//				Global.TEST_METRIC = SN_metriclist.
//						SN_metricList.getSelectedIndex();
//				String metric = SN_metricList.getSelectedValue().toString()

				String selec_metric = SN_metricList.getSelectedValue().toString().split("\\.")[1];

				Global.snboundMetric = SNboundMetric.valueOf(selec_metric);

				// --Benchmark configuration 1
				// OF Version
//				Global.OPENFLOW_VERGlobal.OF_V_3;
				Global.ofVersion = org.projectfloodlight.openflow.protocol.OFVersion.OF_13;
				// -- Benchmark cSION = onfiguration 2
//			    OF_10(1), OF_11(2), OF_12(3), OF_13(4), OF_14(5);

				// topology type 圆形或者直线
//				Global.TOPO_TYPE = SN_comboBoxTopoType.getSelectedIndex();
				Global.topoType = TopologyType.valueOf(SN_comboBoxTopoType.getSelectedItem().toString().toUpperCase());
				// --Benchmark configuration 3
				// node # node数量
				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) SN_numberOfS.getText().toString());
				Global.NUMBER_OF_TEST_FLOW = Integer.parseInt((String) SN_Flow_Number.getText().toString());
				Global.GUARD_TIME = Integer.parseInt((String)SN_GuardTime.getText().toString());

				// test time
				Global.TEST_TIME = 5;

				// Benchmark configuration 4
				// Host Ip 
				Global.HOST_IP[0] = Integer.parseInt((String) SN_hostIP_1.getText().toString());
				Global.HOST_IP[1] = Integer.parseInt((String) SN_hostIP_2.getText().toString());

				// SDN controller configuration 一
				// Controller Mode （Standalone， Cluster）
//				Global.CONTROLLER_MODE = SN_comboBoxControllerMode.getSelectedIndex();
				Global.conMode = ControllerMode
						.valueOf(SN_comboBoxControllerMode.getSelectedItem().toString().toUpperCase());
				if (Global.conMode == ControllerMode.STANDALONE) {
					// standalone mode
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(SN_controller1IpPort.getText().toString(), ":");// 分词器，分离ip和port
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 1000;
					// Global.BUFF_SIZE = 1000000;
				} else {
					// cluster mode
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(SN_controller1IpPort.getText().toString(), ":");
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 200;
					// Global.BUFF_SIZE = 40000;
				}

				// packet loss rate 未完成
				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());

				// SDN controller configuration 二
				// Controller Type
				// CONTROLLER_TYPE_ONOS = 0;
				// CONTROLLER_TYPE_FLOODLIGHT = 1;
				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
//				Global.CONTROLLER_TYPE = S_comboBoxControllerType.getSelectedIndex();
				Global.conTroller = Controller
						.valueOf(SN_comboBoxControllerType.getSelectedItem().toString().toUpperCase());

				// host size
				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());

				// Benchmark configuration 5
				// Asynchronous message type
				// unknowns type asynchronous message type
				// unkowns enum
				if (SN_packetComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (SN_packetComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
				} else if (SN_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
				} else {
					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
				}

				// Benchmark configuration 6
				// Unknown packet type （TCP， UDP）
				// provisioning unknow type
				if (SN_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (SN_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
				} else {
					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
				}

				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());

				// bulk size
				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());

				new Thread(new Runnable() {
					public void run() {
						SN_RunButton.go();
					}
				}).start();

			}
		}

		);
		SN_startButton.setBounds(461, 252, 93, 23);
		snbound.add(SN_startButton);

		JButton btnStop = new JButton("Force Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Initializer.INITIAL_CHANNEL_POOL();
				SN_progressBarTotal.setMaximum(0);
				SN_progressBarTotal.setValue(0);

				SN_progressBar.setMaximum(0);
				SN_progressBar.setValue(0);

			}
		});
		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStop.setBounds(805, 252, 93, 23);
		snbound.add(btnStop);

		SN_tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		SN_tabbedPane.setBounds(41, 344, 992, 310);
		snbound.add(SN_tabbedPane);
		JScrollPane scrollPane_3 = new JScrollPane();
		SN_tabbedPane.addTab("Result", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
				scrollPane_3, null);

		SN_RESULT_TABLE = new JTable();
		SN_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		SN_RESULT_TABLE.setRowHeight(26);
		SN_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		SN_RESULT_TABLE.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
						{ "Proactive Path Provisioning Time", "ms", "N/A", null, null, null },
						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", null, null, null },
						{ "Control Session Capacity", "Sessions", null, null, null, null }, 
						{ "Proactive Path Provisioning Time_WrapAround", "ms", null, null, null, null },
						{ "Proactive Path Provisioning Rate_WrapAround", "Flows/s", null, null, null, null },
						{ "Forwaring Table Capacity_WrapAround", "Flows/s", null, null, null, null },
						},
				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
		SN_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
		SN_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
		SN_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
		SN_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
		SN_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
		SN_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
		SN_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
		SN_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
		SN_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
		SN_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
		scrollPane_3.setViewportView(SN_RESULT_TABLE);

		SN_LOG_TEXT = new JTextArea();
		SN_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
		SN_LOG_TEXT.setLineWrap(true);
		SN_TOPO_TEXT = new JTextArea();
		SN_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
		SN_TOPO_TEXT.setLineWrap(true);

		SN_tabbedPane.addTab("Log", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
				new JScrollPane(SN_LOG_TEXT), null);

		SN_TOPOLOGY_TEXT = new JTextArea();
		SN_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
		SN_TOPOLOGY_TEXT.setLineWrap(true);

		SN_tabbedPane.addTab("Topology", null,
				//new ImageIcon(GUI.class.getResource("/com/alee/examples/content/icons/presentation.png")),
				new JScrollPane(SN_TOPO_TEXT), null);

		JLabel lblNewLabel = new JLabel("Metrics");
		//lblNewLabel.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel.setBounds(41, 10, 93, 15);
		snbound.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		//lblNewLabel_1.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblNewLabel_1.setBounds(309, 10, 170, 15);
		snbound.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(316, 178, 686, 62);
		snbound.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Controller Mode");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(48, 10, 88, 20);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("Controller IP");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(48, 40, 88, 18);
		panel.add(lblNewLabel_3);

		SN_controller2IpPort = new JTextField();
		SN_controller2IpPort.setEditable(false);
		SN_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		SN_controller2IpPort.setAutoscrolls(false);
		SN_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
		SN_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_controller2IpPort.setText("166.104.28.130:6633");
		SN_controller2IpPort.setBounds(316, 40, 160, 18);
		panel.add(SN_controller2IpPort);

		// first IP
		SN_controller1IpPort = new JTextField();
		SN_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		SN_controller1IpPort.setAutoscrolls(false);
		SN_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
		SN_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_controller1IpPort.setText("192.168.1.11:6633");
		SN_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_controller1IpPort.setBounds(142, 40, 160, 18);
		panel.add(SN_controller1IpPort);

		SN_controller3IpPort = new JTextField();
		SN_controller3IpPort.setEditable(false);
		SN_controller3IpPort.setText("166.104.28.131:6633");
		SN_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
		SN_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		SN_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_controller3IpPort.setAutoscrolls(false);
		SN_controller3IpPort.setBounds(486, 40, 160, 18);
		panel.add(SN_controller3IpPort);

		SN_comboBoxControllerMode = new JComboBox();
		SN_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
		SN_comboBoxControllerMode.setSelectedIndex(0);
		SN_comboBoxControllerMode.setBounds(146, 10, 126, 20);
		panel.add(SN_comboBoxControllerMode);

		JLabel lblControllerMode = new JLabel("Controller Mode");
		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblControllerMode.setBounds(48, 10, 88, 20);
		// panel.add(lblControllerMode);

		SN_comboBoxControllerType = new JComboBox();
		SN_comboBoxControllerType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				SN_packetComboBox.removeAllItems();
				SN_provisioningComboBox.removeAllItems();

				Controller conValue = Controller
						.valueOf(SN_comboBoxControllerType.getSelectedItem().toString().toUpperCase());

				switch (conValue) {
				case ONOS:

					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
					break;
				case FLOODLIGHT:
					SN_packetComboBox.removeAllItems();
					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
					break;
				case OPENDAYLIGHT:

					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP", "ECHO" }));
					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP" }));
					break;

				}

			}
		});

		SN_comboBoxControllerType.setBounds(520, 10, 126, 20);
		panel.add(SN_comboBoxControllerType);
		SN_comboBoxControllerType
				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
		SN_comboBoxControllerType.setSelectedIndex(0);
		SN_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblControllerType = new JLabel("Controller Type");
		lblControllerType.setBounds(422, 10, 88, 20);
		panel.add(lblControllerType);
		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
		//lblNewLabel_2.setIcon(new ImageIcon(GUI.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_2.setBounds(316, 165, 204, 15);
		snbound.add(lblNewLabel_2);

		SN_progressBar = new JProgressBar();
		SN_progressBar.setBackground(Color.WHITE);
		SN_progressBar.setStringPainted(true);
		SN_progressBar.setBounds(460, 287, 573, 13);
		snbound.add(SN_progressBar);

		SN_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
		SN_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
		SN_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
		SN_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
		SN_benchmarkCondition.setBounds(293, 344, 771, 15);
		snbound.add(SN_benchmarkCondition);

		SN_progressBarTotal = new JProgressBar();
		SN_progressBarTotal.setStringPainted(true);
		SN_progressBarTotal.setBackground(Color.WHITE);
		SN_progressBarTotal.setBounds(460, 321, 573, 13);
		snbound.add(SN_progressBarTotal);

		JLabel lblCurrentProgress = new JLabel("Current Progress");
		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblCurrentProgress.setBounds(309, 285, 141, 15);
		snbound.add(lblCurrentProgress);

		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalProgress.setBounds(309, 319, 141, 15);
		snbound.add(lblTotalProgress);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_1.setBounds(309, 32, 724, 124);
		snbound.add(panel_1);

		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
		panel_1.add(lblNumberOfSwitch);

		SN_numberOfS = new JTextField();
		SN_numberOfS.setText("1");
		SN_numberOfS.setMargin(new Insets(3, 3, 3, 3));
		SN_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
		SN_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_numberOfS.setAutoscrolls(false);
		SN_numberOfS.setBounds(191, 40, 79, 18);
		panel_1.add(SN_numberOfS);

		SN_comboBoxOFProtocolType = new JComboBox();
		SN_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
		SN_comboBoxOFProtocolType.setSelectedIndex(3);
		SN_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
		panel_1.add(SN_comboBoxOFProtocolType);

		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblOfProtocolVer.setBounds(10, 10, 100, 20);
		panel_1.add(lblOfProtocolVer);

		SN_hostIP_1 = new JTextField();
		SN_hostIP_1.setText("10");
		SN_hostIP_1.setMargin(new Insets(3, 3, 3, 3));
		SN_hostIP_1.setHorizontalAlignment(SwingConstants.CENTER);
		SN_hostIP_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_hostIP_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_hostIP_1.setAutoscrolls(false);
		SN_hostIP_1.setBounds(479, 40, 28, 18);
		panel_1.add(SN_hostIP_1);

		JLabel lblHostIp = new JLabel("Host IP start from");
		lblHostIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblHostIp.setBounds(370, 40, 99, 18);
		panel_1.add(lblHostIp);

		SN_hostIP_2 = new JTextField();
		SN_hostIP_2.setText("0");
		SN_hostIP_2.setMargin(new Insets(3, 3, 3, 3));
		SN_hostIP_2.setHorizontalAlignment(SwingConstants.CENTER);
		SN_hostIP_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_hostIP_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_hostIP_2.setAutoscrolls(false);
		SN_hostIP_2.setBounds(530, 40, 28, 18);
		panel_1.add(SN_hostIP_2);

		SN_textField_3 = new JTextField();
		SN_textField_3.setEditable(false);
		SN_textField_3.setText("0");
		SN_textField_3.setMargin(new Insets(3, 3, 3, 3));
		SN_textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		SN_textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_textField_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_textField_3.setAutoscrolls(false);
		SN_textField_3.setBounds(584, 40, 28, 18);
		panel_1.add(SN_textField_3);

		SN_textField_4 = new JTextField();
		SN_textField_4.setEditable(false);
		SN_textField_4.setText("2");
		SN_textField_4.setMargin(new Insets(3, 3, 3, 3));
		SN_textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		SN_textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_textField_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		SN_textField_4.setAutoscrolls(false);
		SN_textField_4.setBounds(638, 40, 28, 18);
		panel_1.add(SN_textField_4);

		JLabel label_1 = new JLabel(".");
		label_1.setBounds(517, 41, 6, 15);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel(".");
		label_2.setBounds(568, 41, 6, 15);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel(".");
		label_3.setBounds(622, 41, 6, 15);
		panel_1.add(label_3);

		SN_comboBoxTopoType = new JComboBox();
		SN_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
		SN_comboBoxTopoType.setSelectedIndex(0);
		SN_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_comboBoxTopoType.setBounds(517, 10, 79, 20);
		panel_1.add(SN_comboBoxTopoType);

		JLabel label_4 = new JLabel("Topology Type");
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_4.setBounds(370, 10, 88, 20);
		panel_1.add(label_4);

		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketType.setBounds(10, 69, 171, 20);
		panel_1.add(lblPacketType);

		SN_packetComboBox = new JComboBox();
		SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
		SN_packetComboBox.setSelectedIndex(0);
		SN_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_packetComboBox.setBounds(191, 68, 79, 20);
		panel_1.add(SN_packetComboBox);

		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUnknownPacketType.setBounds(370, 68, 144, 20);
		panel_1.add(lblUnknownPacketType);

		SN_provisioningComboBox = new JComboBox();
		SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
		SN_provisioningComboBox.setSelectedIndex(0);
		SN_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		SN_provisioningComboBox.setBounds(518, 69, 78, 20);
		panel_1.add(SN_provisioningComboBox);
		
				JLabel lblNumberOfFlow = new JLabel("Number of Flows");
				lblNumberOfFlow.setBounds(14, 101, 88, 18);
				panel_1.add(lblNumberOfFlow);
				lblNumberOfFlow.setFont(new Font("Times New Roman", Font.PLAIN, 12));
				
						SN_Flow_Number = new JTextField(); // number of flow
						SN_Flow_Number.setBounds(191, 100, 79, 18);
						panel_1.add(SN_Flow_Number);
						SN_Flow_Number.setText("100");
						SN_Flow_Number.setMargin(new Insets(3, 3, 3, 3));
						SN_Flow_Number.setHorizontalAlignment(SwingConstants.CENTER);
						SN_Flow_Number.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						SN_Flow_Number.setBorder(new LineBorder(new Color(0, 0, 0)));
						SN_Flow_Number.setAutoscrolls(false);
						
						JLabel lblGuardTime = new JLabel("Guard Time");
						lblGuardTime.setForeground(Color.RED);
						lblGuardTime.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						lblGuardTime.setBounds(370, 101, 88, 18);
						panel_1.add(lblGuardTime);
						
						SN_GuardTime = new JTextField();
						SN_GuardTime.setText("10");
						SN_GuardTime.setMargin(new Insets(3, 3, 3, 3));
						SN_GuardTime.setHorizontalAlignment(SwingConstants.CENTER);
						SN_GuardTime.setFont(new Font("Times New Roman", Font.PLAIN, 12));
						SN_GuardTime.setBorder(new LineBorder(new Color(0, 0, 0)));
						SN_GuardTime.setAutoscrolls(false);
						SN_GuardTime.setBounds(517, 100, 79, 18);
						panel_1.add(SN_GuardTime);

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
