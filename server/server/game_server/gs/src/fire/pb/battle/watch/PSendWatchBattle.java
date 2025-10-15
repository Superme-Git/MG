
package fire.pb.battle.watch;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import fire.log.Logger;
import fire.msp.role.GSetRoleBattleInfo;
import fire.pb.GsClient;
import fire.pb.PropConf;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.battle.FighterInfo;
import fire.pb.battle.SSendAddFighters;
import fire.pb.battle.SSendWatchBattleStart;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import xbean.BattleInfo;
import xbean.Fighter;

public class PSendWatchBattle extends Procedure {

	private final long watcherid;

	private final long roleid;

	private boolean isTeam;
	
	private final long battleid;

	private static Logger logger = Logger.getLogger("BATTLE");
	public PSendWatchBattle(long watcherid, long roleid) {
		this.watcherid = watcherid;
		this.roleid = roleid;
		this.battleid = 0;
		isTeam = false;
	}
	
	public PSendWatchBattle(long watcherid, long roleid, long battleid) {
		this.watcherid = watcherid;
		this.roleid = roleid;
		this.battleid = battleid;
		isTeam = false;
	}

	/**
	 * 增加观战者.前10个观战者是在战斗中可以看见的,进入和退出观战也必须通知,剩下的观战者是看不见的,他们只是观战,他们离开和进入观战也不用通知其他人
	 * (non-Javadoc)
	 * 
	 * @see mkdb.Procedure#process()
	 */
	@Override
	protected boolean process() throws Exception {
		Long battleid = null;
		if (this.battleid == 0)
			battleid = xtable.Roleid2battleid.select(roleid);
		else {
			battleid = Long.valueOf(this.battleid);
		}
		
		if (battleid == null)// 玩家可能是点击观战者进入观战的
			battleid = xtable.Watcherid2battleid.select(roleid);
		if (battleid == null) {
			MessageMgr.sendMsgNotify(watcherid, 146110, null);
			return true;
		}
		xbean.BattleInfo battle = xtable.Battle.get(battleid);
		if(battle == null)
			return true;
		
		Team watcherTeam = TeamManager.getTeamByRoleId(watcherid);		
		if(watcherTeam != null){
			//是正常的队员不能观战
			if(watcherTeam.isNormalMember(watcherid)){
				return true;
			}			
			if(watcherTeam.isTeamLeader(watcherid))
				isTeam = true;
		}

		if(isTeam&&watcherTeam != null)
		{//如果是队长
			if (battle.getAudience().size()+1+watcherTeam.getNormalMemberIds().size() >= PropConf.Battle.WATCHERS_TOTOAL_NUMS)
				return false;
//			addwatcher2Battle(battle,watcherid);
			for(long normalTeamMemberid: watcherTeam.getNormalMemberIds())
				addwatcher2Battle(battle,normalTeamMemberid);
			return true;
		}
		else
		{
			return addwatcher2Battle(battle,watcherid);
		}		
/*		//在这个地方拿watcherid锁
		Set<Long> lockroleids = new HashSet<Long>();
		lockroleids.addAll(battle.getRoleids().keySet());
		lockroleids.add(watcherid);
		lock(xdb.Lockeys.get(xtable.Locks.ROLELOCK, lockroleids));
		fire.pb.PropRole watcher = new fire.pb.PropRole(watcherid, false);
		fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(watcherid, true);
		
		if(xtable.Watcherid2battleid.select(watcherid)!=null){
			return true;
		}
		if (xtable.Roleid2battleid.select(watcherid)!=null){
			return true;
		}

		// 观战者不能超过200人
		if (battle.getAudience().size() >= BattleField.WATCHERS_TOTOAL_NUMS)
			return false;
		boolean isGm = false;
		Integer userID = xtable.Roleid2userid.select(watcherid);
		if (userID == null){
			logger.info("watch battle can't find userid.watcherid:"+watcherid);
			return false;
		}
//		if(xtable.Auuserinfo.selectBlisgm(userID)==1)
//			isGm = true;
		boolean findRole = false;
		int enemyside = 0;
		int remainTime = 0;
		// 找到被观战的人的fighter,有可能是参战,也可能是观战者
		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		fighters.addAll(battle.getFighters().values());
		fighters.addAll(battle.getWatchers().values());
		for (xbean.Fighter f : fighters) {
			if (f.getUniqueid() == roleid) {
			//	battle.getAudience().add(watcherid);
				findRole = true;
				// 不管是观战还是参战,都有主客方,如果玩家是点击观战者进入观战,则继承该观战者的主客属性
				if (!f.getIshost())
					// if(f.getBattleindex()>BattleField.A_SIDE_FIGHTER_NUM)
					enemyside = 1;
				break;
			}
		}
		if (!findRole)
			return false;
		// 如果是等待操作的phase,发送剩余时间,否则保持0不变
		if (battle.getPhase() == BattleInfo.PHASE_WAITOPERATION)
			remainTime = (int)(BattleField.ACTION_TIMEOUT_DELAY*battle.getGmactiontimetimes()/10.0 -  ((System.currentTimeMillis() - battle.getRoundtime()) / 1000));

//		int indexInAudience = battle.getAudience().indexOf(watcherid);
//		if (indexInAudience == -1)
//			return false;
		// 观战玩家生成一个fighterInfo
		xbean.Fighter fighter = xbean.Pod.newFighter();
		// 观战玩家生成一个Fighter
		// 观战的id从31开始,如果有人退出观战,下个人进入观战应该是用上次退出的人的观战id
		int battleIndex = getBattleIndex(battle.getAudience(),battle.getWatchers());
		if (battleIndex==0)
			return false;
		fighter.setBattleindex(battleIndex);
		fighter.setFightertype(xbean.Fighter.FIGHTER_ROLE);
		fighter.setUniqueid(watcherid);
		fighter.setFightername(watcher.getName());
		fighter.setInihp(watcher.getHp());
		fighter.setIniwound(erole.getWound());
		fighter.setInimp(watcher.getMp());
		// 观战也有主客之分的
		fighter.setIshost(enemyside == 0 ? true : false);
		// 加入battleinfo中
		battle.getWatchers().put(fighter.getBattleindex(),fighter);
		battle.getAudience().put(watcherid, fighter.getBattleindex());
		//xtable.Roleid2battleid.insert(watcherid, battle.getBattleid());
		xtable.Watcherid2battleid.insert(watcherid, battle.getBattleid());
		FighterInfo fi = BattleField.getWatcherFighterInfo(fighter, battle);
		
		SSendWatchBattleStart watchBattleStart = new SSendWatchBattleStart(enemyside, remainTime, battle.getBattletype(),battle.getRound(),battle.getHostform(),battle.getGuestform(),battle.getHostformlevel(), battle.getGuestformlevel(), (byte)battle.getBackground(),(byte)battle.getBgm(), 0);
		psendWhileCommit(watcherid, watchBattleStart);
		
		// 通知其他人新加入了一个观战者,前10的观战者进入了才通知,后面的不用通知
		if (battleIndex < (BattleField.FIRST_WATCHER_BATTLE_INDEX +BattleField.WATCHERS_IN_BATTLE)&&!isGm) {
			SSendAddFighters addsnd = new SSendAddFighters();
			addsnd.fighterlist.add(fi);
			Set<Long> allFightersID = battle.getRoleids().keySet();
			Set<Long> receiverSet = new HashSet<Long>();
			receiverSet.addAll(allFightersID);
			Collection<Long> audiences = battle.getAudience().keySet();
			if (audiences.size() >= 1) {
				receiverSet.addAll(audiences);
				// for (int i = 0; i <audiences.size(); i++) {
				// allFightersID.add(audiences.get(i));
				// }
			}
			if (battleIndex >= (BattleField.FIRST_WATCHER_BATTLE_INDEX +BattleField.WATCHERS_IN_BATTLE))
				logger.error("watch battle error,battleIndex:"+battleIndex+"watchers' keys"+battle.getWatchers().keySet());
			psendWhileCommit(receiverSet, addsnd);
		}
		// 给这个观战者发送其他人的消息,注意如果观战者超过10人,则只要发送前10个人的信息就可以了,剩下的观战者相当于透明的
		SSendAddFighters addsnd2 = new SSendAddFighters();
	
		for (xbean.Fighter f : battle.getFighters().values()) {
			if (!BattleField.checkOutBattle(battle, f.getBattleindex())) 
			addsnd2.fighterlist.add(BattleField.getFighterInfo(f, battle));
		}
		NavigableMap<Integer, Fighter> watchers = battle.getWatchers();
		// 加入前10个观战者就可以了,这10个是在战斗场景里可以看见的
		int num = 0;
		for (int i = BattleField.FIRST_WATCHER_BATTLE_INDEX; i <= watchers.lastKey()&&num<BattleField.WATCHERS_IN_BATTLE; i++) {
			xbean.Fighter f = watchers.get(i);
			if (f!=null) {
			addsnd2.fighterlist.add(BattleField.getWatcherFighterInfo(watchers.get(i), battle));
		     num++;
			}
			if (i >= (BattleField.FIRST_WATCHER_BATTLE_INDEX +BattleField.WATCHERS_IN_BATTLE))
				logger.error("watch battle error,battleIndex:"+battleIndex+"watchers' keys"+battle.getWatchers().keySet());
		}
		psendWhileCommit(watcherid, addsnd2);
		
		// 进入观战发送九屏协议,这个所有观战者都要发
		
		GsClient.pSendWhileCommit(new GSetRoleBattleInfo(watcherid, 2));
		
//		SSetRoleBattleInfo sSetRoleBattleInfo = new SSetRoleBattleInfo(watcherid, 2);
//		fire.pb.move.Role mrole = fire.pb.move.RoleManager.getInstance().getRoleByID(watcherid);
//		mrole.sendAroundExceptMe(sSetRoleBattleInfo);
		return true;*/
	}


