package cn.com.zc.db.bean;

public class PetInfoBean {

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public int getPetScore() {
		return petScore;
	}

	public void setPetScore(int petScore) {
		this.petScore = petScore;
	}

	public long getUniquePetId() {
		return unitid;
	}

	public void setUniquePetId(long unitid) {
		this.unitid = unitid;
	}
	
	public int getPetId() {
		return petId;
	}
	
	public void setPetId(int petId) {
		this.petId = petId;
	}
	
	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	long roleid;
	
	int petId;

	int petScore;
	
	long unitid;
	
	int colour;

}
