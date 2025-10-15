package mylogic;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;
import xmerge.IPreprocess;

public class RolepkrefreshProcess implements IPreprocess{

	@Override
	public boolean preProcess(Dbx srcdb, Table src_table, Dbx dstdb, Table dst_table) {
		List<Long> srcTableKeys = new ArrayList<Long>();
		List<Long> desTableKeys = new ArrayList<Long>();
		
		walkTable(src_table, srcTableKeys);
		walkTable(dst_table, desTableKeys);
		
		Dbx.Table srcDataTable = srcdb.openTable("rolepkrefresh");
		Dbx.Table dstDataTable = dstdb.openTable("rolepkrefresh");
		
		mkdb.Trace.info("srcDataTable size:"+srcTableKeys.size());
		mkdb.Trace.info("dstDataTable size:"+desTableKeys.size());
		
		for(long rid : srcTableKeys){
			modefyRecord(rid, srcDataTable);
		}
		
		for(long rid : desTableKeys){
			modefyRecord(rid, dstDataTable);
		}
		
		mkdb.Trace.info("rolepkrefreshTable deal ok");		
		return true;
	}
	
	private void walkTable(final Table table,  final List<Long> srcTableKeys) {
		table.walk(
				new mkdb.Storage.IWalk() {
					@Override
					public boolean onRecord(byte[] key, byte[] value) {
//						Object osKey = table.getMetaData().getKeyType().unmarshal(key);
//						Object xbeanValue = table.getMetaData().getValueType().unmarshal(value);
//						try{
//							xbean.RolePKRefreshBean shopBean = (xbean.RolePKRefreshBean)xbeanValue;
//							if(shopBean.getLastrank() > 0){
//								Long roleIdKey = (Long)osKey;
//								srcTableKeys.add(roleIdKey);
//							}
//						}catch (Exception e) {
//							mkdb.Trace.error("遍历离线pk表出错111 " + table.getName() + "出错！！！", e);
//							return false;
//						}
						return true;
					}
				}
		);
	}
	
	private void modefyRecord(long roleId, Dbx.Table rolepkrefreshTable){
//		try {
//			OctetsStream srcOs = rolepkrefreshTable.find(new OctetsStream().marshal(roleId));
//			if(srcOs == null){
//				mkdb.Trace.info("rolepkrefresh roleId: 没有找到数据"+roleId);
//				return ;
//			}
//			xbean.RolePKRefreshBean data = xbean.Pod.newRolePKRefreshBeanData();
//			data.unmarshal(srcOs);
//			data.setLastrank(0);
//			rolepkrefreshTable.replace(new OctetsStream().marshal(roleId), data.marshal(new OctetsStream()));
//		} catch (Exception e) {
//			mkdb.Trace.error("rolepkrefresh error", e);
//			e.printStackTrace();
//		}
	}
}
