package fire.log.beans;

public class SNSOpBean {
	private	int	FriCnt;			//好友个数
	private	int	MasUsrId;		//师傅UserID（没有记为空）
	private	int	PreUsrId;		//徒弟UserID（没有记为空）
	private	int	CouplUsrId;		//妻子/丈夫UserID（没有记为空）
	private	int	MasVal;			//良师值
	private	int	IntiVal;		//夫妻亲密值
	private	int	MaPrTask;		//师徒任务完成项
	private	int	CoupSkLvl;		//各夫妻技能等级
	
	/**
	 * 因为其他参数暂时都没有,写这个构造函数
	 * @param friCnt
	 */
	public SNSOpBean(int friCnt) {
		FriCnt = friCnt;
		MasUsrId = -1;		//师傅UserID（没有记为空）
		PreUsrId = -1;		//徒弟UserID（没有记为空）
		CouplUsrId = -1;	//妻子/丈夫UserID（没有记为空）
		MasVal = -1;		//良师值
		IntiVal = -1;		//夫妻亲密值
		MaPrTask = -1;		//师徒任务完成项
		CoupSkLvl = -1;		//各夫妻技能等级
	}

	public SNSOpBean(int friCnt, int masUsrId, int preUsrId, int couplUsrId, int masVal, int intiVal, int maPrTask, int coupSkLvl) {
		FriCnt = friCnt;
		MasUsrId = masUsrId;
		PreUsrId = preUsrId;
		CouplUsrId = couplUsrId;
		MasVal = masVal;
		IntiVal = intiVal;
		MaPrTask = maPrTask;
		CoupSkLvl = coupSkLvl;
	}

	public int getFriCnt() {
		return FriCnt;
	}

	public int getMasUsrId() {
		return MasUsrId;
	}

	public int getPreUsrId() {
		return PreUsrId;
	}

	public int getCouplUsrId() {
		return CouplUsrId;
	}

	public int getMasVal() {
		return MasVal;
	}

	public int getIntiVal() {
		return IntiVal;
	}

	public int getMaPrTask() {
		return MaPrTask;
	}

	public int getCoupSkLvl() {
		return CoupSkLvl;
	}
}
