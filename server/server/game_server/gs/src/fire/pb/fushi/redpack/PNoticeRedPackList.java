package fire.pb.fushi.redpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.clan.ClanUtils;
import fire.pb.fushi.SRedPackConfig;
import fire.pb.main.ConfigManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PNoticeRedPackList extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	
	private final long roleid;


	public PNoticeRedPackList(long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		List<xbean.RedPackInfo> templist=new ArrayList< xbean.RedPackInfo>();
		SNoticeRedPackList sNoticeRedPackList=new SNoticeRedPackList();
		
		xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.select(1);
		if(redPackInfoList==null){
			return false;
		}
		long clanid=0;
		long teamid=0;
		xbean.SRRedPackNumList sRRedPackNumList=xtable.Srredpacknumlisttab.select(roleid);
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if(clanInfo!=null&&clanInfo.getMembers().containsKey(roleid)){
			clanid=clanInfo.getKey();
		}
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if(team!=null){
			teamid=team.getTeamId();
		}
		for(xbean.RedPackBaseInfo redPackBaseInfo:redPackInfoList.getRedpackinfoviewlist().values()){
			if(redPackBaseInfo==null){
				continue;
			}
			xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(redPackBaseInfo.getRoleid());
			if(redPackRoleInfoList==null){
				continue;
			}
			xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
			if(redpackinfo==null){
				continue;
			}
			//判断红包类型
			if(redpackinfo.getModeltype()==RedPackType.TYPE_CLAN){
				if(clanid!=redpackinfo.getValuekey()){
					continue;
				}
			}
			if(redpackinfo.getModeltype()==RedPackType.TYPE_TEAM){
				if(teamid!=redpackinfo.getValuekey()){
					continue;
				}
			}
			//过滤掉过期的红包
			if(System.currentTimeMillis()-redpackinfo.getSendtime()>=RedPackManage.getValidRedPackTime()){
				continue;
			}
			//过滤掉已经领取完成的红包
			if(redpackinfo.getRedpackstate()==RedPackState.STATE_NONE){
				continue;
			}
			//如果玩家可以领取的次数满了，登陆的时候就不在发送
			//判断今天领取的红包是否已经达到上限
			if(sRRedPackNumList!=null){
				SRedPackConfig sRedPackConfigdata=RedPackManage.getSRedPackConfigdataByType(redpackinfo.getModeltype());
				xbean.SRRedPackNum sRRedPackNum=sRRedPackNumList.getSrredpacknumlist().get(redpackinfo.getModeltype());
				if(sRRedPackNum!=null){
					if(sRRedPackNum.getRedpackreceivenum()>=sRedPackConfigdata.getDayreceivemax()){
						continue;
					}
				}
			}
			//过滤掉玩家已经领取过的红包
			if(isReceived(redpackinfo)){
				continue;
			}
			templist.add(redpackinfo);
			RedPackRoleTip redPackRoleTip=new RedPackRoleTip();
			redPackRoleTip.modeltype=redpackinfo.getModeltype(); // 红包类型
			redPackRoleTip.redpackid=redpackinfo.getRedpackid(); // 红包Id
			xbean.Properties player=xtable.Properties.select(redpackinfo.getSendroleid());
			redPackRoleTip.rolename=player.getRolename(); // 角色名
			if(RedPackManage.isSendGold(redpackinfo.getModeltype())){//是否是发送金币的
				redPackRoleTip.fushi=redpackinfo.getRedpackallmoney();
			}else{
				redPackRoleTip.fushi=redpackinfo.getRedpackallmoney()/RedPackManage.CHANGE_NUM;
			}
			sNoticeRedPackList.redpackroletiplist.add(redPackRoleTip);
		}
		psendWhileCommit(roleid, sNoticeRedPackList);
		
		return true;
	}
	
	public boolean isReceived(xbean.RedPackInfo redpackinfo){
		//判断这个玩家是否已经领取过这个红包
		for(int i=0;i<redpackinfo.getRedpackrolehisinfolist().size();i++){
			xbean.RedPackRoleHisInfo his=redpackinfo.getRedpackrolehisinfolist().get(i);
			if(his.getRoleid()==roleid){
				return true;
			}
		}
		return false;
	}


	
	
	

}
