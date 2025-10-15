
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LiveSkill extends mkdb.XBean implements xbean.LiveSkill {
	private int level; // 

	@Override
	public void _reset_unsafe_() {
		level = 0;
	}

	LiveSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LiveSkill() {
		this(0, null, null);
	}

	public LiveSkill(LiveSkill _o_) {
		this(_o_, null, null);
	}

	LiveSkill(xbean.LiveSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LiveSkill) assign((LiveSkill)_o1_);
		else if (_o1_ instanceof LiveSkill.Data) assign((LiveSkill.Data)_o1_);
		else if (_o1_ instanceof LiveSkill.Const) assign(((LiveSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LiveSkill _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
	}

	private void assign(LiveSkill.Data _o_) {
		level = _o_.level;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(level);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		level = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LiveSkill copy() {
		_xdb_verify_unsafe_();
		return new LiveSkill(this);
	}

	@Override
	public xbean.LiveSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveSkill toBean() {
		_xdb_verify_unsafe_();
		return new LiveSkill(this); // same as copy()
	}

	@Override
	public xbean.LiveSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLevel() { // 
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public void setLevel(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LiveSkill _o_ = null;
		if ( _o1_ instanceof LiveSkill ) _o_ = (LiveSkill)_o1_;
		else if ( _o1_ instanceof LiveSkill.Const ) _o_ = ((LiveSkill.Const)_o1_).nThis();
		else return false;
		if (level != _o_.level) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += level;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(level);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		return lb;
	}

	private class Const implements xbean.LiveSkill {
		LiveSkill nThis() {
			return LiveSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LiveSkill copy() {
			return LiveSkill.this.copy();
		}

		@Override
		public xbean.LiveSkill toData() {
			return LiveSkill.this.toData();
		}

		public xbean.LiveSkill toBean() {
			return LiveSkill.this.toBean();
		}

		@Override
		public xbean.LiveSkill toDataIf() {
			return LiveSkill.this.toDataIf();
		}

		public xbean.LiveSkill toBeanIf() {
			return LiveSkill.this.toBeanIf();
		}

		@Override
		public int getLevel() { // 
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public void setLevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
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
			return LiveSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LiveSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LiveSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LiveSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LiveSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LiveSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LiveSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LiveSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return LiveSkill.this.toString();
		}

	}

	public static final class Data implements xbean.LiveSkill {
		private int level; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LiveSkill _o1_) {
			if (_o1_ instanceof LiveSkill) assign((LiveSkill)_o1_);
			else if (_o1_ instanceof LiveSkill.Data) assign((LiveSkill.Data)_o1_);
			else if (_o1_ instanceof LiveSkill.Const) assign(((LiveSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LiveSkill _o_) {
			level = _o_.level;
		}

		private void assign(LiveSkill.Data _o_) {
			level = _o_.level;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(level);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			level = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LiveSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.LiveSkill toData() {
			return new Data(this);
		}

		public xbean.LiveSkill toBean() {
			return new LiveSkill(this, null, null);
		}

		@Override
		public xbean.LiveSkill toDataIf() {
			return this;
		}

		public xbean.LiveSkill toBeanIf() {
			return new LiveSkill(this, null, null);
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
		public int getLevel() { // 
			return level;
		}

		@Override
		public void setLevel(int _v_) { // 
			level = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LiveSkill.Data)) return false;
			LiveSkill.Data _o_ = (LiveSkill.Data) _o1_;
			if (level != _o_.level) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += level;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(level);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
