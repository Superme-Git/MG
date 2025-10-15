package mylogic;

import java.util.Set;

public class SelectDelRoleid {
	public static final int DEL_ROLE_LEVEL=30;
//	public static final int DEL_ROLE_TIME=30*24*60*60*1000;
	public static final int DEL_ROLE_TIME=60*60*1000;
	
	
	public static void selectDelRole(Set<Long> roleIds){
		long curtime=System.currentTimeMillis();
		xtable.Properties.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.Properties>() {
					@Override
					public boolean onRecord(Long roleId, xbean.Properties value) {
						//删除10级以下包括10级的玩家(并且改玩家没有充值记录),30天没有登录的玩家  getOfflinetime，并且没有帮派的玩家
						if(value.getLevel()<=DEL_ROLE_LEVEL&&curtime-value.getOfflinetime()>DEL_ROLE_TIME&&value.getClankey()<=0){
							roleIds.add(roleId);
						}
						return true;
					}
				});
	}

}
