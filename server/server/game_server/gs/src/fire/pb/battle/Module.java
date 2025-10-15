package fire.pb.battle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fire.pb.PReqRoleInfoProc;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.npc.SHireNpc;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.Result;
import fire.pb.team.SZhenFaeffect;
import fire.pb.util.Parser.ID2Odds;
import fire.script.JavaScript;

public class Module implements ModuleInterface{
	
	public final static String MODULE_NAME = "battle";
	static private final Logger logger = Logger.getLogger("BATTLE");
	private final Map<Integer, Map<Integer, FormationConfig>> formLevelMap = new HashMap<Integer, Map<Integer,FormationConfig>>();
	private final Map<Integer, BattleConfig> battleConfigs = new HashMap<Integer, BattleConfig>();
	private java.util.NavigableMap<Integer, SEffectPostions> effectpostions;
	private Map<Integer,SHireNpc> buffnpcs = new HashMap<Integer, SHireNpc>();//key为buffid
	public BattleConfigManager getBattleConfigManager() {
		return BattleConfigManager.getInstance();
	}
	public static Module getInstance()
	{
		return ((fire.pb.battle.Module)ModuleManager.getInstance().getModuleByName(MODULE_NAME));
	}

	@Override
	public void exit()
	{
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		ReloadResult result = fire.pb.skill.Module.getInstance().reload();
		if(!result.isSuccess())//skill
			return result.appendMsg("battle module reload failed, ");
		
		try
		{
			Module m = new Module();
			m.init();
			ModuleManager.getInstance().putModuleByName(MODULE_NAME, m);
			return result;
		}
		catch(Exception e)
		{
			logger.error(e);
			return new ReloadResult(false,"battle module reload failed, ");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() throws Exception{
		logger.info("battle模块初始化开始");
		formLevelMap.clear();
		BattleConfigManager.getInstance().init(fire.pb.main.ConfigManager.getInstance());
		effectpostions = fire.pb.main.ConfigManager.getInstance().getConf(SEffectPostions.class);

		Map<Integer, SFormationConfig> sformbuffs = fire.pb.main.ConfigManager.getInstance().getConf(SFormationConfig.class);
		for (int i = 0; i < sformbuffs.get(0).getPositions().size(); i++){
			Integer pstr = sformbuffs.get(0).getPositions().get(i);
			FormationConfig.cposition2sposition.put(pstr, FormationConfig.bps[i]);
		}
		
		for(SFormationConfig zfcfg: sformbuffs.values())
		{
			FormationConfig.cposition2fightid.put(zfcfg.id, zfcfg.positions);			
		}		
		
		Map<Integer, SZhenFaeffect> sformbuffsnew = fire.pb.main.ConfigManager.getInstance().getConf(SZhenFaeffect.class);
		for(SZhenFaeffect zfe: sformbuffsnew.values()){
			
			int formId = zfe.getZhenfaid();
			int formLevel = zfe.getZhenfaLv();
			SFormationConfig formbuff = sformbuffs.get(formId);
			
			Map<Integer, FormationConfig> formMap = formLevelMap.get(formId);
			if(formMap == null){
				formMap = new HashMap<Integer, FormationConfig>();
				formLevelMap.put(formId, formMap);
			}
			
			FormationConfig config = new FormationConfig(formId, formLevel);
			config.setExp(zfe.getZhenfaExp());
			
			if (zfe.getEffect() != null){
				for (int i = 0; i < zfe.getEffect().size(); i++){
					String effectstrs = zfe.getEffect().get(i);
					if (effectstrs != null && !effectstrs.equals("") && !effectstrs.equals("-1.0") && !effectstrs.equals("null")){
						config.getEffects()[i] = new HashMap<Integer, Float>();
						String[] strs = effectstrs.split(";");
						for (String str : strs){
							String[] tmpstrs = str.split("=");
							int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0]);
							float effectvalue = Float.valueOf(tmpstrs[1]);
							config.getEffects()[i].put(effectId, effectvalue);
						}
					}
				}
			}
			
			config.setName(formbuff.name);
			
			if (zfe.getEffectloss() != null){
				for (int i = 0; i < zfe.getEffectloss().size(); i++){
					String effectstrs = zfe.getEffectloss().get(i);
					if (effectstrs != null && !effectstrs.equals("") && !effectstrs.equals("-1.0") && !effectstrs.equals("null")){
						config.getSuppressEffects()[i] = new HashMap<Integer, Float>();
						String[] strs = effectstrs.split(";");
						for (String str : strs){
							String[] tmpstrs = str.split("=");
							int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0]);
							float effectvalue = Float.valueOf(tmpstrs[1]);
							config.getSuppressEffects()[i].put(effectId, effectvalue);
						}
					}
				}
			}
			
			
			
