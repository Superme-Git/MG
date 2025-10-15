package fire.pb.util;

public class GeneralFormat {
	private int id;
	private int rate;
	private int subid;
	
	public GeneralFormat() {
	}

	
	public GeneralFormat(int id, int rate, int subid) {
		this.id = id;
		this.rate = rate;
		this.subid = subid;
	}
	

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
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}



}
