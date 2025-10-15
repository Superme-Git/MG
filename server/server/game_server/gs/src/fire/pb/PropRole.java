package fire.pb;

import java.util.Map;

import fire.pb.statistics.StatisticUtil;

/**
 * 封装对xtable.Properties 的操作 涉及的属性，都是非 buff影响的属性
 * 
 * 
 */
public class PropRole
{
	private long roleid;
	private final xbean.Properties prop;
	private boolean readonly;
	final fire.pb.role.RoleModData moddata;

	/**
	 * 获取PropRole的工厂方法
	 * @param roleid
	 * @param readonly
	 * @return
	 */
	public static PropRole getPropRole(long roleid, boolean readonly)
	{
		xbean.Properties prop = null;
		if (readonly)
		{
			prop = xtable.Properties.select(roleid);
		} else
		{
			prop = xtable.Properties.get(roleid);
		}
		if(prop == null)
			return null;
		return new PropRole(roleid, prop, readonly);
	}
	
	
	private PropRole(long roleid, xbean.Properties prop, boolean readonly)
	{
		this.roleid = roleid;
		this.prop = prop;
		this.readonly = readonly;
		moddata = RoleConfigManager.getRoleModData(prop.getRace());
	}
	
	public PropRole(long roleid, boolean readonly)
	{
		this.roleid = roleid;
		this.readonly = readonly;

		if (readonly)
		{
			prop = xtable.Properties.select(roleid);
		} else
		{
			prop = xtable.Properties.get(roleid);
		}

		if (prop == null)
		{
			//Module.logger.error("服务器找不到该角色：  " + roleid);
			throw new RuntimeException("Unexist this role:"+roleid);
		}
		moddata = RoleConfigManager.getRoleModData(prop.getRace());
	}
	public xbean.Properties getProperties()
	{
		return prop;
	}
	public long getRoleId()
	{
		return roleid;
	}
	/**
	 * 获得名称
	 * 
	 * @return
	 */
	public String getName()
	{
		return prop.getRolename();
	}

	/**
	 * 
	 */
	public int getLevel()
	{
		return prop.getLevel();
	}
	/**
	 * 
	 */
	public int getViplevel()
	{
		return prop.getViplevel();
	}
	
	/**
	 * 获得当前血量
	 */
	public int getHp()
	{
		return prop.getHp();
	}

	/**
	 * 获得当前法力
	 * 
	 * @return
	 */
	public int getMp()
	{
		return prop.getMp();
	}

	/**
	 * 获得当前经验值
	 * 
	 * @return
	 */
	public long getCurExp()
	{
		return prop.getExp();
	}
	
	public long getAllExp()
	{
		return prop.getAllexp(); 
	}
	

	/**
	 * 获得参战宠物的key, key < 0 为没有参战宠物
	 * 
	 * @return
	 */
	public int getFightpetkey()
	{
		return prop.getFightpetkey();
	}

	/**
	 * 设置参战宠物的key，此处不对key的合法性作检查
	 * 
	 * @param petkey
	 */
	public void setFightpetkey(int petkey)
	{
		if (readonly)
			throw new RuntimeException("Wrong invoke");
		prop.setFightpetkey(petkey);
	}

	public int getShowpetkey()
	{
		return prop.getShowpetkey();
	}

	/**
	 * 获得称谓信息
	 */
	public xbean.TitleInfo getTileInfo()
	{
		int titileId = prop.getTitle();
		xbean.TitleInfo info = prop.getTitles().get(titileId);
		return info;
	}

	public int getTitleID()
	{
		return prop.getTitle();
	}

	/**
	 * 给角色增加一个称谓
	 * 
	 * @param titleid
	 * @param name
	 * @param time
	 */
	public void setTitleInfo(int titleid, String name, int time)
	{
		final xbean.TitleInfo value = xbean.Pod.newTitleInfo();
		value.setTitleid(titleid);
		value.setTitlename(name);
		value.setAvailtime(time);
		prop.getTitles().put(titleid, value);
	}

	/**
	 * 删除角色的一个称谓
	 * 
	 * @param titileid
	 */
	public void deleteTitleInfo(int titileid)
	{
		prop.getTitles().remove(titileid);
	}

	/**
	 * 获得角色性别
	 */
	public int getSex()
	{
		return prop.getSex();
	}


	/**
	 * 获得角色的门派值
	 * 
	 * @return
	 */
	public int getSchool()
	{
		return prop.getSchool();
	}

	/**
	 * 获得玩家的造型值
	 * 
	 * @return
	 */
	public int getShape()
	{
		return prop.getShape();
	}
	
