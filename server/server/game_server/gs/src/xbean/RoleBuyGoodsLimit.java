
package xbean;

public interface RoleBuyGoodsLimit extends mkdb.Bean {
	public RoleBuyGoodsLimit copy(); // deep clone
	public RoleBuyGoodsLimit toData(); // a Data instance
	public RoleBuyGoodsLimit toBean(); // a Bean instance
	public RoleBuyGoodsLimit toDataIf(); // a Data instance If need. else return this
	public RoleBuyGoodsLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimit(); // key=goodsid日限购
	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimitAsData(); // key=goodsid日限购
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimit(); // 周限购
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimitAsData(); // 周限购
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimit(); // 月限购
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimitAsData(); // 月限购

}
