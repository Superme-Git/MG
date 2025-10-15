package fire.pb;

public class RoleBaseInfo {
	public final long roleid;
	public final int rolelevel;
	public int SwXs;
	
	public RoleBaseInfo(final long roleid, final int rolelevel) {
		this.roleid = roleid;
		this.rolelevel = rolelevel;
		this.SwXs = 0;
	}
	
	public void setSwXs(final int SwXs) {
		this.SwXs = SwXs;
	}
}
