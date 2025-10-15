
package xbean;

public interface ETeamMelon extends mkdb.Bean {
	public ETeamMelon copy(); // deep clone
	public ETeamMelon toData(); // a Data instance
	public ETeamMelon toBean(); // a Bean instance
	public ETeamMelon toDataIf(); // a Data instance If need. else return this
	public ETeamMelon toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.TeamMelon> getMelonid2melons(); // 队伍分赃链表 by changhao
	public java.util.Map<Long, xbean.TeamMelon> getMelonid2melonsAsData(); // 队伍分赃链表 by changhao
	public int getMelontype(); // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
	public int getDataid(); // 结合melontype,表示子类型;精英副本里表示副本id
	public long getDataid2(); // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
	public java.util.List<Long> getMelonerlist(); // 有资格分赃的原始人员 by changhao
	public java.util.List<Long> getMelonerlistAsData(); // 有资格分赃的原始人员 by changhao
	public java.util.List<Long> getWatchmelonerlist(); // 没资格观看人员 by changhao
	public java.util.List<Long> getWatchmelonerlistAsData(); // 没资格观看人员 by changhao

	public void setMelontype(int _v_); // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
	public void setDataid(int _v_); // 结合melontype,表示子类型;精英副本里表示副本id
	public void setDataid2(long _v_); // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
}
