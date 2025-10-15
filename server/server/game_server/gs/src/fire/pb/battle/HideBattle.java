package fire.pb.battle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.script.ScriptException;

import org.apache.log4j.Logger;

import fire.pb.battle.specialevent.RunawaySpyBattleHandler;
import fire.pb.battle.specialevent.SpecialHideBattleHandler;
import fire.pb.map.SMineArea;
//import fire.pb.map.SSpecialEvent;
import fire.pb.util.Misc;
import fire.pb.util.Parser.ID2Odds;
import fire.script.ParseStringExpression;
import mkdb.Transaction;


/**暗雷战斗处理*/
public class HideBattle {
	private final long roleID;			// 参战(队长)角色ID
	private final int teamsize;			// 队伍人数,如果单人则为1
	private final int areaconf;			// 暗雷区域类型	
	private int monstertype = 0;			// 战斗类型(参考下面的常量值)	
	
	public static final int HIDEBATTLE_TYPE_COMMON = 0;				// 一般暗雷战斗
	public static final int HIDEBATTLE_TYPE_BABY = 1;				// 宠物pet战斗
	public static final int HIDEBATTLE_TYPE_BOSS = 2;				// BOSS战斗
	public static final int HIDEBATTLE_TYPE_SPECIAL = 3;			// 特殊事件战斗
	
	
	public HideBattle(final long roleID, final int areaconf, final int teamsize){
		this.roleID = roleID;
		this.areaconf = areaconf;
		this.teamsize = teamsize;
	}
	public static final Logger logger = Logger.getLogger("BATTLE");
	public static Random random = new Random();
	private static ParseStringExpression pse = new ParseStringExpression();
	private static Map<String, Object> pseParas = new HashMap<String, Object>();
	/**获取怪物个数*/
	public int getMonsterNum(){
		//队伍人数 ≤ 出战怪物个数 ≤INT(25*(队伍人数+1)/14)
		int monsterNum=Misc.getRandomBetween(teamsize, 25 *(teamsize+1)/14 );
		logger.debug("teamsize:"+teamsize+"monsterNum:"+monsterNum);
		return monsterNum;
	}
	
	BattleEndHandler handler = null;
	
	public void setBattleEndHandler( BattleEndHandler handler ) {
		this.handler = handler;
	}

