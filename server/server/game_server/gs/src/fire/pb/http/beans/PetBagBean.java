package fire.pb.http.beans;

import java.util.ArrayList;
import java.util.List;

public class PetBagBean {
	private int type;
	List<PetInfoBean> pets=new ArrayList<PetInfoBean>();
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<PetInfoBean> getPets() {
		return pets;
	}
	public void setPets(List<PetInfoBean> pets) {
		this.pets = pets;
	}
	
}