	private boolean addwatcher2Battle(xbean.BattleInfo battle, long watcherid)
	{
		//在这个地方拿watcherid锁
		Set<Long> lockroleids = new HashSet<Long>();
		lockroleids.addAll(battle.getRoleids().keySet());
		lockroleids.add(watcherid);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, lockroleids));
		fire.pb.PropRole watcher = new fire.pb.PropRole(watcherid, false);
		fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(watcherid, true);
		
		if(xtable.Watcherid2battleid.select(watcherid)!=null){
			return true;
		}
		if (xtable.Roleid2battleid.select(watcherid)!=null){
			fire.pb.talk.MessageMgr.sendMsgNotify(watcherid,160495, null);
			return true;
		}

		// 观战者不能超过200人
		if (battle.getAudience().size() >= PropConf.Battle.WATCHERS_TOTOAL_NUMS)
			return false;
		boolean isGm = false;
		Integer userID = xtable.Roleid2userid.select(watcherid);
		if (userID == null){
			logger.info("watch battle can't find userid.watcherid:"+watcherid);
			return false;
		}

		boolean findRole = false;
		int enemyside = 0;
		int remainTime = 0;
		
		// 找到被观战的人的fighter,有可能是参战,也可能是观战者
		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		fighters.addAll(battle.getFighters().values());
		fighters.addAll(battle.getWatchers().values());
		for (xbean.Fighter f : fighters) {
			if(battle.getBattletype() == BattleType.BATTLE_CLAN_FIGHT)
			{   
				if(f.getFighterkey() < BattleField.GUEST_FIRST_ID)
				{
					findRole = true;
					if(xtable.Properties.selectClankey(f.getUniqueid()) != watcher.getClanKey())
						enemyside = 1;
					break;
				}
				else if(f.getFighterkey() < BattleField.FIRST_WATCHER_BATTLE_INDEX)
				{
					findRole = true;
					if(xtable.Properties.selectClankey(f.getUniqueid()) != watcher.getClanKey())
						enemyside = 0;
					break;
				}	
			}
			else if (f.getUniqueid() == roleid) 
			{
				findRole = true;
				// 不管是观战还是参战,都有主客方,如果玩家是点击观战者进入观战,则继承该观战者的主客属性
				if (!f.getIshost())
					enemyside = 1;
				break;
			}
		}
		
		if (battle.getBattletype() == BattleType.BATTLE_INST_BATTLE) {
			enemyside = 0;
		}
		else {
			if (!findRole)
				return false;
		}
		
		// 如果是等待操作的phase,发送剩余时间,否则保持0不变
		if (battle.getPhase() == BattleInfo.PHASE_WAITOPERATION)
			remainTime = (int)(BattleField.ACTION_TIMEOUT_DELAY*battle.getGmactiontimetimes()/10.0 -  ((System.currentTimeMillis() - battle.getRoundtime()) / 1000));

		// 观战玩家生成一个fighterInfo
		xbean.Fighter fighter = xbean.Pod.newFighter();
		// 观战玩家生成一个Fighter
		// 观战的id从31开始,如果有人退出观战,下个人进入观战应该是用上次退出的人的观战id
		int battleIndex = getBattleIndex(battle.getAudience(),battle.getWatchers());
		if (battleIndex==0)
			return false;
		fighter.setBattleindex(battleIndex);
		fighter.setFightertype(xbean.Fighter.FIGHTER_ROLE);
		fighter.setUniqueid(watcherid);
		fighter.setFightername(watcher.getName());
		fighter.setInihp(watcher.getHp());
		fighter.setInilevel(erole.getLevel());
		fighter.setInimp(watcher.getMp());
		// 观战也有主客之分的
		fighter.setIshost(enemyside == 0 ? true : false);
		// 加入battleinfo中
		battle.getWatchers().put(fighter.getBattleindex(),fighter);
		battle.getAudience().put(watcherid, fighter.getBattleindex());
		xtable.Watcherid2battleid.insert(watcherid, battle.getBattleid());
		FighterInfo fi = BattleField.getWatcherFighterInfo(fighter, battle);
		
		SSendWatchBattleStart watchBattleStart = new SSendWatchBattleStart(enemyside, remainTime, battle.getBattletype(),battle.getRound(),battle.getHostform(),battle.getGuestform(),battle.getHostformlevel(), battle.getGuestformlevel(), (byte)battle.getBackground(),(byte)battle.getBgm(), 0);
		psendWhileCommit(watcherid, watchBattleStart);
		
		// 通知其他人新加入了一个观战者,前10的观战者进入了才通知,后面的不用通知
		if (battleIndex < (BattleField.FIRST_WATCHER_BATTLE_INDEX +PropConf.Battle.WATCHERS_IN_BATTLE)&&!isGm) {
			SSendAddFighters addsnd = new SSendAddFighters();
			addsnd.fighterlist.add(fi);
			Set<Long> allFightersID = battle.getRoleids().keySet();
			Set<Long> receiverSet = new HashSet<Long>();
			receiverSet.addAll(allFightersID);
			Collection<Long> audiences = battle.getAudience().keySet();
			if (audiences.size() >= 1) {
				receiverSet.addAll(audiences);
			}
			if (battleIndex >= (BattleField.FIRST_WATCHER_BATTLE_INDEX +PropConf.Battle.WATCHERS_IN_BATTLE))
				logger.error("watch battle error,battleIndex:"+battleIndex+"watchers' keys"+battle.getWatchers().keySet());
			psendWhileCommit(receiverSet, addsnd);
		}
		// 给这个观战者发送其他人的消息,注意如果观战者超过10人,则只要发送前10个人的信息就可以了,剩下的观战者相当于透明的
		SSendAddFighters addsnd2 = new SSendAddFighters();
	
		for (xbean.Fighter f : battle.getFighters().values()) {
			if (!BattleField.checkOutBattle(battle, f.getBattleindex())) 
			addsnd2.fighterlist.add(BattleField.getFighterInfo(f, battle));
		}
		NavigableMap<Integer, Fighter> watchers = battle.getWatchers();
		// 加入前10个观战者就可以了,这10个是在战斗场景里可以看见的
		int num = 0;
