
package xbean;

public interface RoleEventNpcInfoColumn extends mkdb.Bean {
	public RoleEventNpcInfoColumn copy(); // deep clone
	public RoleEventNpcInfoColumn toData(); // a Data instance
	public RoleEventNpcInfoColumn toBean(); // a Bean instance
	public RoleEventNpcInfoColumn toDataIf(); // a Data instance If need. else return this
	public RoleEventNpcInfoColumn toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfo(); // //key是活动id
	public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfoAsData(); // //key是活动id

}
