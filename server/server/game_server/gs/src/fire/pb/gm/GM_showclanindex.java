package fire.pb.gm;

import java.util.Collection;

import fire.pb.KeyCounterIndex;
import fire.pb.clan.Module;

public class GM_showclanindex extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				Integer counter = xtable.Tablekeynum.select(KeyCounterIndex.FACTION_KEY);
				if (null == counter) {
					sendToGM("当前数据库公会索引为空");
					return false;
				}else{
					sendToGM("当前数据库公会索引为"+counter);
				}
				int max=0;
				Collection<xbean.ClanInfo> clanInfoList =  Module.clanListMap.values();
				for (xbean.ClanInfo clan : clanInfoList) {
					if(clan!=null){
						if(clan.getIndex()>max){
							max=clan.getIndex();
						}
					}
				}
				sendToGM("当前数据库公会索引为,已经存在的公会最大索引值"+max);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "showclanindex";
	}

}
