
package fire.pb.activity.award;

import java.util.ArrayList;
import java.util.List;

public class RewardObjs {

	private long value;//经验,金钱的数值
	
	private List<Items> items;

	public RewardObjs(List<Items>items){
		this.items = items;
		this.value = 0;
	}
	public RewardObjs(List<Items>items,long value) {

		this.items = items;
		this.value = value;
	}
	public RewardObjs(long value){
		this.items = null;
		this.value = value;
	}
	
	public RewardObjs() {
       this.items = new ArrayList<Items>();
       this.value = 0;
	}
	public long getValue() {
	
		return value;
	}
	
	public void setValue(long value) {
	
		this.value = value;
	}
	
	public List<Items> getItems() {
	
		return items;
	}
	
	public void setItems(List<Items> items) {
	
		this.items = items;
	}
	public boolean itemNotEmpty(){
		if (items!=null&&items.size()>0)
			return true;
		return false;
	}

}