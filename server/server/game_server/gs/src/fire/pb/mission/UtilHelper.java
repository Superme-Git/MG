package fire.pb.mission;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mkdb.Transaction;
import fire.msp.GRefreshRoleEquip;
import fire.pb.GsClient;
import fire.pb.WorldTime;
import fire.pb.activity.winner.WinnerManager;
import fire.pb.battle.ResultType;
import fire.pb.fushi.DayPayManager;
import fire.pb.item.PEquipRideProc;
import fire.pb.item.Pack;
import fire.pb.skill.SkillRole;
import fire.pb.state.StateManager;

public final class UtilHelper {
	public enum AnimeType {
		UNKNOWN,
		ANIMATION, // 动画
		CARD, // 卡片
		ARROW // 箭头
	}
	
	public final static int COMPLETEDTASK = 4; //完成任务4
	public final static int XUEWEILEVEL = 5; //5
	public final static int COMPLETEDSCHOOLTASK = 6; //完成循环任务6
	public final static int GEM_ADD = 16; // 宝石镶嵌16
	public final static int JOIN_CREATE_FACTION = 31; //加入或者创建帮派31
	public final static int GEMCOMBINE = 37; //宝石合成37
	public final static int EQUIP_MAKE = 38; //装备打造38
	public final static int ADD_ROLEPOINT = 39; //人物加点39
	public final static int JINGYING_INST = 40; //精英副本40
	public final static int BINGFENG_INST = 41; //冰封王座41
	/*
	public static int getOtherQuestParamType( final int id ) {
		switch ( id ) {
		case 0:
			return OtherQuestParamType.COMPLETEDSCHOOLTASK;
		case 1:
			return OtherQuestParamType.GEM_ADD;
		case 2:
			return OtherQuestParamType.JOIN_CREATE_FACTION;
		case 3:
			return OtherQuestParamType.GEMCOMBINE;
		case 4:
			return OtherQuestParamType.EQUIP_MAKE;
		case 5:
			return OtherQuestParamType.ADD_ROLEPOINT;
		case 6:
			return OtherQuestParamType.JINGYING_INST;
		case 7:
			return OtherQuestParamType.BINGFENG_INST;
		}
		throw new IllegalArgumentException("xxx");
	}
	*/
	public static class Pair<T> {
		public T t1;
		public T t2;
		public Pair() {}
		public Pair( T t1, T t2 ) {
			this.t1 = t1;
			this.t2 = t2;
		}
	}
	
	public static void clearItemTransformID(final long roleid) {
		
		xbean.TransfromByItemData tibyItem = xtable.Transformbyitem.get(roleid);
		if (tibyItem == null) {
			return;
		}
		xtable.Transformbyitem.delete(roleid);
		fire.msp.task.GChangeShape send2Scene = 
				new fire.msp.task.GChangeShape();
		send2Scene.playerid = roleid;
		send2Scene.shape = getCurshape(roleid);
		send2Scene.changetype = 0;
		fire.pb.GsClient.pSendWhileCommit( send2Scene );

		SkillRole srole = new SkillRole(roleid);  
		Map<Integer, Integer> sextskill = new HashMap<Integer, Integer>();
		srole.addExtSkillWithSP(sextskill);		
	}
	
	public static void clearNpcFollowID(final long roleid, final long questid) {
		xbean.NpcFollowInfo nfi = xtable.Npcfollowroles.get(roleid);
		if (nfi == null || nfi.getQuestid() != questid) {
			return;
		}
		xtable.Npcfollowroles.delete(roleid);
		
		fire.msp.task.GChangeShape send2Scene = 
				new fire.msp.task.GChangeShape();
		send2Scene.playerid = roleid;
		send2Scene.shape = 0;
		send2Scene.changetype = 1;
		fire.pb.GsClient.pSendWhileCommit(send2Scene);		
		fire.pb.GsClient.pSendWhileCommit(new GRefreshRoleEquip(roleid));
	}
	
