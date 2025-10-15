package fire.pb.activity.impexam;

import java.util.List;

//import fire.log.Logger;
import fire.pb.clan.ClanUtils;
import fire.pb.game.ImperialExamVillageRepo;
import fire.pb.game.SImperialExamVillageRepo;
import fire.pb.main.ConfigManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.ImpExamType;
import fire.pb.npc.NpcManager;
import fire.pb.talk.ChatChannel;
import fire.pb.team.TeamManager;

/**
 * 
 * @author cn
 */
public class PSendImpExamHelp extends mkdb.Procedure {

	private final long roleId;
	private final int impExamType;

	//private static Logger logger = Logger.getLogger("SYSTEM");

	public PSendImpExamHelp(long roleid, int impExamType) {
		this.roleId = roleid;
		this.impExamType = impExamType;
	}

	@Override
	protected boolean process() throws Exception {
		//检测一下当前是否在活动范围内
		int ieType = ImpExamManager.getInstance().isInImpExamTime();
		
		xbean.ImpExamRecord record = xtable.Role2impexam.get(roleId);
		
		//或者客户端发送的进程与服务器不一致   客户端结束
		int step = record.getStep();
		if(ieType == -1 || step != impExamType || impExamType != ImpExamType.IMPEXAM_VILL){
			return false;
		}
		
		fire.pb.PropRole pRole = new fire.pb.PropRole(roleId, true);
		
		processVillHelp(record, pRole);
		
		return true;
	}
	
	private boolean processVillHelp(xbean.ImpExamRecord record, fire.pb.PropRole pRole){
		
		int index = record.getLastquesid();
		List<Integer> idList = record.getQuesidlist();
		
		java.util.NavigableMap<Integer, ImperialExamVillageRepo> keju1RepositoryMap = NpcManager.getInstance().getKeju1RepositoryMap();
		ImperialExamVillageRepo curRep = keju1RepositoryMap.get(idList.get(index));
		
		final xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (clanInfo == null) {
			return false;
		}
		
		fire.pb.message.SStringRes msg = ConfigManager.getInstance().getConf(fire.pb.message.SStringRes.class).get(300);
		if (msg == null){
			TeamManager.logger.debug("PSendImpExamHelp:找不到字符串 "+roleId);
			return false;						
		}
		
		int curHelpCnt = record.getHelpcnt();
		if(curHelpCnt >= 3){
			return false;
		}
		
		String msgstring = msg.msg;
		
		msgstring = msgstring.replaceAll("\\$parameter1\\$", curRep.getTopic());

		Integer q = curRep.getId();
		msgstring = msgstring.replaceAll("\\$parameter2\\$", q.toString());
		
		msgstring = msgstring.replaceAll("\\$parameter3\\$", pRole.getName());
		
		msgstring = msgstring.replaceAll("\\$parameter4\\$", String.valueOf(roleId));
		
		msgstring = msgstring.replaceAll("\\$parameter5\\$", String.valueOf(RoleLiveness.IMPEXAMVILL));
		
		final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos  = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
		
		ChatChannel.getInstance().process(roleId, fire.pb.talk.ChannelType.CHANNEL_CLAN, msgstring, null, showinfos, 1);
		
		
		record.setHelpcnt(curHelpCnt + 1);
		
		ImpExamManager.getInstance().sendImpExamHelp(roleId, curHelpCnt + 1);
					
		return true;

	}
	

}
