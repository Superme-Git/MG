
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Bag implements Marshal {
	public java.util.HashMap<Byte,Long> currency; // key 货币类型,参见MoneyType
	public int capacity;
	public java.util.ArrayList<fire.pb.Item> items;

	public Bag() {
		currency = new java.util.HashMap<Byte,Long>();
		items = new java.util.ArrayList<fire.pb.Item>();
	}

	public Bag(java.util.HashMap<Byte,Long> _currency_, int _capacity_, java.util.ArrayList<fire.pb.Item> _items_) {
		this.currency = _currency_;
		this.capacity = _capacity_;
		this.items = _items_;
	}

	public final boolean _validator_() {
		if (capacity < 0) return false;
		for (fire.pb.Item _v_ : items)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.compact_uint32(currency.size());
		for (java.util.Map.Entry<Byte, Long> _e_ : currency.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(capacity);
		_os_.compact_uint32(items.size());
		for (fire.pb.Item _v_ : items) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			long _v_;
			_v_ = _os_.unmarshal_long();
			currency.put(_k_, _v_);
		}
		capacity = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.Item _v_ = new fire.pb.Item();
			_v_.unmarshal(_os_);
			items.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Bag) {
			Bag _o_ = (Bag)_o1_;
			if (!currency.equals(_o_.currency)) return false;
			if (capacity != _o_.capacity) return false;
			if (!items.equals(_o_.items)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += currency.hashCode();
		_h_ += capacity;
		_h_ += items.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currency).append(",");
		_sb_.append(capacity).append(",");
		_sb_.append(items).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

