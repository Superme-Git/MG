package com.locojoy.mini.mt3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtils {
	private FTPClient ftpClient = null;
	private static FTPUtils ftpUtilsInstance = null;
	private String FTPUrl;
	private int FTPPort;
	private String UserName;
	private String UserPassword;
	
	/**
	 * 获取单例
	 */
	public static FTPUtils getInstance() {
		if (ftpUtilsInstance == null) {
			ftpUtilsInstance = new FTPUtils();
		}
		return ftpUtilsInstance;
	}
	
	/**
	 * 删除单例
	 */
	public static void destroyInstance() {
		ftpUtilsInstance = null;
	}

	private FTPUtils()
	{
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.enterLocalPassiveMode();
		try {
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置登录信息
	 */
	public void setLoginInfo(String FTPUrl, int FTPPort, String UserName, String UserPassword) {
		this.FTPUrl = FTPUrl;
		this.FTPPort = FTPPort;
		this.UserName = UserName;
		this.UserPassword = UserPassword;
	}
	
	/**
	 * 打开FTP服务
	 */
	public boolean openConnect() {
		try {
			// 连接至服务器
			ftpClient.connect(this.FTPUrl, this.FTPPort);
			
			// 获取响应值
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			
			// 登录到服务器
			ftpClient.login(this.UserName, this.UserPassword);
			
			// 获取响应值
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 关闭FTP服务.
	 * 
	 * @throws IOException
	 */
	public void closeConnect() {
		try {
			ftpClient.logout();
			ftpClient.disconnect();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 * @param localFilePath	本地文件夹
	 * @param suffix		待上传的文件后缀
	 * @param serverFilePath	服务器文件夹
	 */
	public String uploadFile(String localFilePath, String suffix, String serverFilePath) {
		// 上传成功的dump文件的名称
		String dumpFileName = "";
		
		try {
			// 设置文件传输模式
			ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 改变FTP目录
			ftpClient.changeWorkingDirectory(serverFilePath);

			File file = new File(localFilePath);
			File fileList[] = file.listFiles();
			for (File f : fileList) {
				String fileName = f.getName();
				if (suffix.equals("") || fileName.endsWith(suffix)) {
					// 文件上传
					FileInputStream fileInputStream = new FileInputStream(localFilePath + "/" + fileName);
					ftpClient.storeFile(fileName, fileInputStream);

					// 关闭文件流
					fileInputStream.close();
					
					// 记录最后一个上传成功的dump文件的名
					dumpFileName = fileName;
					
					// 删除已经上传的文件
					f.delete();
				}
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return dumpFileName;
	}
	
	/**
	 * 上传单个文件
	 */
	public boolean uploadSingleFile(String localFile, String serverFileName, String serverFilePath) {
		try {
			// 设置文件传输模式
			ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 改变FTP目录
			ftpClient.changeWorkingDirectory(serverFilePath);
			
			// 文件上传
			FileInputStream fileInputStream = new FileInputStream(localFile);
			ftpClient.storeFile(serverFileName, fileInputStream);

			// 关闭文件流
			fileInputStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
  
	/**
	 * 下载文件
	 * @param FilePath  要存放的文件的路径
	 * @param FileName   远程FTP服务器上的那个文件的名字
	 * @return   true为成功，false为失败
	 */
	public boolean downLoadFile(String FilePath, String FileName) {
		try {
			// 转到指定下载目录
			ftpClient.changeWorkingDirectory("/data");

			// 列出该目录下所有文件
			FTPFile[] files = ftpClient.listFiles();

			// 遍历所有文件，找到指定的文件
			for (FTPFile file : files) {
				if (file.getName().equals(FileName)) {
					//根据绝对路径初始化文件
					File localFile = new File(FilePath);

					// 输出流
					OutputStream outputStream = new FileOutputStream(localFile);
          
					// 下载文件
					ftpClient.retrieveFile(file.getName(), outputStream);
          
					//关闭流
					outputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
