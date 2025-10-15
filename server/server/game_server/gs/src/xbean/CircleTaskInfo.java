
package xbean;

public interface CircleTaskInfo extends mkdb.Bean {
	public CircleTaskInfo copy(); // deep clone
	public CircleTaskInfo toData(); // a Data instance
	public CircleTaskInfo toBean(); // a Bean instance
	public CircleTaskInfo toDataIf(); // a Data instance If need. else return this
	public CircleTaskInfo toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
	public int getQuestid(); // 任务id,代表 职业 公会 捉鬼之类
	public int getQueststate(); // 任务状态
	public int getRound(); // 当前任务的环数
	public int getSumnum(); // 累积次数. 根据不同的任务，拥有不同的含义
	public int getSmallaward(); // 小 奖励 累计次数
	public int getBigaward(); // 大 奖励 累积次数
	public int getQuesttype(); // 任务类型(这里是代表具体循环任务的id)
	public int getDstmapid(); // 目的地图id
	public int getGivetasknpcid(); // 接任务npc的id
	public long getDstnpckey(); // 目的npc的key
	public int getDstnpcid(); // 目的npc的id
	public int getDstitemid(); // 目的道具的id
	public int getDstitemnum(); // 目的道具的数量
	public int getDstx(); // 目的坐标
	public int getDsty(); // 目的坐标
	public long getLastgiveuptime(); // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
	public long getTakequesttime(); // 接任务的时间,单位毫秒
	public long getAcceptquesttime(); // 接任务的时间,单位毫秒 任何情况下都不变
	public int getSametimes(); // 相同任务类型的次数
	public int getLasttype(); // 上次的任务类型
	public int getBattlenum(); // 战斗次数(传说战斗次数)
	public long getFirsttasktime(); // 今天第一次职业的时间,成就系统用.
	public int getDoneparam(); // 任务完成参数
	public String getNpcname(); // npc名称
	public com.locojoy.base.Octets getNpcnameOctets(); // npc名称
	public int getQuestclass(); // 任务类型--类似送信，使用物品之类的任务
	public int getQuestgroup(); // 组类型--类似需要在任务中分种族的任务
	public int getAutocomp(); // 是否自动完成,0:不自动完成,1:是自动完成
	public int getSubmitnpc(); // 非自动完成任务的提交npc
	public int getOpenawardnum(); // 转盘奖励计数
	public int getCatchitsingletime(); // 单倍捉鬼次数
	public int getCatchitdoubletime(); // 双倍捉鬼次数
	public int getSubitemtableid(); // 上交物品的表id

	public void setId(int _v_); // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
	public void setQuestid(int _v_); // 任务id,代表 职业 公会 捉鬼之类
	public void setQueststate(int _v_); // 任务状态
	public void setRound(int _v_); // 当前任务的环数
	public void setSumnum(int _v_); // 累积次数. 根据不同的任务，拥有不同的含义
	public void setSmallaward(int _v_); // 小 奖励 累计次数
	public void setBigaward(int _v_); // 大 奖励 累积次数
	public void setQuesttype(int _v_); // 任务类型(这里是代表具体循环任务的id)
	public void setDstmapid(int _v_); // 目的地图id
	public void setGivetasknpcid(int _v_); // 接任务npc的id
	public void setDstnpckey(long _v_); // 目的npc的key
	public void setDstnpcid(int _v_); // 目的npc的id
	public void setDstitemid(int _v_); // 目的道具的id
	public void setDstitemnum(int _v_); // 目的道具的数量
	public void setDstx(int _v_); // 目的坐标
	public void setDsty(int _v_); // 目的坐标
	public void setLastgiveuptime(long _v_); // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
	public void setTakequesttime(long _v_); // 接任务的时间,单位毫秒
	public void setAcceptquesttime(long _v_); // 接任务的时间,单位毫秒 任何情况下都不变
	public void setSametimes(int _v_); // 相同任务类型的次数
	public void setLasttype(int _v_); // 上次的任务类型
	public void setBattlenum(int _v_); // 战斗次数(传说战斗次数)
	public void setFirsttasktime(long _v_); // 今天第一次职业的时间,成就系统用.
	public void setDoneparam(int _v_); // 任务完成参数
	public void setNpcname(String _v_); // npc名称
	public void setNpcnameOctets(com.locojoy.base.Octets _v_); // npc名称
	public void setQuestclass(int _v_); // 任务类型--类似送信，使用物品之类的任务
	public void setQuestgroup(int _v_); // 组类型--类似需要在任务中分种族的任务
	public void setAutocomp(int _v_); // 是否自动完成,0:不自动完成,1:是自动完成
	public void setSubmitnpc(int _v_); // 非自动完成任务的提交npc
	public void setOpenawardnum(int _v_); // 转盘奖励计数
	public void setCatchitsingletime(int _v_); // 单倍捉鬼次数
	public void setCatchitdoubletime(int _v_); // 双倍捉鬼次数
	public void setSubitemtableid(int _v_); // 上交物品的表id
}
