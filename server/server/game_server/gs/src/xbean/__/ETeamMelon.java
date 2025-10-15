
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ETeamMelon extends mkdb.XBean implements xbean.ETeamMelon {
	private java.util.HashMap<Long, xbean.TeamMelon> melonid2melons; // 队伍分赃链表 by changhao
	private int melontype; // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
	private int dataid; // 结合melontype,表示子类型;精英副本里表示副本id
	private long dataid2; // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
	private java.util.LinkedList<Long> melonerlist; // 有资格分赃的原始人员 by changhao
	private java.util.LinkedList<Long> watchmelonerlist; // 没资格观看人员 by changhao

	@Override
	public void _reset_unsafe_() {
		melonid2melons.clear();
		melontype = 0;
		dataid = 0;
		dataid2 = 0L;
		melonerlist.clear();
		watchmelonerlist.clear();
	}

	ETeamMelon(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
		melontype = 0;
		melonerlist = new java.util.LinkedList<Long>();
		watchmelonerlist = new java.util.LinkedList<Long>();
	}

	public ETeamMelon() {
		this(0, null, null);
	}

	public ETeamMelon(ETeamMelon _o_) {
		this(_o_, null, null);
	}

	ETeamMelon(xbean.ETeamMelon _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ETeamMelon) assign((ETeamMelon)_o1_);
		else if (_o1_ instanceof ETeamMelon.Data) assign((ETeamMelon.Data)_o1_);
		else if (_o1_ instanceof ETeamMelon.Const) assign(((ETeamMelon.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ETeamMelon _o_) {
		_o_._xdb_verify_unsafe_();
		melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
		for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
			melonid2melons.put(_e_.getKey(), new TeamMelon(_e_.getValue(), this, "melonid2melons"));
		melontype = _o_.melontype;
		dataid = _o_.dataid;
		dataid2 = _o_.dataid2;
		melonerlist = new java.util.LinkedList<Long>();
		melonerlist.addAll(_o_.melonerlist);
		watchmelonerlist = new java.util.LinkedList<Long>();
		watchmelonerlist.addAll(_o_.watchmelonerlist);
	}

	private void assign(ETeamMelon.Data _o_) {
		melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
		for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
			melonid2melons.put(_e_.getKey(), new TeamMelon(_e_.getValue(), this, "melonid2melons"));
		melontype = _o_.melontype;
		dataid = _o_.dataid;
		dataid2 = _o_.dataid2;
		melonerlist = new java.util.LinkedList<Long>();
		melonerlist.addAll(_o_.melonerlist);
		watchmelonerlist = new java.util.LinkedList<Long>();
		watchmelonerlist.addAll(_o_.watchmelonerlist);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(melonid2melons.size());
		for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : melonid2melons.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(melontype);
		_os_.marshal(dataid);
		_os_.marshal(dataid2);
		_os_.compact_uint32(melonerlist.size());
		for (Long _v_ : melonerlist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(watchmelonerlist.size());
		for (Long _v_ : watchmelonerlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.TeamMelon _v_ = new TeamMelon(0, this, "melonid2melons");
				_v_.unmarshal(_os_);
				melonid2melons.put(_k_, _v_);
			}
		}
		melontype = _os_.unmarshal_int();
		dataid = _os_.unmarshal_int();
		dataid2 = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			melonerlist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			watchmelonerlist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ETeamMelon copy() {
		_xdb_verify_unsafe_();
		return new ETeamMelon(this);
	}

	@Override
	public xbean.ETeamMelon toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ETeamMelon toBean() {
		_xdb_verify_unsafe_();
		return new ETeamMelon(this); // same as copy()
	}

	@Override
	public xbean.ETeamMelon toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ETeamMelon toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.TeamMelon> getMelonid2melons() { // 队伍分赃链表 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "melonid2melons"), melonid2melons);
	}

	@Override
	public java.util.Map<Long, xbean.TeamMelon> getMelonid2melonsAsData() { // 队伍分赃链表 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.TeamMelon> melonid2melons;
		ETeamMelon _o_ = this;
		melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
		for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
			melonid2melons.put(_e_.getKey(), new TeamMelon.Data(_e_.getValue()));
		return melonid2melons;
	}

	@Override
	public int getMelontype() { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
		_xdb_verify_unsafe_();
		return melontype;
	}

	@Override
	public int getDataid() { // 结合melontype,表示子类型;精英副本里表示副本id
		_xdb_verify_unsafe_();
		return dataid;
	}

	@Override
	public long getDataid2() { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
		_xdb_verify_unsafe_();
		return dataid2;
	}

	@Override
	public java.util.List<Long> getMelonerlist() { // 有资格分赃的原始人员 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "melonerlist"), melonerlist);
	}

	public java.util.List<Long> getMelonerlistAsData() { // 有资格分赃的原始人员 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> melonerlist;
		ETeamMelon _o_ = this;
		melonerlist = new java.util.LinkedList<Long>();
		melonerlist.addAll(_o_.melonerlist);
		return melonerlist;
	}

	@Override
	public java.util.List<Long> getWatchmelonerlist() { // 没资格观看人员 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "watchmelonerlist"), watchmelonerlist);
	}

	public java.util.List<Long> getWatchmelonerlistAsData() { // 没资格观看人员 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> watchmelonerlist;
		ETeamMelon _o_ = this;
		watchmelonerlist = new java.util.LinkedList<Long>();
		watchmelonerlist.addAll(_o_.watchmelonerlist);
		return watchmelonerlist;
	}

	@Override
	public void setMelontype(int _v_) { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "melontype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, melontype) {
					public void rollback() { melontype = _xdb_saved; }
				};}});
		melontype = _v_;
	}

	@Override
	public void setDataid(int _v_) { // 结合melontype,表示子类型;精英副本里表示副本id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dataid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dataid) {
					public void rollback() { dataid = _xdb_saved; }
				};}});
		dataid = _v_;
	}

	@Override
	public void setDataid2(long _v_) { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dataid2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dataid2) {
					public void rollback() { dataid2 = _xdb_saved; }
				};}});
		dataid2 = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ETeamMelon _o_ = null;
		if ( _o1_ instanceof ETeamMelon ) _o_ = (ETeamMelon)_o1_;
		else if ( _o1_ instanceof ETeamMelon.Const ) _o_ = ((ETeamMelon.Const)_o1_).nThis();
		else return false;
		if (!melonid2melons.equals(_o_.melonid2melons)) return false;
		if (melontype != _o_.melontype) return false;
		if (dataid != _o_.dataid) return false;
		if (dataid2 != _o_.dataid2) return false;
		if (!melonerlist.equals(_o_.melonerlist)) return false;
		if (!watchmelonerlist.equals(_o_.watchmelonerlist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += melonid2melons.hashCode();
		_h_ += melontype;
		_h_ += dataid;
		_h_ += dataid2;
		_h_ += melonerlist.hashCode();
		_h_ += watchmelonerlist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid2melons);
		_sb_.append(",");
		_sb_.append(melontype);
		_sb_.append(",");
		_sb_.append(dataid);
		_sb_.append(",");
		_sb_.append(dataid2);
		_sb_.append(",");
		_sb_.append(melonerlist);
		_sb_.append(",");
		_sb_.append(watchmelonerlist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("melonid2melons"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("melontype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dataid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dataid2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("melonerlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("watchmelonerlist"));
		return lb;
	}

	private class Const implements xbean.ETeamMelon {
		ETeamMelon nThis() {
			return ETeamMelon.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ETeamMelon copy() {
			return ETeamMelon.this.copy();
		}

		@Override
		public xbean.ETeamMelon toData() {
			return ETeamMelon.this.toData();
		}

		public xbean.ETeamMelon toBean() {
			return ETeamMelon.this.toBean();
		}

		@Override
		public xbean.ETeamMelon toDataIf() {
			return ETeamMelon.this.toDataIf();
		}

		public xbean.ETeamMelon toBeanIf() {
			return ETeamMelon.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.TeamMelon> getMelonid2melons() { // 队伍分赃链表 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(melonid2melons);
		}

		@Override
		public java.util.Map<Long, xbean.TeamMelon> getMelonid2melonsAsData() { // 队伍分赃链表 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.TeamMelon> melonid2melons;
			ETeamMelon _o_ = ETeamMelon.this;
			melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
			for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
				melonid2melons.put(_e_.getKey(), new TeamMelon.Data(_e_.getValue()));
			return melonid2melons;
		}

		@Override
		public int getMelontype() { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
			_xdb_verify_unsafe_();
			return melontype;
		}

		@Override
		public int getDataid() { // 结合melontype,表示子类型;精英副本里表示副本id
			_xdb_verify_unsafe_();
			return dataid;
		}

		@Override
		public long getDataid2() { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
			_xdb_verify_unsafe_();
			return dataid2;
		}

		@Override
		public java.util.List<Long> getMelonerlist() { // 有资格分赃的原始人员 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(melonerlist);
		}

		public java.util.List<Long> getMelonerlistAsData() { // 有资格分赃的原始人员 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> melonerlist;
			ETeamMelon _o_ = ETeamMelon.this;
		melonerlist = new java.util.LinkedList<Long>();
		melonerlist.addAll(_o_.melonerlist);
			return melonerlist;
		}

		@Override
		public java.util.List<Long> getWatchmelonerlist() { // 没资格观看人员 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(watchmelonerlist);
		}

		public java.util.List<Long> getWatchmelonerlistAsData() { // 没资格观看人员 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> watchmelonerlist;
			ETeamMelon _o_ = ETeamMelon.this;
		watchmelonerlist = new java.util.LinkedList<Long>();
		watchmelonerlist.addAll(_o_.watchmelonerlist);
			return watchmelonerlist;
		}

		@Override
		public void setMelontype(int _v_) { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDataid(int _v_) { // 结合melontype,表示子类型;精英副本里表示副本id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDataid2(long _v_) { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
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
			return ETeamMelon.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ETeamMelon.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ETeamMelon.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ETeamMelon.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ETeamMelon.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ETeamMelon.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ETeamMelon.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ETeamMelon.this.hashCode();
		}

		@Override
		public String toString() {
			return ETeamMelon.this.toString();
		}

	}

	public static final class Data implements xbean.ETeamMelon {
		private java.util.HashMap<Long, xbean.TeamMelon> melonid2melons; // 队伍分赃链表 by changhao
		private int melontype; // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
		private int dataid; // 结合melontype,表示子类型;精英副本里表示副本id
		private long dataid2; // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
		private java.util.LinkedList<Long> melonerlist; // 有资格分赃的原始人员 by changhao
		private java.util.LinkedList<Long> watchmelonerlist; // 没资格观看人员 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
			melontype = 0;
			melonerlist = new java.util.LinkedList<Long>();
			watchmelonerlist = new java.util.LinkedList<Long>();
		}

		Data(xbean.ETeamMelon _o1_) {
			if (_o1_ instanceof ETeamMelon) assign((ETeamMelon)_o1_);
			else if (_o1_ instanceof ETeamMelon.Data) assign((ETeamMelon.Data)_o1_);
			else if (_o1_ instanceof ETeamMelon.Const) assign(((ETeamMelon.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ETeamMelon _o_) {
			melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
			for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
				melonid2melons.put(_e_.getKey(), new TeamMelon.Data(_e_.getValue()));
			melontype = _o_.melontype;
			dataid = _o_.dataid;
			dataid2 = _o_.dataid2;
			melonerlist = new java.util.LinkedList<Long>();
			melonerlist.addAll(_o_.melonerlist);
			watchmelonerlist = new java.util.LinkedList<Long>();
			watchmelonerlist.addAll(_o_.watchmelonerlist);
		}

		private void assign(ETeamMelon.Data _o_) {
			melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>();
			for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : _o_.melonid2melons.entrySet())
				melonid2melons.put(_e_.getKey(), new TeamMelon.Data(_e_.getValue()));
			melontype = _o_.melontype;
			dataid = _o_.dataid;
			dataid2 = _o_.dataid2;
			melonerlist = new java.util.LinkedList<Long>();
			melonerlist.addAll(_o_.melonerlist);
			watchmelonerlist = new java.util.LinkedList<Long>();
			watchmelonerlist.addAll(_o_.watchmelonerlist);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(melonid2melons.size());
			for (java.util.Map.Entry<Long, xbean.TeamMelon> _e_ : melonid2melons.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(melontype);
			_os_.marshal(dataid);
			_os_.marshal(dataid2);
			_os_.compact_uint32(melonerlist.size());
			for (Long _v_ : melonerlist) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(watchmelonerlist.size());
			for (Long _v_ : watchmelonerlist) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					melonid2melons = new java.util.HashMap<Long, xbean.TeamMelon>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.TeamMelon _v_ = xbean.Pod.newTeamMelonData();
					_v_.unmarshal(_os_);
					melonid2melons.put(_k_, _v_);
				}
			}
			melontype = _os_.unmarshal_int();
			dataid = _os_.unmarshal_int();
			dataid2 = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				melonerlist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				watchmelonerlist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ETeamMelon copy() {
			return new Data(this);
		}

		@Override
		public xbean.ETeamMelon toData() {
			return new Data(this);
		}

		public xbean.ETeamMelon toBean() {
			return new ETeamMelon(this, null, null);
		}

		@Override
		public xbean.ETeamMelon toDataIf() {
			return this;
		}

		public xbean.ETeamMelon toBeanIf() {
			return new ETeamMelon(this, null, null);
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
		public java.util.Map<Long, xbean.TeamMelon> getMelonid2melons() { // 队伍分赃链表 by changhao
			return melonid2melons;
		}

		@Override
		public java.util.Map<Long, xbean.TeamMelon> getMelonid2melonsAsData() { // 队伍分赃链表 by changhao
			return melonid2melons;
		}

		@Override
		public int getMelontype() { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
			return melontype;
		}

		@Override
		public int getDataid() { // 结合melontype,表示子类型;精英副本里表示副本id
			return dataid;
		}

		@Override
		public long getDataid2() { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
			return dataid2;
		}

		@Override
		public java.util.List<Long> getMelonerlist() { // 有资格分赃的原始人员 by changhao
			return melonerlist;
		}

		@Override
		public java.util.List<Long> getMelonerlistAsData() { // 有资格分赃的原始人员 by changhao
			return melonerlist;
		}

		@Override
		public java.util.List<Long> getWatchmelonerlist() { // 没资格观看人员 by changhao
			return watchmelonerlist;
		}

		@Override
		public java.util.List<Long> getWatchmelonerlistAsData() { // 没资格观看人员 by changhao
			return watchmelonerlist;
		}

		@Override
		public void setMelontype(int _v_) { // 分赃类型 0表示以前默认的;1表示循环打明雷怪任务,2表示精英副本
			melontype = _v_;
		}

		@Override
		public void setDataid(int _v_) { // 结合melontype,表示子类型;精英副本里表示副本id
			dataid = _v_;
		}

		@Override
		public void setDataid2(long _v_) { // 明雷怪用的角色id,如果是在队伍内,则是队长id;精英副本里表示副本唯一id
			dataid2 = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ETeamMelon.Data)) return false;
			ETeamMelon.Data _o_ = (ETeamMelon.Data) _o1_;
			if (!melonid2melons.equals(_o_.melonid2melons)) return false;
			if (melontype != _o_.melontype) return false;
			if (dataid != _o_.dataid) return false;
			if (dataid2 != _o_.dataid2) return false;
			if (!melonerlist.equals(_o_.melonerlist)) return false;
			if (!watchmelonerlist.equals(_o_.watchmelonerlist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += melonid2melons.hashCode();
			_h_ += melontype;
			_h_ += dataid;
			_h_ += dataid2;
			_h_ += melonerlist.hashCode();
			_h_ += watchmelonerlist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(melonid2melons);
			_sb_.append(",");
			_sb_.append(melontype);
			_sb_.append(",");
			_sb_.append(dataid);
			_sb_.append(",");
			_sb_.append(dataid2);
			_sb_.append(",");
			_sb_.append(melonerlist);
			_sb_.append(",");
			_sb_.append(watchmelonerlist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
