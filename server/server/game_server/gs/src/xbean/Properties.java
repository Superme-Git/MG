
package xbean;

public interface Properties extends mkdb.Bean {
	public Properties copy(); // deep clone
	public Properties toData(); // a Data instance
	public Properties toBean(); // a Bean instance
	public Properties toDataIf(); // a Data instance If need. else return this
	public Properties toBeanIf(); // a Bean instance If need. else return this

	public String getRolename(); // 角色名
	public com.locojoy.base.Octets getRolenameOctets(); // 角色名
	public java.util.List<String> getUsedname(); // 曾用名
	public java.util.List<String> getUsednameAsData(); // 曾用名
	public int getLevel(); // 等级
	public int getShape(); // 主角ID(是指游戏中设定的10个角色id)
	public int getSex(); // 人物性别
	public long getExp(); // 当前经验
	public int getRace(); // 种族
	public int getSchool(); // 职业
	public int getColor(); // 配色方案
	public int getTitle(); // 称谓
	public int getHp(); // 气血
	public int getUplimithp(); // 当前气血上限
	public int getWound(); // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
	public int getMp(); // 法力
	public int getSp(); // 怒气
	public xbean.BasicFightProperties getBfp(); // 存储基础的点数
	public xbean.RoleAddPointProperties getAddpointfp(); // 人物加点方案
	public java.util.Map<Integer, Integer> getPoint(); // 潜能。未分配点数
	public java.util.Map<Integer, Integer> getPointAsData(); // 潜能。未分配点数
	public int getScheme(); // 人物加点方案
	public int getSchemechanges(); // 人物加点方案切换次数
	public long getSchemechangetimes(); // 人物加点方案切换最后时间
	public long getAllexp(); // 玩家出生后获得所有经验
	public long getSceneid(); // 角色所在场景ID
	public int getPosx(); // 坐标x
	public int getPosy(); // 坐标y
	public int getPosz(); // 坐标z
	public long getLaststaticsceneid(); // 最后一次玩家所在的静态场景ID
	public int getLaststaticposx(); // 最后一次玩家所在的静态场景x
	public int getLaststaticposy(); // 最后一次玩家所在的静态场景y
	public int getLaststaticposz(); // 最后一次玩家所在的静态场景z
	public int getFightpetkey(); // 当前战斗宠物
	public int getShowpetkey(); // 当前展示宠物
	public java.util.Map<Integer, Integer> getSysconfigmap(); // 系统设置
	public java.util.Map<Integer, Integer> getSysconfigmapAsData(); // 系统设置
	public java.util.Map<Integer, xbean.TitleInfo> getTitles(); // 拥有的称谓列表
	public java.util.Map<Integer, xbean.TitleInfo> getTitlesAsData(); // 拥有的称谓列表
	public int getDefultform(); // 默认光环
	public java.util.Map<Integer, xbean.FormBean> getFormationsmap(); // key=光环id  value=光环等级
	public java.util.Map<Integer, xbean.FormBean> getFormationsmapAsData(); // key=光环id  value=光环等级
	public long getClankey(); // 角色对应公会的key
	public long getOldclankey(); // 角色对应的上次公会key
	public int getExitstate(); // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
	public long getOnlinetime(); // 角色上次登录时间
	public long getOfflinetime(); // 角色上次离线时间
	public long getCreatetime(); // 角色创建时间
	public long getBindtel(); // 绑定的手机号
	public int getLogintimesofday(); // 当天登录次数
	public int getIsgetbindtelaward(); // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
	public long getBindteltime(); // 绑定手机的时间
	public int getIsbindtelagain(); // 是否需要重新绑定
	public int getBindtelagainnum(); // 重新绑定次数
	public java.util.List<Long> getUnbindteltime(); // 解除绑定的时间点
	public java.util.List<Long> getUnbindteltimeAsData(); // 解除绑定的时间点
	public String getPassword(); // 安全锁密码
	public com.locojoy.base.Octets getPasswordOctets(); // 安全锁密码
	public long getForcedelpdtime(); // 强制删除密码时间
	public long getForcedelendtime(); // 强制删除密码结束时间
	public int getErrortimes(); // 密码剩余输入错误次数
	public long getLockendtime(); // 锁定结束时间点
	public int getIsopensafelock(); // 是否开启道具安全锁
	public long getSumonlinetime(); // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
	public int getUserid(); // 帐号角色id
	public boolean getAddranklist(); // 角色是否参与排行榜
	public long getDeletetime(); // 角色被删除的时间,如果角色没有被删除,这个值为0
	public int getLastloginip(); // 
	public String getPlatformuid(); // userid在平台的唯一id
	public com.locojoy.base.Octets getPlatformuidOctets(); // userid在平台的唯一id
	public int getViplevel(); // vip等级
	public int getEnergy(); // 活力 by changhao
	public int getPlaytimetimes(); // 战斗播放时长倍数 10为1倍速 liuxinhua
	public int getRolezonghemaxscore(); // 人物综合实力历史最高评分 by changhao
	public int getSkillscore(); // 技能评分 by changhao
	public int getPetscore(); // 最强单宠物评分 by changhao
	public int getManypetscore(); // 多宠物评分 by changhao
	public int getLevelscore(); // 等级评分 by changhao
	public int getXiulianscroe(); // 修炼评分 by changhao
	public int getRolescore(); // 人物评分 by changhao
	public int getEquipscore(); // 装备评分 by changhao
	public long getHistorymaxprofessioncontribute(); // 历史最大职业贡献度 by changhao
	public int getRide(); // 当前坐骑 by changhao
	public long getBestpetid(); // 
	public int getRolecolor1(); // 染色方案1
	public int getRolecolor2(); // 染色方案2
	public java.util.List<xbean.RoleColorType> getColorroomlist(); // 衣橱
	public java.util.List<xbean.RoleColorType> getColorroomlistAsData(); // 衣橱
	public int getCruise(); // 巡游状态，默认0没有巡游，否则为巡游id
	public int getCruise2(); // 巡游2 by changhao
	public int getCruise3(); // 巡游3 by changhao
	public java.util.List<xbean.BattleAI> getFighteai(); // 主角ai
	public java.util.List<xbean.BattleAI> getFighteaiAsData(); // 主角ai
	public int getEquipeffect(); // 当前装备特效
	public int getPilot(); // 新手引导项
	public java.util.Map<Integer, Integer> getTupotips(); // 突破任务是否提示过
	public java.util.Map<Integer, Integer> getTupotipsAsData(); // 突破任务是否提示过
	public java.util.Map<Integer, Integer> getTupofinish(); // 突破任务是否完成
	public java.util.Map<Integer, Integer> getTupofinishAsData(); // 突破任务是否完成
	public int getYingfuxishu(); // 盈福转换系数
	public java.util.Map<Integer, String> getDepotnameinfo(); // key=仓库id  value=名称
	public java.util.Map<Integer, String> getDepotnameinfoAsData(); // key=仓库id  value=名称
	public long getExpiretime(); // 点卡结束时间(点卡服专用) by changhao
	public int getFirstprompt(); // 还没提示过就是0(点卡服专用) by changhao
	public long getLastchargetime(); // 最后一次充值时间
	public java.util.Map<Integer, Integer> getLineconfigmap(); // 精英副本自动进入设置
	public java.util.Map<Integer, Integer> getLineconfigmapAsData(); // 精英副本自动进入设置