	public void launch() {
		
		SMineArea areaInfo = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.SMineArea.class).get(areaconf);
		if(null == areaInfo)
			return;
		int commonrate = calBattleTypeRate(areaInfo.commonrate);
		int babyrate = calBattleTypeRate(areaInfo.babyrate);
		int bossrate = calBattleTypeRate(areaInfo.bossrate);
		int specialrate = calBattleTypeRate(areaInfo.specialrate);
		final int [] probs = {commonrate, babyrate, bossrate, specialrate};
		monstertype = Misc.getProbability(probs, 10000, random);
		if(-1 == monstertype)
			return;
		final BattleConfigManager.MonsterNumConfig monsternumConf
			= Module.getInstance().getBattleConfigManager().getMonsterNumConfig(teamsize);
		if ( monsternumConf == null ) {
			return;
		}
	//	final int monsterNum = getMonsterNum();
		final java.util.List<Integer> monsterIDs = new java.util.LinkedList<Integer>();
		switch(monstertype){
		case HIDEBATTLE_TYPE_COMMON :{
			for(int i = 0; i < monsternumConf.getNormalnum(); i++) {
				final int index = Misc.getProbability(areaInfo.getMonsterrate(), 1000);
				monsterIDs.add(areaInfo.getMonster().get(index));
			}
			for( int i = 0; i < monsternumConf.getHeadnum(); i++ ) {
				final int index = Misc.getProbability(areaInfo.getLeaderrate(), 1000);
				monsterIDs.add(areaInfo.getLeader().get(index));
			}
		}
		break;
		case HIDEBATTLE_TYPE_BABY :
		{			
			final int index = Misc.getProbability(areaInfo.getPetrate(), 1000);
			monsterIDs.add(areaInfo.getPet().get(index));
			for( int i = 1; i < monsternumConf.getNormalnum(); i++ ) {
				final int idx = Misc.getProbability(areaInfo.getMonsterrate(), 1000);
				monsterIDs.add(areaInfo.getMonster().get(idx));
			}
			int s = monsternumConf.getNormalnum() > 0 ? 0 : 1;
			for( int i = s; i < monsternumConf.getHeadnum(); i++ ) {
				final int idx = Misc.getProbability(areaInfo.getLeaderrate(), 1000);
				monsterIDs.add(areaInfo.getLeader().get(idx));
			}
		}
		break;
			case HIDEBATTLE_TYPE_BOSS :{
				int battleid = areaInfo.getBossbattleid();
				PNewBattle newbattle = null;
				if ( handler == null )
					newbattle = new PNewBattle(roleID, battleid );
				else
					newbattle = new PNewBattle( roleID, battleid, handler );
				newbattle.setAreaConf(areaconf);
				newbattle.setAreaType(xbean.BattleInfo.AREA_BATTLEHIDE);
				newbattle.setMonsterType(monstertype);
				newbattle.setBattleType(BattleType.BATTLE_BOSS);
				
				if(Transaction.current()!=null)
				{
					mkdb.Procedure.pexecute(newbattle);
				}
				else
				{
					newbattle.submit();
				}
				return;
			}
			case HIDEBATTLE_TYPE_SPECIAL :{
				ID2Odds id2odds = fire.pb.util.Parser.parseIdAndOdds(areaInfo.getSpecialevents());
				int battleconfId = id2odds.ids[Misc.getProbability(id2odds.odds)];
				
				if(battleconfId>= 9116 && battleconfId <= 9199)
					handler = new RunawaySpyBattleHandler();//逃跑的密探
				else
					handler = new SpecialHideBattleHandler();
				PNewBattle newbattle = new PNewBattle(roleID, battleconfId, handler);
				newbattle.setAreaConf(areaconf);
				newbattle.setAreaType(xbean.BattleInfo.AREA_BATTLEHIDE);
				newbattle.setMonsterType(monstertype);
				if(Transaction.current()!=null)
				{
					mkdb.Procedure.pexecute(newbattle);
				}
				else
				{
					newbattle.submit();
				}
				return;
			}
			
			default : break;
		}
		if(monsterIDs.size() != 0){
			//BattleField.launchBattle(roleID, monsterIDs);
			PNewBattle batproc;
			List<Monster> monsters=BattleField.genMonsters(monsterIDs);
		
			if ( handler == null )
				batproc = new PNewBattle(roleID, monsters );
			else
				batproc = new PNewBattle( roleID, monsters, handler );
			if (monstertype==HIDEBATTLE_TYPE_BOSS)
			{//狗屎。上面105行的逻辑已经return。这段代码有屁用
				monsters.get(0).setFighterId(BattleField.GUEST_FIRST_ID);
				batproc.setBattleType(BattleType.BATTLE_BOSS);
			}
			else
			{
				batproc.setBattleType( BattleType.BATTLE_HIDEAREA );
			}
			//判断是否是含有宝宝的战斗，如果有，设置标示
			if(monstertype==HIDEBATTLE_TYPE_BABY){
				batproc.setBabyfight(true);
			}
			
			
			
			batproc.setAreaConf(areaconf);
			batproc.setAreaType(xbean.BattleInfo.AREA_BATTLEHIDE);
			batproc.setMonsterType(monstertype);
			if(Transaction.current()!=null)
				mkdb.Procedure.pexecute(batproc);
			else
				batproc.submit();
		}
	}	
	
	/**calBattleTypeRate:(这里用一句话描述这个方法的作用)*/	
	private int calBattleTypeRate(String commonrate) {
		pseParas.put("TeamNum", teamsize);
		int rate = 0;
		try {
			Double value = pse.ParseStr(commonrate, pseParas);
			rate = value.intValue();
			return rate;
		} catch (ScriptException e) {
			logger.error("error when parsing battleTypeRate String:"+commonrate, e);
			return 0;
		}
		
	}
}
