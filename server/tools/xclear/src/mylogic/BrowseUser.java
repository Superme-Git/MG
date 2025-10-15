package mylogic;

import java.util.Set;

import xbean.User;

public class BrowseUser {
	public static void browseUsersids(Set<Integer> userIds){
		xtable.User.getTable().browse(
				new mkdb.TTable.IWalk<Integer, xbean.User>(){
					@Override
					public boolean onRecord(Integer k, User v) {
						userIds.add(k);
						return true;
					}
				}
			);
		}

}
