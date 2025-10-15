
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanColumn extends mkdb.XBean implements xbean.HuoBanColumn {
	private java.util.ArrayList<Integer> fighthuobans; // 所有参战伙伴
	private java.util.HashMap<Integer, xbean.HuoBanInfo> huobans; // 
	private int viphuoban; // vip伙伴数量
	private int week; // 当前周数,计算伙伴状态的时候使用

	@Override
	public void _reset_unsafe_() {
		fighthuobans.clear();
		huobans.clear();
		viphuoban = 0;
		week = 0;
	}

	HuoBanColumn(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		fighthuobans = new java.util.ArrayList<Integer>();
		huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
	}

	public HuoBanColumn() {
		this(0, null, null);
	}

	public HuoBanColumn(HuoBanColumn _o_) {
		this(_o_, null, null);
	}

	HuoBanColumn(xbean.HuoBanColumn _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanColumn) assign((HuoBanColumn)_o1_);
		else if (_o1_ instanceof HuoBanColumn.Data) assign((HuoBanColumn.Data)_o1_);
		else if (_o1_ instanceof HuoBanColumn.Const) assign(((HuoBanColumn.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanColumn _o_) {
		_o_._xdb_verify_unsafe_();
		fighthuobans = new java.util.ArrayList<Integer>();
		fighthuobans.addAll(_o_.fighthuobans);
		huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanInfo(_e_.getValue(), this, "huobans"));
		viphuoban = _o_.viphuoban;
		week = _o_.week;
	}

	private void assign(HuoBanColumn.Data _o_) {
		fighthuobans = new java.util.ArrayList<Integer>();
		fighthuobans.addAll(_o_.fighthuobans);
		huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanInfo(_e_.getValue(), this, "huobans"));
		viphuoban = _o_.viphuoban;
		week = _o_.week;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(fighthuobans.size());
		for (Integer _v_ : fighthuobans) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(huobans.size());
		for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : huobans.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(viphuoban);
		_os_.marshal(week);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			fighthuobans.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.HuoBanInfo _v_ = new HuoBanInfo(0, this, "huobans");
				_v_.unmarshal(_os_);
				huobans.put(_k_, _v_);
			}
		}
		viphuoban = _os_.unmarshal_int();
		week = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.HuoBanColumn copy() {
		_xdb_verify_unsafe_();
		return new HuoBanColumn(this);
	}

	@Override
	public xbean.HuoBanColumn toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanColumn toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanColumn(this); // same as copy()
	}

	@Override
	public xbean.HuoBanColumn toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanColumn toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getFighthuobans() { // 所有参战伙伴
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "fighthuobans"), fighthuobans);
	}

	public java.util.List<Integer> getFighthuobansAsData() { // 所有参战伙伴
		_xdb_verify_unsafe_();
		java.util.List<Integer> fighthuobans;
		HuoBanColumn _o_ = this;
		fighthuobans = new java.util.ArrayList<Integer>();
		fighthuobans.addAll(_o_.fighthuobans);
		return fighthuobans;
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanInfo> getHuobans() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "huobans"), huobans);
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanInfo> getHuobansAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.HuoBanInfo> huobans;
		HuoBanColumn _o_ = this;
		huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanInfo.Data(_e_.getValue()));
		return huobans;
	}

	@Override
	public int getViphuoban() { // vip伙伴数量
		_xdb_verify_unsafe_();
		return viphuoban;
	}

	@Override
	public int getWeek() { // 当前周数,计算伙伴状态的时候使用
		_xdb_verify_unsafe_();
		return week;
	}

	@Override
	public void setViphuoban(int _v_) { // vip伙伴数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "viphuoban") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, viphuoban) {
					public void rollback() { viphuoban = _xdb_saved; }
				};}});
		viphuoban = _v_;
	}

	@Override
	public void setWeek(int _v_) { // 当前周数,计算伙伴状态的时候使用
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "week") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, week) {
					public void rollback() { week = _xdb_saved; }
				};}});
		week = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanColumn _o_ = null;
		if ( _o1_ instanceof HuoBanColumn ) _o_ = (HuoBanColumn)_o1_;
		else if ( _o1_ instanceof HuoBanColumn.Const ) _o_ = ((HuoBanColumn.Const)_o1_).nThis();
		else return false;
		if (!fighthuobans.equals(_o_.fighthuobans)) return false;
		if (!huobans.equals(_o_.huobans)) return false;
		if (viphuoban != _o_.viphuoban) return false;
		if (week != _o_.week) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += fighthuobans.hashCode();
		_h_ += huobans.hashCode();
		_h_ += viphuoban;
		_h_ += week;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighthuobans);
		_sb_.append(",");
		_sb_.append(huobans);
		_sb_.append(",");
		_sb_.append(viphuoban);
		_sb_.append(",");
		_sb_.append(week);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighthuobans"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("huobans"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("viphuoban"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("week"));
		return lb;
	}

	private class Const implements xbean.HuoBanColumn {
		HuoBanColumn nThis() {
			return HuoBanColumn.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanColumn copy() {
			return HuoBanColumn.this.copy();
		}

		@Override
		public xbean.HuoBanColumn toData() {
			return HuoBanColumn.this.toData();
		}

		public xbean.HuoBanColumn toBean() {
			return HuoBanColumn.this.toBean();
		}

		@Override
		public xbean.HuoBanColumn toDataIf() {
			return HuoBanColumn.this.toDataIf();
		}

		public xbean.HuoBanColumn toBeanIf() {
			return HuoBanColumn.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getFighthuobans() { // 所有参战伙伴
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(fighthuobans);
		}

		public java.util.List<Integer> getFighthuobansAsData() { // 所有参战伙伴
			_xdb_verify_unsafe_();
			java.util.List<Integer> fighthuobans;
			HuoBanColumn _o_ = HuoBanColumn.this;
		fighthuobans = new java.util.ArrayList<Integer>();
		fighthuobans.addAll(_o_.fighthuobans);
			return fighthuobans;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanInfo> getHuobans() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(huobans);
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanInfo> getHuobansAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.HuoBanInfo> huobans;
			HuoBanColumn _o_ = HuoBanColumn.this;
			huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanInfo.Data(_e_.getValue()));
			return huobans;
		}

		@Override
		public int getViphuoban() { // vip伙伴数量
			_xdb_verify_unsafe_();
			return viphuoban;
		}

		@Override
		public int getWeek() { // 当前周数,计算伙伴状态的时候使用
			_xdb_verify_unsafe_();
			return week;
		}

		@Override
		public void setViphuoban(int _v_) { // vip伙伴数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeek(int _v_) { // 当前周数,计算伙伴状态的时候使用
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
			return HuoBanColumn.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanColumn.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanColumn.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanColumn.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanColumn.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanColumn.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanColumn.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanColumn.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanColumn.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanColumn {
		private java.util.ArrayList<Integer> fighthuobans; // 所有参战伙伴
		private java.util.HashMap<Integer, xbean.HuoBanInfo> huobans; // 
		private int viphuoban; // vip伙伴数量
		private int week; // 当前周数,计算伙伴状态的时候使用

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			fighthuobans = new java.util.ArrayList<Integer>();
			huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
		}

		Data(xbean.HuoBanColumn _o1_) {
			if (_o1_ instanceof HuoBanColumn) assign((HuoBanColumn)_o1_);
			else if (_o1_ instanceof HuoBanColumn.Data) assign((HuoBanColumn.Data)_o1_);
			else if (_o1_ instanceof HuoBanColumn.Const) assign(((HuoBanColumn.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanColumn _o_) {
			fighthuobans = new java.util.ArrayList<Integer>();
			fighthuobans.addAll(_o_.fighthuobans);
			huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanInfo.Data(_e_.getValue()));
			viphuoban = _o_.viphuoban;
			week = _o_.week;
		}

		private void assign(HuoBanColumn.Data _o_) {
			fighthuobans = new java.util.ArrayList<Integer>();
			fighthuobans.addAll(_o_.fighthuobans);
			huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanInfo.Data(_e_.getValue()));
			viphuoban = _o_.viphuoban;
			week = _o_.week;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(fighthuobans.size());
			for (Integer _v_ : fighthuobans) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(huobans.size());
			for (java.util.Map.Entry<Integer, xbean.HuoBanInfo> _e_ : huobans.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(viphuoban);
			_os_.marshal(week);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				fighthuobans.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					huobans = new java.util.HashMap<Integer, xbean.HuoBanInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.HuoBanInfo _v_ = xbean.Pod.newHuoBanInfoData();
					_v_.unmarshal(_os_);
					huobans.put(_k_, _v_);
				}
			}
			viphuoban = _os_.unmarshal_int();
			week = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.HuoBanColumn copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanColumn toData() {
			return new Data(this);
		}

		public xbean.HuoBanColumn toBean() {
			return new HuoBanColumn(this, null, null);
		}

		@Override
		public xbean.HuoBanColumn toDataIf() {
			return this;
		}

		public xbean.HuoBanColumn toBeanIf() {
			return new HuoBanColumn(this, null, null);
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
		public java.util.List<Integer> getFighthuobans() { // 所有参战伙伴
			return fighthuobans;
		}

		@Override
		public java.util.List<Integer> getFighthuobansAsData() { // 所有参战伙伴
			return fighthuobans;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanInfo> getHuobans() { // 
			return huobans;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanInfo> getHuobansAsData() { // 
			return huobans;
		}

		@Override
		public int getViphuoban() { // vip伙伴数量
			return viphuoban;
		}

		@Override
		public int getWeek() { // 当前周数,计算伙伴状态的时候使用
			return week;
		}

		@Override
		public void setViphuoban(int _v_) { // vip伙伴数量
			viphuoban = _v_;
		}

		@Override
		public void setWeek(int _v_) { // 当前周数,计算伙伴状态的时候使用
			week = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanColumn.Data)) return false;
			HuoBanColumn.Data _o_ = (HuoBanColumn.Data) _o1_;
			if (!fighthuobans.equals(_o_.fighthuobans)) return false;
			if (!huobans.equals(_o_.huobans)) return false;
			if (viphuoban != _o_.viphuoban) return false;
			if (week != _o_.week) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += fighthuobans.hashCode();
			_h_ += huobans.hashCode();
			_h_ += viphuoban;
			_h_ += week;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(fighthuobans);
			_sb_.append(",");
			_sb_.append(huobans);
			_sb_.append(",");
			_sb_.append(viphuoban);
			_sb_.append(",");
			_sb_.append(week);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
