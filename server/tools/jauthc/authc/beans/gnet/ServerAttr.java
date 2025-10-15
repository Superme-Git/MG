
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 该文件中的协议都是共用的，至少被client，glinkd，gdeliverd，gamedbd四者中的两者使用
*/
public class ServerAttr implements Marshal {
	public final static int FLAG_DOUBLE_DROP = 4;
	public final static int FLAG_DOUBLE_SP = 8;

	public int flags; // 当前服务器状态，即上面列出的状态
	public byte load;
	public java.util.HashMap<Integer,Integer> extra;

	public ServerAttr() {
		extra = new java.util.HashMap<Integer,Integer>();
	}

	public ServerAttr(int _flags_, byte _load_, java.util.HashMap<Integer,Integer> _extra_) {
		this.flags = _flags_;
		this.load = _load_;
		this.extra = _extra_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(flags);
		_os_.marshal(load);
		_os_.compact_uint32(extra.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : extra.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flags = _os_.unmarshal_int();
		load = _os_.unmarshal_byte();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			extra.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ServerAttr) {
			ServerAttr _o_ = (ServerAttr)_o1_;
			if (flags != _o_.flags) return false;
			if (load != _o_.load) return false;
			if (!extra.equals(_o_.extra)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += flags;
		_h_ += (int)load;
		_h_ += extra.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flags).append(",");
		_sb_.append(load).append(",");
		_sb_.append(extra).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

