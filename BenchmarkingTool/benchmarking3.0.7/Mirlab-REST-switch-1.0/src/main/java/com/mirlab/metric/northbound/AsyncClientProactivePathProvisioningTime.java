package com.mirlab.metric.northbound;

import java.io.UnsupportedEncodingException;


import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.component.Node;
import com.mirlab.enumType.NorthboundMetric;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Result;
import com.mirlab.metric.northbound2.SsyncProactiveProTime;
import com.mirlab.metric.wraparound.AsyncClientProactiveProTimeThread_WR;

public class AsyncClientProactivePathProvisioningTime {

	private static final Logger logger = LoggerFactory
			.getLogger(AsyncClientProactivePathProvisioningTime.class.getName());
	public static ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool()));

	private String IP;
	private int port;

	public AsyncClientProactivePathProvisioningTime agent;

	public static String dstPrimaryKey = null;
	public static String srcPrimaryKey = null;

	public static long startTime = 0;
	public static long lastTime = 0;
	public static long total = 0;

	public static ConcurrentHashMap<String, Long> srcTimeMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Long> avgMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, String> srcMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, String> dstMap = new ConcurrentHashMap<>();

	public static String onosIP = null; // server IP
	public static int flowNum = 0; // flow number in test

	
	//GUI result tap 
	public static DecimalFormat df = new DecimalFormat("#0.00");
	static double e = 0;

	public AsyncClientProactivePathProvisioningTime() {

	}

	public static void calTime(String index, Long arrTime) {

		avgMap.put(index, arrTime 
				- srcTimeMap.get(index));
		System.out.println("Time : " + (arrTime - srcTimeMap.get(index)));
		total += arrTime - srcTimeMap.get(index);
	}

	public void setRestClient(AsyncClientProactivePathProvisioningTime agent) {

		System.out.println("### Proactive Path Provisioing TEST ");
		System.out.println("### measure part : " + Global.northORwrap);

		if (Global.sendType == NorthboundMetric.SERIALIZE) {
		} else {
			System.out.println("########################");
			agent.AsyncProactiveProPathTIme_start();

		}

	}

	public void AsyncProactiveProPathTIme_start() {

		ChannelFuture channelFuture = null;

		try {
			System.out.println("## TEST TEST TEST ");

			bootstrap.setOption("tcpNoDelay", true);// set tcpNodelay
			bootstrap.setOption("keepAlive", true);// set tcp option is that keep alive
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", 1000 * 10);// timeout 10s

			bootstrap.setPipelineFactory(new PiplineFactory(new BaseHandler(agent))); // If you want to cluster mode you

			onosIP = Global.SDN_CONTROLLER_IP[0];// make address more ,
			SocketAddress address = new InetSocketAddress(onosIP, 8181);

			flowNum = Global.NUMBER_OF_TEST_FLOW;

			for (int i = 0; i < flowNum; i++) {

//				if (i % 100 == 0) {
//
//					Thread.sleep(1000);
//
//				}
//				

				channelFuture = bootstrap.connect(address).sync();// connect to server
				channelFuture.awaitUninterruptibly();

				if (channelFuture.isSuccess()) {

					HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
							"/onos/v1/flows/of%3A0000000000000001?appId=org.onosproject.testfwd");
					String encoding = null;
					try {
						encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					request.headers().add(HttpHeaders.Names.AUTHORIZATION, "Basic " + encoding);
					request.setProtocolVersion(HttpVersion.HTTP_1_1);
					request.headers().add(HttpHeaders.Names.CONTENT_TYPE, "application/json");
					request.headers().add(HttpHeaders.Names.CONNECTION, "keep-alive");
					request.headers().add(HttpHeaders.Names.HOST, i);
					request.headers().add(HttpHeaders.Names.USER_AGENT, "mir-agent");

					String parseJson = new String(makeJsonMsg().toString());

					ChannelBuffer buffer = ChannelBuffers.copiedBuffer(parseJson, CharsetUtil.UTF_8);
					request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buffer.readableBytes());
					request.setContent(buffer);

					// measure a start-time of postReqeust that from rest-agent to onos

					startTime = System.nanoTime() / 1000000;

					srcTimeMap.put(String.valueOf(i), startTime);

					if (channelFuture.getChannel().isWritable()) {

						
						channelFuture.getChannel().write(request);
						Thread.sleep(0, 33333);
					
					}

				} else {

					bootstrap.releaseExternalResources();

					channelFuture.cancel();
				}
			}

			Thread.sleep(3000);

			System.out.println("###TOTAL TIME : " + total);
			System.out.println("###AVG TIME : " + total / avgMap.size());

			Log.WR_ADD_LOG_PANEL("###Proactive Path Provisioning TEST : ",
					AsyncClientProactivePathProvisioningTime.class.getSimpleName());
			Log.WR_ADD_LOG_PANEL("###Total Flow num " + flowNum,
					AsyncClientProactivePathProvisioningTime.class.getSimpleName());
			Log.WR_ADD_LOG_PANEL("###Number of responses to the request : " + srcTimeMap.size(),
					AsyncClientProactivePathProvisioningTime.class.getSimpleName());
			Log.WR_ADD_LOG_PANEL("###Avg Time : " + total / avgMap.size(),
					AsyncClientProactivePathProvisioningTime.class.getSimpleName());
			
			
//			//Result Tap 
//			e =  Math.round((total / (total / avgMap.size()))*100)/100.0;
//
//			Log.N_ADD_LOG_PANEL("######### [AVG] (Pipeline)Provisioning Time   :    "+String.valueOf(e), SsyncProactiveProTime.class.getSimpleName());;
//			Result.N_ADD_RESULT(df.format(e), 7, 2);
//			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {
			if (channelFuture == null) {

				bootstrap.releaseExternalResources();

			} else {

			}
		}
	}

	public JSONObject makeJsonMsg() throws JSONException {

		JSONObject jsobjpop = new JSONObject();
		jsobjpop.put("appId", "org.onosproject.testfwd");
		jsobjpop.put("groupId", 0);
		jsobjpop.put("priority", 2); // changee
		jsobjpop.put("timeout", 10000);
		jsobjpop.put("deviceId", "of:0000000000000001");// deviceID
		jsobjpop.put("state", "ADDED");
		jsobjpop.put("life", 9069);
		jsobjpop.put("isPermanent", "false");
		jsobjpop.put("packets", 18129);
		jsobjpop.put("bytes", 1776642);
		JSONArray popinstructions = new JSONArray();

		JSONObject popinstructionsObj = new JSONObject();
		popinstructionsObj.put("type", "OUTPUT");
		popinstructionsObj.put("port", 2); // portNum
		popinstructions.put(0, popinstructionsObj);
		JSONArray popdeferred = new JSONArray();
		JSONObject poptreatment = new JSONObject();
		poptreatment.put("instructions", popinstructions);
		poptreatment.put("deferred", popdeferred);
		jsobjpop.put("treatment", poptreatment);

		JSONArray popcriteria = new JSONArray();

		JSONObject popcriteria1 = new JSONObject();
		popcriteria1.put("type", "ETH_DST");
		popcriteria1.put("mac", setDst()); // DST addr
		popcriteria.put(popcriteria1);
		//
		JSONObject popcriteria2 = new JSONObject();
		popcriteria2.put("type", "ETH_SRC");
		popcriteria2.put("mac", setSrc()); // SRC addr
		popcriteria.put(popcriteria2);

		JSONObject popcriteria3 = new JSONObject();
		popcriteria3.put("type", "IN_PORT");
		popcriteria3.put("port", 3); //
		popcriteria.put(popcriteria3);

		JSONObject popselectorCont = new JSONObject();
		popselectorCont.put("criteria", popcriteria);
		jsobjpop.put("selector", popselectorCont);

//		request.setEntity(s);

		return jsobjpop;

	}

	public static void setSrcKey(String key) {
		srcPrimaryKey = key;
	}

	public static void setDstKey(String key) {
		dstPrimaryKey = key;
	}

	public static String setDst() {

		String dst = null;

		String random1 = String.valueOf((int) (Math.random() * 10));
		String random2 = String.valueOf((int) (Math.random() * 10));
		String random3 = String.valueOf((int) (Math.random() * 10));
		String random4 = String.valueOf((int) (Math.random() * 10));
		String random5 = String.valueOf((int) (Math.random() * 10));

		String key = random1 + random2 + random3 + random4 + random5;
		if (dstMap.size() == 0) {

			dst = "4E:1F:61:B" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			dstMap.put(dst, dst);

			return dst;

		} else if (dstMap.containsKey(key)) {

			setDst();

		} else {

			dst = "4E:1F:61:B" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			dstMap.put(dst, dst);

			return dst;
		}

		String notMeanValue = "52:91:81:26:2A:01";

		return notMeanValue;

	}

	public static String setSrc() {
		
		String src = null;

		String random1 = String.valueOf((int) (Math.random() * 10));
		String random2 = String.valueOf((int) (Math.random() * 10));
		String random3 = String.valueOf((int) (Math.random() * 10));
		String random4 = String.valueOf((int) (Math.random() * 10));
		String random5 = String.valueOf((int) (Math.random() * 10));

		String key = random1 + random2 + random3 + random4 + random5;

		if (srcMap.size() == 0) {
			src = "52:91:81:2" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			srcMap.put(src, src);

			return src;

		} else if (srcMap.containsKey(key)) {

			setDst();

		} else {

			src = "52:91:81:2" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			srcMap.put(src, src);

			return src;
		}

		String notMeanValue = "52:91:81:26:2A:01";

		return notMeanValue;

	}

	public void close() {

		bootstrap.releaseExternalResources();

	}

	public void init_NorthBound() {

		srcTimeMap = new ConcurrentHashMap<>();
		avgMap = new ConcurrentHashMap<>();
		srcMap = new ConcurrentHashMap<>();
		dstMap = new ConcurrentHashMap<>();
		flowNum = 0;
		total = 0;

	}
}
