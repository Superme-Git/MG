
package xbean;

public interface ProfessionLeaderTicketInfo extends mkdb.Bean {
	public ProfessionLeaderTicketInfo copy(); // deep clone
	public ProfessionLeaderTicketInfo toData(); // a Data instance
	public ProfessionLeaderTicketInfo toBean(); // a Bean instance
	public ProfessionLeaderTicketInfo toDataIf(); // a Data instance If need. else return this
	public ProfessionLeaderTicketInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid
	public int getTickets(); // role的得票
	public String getWords(); // role的宣言
	public com.locojoy.base.Octets getWordsOctets(); // role的宣言

	public void setRoleid(long _v_); // roleid
	public void setTickets(int _v_); // role的得票
	public void setWords(String _v_); // role的宣言
	public void setWordsOctets(com.locojoy.base.Octets _v_); // role的宣言
}
