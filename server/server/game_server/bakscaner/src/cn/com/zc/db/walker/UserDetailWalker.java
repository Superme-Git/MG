package cn.com.zc.db.walker;

import xbean.User;
import mkdb.util.DatabaseMetaData.Table;

public class UserDetailWalker extends Walker {

	public UserDetailWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		Integer userid = (Integer) key;
		xbean.User userInfo = (User) value;
		
		if(userInfo.getIdlist().contains(new Long(86017))){
			System.out.println("葛征的Userid ： " + userid);
		}
		
		return true;
	}

}
