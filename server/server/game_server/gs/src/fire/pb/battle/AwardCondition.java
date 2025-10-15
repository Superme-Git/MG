package fire.pb.battle;

public class AwardCondition {

	private int locationID;

	private double factor;

	private int awardID;
	
	private int locationType;

	public AwardCondition(int locationID, double factor, int awardID,int locationType) {

		super();
		this.locationID = locationID;
		this.factor = factor;
		this.awardID = awardID;
		this.locationType = locationType;
	}

	public int getLocationID() {

		return locationID;
	}

	public void setLocationID(int locationID) {

		this.locationID = locationID;
	}

	public double getFactor() {

		return factor;
	}

	public void setFactor(double factor) {

		this.factor = factor;
	}

	public int getAwardID() {

		return awardID;
	}

	public void setAwardID(int awardID) {

		this.awardID = awardID;
	}

	
	public int getLocationType() {
	
		return locationType;
	}

	
	public void setLocationType(int locationType) {
	
		this.locationType = locationType;
	}
}
