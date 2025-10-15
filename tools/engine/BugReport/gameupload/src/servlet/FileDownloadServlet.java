package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.world2.utils.UploadConfig;

/**
 * Servlet implementation class for Servlet: FileSaveServlet yangming
 * 
 */
public class FileDownloadServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6669976928665956289L;
	private final static int BLOCK_SIZE = 65000;

	public FileDownloadServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		downloadFile(fileName,response);
	}

	private void downloadFile(String fileName,HttpServletResponse response)throws
			ServletException, IOException {
		if (fileName == null) {
			throw new IllegalArgumentException(String
					.valueOf((new StringBuffer("File '")).append(
							fileName).append("' not found (1040).")));
		}
		if (fileName.equals("")) {
			throw new IllegalArgumentException(String.valueOf((new StringBuffer("File '")).append(
							fileName).append("' not found (1040).")));
		}
		String sourceFilePathName = UploadConfig.getInstance().getFileAbsPath() +UploadConfig.FILE_SEPARATOR + fileName;
		
		if (!isVirtual(sourceFilePathName)) {
			throw new SecurityException("Physical path is denied (1035).");
		}
		
		File file = new java.io.File(sourceFilePathName);
		FileInputStream fileIn = null;
		try{
			fileIn = new FileInputStream(file);
			long fileLen = file.length();
			int readBytes = 0;
			int totalRead = 0;
			byte b[] = new byte[BLOCK_SIZE];
			response.setContentType("application/x-msdownload");
			response.setContentLength((int) fileLen);
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
	
			while ((long) totalRead < fileLen) {
				readBytes = fileIn.read(b, 0, BLOCK_SIZE);
				totalRead += readBytes;
				response.getOutputStream().write(b, 0, readBytes);
			}
		}catch(IOException e){
			throw e;
		}finally{
			if(fileIn != null)
				fileIn.close();
		}
	}
	
	private boolean isVirtual(String pathName) {
			java.io.File virtualFile = new java.io.File(pathName);
			return virtualFile.exists();
	}
}