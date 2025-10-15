
package xbean;

public interface TeamMelon extends mkdb.Bean {
	public TeamMelon copy(); // deep clone
	public TeamMelon toData(); // a Data instance
	public TeamMelon toBean(); // a Bean instance
	public TeamMelon toDataIf(); // a Data instance If need. else return this
	public TeamMelon toBeanIf(); // a Bean instance If need. else return this

	public int getItemid(); // 道具表id by changhao
	public int getItemnum(); // 道具数量 by changha
	public xbean.Item getItemdata(); // 道具数据 by changhao
	public int getAwardid(); // 奖励表id
	public java.util.Map<Long, Integer> getMelonroleids(); // 分赃人员(roll点决定) by changhao
	public java.util.Map<Long, Integer> getMelonroleidsAsData(); // 分赃人员(roll点决定) by changhao
	public java.util.Map<Long, Integer> getOpmelonroleids(); // 已经操作的分赃人员 by changhao
	public java.util.Map<Long, Integer> getOpmelonroleidsAsData(); // 已经操作的分赃人员 by changhao
	public int getMaxrollpoint(); // 最大ROLL点 by changhao
	public int getOpnum(); // 已经ROLL点的人员数量 by changhao
	public long getBattleid(); // 战斗ID by changhao
	public int getBattleresult(); // 战斗结果 by changhao

	public void setItemid(int _v_); // 道具表id by changhao
	public void setItemnum(int _v_); // 道具数量 by changha
	public void setAwardid(int _v_); // 奖励表id
	public void setMaxrollpoint(int _v_); // 最大ROLL点 by changhao
	public void setOpnum(int _v_); // 已经ROLL点的人员数量 by changhao
	public void setBattleid(long _v_); // 战斗ID by changhao
	public void setBattleresult(int _v_); // 战斗结果 by changhao
}
