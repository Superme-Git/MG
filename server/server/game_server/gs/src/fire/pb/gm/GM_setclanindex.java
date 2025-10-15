package fire.pb.gm;

import java.util.Collection;

import fire.pb.KeyCounterIndex;
import fire.pb.clan.Module;

public class GM_setclanindex extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				Integer counter = xtable.Tablekeynum.get(KeyCounterIndex.FACTION_KEY);
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
				int last=max + 1;
				xtable.Tablekeynum.remove(KeyCounterIndex.FACTION_KEY);
				xtable.Tablekeynum.insert(KeyCounterIndex.FACTION_KEY, max + 1);
				sendToGM("当前数据库公会索引为,修改后最大索引值"+last);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addmoney gold";
	}

}
