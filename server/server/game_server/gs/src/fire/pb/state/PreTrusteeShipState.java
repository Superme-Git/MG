package fire.pb.state;

import fire.pb.PropRole;
import fire.pb.map.Transfer;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.RoleMission;
import fire.pb.move.SRoleEnterScene;



public class PreTrusteeShipState extends State{

	protected boolean blockscreen = false;
	public PreTrusteeShipState(long roleId)
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
		if(trigger == State.TRIGGER_OFFLINE_BATTLE && oldstate == State.ENTRY_STATE)
				valid = true;
		if(trigger == State.TRIGGER_OFFLINE && oldstate == State.ENTRY_STATE)
			valid = true;
		if(!valid)
			return false;
		
		return execute();
	}
	

	@Override
	public boolean execute() {
		// TODO 进入准备战斗托管状态后的一些操作		
		try
		{
			PropRole prole = null; 

			if (xtable.Watcherid2battleid.select(roleId)!=null) 
				new fire.pb.battle.watch.PEndWatchBattle(roleId).call();
			else
			{	
				//发送最少的基本角色信息经Delivery到AuAny
				Long battleId = xtable.Roleid2battleid.get(roleId);
				if(battleId!=null)
				{	
					xbean.BattleInfo battle = xtable.Battle.get(battleId);
					if (battle == null)
						return true;
					// 保存战斗者的当前坐标
					prole = new PropRole(roleId, false);
					fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
					if (role != null)
					{
						prole.getProperties().setSceneid(role.getScene());
						prole.getProperties().setPosx(role.getPos().getX());
						prole.getProperties().setPosy(role.getPos().getY());
					}
					if(!battle.getOfflineroleids().contains(roleId))
						battle.getOfflineroleids().add(roleId);
					// 通知战斗内其他玩家
				}
			}
			
			if(prole == null)
				prole = new PropRole(roleId, false);
			MissionColumn tsc = new MissionColumn(roleId, true);
			for (RoleMission task : tsc) {
				if (task.getConf() != null && task.getConf().exeIndo.cruiseID > 0) {
					fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
					send2Scene.playerid = roleId;
					send2Scene.changetype = 4;
					send2Scene.shape = task.getConf().exeIndo.cruiseID;
					fire.pb.GsClient.pSendWhileCommit(send2Scene);
					StateManager.logger.debug(roleId + "有巡游任务" + task.getId() + ",巡游id:" + task.getConf().exeIndo.cruiseID);
					int tm = task.getConf().exeIndo.mapID;
					int tx = (task.getConf().exeIndo.leftPos + task.getConf().exeIndo.rightPos)/2;
					int ty = (task.getConf().exeIndo.topPos + task.getConf().exeIndo.bottomPos)/2;
					Transfer.justGoto(roleId, tm, tx, ty, SRoleEnterScene.QUEST_CG);
					StateManager.logger.debug(roleId + "下线完成巡游任务" + task.getId() + ",跳转到:" + tm + "," + tx + "," + ty);
					
					prole.getProperties().setCruise(0);
					prole.getProperties().setCruise2(0);
					prole.getProperties().setCruise3(0);
				}
			}
			
			prole.setLastOfflineTime(System.currentTimeMillis());			

			pexecute();
		}
		catch(Exception e)
		{
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 准备战斗托管状态：" ).append(this.getClass()).append(" 执行出错，可能有部分下线处理没有执行。"), e);
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, getState());
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：" ).append(this.getClass()));
		this.trigger(State.TRIGGER_PROCESS_DONE);
		return true;
	}

	private void pexecute(){
		// 删除在线属性增加的计时任务
		mkdb.Procedure.pexecute(new fire.pb.POnlineBegin(roleId, false));
	}

	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_PROCESS_DONE)
		{
			if(blockscreen)
				return new TrusteeShipState(roleId,true).enter(trigger);
			else
				return new TrusteeShipState(roleId).enter(trigger);
		}
		return false;
	}

	@Override
	public int getState(){
		return State.PRE_TRUSTEESHIP_STATE;
	}
}
