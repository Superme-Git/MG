package gs.counter.bean;

public class MoneyStatKey extends LevelStatKey {

	public int mtype;
	public long asid;

	public MoneyStatKey() {
		mtype = 0;
		asid = 0;
	}

	public MoneyStatKey( MoneyStatKey _src_) {
		super( _src_);
		mtype = _src_.mtype;
		asid = _src_.asid;
	}

	public MoneyStatKey( LevelStatKey extendbean, int mtype, long asid) {
		super( extendbean);
		this.mtype = mtype;
		this.asid = asid;
	}

	public int getMtype() {
		return mtype;
	}

	public long getAsid() {
		return asid;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ mtype ^ (int)(asid ^ (asid >>> 32 ));
	}

	@Override
	public boolean equals(Object obj) {
		if( !super.equals(obj))
			return false;
		MoneyStatKey dst = (MoneyStatKey)obj;
		return ( mtype == dst.mtype) && ( asid == dst.asid);
	}

}

