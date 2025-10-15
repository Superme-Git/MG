
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EventInfo extends mkdb.XBean implements xbean.EventInfo {
	private fire.pb.WorldEventTab.CrontabTask event; // 

	@Override
	public void _reset_unsafe_() {
		event = null;
	}

	EventInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		event = null;
	}

	public EventInfo() {
		this(0, null, null);
	}

	public EventInfo(EventInfo _o_) {
		this(_o_, null, null);
	}

	EventInfo(xbean.EventInfo _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.EventInfo copy() {
		_xdb_verify_unsafe_();
		return new EventInfo(this);
	}

	@Override
	public xbean.EventInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EventInfo toBean() {
		_xdb_verify_unsafe_();
		return new EventInfo(this); // same as copy()
	}

	@Override
	public xbean.EventInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EventInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public fire.pb.WorldEventTab.CrontabTask getEvent() { // 
		_xdb_verify_unsafe_();
		return event;
	}

	@Override
	public void setEvent(fire.pb.WorldEventTab.CrontabTask _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "event") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.pb.WorldEventTab.CrontabTask>(this, event) {
					public void rollback() { event = _xdb_saved; }
			}; }});
		event = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EventInfo _o_ = null;
		if ( _o1_ instanceof EventInfo ) _o_ = (EventInfo)_o1_;
		else if ( _o1_ instanceof EventInfo.Const ) _o_ = ((EventInfo.Const)_o1_).nThis();
		else return false;
		if ((null == event && null != _o_.event) || (null != event && null == _o_.event) || (null != event && null != _o_.event && !event.equals(_o_.event))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (event == null ? 0 : event.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(event);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("event"));
		return lb;
	}

	private class Const implements xbean.EventInfo {
		EventInfo nThis() {
			return EventInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EventInfo copy() {
			return EventInfo.this.copy();
		}

		@Override
		public xbean.EventInfo toData() {
			return EventInfo.this.toData();
		}

		public xbean.EventInfo toBean() {
			return EventInfo.this.toBean();
		}

		@Override
		public xbean.EventInfo toDataIf() {
			return EventInfo.this.toDataIf();
		}

		public xbean.EventInfo toBeanIf() {
			return EventInfo.this.toBeanIf();
		}

		@Override
		public fire.pb.WorldEventTab.CrontabTask getEvent() { // 
			_xdb_verify_unsafe_();
			return event;
		}

		@Override
		public void setEvent(fire.pb.WorldEventTab.CrontabTask _v_) { // 
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
			return EventInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EventInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EventInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EventInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EventInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EventInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EventInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EventInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return EventInfo.this.toString();
		}

	}

	public static final class Data implements xbean.EventInfo {
		private fire.pb.WorldEventTab.CrontabTask event; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			event = null;
		}

		Data(xbean.EventInfo _o1_) {
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
		public xbean.EventInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.EventInfo toData() {
			return new Data(this);
		}

		public xbean.EventInfo toBean() {
			return new EventInfo(this, null, null);
		}

		@Override
		public xbean.EventInfo toDataIf() {
			return this;
		}

		public xbean.EventInfo toBeanIf() {
			return new EventInfo(this, null, null);
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
		public fire.pb.WorldEventTab.CrontabTask getEvent() { // 
			return event;
		}

		@Override
		public void setEvent(fire.pb.WorldEventTab.CrontabTask _v_) { // 
			event = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EventInfo.Data)) return false;
			EventInfo.Data _o_ = (EventInfo.Data) _o1_;
			if ((null == event && null != _o_.event) || (null != event && null == _o_.event) || (null != event && null != _o_.event && !event.equals(_o_.event))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (event == null ? 0 : event.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(event);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
