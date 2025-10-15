
package xbean;

public interface FirstCharge extends mkdb.Bean {
	public FirstCharge copy(); // deep clone
	public FirstCharge toData(); // a Data instance
	public FirstCharge toBean(); // a Bean instance
	public FirstCharge toDataIf(); // a Data instance If need. else return this
	public FirstCharge toBeanIf(); // a Bean instance If need. else return this

	public long getFirstchargetime(); // 首冲的时间,可以被刷新的
	public long getFirstchargeclearpresenttime(); // 
	public long getChargestatus(); // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了

	public void setFirstchargetime(long _v_); // 首冲的时间,可以被刷新的
	public void setFirstchargeclearpresenttime(long _v_); // 
	public void setChargestatus(long _v_); // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
}
