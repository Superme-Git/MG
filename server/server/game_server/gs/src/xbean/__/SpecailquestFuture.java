
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SpecailquestFuture extends mkdb.XBean implements xbean.SpecailquestFuture {
	private java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>> questfuture; // 

	@Override
	public void _reset_unsafe_() {
		questfuture.clear();
	}

	SpecailquestFuture(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		questfuture = new java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>>();
	}

	public SpecailquestFuture() {
		this(0, null, null);
	}

	public SpecailquestFuture(SpecailquestFuture _o_) {
		this(_o_, null, null);
	}

	SpecailquestFuture(xbean.SpecailquestFuture _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.SpecailquestFuture copy() {
		_xdb_verify_unsafe_();
		return new SpecailquestFuture(this);
	}

	@Override
	public xbean.SpecailquestFuture toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SpecailquestFuture toBean() {
		_xdb_verify_unsafe_();
		return new SpecailquestFuture(this); // same as copy()
	}

	@Override
	public xbean.SpecailquestFuture toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SpecailquestFuture toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getQuestfuture() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "questfuture"), questfuture);
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SpecailquestFuture _o_ = null;
		if ( _o1_ instanceof SpecailquestFuture ) _o_ = (SpecailquestFuture)_o1_;
		else if ( _o1_ instanceof SpecailquestFuture.Const ) _o_ = ((SpecailquestFuture.Const)_o1_).nThis();
		else return false;
		if (!questfuture.equals(_o_.questfuture)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += questfuture.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questfuture);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("questfuture"));
		return lb;
	}

	private class Const implements xbean.SpecailquestFuture {
		SpecailquestFuture nThis() {
			return SpecailquestFuture.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SpecailquestFuture copy() {
			return SpecailquestFuture.this.copy();
		}

		@Override
		public xbean.SpecailquestFuture toData() {
			return SpecailquestFuture.this.toData();
		}

		public xbean.SpecailquestFuture toBean() {
			return SpecailquestFuture.this.toBean();
		}

		@Override
		public xbean.SpecailquestFuture toDataIf() {
			return SpecailquestFuture.this.toDataIf();
		}

		public xbean.SpecailquestFuture toBeanIf() {
			return SpecailquestFuture.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getQuestfuture() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(questfuture);
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
			return SpecailquestFuture.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SpecailquestFuture.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SpecailquestFuture.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SpecailquestFuture.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SpecailquestFuture.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SpecailquestFuture.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SpecailquestFuture.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SpecailquestFuture.this.hashCode();
		}

		@Override
		public String toString() {
			return SpecailquestFuture.this.toString();
		}

	}

	public static final class Data implements xbean.SpecailquestFuture {
		private java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>> questfuture; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			questfuture = new java.util.HashMap<Integer, java.util.concurrent.ScheduledFuture<?>>();
		}

		Data(xbean.SpecailquestFuture _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SpecailquestFuture copy() {
			return new Data(this);
		}

		@Override
		public xbean.SpecailquestFuture toData() {
			return new Data(this);
		}

		public xbean.SpecailquestFuture toBean() {
			return new SpecailquestFuture(this, null, null);
		}

		@Override
		public xbean.SpecailquestFuture toDataIf() {
			return this;
		}

		public xbean.SpecailquestFuture toBeanIf() {
			return new SpecailquestFuture(this, null, null);
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
		public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getQuestfuture() { // 
			return questfuture;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SpecailquestFuture.Data)) return false;
			SpecailquestFuture.Data _o_ = (SpecailquestFuture.Data) _o1_;
			if (!questfuture.equals(_o_.questfuture)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += questfuture.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(questfuture);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
