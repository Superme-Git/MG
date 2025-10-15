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
public class FileUploadWithInfoServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {

	private static final long serialVersionUID = -754289241523209901L;

	private final static String FILE_SEPARATOR = java.io.File.separator;
	private final static String PARAM_ENCODING = "UTF-8";

	public FileUploadWithInfoServlet() {
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

		String encoding = request.getCharacterEncoding();
		if (encoding == null )
		{
			encoding = PARAM_ENCODING;
			request.setCharacterEncoding(encoding);
		}
		LogUtil.logger.info("CharacterEncoding:" + encoding);

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
					} else if ("PhoneNumber".equals(item.getFieldName())) {
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

			//mkdir
			String savePath = null;
			if(relativePath != null && relativePath.trim().length() != 0){
				savePath = UploadConfig.getInstance().getFileAbsPath() + FILE_SEPARATOR + relativePath + FILE_SEPARATOR;
			}else
				savePath = UploadConfig.getInstance().getFileAbsPath() + FILE_SEPARATOR;
			File dir = new File(savePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			e_table.setRelativePath(relativePath);
			
			// set Current datetime
			e_table.setUploadTime(new java.util.Date());

			String fileAbsname = null;
			String fileName = null;
			boolean insertAny = false;
			
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
								+ fileAbsname + ",to=" + savePath + fileName + ",ip="
								+ request.getRemoteAddr());
					}
				}
			}
			
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