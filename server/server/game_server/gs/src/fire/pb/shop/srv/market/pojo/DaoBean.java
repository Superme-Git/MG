package fire.pb.shop.srv.market.pojo;

public class DaoBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1772030350748470607L;

	protected long id;
	protected int firstno;
	protected int twono;
	protected int threeno;
	protected long uniquid;
	protected int key;
	protected int itemid;
	protected long extid;
	protected String name;
	protected long roleid;
	protected long showtime;
	protected long expiretime;
	protected int level; // 等级(目的就是用于离线快照需要过滤级别)
	protected int number;// 数量
	protected int price;// 价格
	protected int attentionnumber; // 关注数量

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFirstno() {
		return firstno;
	}

	public void setFirstno(int firstno) {
		this.firstno = firstno;
	}

	public int getTwono() {
		return twono;
	}

	public void setTwono(int twono) {
		this.twono = twono;
	}

	public int getThreeno() {
		return threeno;
	}

	public void setThreeno(int threeno) {
		this.threeno = threeno;
	}

	public long getUniquid() {
		return uniquid;
	}

	public void setUniquid(long uniquid) {
		this.uniquid = uniquid;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public long getExtid() {
		return extid;
	}

	public void setExtid(long extid) {
		this.extid = extid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public long getShowtime() {
		return showtime;
	}

	public void setShowtime(long showtime) {
		this.showtime = showtime;
	}

	public long getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(long expiretime) {
		this.expiretime = expiretime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getAttentionnumber() {
		return attentionnumber;
	}

	public void setAttentionnumber(int attentionnumber) {
		this.attentionnumber = attentionnumber;
	}

}
