package cn.com.zc.db.bean;

public class RoseRecordBean{
	public long getRoleid() {
		return roleid;
	}
	public String getName() {
		return name;
	}
	public int getRosenum() {
		return rosenum;
	}
	public RoseRecordBean(long roleid, String name, int rosenum) {
		super();
		this.roleid = roleid;
		this.name = name;
		this.rosenum = rosenum;
	}
	private long roleid; // 角色id
	private String name; // 角色名
	private int rosenum; // 玫瑰数量
}
