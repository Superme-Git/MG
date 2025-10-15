package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.world2.dao.Exceptions;
import com.world2.hql.*;
import com.world2.utils.UploadConfig;
/**
 * Servlet implementation class for Servlet: FileSaveServlet
 * yangming
 *
 */
 public class DeleteFileServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	private static final long serialVersionUID = -77027473587893003L;
   
	public DeleteFileServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchName = request.getParameter("searchName");
		String start = request.getParameter("start");
		String id = request.getParameter("id");
		int int_id = -1;
		try{
			int_id = Integer.valueOf(id);
		}catch(Exception ex){
			int_id = -1;
		}
		Exceptions ex = GameUploadHql.deleteException(int_id);
		if(ex != null){
			if(deleteFile(ex.getUpload()))
				request.setAttribute("tip","É¾³ý³É¹¦");
			else
				request.setAttribute("tip","É¾³ýÎÄ¼þÊ§°Ü");
		}
		else
			request.setAttribute("tip","É¾³ýÊ§°Ü");
		
		
		StringBuilder builder = new StringBuilder();
		if(searchName != null && searchName.trim().length() != 0){
			builder.append("&searchName=");
			builder.append(searchName);
		}
		
		if(start != null){
			builder.append("&start=");
			builder.append(start);
		}
		request.getRequestDispatcher("/fileList?"+builder.toString()).forward(request,response);
		return;
	}
	
	private boolean deleteFile(String fileName){
		String sourceFilePathName = UploadConfig.getInstance().getFileAbsPath() +UploadConfig.FILE_SEPARATOR + fileName;
		File file = new File(sourceFilePathName);
		if(file.exists())
			return file.delete();
		return false;
	}
}