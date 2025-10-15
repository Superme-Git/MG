package xmerge;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;

public abstract class AbstractSeqKeyPreProcess implements IPreprocess {

	
	private int numTotal;

	protected List<Object> valueList = new ArrayList<Object>();
	
	private void insertToValueList(Object value) throws Exception {
		for(int i=valueList.size()-1; i>=0; i--){
			Object src = valueList.get(i);
			if(compareTo(src, value)){
				valueList.add(i+1, value);
			}else{
				if(i == 0){
					valueList.add(0, value);
				}
			}
		}
	}

	@Override
	public boolean preProcess(Dbx srcdb,Dbx.Table src_table,Dbx dstdb,Dbx.Table dst_table) {
		
		walkTable(src_table);
		
		walkTable(dst_table);
		
		int index = 1;
		for(Object value : valueList){
			src_table.insert(new OctetsStream(index), ((mkdb.Bean)value).marshal(new OctetsStream()));
			index ++;
			if(numTotal != -1 && index >= numTotal){
				break;
			}
		}
		
		return true;
	}
	
	private void walkTable(final Table table) {
		final List<OctetsStream> keyList = new ArrayList<OctetsStream>();
		table.walk(
				new mkdb.Storage.IWalk() {
					
					@Override
					public boolean onRecord(byte[] key, byte[] value) {
						new OctetsStream(new Octets(key));
						Object xbeanValue = table.getMetaData().getValueType().unmarshal(value);
						try{
							insertToValueList(xbeanValue);
						}catch (Exception e) {
							mkdb.Trace.error("遍历数据表：  " + table.getName() + "出错！！！", e);
							return false;
						}
						return true;
					}
				}
				
		);
		
		for(OctetsStream key : keyList){
			table.remove(key);
		}
		
	}
	
	public int getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(int numTotal) {
		this.numTotal = numTotal;
	}

	public abstract boolean compareTo(Object src_value, Object des_value) throws Exception;


}
