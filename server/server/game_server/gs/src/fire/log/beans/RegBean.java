package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

public class RegBean {
	//操作类型	类型ID
	public static final int REG_ONE=9001;//每日签到	9001
	public static final int REG_SERVEN=9002;//七天签到	9002

	
	private int opid;//签到类型（每日和七日）
	private int num;//累计签到次数
	private List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();//本次获得的奖励
	
	public RegBean(int opid,int num,List<ItemBaseBean> items){
		this.opid=opid;
		this.num=num;
		this.items.addAll(items);
	}

	public int getOpid() {
		return opid;
	}

	public void setOpid(int opid) {
		this.opid = opid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<ItemBaseBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBaseBean> items) {
		this.items = items;
	}
	
	

}
