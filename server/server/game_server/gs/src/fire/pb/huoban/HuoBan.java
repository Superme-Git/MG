package fire.pb.huoban;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.log.Logger;
import fire.pb.attr.AttrType;
import fire.pb.effect.HuoBanImpl;
import fire.pb.effect.Role;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.util.CalendarUtil;

public class HuoBan {
	
	public static Set<Integer> clientAttrs = new HashSet<Integer>();
	static
	{
		clientAttrs.add(AttrType.MAX_HP); 	//生命
		clientAttrs.add(AttrType.MAX_MP);	//魔法
		clientAttrs.add(AttrType.ATTACK);	//物攻
		clientAttrs.add(AttrType.DEFEND);	//物防
		clientAttrs.add(AttrType.MAGIC_ATTACK);	//法攻
		clientAttrs.add(AttrType.MAGIC_DEF); 	//法防
		clientAttrs.add(AttrType.SPEED);		//速度
		clientAttrs.add(AttrType.MEDICAL);		//治疗强度
		clientAttrs.add(AttrType.SEAL);		//控制命中
		clientAttrs.add(AttrType.UNSEAL);	//控制抗性
	}
	
	private final Logger logger = Logger.getLogger("HUOBAN");
	private xbean.HuoBanInfo huobaninfo;
//	private final boolean readonly;
	
	public SHeroBaseInfo huobanCfg;
	
	public HuoBan(xbean.HuoBanInfo huobanInfo, boolean readonly) {
		this.huobaninfo = huobanInfo;
//		this.readonly = readonly;
		
		huobanCfg = ConfigManager.getInstance().getConf(SHeroBaseInfo.class).get(huobaninfo.getId());
		if (huobanCfg == null)
			logger.error("huoban cfg info is null, config id:" + huobaninfo.getId());
	}
	
	public xbean.HuoBanInfo getHuoBanInfo() {
		return huobaninfo;
	}
	
	public static HuoBan getHuoBan(long roleId, int huobanId, boolean readonly) {
		HuoBanColumn huobanCol = HuoBanColumn.getHuoBanColumn(roleId, readonly);
		return huobanCol.getHuoBan(huobanId);
	}
	
	public static HuoBan getHuoBan(xbean.HuoBanInfo huobanInfo, boolean readonly) {
		return new HuoBan(huobanInfo, readonly);
	}	
	
	public int getId()
	{
		return huobaninfo.getId();
	}
	
	public int getType() {
		return huobaninfo.getType();
	}
	
	public String getHuoBanName() {
		return huobanCfg.name;
	}
	
	public Role getEffectRole(final long roleid)
	{
		return new HuoBanImpl(huobaninfo, roleid);
	}
	
	public Map<Integer,Float> getL2Attrs(final long roleid)
	{
		Map<Integer,Float> l2attrs = new HashMap<Integer, Float>();
		
		Role ehuoban = getEffectRole(roleid);
		
		for(int attrId : clientAttrs)
		{
			l2attrs.put(attrId, ehuoban.getAttrById(attrId));
		}
		
		return l2attrs;
	}
	
	
	public void setHuoBanState(int nState) {
		this.huobaninfo.setState(nState);
		this.huobaninfo.setSettimes(System.currentTimeMillis());
	}
	
	public void setHuoBanWeekIndex(int id) {
		this.huobaninfo.setWeekindex(id);
	}
	
	public int getHuoBanWeekIndex() {
		return this.huobaninfo.getWeekindex();
	}
	
	public long getSetTimes() {
		return this.huobaninfo.getSettimes();
	}
	
	public int getHuoBanState() {
		return this.huobaninfo.getState();
	}
	
	public void setLevel(final int level) {
		this.huobaninfo.setLevel(level);
	}
	
	
	public int getInfight() {
		return this.huobaninfo.getInfight();
	}
	
	public void setInfight(int fight) {
		this.huobaninfo.setInfight(fight);
	}
	
	
	public Long getHuoBanFinalState() {
		Long nRet = 0L;
		int nState = getHuoBanState();
		switch (nState) {
			case 3:	{
				long nDays = getHuoBanTimes();
				if (nDays < System.currentTimeMillis()) { //免费到期
					setHuoBanState(0);
					nRet = 0L;
				} else {
//					nRet = CalendarUtil.getLastListFormat(nDays) + 10; //分钟
					nRet = CalendarUtil.getLastListSecFormat(nDays) + 10; //分钟
					
				}
				break;
			}
			case 2:
			case 1:
			case 0:	{
				nRet = Long.valueOf(nState);
				break;
			}
		}
		
		return nRet;
	}
	
	public int getWeekFree() {
		if (getHuoBanState() == 2) {
			return 1;
		}
		else 
			return 0;
	}
	
	public void setHuoBanTimes(long times) {
		this.huobaninfo.setTimes(times);
	}
	
	public long getHuoBanTimes() {
		return this.huobaninfo.getTimes();
	}
	
	public void addHuoBanTimes(int day) {
		long nDays = getHuoBanTimes();
		if (nDays < System.currentTimeMillis()) { //免费到期
			nDays = System.currentTimeMillis();
			nDays += CalendarUtil.getTimeByContinueDays(day);			
		} else {
			nDays += CalendarUtil.getTimeByContinueDays(day);
		}
		setHuoBanState(3); //免费使用
		setHuoBanTimes(nDays);
	}
	
	public void addHuoBanTimes(long times) {
		long nDays = getHuoBanTimes();
		if (nDays < System.currentTimeMillis()) { //免费到期
			nDays = System.currentTimeMillis();
			nDays += times;			
		} else {
			nDays += times;
		}
		setHuoBanState(3); //免费使用
		setHuoBanTimes(nDays);
	}
	
//	public List<Integer> getHuoBanSkillsList() {
//		return this.huobaninfo.getSkills();
//	}
//	
//	public int getHuoBanMasterSkill() {
//		return this.huobaninfo.getSkillmaster();
//	}
	
	public int getLevel() {
		return this.huobaninfo.getLevel();
	}
	
	public boolean canFight() {
		// 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数		
		return this.huobaninfo.getState() > 0;
	}

}
