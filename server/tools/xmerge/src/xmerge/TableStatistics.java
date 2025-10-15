package xmerge;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 统计管理，慢慢扩展
 * <li>三个操作中直接包含一个TableStatistics对象
 * <li>根据需要进行统计:出错计数以及（出错时key的记录）
 * */
public class TableStatistics {
	private Map<String, Statistic> Statistics = new HashMap<String, Statistic>();
	private boolean hasLog;
	
	public TableStatistics() {
		for (mkdb.util.DatabaseMetaData.Table table : mkdb.util.DatabaseMetaData.getInstance().getTables()) 
			addTable(table.getName());
	}
	
	private void addTable(String tablename){
		this.Statistics.put(tablename, new Statistic());
	}
	
	/**
	 * 暂时先提供这几个统计方法，如果不够用了再添加，方法名不合理的再修改
	 * 根据MetaData构造map，如果出现表名关联项不存在的情况，返回null或false或-1... ...
	 * */
	public boolean hasLog(){
		return hasLog;
	}
	
	public long getCount(String tablename){
		Statistic s = Statistics.get(tablename);
		return (null != s) ? s.getCount() : -1L;
	}
	
	public void incrementCount(String tablename){
		Statistic s = Statistics.get(tablename);
		if(null != s)
			s.incrementCount();
		hasLog = true;
	}
	
	public boolean addKey(String tablename, Object key){
		hasLog = true;
		Statistic s = Statistics.get(tablename);
		return (null != s) ? s.addKey(key) : false;
	}
	
	public boolean addKeyValue(String tablename, Object key, String valueName){
		hasLog = true;
		Statistic s = Statistics.get(tablename);
		if(null != s){
			return s.addKeyValue(key, valueName);
		}else{
			return false;
		}
	}
	
	public Collection<Object> getKeyList(String tablename){
		Statistic s = Statistics.get(tablename);
		return (null != s) ? s.getKeyList() : null;
	}
	
	public void clearStatistic(){
		for(Statistic s : Statistics.values())
			s.clear();
		hasLog = false;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		Iterator<Map.Entry<String, Statistic>> it = Statistics.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Statistic> entry = it.next();
			Statistic s = entry.getValue();
			if(s.hasLog){
				sb.append("Table: ").append(entry.getKey()).append("\n");
				sb.append("Statictis: \n").append(entry.getValue().toString()).append("\n");
			}
		}
		return sb.toString();
	}
	/**
	 * 统计信息类
	 * <li>现在只记录操作(出错|异常|重复)次数 以及 需要记录的key(非必须，根据需要记录)
	 * <li>目前看来，errnum和keylist不是直接相关的，比较崩溃...
	 * */
	public class Statistic {
		private AtomicLong count =  new AtomicLong();
		private List<Object> keylist = new ArrayList<Object>();
		private Map<Object, List<String> > keyValueMap = new HashMap<Object, List<String> >();
		public boolean hasLog = false;
		
		public long getCount(){
			return count.get();
		}
		
		public void incrementCount(){
			count.incrementAndGet();
			hasLog = true;
		}
		
		public boolean addKey(Object key){
			hasLog = true;
			return keylist.add(key);
		}
		
		public boolean addKeyValue(Object key, String valueName){
			hasLog = true;
			if(null == keyValueMap.get(key)){
				List<String> l = new ArrayList<String>();
				l.add(valueName);
				return (null == keyValueMap.put(key, l));
			}else{
				List<String> l = keyValueMap.get(key);
				return l.add(valueName);
			}
		}
		
		public List<Object> getKeyList(){
			return Collections.unmodifiableList(keylist);
		}
		
		public List<String> getKeyValueCol(Object key){
			return Collections.unmodifiableList(keyValueMap.get(key));
		}
		
		public void clear(){
			hasLog = false;
			count.set(0L);
			keylist.clear();
			keyValueMap.clear();
		}
		
		@Override
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append("Error count:").append(getCount()).append("\n");
			sb.append("Error keys:").append(getKeyList().toString()).append("\n");
			for(Object key: this.keyValueMap.keySet())
				sb.append("Error keyValues:").append(getKeyValueCol(key)).append("\n");
			return sb.toString();
		}
	}
}

