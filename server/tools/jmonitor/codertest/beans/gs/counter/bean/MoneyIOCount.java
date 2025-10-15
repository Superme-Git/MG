package gs.counter.bean;

public class MoneyIOCount {

	public long incount;
	public long outcount;

	public MoneyIOCount() {
		incount = 0;
		outcount = 0;
	}

	public MoneyIOCount( MoneyIOCount _src_) {
		incount = _src_.incount;
		outcount = _src_.outcount;
	}

	public MoneyIOCount( long incount, long outcount) {
		this.incount = incount;
		this.outcount = outcount;
	}

	public long getIncount() {
		return incount;
	}

	public long getOutcount() {
		return outcount;
	}

	@Override
	public int hashCode() {
		return (int)(incount ^ (incount >>> 32 )) ^ (int)(outcount ^ (outcount >>> 32 ));
	}

	@Override
	public boolean equals(Object obj) {
		MoneyIOCount dst = (MoneyIOCount)obj;
		return ( incount == dst.incount) && ( outcount == dst.outcount);
	}

}

