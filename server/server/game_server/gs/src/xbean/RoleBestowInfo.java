
package xbean;

public interface RoleBestowInfo extends mkdb.Bean {
	public RoleBestowInfo copy(); // deep clone
	public RoleBestowInfo toData(); // a Data instance
	public RoleBestowInfo toBean(); // a Bean instance
	public RoleBestowInfo toDataIf(); // a Data instance If need. else return this
	public RoleBestowInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfo(); // 每个宝箱的开启次数
	public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfoAsData(); // 每个宝箱的开启次数

}
