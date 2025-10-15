
package xbean;

public interface HuoBanZhenrong extends mkdb.Bean {
	public HuoBanZhenrong copy(); // deep clone
	public HuoBanZhenrong toData(); // a Data instance
	public HuoBanZhenrong toBean(); // a Bean instance
	public HuoBanZhenrong toDataIf(); // a Data instance If need. else return this
	public HuoBanZhenrong toBeanIf(); // a Bean instance If need. else return this

	public int getCurrent(); // 当前阵容编号
	public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrong(); // key-阵容编号(1,2,3)
	public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrongAsData(); // key-阵容编号(1,2,3)

	public void setCurrent(int _v_); // 当前阵容编号
}
