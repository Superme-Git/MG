
package xbean;

public interface SingleInvitings extends mkdb.Bean {
	public SingleInvitings copy(); // deep clone
	public SingleInvitings toData(); // a Data instance
	public SingleInvitings toBean(); // a Bean instance
	public SingleInvitings toDataIf(); // a Data instance If need. else return this
	public SingleInvitings toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Long> getInvitingids(); // 
	public java.util.Map<Long, Long> getInvitingidsAsData(); // 

}
