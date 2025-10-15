package xmerge;

import java.util.HashMap;
import java.util.Map;

import com.locojoy.base.Marshal.OctetsStream;


public class KnownMergers {
	private Map<String, IMerge> stubs = new HashMap<String, IMerge>();
	
	public KnownMergers(){
	}
	
	public void addImerge(String tablename, IMerge merger){
		stubs.put(tablename, merger);
		XmergeConf.getInstance().addTable(tablename, true, merger);
	}
	
	public void removeImerge(String tablename){
		stubs.remove(tablename);
		XmergeConf.getInstance().removeTable(tablename);
	}
	
	public IMerge getImerge(String tablename){
		return stubs.get(tablename);
	}
	
	public boolean doKnownMerge(String tablename, OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception{
		IMerge merger = stubs.get(tablename);
		if(null == merger) return false;
		return merger.merge(src_key, src_value, dest_value);
	}
}
