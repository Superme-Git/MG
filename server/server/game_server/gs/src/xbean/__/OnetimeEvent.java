
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OnetimeEvent extends mkdb.XBean implements xbean.OnetimeEvent {
	private fire.pb.WorldEventTab.OnetimeTask onetimeevent; // 

	@Override
	public void _reset_unsafe_() {
		onetimeevent = null;
	}

	OnetimeEvent(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		onetimeevent = null;
	}

	public OnetimeEvent() {
		this(0, null, null);
	}

	public OnetimeEvent(OnetimeEvent _o_) {
		this(_o_, null, null);
	}

	OnetimeEvent(xbean.OnetimeEvent _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.OnetimeEvent copy() {
		_xdb_verify_unsafe_();
		return new OnetimeEvent(this);
	}

	@Override
	public xbean.OnetimeEvent toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OnetimeEvent toBean() {
		_xdb_verify_unsafe_();
		return new OnetimeEvent(this); // same as copy()
	}

	@Override
	public xbean.OnetimeEvent toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OnetimeEvent toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public fire.pb.WorldEventTab.OnetimeTask getOnetimeevent() { // 
		_xdb_verify_unsafe_();
		return onetimeevent;
	}

	@Override
	public void setOnetimeevent(fire.pb.WorldEventTab.OnetimeTask _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onetimeevent") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.pb.WorldEventTab.OnetimeTask>(this, onetimeevent) {
					public void rollback() { onetimeevent = _xdb_saved; }
			}; }});
		onetimeevent = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OnetimeEvent _o_ = null;
		if ( _o1_ instanceof OnetimeEvent ) _o_ = (OnetimeEvent)_o1_;
		else if ( _o1_ instanceof OnetimeEvent.Const ) _o_ = ((OnetimeEvent.Const)_o1_).nThis();
		else return false;
		if ((null == onetimeevent && null != _o_.onetimeevent) || (null != onetimeevent && null == _o_.onetimeevent) || (null != onetimeevent && null != _o_.onetimeevent && !onetimeevent.equals(_o_.onetimeevent))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (onetimeevent == null ? 0 : onetimeevent.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(onetimeevent);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onetimeevent"));
		return lb;
	}

	private class Const implements xbean.OnetimeEvent {
		OnetimeEvent nThis() {
			return OnetimeEvent.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OnetimeEvent copy() {
			return OnetimeEvent.this.copy();
		}

		@Override
		public xbean.OnetimeEvent toData() {
			return OnetimeEvent.this.toData();
		}

		public xbean.OnetimeEvent toBean() {
			return OnetimeEvent.this.toBean();
		}

		@Override
		public xbean.OnetimeEvent toDataIf() {
			return OnetimeEvent.this.toDataIf();
		}

		public xbean.OnetimeEvent toBeanIf() {
			return OnetimeEvent.this.toBeanIf();
		}

		@Override
		public fire.pb.WorldEventTab.OnetimeTask getOnetimeevent() { // 
			_xdb_verify_unsafe_();
			return onetimeevent;
		}

		@Override
		public void setOnetimeevent(fire.pb.WorldEventTab.OnetimeTask _v_) { // 
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
			return OnetimeEvent.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OnetimeEvent.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OnetimeEvent.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OnetimeEvent.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OnetimeEvent.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OnetimeEvent.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OnetimeEvent.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OnetimeEvent.this.hashCode();
		}

		@Override
		public String toString() {
			return OnetimeEvent.this.toString();
		}

	}

	public static final class Data implements xbean.OnetimeEvent {
		private fire.pb.WorldEventTab.OnetimeTask onetimeevent; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			onetimeevent = null;
		}

		Data(xbean.OnetimeEvent _o1_) {
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
		public xbean.OnetimeEvent copy() {
			return new Data(this);
		}

		@Override
		public xbean.OnetimeEvent toData() {
			return new Data(this);
		}

		public xbean.OnetimeEvent toBean() {
			return new OnetimeEvent(this, null, null);
		}

		@Override
		public xbean.OnetimeEvent toDataIf() {
			return this;
		}

		public xbean.OnetimeEvent toBeanIf() {
			return new OnetimeEvent(this, null, null);
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
		public fire.pb.WorldEventTab.OnetimeTask getOnetimeevent() { // 
			return onetimeevent;
		}

		@Override
		public void setOnetimeevent(fire.pb.WorldEventTab.OnetimeTask _v_) { // 
			onetimeevent = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OnetimeEvent.Data)) return false;
			OnetimeEvent.Data _o_ = (OnetimeEvent.Data) _o1_;
			if ((null == onetimeevent && null != _o_.onetimeevent) || (null != onetimeevent && null == _o_.onetimeevent) || (null != onetimeevent && null != _o_.onetimeevent && !onetimeevent.equals(_o_.onetimeevent))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (onetimeevent == null ? 0 : onetimeevent.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(onetimeevent);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
