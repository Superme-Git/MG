
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SingleCompensation extends mkdb.XBean implements xbean.SingleCompensation {
	private long time; // 操作时间
	private long validtime; // 有效时间
	private String title; // 标题
	private String content; // 内容
	private java.util.LinkedList<xbean.SingleCompensationAward> awards; // 奖励列表
	private int givetype; // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
	private java.util.LinkedList<Long> roleids; // 
	private java.util.LinkedList<Long> roleidsofget; // 把key给角色后,把角色id记录在这里
	private java.util.LinkedList<Long> roleidsofexcept; // 排除的角色id列表
	private java.util.LinkedList<xbean.SingleCompensationCond> conditions; // 条件
	private String opid; // 操作流水号,10字节定长字符串,运营需求
	private String sign; // 签名,32字节定长字符串,运营需求

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		validtime = 0L;
		title = "";
		content = "";
		awards.clear();
		givetype = 0;
		roleids.clear();
		roleidsofget.clear();
		roleidsofexcept.clear();
		conditions.clear();
		opid = "";
		sign = "";
	}

	SingleCompensation(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		title = "";
		content = "";
		awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
		roleids = new java.util.LinkedList<Long>();
		roleidsofget = new java.util.LinkedList<Long>();
		roleidsofexcept = new java.util.LinkedList<Long>();
		conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
		opid = "";
		sign = "";
	}

	public SingleCompensation() {
		this(0, null, null);
	}

	public SingleCompensation(SingleCompensation _o_) {
		this(_o_, null, null);
	}

	SingleCompensation(xbean.SingleCompensation _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SingleCompensation) assign((SingleCompensation)_o1_);
		else if (_o1_ instanceof SingleCompensation.Data) assign((SingleCompensation.Data)_o1_);
		else if (_o1_ instanceof SingleCompensation.Const) assign(((SingleCompensation.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SingleCompensation _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		validtime = _o_.validtime;
		title = _o_.title;
		content = _o_.content;
		awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
		for (xbean.SingleCompensationAward _v_ : _o_.awards)
			awards.add(new SingleCompensationAward(_v_, this, "awards"));
		givetype = _o_.givetype;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		roleidsofget = new java.util.LinkedList<Long>();
		roleidsofget.addAll(_o_.roleidsofget);
		roleidsofexcept = new java.util.LinkedList<Long>();
		roleidsofexcept.addAll(_o_.roleidsofexcept);
		conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
		for (xbean.SingleCompensationCond _v_ : _o_.conditions)
			conditions.add(new SingleCompensationCond(_v_, this, "conditions"));
		opid = _o_.opid;
		sign = _o_.sign;
	}

	private void assign(SingleCompensation.Data _o_) {
		time = _o_.time;
		validtime = _o_.validtime;
		title = _o_.title;
		content = _o_.content;
		awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
		for (xbean.SingleCompensationAward _v_ : _o_.awards)
			awards.add(new SingleCompensationAward(_v_, this, "awards"));
		givetype = _o_.givetype;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		roleidsofget = new java.util.LinkedList<Long>();
		roleidsofget.addAll(_o_.roleidsofget);
		roleidsofexcept = new java.util.LinkedList<Long>();
		roleidsofexcept.addAll(_o_.roleidsofexcept);
		conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
		for (xbean.SingleCompensationCond _v_ : _o_.conditions)
			conditions.add(new SingleCompensationCond(_v_, this, "conditions"));
		opid = _o_.opid;
		sign = _o_.sign;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.marshal(validtime);
		_os_.marshal(title, mkdb.Const.IO_CHARSET);
		_os_.marshal(content, mkdb.Const.IO_CHARSET);
		_os_.compact_uint32(awards.size());
		for (xbean.SingleCompensationAward _v_ : awards) {
			_v_.marshal(_os_);
		}
		_os_.marshal(givetype);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(roleidsofget.size());
		for (Long _v_ : roleidsofget) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(roleidsofexcept.size());
		for (Long _v_ : roleidsofexcept) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(conditions.size());
		for (xbean.SingleCompensationCond _v_ : conditions) {
			_v_.marshal(_os_);
		}
		_os_.marshal(opid, mkdb.Const.IO_CHARSET);
		_os_.marshal(sign, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		validtime = _os_.unmarshal_long();
		title = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.SingleCompensationAward _v_ = new SingleCompensationAward(0, this, "awards");
			_v_.unmarshal(_os_);
			awards.add(_v_);
		}
		givetype = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleidsofget.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleidsofexcept.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.SingleCompensationCond _v_ = new SingleCompensationCond(0, this, "conditions");
			_v_.unmarshal(_os_);
			conditions.add(_v_);
		}
		opid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		sign = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.SingleCompensation copy() {
		_xdb_verify_unsafe_();
		return new SingleCompensation(this);
	}

	@Override
	public xbean.SingleCompensation toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensation toBean() {
		_xdb_verify_unsafe_();
		return new SingleCompensation(this); // same as copy()
	}

	@Override
	public xbean.SingleCompensation toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensation toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 操作时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public long getValidtime() { // 有效时间
		_xdb_verify_unsafe_();
		return validtime;
	}

	@Override
	public String getTitle() { // 标题
		_xdb_verify_unsafe_();
		return title;
	}

	@Override
	public com.locojoy.base.Octets getTitleOctets() { // 标题
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTitle(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getContent() { // 内容
		_xdb_verify_unsafe_();
		return content;
	}

	@Override
	public com.locojoy.base.Octets getContentOctets() { // 内容
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public java.util.List<xbean.SingleCompensationAward> getAwards() { // 奖励列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "awards"), awards);
	}

	public java.util.List<xbean.SingleCompensationAward> getAwardsAsData() { // 奖励列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.SingleCompensationAward> awards;
		SingleCompensation _o_ = this;
		awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
		for (xbean.SingleCompensationAward _v_ : _o_.awards)
			awards.add(new SingleCompensationAward.Data(_v_));
		return awards;
	}

	@Override
	public int getGivetype() { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
		_xdb_verify_unsafe_();
		return givetype;
	}

	@Override
	public java.util.List<Long> getRoleids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roleids"), roleids);
	}

	public java.util.List<Long> getRoleidsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Long> roleids;
		SingleCompensation _o_ = this;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		return roleids;
	}

	@Override
	public java.util.List<Long> getRoleidsofget() { // 把key给角色后,把角色id记录在这里
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roleidsofget"), roleidsofget);
	}

	public java.util.List<Long> getRoleidsofgetAsData() { // 把key给角色后,把角色id记录在这里
		_xdb_verify_unsafe_();
		java.util.List<Long> roleidsofget;
		SingleCompensation _o_ = this;
		roleidsofget = new java.util.LinkedList<Long>();
		roleidsofget.addAll(_o_.roleidsofget);
		return roleidsofget;
	}

	@Override
	public java.util.List<Long> getRoleidsofexcept() { // 排除的角色id列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roleidsofexcept"), roleidsofexcept);
	}

	public java.util.List<Long> getRoleidsofexceptAsData() { // 排除的角色id列表
		_xdb_verify_unsafe_();
		java.util.List<Long> roleidsofexcept;
		SingleCompensation _o_ = this;
		roleidsofexcept = new java.util.LinkedList<Long>();
		roleidsofexcept.addAll(_o_.roleidsofexcept);
		return roleidsofexcept;
	}

	@Override
	public java.util.List<xbean.SingleCompensationCond> getConditions() { // 条件
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "conditions"), conditions);
	}

	public java.util.List<xbean.SingleCompensationCond> getConditionsAsData() { // 条件
		_xdb_verify_unsafe_();
		java.util.List<xbean.SingleCompensationCond> conditions;
		SingleCompensation _o_ = this;
		conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
		for (xbean.SingleCompensationCond _v_ : _o_.conditions)
			conditions.add(new SingleCompensationCond.Data(_v_));
		return conditions;
	}

	@Override
	public String getOpid() { // 操作流水号,10字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		return opid;
	}

	@Override
	public com.locojoy.base.Octets getOpidOctets() { // 操作流水号,10字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOpid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getSign() { // 签名,32字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		return sign;
	}

	@Override
	public com.locojoy.base.Octets getSignOctets() { // 签名,32字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getSign(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setTime(long _v_) { // 操作时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setValidtime(long _v_) { // 有效时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "validtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, validtime) {
					public void rollback() { validtime = _xdb_saved; }
				};}});
		validtime = _v_;
	}

	@Override
	public void setTitle(String _v_) { // 标题
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "title") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, title) {
					public void rollback() { title = _xdb_saved; }
				};}});
		title = _v_;
	}

	@Override
	public void setTitleOctets(com.locojoy.base.Octets _v_) { // 标题
		_xdb_verify_unsafe_();
		this.setTitle(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setContent(String _v_) { // 内容
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, content) {
					public void rollback() { content = _xdb_saved; }
				};}});
		content = _v_;
	}

	@Override
	public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
		_xdb_verify_unsafe_();
		this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setGivetype(int _v_) { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "givetype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, givetype) {
					public void rollback() { givetype = _xdb_saved; }
				};}});
		givetype = _v_;
	}

	@Override
	public void setOpid(String _v_) { // 操作流水号,10字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "opid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, opid) {
					public void rollback() { opid = _xdb_saved; }
				};}});
		opid = _v_;
	}

	@Override
	public void setOpidOctets(com.locojoy.base.Octets _v_) { // 操作流水号,10字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		this.setOpid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSign(String _v_) { // 签名,32字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sign") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, sign) {
					public void rollback() { sign = _xdb_saved; }
				};}});
		sign = _v_;
	}

	@Override
	public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名,32字节定长字符串,运营需求
		_xdb_verify_unsafe_();
		this.setSign(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SingleCompensation _o_ = null;
		if ( _o1_ instanceof SingleCompensation ) _o_ = (SingleCompensation)_o1_;
		else if ( _o1_ instanceof SingleCompensation.Const ) _o_ = ((SingleCompensation.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (validtime != _o_.validtime) return false;
		if (!title.equals(_o_.title)) return false;
		if (!content.equals(_o_.content)) return false;
		if (!awards.equals(_o_.awards)) return false;
		if (givetype != _o_.givetype) return false;
		if (!roleids.equals(_o_.roleids)) return false;
		if (!roleidsofget.equals(_o_.roleidsofget)) return false;
		if (!roleidsofexcept.equals(_o_.roleidsofexcept)) return false;
		if (!conditions.equals(_o_.conditions)) return false;
		if (!opid.equals(_o_.opid)) return false;
		if (!sign.equals(_o_.sign)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += validtime;
		_h_ += title.hashCode();
		_h_ += content.hashCode();
		_h_ += awards.hashCode();
		_h_ += givetype;
		_h_ += roleids.hashCode();
		_h_ += roleidsofget.hashCode();
		_h_ += roleidsofexcept.hashCode();
		_h_ += conditions.hashCode();
		_h_ += opid.hashCode();
		_h_ += sign.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(validtime);
		_sb_.append(",");
		_sb_.append("'").append(title).append("'");
		_sb_.append(",");
		_sb_.append("'").append(content).append("'");
		_sb_.append(",");
		_sb_.append(awards);
		_sb_.append(",");
		_sb_.append(givetype);
		_sb_.append(",");
		_sb_.append(roleids);
		_sb_.append(",");
		_sb_.append(roleidsofget);
		_sb_.append(",");
		_sb_.append(roleidsofexcept);
		_sb_.append(",");
		_sb_.append(conditions);
		_sb_.append(",");
		_sb_.append("'").append(opid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(sign).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("validtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("title"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("content"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awards"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("givetype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleidsofget"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleidsofexcept"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("conditions"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("opid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sign"));
		return lb;
	}

	private class Const implements xbean.SingleCompensation {
		SingleCompensation nThis() {
			return SingleCompensation.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SingleCompensation copy() {
			return SingleCompensation.this.copy();
		}

		@Override
		public xbean.SingleCompensation toData() {
			return SingleCompensation.this.toData();
		}

		public xbean.SingleCompensation toBean() {
			return SingleCompensation.this.toBean();
		}

		@Override
		public xbean.SingleCompensation toDataIf() {
			return SingleCompensation.this.toDataIf();
		}

		public xbean.SingleCompensation toBeanIf() {
			return SingleCompensation.this.toBeanIf();
		}

		@Override
		public long getTime() { // 操作时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public long getValidtime() { // 有效时间
			_xdb_verify_unsafe_();
			return validtime;
		}

		@Override
		public String getTitle() { // 标题
			_xdb_verify_unsafe_();
			return title;
		}

		@Override
		public com.locojoy.base.Octets getTitleOctets() { // 标题
			_xdb_verify_unsafe_();
			return SingleCompensation.this.getTitleOctets();
		}

		@Override
		public String getContent() { // 内容
			_xdb_verify_unsafe_();
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 内容
			_xdb_verify_unsafe_();
			return SingleCompensation.this.getContentOctets();
		}

		@Override
		public java.util.List<xbean.SingleCompensationAward> getAwards() { // 奖励列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(awards);
		}

		public java.util.List<xbean.SingleCompensationAward> getAwardsAsData() { // 奖励列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.SingleCompensationAward> awards;
			SingleCompensation _o_ = SingleCompensation.this;
		awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
		for (xbean.SingleCompensationAward _v_ : _o_.awards)
			awards.add(new SingleCompensationAward.Data(_v_));
			return awards;
		}

		@Override
		public int getGivetype() { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
			_xdb_verify_unsafe_();
			return givetype;
		}

		@Override
		public java.util.List<Long> getRoleids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roleids);
		}

		public java.util.List<Long> getRoleidsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Long> roleids;
			SingleCompensation _o_ = SingleCompensation.this;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
			return roleids;
		}

		@Override
		public java.util.List<Long> getRoleidsofget() { // 把key给角色后,把角色id记录在这里
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roleidsofget);
		}

		public java.util.List<Long> getRoleidsofgetAsData() { // 把key给角色后,把角色id记录在这里
			_xdb_verify_unsafe_();
			java.util.List<Long> roleidsofget;
			SingleCompensation _o_ = SingleCompensation.this;
		roleidsofget = new java.util.LinkedList<Long>();
		roleidsofget.addAll(_o_.roleidsofget);
			return roleidsofget;
		}

		@Override
		public java.util.List<Long> getRoleidsofexcept() { // 排除的角色id列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roleidsofexcept);
		}

		public java.util.List<Long> getRoleidsofexceptAsData() { // 排除的角色id列表
			_xdb_verify_unsafe_();
			java.util.List<Long> roleidsofexcept;
			SingleCompensation _o_ = SingleCompensation.this;
		roleidsofexcept = new java.util.LinkedList<Long>();
		roleidsofexcept.addAll(_o_.roleidsofexcept);
			return roleidsofexcept;
		}

		@Override
		public java.util.List<xbean.SingleCompensationCond> getConditions() { // 条件
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(conditions);
		}

		public java.util.List<xbean.SingleCompensationCond> getConditionsAsData() { // 条件
			_xdb_verify_unsafe_();
			java.util.List<xbean.SingleCompensationCond> conditions;
			SingleCompensation _o_ = SingleCompensation.this;
		conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
		for (xbean.SingleCompensationCond _v_ : _o_.conditions)
			conditions.add(new SingleCompensationCond.Data(_v_));
			return conditions;
		}

		@Override
		public String getOpid() { // 操作流水号,10字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			return opid;
		}

		@Override
		public com.locojoy.base.Octets getOpidOctets() { // 操作流水号,10字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			return SingleCompensation.this.getOpidOctets();
		}

		@Override
		public String getSign() { // 签名,32字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			return sign;
		}

		@Override
		public com.locojoy.base.Octets getSignOctets() { // 签名,32字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			return SingleCompensation.this.getSignOctets();
		}

		@Override
		public void setTime(long _v_) { // 操作时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValidtime(long _v_) { // 有效时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTitle(String _v_) { // 标题
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTitleOctets(com.locojoy.base.Octets _v_) { // 标题
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContent(String _v_) { // 内容
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGivetype(int _v_) { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpid(String _v_) { // 操作流水号,10字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpidOctets(com.locojoy.base.Octets _v_) { // 操作流水号,10字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSign(String _v_) { // 签名,32字节定长字符串,运营需求
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名,32字节定长字符串,运营需求
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
			return SingleCompensation.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SingleCompensation.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SingleCompensation.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SingleCompensation.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SingleCompensation.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SingleCompensation.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SingleCompensation.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SingleCompensation.this.hashCode();
		}

		@Override
		public String toString() {
			return SingleCompensation.this.toString();
		}

	}

	public static final class Data implements xbean.SingleCompensation {
		private long time; // 操作时间
		private long validtime; // 有效时间
		private String title; // 标题
		private String content; // 内容
		private java.util.LinkedList<xbean.SingleCompensationAward> awards; // 奖励列表
		private int givetype; // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
		private java.util.LinkedList<Long> roleids; // 
		private java.util.LinkedList<Long> roleidsofget; // 把key给角色后,把角色id记录在这里
		private java.util.LinkedList<Long> roleidsofexcept; // 排除的角色id列表
		private java.util.LinkedList<xbean.SingleCompensationCond> conditions; // 条件
		private String opid; // 操作流水号,10字节定长字符串,运营需求
		private String sign; // 签名,32字节定长字符串,运营需求

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			title = "";
			content = "";
			awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
			roleids = new java.util.LinkedList<Long>();
			roleidsofget = new java.util.LinkedList<Long>();
			roleidsofexcept = new java.util.LinkedList<Long>();
			conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
			opid = "";
			sign = "";
		}

		Data(xbean.SingleCompensation _o1_) {
			if (_o1_ instanceof SingleCompensation) assign((SingleCompensation)_o1_);
			else if (_o1_ instanceof SingleCompensation.Data) assign((SingleCompensation.Data)_o1_);
			else if (_o1_ instanceof SingleCompensation.Const) assign(((SingleCompensation.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SingleCompensation _o_) {
			time = _o_.time;
			validtime = _o_.validtime;
			title = _o_.title;
			content = _o_.content;
			awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
			for (xbean.SingleCompensationAward _v_ : _o_.awards)
				awards.add(new SingleCompensationAward.Data(_v_));
			givetype = _o_.givetype;
			roleids = new java.util.LinkedList<Long>();
			roleids.addAll(_o_.roleids);
			roleidsofget = new java.util.LinkedList<Long>();
			roleidsofget.addAll(_o_.roleidsofget);
			roleidsofexcept = new java.util.LinkedList<Long>();
			roleidsofexcept.addAll(_o_.roleidsofexcept);
			conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
			for (xbean.SingleCompensationCond _v_ : _o_.conditions)
				conditions.add(new SingleCompensationCond.Data(_v_));
			opid = _o_.opid;
			sign = _o_.sign;
		}

		private void assign(SingleCompensation.Data _o_) {
			time = _o_.time;
			validtime = _o_.validtime;
			title = _o_.title;
			content = _o_.content;
			awards = new java.util.LinkedList<xbean.SingleCompensationAward>();
			for (xbean.SingleCompensationAward _v_ : _o_.awards)
				awards.add(new SingleCompensationAward.Data(_v_));
			givetype = _o_.givetype;
			roleids = new java.util.LinkedList<Long>();
			roleids.addAll(_o_.roleids);
			roleidsofget = new java.util.LinkedList<Long>();
			roleidsofget.addAll(_o_.roleidsofget);
			roleidsofexcept = new java.util.LinkedList<Long>();
			roleidsofexcept.addAll(_o_.roleidsofexcept);
			conditions = new java.util.LinkedList<xbean.SingleCompensationCond>();
			for (xbean.SingleCompensationCond _v_ : _o_.conditions)
				conditions.add(new SingleCompensationCond.Data(_v_));
			opid = _o_.opid;
			sign = _o_.sign;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.marshal(validtime);
			_os_.marshal(title, mkdb.Const.IO_CHARSET);
			_os_.marshal(content, mkdb.Const.IO_CHARSET);
			_os_.compact_uint32(awards.size());
			for (xbean.SingleCompensationAward _v_ : awards) {
				_v_.marshal(_os_);
			}
			_os_.marshal(givetype);
			_os_.compact_uint32(roleids.size());
			for (Long _v_ : roleids) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(roleidsofget.size());
			for (Long _v_ : roleidsofget) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(roleidsofexcept.size());
			for (Long _v_ : roleidsofexcept) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(conditions.size());
			for (xbean.SingleCompensationCond _v_ : conditions) {
				_v_.marshal(_os_);
			}
			_os_.marshal(opid, mkdb.Const.IO_CHARSET);
			_os_.marshal(sign, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			validtime = _os_.unmarshal_long();
			title = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.SingleCompensationAward _v_ = xbean.Pod.newSingleCompensationAwardData();
				_v_.unmarshal(_os_);
				awards.add(_v_);
			}
			givetype = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleids.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleidsofget.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleidsofexcept.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.SingleCompensationCond _v_ = xbean.Pod.newSingleCompensationCondData();
				_v_.unmarshal(_os_);
				conditions.add(_v_);
			}
			opid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			sign = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.SingleCompensation copy() {
			return new Data(this);
		}

		@Override
		public xbean.SingleCompensation toData() {
			return new Data(this);
		}

		public xbean.SingleCompensation toBean() {
			return new SingleCompensation(this, null, null);
		}

		@Override
		public xbean.SingleCompensation toDataIf() {
			return this;
		}

		public xbean.SingleCompensation toBeanIf() {
			return new SingleCompensation(this, null, null);
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
		public long getTime() { // 操作时间
			return time;
		}

		@Override
		public long getValidtime() { // 有效时间
			return validtime;
		}

		@Override
		public String getTitle() { // 标题
			return title;
		}

		@Override
		public com.locojoy.base.Octets getTitleOctets() { // 标题
			return com.locojoy.base.Octets.wrap(getTitle(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getContent() { // 内容
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 内容
			return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public java.util.List<xbean.SingleCompensationAward> getAwards() { // 奖励列表
			return awards;
		}

		@Override
		public java.util.List<xbean.SingleCompensationAward> getAwardsAsData() { // 奖励列表
			return awards;
		}

		@Override
		public int getGivetype() { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
			return givetype;
		}

		@Override
		public java.util.List<Long> getRoleids() { // 
			return roleids;
		}

		@Override
		public java.util.List<Long> getRoleidsAsData() { // 
			return roleids;
		}

		@Override
		public java.util.List<Long> getRoleidsofget() { // 把key给角色后,把角色id记录在这里
			return roleidsofget;
		}

		@Override
		public java.util.List<Long> getRoleidsofgetAsData() { // 把key给角色后,把角色id记录在这里
			return roleidsofget;
		}

		@Override
		public java.util.List<Long> getRoleidsofexcept() { // 排除的角色id列表
			return roleidsofexcept;
		}

		@Override
		public java.util.List<Long> getRoleidsofexceptAsData() { // 排除的角色id列表
			return roleidsofexcept;
		}

		@Override
		public java.util.List<xbean.SingleCompensationCond> getConditions() { // 条件
			return conditions;
		}

		@Override
		public java.util.List<xbean.SingleCompensationCond> getConditionsAsData() { // 条件
			return conditions;
		}

		@Override
		public String getOpid() { // 操作流水号,10字节定长字符串,运营需求
			return opid;
		}

		@Override
		public com.locojoy.base.Octets getOpidOctets() { // 操作流水号,10字节定长字符串,运营需求
			return com.locojoy.base.Octets.wrap(getOpid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getSign() { // 签名,32字节定长字符串,运营需求
			return sign;
		}

		@Override
		public com.locojoy.base.Octets getSignOctets() { // 签名,32字节定长字符串,运营需求
			return com.locojoy.base.Octets.wrap(getSign(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setTime(long _v_) { // 操作时间
			time = _v_;
		}

		@Override
		public void setValidtime(long _v_) { // 有效时间
			validtime = _v_;
		}

		@Override
		public void setTitle(String _v_) { // 标题
			if (null == _v_)
				throw new NullPointerException();
			title = _v_;
		}

		@Override
		public void setTitleOctets(com.locojoy.base.Octets _v_) { // 标题
			this.setTitle(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setContent(String _v_) { // 内容
			if (null == _v_)
				throw new NullPointerException();
			content = _v_;
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 内容
			this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setGivetype(int _v_) { // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
			givetype = _v_;
		}

		@Override
		public void setOpid(String _v_) { // 操作流水号,10字节定长字符串,运营需求
			if (null == _v_)
				throw new NullPointerException();
			opid = _v_;
		}

		@Override
		public void setOpidOctets(com.locojoy.base.Octets _v_) { // 操作流水号,10字节定长字符串,运营需求
			this.setOpid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSign(String _v_) { // 签名,32字节定长字符串,运营需求
			if (null == _v_)
				throw new NullPointerException();
			sign = _v_;
		}

		@Override
		public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名,32字节定长字符串,运营需求
			this.setSign(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SingleCompensation.Data)) return false;
			SingleCompensation.Data _o_ = (SingleCompensation.Data) _o1_;
			if (time != _o_.time) return false;
			if (validtime != _o_.validtime) return false;
			if (!title.equals(_o_.title)) return false;
			if (!content.equals(_o_.content)) return false;
			if (!awards.equals(_o_.awards)) return false;
			if (givetype != _o_.givetype) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!roleidsofget.equals(_o_.roleidsofget)) return false;
			if (!roleidsofexcept.equals(_o_.roleidsofexcept)) return false;
			if (!conditions.equals(_o_.conditions)) return false;
			if (!opid.equals(_o_.opid)) return false;
			if (!sign.equals(_o_.sign)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += validtime;
			_h_ += title.hashCode();
			_h_ += content.hashCode();
			_h_ += awards.hashCode();
			_h_ += givetype;
			_h_ += roleids.hashCode();
			_h_ += roleidsofget.hashCode();
			_h_ += roleidsofexcept.hashCode();
			_h_ += conditions.hashCode();
			_h_ += opid.hashCode();
			_h_ += sign.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(validtime);
			_sb_.append(",");
			_sb_.append("'").append(title).append("'");
			_sb_.append(",");
			_sb_.append("'").append(content).append("'");
			_sb_.append(",");
			_sb_.append(awards);
			_sb_.append(",");
			_sb_.append(givetype);
			_sb_.append(",");
			_sb_.append(roleids);
			_sb_.append(",");
			_sb_.append(roleidsofget);
			_sb_.append(",");
			_sb_.append(roleidsofexcept);
			_sb_.append(",");
			_sb_.append(conditions);
			_sb_.append(",");
			_sb_.append("'").append(opid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(sign).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
