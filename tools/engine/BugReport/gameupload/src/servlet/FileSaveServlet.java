package servlet;

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

import com.locojoy.iplimit.IpSpeedCtrl;
import com.world2.dao.Exceptions;
import com.world2.utils.CommonTools;
import com.world2.utils.LogUtil;
import com.world2.utils.UploadConfig;
import com.world2.hql.*;

/**
 * Servlet implementation class for Servlet: FileSaveServlet yangming
 * 
 */
public class FileSaveServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private static final long serialVersionUID = -754289241523209901L;

	private final static String FILE_SEPARATOR = java.io.File.separator;
	
	public FileSaveServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// From Bugreport/PostUp.cpp
		// Content-Type: multipart/form-data; charset=utf-8; boundary=--7d9271\r\n
		//
		// 为Content-Type添加charset属性，这样，服务器端的javax.servlet.ServletRequest.getCharacterEncoding()可以据此确定form-data的编码。
		// 另外，由于历史原因，InfoPart部分的属性名是ANSI编码的，由于没有中文字符，此部分和utf-8兼容，而InfoValue部分则是UTF-16LE编码的。
		// 这个问题留在服务器端处理。
		// liudongxue01238 
		// 2011-07-12
		String encoding = request.getCharacterEncoding();
		if (encoding == null )
		{
			request.setCharacterEncoding("UTF-8");
			LogUtil.logger.info("request.setCharacterEncoding:UTF-8");
			
			// 下面的2行代码可写可不写，参见FileUploadBase.java:
			//  /*  924 */       String charEncoding = FileUploadBase.this.headerEncoding;
			//	/*  925 */       if (charEncoding == null) {
			//	/*  926 */         charEncoding = ctx.getCharacterEncoding();
			upload.setHeaderEncoding("UTF-8");
			LogUtil.logger.info("upload.setHeaderEncoding: UTF-8");
		}
		else
		{
			LogUtil.logger.info("request.getCharacterEncoding:" + encoding);
		}
		
		encoding = "UTF-16LE";
		LogUtil.logger.info("content Character Encoding:" + encoding);

		// Set overall request size constraint
		upload.setSizeMax(1024 * 1024 * 200);
		// Parse the request
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
			
			// get relative path
			Iterator<FileItem> iter1 = items.iterator();
			String relativePath = null;
			Exceptions e_table = new Exceptions();

			String tmp = null;
			while (iter1.hasNext()) {
				FileItem item = iter1.next();
				if (item.isFormField()) {
					if ("RELATIVEPATH".equals(item.getFieldName())) {
						relativePath = item.getString(encoding);
					} else if ("ExceptionCode".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null == tmp) {
							LogUtil.logger
									.error("upload: failed haven't ExceptionCode");
							return;
						}
						e_table.setExceptionCode(tmp);
					} else if ("ExceptionAddress".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null == tmp) {
							LogUtil.logger
									.error("upload: failed haven't ExceptionAddress");
							return;
						}
						e_table.setExceptionAddress(tmp);
					} else if ("StackInfo".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							e_table.setStackInfo(tmp);
						}
					} else if ("OSVersion".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp)
							e_table.setOsVersion(tmp);
					} else if ("Graphic".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp)
							e_table.setGraphic(tmp);
					} else if ("Driver".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp)
							e_table.setDriver(tmp);
					} else if ("Descript".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 1024)
								tmp = tmp.substring(0, 1023);
							e_table.setDescript(tmp);
						}
					} else if ("Register".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 512)
								tmp = tmp.substring(0, 511);
							e_table.setRegister(tmp);
						}
					} else if ("AppVersion".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null == tmp) {
							LogUtil.logger
									.error("upload: failed haven't AppVersion");
							return;
						}
						if (tmp.length() > 128)
							tmp = tmp.substring(0, 127);
						e_table.setAppversion(tmp);
					} else if ("ComputerName".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 128)
								tmp = tmp.substring(0, 127);
							e_table.setComputerName(tmp);
						}
					} else if ("UserName".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 128)
								tmp = tmp.substring(0, 127);
							e_table.setUserName(tmp);
						}
					} else if ("PhoneNo".equalsIgnoreCase(item.getFieldName())
							|| "PhoneNumber".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 128)
								tmp = tmp.substring(0, 127);
							e_table.setPhoneNumber(tmp);
						}
					} else if ("QQorMSN".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 128)
								tmp = tmp.substring(0, 127);
							e_table.setQqOrMsn(tmp);
						}
					} else if ("Email".equals(item.getFieldName())) {
						tmp = item.getString(encoding);
						if (null != tmp) {
							if (tmp.length() > 128)
								tmp = tmp.substring(0, 127);
							e_table.setEmail(tmp);
						}
					}
				}
			}
			
			if (relativePath != null && relativePath.indexOf("..") > -1) {
				LogUtil.logger
						.error("upload: failed, relativePath contians...");
				return;
			}

			// mkdir
			String savePath = null;
			if (relativePath != null && relativePath.trim().length() != 0)
			{
				savePath = UploadConfig.getInstance().getFileAbsPath()+ FILE_SEPARATOR
					+ relativePath + FILE_SEPARATOR;
			}
			else
			{
				savePath = UploadConfig.getInstance().getFileAbsPath() + FILE_SEPARATOR;
			}
			
			File dir = new File(savePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			e_table.setRelativePath(relativePath);

			// set Current datetime
			e_table.setUploadTime(new java.util.Date());

			boolean insertAny = false;
			String fileAbsname = null;
			String fileName = null;
			int posi;
			Iterator<FileItem> iter2 = items.iterator();
			while (iter2.hasNext()) {
				FileItem item = iter2.next();
				if (!item.isFormField()) {
					// get file name
					fileAbsname = item.getName();
					if ((posi = fileAbsname.lastIndexOf("\\")) > -1) {
						fileName = fileAbsname.substring(posi + 1);
					} else if ((posi = fileAbsname.lastIndexOf(FILE_SEPARATOR)) > -1) {
						fileName = fileAbsname.substring(posi + 1);
					} else {
						fileName = fileAbsname;
					}
					int max = GameUploadHql.getMaxID();
					String prefix = null;
					if (max == -1 || max == -2) {
						Calendar cal = Calendar.getInstance();
						prefix = String.valueOf(cal.getTimeInMillis());
					} else
						prefix = String.valueOf(max);
					fileName = prefix + "_" + fileName;

					// write file
					File uploadedFile = new File(savePath + fileName);
					item.write(uploadedFile);

					// 保存数据记录到数据库
					e_table.setUpload(fileName);

					insertAny = true;
					
					if (GameUploadHql.insertNewException(e_table)) {
						LogUtil.logger.info("upload:from=" + fileAbsname
								+ ",to=" + savePath + fileName + ",ip="
								+ request.getRemoteAddr());
					} else {
						LogUtil.logger.error("upload: save file failed ,from="
								+ fileAbsname + ",to=" + savePath + fileName
								+ ",ip=" + request.getRemoteAddr());
					}
				}
			}
			
			// 如果用户没有上传任何文件，只有描述相关的信息，这里对其进行记录。
			if( !insertAny )
			{
				if (GameUploadHql.insertNewException(e_table)){
					LogUtil.logger.info("upload: ip="+ request.getRemoteAddr());
				} else {
					LogUtil.logger.error("upload: insert exception info to table falied. ip=" + request.getRemoteAddr());
				}
			}
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
