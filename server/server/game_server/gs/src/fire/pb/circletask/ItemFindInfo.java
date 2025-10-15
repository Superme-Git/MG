package fire.pb.circletask;

public class ItemFindInfo {
	private int id;
	private int rate;
	private int num;
	private int levelmin;
	private int levelmax;
	private int shopnpc;
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getRate() {
		return rate;
	}


	public void setRate(int rate) {
		this.rate = rate;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public int getLevelmin() {
		return levelmin;
	}


	public void setLevelmin(int levelmin) {
		this.levelmin = levelmin;
	}


	public int getLevelmax() {
		return levelmax;
	}


	public void setLevelmax(int levelmax) {
		this.levelmax = levelmax;
	}

	public int getShopnpc() {
		return shopnpc;
	}


	public void setShopnpc(int shopnpc) {
		this.shopnpc = shopnpc;
	}


	public ItemFindInfo() {
	}

	
	public ItemFindInfo(int id, int rate, int num, int levelmin, int levelmax, int shopnpc) {
		this.id = id;
		this.rate = rate;
		this.num = num;
		this.levelmin = levelmin;
		this.levelmax = levelmax;
		this.shopnpc = shopnpc;
	}
}
