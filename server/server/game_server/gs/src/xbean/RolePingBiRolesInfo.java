
package xbean;

public interface RolePingBiRolesInfo extends mkdb.Bean {
	public RolePingBiRolesInfo copy(); // deep clone
	public RolePingBiRolesInfo toData(); // a Data instance
	public RolePingBiRolesInfo toBean(); // a Bean instance
	public RolePingBiRolesInfo toDataIf(); // a Data instance If need. else return this
	public RolePingBiRolesInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Long> getPingbiroles(); // //存储黑名单角色id
	public java.util.Map<Long, Long> getPingbirolesAsData(); // //存储黑名单角色id

}