	public void setRolename(String _v_); // 角色名
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 角色名
	public void setLevel(int _v_); // 等级
	public void setShape(int _v_); // 主角ID(是指游戏中设定的10个角色id)
	public void setSex(int _v_); // 人物性别
	public void setExp(long _v_); // 当前经验
	public void setRace(int _v_); // 种族
	public void setSchool(int _v_); // 职业
	public void setColor(int _v_); // 配色方案
	public void setTitle(int _v_); // 称谓
	public void setHp(int _v_); // 气血
	public void setUplimithp(int _v_); // 当前气血上限
	public void setWound(int _v_); // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
	public void setMp(int _v_); // 法力
	public void setSp(int _v_); // 怒气
	public void setScheme(int _v_); // 人物加点方案
	public void setSchemechanges(int _v_); // 人物加点方案切换次数
	public void setSchemechangetimes(long _v_); // 人物加点方案切换最后时间
	public void setAllexp(long _v_); // 玩家出生后获得所有经验
	public void setSceneid(long _v_); // 角色所在场景ID
	public void setPosx(int _v_); // 坐标x
	public void setPosy(int _v_); // 坐标y
	public void setPosz(int _v_); // 坐标z
	public void setLaststaticsceneid(long _v_); // 最后一次玩家所在的静态场景ID
	public void setLaststaticposx(int _v_); // 最后一次玩家所在的静态场景x
	public void setLaststaticposy(int _v_); // 最后一次玩家所在的静态场景y
	public void setLaststaticposz(int _v_); // 最后一次玩家所在的静态场景z
	public void setFightpetkey(int _v_); // 当前战斗宠物
	public void setShowpetkey(int _v_); // 当前展示宠物
	public void setDefultform(int _v_); // 默认光环
	public void setClankey(long _v_); // 角色对应公会的key
	public void setOldclankey(long _v_); // 角色对应的上次公会key
	public void setExitstate(int _v_); // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
	public void setOnlinetime(long _v_); // 角色上次登录时间
	public void setOfflinetime(long _v_); // 角色上次离线时间
	public void setCreatetime(long _v_); // 角色创建时间
	public void setBindtel(long _v_); // 绑定的手机号
	public void setLogintimesofday(int _v_); // 当天登录次数
	public void setIsgetbindtelaward(int _v_); // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
	public void setBindteltime(long _v_); // 绑定手机的时间
	public void setIsbindtelagain(int _v_); // 是否需要重新绑定
	public void setBindtelagainnum(int _v_); // 重新绑定次数
	public void setPassword(String _v_); // 安全锁密码
	public void setPasswordOctets(com.locojoy.base.Octets _v_); // 安全锁密码
	public void setForcedelpdtime(long _v_); // 强制删除密码时间
	public void setForcedelendtime(long _v_); // 强制删除密码结束时间
	public void setErrortimes(int _v_); // 密码剩余输入错误次数
	public void setLockendtime(long _v_); // 锁定结束时间点
	public void setIsopensafelock(int _v_); // 是否开启道具安全锁
	public void setSumonlinetime(long _v_); // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
	public void setUserid(int _v_); // 帐号角色id
	public void setAddranklist(boolean _v_); // 角色是否参与排行榜
	public void setDeletetime(long _v_); // 角色被删除的时间,如果角色没有被删除,这个值为0
	public void setLastloginip(int _v_); // 
	public void setPlatformuid(String _v_); // userid在平台的唯一id
	public void setPlatformuidOctets(com.locojoy.base.Octets _v_); // userid在平台的唯一id
	public void setViplevel(int _v_); // vip等级
	public void setEnergy(int _v_); // 活力 by changhao
	public void setPlaytimetimes(int _v_); // 战斗播放时长倍数 10为1倍速 liuxinhua
	public void setRolezonghemaxscore(int _v_); // 人物综合实力历史最高评分 by changhao
	public void setSkillscore(int _v_); // 技能评分 by changhao
	public void setPetscore(int _v_); // 最强单宠物评分 by changhao
	public void setManypetscore(int _v_); // 多宠物评分 by changhao
	public void setLevelscore(int _v_); // 等级评分 by changhao
	public void setXiulianscroe(int _v_); // 修炼评分 by changhao
	public void setRolescore(int _v_); // 人物评分 by changhao
	public void setEquipscore(int _v_); // 装备评分 by changhao
	public void setHistorymaxprofessioncontribute(long _v_); // 历史最大职业贡献度 by changhao
	public void setRide(int _v_); // 当前坐骑 by changhao
	public void setBestpetid(long _v_); // 
	public void setRolecolor1(int _v_); // 染色方案1
	public void setRolecolor2(int _v_); // 染色方案2
	public void setCruise(int _v_); // 巡游状态，默认0没有巡游，否则为巡游id
	public void setCruise2(int _v_); // 巡游2 by changhao
	public void setCruise3(int _v_); // 巡游3 by changhao
	public void setEquipeffect(int _v_); // 当前装备特效
	public void setPilot(int _v_); // 新手引导项
	public void setYingfuxishu(int _v_); // 盈福转换系数
	public void setExpiretime(long _v_); // 点卡结束时间(点卡服专用) by changhao
	public void setFirstprompt(int _v_); // 还没提示过就是0(点卡服专用) by changhao
	public void setLastchargetime(long _v_); // 最后一次充值时间
}
