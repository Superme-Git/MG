
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP5Race extends mkdb.XBean implements xbean.PvP5Race {
	private java.util.LinkedList<xbean.PvP5RaceRole> allrolescampa; // 赛场中A阵营的角色的积分排行
	private java.util.LinkedList<xbean.PvP5RaceRole> allrolescampb; // 赛场中B阵营的角色的积分排行
	private java.util.LinkedList<Long> allrolesidcampa; // 赛场中A阵营的角色ID
	private java.util.LinkedList<Long> allrolesidcampb; // 赛场中B阵营的角色ID
	private java.util.LinkedList<xbean.PvP5QueueRole> waitingqueue; // 等待序列
	private int isend; // 是否已经决出胜负

	@Override
	public void _reset_unsafe_() {
		allrolescampa.clear();
		allrolescampb.clear();
		allrolesidcampa.clear();
		allrolesidcampb.clear();
		waitingqueue.clear();
		isend = 0;
	}

	PvP5Race(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
		allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
		allrolesidcampa = new java.util.LinkedList<Long>();
		allrolesidcampb = new java.util.LinkedList<Long>();
		waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
	}

	public PvP5Race() {
		this(0, null, null);
	}

	public PvP5Race(PvP5Race _o_) {
		this(_o_, null, null);
	}

	PvP5Race(xbean.PvP5Race _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP5Race) assign((PvP5Race)_o1_);
		else if (_o1_ instanceof PvP5Race.Data) assign((PvP5Race.Data)_o1_);
		else if (_o1_ instanceof PvP5Race.Const) assign(((PvP5Race.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP5Race _o_) {
		_o_._xdb_verify_unsafe_();
		allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
			allrolescampa.add(new PvP5RaceRole(_v_, this, "allrolescampa"));
		allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
			allrolescampb.add(new PvP5RaceRole(_v_, this, "allrolescampb"));
		allrolesidcampa = new java.util.LinkedList<Long>();
		allrolesidcampa.addAll(_o_.allrolesidcampa);
		allrolesidcampb = new java.util.LinkedList<Long>();
		allrolesidcampb.addAll(_o_.allrolesidcampb);
		waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
		for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
			waitingqueue.add(new PvP5QueueRole(_v_, this, "waitingqueue"));
		isend = _o_.isend;
	}

	private void assign(PvP5Race.Data _o_) {
		allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
			allrolescampa.add(new PvP5RaceRole(_v_, this, "allrolescampa"));
		allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
			allrolescampb.add(new PvP5RaceRole(_v_, this, "allrolescampb"));
		allrolesidcampa = new java.util.LinkedList<Long>();
		allrolesidcampa.addAll(_o_.allrolesidcampa);
		allrolesidcampb = new java.util.LinkedList<Long>();
		allrolesidcampb.addAll(_o_.allrolesidcampb);
		waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
		for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
			waitingqueue.add(new PvP5QueueRole(_v_, this, "waitingqueue"));
		isend = _o_.isend;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(allrolescampa.size());
		for (xbean.PvP5RaceRole _v_ : allrolescampa) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(allrolescampb.size());
		for (xbean.PvP5RaceRole _v_ : allrolescampb) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(allrolesidcampa.size());
		for (Long _v_ : allrolesidcampa) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(allrolesidcampb.size());
		for (Long _v_ : allrolesidcampb) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(waitingqueue.size());
		for (xbean.PvP5QueueRole _v_ : waitingqueue) {
			_v_.marshal(_os_);
		}
		_os_.marshal(isend);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PvP5RaceRole _v_ = new PvP5RaceRole(0, this, "allrolescampa");
			_v_.unmarshal(_os_);
			allrolescampa.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PvP5RaceRole _v_ = new PvP5RaceRole(0, this, "allrolescampb");
			_v_.unmarshal(_os_);
			allrolescampb.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			allrolesidcampa.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			allrolesidcampb.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PvP5QueueRole _v_ = new PvP5QueueRole(0, this, "waitingqueue");
			_v_.unmarshal(_os_);
			waitingqueue.add(_v_);
		}
		isend = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PvP5Race copy() {
		_xdb_verify_unsafe_();
		return new PvP5Race(this);
	}

	@Override
	public xbean.PvP5Race toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP5Race toBean() {
		_xdb_verify_unsafe_();
		return new PvP5Race(this); // same as copy()
	}

	@Override
	public xbean.PvP5Race toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP5Race toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.PvP5RaceRole> getAllrolescampa() { // 赛场中A阵营的角色的积分排行
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "allrolescampa"), allrolescampa);
	}

	public java.util.List<xbean.PvP5RaceRole> getAllrolescampaAsData() { // 赛场中A阵营的角色的积分排行
		_xdb_verify_unsafe_();
		java.util.List<xbean.PvP5RaceRole> allrolescampa;
		PvP5Race _o_ = this;
		allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
			allrolescampa.add(new PvP5RaceRole.Data(_v_));
		return allrolescampa;
	}

	@Override
	public java.util.List<xbean.PvP5RaceRole> getAllrolescampb() { // 赛场中B阵营的角色的积分排行
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "allrolescampb"), allrolescampb);
	}

	public java.util.List<xbean.PvP5RaceRole> getAllrolescampbAsData() { // 赛场中B阵营的角色的积分排行
		_xdb_verify_unsafe_();
		java.util.List<xbean.PvP5RaceRole> allrolescampb;
		PvP5Race _o_ = this;
		allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
			allrolescampb.add(new PvP5RaceRole.Data(_v_));
		return allrolescampb;
	}

	@Override
	public java.util.List<Long> getAllrolesidcampa() { // 赛场中A阵营的角色ID
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "allrolesidcampa"), allrolesidcampa);
	}

	public java.util.List<Long> getAllrolesidcampaAsData() { // 赛场中A阵营的角色ID
		_xdb_verify_unsafe_();
		java.util.List<Long> allrolesidcampa;
		PvP5Race _o_ = this;
		allrolesidcampa = new java.util.LinkedList<Long>();
		allrolesidcampa.addAll(_o_.allrolesidcampa);
		return allrolesidcampa;
	}

	@Override
	public java.util.List<Long> getAllrolesidcampb() { // 赛场中B阵营的角色ID
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "allrolesidcampb"), allrolesidcampb);
	}

	public java.util.List<Long> getAllrolesidcampbAsData() { // 赛场中B阵营的角色ID
		_xdb_verify_unsafe_();
		java.util.List<Long> allrolesidcampb;
		PvP5Race _o_ = this;
		allrolesidcampb = new java.util.LinkedList<Long>();
		allrolesidcampb.addAll(_o_.allrolesidcampb);
		return allrolesidcampb;
	}

	@Override
	public java.util.List<xbean.PvP5QueueRole> getWaitingqueue() { // 等待序列
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "waitingqueue"), waitingqueue);
	}

	public java.util.List<xbean.PvP5QueueRole> getWaitingqueueAsData() { // 等待序列
		_xdb_verify_unsafe_();
		java.util.List<xbean.PvP5QueueRole> waitingqueue;
		PvP5Race _o_ = this;
		waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
		for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
			waitingqueue.add(new PvP5QueueRole.Data(_v_));
		return waitingqueue;
	}

	@Override
	public int getIsend() { // 是否已经决出胜负
		_xdb_verify_unsafe_();
		return isend;
	}

	@Override
	public void setIsend(int _v_) { // 是否已经决出胜负
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isend") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isend) {
					public void rollback() { isend = _xdb_saved; }
				};}});
		isend = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PvP5Race _o_ = null;
		if ( _o1_ instanceof PvP5Race ) _o_ = (PvP5Race)_o1_;
		else if ( _o1_ instanceof PvP5Race.Const ) _o_ = ((PvP5Race.Const)_o1_).nThis();
		else return false;
		if (!allrolescampa.equals(_o_.allrolescampa)) return false;
		if (!allrolescampb.equals(_o_.allrolescampb)) return false;
		if (!allrolesidcampa.equals(_o_.allrolesidcampa)) return false;
		if (!allrolesidcampb.equals(_o_.allrolesidcampb)) return false;
		if (!waitingqueue.equals(_o_.waitingqueue)) return false;
		if (isend != _o_.isend) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += allrolescampa.hashCode();
		_h_ += allrolescampb.hashCode();
		_h_ += allrolesidcampa.hashCode();
		_h_ += allrolesidcampb.hashCode();
		_h_ += waitingqueue.hashCode();
		_h_ += isend;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(allrolescampa);
		_sb_.append(",");
		_sb_.append(allrolescampb);
		_sb_.append(",");
		_sb_.append(allrolesidcampa);
		_sb_.append(",");
		_sb_.append(allrolesidcampb);
		_sb_.append(",");
		_sb_.append(waitingqueue);
		_sb_.append(",");
		_sb_.append(isend);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allrolescampa"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allrolescampb"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allrolesidcampa"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allrolesidcampb"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("waitingqueue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isend"));
		return lb;
	}

	private class Const implements xbean.PvP5Race {
		PvP5Race nThis() {
			return PvP5Race.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP5Race copy() {
			return PvP5Race.this.copy();
		}

		@Override
		public xbean.PvP5Race toData() {
			return PvP5Race.this.toData();
		}

		public xbean.PvP5Race toBean() {
			return PvP5Race.this.toBean();
		}

		@Override
		public xbean.PvP5Race toDataIf() {
			return PvP5Race.this.toDataIf();
		}

		public xbean.PvP5Race toBeanIf() {
			return PvP5Race.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampa() { // 赛场中A阵营的角色的积分排行
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(allrolescampa);
		}

		public java.util.List<xbean.PvP5RaceRole> getAllrolescampaAsData() { // 赛场中A阵营的角色的积分排行
			_xdb_verify_unsafe_();
			java.util.List<xbean.PvP5RaceRole> allrolescampa;
			PvP5Race _o_ = PvP5Race.this;
		allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
			allrolescampa.add(new PvP5RaceRole.Data(_v_));
			return allrolescampa;
		}

		@Override
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampb() { // 赛场中B阵营的角色的积分排行
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(allrolescampb);
		}

		public java.util.List<xbean.PvP5RaceRole> getAllrolescampbAsData() { // 赛场中B阵营的角色的积分排行
			_xdb_verify_unsafe_();
			java.util.List<xbean.PvP5RaceRole> allrolescampb;
			PvP5Race _o_ = PvP5Race.this;
		allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
		for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
			allrolescampb.add(new PvP5RaceRole.Data(_v_));
			return allrolescampb;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampa() { // 赛场中A阵营的角色ID
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(allrolesidcampa);
		}

		public java.util.List<Long> getAllrolesidcampaAsData() { // 赛场中A阵营的角色ID
			_xdb_verify_unsafe_();
			java.util.List<Long> allrolesidcampa;
			PvP5Race _o_ = PvP5Race.this;
		allrolesidcampa = new java.util.LinkedList<Long>();
		allrolesidcampa.addAll(_o_.allrolesidcampa);
			return allrolesidcampa;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampb() { // 赛场中B阵营的角色ID
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(allrolesidcampb);
		}

		public java.util.List<Long> getAllrolesidcampbAsData() { // 赛场中B阵营的角色ID
			_xdb_verify_unsafe_();
			java.util.List<Long> allrolesidcampb;
			PvP5Race _o_ = PvP5Race.this;
		allrolesidcampb = new java.util.LinkedList<Long>();
		allrolesidcampb.addAll(_o_.allrolesidcampb);
			return allrolesidcampb;
		}

		@Override
		public java.util.List<xbean.PvP5QueueRole> getWaitingqueue() { // 等待序列
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(waitingqueue);
		}

		public java.util.List<xbean.PvP5QueueRole> getWaitingqueueAsData() { // 等待序列
			_xdb_verify_unsafe_();
			java.util.List<xbean.PvP5QueueRole> waitingqueue;
			PvP5Race _o_ = PvP5Race.this;
		waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
		for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
			waitingqueue.add(new PvP5QueueRole.Data(_v_));
			return waitingqueue;
		}

		@Override
		public int getIsend() { // 是否已经决出胜负
			_xdb_verify_unsafe_();
			return isend;
		}

		@Override
		public void setIsend(int _v_) { // 是否已经决出胜负
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
			return PvP5Race.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP5Race.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP5Race.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP5Race.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP5Race.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP5Race.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP5Race.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP5Race.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP5Race.this.toString();
		}

	}

	public static final class Data implements xbean.PvP5Race {
		private java.util.LinkedList<xbean.PvP5RaceRole> allrolescampa; // 赛场中A阵营的角色的积分排行
		private java.util.LinkedList<xbean.PvP5RaceRole> allrolescampb; // 赛场中B阵营的角色的积分排行
		private java.util.LinkedList<Long> allrolesidcampa; // 赛场中A阵营的角色ID
		private java.util.LinkedList<Long> allrolesidcampb; // 赛场中B阵营的角色ID
		private java.util.LinkedList<xbean.PvP5QueueRole> waitingqueue; // 等待序列
		private int isend; // 是否已经决出胜负

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
			allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
			allrolesidcampa = new java.util.LinkedList<Long>();
			allrolesidcampb = new java.util.LinkedList<Long>();
			waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
		}

		Data(xbean.PvP5Race _o1_) {
			if (_o1_ instanceof PvP5Race) assign((PvP5Race)_o1_);
			else if (_o1_ instanceof PvP5Race.Data) assign((PvP5Race.Data)_o1_);
			else if (_o1_ instanceof PvP5Race.Const) assign(((PvP5Race.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP5Race _o_) {
			allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
			for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
				allrolescampa.add(new PvP5RaceRole.Data(_v_));
			allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
			for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
				allrolescampb.add(new PvP5RaceRole.Data(_v_));
			allrolesidcampa = new java.util.LinkedList<Long>();
			allrolesidcampa.addAll(_o_.allrolesidcampa);
			allrolesidcampb = new java.util.LinkedList<Long>();
			allrolesidcampb.addAll(_o_.allrolesidcampb);
			waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
			for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
				waitingqueue.add(new PvP5QueueRole.Data(_v_));
			isend = _o_.isend;
		}

		private void assign(PvP5Race.Data _o_) {
			allrolescampa = new java.util.LinkedList<xbean.PvP5RaceRole>();
			for (xbean.PvP5RaceRole _v_ : _o_.allrolescampa)
				allrolescampa.add(new PvP5RaceRole.Data(_v_));
			allrolescampb = new java.util.LinkedList<xbean.PvP5RaceRole>();
			for (xbean.PvP5RaceRole _v_ : _o_.allrolescampb)
				allrolescampb.add(new PvP5RaceRole.Data(_v_));
			allrolesidcampa = new java.util.LinkedList<Long>();
			allrolesidcampa.addAll(_o_.allrolesidcampa);
			allrolesidcampb = new java.util.LinkedList<Long>();
			allrolesidcampb.addAll(_o_.allrolesidcampb);
			waitingqueue = new java.util.LinkedList<xbean.PvP5QueueRole>();
			for (xbean.PvP5QueueRole _v_ : _o_.waitingqueue)
				waitingqueue.add(new PvP5QueueRole.Data(_v_));
			isend = _o_.isend;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(allrolescampa.size());
			for (xbean.PvP5RaceRole _v_ : allrolescampa) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(allrolescampb.size());
			for (xbean.PvP5RaceRole _v_ : allrolescampb) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(allrolesidcampa.size());
			for (Long _v_ : allrolesidcampa) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(allrolesidcampb.size());
			for (Long _v_ : allrolesidcampb) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(waitingqueue.size());
			for (xbean.PvP5QueueRole _v_ : waitingqueue) {
				_v_.marshal(_os_);
			}
			_os_.marshal(isend);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PvP5RaceRole _v_ = xbean.Pod.newPvP5RaceRoleData();
				_v_.unmarshal(_os_);
				allrolescampa.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PvP5RaceRole _v_ = xbean.Pod.newPvP5RaceRoleData();
				_v_.unmarshal(_os_);
				allrolescampb.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				allrolesidcampa.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				allrolesidcampb.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PvP5QueueRole _v_ = xbean.Pod.newPvP5QueueRoleData();
				_v_.unmarshal(_os_);
				waitingqueue.add(_v_);
			}
			isend = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PvP5Race copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP5Race toData() {
			return new Data(this);
		}

		public xbean.PvP5Race toBean() {
			return new PvP5Race(this, null, null);
		}

		@Override
		public xbean.PvP5Race toDataIf() {
			return this;
		}

		public xbean.PvP5Race toBeanIf() {
			return new PvP5Race(this, null, null);
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
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampa() { // 赛场中A阵营的角色的积分排行
			return allrolescampa;
		}

		@Override
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampaAsData() { // 赛场中A阵营的角色的积分排行
			return allrolescampa;
		}

		@Override
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampb() { // 赛场中B阵营的角色的积分排行
			return allrolescampb;
		}

		@Override
		public java.util.List<xbean.PvP5RaceRole> getAllrolescampbAsData() { // 赛场中B阵营的角色的积分排行
			return allrolescampb;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampa() { // 赛场中A阵营的角色ID
			return allrolesidcampa;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampaAsData() { // 赛场中A阵营的角色ID
			return allrolesidcampa;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampb() { // 赛场中B阵营的角色ID
			return allrolesidcampb;
		}

		@Override
		public java.util.List<Long> getAllrolesidcampbAsData() { // 赛场中B阵营的角色ID
			return allrolesidcampb;
		}

		@Override
		public java.util.List<xbean.PvP5QueueRole> getWaitingqueue() { // 等待序列
			return waitingqueue;
		}

		@Override
		public java.util.List<xbean.PvP5QueueRole> getWaitingqueueAsData() { // 等待序列
			return waitingqueue;
		}

		@Override
		public int getIsend() { // 是否已经决出胜负
			return isend;
		}

		@Override
		public void setIsend(int _v_) { // 是否已经决出胜负
			isend = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PvP5Race.Data)) return false;
			PvP5Race.Data _o_ = (PvP5Race.Data) _o1_;
			if (!allrolescampa.equals(_o_.allrolescampa)) return false;
			if (!allrolescampb.equals(_o_.allrolescampb)) return false;
			if (!allrolesidcampa.equals(_o_.allrolesidcampa)) return false;
			if (!allrolesidcampb.equals(_o_.allrolesidcampb)) return false;
			if (!waitingqueue.equals(_o_.waitingqueue)) return false;
			if (isend != _o_.isend) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += allrolescampa.hashCode();
			_h_ += allrolescampb.hashCode();
			_h_ += allrolesidcampa.hashCode();
			_h_ += allrolesidcampb.hashCode();
			_h_ += waitingqueue.hashCode();
			_h_ += isend;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(allrolescampa);
			_sb_.append(",");
			_sb_.append(allrolescampb);
			_sb_.append(",");
			_sb_.append(allrolesidcampa);
			_sb_.append(",");
			_sb_.append(allrolesidcampb);
			_sb_.append(",");
			_sb_.append(waitingqueue);
			_sb_.append(",");
			_sb_.append(isend);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
