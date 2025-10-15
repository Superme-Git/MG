package fire.pb.activity.timernpc;

public class LevelGroupAward {
	public final int id;
	public final int levelmin;
	public final int levelmax;
	public final int awardid;
	
	public LevelGroupAward(final int id, final int levelmin, final int levelmax, final int awardid) {
		this.id = id;
		this.levelmin = levelmin;
		this.levelmax = levelmax;
		this.awardid = awardid;
	}

}
