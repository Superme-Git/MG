package mylogic;

public class Rolebean {
	private long roleid;
	private int level;
	private int serverid;
	private long createtime;
	private String name;
	
	public Rolebean(long roleid,int level,int serverid,long createtime,String name){
		this.roleid=roleid;
		this.level=level;
		this.serverid=serverid;
		this.createtime=createtime;
		this.name=name;
	}
	
	public long getRoleid() {
		return roleid;
	}
	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getServerid() {
		return serverid;
	}
	public void setServerid(int serverid) {
		this.serverid = serverid;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
