package mylogic;

import mkdb.util.Dbx;
import xmerge.AbstractAutoKeyPreProcess;

import com.locojoy.base.Marshal.OctetsStream;

public class ClanInfoPreProcess extends AbstractAutoKeyPreProcess {

	public ClanInfoPreProcess(){
		super.setNumTotal(xmerge.Constant.NONE_NUM_TOTAL);
	}
	
	@Override
	public void changeXBeanValueID(Object xbeanValue, int id) {
		xbean.ClanInfo value = (xbean.ClanInfo)xbeanValue;
		value.setIndex(id);

	}

	@Override
	public boolean compareTo(Object src_value, Object des_value) throws Exception {
		xbean.ClanInfo src_info = (xbean.ClanInfo)src_value;
		xbean.ClanInfo des_info = (xbean.ClanInfo)des_value;
		
		if(src_info.getLastclandkp()>des_info.getLastclandkp()){
			return true;
		}else if(src_info.getLastclandkp()==des_info.getLastclandkp()){
			if(src_info.getCreatetime() < des_info.getCreatetime()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateId2Key(Dbx srcdb, Dbx dstdb) {
		Dbx.Table indexTable = srcdb.openTable("clanindex");
		Dbx.Table dstIndexTable = dstdb.openTable("clanindex");
		
		if(null == indexTable || null == dstIndexTable)
			return;
		
		OctetsStream srcOs = new OctetsStream();
		boolean result1 = indexTable.remove(srcOs.marshal(1));
		OctetsStream desOs = new OctetsStream();
		boolean result2 = dstIndexTable.remove(desOs.marshal(1));
		
		xbean.ClanIndex newIndex = xbean.Pod.newClanIndexData();
		newIndex.getIndex().putAll(id2Key);
		
		System.out.println(id2Key);
		
		OctetsStream os2 = new OctetsStream();
		OctetsStream resultOs = new OctetsStream();
		boolean result3 = dstIndexTable.insert(resultOs.marshal(1), newIndex.marshal(os2));
		
		if(!result1 || !result2 || !result3){
			System.out.println("帮派ID重新排序的预处理错误!!!!");
			System.out.println("result1 : " + result1);
			System.out.println("result2 : " + result2);
			System.out.println("result3 : " + result3);
		}
		
	}
	

	@Override
	public Long getKey(Object value) {
		return ((xbean.ClanInfo)value).getKey();
	}


}
