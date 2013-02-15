package com.vf.mxbeans;

import com.vf.annotations.*;

@Author("Vitalii Fedoryshyn")
@Version("1.0")
public interface ServerInfoMXBean {

	@DisplayName("GETTER: ServerProperties")
	ServerProperties getServerInfo();

	@DisplayName("OPERATION: restartServer")
	void restartServer();

	@DisplayName("OPERATION: startAdditionalNode")
	void startAdditionalNode();

}