//		for (int i = BattleField.FIRST_WATCHER_BATTLE_INDEX; i <= watchers.lastKey()&&num<PropConf.Battle.WATCHERS_IN_BATTLE; i++) {
		for (int i = BattleField.FIRST_WATCHER_BATTLE_INDEX; i <= watchers.lastKey()&&i < (BattleField.FIRST_WATCHER_BATTLE_INDEX +PropConf.Battle.WATCHERS_IN_BATTLE); i++) {
			xbean.Fighter f = watchers.get(i);
			if (f!=null) {
			addsnd2.fighterlist.add(BattleField.getWatcherFighterInfo(watchers.get(i), battle));
		     num++;
			}
			if (i >= (BattleField.FIRST_WATCHER_BATTLE_INDEX +PropConf.Battle.WATCHERS_IN_BATTLE))
				logger.error("watch battle error,battleIndex:"+battleIndex+"watchers' keys"+battle.getWatchers().keySet());
		}
		psendWhileCommit(watcherid, addsnd2);
		
		// 进入观战发送九屏协议,这个所有观战者都要发
		GsClient.pSendWhileCommit(new GSetRoleBattleInfo(watcherid, 2));
		return true;
	}
	
	private int getBattleIndex(Map<Long, Integer> audiences, NavigableMap<Integer, Fighter> watchers)
	{
		if (watchers.size() == 0)
			return BattleField.FIRST_WATCHER_BATTLE_INDEX;
		int lastkey = watchers.lastKey();
		if (lastkey + 1 == audiences.size() + BattleField.FIRST_WATCHER_BATTLE_INDEX)
			return lastkey + 1;
		// 如果不等,说明有人退出了观战,找到这个空缺的位置
		for (int i = BattleField.FIRST_WATCHER_BATTLE_INDEX; i < lastkey; i++)
		{
			if (watchers.containsKey(i))
				continue;
			else
				return i;
		}
		return lastkey + 1;
	}

}