	public void setShape(int shape){
		prop.setShape(shape);
	}

	public long getClanKey()
	{
		return prop.getClankey();
	}

	/**
	 * 向角色称谓列表里加一条称谓
	 * 
	 * @param i
	 * @param info
	 */
	public void setTitle(Integer titleId, xbean.TitleInfo info)
	{
		prop.getTitles().put(titleId, info);
	}

	/**
	 * 从角色称谓列表里删除一条称谓
	 * 
	 * @param titleId
	 */
	public void removeTitle(Integer titleId)
	{
		prop.getTitles().remove(titleId);
	}

	public int getCons()
	{			
		return prop.getBfp().getCons();
	}

	public int getStr()
	{		
		return prop.getBfp().getStr();
	}

	public int getIq()
	{		
		return prop.getBfp().getIq();
	}

	public int getEndu()
	{		
		return prop.getBfp().getEndu();
	}

	public int getAgi()
	{		
		return prop.getBfp().getAgi();
	}

	public long getLastOfflineTime()
	{
		return prop.getOfflinetime();
	}
	
	public boolean hasFormation(int formId)
	{
		if(formId <0 || formId > 10)
			return false;
		if(formId == 0)//普通光环，必会
			return true;
		
		if(prop.getFormationsmap().get(formId) != null){
			return true;
		}else{			
			return false;
		}
	}
	
	public Map<Integer, xbean.FormBean> getFormtions(){
		return prop.getFormationsmap();
	}
	
	public int getFormLevel(int formId){
		xbean.FormBean formLevel = prop.getFormationsmap().get(formId);
		if(formLevel != null){
			return formLevel.getLevel();
		}
		return 0;
	}

	public void setLastOfflineTime(long lastOffline)
	{
		lastOffline -= fire.pb.gm.GM_offlinetime.getAndResetGMOffLineMillis();
		if(prop.getOfflinetime() > prop.getOnlinetime())
			return;//战斗托管时已经设置过了，战斗托管结束时不再设置
		
		prop.setOfflinetime(lastOffline);
		StatisticUtil.updateRoleActiveStats(roleid, (int)((prop.getOfflinetime() - prop.getOnlinetime())/60000));
		//设置累计游戏时间，只有下线时才改变
		prop.setSumonlinetime(prop.getSumonlinetime() + (prop.getOfflinetime() - prop.getOnlinetime() ));
	}
	
	public int getUserid(){
		return prop.getUserid();
	}
	
	public java.util.Map<Integer, xbean.TitleInfo> getRoleTitles(){
		return prop.getTitles();
	}
	
	/**
	 * 当角色在线时，通过此方法获取真正的在线时长
	 * @return
	 */
	public long getRealSumOnlineTime()
	{
		if(prop.getOnlinetime() <= prop.getOfflinetime())
			//说明角色已经下线
			return prop.getSumonlinetime();
		else//当角色在线时，需要加上本次在线的时长
			return prop.getSumonlinetime() + (System.currentTimeMillis() - prop.getOnlinetime());
	}
	
	/**
	 * 得到角色的最近离线时长（即最近多久没有上线了）
	 * 
	 * @return 如果目前正在线则返回-1
	 */
	public long getLastOfflineTimeSpace(){
		if(prop.getOnlinetime() <= prop.getOfflinetime()){
			return System.currentTimeMillis() - prop.getOfflinetime();
		}else{
			return -1;
		}
	}
	
	public int CalcEnergyMaxLimit() {
		return 500 + prop.getLevel() * 20;
	}
	
	public int getDealutFormId(){
		return prop.getDefultform();
	}
	
	public void setFormId(int formId){
		prop.setDefultform(formId);
	}
	
	public int getZhongheScore(){
		return prop.getRolezonghemaxscore();
	}
	
	public Map<Integer, Integer> getSystemConfig(){
		return prop.getSysconfigmap();
	}
	
	public void setScheme(int schemeId){
		prop.setScheme(schemeId);
	}	
	
	public void setPlaytimetimes(int schemeId){
		prop.setPlaytimetimes(schemeId);
	}
	
	public boolean checkLimitEnergy() {
		return CalcEnergyMaxLimit() > prop.getEnergy();
	}
	
	public void addEnergy(int energy) {
		int v = Math.max(prop.getEnergy() + energy, 0);
		int e = Math.min(CalcEnergyMaxLimit(), v);
		prop.setEnergy(e);
	}
	
	public int getEnergy() {
		return prop.getEnergy();
	}
}
