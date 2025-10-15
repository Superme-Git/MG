
package xbean;

public interface YbNums extends mkdb.Bean {
	public YbNums copy(); // deep clone
	public YbNums toData(); // a Data instance
	public YbNums toBean(); // a Bean instance
	public YbNums toDataIf(); // a Data instance If need. else return this
	public YbNums toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.YbNum> getRoleyb(); // 角色id -> 符石 by changhao
	public java.util.Map<Long, xbean.YbNum> getRoleybAsData(); // 角色id -> 符石 by changhao

}
