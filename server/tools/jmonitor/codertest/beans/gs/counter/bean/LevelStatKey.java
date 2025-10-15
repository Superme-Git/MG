package gs.counter.bean;

public class LevelStatKey {

	public int level;
	public int sex;
	public int menpai;

	public LevelStatKey() {
		level = 0;
		sex = 0;
		menpai = 0;
	}

	public LevelStatKey( LevelStatKey _src_) {
		level = _src_.level;
		sex = _src_.sex;
		menpai = _src_.menpai;
	}

	public LevelStatKey( int level, int sex, int menpai) {
		this.level = level;
		this.sex = sex;
		this.menpai = menpai;
	}

	public int getLevel() {
		return level;
	}

	public int getSex() {
		return sex;
	}

	public int getMenpai() {
		return menpai;
	}

	@Override
	public int hashCode() {
		return level ^ sex ^ menpai;
	}

	@Override
	public boolean equals(Object obj) {
		LevelStatKey dst = (LevelStatKey)obj;
		return ( level == dst.level) && ( sex == dst.sex) && ( menpai == dst.menpai);
	}

}

