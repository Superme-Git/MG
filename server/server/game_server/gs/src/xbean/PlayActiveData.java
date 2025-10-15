
package xbean;

public interface PlayActiveData extends mkdb.Bean {
	public PlayActiveData copy(); // deep clone
	public PlayActiveData toData(); // a Data instance
	public PlayActiveData toBean(); // a Bean instance
	public PlayActiveData toDataIf(); // a Data instance If need. else return this
	public PlayActiveData toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 
	public int getCount(); // 完成次数
	public int getCount2(); // 完成次数2
	public float getActiveness(); // 完成共获得的活跃度

	public void setId(int _v_); // 
	public void setCount(int _v_); // 完成次数
	public void setCount2(int _v_); // 完成次数2
	public void setActiveness(float _v_); // 完成共获得的活跃度
}
