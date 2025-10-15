package fire.log.beans;


public class ClanBuildUPBean {
	private long clankey;//公会ID
	private int opid;//升级类别（公会、金库、药房、旅馆）
	private int prelevel;//升级前等级
	private int level;//升级后等级
	private int cost;//消耗公会资源
	
	public ClanBuildUPBean(long clankey,int opid,int prelevel,int level,int cost){
		this.clankey=clankey;
		this.opid=opid;
		this.prelevel=prelevel;
		this.level=level;
		this.cost=cost;
	}

	public int getPrelevel() {
		return prelevel;
	}

	public void setPrelevel(int prelevel) {
		this.prelevel = prelevel;
	}
	
	public long getFactionkey() {
		return clankey;
	}

	public void setFactionkey(long factionkey) {
		this.clankey = factionkey;
	}

	public int getOpid() {
		return opid;
	}

	public void setOpid(int opid) {
		this.opid = opid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	


}
