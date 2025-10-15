package fire.pb.item.make;

public class BoDongDuan {

	public int bodongduan;
	public int bodongduanbase; //波动段权重
	public int min;
	public int max;
	
	public BoDongDuan() {
		this.bodongduan = 0;
		this.bodongduanbase = 0;
		this.min = 0;
		this.max = 0;
	}
	
	public BoDongDuan(int bodongduan, int bodongduanbase, int min, int max) {
		this.bodongduan = bodongduan;
		this.bodongduanbase = bodongduanbase;
		this.min = min;
		this.max = max;
	}
}