	public static void clearTransformID(final long roleid, final long questid) {
		xbean.TransfromInfo ti = xtable.Transfromroles.get(roleid);
		if (ti == null || ti.getQuestid() != questid) {
			return;
		}
		int rideid = ti.getRide();
		xtable.Transfromroles.delete(roleid);
	
		fire.msp.task.GChangeShape send2Scene = 
			new fire.msp.task.GChangeShape();
		send2Scene.playerid = roleid;
		send2Scene.shape = getCurshape(roleid);
		send2Scene.changetype = 0;
		fire.pb.GsClient.pSendWhileCommit( send2Scene );
		fire.pb.GsClient.pSendWhileCommit(new GRefreshRoleEquip(roleid));
		if(rideid > 0){
			final Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, true);
			if (bag.getItemNum(rideid, 0) > 0){
				if(Transaction.current() != null){
					new PEquipRideProc(roleid, rideid, -1).call();
				}else{
					new PEquipRideProc(roleid, rideid, -1).submit();
				}
			}
		}
	}
	

	public static void clearcruisetask(final long roleid, final int cruiseid, final int questid) {
		if(cruiseid <= 0)
			return;	
		
		xbean.Properties prop = xtable.Properties.get(roleid);
		if(cruiseid == prop.getCruise() || prop.getCruise() == 0){
			prop.setCruise(0);
			prop.setCruise2(0);
			prop.setCruise3(0);
			fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
			send2Scene.playerid = roleid;
			send2Scene.changetype = 4;
			send2Scene.shape = cruiseid;			
			fire.pb.GsClient.pSendWhileCommit(send2Scene);	
			StateManager.logger.debug(roleid + "正常完成巡游任务" + questid);
			
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
				@Override
				protected boolean process() {
					if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
					{
						boolean ok = DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
						if (ok == false)
						{
							GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid,(byte)1));					
						}
					}
					return true;
				}
			});
		}
	}
	
	public static void cruisetask(final long roleid, final int cruiseid, final int questid) {
		if(cruiseid <= 0)
			return;
		
		xbean.Properties prop = xtable.Properties.get(roleid);
		prop.setCruise(cruiseid);
		
		fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
		send2Scene.playerid = roleid;
		send2Scene.changetype = 3;
		send2Scene.shape = cruiseid;
		fire.pb.GsClient.pSendWhileCommit(send2Scene);
		
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
				{
					boolean ok = DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
					if (ok == false)
					{
						GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid,(byte)0));					
					}
				}
				return true;
			}
		});
	}
	
	/***
	 * 巡游中途更新数据 by changhao
	 * @param roleid
	 * @param cruiseid
	 * @param cruiseid2
	 * @param cruiseid3
	 */
	public static void cruisetaskupdate(final long roleid, final int cruiseid, final int cruiseid2, final int cruiseid3) {
		
		xbean.Properties prop = xtable.Properties.get(roleid);
		if (cruiseid == -1)
		{
			prop.setCruise(0);
			prop.setCruise2(0);
			prop.setCruise3(0);
			
			fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
			send2Scene.playerid = roleid;
			send2Scene.changetype = 5;
			send2Scene.shape = 0;
			send2Scene.shape2 = 0;
			send2Scene.shape3 = 0;
			fire.pb.GsClient.pSendWhileCommit(send2Scene);				
			
			return;
		}
		
		if (prop.getCruise() == cruiseid)
		{
			prop.setCruise2(cruiseid2);
			prop.setCruise3(cruiseid3);
			
			fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
			send2Scene.playerid = roleid;
			send2Scene.changetype = 5;
			send2Scene.shape = prop.getCruise();
			send2Scene.shape2 = cruiseid2;
			send2Scene.shape3 = cruiseid3;
			fire.pb.GsClient.pSendWhileCommit(send2Scene);				
		}	
	}
	
	
	public static AnimeType getAnimeType( final int animationID ) {
		if ( animationID >= 10000 && animationID <= 19999 ) {
			return AnimeType.ANIMATION;
		} else if ( animationID >= 20000 && animationID <= 29999 ) {
			return AnimeType.CARD;
		} else if ( animationID >= 30000 && animationID <= 39999 ) {
			return AnimeType.ARROW;
		}else {
			return AnimeType.UNKNOWN;
	//		throw new IllegalArgumentException( "动画ID"+animationID+"有误" );
		}
	}
	
	private static int getCurshape(final long roleid) {
		int shape = getTransformid(roleid);
		if (shape == 0) {
			Integer rShape = xtable.Properties.selectShape(roleid);
			return rShape == null ? 0 : rShape;
		}
		return shape;
	}
	
	public static int getFirstStep() {
		return 1;
	}
	
	public static int getFollowid(final long roleid) {
		final xbean.NpcFollowInfo nfi = xtable.Npcfollowroles.select(roleid);
		if (nfi != null) {
			WorldTime now = WorldTime.getInstance();
			if (now.getTimeInMillis() < nfi.getValiddate()) {
				return nfi.getNpcid();
			}
		}
		return 0;
	}
	
	public static void getLastestWeekday( final WorldTime now ) {
		if ( now.get( Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) {
			now.add( Calendar.WEEK_OF_YEAR, 1 );
		} 
		now.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
		now.set( Calendar.HOUR_OF_DAY, 0 );
		now.set( Calendar.MINUTE, 0 );
		now.set( Calendar.SECOND, 0 );
	}
	
	public static int getLastStep() { return 99; }
	
	public static String getRoleNames(List<Long> roleids) {
		String rolenames = "";
		for (long roleid : roleids) {
			String rolename = xtable.Properties.selectRolename(roleid);
			if (rolename != null) {
				rolenames += rolename + "、";
			}
		}
		if (rolenames.length() > 1)
			rolenames = rolenames.substring(0, rolenames.length()-1);
		return rolenames;
	}
	
	public static int getRoleShapeID(final long roleid) {
		int shapeid = getTransformid(roleid);
		if (shapeid != 0) { return shapeid; }
		final xbean.Properties prop = xtable.Properties.select(roleid);
		return prop.getShape();
	}
	
	public static int getMissionExeType( int missiontype ) {
		return missiontype / 10;
	}
	
	public static int getMissionFinType( int missiontype ) {
		return missiontype % 10;
	}
	
	public static int getMissionId( int id, int step ) {
		return id * 100 + step;
	}
	
	public static int getMissionLineid( int missionid ) {
		return missionid / 100;
	}
	
	public static int getMissionStep( int missionid ) {
		return missionid % 100;
	}
	
	public static int getMissionType( final int exetype, final int fintype ) {
		return exetype * 10 + fintype;
	}
	
	public static int getTransformid(final long roleid) {
		final xbean.TransfromInfo ti = xtable.Transfromroles.select(roleid);
		if (ti != null) {
			WorldTime now = WorldTime.getInstance();
			if (now.getTimeInMillis() < ti.getValiddate()) {
				return ti.getTransformid();
			}
		}
		final xbean.TransfromByItemData tiby = xtable.Transformbyitem.select(roleid);
		if (tiby != null) {
			WorldTime now = WorldTime.getInstance();
			if (now.getTimeInMillis() < tiby.getValiddate()) {
				return tiby.getTransformid();
			}
		}
		return 0;
	}

	public static boolean isBranchScenarioMission( final int missionID ) {
		if(missionID==WinnerManager.WINNER_TASK_ID||missionID==WinnerManager.WINNER_TASK_ID_ACCEPT){
			//如果是冠军试炼任务
			return false;
		}
		return missionID > 199999 && missionID < 999999;
	}
	
	public static boolean isTuPoMission(final int missionID) {
		return missionID >= 510000 && missionID <= 519999;
	}

	public static boolean isFairylandMap( final int mapID ) {
		return fire.pb.mission.fairyland.FairylandMgr.isInDreamMap(mapID);
	}

	public static boolean isFirstStepInLine( final int step ) {
		return step == getFirstStep();
	}
	
	public static boolean isLastStepInLine( final int step ) {
		return step == getLastStep();
	}
	
	public static boolean isMajorScenarioMission( final int missionID ) {
		return missionID > 99999 && missionID < 199999;
	}
	
	public static boolean isMonsterType( final int id ) {
		return id > 99999;
	}
	
	public static boolean isScenarioMission( final int taskid ) {
		return isMajorScenarioMission( taskid ) || isBranchScenarioMission( taskid );
	}
	
	public static boolean isSpecialQuest(final int questid) {
		return questid > 1000000 && questid < 2000000;
	}
	
	public static boolean isWinBattle( final fire.pb.event.BattleEndEvent e ) {
		final long roleid = e.getRoleid();
		for (xbean.Fighter fighter : e.getBattleResult().getFighters().values() ) {
			int index = fighter.getBattleindex();
			final Integer val = e.getBattleResult().getRoundresult().get(index);
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE && fighter.getUniqueid() == roleid )
				return val != null && (val & ResultType.RESULT_DEATH) == 0;
		}
		return false;
	}
	
	public static void itemTransform(final long roleid, final int newshapeid, final int time) {
		final xbean.Properties prop = xtable.Properties.select(roleid);
		xbean.TransfromByItemData ti = xtable.Transformbyitem.get(roleid);
		if (ti == null) {
			ti = xbean.Pod.newTransfromByItemData();
			xtable.Transformbyitem.insert(roleid, ti);
		}
		final int curTransformid = ti.getTransformid();
		if (curTransformid != newshapeid) {
			// 告诉客户端变身,周围玩家也要看到
			ti.setTransformid(newshapeid);
			WorldTime now = WorldTime.getInstance();
			now.add(Calendar.MINUTE, time);
			ti.setValiddate(now.getTimeInMillis());
			
			final xbean.TransfromInfo ti2 = xtable.Transfromroles.select(roleid);
			if (ti2 == null || WorldTime.getInstance().getTimeInMillis() >= ti2.getValiddate()) {
				fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
				send2Scene.playerid = roleid;
				send2Scene.changetype = 0;
				if (newshapeid != 0)
					send2Scene.shape = newshapeid;
				else
					send2Scene.shape = prop.getShape();
				fire.pb.GsClient.pSendWhileCommit(send2Scene);
			}
			
			mkdb.Executor.getInstance().schedule(new Runnable() {		
				@Override
				public void run() {
					new mkdb.Procedure(){

						@Override
						protected boolean process() throws Exception {
							clearItemTransformID(roleid);
							return true;
						}
						
					}.submit();
				}
			}, time, TimeUnit.MINUTES);

		}
	}
	
	public static void npcfollowme(final long roleid, final int npcid, final int time, final int questid) {
		xbean.NpcFollowInfo nfi = xtable.Npcfollowroles.get(roleid);
		boolean canfollow = false;
		if (nfi == null) {
			nfi = xbean.Pod.newNpcFollowInfo();
			xtable.Npcfollowroles.insert(roleid, nfi);
			canfollow = true;
		} else {
			if (isMajorScenarioMission(questid) && (isMajorScenarioMission(nfi.getQuestid()) || nfi.getQuestid() == 0)){
				canfollow = true;
			} else if (isBranchScenarioMission(questid) && (isBranchScenarioMission(nfi.getQuestid()) || nfi.getQuestid() == 0)){
				canfollow = true;
			} else {
				canfollow = false;
			}
			//if (getTaskLineid(nfi.getQuestid()) != getTaskLineid(questid))
				//return;
		}
		if(canfollow) {
			final int curFollowNpcid = nfi.getNpcid();
			if (curFollowNpcid != npcid) {
				nfi.setNpcid(npcid);
				nfi.setQuestid(questid);
				WorldTime now = WorldTime.getInstance();
				now.add(Calendar.MINUTE, time);
				nfi.setValiddate(now.getTimeInMillis());
				
				fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
				send2Scene.playerid = roleid;
				send2Scene.changetype = 1;
				send2Scene.shape = npcid;
				fire.pb.GsClient.pSendWhileCommit(send2Scene);
				
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						new mkdb.Procedure(){
							@Override
							protected boolean process() throws Exception {
								clearNpcFollowID(roleid,questid);
								return true;
							}
							
						}.submit();
					}
				}, time, TimeUnit.MINUTES);
			}
		}
	}
	
	public static void transform(final long roleid, final int newshapeid, final int time, final int questid) {
		final xbean.Properties prop = xtable.Properties.select(roleid);
		xbean.TransfromInfo ti = xtable.Transfromroles.get(roleid);
		boolean cantransform = false;
		if (ti == null) {
			ti = xbean.Pod.newTransfromInfo();
			xtable.Transfromroles.insert(roleid, ti);
			cantransform = true;
		} else {
			if (isMajorScenarioMission(questid) && (isMajorScenarioMission(ti.getQuestid()) || ti.getQuestid() == 0)){
				cantransform = true;
			} else if (isBranchScenarioMission(questid) && (isBranchScenarioMission(ti.getQuestid()) || ti.getQuestid() == 0)){
				cantransform = true;
			} else {
				cantransform = false;
			}
			//if (getTaskLineid(nfi.getQuestid()) != getTaskLineid(questid))
				//return;
		}
		if(cantransform) {
			final int curTransformid = ti.getTransformid();
			if (curTransformid != newshapeid) {
				// 告诉客户端变身,周围玩家也要看到
				ti.setTransformid(newshapeid);
				ti.setQuestid(questid);
				WorldTime now = WorldTime.getInstance();
				now.add(Calendar.MINUTE, time);
				ti.setValiddate(now.getTimeInMillis());
				fire.msp.task.GChangeShape send2Scene = new fire.msp.task.GChangeShape();
				send2Scene.playerid = roleid;
				send2Scene.changetype = 0;
				if (newshapeid != 0) {
					send2Scene.shape = newshapeid;
					ti.setRide(prop.getRide());
					new PEquipRideProc(roleid, 0, -1).call();
				}
				else {
					send2Scene.shape = prop.getShape();
					int rideid = ti.getRide();
					if(rideid > 0){
						send2Scene.changetype = 2;
						//final Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, true);
						//if (bag.getItemNum(rideid, 0) > 0) 
							//new PEquipRideProc(roleid, rideid).call();
					}
				}
				fire.pb.GsClient.pSendWhileCommit(send2Scene);
				
		        mkdb.Executor.getInstance().schedule(new Runnable() {
					
					@Override
					public void run() {
						new mkdb.Procedure(){
	
							@Override
							protected boolean process() throws Exception {
								clearTransformID(roleid,questid);
								return true;
							}
							
						}.submit();
					}
				}, time, TimeUnit.MINUTES);
			}
		}
	}
	
	private UtilHelper() {
		throw new AssertionError();
	}
}
