
package xbean;

public interface YybFushiNum extends mkdb.Bean {
	public YybFushiNum copy(); // deep clone
	public YybFushiNum toData(); // a Data instance
	public YybFushiNum toBean(); // a Bean instance
	public YybFushiNum toDataIf(); // a Data instance If need. else return this
	public YybFushiNum toBeanIf(); // a Bean instance If need. else return this

	public int getBalance(); // 现金充值符石总数
	public int getGenbalance(); // 系统赠送符石数
	public int getSaveamt(); // 累计充值金额
	public long getFushiall(); // 累计符石总数，包括所有产出途径的符石数

	public void setBalance(int _v_); // 现金充值符石总数
	public void setGenbalance(int _v_); // 系统赠送符石数
	public void setSaveamt(int _v_); // 累计充值金额
	public void setFushiall(long _v_); // 累计符石总数，包括所有产出途径的符石数
}
