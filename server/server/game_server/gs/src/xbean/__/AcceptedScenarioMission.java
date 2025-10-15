
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AcceptedScenarioMission extends mkdb.XBean implements xbean.AcceptedScenarioMission {
	private java.util.HashMap<Integer, xbean.Mission> accepted; // 支线任务

	@Override
	public void _reset_unsafe_() {
		accepted.clear();
	}

	AcceptedScenarioMission(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		accepted = new java.util.HashMap<Integer, xbean.Mission>();
	}

	public AcceptedScenarioMission() {
		this(0, null, null);
	}

	public AcceptedScenarioMission(AcceptedScenarioMission _o_) {
		this(_o_, null, null);
	}

	AcceptedScenarioMission(xbean.AcceptedScenarioMission _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AcceptedScenarioMission) assign((AcceptedScenarioMission)_o1_);
		else if (_o1_ instanceof AcceptedScenarioMission.Data) assign((AcceptedScenarioMission.Data)_o1_);
		else if (_o1_ instanceof AcceptedScenarioMission.Const) assign(((AcceptedScenarioMission.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AcceptedScenarioMission _o_) {
		_o_._xdb_verify_unsafe_();
		accepted = new java.util.HashMap<Integer, xbean.Mission>();
		for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
			accepted.put(_e_.getKey(), new Mission(_e_.getValue(), this, "accepted"));
	}

	private void assign(AcceptedScenarioMission.Data _o_) {
		accepted = new java.util.HashMap<Integer, xbean.Mission>();
		for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
			accepted.put(_e_.getKey(), new Mission(_e_.getValue(), this, "accepted"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(accepted.size());
		for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : accepted.entrySet())
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
				accepted = new java.util.HashMap<Integer, xbean.Mission>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Mission _v_ = new Mission(0, this, "accepted");
				_v_.unmarshal(_os_);
				accepted.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.AcceptedScenarioMission copy() {
		_xdb_verify_unsafe_();
		return new AcceptedScenarioMission(this);
	}

	@Override
	public xbean.AcceptedScenarioMission toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AcceptedScenarioMission toBean() {
		_xdb_verify_unsafe_();
		return new AcceptedScenarioMission(this); // same as copy()
	}

	@Override
	public xbean.AcceptedScenarioMission toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AcceptedScenarioMission toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.Mission> getAccepted() { // 支线任务
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "accepted"), accepted);
	}

	@Override
	public java.util.Map<Integer, xbean.Mission> getAcceptedAsData() { // 支线任务
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Mission> accepted;
		AcceptedScenarioMission _o_ = this;
		accepted = new java.util.HashMap<Integer, xbean.Mission>();
		for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
			accepted.put(_e_.getKey(), new Mission.Data(_e_.getValue()));
		return accepted;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AcceptedScenarioMission _o_ = null;
		if ( _o1_ instanceof AcceptedScenarioMission ) _o_ = (AcceptedScenarioMission)_o1_;
		else if ( _o1_ instanceof AcceptedScenarioMission.Const ) _o_ = ((AcceptedScenarioMission.Const)_o1_).nThis();
		else return false;
		if (!accepted.equals(_o_.accepted)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += accepted.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(accepted);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("accepted"));
		return lb;
	}

	private class Const implements xbean.AcceptedScenarioMission {
		AcceptedScenarioMission nThis() {
			return AcceptedScenarioMission.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AcceptedScenarioMission copy() {
			return AcceptedScenarioMission.this.copy();
		}

		@Override
		public xbean.AcceptedScenarioMission toData() {
			return AcceptedScenarioMission.this.toData();
		}

		public xbean.AcceptedScenarioMission toBean() {
			return AcceptedScenarioMission.this.toBean();
		}

		@Override
		public xbean.AcceptedScenarioMission toDataIf() {
			return AcceptedScenarioMission.this.toDataIf();
		}

		public xbean.AcceptedScenarioMission toBeanIf() {
			return AcceptedScenarioMission.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.Mission> getAccepted() { // 支线任务
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(accepted);
		}

		@Override
		public java.util.Map<Integer, xbean.Mission> getAcceptedAsData() { // 支线任务
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Mission> accepted;
			AcceptedScenarioMission _o_ = AcceptedScenarioMission.this;
			accepted = new java.util.HashMap<Integer, xbean.Mission>();
			for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
				accepted.put(_e_.getKey(), new Mission.Data(_e_.getValue()));
			return accepted;
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
			return AcceptedScenarioMission.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AcceptedScenarioMission.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AcceptedScenarioMission.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AcceptedScenarioMission.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AcceptedScenarioMission.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AcceptedScenarioMission.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AcceptedScenarioMission.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AcceptedScenarioMission.this.hashCode();
		}

		@Override
		public String toString() {
			return AcceptedScenarioMission.this.toString();
		}

	}

	public static final class Data implements xbean.AcceptedScenarioMission {
		private java.util.HashMap<Integer, xbean.Mission> accepted; // 支线任务

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			accepted = new java.util.HashMap<Integer, xbean.Mission>();
		}

		Data(xbean.AcceptedScenarioMission _o1_) {
			if (_o1_ instanceof AcceptedScenarioMission) assign((AcceptedScenarioMission)_o1_);
			else if (_o1_ instanceof AcceptedScenarioMission.Data) assign((AcceptedScenarioMission.Data)_o1_);
			else if (_o1_ instanceof AcceptedScenarioMission.Const) assign(((AcceptedScenarioMission.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AcceptedScenarioMission _o_) {
			accepted = new java.util.HashMap<Integer, xbean.Mission>();
			for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
				accepted.put(_e_.getKey(), new Mission.Data(_e_.getValue()));
		}

		private void assign(AcceptedScenarioMission.Data _o_) {
			accepted = new java.util.HashMap<Integer, xbean.Mission>();
			for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : _o_.accepted.entrySet())
				accepted.put(_e_.getKey(), new Mission.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(accepted.size());
			for (java.util.Map.Entry<Integer, xbean.Mission> _e_ : accepted.entrySet())
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
					accepted = new java.util.HashMap<Integer, xbean.Mission>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Mission _v_ = xbean.Pod.newMissionData();
					_v_.unmarshal(_os_);
					accepted.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.AcceptedScenarioMission copy() {
			return new Data(this);
		}

		@Override
		public xbean.AcceptedScenarioMission toData() {
			return new Data(this);
		}

		public xbean.AcceptedScenarioMission toBean() {
			return new AcceptedScenarioMission(this, null, null);
		}

		@Override
		public xbean.AcceptedScenarioMission toDataIf() {
			return this;
		}

		public xbean.AcceptedScenarioMission toBeanIf() {
			return new AcceptedScenarioMission(this, null, null);
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
		public java.util.Map<Integer, xbean.Mission> getAccepted() { // 支线任务
			return accepted;
		}

		@Override
		public java.util.Map<Integer, xbean.Mission> getAcceptedAsData() { // 支线任务
			return accepted;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AcceptedScenarioMission.Data)) return false;
			AcceptedScenarioMission.Data _o_ = (AcceptedScenarioMission.Data) _o1_;
			if (!accepted.equals(_o_.accepted)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += accepted.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(accepted);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
