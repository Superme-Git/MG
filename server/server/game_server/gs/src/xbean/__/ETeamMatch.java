
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ETeamMatch extends mkdb.XBean implements xbean.ETeamMatch {
	private java.util.HashMap<Long, xbean.TeamMatch> roleid2matchdata; // 
	private java.util.HashMap<Long, xbean.TeamMatch> teamid2matchdata; // 
	private java.util.LinkedList<xbean.TeamMatch> teammatchdatalist; // 
	private java.util.LinkedList<xbean.TeamMatch> rolematchdatalist; // 

	@Override
	public void _reset_unsafe_() {
		roleid2matchdata.clear();
		teamid2matchdata.clear();
		teammatchdatalist.clear();
		rolematchdatalist.clear();
	}

	ETeamMatch(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
	}

	public ETeamMatch() {
		this(0, null, null);
	}

	public ETeamMatch(ETeamMatch _o_) {
		this(_o_, null, null);
	}

	ETeamMatch(xbean.ETeamMatch _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ETeamMatch) assign((ETeamMatch)_o1_);
		else if (_o1_ instanceof ETeamMatch.Data) assign((ETeamMatch.Data)_o1_);
		else if (_o1_ instanceof ETeamMatch.Const) assign(((ETeamMatch.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ETeamMatch _o_) {
		_o_._xdb_verify_unsafe_();
		roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
			roleid2matchdata.put(_e_.getKey(), new TeamMatch(_e_.getValue(), this, "roleid2matchdata"));
		teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
			teamid2matchdata.put(_e_.getKey(), new TeamMatch(_e_.getValue(), this, "teamid2matchdata"));
		teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
			teammatchdatalist.add(new TeamMatch(_v_, this, "teammatchdatalist"));
		rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
			rolematchdatalist.add(new TeamMatch(_v_, this, "rolematchdatalist"));
	}

	private void assign(ETeamMatch.Data _o_) {
		roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
			roleid2matchdata.put(_e_.getKey(), new TeamMatch(_e_.getValue(), this, "roleid2matchdata"));
		teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
			teamid2matchdata.put(_e_.getKey(), new TeamMatch(_e_.getValue(), this, "teamid2matchdata"));
		teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
			teammatchdatalist.add(new TeamMatch(_v_, this, "teammatchdatalist"));
		rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
			rolematchdatalist.add(new TeamMatch(_v_, this, "rolematchdatalist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleid2matchdata.size());
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : roleid2matchdata.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(teamid2matchdata.size());
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : teamid2matchdata.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(teammatchdatalist.size());
		for (xbean.TeamMatch _v_ : teammatchdatalist) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(rolematchdatalist.size());
		for (xbean.TeamMatch _v_ : rolematchdatalist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.TeamMatch _v_ = new TeamMatch(0, this, "roleid2matchdata");
				_v_.unmarshal(_os_);
				roleid2matchdata.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.TeamMatch _v_ = new TeamMatch(0, this, "teamid2matchdata");
				_v_.unmarshal(_os_);
				teamid2matchdata.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TeamMatch _v_ = new TeamMatch(0, this, "teammatchdatalist");
			_v_.unmarshal(_os_);
			teammatchdatalist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TeamMatch _v_ = new TeamMatch(0, this, "rolematchdatalist");
			_v_.unmarshal(_os_);
			rolematchdatalist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ETeamMatch copy() {
		_xdb_verify_unsafe_();
		return new ETeamMatch(this);
	}

	@Override
	public xbean.ETeamMatch toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ETeamMatch toBean() {
		_xdb_verify_unsafe_();
		return new ETeamMatch(this); // same as copy()
	}

	@Override
	public xbean.ETeamMatch toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ETeamMatch toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdata() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roleid2matchdata"), roleid2matchdata);
	}

	@Override
	public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdataAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.TeamMatch> roleid2matchdata;
		ETeamMatch _o_ = this;
		roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
			roleid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
		return roleid2matchdata;
	}

	@Override
	public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdata() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "teamid2matchdata"), teamid2matchdata);
	}

	@Override
	public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdataAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.TeamMatch> teamid2matchdata;
		ETeamMatch _o_ = this;
		teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
		for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
			teamid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
		return teamid2matchdata;
	}

	@Override
	public java.util.List<xbean.TeamMatch> getTeammatchdatalist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "teammatchdatalist"), teammatchdatalist);
	}

	public java.util.List<xbean.TeamMatch> getTeammatchdatalistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.TeamMatch> teammatchdatalist;
		ETeamMatch _o_ = this;
		teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
			teammatchdatalist.add(new TeamMatch.Data(_v_));
		return teammatchdatalist;
	}

	@Override
	public java.util.List<xbean.TeamMatch> getRolematchdatalist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "rolematchdatalist"), rolematchdatalist);
	}

	public java.util.List<xbean.TeamMatch> getRolematchdatalistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.TeamMatch> rolematchdatalist;
		ETeamMatch _o_ = this;
		rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
			rolematchdatalist.add(new TeamMatch.Data(_v_));
		return rolematchdatalist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ETeamMatch _o_ = null;
		if ( _o1_ instanceof ETeamMatch ) _o_ = (ETeamMatch)_o1_;
		else if ( _o1_ instanceof ETeamMatch.Const ) _o_ = ((ETeamMatch.Const)_o1_).nThis();
		else return false;
		if (!roleid2matchdata.equals(_o_.roleid2matchdata)) return false;
		if (!teamid2matchdata.equals(_o_.teamid2matchdata)) return false;
		if (!teammatchdatalist.equals(_o_.teammatchdatalist)) return false;
		if (!rolematchdatalist.equals(_o_.rolematchdatalist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid2matchdata.hashCode();
		_h_ += teamid2matchdata.hashCode();
		_h_ += teammatchdatalist.hashCode();
		_h_ += rolematchdatalist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid2matchdata);
		_sb_.append(",");
		_sb_.append(teamid2matchdata);
		_sb_.append(",");
		_sb_.append(teammatchdatalist);
		_sb_.append(",");
		_sb_.append(rolematchdatalist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleid2matchdata"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("teamid2matchdata"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teammatchdatalist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolematchdatalist"));
		return lb;
	}

	private class Const implements xbean.ETeamMatch {
		ETeamMatch nThis() {
			return ETeamMatch.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ETeamMatch copy() {
			return ETeamMatch.this.copy();
		}

		@Override
		public xbean.ETeamMatch toData() {
			return ETeamMatch.this.toData();
		}

		public xbean.ETeamMatch toBean() {
			return ETeamMatch.this.toBean();
		}

		@Override
		public xbean.ETeamMatch toDataIf() {
			return ETeamMatch.this.toDataIf();
		}

		public xbean.ETeamMatch toBeanIf() {
			return ETeamMatch.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdata() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roleid2matchdata);
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdataAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.TeamMatch> roleid2matchdata;
			ETeamMatch _o_ = ETeamMatch.this;
			roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
				roleid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			return roleid2matchdata;
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdata() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(teamid2matchdata);
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdataAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.TeamMatch> teamid2matchdata;
			ETeamMatch _o_ = ETeamMatch.this;
			teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
				teamid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			return teamid2matchdata;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getTeammatchdatalist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(teammatchdatalist);
		}

		public java.util.List<xbean.TeamMatch> getTeammatchdatalistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.TeamMatch> teammatchdatalist;
			ETeamMatch _o_ = ETeamMatch.this;
		teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
			teammatchdatalist.add(new TeamMatch.Data(_v_));
			return teammatchdatalist;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getRolematchdatalist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(rolematchdatalist);
		}

		public java.util.List<xbean.TeamMatch> getRolematchdatalistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.TeamMatch> rolematchdatalist;
			ETeamMatch _o_ = ETeamMatch.this;
		rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
			rolematchdatalist.add(new TeamMatch.Data(_v_));
			return rolematchdatalist;
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
			return ETeamMatch.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ETeamMatch.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ETeamMatch.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ETeamMatch.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ETeamMatch.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ETeamMatch.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ETeamMatch.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ETeamMatch.this.hashCode();
		}

		@Override
		public String toString() {
			return ETeamMatch.this.toString();
		}

	}

	public static final class Data implements xbean.ETeamMatch {
		private java.util.HashMap<Long, xbean.TeamMatch> roleid2matchdata; // 
		private java.util.HashMap<Long, xbean.TeamMatch> teamid2matchdata; // 
		private java.util.LinkedList<xbean.TeamMatch> teammatchdatalist; // 
		private java.util.LinkedList<xbean.TeamMatch> rolematchdatalist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
			rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
		}

		Data(xbean.ETeamMatch _o1_) {
			if (_o1_ instanceof ETeamMatch) assign((ETeamMatch)_o1_);
			else if (_o1_ instanceof ETeamMatch.Data) assign((ETeamMatch.Data)_o1_);
			else if (_o1_ instanceof ETeamMatch.Const) assign(((ETeamMatch.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ETeamMatch _o_) {
			roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
				roleid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
				teamid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
			for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
				teammatchdatalist.add(new TeamMatch.Data(_v_));
			rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
			for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
				rolematchdatalist.add(new TeamMatch.Data(_v_));
		}

		private void assign(ETeamMatch.Data _o_) {
			roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.roleid2matchdata.entrySet())
				roleid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>();
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : _o_.teamid2matchdata.entrySet())
				teamid2matchdata.put(_e_.getKey(), new TeamMatch.Data(_e_.getValue()));
			teammatchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
			for (xbean.TeamMatch _v_ : _o_.teammatchdatalist)
				teammatchdatalist.add(new TeamMatch.Data(_v_));
			rolematchdatalist = new java.util.LinkedList<xbean.TeamMatch>();
			for (xbean.TeamMatch _v_ : _o_.rolematchdatalist)
				rolematchdatalist.add(new TeamMatch.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleid2matchdata.size());
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : roleid2matchdata.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(teamid2matchdata.size());
			for (java.util.Map.Entry<Long, xbean.TeamMatch> _e_ : teamid2matchdata.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(teammatchdatalist.size());
			for (xbean.TeamMatch _v_ : teammatchdatalist) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(rolematchdatalist.size());
			for (xbean.TeamMatch _v_ : rolematchdatalist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					roleid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.TeamMatch _v_ = xbean.Pod.newTeamMatchData();
					_v_.unmarshal(_os_);
					roleid2matchdata.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					teamid2matchdata = new java.util.HashMap<Long, xbean.TeamMatch>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.TeamMatch _v_ = xbean.Pod.newTeamMatchData();
					_v_.unmarshal(_os_);
					teamid2matchdata.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TeamMatch _v_ = xbean.Pod.newTeamMatchData();
				_v_.unmarshal(_os_);
				teammatchdatalist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TeamMatch _v_ = xbean.Pod.newTeamMatchData();
				_v_.unmarshal(_os_);
				rolematchdatalist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ETeamMatch copy() {
			return new Data(this);
		}

		@Override
		public xbean.ETeamMatch toData() {
			return new Data(this);
		}

		public xbean.ETeamMatch toBean() {
			return new ETeamMatch(this, null, null);
		}

		@Override
		public xbean.ETeamMatch toDataIf() {
			return this;
		}

		public xbean.ETeamMatch toBeanIf() {
			return new ETeamMatch(this, null, null);
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
		public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdata() { // 
			return roleid2matchdata;
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdataAsData() { // 
			return roleid2matchdata;
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdata() { // 
			return teamid2matchdata;
		}

		@Override
		public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdataAsData() { // 
			return teamid2matchdata;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getTeammatchdatalist() { // 
			return teammatchdatalist;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getTeammatchdatalistAsData() { // 
			return teammatchdatalist;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getRolematchdatalist() { // 
			return rolematchdatalist;
		}

		@Override
		public java.util.List<xbean.TeamMatch> getRolematchdatalistAsData() { // 
			return rolematchdatalist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ETeamMatch.Data)) return false;
			ETeamMatch.Data _o_ = (ETeamMatch.Data) _o1_;
			if (!roleid2matchdata.equals(_o_.roleid2matchdata)) return false;
			if (!teamid2matchdata.equals(_o_.teamid2matchdata)) return false;
			if (!teammatchdatalist.equals(_o_.teammatchdatalist)) return false;
			if (!rolematchdatalist.equals(_o_.rolematchdatalist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid2matchdata.hashCode();
			_h_ += teamid2matchdata.hashCode();
			_h_ += teammatchdatalist.hashCode();
			_h_ += rolematchdatalist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid2matchdata);
			_sb_.append(",");
			_sb_.append(teamid2matchdata);
			_sb_.append(",");
			_sb_.append(teammatchdatalist);
			_sb_.append(",");
			_sb_.append(rolematchdatalist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
