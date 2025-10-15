package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 道具合成
 *
 */
public class CompareItemBean {
	private List<ItemBean> sourceitems=new ArrayList<ItemBean>();//合成道具
	private int money;//消耗的钱
	private ItemBean resultitem;//合成后的道具
	
	public CompareItemBean() {
	}
	
	public List<ItemBean> getSourceitems() {
		return sourceitems;
	}
	public void setSourceitems(List<ItemBean> sourceitems) {
		this.sourceitems = sourceitems;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public ItemBean getResultitem() {
		return resultitem;
	}
	public void setResultitem(ItemBean resultitem) {
		this.resultitem = resultitem;
	}
	
	

}
