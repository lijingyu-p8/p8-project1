package com.jiuqi.budget.electronicarchives.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @author lijingyu
 *
 */
public class FtpClientFactory {
	/**
	 * ����
	 */
	private FtpClientProperties config;

	public FtpClientFactory(FtpClientProperties config) {
		this.config = config;
	}

	/**
	 * ����FtpClient����
	 */
	public FTPClient create() {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding(config.getEncoding());
		try {
			ftpClient.connect(config.getHost(), config.getPort());
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				System.out.println("ftpServer refused connection,replyCode:" + replyCode);
				return null;
			}

			if (!ftpClient.login(config.getUsername(), config.getPassword())) {
				System.out.println("ftpClient login failed... username is {" + config.getUsername() + "}; password: {"
						+ config.getPassword() + "}");
			}

			ftpClient.setBufferSize(config.getBufferSize());
			ftpClient.setFileType(config.getTransferFileType());
			if (config.isPassiveMode()) {
				ftpClient.enterLocalPassiveMode();
			}
		} catch (IOException e) {
			System.out.println("create ftp connection failed...");
			e.printStackTrace();
		}
		return ftpClient;
	}

	/**
	 * ����FtpClient����
	 */
	public void destroyObject(FTPClient ftpClient) {
		try {
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			} else {
				ftpClient.logout();
			}
		} catch (IOException e) {
			if (ftpClient != null) {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			e.printStackTrace();
		}
	}

	/**
	 * �ж������Ƿ����
	 * 
	 * @param ftpClient
	 * @return
	 */
	public boolean isAvailable(FTPClient ftpClient) {
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()) || !ftpClient.isConnected()
				|| !ftpClient.isAvailable()) {
			return false;
		}
		return true;
	}
}
