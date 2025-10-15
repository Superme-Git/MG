package mylogic;

import java.util.Set;

import xbean.ClanInfo;

public class BrowseClans {
	public static void browseClansids(Set<Long> clanIds){
		xtable.Clans.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.ClanInfo>(){
					@Override
					public boolean onRecord(Long k, ClanInfo v) {
						clanIds.add(v.getKey());
						return true;
					}
				}
			);
		}

}
