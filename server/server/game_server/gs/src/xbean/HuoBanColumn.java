
package xbean;

public interface HuoBanColumn extends mkdb.Bean {
	public HuoBanColumn copy(); // deep clone
	public HuoBanColumn toData(); // a Data instance
	public HuoBanColumn toBean(); // a Bean instance
	public HuoBanColumn toDataIf(); // a Data instance If need. else return this
	public HuoBanColumn toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getFighthuobans(); // 所有参战伙伴
	public java.util.List<Integer> getFighthuobansAsData(); // 所有参战伙伴
	public java.util.Map<Integer, xbean.HuoBanInfo> getHuobans(); // 
	public java.util.Map<Integer, xbean.HuoBanInfo> getHuobansAsData(); // 
	public int getViphuoban(); // vip伙伴数量
	public int getWeek(); // 当前周数,计算伙伴状态的时候使用

	public void setViphuoban(int _v_); // vip伙伴数量
	public void setWeek(int _v_); // 当前周数,计算伙伴状态的时候使用
}