			if (formbuff.getPositions() != null){
				for (int i = 0; i < formbuff.getPositions().size(); i++){
					BattleField.Position pos = FormationConfig.getSPosition(formbuff.getPositions().get(i));
					config.getPositions().put(i + 1, pos);
				}
			}
			
			if(formbuff.getSuppress1()!=null&&formbuff.getSuppressparam1()!=null)
			{
				String s1 = formbuff.getSuppress1();
				Float param1 = Float.parseFloat(formbuff.getSuppressparam1());
				Map<Integer, Float> map1 = config.getmapSuppressform1();
				String sup1[] = s1.split(",");
				for (String s : sup1)
				{
					Integer v = Integer.parseInt(s);
					map1.put(v, param1);
				}
			}
			
			if(formbuff.getSuppress2()!=null&&formbuff.getSuppressparam2()!=null)
			{
				String s2 = formbuff.getSuppress2();			
				Float param2 = Float.parseFloat(formbuff.getSuppressparam2());
				Map<Integer, Float> map2 = config.getmapSuppressform2();
				String sup2[] = s2.split(",");
				for (String s : sup2)
				{
					Integer v = Integer.parseInt(s);
					map2.put(v, param2);
				}			
			}			
			formMap.put(formLevel, config);
		}
		logger.info("battle阵型信息初始化完成");
		
		
		monstershowConfigManager = new MonstershowConfigManager(fire.pb.main.ConfigManager.getInstance());
		// Battle AI
		try
		{
			BattleAIManager.getInstance().initBattleAI();
		} catch (Exception e)
		{
			logger.error("Battle AI模块初始化错误。", e);
		}
		logger.info("battle AI信息初始化完成");

		// 战场信息
		Map<Integer, SBattleInfo> sBattleInfos = fire.pb.main.ConfigManager.getInstance().getConf(SBattleInfo.class);
		battleConfigs.clear();
		for (SBattleInfo sbattleInfo : sBattleInfos.values())
		{
			try
			{
				BattleConfig battleconf = new BattleConfig();
				battleconf.id = sbattleInfo.id;
				battleconf.award = sbattleInfo.award;
				battleconf.fightAItype = sbattleInfo.fightAItype;				
				battleconf.monsterNumScript = new JavaScript(sbattleInfo.monsterNumScript);
				battleconf.sceneId = sbattleInfo.scene;
				battleconf.environment = sbattleInfo.environment;
				battleconf.background = sbattleInfo.scene;
				battleconf.bgm = sbattleInfo.bgm;
				battleconf.victoryconditionType=sbattleInfo.victorycondition ;
				battleconf.isNotDecEndure = sbattleInfo.isNotDecEndure;
				battleconf.xiezhannum = sbattleInfo.xiezhan;
				battleconf.cameratype = sbattleInfo.cameratype;
				
				if(sbattleInfo.battlelevel != null)
					battleconf.leveljs = new JavaScript(sbattleInfo.battlelevel);
				if (sbattleInfo.battleAI != null && !sbattleInfo.battleAI.equals("0"))
				{
					String[] strs = sbattleInfo.battleAI.split(";");
					for (String str : strs)
						battleconf.battleAIs.add(Float.valueOf(str).intValue());

				}
				
				if (sbattleInfo.randompos != null) //站位随机 by changhao
				{
					String[] strs = sbattleInfo.randompos.split(";");
					for (String str : strs)
					{
						battleconf.randomPos.add(Integer.valueOf(str).intValue());
					}
				}
				
				if (sbattleInfo.positions.size() != BattleField.A_SIDE_FIGHTER_NUM * 2)
				{
					logger.error("战斗信息：" + battleconf.id + " 战斗点位信息不够28个。");
					continue;// 没有配全28个点位
				}
				// 初始化每个点位的怪随机信息
				for (int index = 0; index < sbattleInfo.positions.size(); index++)
				{
					String position = sbattleInfo.positions.get(index);
					if (!position.equals("0"))
					{

						ID2Odds id2odds = fire.pb.util.Parser.parseIdAndOdds(position);
						if (id2odds != null)
							battleconf.monsterRands.put(index + 1, id2odds);
					}
				}
				battleConfigs.put(battleconf.id, battleconf);
			} 
			catch (Exception e)
			{
				logger.error("战场信息模块初始化错误。", e);
			}
		}
		logger.info("战场信息初始化完成");
		
		for(SHireNpc entry : ConfigManager.getInstance().getConf(SHireNpc.class).values())
		{
			buffnpcs.put(entry.getBuffID(), entry);
		}
		
		logger.info("battle模块初始化完成");
	}
	
	private MonstershowConfigManager monstershowConfigManager;

	public MonstershowConfigManager getMonstershowConfigManager()
	{
		return monstershowConfigManager;
	}

	public FormationConfig getFormationById(int formId, int formLevel){
		if(formLevel == 0)
			formId = 0;
		Map<Integer, FormationConfig> levelFormBuff = formLevelMap.get(formId);
		if(levelFormBuff != null){
			return levelFormBuff.get(formLevel);
		}
		
		return null;
	}

	public Map<Integer, BattleConfig> getBattleConfigs()
	{
		return battleConfigs;
	}

	public java.util.NavigableMap<Integer, SEffectPostions> getEffectPostions()
	{
		return effectpostions;
	}
	
	public List<Integer> getDiffusePoss(int nindex)
	{
		if(effectpostions.get(nindex) != null)
			return effectpostions.get(nindex).diffuseposs;
		if (BattleField.logger.isInfoEnabled()) {BattleField.logger.info("getDiffusePoss return null！！！   FighterId="+nindex);}
		return null;
	}
	
	public List<Integer> getEffectPoss(int nindex)
	{
		if(effectpostions.get(nindex) != null)
			return effectpostions.get(nindex).effectposs;
		if (BattleField.logger.isInfoEnabled()) {BattleField.logger.info("getEffectPoss return null！！！   FighterId="+nindex);}
		return null;
	}
	
	public void checkBattleBuff(final long roleid) {
		fire.pb.buff.BuffRoleImpl bri = new fire.pb.buff.BuffRoleImpl(roleid, false);
		fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(roleid, false);
		SRefreshRoleData sendrole = new SRefreshRoleData();
		if (bri.existBuff(BuffConstant.ROLE_RECOVER_HP) && role.getHp() != 0) {
			ConstantlyBuff buff = bri.getBuff(BuffConstant.ROLE_RECOVER_HP);
			// int res = role.getMaxHp() - role.getHp();
			long res = (long) (role.getUplimithp() - role.getHp());
			if (buff.getAmount() < res)
				res = buff.getAmount();
			if (res > 0) {
				role.addHp((int) res);
				sendrole.datas.put(AttrType.HP, (float) role.getHp());
				buff.setAmount(buff.getAmount() - res);
				if (buff.getAmount() == 0) {
					removeRecoverBuff(bri, BuffConstant.ROLE_RECOVER_HP);
				} else {
					Result result = new Result(true);
					result.addAddedBuff(buff);
					bri.psendSBuffChangeResult(result);
				}
			}
		}
		if (bri.existBuff(BuffConstant.ROLE_RECOVER_MP) && role.getHp() != 0) {
			ConstantlyBuff buff = bri.getBuff(BuffConstant.ROLE_RECOVER_MP);
			long res = (long) (role.getMaxMp() - role.getMp());
			if (buff.getAmount() < res)
				res = buff.getAmount();
			if (res > 0) {
				role.addMp((int) res);
				sendrole.datas.put(AttrType.MP, (float) role.getMp());
				buff.setAmount(buff.getAmount() - res);
				if (buff.getAmount() == 0) {
					removeRecoverBuff(bri, BuffConstant.ROLE_RECOVER_MP);
				} else {
					Result result = new Result(true);
					result.addAddedBuff(buff);
					bri.psendSBuffChangeResult(result);
				}
			}
		}
		if (!sendrole.datas.isEmpty())
			mkdb.Procedure.psendWhileCommit(roleid, sendrole);

		fire.pb.effect.PetImpl pet = null;
		PetColumn depotcol = new PetColumn(roleid, PetColumnTypes.PET, false);
		Set<Integer> existIds = depotcol.getPetsMap().keySet();
		if (existIds.size() > 0) {
			for (int petkey : existIds) {
				// Integer petkey =
				// protocolpet.key;//xtable.Properties.selectFightpetkey( roleid
				// );
				if (petkey != -1 && petkey != 0) {
					pet = new fire.pb.effect.PetImpl(roleid, petkey, false);
					SRefreshPetData sendpet = new SRefreshPetData();
					if (bri.existBuff(BuffConstant.ROLE_RECOVER_HP)) {
						ConstantlyBuff buff = bri
								.getBuff(BuffConstant.ROLE_RECOVER_HP);
						long res = (long) (pet.getMaxHp() - pet.getHp());
						if (buff.getAmount() < res)
							res = buff.getAmount();
						if (res > 0) {
							pet.addHp((int) res);
							sendpet.datas.put(AttrType.HP, (float) pet.getHp());
							buff.setAmount(buff.getAmount() - res);
							if (buff.getAmount() == 0) {
								removeRecoverBuff(bri,
										BuffConstant.ROLE_RECOVER_HP);
							} else {
								Result result = new Result(true);
								result.addAddedBuff(buff);
								bri.psendSBuffChangeResult(result);
							}
						}
					}
					if (bri.existBuff(BuffConstant.ROLE_RECOVER_MP)
							&& pet != null) {
						ConstantlyBuff buff = bri
								.getBuff(BuffConstant.ROLE_RECOVER_MP);
						long res = (long) (pet.getMaxMp() - pet.getMp());
						if (buff.getAmount() < res)
							res = buff.getAmount();
						if (res > 0) {
							pet.addMp((int) res);
							sendpet.datas.put(AttrType.MP, (float) pet.getMp());
							buff.setAmount(buff.getAmount() - res);
							if (buff.getAmount() == 0) {
								removeRecoverBuff(bri,
										BuffConstant.ROLE_RECOVER_MP);
							} else {
								Result result = new Result(true);
								result.addAddedBuff(buff);
								bri.psendSBuffChangeResult(result);
							}
						}
					}
					if (!sendpet.datas.isEmpty()) {
						sendpet.columnid = fire.pb.pet.PetColumnTypes.PET;
						sendpet.petkey = petkey;
						mkdb.Procedure.psendWhileCommit(roleid, sendpet);
					}
				}
			}
		}

		mkdb.Procedure.pexecuteWhileCommit(new PReqRoleInfoProc(roleid));
	}
	
	private boolean removeRecoverBuff(fire.pb.buff.BuffRoleImpl bri, int buffId)
	{
		bri.removeCBuffWithSP(buffId);
		return false;
	}
	
	public SHireNpc getSHireNpcByBuffId(int buffId)
	{
		return buffnpcs.get(buffId);
	}
	
	public static void serverShutdown()
	{
		final Map<Long,Integer> battleresults = new HashMap<Long, Integer>();
		
		try
		{
			new mkdb.Procedure()
			{
				protected boolean process() throws Exception {
					mkdb.TTableCache<Long, xbean.BattleInfo> btable = xtable.Battle.getCache();
					btable.walk(new mkdb.CacheQuery<Long, xbean.BattleInfo>()
					{

						@Override
						public void onQuery(Long battleId, xbean.BattleInfo battle)
						{
							int result = 0;
							if (battle.getBattletype() == BattleType.BATTLE_BINGFENG_WAR)
								result = BattleField.BATTLE_DRAW;// 冰封王座战斗算平
							else if (battle.getBattletype() % 10 == 0)
								result = BattleField.BATTLE_LOSE;// PVE战斗算输
							else
								result = BattleField.BATTLE_DRAW;// PVP战斗算平
							battleresults.put(battleId, result);
						}

					});
					return true;
				};
			}.submit().get();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		for(Map.Entry<Long, Integer> entry : battleresults.entrySet())
		{
			try
			{
				new PBattleEnd(entry.getKey(), entry.getValue()).submit().get();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}	
}
