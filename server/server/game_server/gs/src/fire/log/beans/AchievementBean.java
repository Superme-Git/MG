package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

public class AchievementBean {
	private int id;//完成并且领取奖励的成就id
	private List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();
	
	public List<ItemBaseBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBaseBean> items) {
		this.items = items;
	}

	public AchievementBean(int id,List<ItemBaseBean> items){
		this.id=id;
		this.items.addAll(items);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
