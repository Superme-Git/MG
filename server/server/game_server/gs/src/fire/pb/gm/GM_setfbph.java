package fire.pb.gm;

import java.util.Iterator;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

public class GM_setfbph extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 3) {
			sendToGM("参数错误：" + usage());
			return false;
		}
		//[gonghuiid] [fubenid] [saveid] [time] [bosshp]
		final int clanIndex;
		final int fubenid;
		final int saveid;
		final int minute;
		final Float bosshp;

		clanIndex = Integer.valueOf(args[0]);
		fubenid = Integer.valueOf(args[1]);
		saveid = Integer.valueOf(args[2]);
		minute = Integer.valueOf(args[3]);
		bosshp = Float.valueOf(args[4]);
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				InstanceZoneConfig zoneconfig = Module.getInstance().getInstanceZoneConfigs().get(fubenid);
				if (zoneconfig == null)
					return false;
				
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoByIndex(clanIndex);
				if (null == clanInfo)
					return false;
				
				int ranktype = RankType.FACTION_COPY;
				
				final long now = java.util.Calendar.getInstance().getTimeInMillis();
				xbean.ClanProgressRankRecord record = xbean.Pod.newClanProgressRankRecord();
				record.setClankey(clanInfo.getKey());
				record.setClanname(clanInfo.getClanname());
				record.setProgress(saveid);
				record.setTime(minute*60*1000);
				record.setTriggertime(now);
				record.setCopyid(fubenid);
				record.setBosshp(1-bosshp);
				record.setCopyname(zoneconfig.name);
				
				xbean.ClanProgressRankList list = xtable.Clanprogressranklist.get(ranktype);
				if (null == list) {
					list = xbean.Pod.newClanProgressRankList();
					xtable.Clanprogressranklist.insert(ranktype, list);
				}
				
				Iterator<xbean.ClanProgressRankRecord> it = list.getRecords().iterator();
				while (it.hasNext()) {
					xbean.ClanProgressRankRecord tmp = it.next();
					if (tmp.getClankey() == clanInfo.getKey()) {
						it.remove();
						break;
					}
				}
				
				RankListManager.getInstance().tryInsertRecord(ranktype, list.getRecords(), record);
				sendToGM("公会:" + clanInfo.getClanname() + ",公会id:" + clanInfo.getIndex() +	",公会副本排行榜设置成功");
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//setfbph [gonghuiid] [fubenid] [saveid] [time] [bosshp]";
	}

}
