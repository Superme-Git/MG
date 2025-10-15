package fire.pb.fushi.redpack;

public class RedPackInfoBean {
	private java.lang.String redpackid; // 红包Id
	private long roleid; // 角色Id
	private java.lang.String rolename; // 角色名
	private java.lang.String redpackdes; // 红包寄语
	private int redpackstate; // 红包状态
	private int fushi; // 符石数
	private long valuekey;//公会或者队伍id
	private long sendtime;
	
	public RedPackInfoBean(java.lang.String _redpackid_, long _roleid_, java.lang.String _rolename_, java.lang.String _redpackdes_, int _redpackstate_, int _fushi_ ,long valuekey,long time) {
		this.redpackid = _redpackid_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.redpackdes = _redpackdes_;
		this.redpackstate = _redpackstate_;
		this.fushi = _fushi_;
		this.valuekey = valuekey;
		this.sendtime = time;
	}
	
	public java.lang.String getRedpackid() {
		return redpackid;
	}
	public void setRedpackid(java.lang.String redpackid) {
		this.redpackid = redpackid;
	}
	public long getRoleid() {
		return roleid;
	}
	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}
	public java.lang.String getRolename() {
		return rolename;
	}
	public void setRolename(java.lang.String rolename) {
		this.rolename = rolename;
	}
	public java.lang.String getRedpackdes() {
		return redpackdes;
	}
	public void setRedpackdes(java.lang.String redpackdes) {
		this.redpackdes = redpackdes;
	}
	public int getRedpackstate() {
		return redpackstate;
	}
	public void setRedpackstate(int redpackstate) {
		this.redpackstate = redpackstate;
	}
	public int getFushi() {
		return fushi;
	}
	public void setFushi(int fushi) {
		this.fushi = fushi;
	}
	public long getValuekey() {
		return valuekey;
	}
	public void setValuekey(long valuekey) {
		this.valuekey = valuekey;
	}
	public long getSendtime() {
		return sendtime;
	}
	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}
	
	

}
