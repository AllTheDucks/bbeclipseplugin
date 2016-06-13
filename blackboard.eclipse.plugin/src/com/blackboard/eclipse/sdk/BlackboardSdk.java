package com.blackboard.eclipse.sdk;

import java.io.File;
import java.util.ArrayList;

public class BlackboardSdk {

	private String version;
	private String path;
	
	public BlackboardSdk() {
		
	}
	
	public BlackboardSdk(String version, String path) {
		this.version = version;
		this.path = path;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	
	
}
