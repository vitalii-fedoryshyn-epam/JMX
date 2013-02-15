package com.vf.mbeans;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Boring extends NotificationBroadcasterSupport implements
		BoringMBean {

	private static final String BORING_GENDER = "male";
	private static final String BORING_NAME = "Boring Bean";

	private String name = BORING_NAME;

	private long sequenceNumber = 1;

	@Override
	public String getGender() {
		return BORING_GENDER;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;

		Notification n = new AttributeChangeNotification(this,
				sequenceNumber++, System.currentTimeMillis(), "Name changed",
				"Name", "String", oldName, this.name);

		sendNotification(n);
	}

	@Override
	public void sayYourName() {
		System.out.println("Hello world. It's " + name + " calling");
	}

	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };
		String name = AttributeChangeNotification.class.getName();
		String description = "An attribute of this MBean has changed";
		MBeanNotificationInfo info = new MBeanNotificationInfo(types, name,
				description);
		return new MBeanNotificationInfo[] { info };
	}

}
