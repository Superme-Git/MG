
package xbean;

public interface SRRedPackNum extends mkdb.Bean {
	public SRRedPackNum copy(); // deep clone
	public SRRedPackNum toData(); // a Data instance
	public SRRedPackNum toBean(); // a Bean instance
	public SRRedPackNum toDataIf(); // a Data instance If need. else return this
	public SRRedPackNum toBeanIf(); // a Bean instance If need. else return this

	public int getModeltype(); // 红包类型(不要用)
	public int getRedpacksendnum(); // 发红包数量
	public int getRedpackreceivenum(); // 收红包数量
	public int getRedpacksendfushinum(); // 发红包符石总数

	public void setModeltype(int _v_); // 红包类型(不要用)
	public void setRedpacksendnum(int _v_); // 发红包数量
	public void setRedpackreceivenum(int _v_); // 收红包数量
	public void setRedpacksendfushinum(int _v_); // 发红包符石总数
}
