package com.locojoy.bugreport.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.world2.utils.DateParser;
import com.world2.utils.LogUtil;
import com.world2.utils.SystemConfUtil;
/**
 * Servlet implementation class for Servlet: FileSaveServlet
 * yangming
 *
 */
 public class FileUploadServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	private static final long serialVersionUID = -754289241523209901L;
	
	private final static String FILE_SEPARATOR = "/"; 

	 public FileUploadServlet() {
		super();
	}   	
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Create a factory for disk-based file items
		
		/*if (!IpSpeedCtrl.getInstance().getUploadSpeedLimit().add(
				CommonTools.getRemoteIp(request))) {
			LogUtil.logger.info("upload:ip="+ CommonTools.getRemoteIp(request)+"upload frequently");
			return;
		}*/
		LogUtil.logger.info("receive request"+ request.getRemoteHost());
		
		DiskFileItemFactory factory = new DiskFileItemFactory();

		//Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		//Set overall request size constraint
		upload.setSizeMax(1024*1024*200);
		//Parse the request
		try {
			List<FileItem> items = upload.parseRequest(request);
			//get relative path
			String fileAbsname = null;
			String fileName = null;
			int posi;
			Iterator<FileItem> iter2 = items.iterator();
	    	String prefix = DateParser.getInstance().getDayTme(DateParser.DATETIME_FORM2, Calendar.getInstance().getTime());
	    	//mkdir
	    	String savePath = SystemConfUtil.getFileAbsPath() + FILE_SEPARATOR;
	    	File dir = new File(savePath);
	    	if (!dir.exists()) {
	    		dir.mkdirs();
	    	}
	    	int k = 0;
			while (iter2.hasNext()) {
			    FileItem item = iter2.next();
			    if (!item.isFormField()) {
			    	//get file name
			    	fileAbsname = item.getName();
			    	if ((posi = fileAbsname.lastIndexOf("\\")) > -1) {
			    		fileName = fileAbsname.substring(posi+1); 
			    	} else if ((posi = fileAbsname.lastIndexOf(FILE_SEPARATOR)) > -1) {
			    		fileName = fileAbsname.substring(posi+1);
			    	} else {
			    		fileName = fileAbsname; 
			    	}
			    	fileName = prefix + "_"+ k++ + "_" + fileName;
			    	//write file
			    	File uploadedFile = new File(savePath + fileName);
			        item.write(uploadedFile);
			        LogUtil.logger.info("save file:" + savePath + fileName);
			    }
			}
			LogUtil.logger.info("upload:from="+ fileAbsname + ",to=" + dir.getAbsolutePath() + FILE_SEPARATOR + fileName + ",ip="+request.getRemoteAddr());
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}