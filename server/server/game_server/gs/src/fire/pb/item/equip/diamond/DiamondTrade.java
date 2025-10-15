

package fire.pb.item.equip.diamond;


public class DiamondTrade {
    public static final int DEFAULT_NUM=15;
	private int id;
	
	private int index; // 指数

	private int price; // 价格
	
	private int remainNum=DEFAULT_NUM;//默认15个

	public DiamondTrade(int id,int index, int price) {

		super();
		this.id = id;
		this.index = index;
		this.price = price;
	}

	public int getIndex() {

		return index;
	}

	public void setIndex(int index) {

		this.index = index;
	}

	public int getPrice() {

		return price;
	}

	public void setPrice(int price) {

		this.price = price;
	}

	
	public int getRemainNum() {
	
		return remainNum;
	}

	
	public void setRemainNum(int remainNum) {
	
		this.remainNum = remainNum;
	}

	
	public int getId() {
	
		return id;
	}
}
