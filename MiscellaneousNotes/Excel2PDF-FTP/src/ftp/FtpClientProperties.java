package com.jiuqi.budget.electronicarchives.ftp;

import org.apache.commons.net.ftp.FTP;

public class FtpClientProperties {
	/**
     * ftp��ַ
     */
    private String host;

    /**
     * �˿ں�
     */
    private Integer port = 21;

    /**
     * ��¼�û�
     */
    private String username;

	/**
     * ��¼����
     */
    private String password;

    /**
     * ����ģʽ
     */
    private boolean passiveMode = true;

    /**
     * ����
     */
    private String encoding = "UTF-8";

    /**
     * ���ӳ�ʱʱ��(��)
     */
    private Integer connectTimeout;

    /**
     * ���䳬ʱʱ��(��)
     */
    private Integer dataTimeout;

    /**
     * �����С
     */
    private Integer bufferSize = 1024;

    /**
     * ����keepAlive
     * ��λ:��  0����
     * Zero (or less) disables
     */
    private Integer keepAliveTimeout = 0;

    /**
     * �����ļ�����
     * in theory this should not be necessary as servers should default to ASCII
     * but they don't all do so - see NET-500
     */
    private Integer transferFileType = FTP.ASCII_FILE_TYPE;
    
    public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPassiveMode() {
		return passiveMode;
	}

	public void setPassiveMode(boolean passiveMode) {
		this.passiveMode = passiveMode;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getDataTimeout() {
		return dataTimeout;
	}

	public void setDataTimeout(Integer dataTimeout) {
		this.dataTimeout = dataTimeout;
	}

	public Integer getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	public Integer getKeepAliveTimeout() {
		return keepAliveTimeout;
	}

	public void setKeepAliveTimeout(Integer keepAliveTimeout) {
		this.keepAliveTimeout = keepAliveTimeout;
	}

	public Integer getTransferFileType() {
		return transferFileType;
	}

	public void setTransferFileType(Integer transferFileType) {
		this.transferFileType = transferFileType;
	}

}
