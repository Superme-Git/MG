
package fire.pb.clan;

import java.util.Arrays;

import org.apache.log4j.Logger;

import fire.pb.clan.srv.ClanAuthManager;
import fire.pb.clan.srv.ClanBaseManager;
import fire.pb.clan.srv.ClanManage;
import fire.pb.clan.srv.ClanRank;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;


/**
 * 公会每日维护
 *
 */
public class PClanMaintain extends Procedure {
	public static final Logger logger = Logger.getLogger("CLAN");
	private long factionId;
	
	public PClanMaintain(long factionId) {
		this.factionId = factionId;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.ClanInfo clanInfo = xtable.Clans.get(factionId);
		if(clanInfo==null){
			return true;
		}
		long clankey=clanInfo.getKey();
		//清除每天公会踢人数量
		clanInfo.setFiremembernum(0);
		
		int level = clanInfo.getClanlevel();
		fire.pb.clan.SClanLobby lobby =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanLobby.class).get(level);
		if (lobby == null)
		{
			return true;
		}
		
		int cost = ClanUtils.getCostClanDTMoney(clanInfo);
		boolean flag=true;//是否需要检测弹劾标志
		
		boolean ok = ClanManage.AddClanMoney(-cost, clanInfo);
		if (ok) //维护扣钱成功 by changhao
		{
			//告诉会长和副会长 by changhao
			Integer c = cost;
			if (clanInfo.getClanmaster() != 0)
			{
				MessageMgr.psendSystemMessageToRole(clanInfo.getClanmaster(), 160228, Arrays.asList(c.toString()));
			}
			
			if (clanInfo.getClanvicemaster() != 0)
			{
				MessageMgr.psendSystemMessageToRole(clanInfo.getClanvicemaster(), 160228, Arrays.asList(c.toString()));
			}
			//在公会频道发送以下160227
			// 通知公会中的兄弟
			MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160227, 0,Arrays.asList(cost+""));
			//处理帮派事件
			fire.pb.clan.srv.ClanManage.addDealClanEventInfo(clanInfo,fire.pb.clan.srv.ClanManage.EVENT_MAINTENANCE,0,cost+"","","");
			logger.info("公会维护\t"+"维护成功 "+"\t公会key\t"+clankey+"扣除公会资金"+cost);
		}
		else //公会费用不足已维护 降级 by changhao
		{
			if (level <= 1) //解散 by changhao
			{	
				flag = false;					
				ClanBaseManager.getInstance().dismissClan(clanInfo);
				logger.info("公会维护\t"+"维护资金不足解散公会 "+"\t公会key\t"+clankey);
			}
			else //降级 by changhao
			{
				//当公会维护费用不足时，公会等级降低1级，同时系统给予500万公会资金，会长将在好友界面中收到系统消息：“你的公会资金不足以进行公会维护，
				//因此公会等级降低一级，同时公会将得到500万公会资金补给，请尽快带领你的公会成员参与公会活动。
				clanInfo.setClanlevel(level - 1);
				ClanManage.AddClanMoney(lobby.downcompensate, clanInfo);//降级公会资金补偿 by changhao
				
				//更新公会等级排行 by changhao
				mkdb.Procedure.pexecuteWhileCommit(new ClanRank(clanInfo.toData()));
								
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(clanInfo.getKey(), false));
				
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightUpdateClanLevel(clankey, level));
				
				//会长和副会长发送到好友系统频道160232
				//<T t='你的公会资金不足以进行公会维护，因此公会等级降低一级，如果公会金库与公会药房高于当前公会等级则效果与公会等级一致，同时公会将得到500万公会资金补给，请尽快带领你的公会成员参与公会活动。如公会等级降至1级时依然如此，你的公会将自动解散。' c='FF693F00'/>
				if (clanInfo.getClanmaster() != 0)
				{
					MessageMgr.psendSystemMessageToRole(clanInfo.getClanmaster(), 160232, null);
				}
				if (clanInfo.getClanvicemaster() != 0)
				{
					MessageMgr.psendSystemMessageToRole(clanInfo.getClanvicemaster(), 160232,null);
				}
				
				//在公会频道发送以下160110
				// 通知公会中的兄弟
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160110, 0,null);
				
				//处理帮派事件
				fire.pb.clan.srv.ClanManage.addDealClanEventInfo(clanInfo,fire.pb.clan.srv.ClanManage.EVENT_BUILD_DOWN,0,clanInfo.getClanlevel()+"","","");
				logger.info("公会维护\t"+"维护失败，降级 "+"\t公会key\t"+clankey+"当前公会等级"+clanInfo.getClanlevel());
			}
		}
		
		if(flag==true){
			pexecute(dealSysImpeachMent(clankey));
		}
		return true;
	
	}
	
	/**
	 * 处理系统弹劾功能
	 * @param clankey
	 * @return
	 */
	public mkdb.Procedure dealSysImpeachMent(long clankey){
		//1)游戏中连续10天没上线的公会会长将被系统弹劾，新会长由副会长接任
		//2)如果公会没有副会长，就由公会内DKP最多的人担任
		//3)接替会长职位的人10日之内需要有上限记录。如果期间未曾上限，公会将会自动解散
		return new mkdb.Procedure(){
			@Override
			protected boolean process() {
				xbean.ClanInfo claninfo = xtable.Clans.get(clankey);
				if(claninfo==null){
					return false;
				}
				Long mastertime=xtable.Properties.selectOnlinetime(claninfo.getClanmaster());
				if(mastertime==null){
					return false;
				}
				if(System.currentTimeMillis()-mastertime>PRequestImpeachMent.IMPEACHMENT_TEN_TIME){
					//处理系统弹劾功能
					//判断是否有副会长，如果有，判断10天内是否登录过
					long otherroleid = claninfo.getClanvicemaster();
					int max=0;
					if(otherroleid<=0){
						//没有副会长，选择一个历史帮贡最大的玩家
						for(long id:claninfo.getMembers().keySet()){
							//过滤会长
							if(id==claninfo.getClanmaster()){
								continue;
							}
							Integer clanpoint = xtable.Roleclanpoint.selectHistoryclanpoint(id);
							if(clanpoint==null){
								continue;
							}
							if(clanpoint>max){
								max=clanpoint;
								otherroleid=id;
							}else if(clanpoint==max){
								//处理dpk相等的情况
								Long time1=xtable.Properties.selectOnlinetime(id);
								Long time2=xtable.Properties.selectOnlinetime(otherroleid);
								if(time1!=null&&time2!=null&&time1>time2){
									otherroleid=id;
								}
							}
						}
					}
					//判断当前玩家10天内是否上过线
					Long onlinetime=xtable.Properties.selectOnlinetime(otherroleid);
					if(onlinetime!=null&&System.currentTimeMillis()-onlinetime<PRequestImpeachMent.IMPEACHMENT_TEN_TIME){
						//处理弹劾
						// 现任会长任命下任会长
						long oldmasterid=claninfo.getClanmaster();
						xbean.ClanMemberInfo masterInfo = claninfo.getMembers().get(oldmasterid);
						xbean.ClanMemberInfo memberInfo = claninfo.getMembers().get(otherroleid);
						claninfo.setClanmaster(otherroleid);
						masterInfo.setClanposition(ClanPositionType.ClanMember);
						// 称谓变更为成员
						ClanUtils.changeClanTitleByPosition(oldmasterid, ClanPositionType.ClanMaster,ClanPositionType.ClanMember);
						Procedure.psendWhileCommit(claninfo.getMembers().keySet(), new SRefreshPosition(oldmasterid, ClanPositionType.ClanMember));
						//这里需要处理原来的职位是不是副会长，如果是需要把副会长标志设置为空
						int oldposition=memberInfo.getClanposition();
						if(oldposition==ClanPositionType.ClanViceMaster){
							claninfo.setClanvicemaster(0);
						}
						//职位变化后的一些逻辑处理
						String membername=xtable.Properties.selectRolename(otherroleid);
						ClanAuthManager.getInstance().changeImpeachMentPosition(oldmasterid,otherroleid,ClanPositionType.ClanMaster,oldposition,memberInfo,membername,claninfo);
						logger.info("公会维护\t"+"系统弹劾 "+"\t公会key\t"+clankey+"任命新的会长\t"+otherroleid+"\t原会长"+oldmasterid);
					}else{
						//解散公会
						ClanBaseManager.getInstance().dismissClan(claninfo);
						logger.info("公会维护\t"+"系统弹劾解散公会 "+"\t公会key\t"+clankey);
					}
				}
				return true;
			}
		};
	}
	
	
}

