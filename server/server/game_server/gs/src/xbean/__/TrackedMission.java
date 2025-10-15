
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TrackedMission extends mkdb.XBean implements xbean.TrackedMission {
	private java.util.HashMap<Integer, xbean.Track> questids; // 

	@Override
	public void _reset_unsafe_() {
		questids.clear();
	}

	TrackedMission(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		questids = new java.util.HashMap<Integer, xbean.Track>();
	}

	public TrackedMission() {
		this(0, null, null);
	}

	public TrackedMission(TrackedMission _o_) {
		this(_o_, null, null);
	}

	TrackedMission(xbean.TrackedMission _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TrackedMission) assign((TrackedMission)_o1_);
		else if (_o1_ instanceof TrackedMission.Data) assign((TrackedMission.Data)_o1_);
		else if (_o1_ instanceof TrackedMission.Const) assign(((TrackedMission.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TrackedMission _o_) {
		_o_._xdb_verify_unsafe_();
		questids = new java.util.HashMap<Integer, xbean.Track>();
		for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
			questids.put(_e_.getKey(), new Track(_e_.getValue(), this, "questids"));
	}

	private void assign(TrackedMission.Data _o_) {
		questids = new java.util.HashMap<Integer, xbean.Track>();
		for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
			questids.put(_e_.getKey(), new Track(_e_.getValue(), this, "questids"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(questids.size());
		for (java.util.Map.Entry<Integer, xbean.Track> _e_ : questids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				questids = new java.util.HashMap<Integer, xbean.Track>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Track _v_ = new Track(0, this, "questids");
				_v_.unmarshal(_os_);
				questids.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.TrackedMission copy() {
		_xdb_verify_unsafe_();
		return new TrackedMission(this);
	}

	@Override
	public xbean.TrackedMission toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TrackedMission toBean() {
		_xdb_verify_unsafe_();
		return new TrackedMission(this); // same as copy()
	}

	@Override
	public xbean.TrackedMission toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TrackedMission toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.Track> getQuestids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "questids"), questids);
	}

	@Override
	public java.util.Map<Integer, xbean.Track> getQuestidsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Track> questids;
		TrackedMission _o_ = this;
		questids = new java.util.HashMap<Integer, xbean.Track>();
		for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
			questids.put(_e_.getKey(), new Track.Data(_e_.getValue()));
		return questids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TrackedMission _o_ = null;
		if ( _o1_ instanceof TrackedMission ) _o_ = (TrackedMission)_o1_;
		else if ( _o1_ instanceof TrackedMission.Const ) _o_ = ((TrackedMission.Const)_o1_).nThis();
		else return false;
		if (!questids.equals(_o_.questids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += questids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("questids"));
		return lb;
	}

	private class Const implements xbean.TrackedMission {
		TrackedMission nThis() {
			return TrackedMission.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TrackedMission copy() {
			return TrackedMission.this.copy();
		}

		@Override
		public xbean.TrackedMission toData() {
			return TrackedMission.this.toData();
		}

		public xbean.TrackedMission toBean() {
			return TrackedMission.this.toBean();
		}

		@Override
		public xbean.TrackedMission toDataIf() {
			return TrackedMission.this.toDataIf();
		}

		public xbean.TrackedMission toBeanIf() {
			return TrackedMission.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.Track> getQuestids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(questids);
		}

		@Override
		public java.util.Map<Integer, xbean.Track> getQuestidsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Track> questids;
			TrackedMission _o_ = TrackedMission.this;
			questids = new java.util.HashMap<Integer, xbean.Track>();
			for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
				questids.put(_e_.getKey(), new Track.Data(_e_.getValue()));
			return questids;
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
			return TrackedMission.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TrackedMission.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TrackedMission.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TrackedMission.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TrackedMission.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TrackedMission.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TrackedMission.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TrackedMission.this.hashCode();
		}

		@Override
		public String toString() {
			return TrackedMission.this.toString();
		}

	}

	public static final class Data implements xbean.TrackedMission {
		private java.util.HashMap<Integer, xbean.Track> questids; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			questids = new java.util.HashMap<Integer, xbean.Track>();
		}

		Data(xbean.TrackedMission _o1_) {
			if (_o1_ instanceof TrackedMission) assign((TrackedMission)_o1_);
			else if (_o1_ instanceof TrackedMission.Data) assign((TrackedMission.Data)_o1_);
			else if (_o1_ instanceof TrackedMission.Const) assign(((TrackedMission.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TrackedMission _o_) {
			questids = new java.util.HashMap<Integer, xbean.Track>();
			for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
				questids.put(_e_.getKey(), new Track.Data(_e_.getValue()));
		}

		private void assign(TrackedMission.Data _o_) {
			questids = new java.util.HashMap<Integer, xbean.Track>();
			for (java.util.Map.Entry<Integer, xbean.Track> _e_ : _o_.questids.entrySet())
				questids.put(_e_.getKey(), new Track.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(questids.size());
			for (java.util.Map.Entry<Integer, xbean.Track> _e_ : questids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					questids = new java.util.HashMap<Integer, xbean.Track>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Track _v_ = xbean.Pod.newTrackData();
					_v_.unmarshal(_os_);
					questids.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.TrackedMission copy() {
			return new Data(this);
		}

		@Override
		public xbean.TrackedMission toData() {
			return new Data(this);
		}

		public xbean.TrackedMission toBean() {
			return new TrackedMission(this, null, null);
		}

		@Override
		public xbean.TrackedMission toDataIf() {
			return this;
		}

		public xbean.TrackedMission toBeanIf() {
			return new TrackedMission(this, null, null);
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
		public java.util.Map<Integer, xbean.Track> getQuestids() { // 
			return questids;
		}

		@Override
		public java.util.Map<Integer, xbean.Track> getQuestidsAsData() { // 
			return questids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TrackedMission.Data)) return false;
			TrackedMission.Data _o_ = (TrackedMission.Data) _o1_;
			if (!questids.equals(_o_.questids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += questids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(questids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
