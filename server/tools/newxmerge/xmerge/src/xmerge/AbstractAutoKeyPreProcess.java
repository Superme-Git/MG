package xmerge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

public abstract class AbstractAutoKeyPreProcess implements IPreprocess {
	
	private int numTotal;

	protected List<XBeanValueWrap> valueList = new ArrayList<XBeanValueWrap>();
	
	protected Map<Integer, Long> id2Key = new HashMap<Integer, Long>();
	
	private void insertToValueList(XBeanValueWrap value) throws Exception {
		if(valueList.size() == 0){
			valueList.add(value);
			return;
		}
		
		for(int i=valueList.size()-1; i>=0; i--){
			Object src = valueList.get(i).getValue();
			if(!compareTo(src, value.getValue())){
				valueList.add(i+1, value);
				break;
			}else{
				if(i == 0){
					valueList.add(0, value);
				}
			}
		}
	}

	@Override
	public boolean preProcess(Dbx srcdb,Dbx.Table src_table,Dbx dstdb,Dbx.Table dst_table) {
		
		walkTable(src_table, true);
		
		walkTable(dst_table, false);
		
		int index = 0;
		Map<Long, Integer> key2Id = null;
		if(getClass().getName().contains("ClanInfoPreProcess"))
			key2Id = XmergeConf.getInstance().facKey2Id;
		else
			key2Id = XmergeConf.getInstance().famKey2Id;
		for(int i=valueList.size()-1; i>=0; i--){
			XBeanValueWrap value = valueList.get(i);
			changeXBeanValueID(value.getValue(), index+1);
			id2Key.put(index+1, getKey(value.getValue()));
			//mkdb.Trace.error(index+1);
			//mkdb.Trace.error("id : " + (index+1) + "----- key" + getKey(value.getValue()));
			key2Id.put(getKey(value.getValue()), index+1);
			
			if(value.isSrc()){
				src_table.replace(value.getKey(), ((mkdb.Bean)value.getValue()).marshal(new OctetsStream()));
			}else{
				dst_table.replace(value.getKey(), ((mkdb.Bean)value.getValue()).marshal(new OctetsStream()));
			}
			index ++;
			if(numTotal != -1 && index >= numTotal){
				break;
			}
		}
		
		updateId2Key(srcdb, dstdb);
		
		return true;
	}
	
	public abstract  Long getKey(Object value);

	public abstract void changeXBeanValueID(Object xbeanValue, int id);
	
	public abstract void updateId2Key(Dbx srcdb, Dbx dstdb);
	
	private void walkTable(final Table table, final boolean isSrc) {
		
		table.walk(
				new mkdb.Storage.IWalk() {
					
					@Override
					public boolean onRecord(byte[] key, byte[] value) {
						OctetsStream osKey = new OctetsStream(new Octets(key));
						Object xbeanValue = table.getMetaData().getValueType().unmarshal(value);
						try{
							insertToValueList(new XBeanValueWrap(xbeanValue, isSrc, osKey));
						}catch (Exception e) {
							mkdb.Trace.error("遍历数据表：  " + table.getName() + "出错！！！", e);
							return false;
						}
						return true;
					}
				}
				
		);
		
	}
	
	public int getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(int numTotal) {
		this.numTotal = numTotal;
	}

	public abstract boolean compareTo(Object src_value, Object des_value) throws Exception;
	
	class XBeanValueWrap{

		private Object value;
		private boolean isSrc;
		private OctetsStream key;
		
		public XBeanValueWrap(Object value, boolean isSrc, OctetsStream key) {
			this.value = value;
			this.isSrc = isSrc;
			this.key = key;
		}
		
		public Object getValue() {
			return value;
		}
		public boolean isSrc() {
			return isSrc;
		}
		public OctetsStream getKey() {
			return key;
		}
		
	}
	
	

}
