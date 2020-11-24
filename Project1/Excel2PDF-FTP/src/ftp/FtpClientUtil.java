package com.jiuqi.budget.electronicarchives.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 
 * @author lijingyu
 *
 */
public class FtpClientUtil {

	/**
	 * �����ļ�
	 * @param ftpClient
	 * @param filePath
	 * @param srcFilePath 
	 * @param isDelete
	 * @return
	 */
	public static boolean pushFile(FTPClient ftpClient,String tarfilePath,String srcFilePath, boolean isDelete) {
		if (ftpClient == null) {
			System.out.println("ftp����������ʧ�ܡ�");
			return false;
		}
		boolean result = true;
		InputStream inputStream = null;
		File file = null;
		try {
			file = new File(srcFilePath);
			inputStream = new FileInputStream(file);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			CreateDirecroty(tarfilePath, ftpClient);
//			ftpClient.sendCommand("OPTS UTF8", "ON");
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(tarfilePath);
			ftpClient.storeFile(file.getName(), inputStream);
			ftpClient.changeToParentDirectory();
			ftpClient.changeToParentDirectory();
			ftpClient.changeToParentDirectory();
			ftpClient.changeToParentDirectory();
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isDelete && file != null) {
				file.delete();
			}
		}
		return result;
	}

	/**
	 * �������Ŀ¼�ļ��������ftp�������Ѵ��ڸ��ļ����򲻴���������ޣ��򴴽�
	 * 
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	private static boolean CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
		boolean success = true;
		String directory = remote + File.separator;
		// ���Զ��Ŀ¼�����ڣ���ݹ鴴��Զ�̷�����Ŀ¼
		if (!directory.equalsIgnoreCase(File.separator) && !changeWorkingDirectory(new String(directory), ftpClient)) {
			int start = 0;
			int end = 0;
			if (directory.startsWith(File.separator)) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf(File.separator, start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = remote.substring(start, end);
				path = path + File.separator + subDirectory;
				if (!existFile(path, ftpClient)) {
					if (makeDirectory(subDirectory, ftpClient)) {
						changeWorkingDirectory(subDirectory, ftpClient);
					} else {
						changeWorkingDirectory(subDirectory, ftpClient);
					}
				} else {
					changeWorkingDirectory(subDirectory, ftpClient);
				}

				paths = paths + File.separator + subDirectory;
				start = end + 1;
				end = directory.indexOf(File.separator, start);
				// �������Ŀ¼�Ƿ񴴽����
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * �ı�Ŀ¼·��
	 * 
	 * @param directory
	 * @return
	 */
	private static boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("�����ļ���" + directory + " �ɹ���");
			} else {
				System.out.println("�����ļ���" + directory + " ʧ�ܣ���ʼ�����ļ���");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param dir
	 * @return
	 */
	private static boolean makeDirectory(String dir, FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("�����ļ���" + dir + " �ɹ���");
			} else {
				System.out.println("�����ļ���" + dir + " ʧ�ܣ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * �ж�ftp�������ļ��Ƿ����
	 * 
	 * @param path
	 * @param ftpClient
	 * @return
	 * @throws IOException
	 */
	private static boolean existFile(String path, FTPClient ftpClient) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}
}
