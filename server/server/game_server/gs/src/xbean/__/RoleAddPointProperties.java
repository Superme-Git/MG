
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleAddPointProperties extends mkdb.XBean implements xbean.RoleAddPointProperties {
	private java.util.HashMap<Integer, Integer> cons_save; // 已分配体质
	private java.util.HashMap<Integer, Integer> iq_save; // 已分配智力
	private java.util.HashMap<Integer, Integer> str_save; // 已分配力量
	private java.util.HashMap<Integer, Integer> endu_save; // 已分配耐力
	private java.util.HashMap<Integer, Integer> agi_save; // 已分配敏捷

	@Override
	public void _reset_unsafe_() {
		cons_save.clear();
		iq_save.clear();
		str_save.clear();
		endu_save.clear();
		agi_save.clear();
	}

	RoleAddPointProperties(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		cons_save = new java.util.HashMap<Integer, Integer>();
		iq_save = new java.util.HashMap<Integer, Integer>();
		str_save = new java.util.HashMap<Integer, Integer>();
		endu_save = new java.util.HashMap<Integer, Integer>();
		agi_save = new java.util.HashMap<Integer, Integer>();
	}

	public RoleAddPointProperties() {
		this(0, null, null);
	}

	public RoleAddPointProperties(RoleAddPointProperties _o_) {
		this(_o_, null, null);
	}

	RoleAddPointProperties(xbean.RoleAddPointProperties _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleAddPointProperties) assign((RoleAddPointProperties)_o1_);
		else if (_o1_ instanceof RoleAddPointProperties.Data) assign((RoleAddPointProperties.Data)_o1_);
		else if (_o1_ instanceof RoleAddPointProperties.Const) assign(((RoleAddPointProperties.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleAddPointProperties _o_) {
		_o_._xdb_verify_unsafe_();
		cons_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
			cons_save.put(_e_.getKey(), _e_.getValue());
		iq_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
			iq_save.put(_e_.getKey(), _e_.getValue());
		str_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
			str_save.put(_e_.getKey(), _e_.getValue());
		endu_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
			endu_save.put(_e_.getKey(), _e_.getValue());
		agi_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
			agi_save.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RoleAddPointProperties.Data _o_) {
		cons_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
			cons_save.put(_e_.getKey(), _e_.getValue());
		iq_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
			iq_save.put(_e_.getKey(), _e_.getValue());
		str_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
			str_save.put(_e_.getKey(), _e_.getValue());
		endu_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
			endu_save.put(_e_.getKey(), _e_.getValue());
		agi_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
			agi_save.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(cons_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : cons_save.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(iq_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : iq_save.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(str_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : str_save.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(endu_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : endu_save.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(agi_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : agi_save.entrySet())
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
				cons_save = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				cons_save.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				iq_save = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				iq_save.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				str_save = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				str_save.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				endu_save = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				endu_save.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				agi_save = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				agi_save.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleAddPointProperties copy() {
		_xdb_verify_unsafe_();
		return new RoleAddPointProperties(this);
	}

	@Override
	public xbean.RoleAddPointProperties toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleAddPointProperties toBean() {
		_xdb_verify_unsafe_();
		return new RoleAddPointProperties(this); // same as copy()
	}

	@Override
	public xbean.RoleAddPointProperties toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleAddPointProperties toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getCons_save() { // 已分配体质
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "cons_save"), cons_save);
	}

	@Override
	public java.util.Map<Integer, Integer> getCons_saveAsData() { // 已分配体质
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> cons_save;
		RoleAddPointProperties _o_ = this;
		cons_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
			cons_save.put(_e_.getKey(), _e_.getValue());
		return cons_save;
	}

	@Override
	public java.util.Map<Integer, Integer> getIq_save() { // 已分配智力
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "iq_save"), iq_save);
	}

	@Override
	public java.util.Map<Integer, Integer> getIq_saveAsData() { // 已分配智力
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> iq_save;
		RoleAddPointProperties _o_ = this;
		iq_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
			iq_save.put(_e_.getKey(), _e_.getValue());
		return iq_save;
	}

	@Override
	public java.util.Map<Integer, Integer> getStr_save() { // 已分配力量
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "str_save"), str_save);
	}

	@Override
	public java.util.Map<Integer, Integer> getStr_saveAsData() { // 已分配力量
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> str_save;
		RoleAddPointProperties _o_ = this;
		str_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
			str_save.put(_e_.getKey(), _e_.getValue());
		return str_save;
	}

	@Override
	public java.util.Map<Integer, Integer> getEndu_save() { // 已分配耐力
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "endu_save"), endu_save);
	}

	@Override
	public java.util.Map<Integer, Integer> getEndu_saveAsData() { // 已分配耐力
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> endu_save;
		RoleAddPointProperties _o_ = this;
		endu_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
			endu_save.put(_e_.getKey(), _e_.getValue());
		return endu_save;
	}

	@Override
	public java.util.Map<Integer, Integer> getAgi_save() { // 已分配敏捷
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "agi_save"), agi_save);
	}

	@Override
	public java.util.Map<Integer, Integer> getAgi_saveAsData() { // 已分配敏捷
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> agi_save;
		RoleAddPointProperties _o_ = this;
		agi_save = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
			agi_save.put(_e_.getKey(), _e_.getValue());
		return agi_save;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleAddPointProperties _o_ = null;
		if ( _o1_ instanceof RoleAddPointProperties ) _o_ = (RoleAddPointProperties)_o1_;
		else if ( _o1_ instanceof RoleAddPointProperties.Const ) _o_ = ((RoleAddPointProperties.Const)_o1_).nThis();
		else return false;
		if (!cons_save.equals(_o_.cons_save)) return false;
		if (!iq_save.equals(_o_.iq_save)) return false;
		if (!str_save.equals(_o_.str_save)) return false;
		if (!endu_save.equals(_o_.endu_save)) return false;
		if (!agi_save.equals(_o_.agi_save)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += cons_save.hashCode();
		_h_ += iq_save.hashCode();
		_h_ += str_save.hashCode();
		_h_ += endu_save.hashCode();
		_h_ += agi_save.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cons_save);
		_sb_.append(",");
		_sb_.append(iq_save);
		_sb_.append(",");
		_sb_.append(str_save);
		_sb_.append(",");
		_sb_.append(endu_save);
		_sb_.append(",");
		_sb_.append(agi_save);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("cons_save"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("iq_save"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("str_save"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("endu_save"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("agi_save"));
		return lb;
	}

	private class Const implements xbean.RoleAddPointProperties {
		RoleAddPointProperties nThis() {
			return RoleAddPointProperties.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleAddPointProperties copy() {
			return RoleAddPointProperties.this.copy();
		}

		@Override
		public xbean.RoleAddPointProperties toData() {
			return RoleAddPointProperties.this.toData();
		}

		public xbean.RoleAddPointProperties toBean() {
			return RoleAddPointProperties.this.toBean();
		}

		@Override
		public xbean.RoleAddPointProperties toDataIf() {
			return RoleAddPointProperties.this.toDataIf();
		}

		public xbean.RoleAddPointProperties toBeanIf() {
			return RoleAddPointProperties.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getCons_save() { // 已分配体质
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(cons_save);
		}

		@Override
		public java.util.Map<Integer, Integer> getCons_saveAsData() { // 已分配体质
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> cons_save;
			RoleAddPointProperties _o_ = RoleAddPointProperties.this;
			cons_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
				cons_save.put(_e_.getKey(), _e_.getValue());
			return cons_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getIq_save() { // 已分配智力
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(iq_save);
		}

		@Override
		public java.util.Map<Integer, Integer> getIq_saveAsData() { // 已分配智力
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> iq_save;
			RoleAddPointProperties _o_ = RoleAddPointProperties.this;
			iq_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
				iq_save.put(_e_.getKey(), _e_.getValue());
			return iq_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getStr_save() { // 已分配力量
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(str_save);
		}

		@Override
		public java.util.Map<Integer, Integer> getStr_saveAsData() { // 已分配力量
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> str_save;
			RoleAddPointProperties _o_ = RoleAddPointProperties.this;
			str_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
				str_save.put(_e_.getKey(), _e_.getValue());
			return str_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getEndu_save() { // 已分配耐力
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(endu_save);
		}

		@Override
		public java.util.Map<Integer, Integer> getEndu_saveAsData() { // 已分配耐力
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> endu_save;
			RoleAddPointProperties _o_ = RoleAddPointProperties.this;
			endu_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
				endu_save.put(_e_.getKey(), _e_.getValue());
			return endu_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getAgi_save() { // 已分配敏捷
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(agi_save);
		}

		@Override
		public java.util.Map<Integer, Integer> getAgi_saveAsData() { // 已分配敏捷
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> agi_save;
			RoleAddPointProperties _o_ = RoleAddPointProperties.this;
			agi_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
				agi_save.put(_e_.getKey(), _e_.getValue());
			return agi_save;
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
			return RoleAddPointProperties.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleAddPointProperties.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleAddPointProperties.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleAddPointProperties.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleAddPointProperties.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleAddPointProperties.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleAddPointProperties.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleAddPointProperties.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleAddPointProperties.this.toString();
		}

	}

	public static final class Data implements xbean.RoleAddPointProperties {
		private java.util.HashMap<Integer, Integer> cons_save; // 已分配体质
		private java.util.HashMap<Integer, Integer> iq_save; // 已分配智力
		private java.util.HashMap<Integer, Integer> str_save; // 已分配力量
		private java.util.HashMap<Integer, Integer> endu_save; // 已分配耐力
		private java.util.HashMap<Integer, Integer> agi_save; // 已分配敏捷

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			cons_save = new java.util.HashMap<Integer, Integer>();
			iq_save = new java.util.HashMap<Integer, Integer>();
			str_save = new java.util.HashMap<Integer, Integer>();
			endu_save = new java.util.HashMap<Integer, Integer>();
			agi_save = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.RoleAddPointProperties _o1_) {
			if (_o1_ instanceof RoleAddPointProperties) assign((RoleAddPointProperties)_o1_);
			else if (_o1_ instanceof RoleAddPointProperties.Data) assign((RoleAddPointProperties.Data)_o1_);
			else if (_o1_ instanceof RoleAddPointProperties.Const) assign(((RoleAddPointProperties.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleAddPointProperties _o_) {
			cons_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
				cons_save.put(_e_.getKey(), _e_.getValue());
			iq_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
				iq_save.put(_e_.getKey(), _e_.getValue());
			str_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
				str_save.put(_e_.getKey(), _e_.getValue());
			endu_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
				endu_save.put(_e_.getKey(), _e_.getValue());
			agi_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
				agi_save.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RoleAddPointProperties.Data _o_) {
			cons_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.cons_save.entrySet())
				cons_save.put(_e_.getKey(), _e_.getValue());
			iq_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.iq_save.entrySet())
				iq_save.put(_e_.getKey(), _e_.getValue());
			str_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.str_save.entrySet())
				str_save.put(_e_.getKey(), _e_.getValue());
			endu_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.endu_save.entrySet())
				endu_save.put(_e_.getKey(), _e_.getValue());
			agi_save = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.agi_save.entrySet())
				agi_save.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(cons_save.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : cons_save.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(iq_save.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : iq_save.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(str_save.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : str_save.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(endu_save.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : endu_save.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(agi_save.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : agi_save.entrySet())
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
					cons_save = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					cons_save.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					iq_save = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					iq_save.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					str_save = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					str_save.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					endu_save = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					endu_save.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					agi_save = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					agi_save.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleAddPointProperties copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleAddPointProperties toData() {
			return new Data(this);
		}

		public xbean.RoleAddPointProperties toBean() {
			return new RoleAddPointProperties(this, null, null);
		}

		@Override
		public xbean.RoleAddPointProperties toDataIf() {
			return this;
		}

		public xbean.RoleAddPointProperties toBeanIf() {
			return new RoleAddPointProperties(this, null, null);
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
		public java.util.Map<Integer, Integer> getCons_save() { // 已分配体质
			return cons_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getCons_saveAsData() { // 已分配体质
			return cons_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getIq_save() { // 已分配智力
			return iq_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getIq_saveAsData() { // 已分配智力
			return iq_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getStr_save() { // 已分配力量
			return str_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getStr_saveAsData() { // 已分配力量
			return str_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getEndu_save() { // 已分配耐力
			return endu_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getEndu_saveAsData() { // 已分配耐力
			return endu_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getAgi_save() { // 已分配敏捷
			return agi_save;
		}

		@Override
		public java.util.Map<Integer, Integer> getAgi_saveAsData() { // 已分配敏捷
			return agi_save;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleAddPointProperties.Data)) return false;
			RoleAddPointProperties.Data _o_ = (RoleAddPointProperties.Data) _o1_;
			if (!cons_save.equals(_o_.cons_save)) return false;
			if (!iq_save.equals(_o_.iq_save)) return false;
			if (!str_save.equals(_o_.str_save)) return false;
			if (!endu_save.equals(_o_.endu_save)) return false;
			if (!agi_save.equals(_o_.agi_save)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += cons_save.hashCode();
			_h_ += iq_save.hashCode();
			_h_ += str_save.hashCode();
			_h_ += endu_save.hashCode();
			_h_ += agi_save.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(cons_save);
			_sb_.append(",");
			_sb_.append(iq_save);
			_sb_.append(",");
			_sb_.append(str_save);
			_sb_.append(",");
			_sb_.append(endu_save);
			_sb_.append(",");
			_sb_.append(agi_save);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
