package fire.pb.fanpai;


public class CardItem {

	private int type;
	private int number;
	private int times;
	private int probability;
	private int realindex;
	
	private int itemId;
	private int bind;
	private int msgId;
	private String obs;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
	public int getProbability() {
		return probability;
	}
	
	public void setProbability(int probability) {
		this.probability = probability;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public int getBind() {
		return bind;
	}
	
	public void setBind(int bind) {
		this.bind = bind;
	}
	
	public int getMsgId() {
		return msgId;
	}
	
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getRealIndex() {
		return realindex;
	}

	public void setRealIndex(int realIndex) {
		this.realindex = realIndex;
	}
	
	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public boolean equals(Object arg0) {
		CardItem item = (CardItem)arg0;
		return obs.equals(item.getObs());
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
