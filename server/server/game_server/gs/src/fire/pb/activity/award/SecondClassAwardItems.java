
package fire.pb.activity.award;

import java.util.List;


public class SecondClassAwardItems {
	
	public static final int UNRELATED_RANDOM=1;//不关联随机
	
	public static final int RELATED_RANDOM=0;//关联随机
	//随机奖励物品的总概率
	private int base;
	
	//随机奖励物品的随机方式
	private int randomType;
	
	//是否给予物品奖励的总开关,可能是公式
	private String totalProb;
	
	private List<SecondClassAwardItem> items;
	
	public int getBase() {
	
		return base;
	}
	
	public void setBase(int base) {
	
		this.base = base;
	}
	
	public int getRandomType() {
	
		return randomType;
	}
	
	public void setRandomType(int randomType) {
	
		this.randomType = randomType;
	}
	
	public List<SecondClassAwardItem> getItems() {
	
		return items;
	}
	
	public void setItems(List<SecondClassAwardItem> items) {
	
		this.items = items;
	}

	public String getTotalProb() {
	
		return totalProb;
	}
	
	public void setTotalProb(String totalProb) {
	
		this.totalProb = totalProb;
	}
}

