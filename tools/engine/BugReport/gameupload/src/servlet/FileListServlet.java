package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.locojoy.pager.*;
import com.world2.hql.*;
import com.world2.dao.*;
/**
 * Servlet implementation class for Servlet: FileSaveServlet
 * yangming
 *
 */
 public class FileListServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	private static final long serialVersionUID = -77027473587893003L;
   
	public FileListServlet() {
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
		StringBuilder builder = new StringBuilder();
		if(searchName != null && searchName.trim().length() != 0){
			builder.append("&searchName=");
			builder.append(searchName);
		}
		//分页控制
		PageList pl = new PageList(15,GameUploadHql.getTotalItem()); //每页显示大小和总的记录条数
		pl.setRelativeURI(request.getContextPath()+"/fileList");
		//起始页
		String start = request.getParameter("start");
		
		if (start != null)
			pl.setStart(Integer.parseInt(start));
		
		StringBuffer strBuff = new StringBuffer();
		if (pl.getURIAndParams() != null) {
			strBuff.append(pl.getURIAndParams());
		}
		strBuff.append(builder.toString());
		pl.setURIAndParams(strBuff.toString());
		FilePageManager manager = new FilePageManager(pl);
		List<Exceptions> list = null;
		if(searchName != null && searchName.trim().length() !=0)
			list = manager.getResultSet(searchName);
		else
			list = manager.getResultSet();
		
		request.setAttribute("result",list);
		request.setAttribute("manager", manager);
		request.getRequestDispatcher("/jsp/fileList.jsp").forward(request,response);
		return;
	}
}