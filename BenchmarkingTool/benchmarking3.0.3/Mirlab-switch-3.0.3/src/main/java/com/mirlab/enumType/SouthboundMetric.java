package com.mirlab.enumType;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月15日 下午3:49:03 类说明
 */
public enum SouthboundMetric {

	TOPOLOGY_DISCOVERY_TIME, TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP, 
	ASYNCHRONOUS_MESSAGE_PROCESSING_TIME,
	ASYNCHRONOUS_MESSAGE_PROCESSING_RATE, 
	REACTIVE_PATH_PROVISIONING_TIME, REACTIVE_PATH_PROVISIONING_RATE,
	PROACTIVE_PATH_PROVISIONING_TIME, PROACTIVE_PATH_PROVISIONING_RATE,
	////
	TEST_TOTAL, 
	CONTROL_SESSION_CAPACITY_CCD, 
	NETWORK_DISCOVERY_SIZE_NS, 
	FORWARDING_TABLE_CAPACITY_NRP,
	EXCEPTION_HANDLING_SECURITY, 
	DOS_ATTACKS_SECURITY, 
	CONTROLLER_FAILOVER_TIME_RELIABILITY, 
	PROVISINIONING_PACKET_TYPE, // where																															// it
	NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE

}
