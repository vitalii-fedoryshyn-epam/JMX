package com.vf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.vf.mbeans.Boring;
import com.vf.mbeans.BoringMBean;
import com.vf.mxbeans.ServerInfo;
import com.vf.mxbeans.ServerInfoMXBean;
import com.vf.mxbeans.ServerProperties;

public class Client1 {

	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		echo("\nDomains:");
		String domains[] = mbsc.getDomains();
		Arrays.sort(domains);
		for (String domain : domains) {
			echo("\tDomain = " + domain);
		}
		waitForEnterPressed();

		echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());
		echo("\nMBean count = " + mbsc.getMBeanCount());
		echo("\nQuery MBeanServer MBeans:");
		Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
		for (ObjectName name : names) {
			echo("\tObjectName = " + name);
		}
		waitForEnterPressed();

		testBoringMBean(mbsc);
		waitForEnterPressed();

		testServerInfoMXBean(mbsc);
		waitForEnterPressed();

		testMemory(mbsc);
		waitForEnterPressed();

		echo("The End.");
		waitForEnterPressed();
	}

	private static void testBoringMBean(MBeanServerConnection mbsc) throws Exception {
		echo("Testing Boring MBean");
		ObjectName mbeanName = new ObjectName("com.vf:type=Boring,name=1");

		BoringMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, BoringMBean.class, true);

		mbeanProxy.setName("Boring New Name");
		echo("Boring gender: " + mbeanProxy.getGender());
		echo("Boring name: " + mbeanProxy.getName());
		mbeanProxy.sayYourName();

		echo("\nCreate new Boring MBean name=2");
		mbeanName = new ObjectName("com.vf:type=Boring,name=2");
		mbsc.createMBean(Boring.class.getCanonicalName(), mbeanName, null, null);

		echo("\nCreate new Boring MBean name=3");
		mbeanName = new ObjectName("com.vf:type=Boring,name=3");
		mbsc.createMBean(Boring.class.getCanonicalName(), mbeanName, null, null);
		echo("\nUnregister Boring MBean name=3");
		mbsc.unregisterMBean(mbeanName);
	}

	private static void testServerInfoMXBean(MBeanServerConnection mbsc) throws Exception {
		echo("Testing ServerInfo MXBean");
		ObjectName mxbeanName = new ObjectName(mbsc.getDefaultDomain() + ":type=ServerInfo,name=1");

		// Access MXBean via proxy
		// ServerInfoMXBean mxbeanProxy = JMX.newMXBeanProxy(mbsc, mxbeanName, ServerInfoMXBean.class);
		// ServerProperties serverProperties = mxbeanProxy.getServerInfo();
		// echo("Server Name: " + serverProperties.getName());
		// echo("Server Nodes: " + serverProperties.getNodes());
		// echo("Server StartUp Date: " + serverProperties.getStartUpDate());

		// Access MXBean directly
		CompositeData queueSample = (CompositeData) mbsc.getAttribute(mxbeanName, "ServerInfo");

		echo("Server Name: " + (String) queueSample.get("name"));
		echo("Server Nodes: " + (Integer) queueSample.get("nodes"));
		echo("Server StartUp Date: " + (Date) queueSample.get("startUpDate"));
		echo("\nCalling Server Restart");
		mbsc.invoke(mxbeanName, "restartServer", null, null);
		echo("\nStart additional node");
		mbsc.invoke(mxbeanName, "startAdditionalNode", null, null);
	}

	private static void testMemory(MBeanServerConnection mbsc) throws Exception {
		echo("\nPrinting Platform MBeans\n");
		{
			echo("Operating System Details");
			ObjectName operatingSystemMXBean = new ObjectName("java.lang:type=OperatingSystem");
			Object systemLoadAverage = mbsc.getAttribute(operatingSystemMXBean, "SystemLoadAverage");

			Long freePhysicalMemory = (Long) mbsc.getAttribute(operatingSystemMXBean, "FreePhysicalMemorySize");
			Long processCpuTime = (Long) mbsc.getAttribute(operatingSystemMXBean, "ProcessCpuTime");
			Long committedVirtualMemorySize = (Long) mbsc.getAttribute(operatingSystemMXBean, "CommittedVirtualMemorySize");
			Long freeSwapSpaceSize = (Long) mbsc.getAttribute(operatingSystemMXBean, "FreeSwapSpaceSize");
			Long totalPhysicalMemorySize = (Long) mbsc.getAttribute(operatingSystemMXBean, "TotalPhysicalMemorySize");
			Long totalSwapSpaceSize = (Long) mbsc.getAttribute(operatingSystemMXBean, "TotalSwapSpaceSize");

			echo("Operating SystemLoadAverage: " + systemLoadAverage);
			echo("Operating System FreePhysicalMemory: " + (freePhysicalMemory / (1024 * 1024)) + "-MB");
			echo("Operating System processCpuTime: " + processCpuTime);
			echo("Operating System committedVirtualMemorySize: " + (committedVirtualMemorySize / (1024 * 1024)) + "-MB");
			echo("Operating System freeSwapSpaceSize: " + (freeSwapSpaceSize / (1024 * 1024)) + "-MB");
			echo("Operating System totalPhysicalMemorySize: " + (totalPhysicalMemorySize / (1024 * 1024)) + "-MB");
			echo("Operating System totalSwapSpaceSize: " + (totalSwapSpaceSize / (1024 * 1024)) + "-MB");
		}
		{
			echo("Heap Memory Usage");
			ObjectName memoryMXBean = new ObjectName("java.lang:type=Memory");
			CompositeDataSupport dataSenders = (CompositeDataSupport) mbsc.getAttribute(memoryMXBean, "HeapMemoryUsage");
			if (dataSenders != null) {
				Long commited = (Long) dataSenders.get("committed");
				Long init = (Long) dataSenders.get("init");
				Long max = (Long) dataSenders.get("max");
				Long used = (Long) dataSenders.get("used");
				Long percentage = ((used * 100) / max);
				echo("nnt commited   : " + commited / (1024 * 1024) + " MB");
				echo("t init       : " + init / (1024 * 1024) + " MB");
				echo("t max        : " + max / (1024 * 1024) + " MB");
				echo("t used       : " + used / (1024 * 1024) + " MB");
				echo("t percentage : " + percentage + " %");
			}
		}
		{
			echo("Non-Heap Memory Usage");
			ObjectName memoryMXBean = new ObjectName("java.lang:type=Memory");
			CompositeDataSupport dataSenders = (CompositeDataSupport) mbsc.getAttribute(memoryMXBean, "NonHeapMemoryUsage");
			if (dataSenders != null) {
				Long commited = (Long) dataSenders.get("committed");
				Long init = (Long) dataSenders.get("init");
				Long max = (Long) dataSenders.get("max");
				Long used = (Long) dataSenders.get("used");
				Long percentage = ((used * 100) / max);
				echo("nnt commited   : " + commited / (1024 * 1024) + " MB");
				echo("t init       : " + init / (1024 * 1024) + " MB");
				echo("t max        : " + max / (1024 * 1024) + " MB");
				echo("t used       : " + used / (1024 * 1024) + " MB");
				echo("t percentage : " + percentage + " %");
			}
		}
	}

	private static void echo(String msg) {
		System.out.println(msg);
	}

	private static void waitForEnterPressed() {
		try {
			echo("\nPress <Enter> to continue...");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
