package com.mirlab.component;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version ����ʱ�䣺2017��12��27�� ����6:41:54 ��˵��
 */
public class Host {
	private String ip;
	private String mac;

	private Port belong2Port;
	private Agent connectedAgent;

	public Host(String ip, String mac) {
		this.ip = ip;
		this.mac = mac;
	}

	public Agent getConnectedAgent() {
		return connectedAgent;
	}

	public void setConnectedAgent(Agent connectedAgent) {
		this.connectedAgent = connectedAgent;
	}

	public String getMac() {
		return mac;
	}

	public String getIp() {
		return ip;
	}

	public Port getBelong2Port() {
		return belong2Port;
	}

	public void setBelong2Port(Port belong2Port) {
		this.belong2Port = belong2Port;
	}

	public void sendARPrequest() {

	}

	public void sendARPreply() {

	}
}
