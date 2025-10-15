package cn.com.zc.db.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.zc.db.bean.PetScoreConfig;
import cn.com.zc.db.exception.FileParseException;

public class FileParser {

	public static Map<String, String> parseTable2Walker(String filePath) throws FileParseException {
		Map<String, String> walkerMapping = new LinkedHashMap<String, String>();
		File resource = new File(filePath);
		if (!resource.exists())
			return walkerMapping;

		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(resource));
			while ((line = reader.readLine()) != null) {
				String[] param = line.split("=");
				if (param.length != 2)
					continue;

				String tableName = param[0].trim();
				String objectName = param[1].trim();

				walkerMapping.put(tableName, objectName);
			}

		} catch (FileNotFoundException e) {
			throw new FileParseException("Xdb数据库表名与Object对应关系文件没找到", e);
		} catch (IOException e) {
			throw new FileParseException("Xdb数据库表名与Object对应关系文件解析出错", e);
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				//nothing to do
			}
		}

		return walkerMapping;

	}
	
	public static List<PetScoreConfig> getPetScoreConfig(){
		List<PetScoreConfig> configList = new ArrayList<PetScoreConfig>();
		File configFile = new File("e://petscore.txt");
		if(!configFile.exists()){
			mkdb.Trace.info("宠物评分的配置文件不存在！！！");
			return configList;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(configFile));
			String line;
			while((line = reader.readLine()) != null){
				String[] param = line.trim().split(",");
				if(param.length != 3)
					continue;
				
				PetScoreConfig config = new PetScoreConfig();
				config.setAttrid(Integer.parseInt(param[1]));
				config.setWeight(Double.parseDouble(param[2]));
				configList.add(config);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			mkdb.Trace.error(e.getMessage());
		}
		
		return configList;
	}
	
	
}
