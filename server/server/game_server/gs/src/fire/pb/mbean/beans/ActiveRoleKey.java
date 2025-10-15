package fire.pb.mbean.beans;

/**
 * 
 * 等级分段
 *  ID      区间
 *  1      1----5
 *  2      6----10
 *  3      11---20
 *  .......
 *         141--150
 *         151--155
 *         
 *  充值和消费额度分段
 *  ID      区间（左开右闭）
 *  1       (0--5]
 *  2       (5--10]
 *  3       (10--20]
 *  4       (20--30]
 *  5       (30--40]
 *  6       (40--100]
 *  7       (100--200]
 *  8       (200--300]
 *  9       (300,)
 *  
 *  在线时长分段
 *  ID      区间（左开右闭）
 *  1       (0--15]
 *  2       (15--30]
 *  3       (30--60]
 *  4       (60--120]
 *  5       (120--180]
 *  6       (180--240]
 *  7       (240--360]
 *  8       (360,)
 *  
 *  
 *
 */

public class ActiveRoleKey {
	
	private int stateType; //活跃类型
	
	private int level; // 级别区间段
	
	private int school; // 门派
	
	private int shape; // 主角造型
	
	private int onlinesTime; //在线时长
	
	public ActiveRoleKey(int stateType, int level, int school, int shape, int onlinesTime){
		this.stateType = stateType;
		this.level = level;
		this.school = school;
		this.shape = shape;
		this.onlinesTime = onlinesTime;
	}
	
	public int getLevel() {
		return level;
	}

	public int getStateType() {
		return stateType;
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
		return stateType ^ level ^ school ^ shape ^ onlinesTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		ActiveRoleKey dst = (ActiveRoleKey)obj;
		return (dst.level == this.level) && (dst.onlinesTime == this.onlinesTime) && (dst.school == this.onlinesTime)
					&& (dst.shape == this.shape) && (dst.stateType == this.stateType);
	}
	
	
}
