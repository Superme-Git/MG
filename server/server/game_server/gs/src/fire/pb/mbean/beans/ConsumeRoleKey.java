package fire.pb.mbean.beans;

public class ConsumeRoleKey {

	private int consumeType;
	
	private int level;
	
	private int consumeValue;
	
	private int school;
	
	private int shape;
	
	private int onlinesTime;
	
	public ConsumeRoleKey(int consumeType, int level, int consumeValue,
			int school, int shape, int onlinesTime) {
		this.consumeType = consumeType;
		this.level = level;
		this.consumeValue = consumeValue;
		this.school = school;
		this.shape = shape;
		this.onlinesTime = onlinesTime;
	}

	public int getConsumeType() {
		return consumeType;
	}

	public int getLevel() {
		return level;
	}

	public int getConsumeValue() {
		return consumeValue;
	}

	public int getSchool() {
		return school;
	}

	public int getShape() {
		return shape;
	}

	public int getOnlinesTime() {
		return onlinesTime;
	}
	
	@Override
	public int hashCode(){
		return consumeType ^ level ^ consumeValue ^ school ^ shape ^ onlinesTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		ConsumeRoleKey other = (ConsumeRoleKey)obj;
		return (other.consumeType == this.consumeType) && (other.consumeValue == this.consumeValue)
					&&(other.level == this.level) && (other.onlinesTime == this.onlinesTime) && (other.school == this.school)
								&&(other.shape == this.shape);
	}
	
}
