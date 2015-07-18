package com.mobile.syslogng.monitor;

public class Syslogng {
	
	private String syslogngName;
	
	private String hostName;
	
	private String portNumber;
	
	private String certificateFileName;
	
	private String certificatePassword;
	
	private String key;

	public String getSyslogngName() {
		return syslogngName;
	}

	public void setSyslogngName(String syslogngName) {
		this.syslogngName = syslogngName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getCertificateFileName() {
		return certificateFileName;
	}

	public void setCertificateFileName(String certificateFileName) {
		this.certificateFileName = certificateFileName;
	}

	public String getCertificatePassword() {
		return certificatePassword;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
