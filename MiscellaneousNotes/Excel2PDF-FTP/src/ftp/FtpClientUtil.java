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
	 * 推送文件
	 * @param ftpClient
	 * @param filePath
	 * @param srcFilePath 
	 * @param isDelete
	 * @return
	 */
	public static boolean pushFile(FTPClient ftpClient,String tarfilePath,String srcFilePath, boolean isDelete) {
		if (ftpClient == null) {
			System.out.println("ftp服务器连接失败。");
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
	 * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	 * 
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	private static boolean CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
		boolean success = true;
		String directory = remote + File.separator;
		// 如果远程目录不存在，则递归创建远程服务器目录
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
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * 改变目录路径
	 * 
	 * @param directory
	 * @return
	 */
	private static boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("进入文件夹" + directory + " 成功！");
			} else {
				System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 * @return
	 */
	private static boolean makeDirectory(String dir, FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("创建文件夹" + dir + " 成功！");
			} else {
				System.out.println("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断ftp服务器文件是否存在
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
