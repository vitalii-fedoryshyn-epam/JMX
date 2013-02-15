package com.vf.mxbeans;

import java.beans.ConstructorProperties;
import java.util.Date;

public class ServerProperties {

	private final Date startUpDate;
	private final String name;
	private final int nodes;

	@ConstructorProperties({ "startUpDate", "name", "nodes" })
	public ServerProperties(Date startUpDate, String name, int nodes) {
		this.startUpDate = startUpDate;
		this.name = name;
		this.nodes = nodes;
	}

	public Date getStartUpDate() {
		return startUpDate;
	}

	public String getName() {
		return name;
	}

	public int getNodes() {
		return nodes;
	}

}
