
package xbean;

public interface Vipinfo extends mkdb.Bean {
	public Vipinfo copy(); // deep clone
	public Vipinfo toData(); // a Data instance
	public Vipinfo toBean(); // a Bean instance
	public Vipinfo toDataIf(); // a Data instance If need. else return this
	public Vipinfo toBeanIf(); // a Bean instance If need. else return this

	public int getVipexp(); // 已充符石数量
	public int getViplevel(); // vip等级
	public int getBonus(); // 可领奖励
	public int getGotbonus(); // 已领奖励

	public void setVipexp(int _v_); // 已充符石数量
	public void setViplevel(int _v_); // vip等级
	public void setBonus(int _v_); // 可领奖励
	public void setGotbonus(int _v_); // 已领奖励
}
