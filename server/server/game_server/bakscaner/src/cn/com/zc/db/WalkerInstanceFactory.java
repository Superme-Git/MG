package cn.com.zc.db;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mkdb.Storage.IWalk;
import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;
import cn.com.zc.db.exception.FileParseException;
import cn.com.zc.db.util.FileParser;
import cn.com.zc.db.walker.Walker;

public class WalkerInstanceFactory {
	
	private static WalkerInstanceFactory _factory;
	
	private WalkerInstanceFactory(){}
	
	public static WalkerInstanceFactory getFactory(){
		synchronized(WalkerInstanceFactory.class){
			if(null == _factory){
				_factory = new WalkerInstanceFactory();
			}
			
			return _factory;
		}
	}
	
	public boolean createWalkerList(Dbx dbx, Map<Table, IWalk> walkerMap, int type, String path){
		Map<String, String> config = getTableWalkerConfigByType(path, type);
		
		if(null == config ||config.size() == 0)
			return false;
		
		Iterator<Entry<String, String>> iterator = config.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> current = iterator.next();
			Table table = dbx.openTable(current.getKey());
			IWalk walk = null;
			try {
				walk = getWalkByName(current.getValue(), current.getKey(), dbx);
			} catch (Exception e) {
				e.printStackTrace();
				mkdb.Trace.error("Walk对象发射生成错误", e);
			}
			((Walker)walk).setQueryType(type);
			walkerMap.put(table, walk);
		}
		
		return true;
	}

	private Map<String, String> getTableWalkerConfigByType(String filePath, int type){
		//有序
		Map<String, String> config = new LinkedHashMap<String, String>();
		if(Constant.ALL_TYPE == type){
			try {
				return FileParser.parseTable2Walker(filePath);
			} catch (FileParseException e) {
				e.printStackTrace();
				mkdb.Trace.error(e.getMessage(), e);
			}
		}else if((type & Constant.SORT_MONEY) == Constant.SORT_MONEY){
			config.put("properties", "cn.com.zc.db.walker.RolePropertyWalker");
		}else if((type & Constant.SORT_ROLE_LEVEL) == Constant.SORT_ROLE_LEVEL){
			config.put("properties", "cn.com.zc.db.walker.RolePropertyWalker");
		}else if((type & Constant.STATE_LOG) == Constant.STATE_LOG){
			config.put("activeusertable", "cn.com.zc.db.walker.ActiveUserWalker");
			config.put("activeroletable", "cn.com.zc.db.walker.ActiveRoleWalker");
		}else if((type & Constant.QUICK_PHOTO) == Constant.QUICK_PHOTO){
			config.put("properties", "cn.com.zc.db.walker.RolePropertyWalker");
			config.put("families", "cn.com.zc.db.walker.FamilyDetailWalker");
			config.put("factions", "cn.com.zc.db.walker.FactionDetailWalker");
			config.put("skillroles", "cn.com.zc.db.walker.SkillDetailWalker");
		}else if((type & Constant.SORT_ROSE_NUM) == Constant.SORT_ROSE_NUM){
			config.put("roserank", "cn.com.zc.db.walker.RoseRankWalker");
		}
		
		return config;
	}
	
	@SuppressWarnings("unchecked")
	private IWalk getWalkByName(String objectName, String tableName, Dbx dbx) throws Exception {
		DatabaseMetaData.Table tableMetaData = dbx.getMetaData().getTable(tableName);
		//生成构造函数
		Constructor<? extends Walker> con = (Constructor<? extends Walker>) Class
			.forName(objectName).getConstructor(mkdb.util.DatabaseMetaData.Table.class);
		
		Walker walkerInstance = con.newInstance(tableMetaData);
		
		return walkerInstance;
	}

	

}
