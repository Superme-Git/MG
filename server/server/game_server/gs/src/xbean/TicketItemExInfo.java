
package xbean;

public interface TicketItemExInfo extends mkdb.Bean {
	public TicketItemExInfo copy(); // deep clone
	public TicketItemExInfo toData(); // a Data instance
	public TicketItemExInfo toBean(); // a Bean instance
	public TicketItemExInfo toDataIf(); // a Data instance If need. else return this
	public TicketItemExInfo toBeanIf(); // a Bean instance If need. else return this

	public String getTicketcode(); // 
	public com.locojoy.base.Octets getTicketcodeOctets(); // 

	public void setTicketcode(String _v_); // 
	public void setTicketcodeOctets(com.locojoy.base.Octets _v_); // 
}
