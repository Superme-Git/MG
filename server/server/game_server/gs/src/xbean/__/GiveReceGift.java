
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GiveReceGift extends mkdb.XBean implements xbean.GiveReceGift {
	private java.util.HashMap<Integer, Integer> givegift; // 发送礼物 key 为道具itemid value 为数量 by changhao
	private java.util.HashMap<Integer, Integer> recegift; // 接收礼物 key 为道具itemid value 为数量 by changhao

	@Override
	public void _reset_unsafe_() {
		givegift.clear();
		recegift.clear();
	}

	GiveReceGift(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		givegift = new java.util.HashMap<Integer, Integer>();
		recegift = new java.util.HashMap<Integer, Integer>();
	}

	public GiveReceGift() {
		this(0, null, null);
	}

	public GiveReceGift(GiveReceGift _o_) {
		this(_o_, null, null);
	}

	GiveReceGift(xbean.GiveReceGift _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GiveReceGift) assign((GiveReceGift)_o1_);
		else if (_o1_ instanceof GiveReceGift.Data) assign((GiveReceGift.Data)_o1_);
		else if (_o1_ instanceof GiveReceGift.Const) assign(((GiveReceGift.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GiveReceGift _o_) {
		_o_._xdb_verify_unsafe_();
		givegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
			givegift.put(_e_.getKey(), _e_.getValue());
		recegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
			recegift.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(GiveReceGift.Data _o_) {
		givegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
			givegift.put(_e_.getKey(), _e_.getValue());
		recegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
			recegift.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(givegift.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : givegift.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(recegift.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : recegift.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				givegift = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				givegift.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				recegift = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				recegift.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.GiveReceGift copy() {
		_xdb_verify_unsafe_();
		return new GiveReceGift(this);
	}

	@Override
	public xbean.GiveReceGift toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GiveReceGift toBean() {
		_xdb_verify_unsafe_();
		return new GiveReceGift(this); // same as copy()
	}

	@Override
	public xbean.GiveReceGift toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GiveReceGift toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getGivegift() { // 发送礼物 key 为道具itemid value 为数量 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "givegift"), givegift);
	}

	@Override
	public java.util.Map<Integer, Integer> getGivegiftAsData() { // 发送礼物 key 为道具itemid value 为数量 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> givegift;
		GiveReceGift _o_ = this;
		givegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
			givegift.put(_e_.getKey(), _e_.getValue());
		return givegift;
	}

	@Override
	public java.util.Map<Integer, Integer> getRecegift() { // 接收礼物 key 为道具itemid value 为数量 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "recegift"), recegift);
	}

	@Override
	public java.util.Map<Integer, Integer> getRecegiftAsData() { // 接收礼物 key 为道具itemid value 为数量 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> recegift;
		GiveReceGift _o_ = this;
		recegift = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
			recegift.put(_e_.getKey(), _e_.getValue());
		return recegift;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GiveReceGift _o_ = null;
		if ( _o1_ instanceof GiveReceGift ) _o_ = (GiveReceGift)_o1_;
		else if ( _o1_ instanceof GiveReceGift.Const ) _o_ = ((GiveReceGift.Const)_o1_).nThis();
		else return false;
		if (!givegift.equals(_o_.givegift)) return false;
		if (!recegift.equals(_o_.recegift)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += givegift.hashCode();
		_h_ += recegift.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(givegift);
		_sb_.append(",");
		_sb_.append(recegift);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("givegift"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("recegift"));
		return lb;
	}

	private class Const implements xbean.GiveReceGift {
		GiveReceGift nThis() {
			return GiveReceGift.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GiveReceGift copy() {
			return GiveReceGift.this.copy();
		}

		@Override
		public xbean.GiveReceGift toData() {
			return GiveReceGift.this.toData();
		}

		public xbean.GiveReceGift toBean() {
			return GiveReceGift.this.toBean();
		}

		@Override
		public xbean.GiveReceGift toDataIf() {
			return GiveReceGift.this.toDataIf();
		}

		public xbean.GiveReceGift toBeanIf() {
			return GiveReceGift.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getGivegift() { // 发送礼物 key 为道具itemid value 为数量 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(givegift);
		}

		@Override
		public java.util.Map<Integer, Integer> getGivegiftAsData() { // 发送礼物 key 为道具itemid value 为数量 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> givegift;
			GiveReceGift _o_ = GiveReceGift.this;
			givegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
				givegift.put(_e_.getKey(), _e_.getValue());
			return givegift;
		}

		@Override
		public java.util.Map<Integer, Integer> getRecegift() { // 接收礼物 key 为道具itemid value 为数量 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(recegift);
		}

		@Override
		public java.util.Map<Integer, Integer> getRecegiftAsData() { // 接收礼物 key 为道具itemid value 为数量 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> recegift;
			GiveReceGift _o_ = GiveReceGift.this;
			recegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
				recegift.put(_e_.getKey(), _e_.getValue());
			return recegift;
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return GiveReceGift.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GiveReceGift.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GiveReceGift.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GiveReceGift.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GiveReceGift.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GiveReceGift.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GiveReceGift.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GiveReceGift.this.hashCode();
		}

		@Override
		public String toString() {
			return GiveReceGift.this.toString();
		}

	}

	public static final class Data implements xbean.GiveReceGift {
		private java.util.HashMap<Integer, Integer> givegift; // 发送礼物 key 为道具itemid value 为数量 by changhao
		private java.util.HashMap<Integer, Integer> recegift; // 接收礼物 key 为道具itemid value 为数量 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			givegift = new java.util.HashMap<Integer, Integer>();
			recegift = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.GiveReceGift _o1_) {
			if (_o1_ instanceof GiveReceGift) assign((GiveReceGift)_o1_);
			else if (_o1_ instanceof GiveReceGift.Data) assign((GiveReceGift.Data)_o1_);
			else if (_o1_ instanceof GiveReceGift.Const) assign(((GiveReceGift.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GiveReceGift _o_) {
			givegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
				givegift.put(_e_.getKey(), _e_.getValue());
			recegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
				recegift.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(GiveReceGift.Data _o_) {
			givegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.givegift.entrySet())
				givegift.put(_e_.getKey(), _e_.getValue());
			recegift = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.recegift.entrySet())
				recegift.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(givegift.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : givegift.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(recegift.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : recegift.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					givegift = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					givegift.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					recegift = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					recegift.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.GiveReceGift copy() {
			return new Data(this);
		}

		@Override
		public xbean.GiveReceGift toData() {
			return new Data(this);
		}

		public xbean.GiveReceGift toBean() {
			return new GiveReceGift(this, null, null);
		}

		@Override
		public xbean.GiveReceGift toDataIf() {
			return this;
		}

		public xbean.GiveReceGift toBeanIf() {
			return new GiveReceGift(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public java.util.Map<Integer, Integer> getGivegift() { // 发送礼物 key 为道具itemid value 为数量 by changhao
			return givegift;
		}

		@Override
		public java.util.Map<Integer, Integer> getGivegiftAsData() { // 发送礼物 key 为道具itemid value 为数量 by changhao
			return givegift;
		}

		@Override
		public java.util.Map<Integer, Integer> getRecegift() { // 接收礼物 key 为道具itemid value 为数量 by changhao
			return recegift;
		}

		@Override
		public java.util.Map<Integer, Integer> getRecegiftAsData() { // 接收礼物 key 为道具itemid value 为数量 by changhao
			return recegift;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GiveReceGift.Data)) return false;
			GiveReceGift.Data _o_ = (GiveReceGift.Data) _o1_;
			if (!givegift.equals(_o_.givegift)) return false;
			if (!recegift.equals(_o_.recegift)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += givegift.hashCode();
			_h_ += recegift.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(givegift);
			_sb_.append(",");
			_sb_.append(recegift);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
