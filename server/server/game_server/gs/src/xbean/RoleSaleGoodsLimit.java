
package xbean;

public interface RoleSaleGoodsLimit extends mkdb.Bean {
	public RoleSaleGoodsLimit copy(); // deep clone
	public RoleSaleGoodsLimit toData(); // a Data instance
	public RoleSaleGoodsLimit toBean(); // a Bean instance
	public RoleSaleGoodsLimit toDataIf(); // a Data instance If need. else return this
	public RoleSaleGoodsLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimit(); // 日限售
	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimitAsData(); // 日限售
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimit(); // 周限售
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimitAsData(); // 周限售
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimit(); // 月限售
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimitAsData(); // 月限售

}
