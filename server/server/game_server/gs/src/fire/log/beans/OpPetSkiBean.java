package fire.log.beans;


public class OpPetSkiBean {
	public static int Op_PetSkiBean_Operate_CerMagi = 1;			// 认证
	public static int Op_PetSkiBean_Operate_CancelCerMagi = 2;		// 取消认证
	public static int Op_PetSkiBean_Operate_Add = 3;				// 添加/学习技能
	
	private int PetId;		// 放生宝宝id
	private int IsTrea;     // 是否珍品0否，1是
	private long ItKey;		// 宠物唯一Id
	private int SkiId;      // 学习宝宝技能ID
	private int OpId;    // 操作类型
	public OpPetSkiBean(int petId, int isTrea, long itKey, int skiId, int operate) {
		PetId = petId;
		IsTrea = isTrea;
		ItKey = itKey;
		SkiId = skiId;
		OpId = operate;
	}
	
	public long getItKey() {
		return ItKey;
	}
	public int getPetId() {
		return PetId;
	}
	public int getIsTrea() {
		return IsTrea;
	}
	public int getSkiId() {
		return SkiId;
	}
	public int getOpId() {
		return OpId;
	}
	
	
}
