package fire.pb.clan;

import org.apache.log4j.Logger;

/**
 * 更新处理帮派成员每日逻辑
 * @author
 *
 */
public class PUpdateClanRoleProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("CLAN");
	public static final long KICK_TIME=72*60*60*1000;//踢出时间
	private long factionId;
	public PUpdateClanRoleProc(long factionId){
		this.factionId = factionId;
	}
	
	public boolean process(){
		xbean.ClanInfo clanInfo = xtable.Clans.get(factionId);
		if(clanInfo == null) return false;
		//清除每天公会踢人数量
		clanInfo.setFiremembernum(0);
		
//		//成员处理
//		for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembersAsData().entrySet()){
//			try {
//				long roleId = entry.getKey();
//				xbean.Properties properties = xtable.Properties.select(roleId);
//				if(entry.getValue()!=null&&properties!=null){
//					int position=entry.getValue().getClanposition();
//					//公会学徒等级达到50级，公会贡献达到150点，入会时间达到48小时，第一次0点维护没有自动转为成员
//					if(properties.getLevel()>=fire.pb.clan.srv.ClanManage.getPositionChangeLevel()
//							&&entry.getValue().getClanposition()==ClanPositionType.ClanApprentice){
//						int retposition=fire.pb.clan.srv.ClanAuthManager.getInstance().changePosition(roleId);
//						if(retposition!=-1){
//							position=retposition;
//						}
//					}
//					if(properties.getOfflinetime()>0&&position==ClanPositionType.ClanApprentice){
//						//判断玩家上次登录时间和现在对比，是否超过了72小时
//						//公会学徒离线时长超过72小时后没有被系统踢出公会
//						if(System.currentTimeMillis()-properties.getOfflinetime()>KICK_TIME){
//							//踢出玩家
//							fire.pb.clan.srv.ClanAuthManager.getInstance().fireMemeberClanResult(0,roleId,FireReasonType.LONG_TIME_OFF_LINE,clanInfo,entry.getValue().getClanposition());
//						}
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				logger.error(e);
//			}
//		}
		return true;
	}


}