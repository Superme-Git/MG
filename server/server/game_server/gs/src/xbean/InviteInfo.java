
package xbean;

public interface InviteInfo extends mkdb.Bean {
	public InviteInfo copy(); // deep clone
	public InviteInfo toData(); // a Data instance
	public InviteInfo toBean(); // a Bean instance
	public InviteInfo toDataIf(); // a Data instance If need. else return this
	public InviteInfo toBeanIf(); // a Bean instance If need. else return this

	public boolean getBeinginvited(); // 
	public xbean.TeamInvite getInviting(); // 
	public java.util.List<xbean.TeamInvite> getInvited(); // 
	public java.util.List<xbean.TeamInvite> getInvitedAsData(); // 

	public void setBeinginvited(boolean _v_); // 
}
