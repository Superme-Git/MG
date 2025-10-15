package fire.pb.huoban;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.PLevelUpProc;
import fire.pb.attr.AttrType;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.npc.SHeroWeekFreeInfo;
import mkdb.Procedure;

public class Module implements ModuleInterface {
	
	public final static String MODULE_NAME = "huoban";
	public static final Logger logger = Logger.getLogger("HUOBAN");
	
	private static Map<Integer, SHeroBaseInfo> huoBanBaseInfo = new HashMap<Integer, SHeroBaseInfo>();
	private static Map<Integer, SHeroWeekFreeInfo> heroFreeInfo = new HashMap<Integer, SHeroWeekFreeInfo>();
	
	public static Module getInstance()
	{
		return ((fire.pb.huoban.Module)ModuleManager.getInstance().getModuleByName(MODULE_NAME));
	}
	
	@Override
	public void exit()
	{
	}
	
	@Override
	public void init() throws Exception{
		logger.info("huoban模块初始化开始");
		
		huoBanBaseInfo = fire.pb.main.ConfigManager.getInstance().getConf(SHeroBaseInfo.class);
		heroFreeInfo = fire.pb.main.ConfigManager.getInstance().getConf(SHeroWeekFreeInfo.class);
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.WeekIndex weekIndex = xtable.Weekindexs.get(1);
				if (weekIndex == null) {
					weekIndex = xbean.Pod.newWeekIndex();
					xtable.Weekindexs.insert(1, weekIndex);
					weekIndex.setIndex(1);
				}
				
				int freeIndex = weekIndex.getIndex();
				if (!heroFreeInfo.containsKey(freeIndex)) {
					weekIndex.setIndex(1);
					freeIndex = 1;
				}
				
				logger.info("freeIndex:" + freeIndex);
				return true;
			}
			
		}.submit();
		
		//初始化伙伴内存表
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				int roleMaxLevel = PLevelUpProc.getMaxLevel();
				
				for (Map.Entry<Integer, SHeroBaseInfo> huobanInfos : huoBanBaseInfo.entrySet()) {
					xbean.HuoBanShuxingLevel huobanShuxingLv = xtable.Huobanproperty.get(huobanInfos.getKey());
					if (huobanShuxingLv == null) {
						huobanShuxingLv = xbean.Pod.newHuoBanShuxingLevel();
						xtable.Huobanproperty.insert(huobanInfos.getKey(), huobanShuxingLv);
					}
					
					for (int i = 1; i <= roleMaxLevel; i ++) {
						xbean.HuoBanshuxing huobanShuxing = huobanShuxingLv.getHuobans().get(i);
						if (huobanShuxing == null) {
							huobanShuxing = xbean.Pod.newHuoBanshuxing();
							huobanShuxingLv.getHuobans().put(i, huobanShuxing);
						}
						
						huobanShuxing.setLevel(i);
						
						huobanShuxing.getSkills().clear();
						huobanShuxing.getSkills().addAll(huobanInfos.getValue().skillid);
						if (huobanInfos.getValue().battle_ai != null) {
							huobanShuxing.getBattleai().clear();
							huobanShuxing.getBattleai().addAll(huobanInfos.getValue().battle_ai);
						}
						
						huobanShuxing.setSkillmaster(huobanInfos.getValue().first_skill);
						huobanShuxing.setLevelfactor((float) huobanInfos.getValue().levelfactor);
						huobanShuxing.setLevelconstant(huobanInfos.getValue().levelconstant);
						//名字
						huobanShuxing.setHuobanname(huobanInfos.getValue().name);
						//职业
						huobanShuxing.setSchool(huobanInfos.getValue().school);
						//造型id
						huobanShuxing.setShape(huobanInfos.getValue().shapeid);
						//大小
						huobanShuxing.setBodytype((float)huobanInfos.getValue().bodytype);
						
						//成长
						float val = 0.0f;
						for (int attrId : HuoBan.clientAttrs) {
							switch (attrId)
							{
							case AttrType.MAX_HP:
								val = (float)huobanInfos.getValue().life + (float)huobanInfos.getValue().life_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.MAX_MP:
								val = (float)huobanInfos.getValue().magic + (float)huobanInfos.getValue().magic_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.ATTACK:
								val = (float)huobanInfos.getValue().phyatk + (float)huobanInfos.getValue().phyatk_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.DEFEND:
								val = (float)huobanInfos.getValue().phydef + (float)huobanInfos.getValue().phydef_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.MAGIC_ATTACK:
								val = (float)huobanInfos.getValue().magatk + (float)huobanInfos.getValue().magatk_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.MAGIC_DEF:
								val = (float)huobanInfos.getValue().magdef + (float)huobanInfos.getValue().magdef_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.SPEED:
								val = (float)huobanInfos.getValue().speed + (float)huobanInfos.getValue().speed_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.MEDICAL:
								val = (float)huobanInfos.getValue().treat + (float)huobanInfos.getValue().treat_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.SEAL:
								val = (float)huobanInfos.getValue().hit + (float)huobanInfos.getValue().hit_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							case AttrType.UNSEAL:
								val = (float)huobanInfos.getValue().resistance + (float)huobanInfos.getValue().resistance_growup*i;
								huobanShuxing.getFinalattrs().put(attrId, val);
								break;
							default:
								break;
							}
						}
					}
				}
				
				logger.info("伙伴内存表初始化成功");
				return true;
			}
		}.submit();
		
	}

	@Override
	public ReloadResult reload() throws Exception {		
		return null;
	}
	
	public Map<Integer, SHeroBaseInfo> getHuoBanBaseInfo() {
		return huoBanBaseInfo;
	}
	
	public SHeroWeekFreeInfo getHeroWeekFreeInfo() {
		xbean.WeekIndex weekIndex = xtable.Weekindexs.select(1);
		if (weekIndex == null)
			return null;
		return heroFreeInfo.get(weekIndex.getIndex());
	}
	
	public void SetHeroWeekFree() {
		mkdb.Procedure setHeroFree = new Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.WeekIndex weekIndex = xtable.Weekindexs.get(1);
				if (weekIndex == null) {
					weekIndex = xbean.Pod.newWeekIndex();
					xtable.Weekindexs.insert(1, weekIndex);
					weekIndex.setIndex(1);
				}
				else {
					weekIndex.setIndex(weekIndex.getIndex() + 1);
					if (!heroFreeInfo.containsKey(weekIndex.getIndex())) {
						weekIndex.setIndex(1);
					}
				}
				
				logger.info("set free index:" + weekIndex.getIndex());
				return true;
			}
			
		};
		
		if (mkdb.Transaction.current() != null) {
			setHeroFree.call();
		}
		else {
			setHeroFree.submit();
		}
	}
	
	public static Map<Integer, Float> getHuobanEffect(final int rolelv, final int huobanid) {
		xbean.HuoBanShuxingLevel huobanShuxingLv = xtable.Huobanproperty.select(huobanid);
		if (huobanShuxingLv == null)
			return null;
		xbean.HuoBanshuxing huobanShuxing = huobanShuxingLv.getHuobans().get(rolelv);
		if (huobanShuxing == null)
			return null;
		
		return huobanShuxing.getEffects();
	}
	
	public static Map<Integer, Float> getHuobanFinalAttrs(final int rolelv, final int huobanid) {
		xbean.HuoBanShuxingLevel huobanShuxingLv = xtable.Huobanproperty.select(huobanid);
		if (huobanShuxingLv == null)
			return null;
		xbean.HuoBanshuxing huobanShuxing = huobanShuxingLv.getHuobans().get(rolelv);
		if (huobanShuxing == null)
			return null;
		
		return huobanShuxing.getFinalattrs();
	}
	
	public static xbean.HuoBanshuxing getHuobanshuxing(final int rolelv, final int huobanid) {
		xbean.HuoBanShuxingLevel huobanShuxingLv = xtable.Huobanproperty.select(huobanid);
		if (huobanShuxingLv == null)
			return null;
		xbean.HuoBanshuxing huobanShuxing = huobanShuxingLv.getHuobans().get(rolelv);
		if (huobanShuxing == null)
			return null;
		
		return huobanShuxing;
	}
	
	/**
	 * 设置额外的周免费伙伴数量
	 * @param roleid
	 * @param count
	 */
	public static void setVipHuoBanCount(final long roleid, final int count) {
		mkdb.Procedure pSetVipHuoban = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				HuoBanColumn huobanColumn = HuoBanColumn.getHuoBanColumn(roleid, false);
				huobanColumn.setVipHuoBanCnt(count);
				
				new PSetVipHuoban(roleid).call();
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			pSetVipHuoban.call();
		}
		else {
			pSetVipHuoban.submit();
		}
	}
}
