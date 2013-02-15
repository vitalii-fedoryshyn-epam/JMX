package com.vf.mxbeans;

import java.util.Date;

public class ServerInfo implements ServerInfoMXBean {

	private static final String DEFAULF_NAME = "my cluster";
	private static final int DEFAULF_NODES_NUMBER = 3;

	private Date startUpDate = new Date();
	private String name = DEFAULF_NAME;
	private int nodes = DEFAULF_NODES_NUMBER;

	@Override
	public ServerProperties getServerInfo() {
		return new ServerProperties(startUpDate, name, nodes);
	}

	@Override
	public void restartServer() {
		startUpDate = new Date();
		name = DEFAULF_NAME;
		nodes = DEFAULF_NODES_NUMBER;
	}

	@Override
	public void startAdditionalNode() {
		++nodes;
	}

}
