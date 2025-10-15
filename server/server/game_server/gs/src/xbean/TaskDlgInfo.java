
package xbean;

public interface TaskDlgInfo extends mkdb.Bean {
	public TaskDlgInfo copy(); // deep clone
	public TaskDlgInfo toData(); // a Data instance
	public TaskDlgInfo toBean(); // a Bean instance
	public TaskDlgInfo toDataIf(); // a Data instance If need. else return this
	public TaskDlgInfo toBeanIf(); // a Bean instance If need. else return this

	public int getTaskid(); // 任务id
	public int getTaskstate(); // 任务状态
	public int getRound(); // 当前任务的环数
	public int getSumnum(); // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
	public int getDstmapid(); // 目的地图id
	public long getDstnpckey(); // 目的npc的key
	public long getEndtime(); // 任务的结束时间
	public int getDstnpcid(); // 目的npc的id
	public int getDstitemid(); // 目的道具的id
	public int getDstitemnum(); // 目的道具的数量
	public int getDstx(); // 目的坐标
	public int getDsty(); // 目的坐标

	public void setTaskid(int _v_); // 任务id
	public void setTaskstate(int _v_); // 任务状态
	public void setRound(int _v_); // 当前任务的环数
	public void setSumnum(int _v_); // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
	public void setDstmapid(int _v_); // 目的地图id
	public void setDstnpckey(long _v_); // 目的npc的key
	public void setEndtime(long _v_); // 任务的结束时间
	public void setDstnpcid(int _v_); // 目的npc的id
	public void setDstitemid(int _v_); // 目的道具的id
	public void setDstitemnum(int _v_); // 目的道具的数量
	public void setDstx(int _v_); // 目的坐标
	public void setDsty(int _v_); // 目的坐标
}
