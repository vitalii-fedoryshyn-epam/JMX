package com.vf;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.vf.mbeans.Boring;
import com.vf.mxbeans.ServerInfo;

public class Server {

	public static void main(String[] args) throws Exception {
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

		registrateMBeans(mbeanServer);

		System.out.println("Waiting forever...");
		Thread.sleep(Long.MAX_VALUE);
	}

	public static void registrateMBeans(MBeanServer mbeanServer) throws Exception {

		// Create and register the Boring MBean
		String domain = "com.vf";

		String mbeanClassName = "Boring";
		String mbeanObjectNameStr = domain + ":type=" + mbeanClassName + ",name=1";
		ObjectName objectName = new ObjectName(mbeanObjectNameStr);
		Boring boring = new Boring();
		mbeanServer.registerMBean(boring, objectName);

		// Create and register the ServerInfo MXBean
		domain = mbeanServer.getDefaultDomain();
		mbeanClassName = "ServerInfo";
		mbeanObjectNameStr = domain + ":type=" + mbeanClassName + ",name=1";
		objectName = new ObjectName(mbeanObjectNameStr);
		ServerInfo serverInfo = new ServerInfo();
		mbeanServer.registerMBean(serverInfo, objectName);
	}

}
