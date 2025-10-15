package fire.pb.state;

import xbean.RolePos;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.WorldTime;
import fire.pb.battle.livedie.LiveDieMange;
import fire.pb.friends.FriendHelper;
import fire.pb.item.onlinegift.TimingRewardAssistant;
import fire.pb.map.RoleManager;
import fire.pb.mission.notify.TuiSongNotifyManager;
import fire.pb.school.shouxi.PSendShouxiOnlineMsg;


public class PreOfflineProtectState extends State{

	public PreOfflineProtectState(long roleId)
	{
		super(roleId);
	}
	
	@Override
	public boolean enter(int trigger)
	{
		Integer oldstate = xtable.Roleonoffstate.get(roleId);
		if (oldstate == null)
			oldstate = State.UNENTRY_STATE;
		boolean valid = false;
		if(trigger == State.TRIGGER_OFFLINE && oldstate == State.ENTRY_STATE)
			valid = true;
		if(trigger == State.TRIGGER_BATTLE_END && oldstate == State.TRUSTEESHIP_STATE)
			valid = true;
		if(trigger == State.TRIGGER_PROCESS_DONE && oldstate == State.END_TRUSTEESHIP_STATE)
			valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, getState());
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：" ).append(this.getClass()));
		
		return execute();
	}
	
	
	@Override
	public boolean execute() {

		try
		{
			if (xtable.Roleposes.select(roleId)!=null&&xtable.Roleposes.select(roleId).getStatus()==RolePos.INDREAM) 
				new	fire.pb.mission.fairyland.PRoleFairylandOffline(roleId).call(); 
			PropRole pro = new PropRole(roleId,false);
			try
			{
				pro.setLastOfflineTime(WorldTime.getInstance().getTimeInMillis());
				fire.pb.map.Role maprole = RoleManager.getInstance().removeRoleByID(roleId);
				if (maprole != null)
				{
					pro.getProperties().setSceneid(maprole.getScene());
					fire.pb.map.Position pos = maprole.getPos();
					pro.getProperties().setPosx(pos.getX());
					pro.getProperties().setPosy(pos.getY());
					StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 玩家" ).append(pro.getProperties().getRolename()).append(" 下线，门派：" )
							.append(pro.getSchool()).append("，等级 ：").append(pro.getLevel()));
				}
			} catch (Exception e)
			{
				StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 下线保护准备状态保存人物当前坐标出错，可能有部分下线处理没有执行。"), e);
			}
			// 队伍状态异步更新，不然继承之前的角色锁，容易死锁
			mkdb.Procedure.pexecute(new fire.pb.team.PRoleOffline(roleId));
			
			// 同步清除buff
			new fire.pb.buff.PRoleOffline(roleId).call();
			// 让场景的角色下线
			fire.msp.GRoleOfflineGs snd = new fire.msp.GRoleOfflineGs();
			snd.roleid = roleId;
			GsClient.pSendWhileCommit(snd);

			// 清除跳转场景时间
			xtable.Requestgoto.remove(roleId);

			pexecute();
			
			//下线的时候清理掉角色推送定时器
			TuiSongNotifyManager.getInstance().roleLogout(roleId);
			
			//下线计算礼物剩余时间
			TimingRewardAssistant.offline(roleId);
			
			//下线处理生死战逻辑
			LiveDieMange.removePlayerLiveDieInfoLoginout(roleId);
			
			mkdb.Procedure.pexecute(new fire.pb.clan.fight.PRoleOffline(roleId)); //公会战角色下线 by changhao
			
		} catch (Exception e)
		{
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 下线保护准备状态：").append(this.getClass()).append(" 执行出错，可能有部分下线处理没有执行。") , e);
		}
		
		this.trigger(State.TRIGGER_PROCESS_DONE);
		return true;//下线的状态转换必须执行成功，不可逆
	}
	
	
	private void pexecute()
	{
		//cn 该玩家如果是首席,则下线发送门派消息
		//mkdb.Procedure.pexecute(new PSendShouxiOnlineMsg(roleId, false));
			
		// 删除在线属性增加的计时任务
		mkdb.Procedure.pexecute(new fire.pb.POnlineBegin(roleId, false));
		
		//下线时如果处在副本中，处理副本中的数据
		mkdb.Procedure.pexecute(new fire.pb.instancezone.PRoleOffline(roleId));
		
		// 向好友通知离线
		FriendHelper.notifyFriendStatus(false, roleId);
		
		//计算角色领取节日礼物的时间间隔
		calcDrawFestivalGiftTimeSpace();
		
		// 1v1
		fire.pb.battle.pvp1.PvP1Control.getInstance().onRoleOffline(roleId);
		// 3v3
		fire.pb.battle.pvp3.PvP3Control.getInstance().onRoleOffline(roleId);
		// 5v5
		fire.pb.battle.pvp5.PvP5Control.getInstance().onRoleOffline(roleId);
	}

	private void calcDrawFestivalGiftTimeSpace() {
		mkdb.Procedure.pexecute(new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.FestivalGift festivalGift = xtable.Festival.get(roleId);
				if(null == festivalGift){
					festivalGift = xbean.Pod.newFestivalGift();
					xtable.Festival.insert(roleId, festivalGift);
					return true;
				}
				long lastDrawTime = festivalGift.getTime();
				long lastOnlineTime = xtable.Properties.selectOnlinetime(roleId);
				long onlineTotal = festivalGift.getOnlinetotal();
				if(lastDrawTime > lastOnlineTime){
					festivalGift.setOnlinetotal((System.currentTimeMillis()-lastDrawTime)+onlineTotal);
				}else{
					festivalGift.setOnlinetotal((System.currentTimeMillis()-lastOnlineTime)+onlineTotal);
				}
				return true;
			}
		});
	}

	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_PROCESS_DONE)
			return new OfflineProtectState(roleId).enter(trigger);
		triggerErrorLog(trigger);
		return false;
	}
	
	@Override
	public int getState(){
		return State.PRE_OFFLINE_PROTECT_STATE;
	}
}
