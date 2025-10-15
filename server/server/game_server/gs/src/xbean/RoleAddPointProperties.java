
package xbean;

public interface RoleAddPointProperties extends mkdb.Bean {
	public RoleAddPointProperties copy(); // deep clone
	public RoleAddPointProperties toData(); // a Data instance
	public RoleAddPointProperties toBean(); // a Bean instance
	public RoleAddPointProperties toDataIf(); // a Data instance If need. else return this
	public RoleAddPointProperties toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getCons_save(); // 已分配体质
	public java.util.Map<Integer, Integer> getCons_saveAsData(); // 已分配体质
	public java.util.Map<Integer, Integer> getIq_save(); // 已分配智力
	public java.util.Map<Integer, Integer> getIq_saveAsData(); // 已分配智力
	public java.util.Map<Integer, Integer> getStr_save(); // 已分配力量
	public java.util.Map<Integer, Integer> getStr_saveAsData(); // 已分配力量
	public java.util.Map<Integer, Integer> getEndu_save(); // 已分配耐力
	public java.util.Map<Integer, Integer> getEndu_saveAsData(); // 已分配耐力
	public java.util.Map<Integer, Integer> getAgi_save(); // 已分配敏捷
	public java.util.Map<Integer, Integer> getAgi_saveAsData(); // 已分配敏捷

}
