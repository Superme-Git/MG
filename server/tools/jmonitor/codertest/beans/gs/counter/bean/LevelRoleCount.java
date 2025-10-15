package gs.counter.bean;

public class LevelRoleCount {

	public long count;

	public LevelRoleCount() {
		count = 0;
	}

	public LevelRoleCount( LevelRoleCount _src_) {
		count = _src_.count;
	}

	public LevelRoleCount( long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	@Override
	public int hashCode() {
		return (int)(count ^ (count >>> 32 ));
	}

	@Override
	public boolean equals(Object obj) {
		LevelRoleCount dst = (LevelRoleCount)obj;
		return ( count == dst.count);
	}

}

