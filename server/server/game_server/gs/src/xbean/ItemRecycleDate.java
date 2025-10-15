
package xbean;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public class ItemRecycleDate implements Marshal, Comparable<ItemRecycleDate> {

	private int year; // 
	private int dayofyear; // 

	public ItemRecycleDate() {
	}

	public ItemRecycleDate(int year, int dayofyear) {
		this.year = year;
		this.dayofyear = dayofyear;
	}

	public int getYear() { // 
		return year;
	}

	public int getDayofyear() { // 
		return dayofyear;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(year);
		_os_.marshal(dayofyear);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		year = _os_.unmarshal_int();
		dayofyear = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(ItemRecycleDate _o_) {
		if (_o_ == this)
			return 0;
		int _c_ = 0;
		_c_ = Integer.signum(year - _o_.year);
		if (0 != _c_) return _c_;
		_c_ = Integer.signum(dayofyear - _o_.dayofyear);
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o_) {
		if (_o_ instanceof ItemRecycleDate)
			return 0 == this.compareTo((ItemRecycleDate)_o_);
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += year;
		_h_ += dayofyear;
		return _h_;
	}

}
