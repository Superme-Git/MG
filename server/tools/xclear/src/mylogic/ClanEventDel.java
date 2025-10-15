package mylogic;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import mkdb.Trace;
import fire.pb.KeyCounterIndex;

public class ClanEventDel {
	
	public static void clearClanEvent(Set<Long> clanIds){

		try {
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					if (clanIds.isEmpty()) {
						return true;
					}
					lock(mkdb.Lockeys.get(xtable.Locks.CLANS, clanIds));
					int max=0;//当前已有公会的，公会索引最大值
					for (long clanid : clanIds) {
						xbean.ClanInfo claninfo = xtable.Clans.get(clanid);
						if(claninfo==null){
							continue;
						}
						if(claninfo.getIndex()>max){
							max=claninfo.getIndex();
						}
						claninfo.getClaneventrecordlist().clear();
					}
					//处理一下公会当前最大索引
					Integer counter = xtable.Tablekeynum.get(KeyCounterIndex.FACTION_KEY);
					if(counter!=null){
						int last=max + 1;
						if(counter!=last){
							xtable.Tablekeynum.remove(KeyCounterIndex.FACTION_KEY);
							xtable.Tablekeynum.insert(KeyCounterIndex.FACTION_KEY, last);
							System.out.println("设置公会索引最大值为============= "+last);
							Trace.info("设置公会索引最大值为============= \t"+last+"\t原公会索引值="+counter+"\t当前公会最大值\t"+max);
						}
					}
					return true;
				}
			};
			proc.submit().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
