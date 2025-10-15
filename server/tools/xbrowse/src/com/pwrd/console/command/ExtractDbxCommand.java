package com.pwrd.console.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.locojoy.base.Marshal.OctetsStream;
import com.pwrd.command.Command;
import com.pwrd.dbx.DbxManager;
import com.pwrd.io.IO;
import com.pwrd.util.Toolkit;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

public class ExtractDbxCommand implements Command {

	@Override
	public void execute(String cmdStr) {
		String[] cmdArray = Toolkit.splitStringBySpace(cmdStr);
		// extractdbx xdb_xml_path new_db_path conditions
		if (cmdArray.length >= 4) {
			if (DbxManager.getInstance().getCurrentDbx() != null) {
				Dbx curDbx = DbxManager.getInstance().getCurrentDbx();
				Map<String, List<String>> lock2Tables = getLock2Tables(cmdArray[1]);
				Dbx newDbx = Dbx.open(new File(cmdArray[2]), DatabaseMetaData.getInstance());
				for (int i = 3; i < cmdArray.length; ++i) {
					String condition = cmdArray[i];
					String[] condPair = condition.split(":");
					assert condPair.length == 2;
					if (lock2Tables.containsKey(condPair[0])) {
						List<String> tableNames = lock2Tables.get(condPair[0]);
						// 如果该锁没有对应的表，则跳过
						if (tableNames == null || tableNames.size() == 0)
							continue;
						// 共享同一把锁的所有表键类型是一致的，随便选一个表得到其键类型即可
						DatabaseMetaData.Type keyType = DatabaseMetaData.getInstance().getTable(tableNames.get(0))
								.getKeyType();
						Object key = null;
						switch (keyType.getTypeId()) {
						case SHORT:
							key = Short.valueOf(condPair[1]);
							break;
						case INT:
							key = Integer.valueOf(condPair[1]);
							break;
						case LONG:
							key = Long.valueOf(condPair[1]);
							break;
						case FLOAT:
							key = Float.valueOf(condPair[1]);
							break;
						case STRING:
							key = condPair[1];
							break;
						default:
							IO.getInstance().println("Unsupported data type as argument: " + keyType);
							break;
						}
						if (key == null)
							break;
						OctetsStream keyStream = keyType.marshal(key);
						for (String tableName : tableNames) {
							if (DatabaseMetaData.getInstance().getTable(tableName) == null)
								continue;
							Dbx.Table curTable = curDbx.openTable(tableName);
							Dbx.Table newTable = newDbx.openTable(tableName);
							OctetsStream valueStream = curTable.find(keyStream);
							if (valueStream != null)
								newTable.insert(keyStream, valueStream);
						}
					}
				}
				newDbx.close();
			} else
				IO.getInstance().println("Please use command to set current dbx");
		} else
			IO.getInstance().println("Insufficient arguments");
	}

	private Map<String, List<String>> getLock2Tables(String xdbXml) {
		Map<String, List<String>> lock2Tables = new HashMap<String, List<String>>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(xdbXml));
			Element xdbElem = doc.getDocumentElement();
			NodeList tableElems = xdbElem.getElementsByTagName("table");
			for (int i = 0; i < tableElems.getLength(); ++i) {
				Element tableElem = Element.class.cast(tableElems.item(i));
				if (tableElem.hasAttribute("lock")) {
					String lockName = tableElem.getAttribute("lock");
					List<String> tableNames = lock2Tables.get(lockName);
					if (tableNames == null) {
						tableNames = new ArrayList<String>();
						lock2Tables.put(lockName, tableNames);
					}
					tableNames.add(tableElem.getAttribute("name"));
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lock2Tables;
	}
}
