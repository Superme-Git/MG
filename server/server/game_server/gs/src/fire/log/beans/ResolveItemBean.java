package fire.log.beans;

import java.util.ArrayList;
import java.util.List;
/**
 * 道具分解
 *
 */
public class ResolveItemBean {
	private ItemBean sourceitem;//被分解的道具
	private List<ItemBean> resultitems=new ArrayList<ItemBean>();//分解后得到的道具
	private int money;//分解后得到的钱
	
	public ResolveItemBean() {
	}
	
	public ResolveItemBean(ItemBean sourceitem, List<ItemBean> resultitems,
			int money) {
		this.sourceitem = sourceitem;
		this.resultitems = resultitems;
		this.money = money;
	}
	public ItemBean getSourceitem() {
		return sourceitem;
	}
	public void setSourceitem(ItemBean sourceitem) {
		this.sourceitem = sourceitem;
	}
	public List<ItemBean> getResultitems() {
		return resultitems;
	}
	public void setResultitems(List<ItemBean> resultitems) {
		this.resultitems = resultitems;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	

}
