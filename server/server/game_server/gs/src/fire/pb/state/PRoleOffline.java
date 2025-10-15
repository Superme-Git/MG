package fire.pb.state;

import gnet.link.Onlines;
import gnet.link.User;
import mkdb.Procedure;
import mkdb.Transaction;
import fire.pb.KickErrConst;
import fire.pb.SReturnLogin;
import fire.pb.SRoleOffline;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.tel.utils.CheckCodeUtils;

public class PRoleOffline extends mkdb.Procedure
{
	final private long roleId;
	final private int type;
	
	final public static int TYPE_OFFLINE = 1;//主动下线，退出游戏
	final public static int TYPE_LINK_BROKEN = 2;//被动断线
	final public static int TYPE_CHOSEE_ROLE = 3;//返回人物选择界面
	final public static int TYPE_RETURN_LOGIN= 4;//返回登录界面
	
	public PRoleOffline(long roleId,int type){
		this.roleId = roleId;
		this.type = type;
	}
	
	@Override
	protected boolean process()
	{
		xbean.Properties prop = xtable.Properties.select(roleId);
		if(prop == null)
			return true;
		int userid = prop.getUserid();
		try
		{
			
			//fire.pb.map.Role maprole = RoleManager.getInstance().getRoleByID(roleId);
			
			Long battleId = xtable.Roleid2battleid.select(roleId);
			xbean.BattleInfo battle = null;
			if (battleId != null){
				// if (type == TYPE_CHOSEE_ROLE){
				// MessageMgr.psendMsgNotifyWhileRollback(roleId, 142663, null);
				// return false;// 不能在战斗中返回选择界面
				// }
				
				// 如果战斗内下线，先锁battlelock
				battle = xtable.Battle.get(battleId);
				if (battle != null){
					// 锁所有战斗中角色的rolelock
					Long teamId = xtable.Roleid2teamid.select(roleId);
					if(teamId != null)
						lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, new Object[] { teamId }));// 锁teamlock
					lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, new Object[] { userid }));// 锁userlock
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));
				}
				else
					lock(userid);
			}
			else{
				lock(userid);
			}
			
			//累计登录送礼处理
			final long offlineTime = prop.getOfflinetime();

			mkdb.Procedure.pexecute(new Procedure(){
				public boolean process(){
					MulDayLogin.offline(roleId, offlineTime);
					return true;
				}
			});

			boolean succ = false;
			IState state = StateManager.getStateByRoleId(roleId);
			if (battle != null && battle.getRoleids().containsKey(roleId))
				succ = state.trigger(State.TRIGGER_OFFLINE_BATTLE);// 战斗者（非观战者）
			else
				succ = state.trigger(State.TRIGGER_OFFLINE);
			
			if (succ)
			{
				TeamManager.getInstance().delTeamMatchAsyn(roleId);
			}
			
			if(!succ)
				return false;
		}
		catch(Exception e)
		{
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 下线处理出错"));
		}
		
		//针对不同的下线情况，做不同的后续处理
		if(type == TYPE_OFFLINE)
		{
			StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 客户端主动下线，退出游戏 :").append(type));
			Transaction.tsend(roleId, new SRoleOffline());
			Onlines.getInstance().getConnectedUsers().offline(userid, false);
		}
		else if(type == TYPE_LINK_BROKEN)
		{
			StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 客户端被动断线 :").append(type));
			Onlines.getInstance().getConnectedUsers().offline(userid, true);
		} 
		else if(type == TYPE_CHOSEE_ROLE)
		{
			StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 客户端请求返回人物选择界面 :").append(type));
			User user = Onlines.getInstance().getConnectedUsers().get(userid);
			if(user != null)
			{
				if(user.getState() == User.STATE_LOGIN)
					user.sendRoleList(true);
				else
					user.kick(KickErrConst.ERR_GM_KICKOUT);//user状态不对，踢下线
			}
		}
		else if(type == TYPE_RETURN_LOGIN)
		{
			StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 客户端请求返回登陆界面 "));
			Transaction.tsend(roleId, new SReturnLogin());
			Onlines.getInstance().getConnectedUsers().offline(userid, false);
		}
		//清除短信验证码相关临时数据
		CheckCodeUtils.cleanData(roleId);
		//处理玩家下线日志
		fire.log.YYLogger.OpLogAsyn(userid, roleId, "logout", 1, 1);
		return true;
	}
	
	private void lock(int userid)
	{
		Team team = TeamManager.getTeamByRoleId(roleId);
		lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, new Object[] { userid }));// 锁userlock
		if(team != null && team.isTeamLeader(roleId))
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[] {team.getAllMemberIds()}));// 队长下线要锁正常队员
		else
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[] { roleId }));// 锁rolelock
	}
	
}
