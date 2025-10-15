package fire.pb.instancezone.faction;

import java.util.Map;

import fire.pb.clan.srv.ClanManage;
import fire.pb.instance.SInstaceConfig;
import fire.pb.main.ConfigManager;
import mkdb.Procedure;

public class PSetFactionInst extends Procedure {

	private final long clanid;
	private final int instid;
	private final String instname;
	
	public PSetFactionInst(final long clanid, final int instid, final String instname) {
		this.clanid = clanid;
		this.instid = instid;
		this.instname = instname;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.ClanInfo clanInfo = xtable.Clans.get(clanid);
		if (clanInfo != null) {
			xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());
			if (clanInst.getInststate().get(instid) == null)
				return false;
			if (clanInst.getInststate().get(instid) == 1) {
				Map<Integer, SInstaceConfig> sInstaceConfig = ConfigManager.getInstance().getConf(SInstaceConfig.class);
				for (SInstaceConfig configOne : sInstaceConfig.values()) {
					int beforeInstid = configOne.getAfterid();
					if (instid != beforeInstid)
						continue;
					int serviceid = configOne.getServiceid();
					if (serviceid != 0) {
						clanInfo.setClaninstservice(serviceid);
						ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_CHANGE_INST_SYS, 0, instname, configOne.getName(), "");
						//List<String> paras2 = MessageUtil.getMsgParaList(getZoneConfig().name, configOne.getName());
						//fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 166123, 0, paras2);
					}
					break;
				}
			}
		}
		
		return true;
	}
	
}
