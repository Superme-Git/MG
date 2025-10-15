package cn.com.zc.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;

public class FileManager {

	private static FileManager _instance;
	
	private String fileBasePath = null;
	
	public void setFileBasePath(String path){
		this.fileBasePath = path;
	}
	
	private FileManager(){}
	
	private Map<Integer, List<String>> quickPhotoList = new HashMap<Integer, List<String>>();
	
	public static FileManager getInstance(){
		if(_instance == null){
			_instance = new FileManager();
		}
		
		return _instance;
	}
	
	public void setQuickPhotoRecord(int id, String log){
		if(quickPhotoList.get(id) == null){
			List<String> list = new ArrayList<String>();
			list.add(log);
			quickPhotoList.put(id, list);
		}else{
			quickPhotoList.get(id).add(log);
		}
	}
	
	public void writeQuickPhotoFile(){
		mkdb.Trace.info("正在回写快照……");
		Iterator<Entry<Integer, List<String>>> iterator = quickPhotoList.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, List<String>> currrent = iterator.next();
			try {
				writeQuickPhotoFileById(currrent.getKey(), currrent.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeQuickPhotoFileById(Integer key, List<String> value) throws Exception {
		String fileName = getFileNameByID(key);
		String firstLine = getFileFirstLineByID(key) + "\n";
		
		File dir = new File(getQuickPhotoFilePath());
		if(!dir.exists() || !dir.isDirectory())
			dir.mkdir();
		
		File file = new File(getQuickPhotoFilePath() + fileName);
		
		mkdb.Trace.info("回写文件目录： " + getQuickPhotoFilePath() + fileName);
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(firstLine.toCharArray());
		for(String line : value){
			line = line + "\n";
			writer.write(line.toCharArray());
		}
		
		writer.flush();
		writer.close();
	}

	private String getFileFirstLineByID(Integer key) {
		switch (key) {
		case Constant.ROLE_PROPERTY:
			return Constant.ROLE_PROPERTY_FIRST_LINE;

		case Constant.FAMILY_DETAIL:
			return Constant.FAMILY_DETAIL_FIRST_LINE;
			
		case Constant.FACTION_DETAIL:
			return Constant.FACTION_DETAIL_FIRST_LINE;
			
		case Constant.SHOP_DETAIL:
			return Constant.SHOP_DETAIL_FIRST_LINE;
			
		case Constant.SKILL_DETAIL:
			return Constant.SKILL_DETAIL_FIRST_LINE;
			
		case Constant.ITEM_DETAIL:
			return Constant.ITEM_DETAIL_FIRST_LINE;
			
		case Constant.FU_SHI:
			return Constant.FU_SHI_FIRST_LINE;
			
		case Constant.ROLE_RELATION_PROPERTY:
			return Constant.ROLE_RELATION_PROPERTY_FIRST_LINE;
			
		case Constant.ROLE_XIULIAN_PROPERTY:
			return Constant.ROLE_XIULIAN_PROPERTY_FIRST_LINE;
			
		case Constant.ROLE_EQUIP_DETAIL:
			return Constant.ROLE_EQUIP_DETAIL_FIRST_LINE;
			
		default:
			break;
		}
		return null;
	}

	private String getFileNameByID(Integer key) {
		switch (key) {
		case Constant.ROLE_PROPERTY:
			return "roles.csv.begin";

		case Constant.FAMILY_DETAIL:
			return "familys.csv.begin";
			
		case Constant.FACTION_DETAIL:
			return "factions.csv.begin";
			
		case Constant.SHOP_DETAIL:
			return "shop.csv.begin";
			
		case Constant.SKILL_DETAIL:
			return "skill.csv.begin";
			
		case Constant.ITEM_DETAIL:
			return "items.csv.begin";
			
		case Constant.FU_SHI:
			return "fushi.csv.begin";
			
		case Constant.ROLE_RELATION_PROPERTY:
			return "roles_social.csv.begin";
			
		case Constant.ROLE_XIULIAN_PROPERTY:
			return "xiulian.csv.begin";
			
		case Constant.ROLE_EQUIP_DETAIL:
			return "equips.csv.begin";
			
		default:
			break;
		}
		return null;
	}

	public String getQuickPhotoFilePath(){
		String bakDirName = DbxManager.getInstance().getXdbBakName();
		StringBuffer sb = new StringBuffer();
		sb.append(bakDirName.substring(0, 4) + "-");
		sb.append(bakDirName.substring(4, 6) + "-");
		sb.append(bakDirName.substring(6, 8) + "/");
		
		return fileBasePath + sb.toString();
	}
	
	public String getRankListFilePath(){
		String bakDirName = DbxManager.getInstance().getXdbBakName();
		StringBuffer sb = new StringBuffer();
		sb.append(bakDirName.substring(0, 4) + "-");
		sb.append(bakDirName.substring(4, 6) + "-");
		sb.append(bakDirName.substring(6, 8) + "/");
		return sb.toString();
	}
	
	
	public String getBasePath(){
		return new File(".").getAbsolutePath();
	}
	
}
