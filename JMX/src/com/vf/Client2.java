package com.vf;

import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Client2 {

	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		ObjectName mbeanName = new ObjectName("com.vf:type=Boring,name=1");

		Client2Listener listener = new Client2Listener();
		mbsc.addNotificationListener(mbeanName, listener, null, null);

		System.out.println("Waiting forever...");
		Thread.sleep(Long.MAX_VALUE);
	}

	static class Client2Listener implements NotificationListener {

		@Override
		public void handleNotification(Notification notification, Object handback) {
			System.out.println("\nReceived notification: " + notification);
		}

	}

}
