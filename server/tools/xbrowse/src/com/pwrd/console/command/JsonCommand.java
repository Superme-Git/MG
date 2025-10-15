package com.pwrd.console.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mkdb.util.Dbx;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import com.pwrd.command.Command;
import com.pwrd.dbx.DbxManager;
import com.pwrd.io.IO;
import com.pwrd.util.Toolkit;
import com.pwrd.xql.XQLSelectStatement.DbxIWalkImpl;

public class JsonCommand implements Command {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(String cmdStr) {
		String[] cmdArray = Toolkit.splitStringBySpace(cmdStr);
		if (cmdArray.length >= 2) {
			
			if (DbxManager.getInstance().getCurrentDbx() == null){
				IO.getInstance().println("Please use command to set current dbx");
				return;
			}

			// json_out_tbl.ini
			File fJsonOutTbl = new File("./json_out_tbl.ini");
			List<String> jsonTblLst = loadJsonOutTblInfo(fJsonOutTbl);

			String filePath = cmdArray[1];
			filePath = checkJsonOutFilePathString(filePath);
			if (!checkJsonOutFile(filePath)) {
				return;
			}
			
			JsonConfig jconfig = new JsonConfig();
			setJsonConfig(jconfig);

			for (String tblName : jsonTblLst) {
				if (tblName.equals("offlinemsg")) {
					continue;
				}
				
				Dbx.Table ttable = DbxManager.getInstance().getCurrentDbx().openTable(tblName);
				DbxIWalkImpl walkImpl = new DbxIWalkImpl(tblName);
				ttable.walk(walkImpl);
				System.out.println("doing " + tblName + " write");
				try {
					Map data = walkImpl.getData();

					File f = new File(filePath + tblName + ".txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(f));
					for (Iterator it = data.entrySet().iterator(); it.hasNext();) {
						Map.Entry record = (Map.Entry) it.next();
						String jsonString = formatLoggerString(record, jconfig);
						writer.write(jsonString + "\r\n");
					}
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				} catch (Exception e) {
					IO.getInstance().println("write " + tblName + " error!!");
					e.printStackTrace();
					continue;
				}
			}
			IO.getInstance().println("Json File Write Finished!!");
		} else
			IO.getInstance().println("Insufficient arguments");
	}

	/**
	 *  修正文件路径
	 * @param filePath
	 * @return
	 */
	private String checkJsonOutFilePathString(String filePath) {
		if (filePath.isEmpty()) {
			filePath = "./jsonOutput/";
		} else {
			filePath = filePath.replace("\\", "/");
			if (!filePath.endsWith("/")) {
				filePath += "/";
			}
		}
		return filePath;
	}

	/**
	 * json输出目录相关处理
	 * @param filePath
	 * @return
	 */
	private boolean checkJsonOutFile(String filePath) {
		File fDir = new File(filePath);
		if (!fDir.isDirectory() || !fDir.exists()) {
			if (!fDir.mkdirs()) {
				IO.getInstance().println("JSON,创建目录失败");
				return false;
			}
		}

		File[] listDeleteFile = fDir.listFiles();
		if (listDeleteFile.length > 0) {
			try {
				System.out.println("将删除目录" + filePath + "内所有内容,是否继续(Y/N)");
				byte[] rst = new byte[64];
				System.in.read(rst);
				String resu = new String(rst).toLowerCase();
				if (resu.startsWith("n") || resu.startsWith("0")) {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		for (File fDel : fDir.listFiles()) {
			if (!fDel.delete()) {
				IO.getInstance().println("JSON,删除目录" + filePath + "内容失败");
				return false;
			}
		}

		return true;
	}

	/**
	 * 加载json导出表配置
	 * @param fJsonOutTbl
	 * @return
	 */
	@SuppressWarnings("resource")
	private List<String> loadJsonOutTblInfo(File fJsonOutTbl) {
		if (null == fJsonOutTbl) {
			IO.getInstance().println("fail to load ./json_out_tbl.ini");
			return null;
		}

		List<String> ret = new ArrayList<>();
		try {
			if (fJsonOutTbl.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(fJsonOutTbl));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.length() <= 0)
						continue;
					ret.add(line.trim());
				}
			} else {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fJsonOutTbl));
				Dbx curDbx = DbxManager.getInstance().getCurrentDbx();
				for (mkdb.util.DatabaseMetaData.Table table : curDbx.getMetaData().getTables()) {
					if(table.getName() == null) 
						continue;
					
					String tblName = table.getName().trim();
					if(!tblName.isEmpty()){
						ret.add(tblName);
						writer.write(tblName + "\r\n");
					}
				}
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	/**
	 * 设置JsonConfig
	 * @param jconfig
	 */
	private void setJsonConfig(JsonConfig jconfig) {
		// jconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// jconfig.setIgnoreDefaultExcludes(true);
		jconfig.setAllowNonStringKeys(true);
		jconfig.registerJsonValueProcessor(com.locojoy.base.Octets.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
				return "";
			}

			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return "";
			}
		});

		jconfig.registerJsonValueProcessor(com.locojoy.base.Marshal.Marshal.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
				return "";
			}

			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return "";
			}
		});
	}

	/**
	 * 格式转换
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatLoggerString(Object obj, JsonConfig jsonConfig) {
		try {
			return JSONObject.fromObject(obj, jsonConfig).toString();
		} catch (net.sf.json.JSONException e) {
			return obj.toString();
		} catch (Exception e) {
			System.out.println("Json格式转换错误!!!");
		}
		return "";
	}
}
