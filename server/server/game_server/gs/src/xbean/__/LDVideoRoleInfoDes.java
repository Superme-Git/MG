
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDVideoRoleInfoDes extends mkdb.XBean implements xbean.LDVideoRoleInfoDes {
	private xbean.LDRoleInfoDes role1; // 对手1
	private xbean.LDRoleInfoDes role2; // 对手1
	private java.util.ArrayList<xbean.LDTeamRoleInfoDes> teamlist1; // //如果是组队，队员详情
	private java.util.ArrayList<xbean.LDTeamRoleInfoDes> teamlist2; // //如果是组队，队员详情
	private int battleresult; // 1胜利  -1失败  0平局
	private int rosenum; // 点赞次数
	private String videoid; // 录像id
	private long fighttime; // 时间
	private int beforevideosize; // 压缩前录像大小
	private long aftervideosize; // 压缩后录像大小
	private String videourl; // url
	private int saveresult; // 0没有保存   1保存成功

	@Override
	public void _reset_unsafe_() {
		role1._reset_unsafe_();
		role2._reset_unsafe_();
		teamlist1.clear();
		teamlist2.clear();
		battleresult = 0;
		rosenum = 0;
		videoid = "";
		fighttime = 0L;
		beforevideosize = 0;
		aftervideosize = 0L;
		videourl = "";
		saveresult = 0;
	}

	LDVideoRoleInfoDes(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		role1 = new LDRoleInfoDes(0, this, "role1");
		role2 = new LDRoleInfoDes(0, this, "role2");
		teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		videoid = "";
		videourl = "";
	}

	public LDVideoRoleInfoDes() {
		this(0, null, null);
	}

	public LDVideoRoleInfoDes(LDVideoRoleInfoDes _o_) {
		this(_o_, null, null);
	}

	LDVideoRoleInfoDes(xbean.LDVideoRoleInfoDes _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDVideoRoleInfoDes) assign((LDVideoRoleInfoDes)_o1_);
		else if (_o1_ instanceof LDVideoRoleInfoDes.Data) assign((LDVideoRoleInfoDes.Data)_o1_);
		else if (_o1_ instanceof LDVideoRoleInfoDes.Const) assign(((LDVideoRoleInfoDes.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDVideoRoleInfoDes _o_) {
		_o_._xdb_verify_unsafe_();
		role1 = new LDRoleInfoDes(_o_.role1, this, "role1");
		role2 = new LDRoleInfoDes(_o_.role2, this, "role2");
		teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
			teamlist1.add(new LDTeamRoleInfoDes(_v_, this, "teamlist1"));
		teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
			teamlist2.add(new LDTeamRoleInfoDes(_v_, this, "teamlist2"));
		battleresult = _o_.battleresult;
		rosenum = _o_.rosenum;
		videoid = _o_.videoid;
		fighttime = _o_.fighttime;
		beforevideosize = _o_.beforevideosize;
		aftervideosize = _o_.aftervideosize;
		videourl = _o_.videourl;
		saveresult = _o_.saveresult;
	}

	private void assign(LDVideoRoleInfoDes.Data _o_) {
		role1 = new LDRoleInfoDes(_o_.role1, this, "role1");
		role2 = new LDRoleInfoDes(_o_.role2, this, "role2");
		teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
			teamlist1.add(new LDTeamRoleInfoDes(_v_, this, "teamlist1"));
		teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
			teamlist2.add(new LDTeamRoleInfoDes(_v_, this, "teamlist2"));
		battleresult = _o_.battleresult;
		rosenum = _o_.rosenum;
		videoid = _o_.videoid;
		fighttime = _o_.fighttime;
		beforevideosize = _o_.beforevideosize;
		aftervideosize = _o_.aftervideosize;
		videourl = _o_.videourl;
		saveresult = _o_.saveresult;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		role1.marshal(_os_);
		role2.marshal(_os_);
		_os_.compact_uint32(teamlist1.size());
		for (xbean.LDTeamRoleInfoDes _v_ : teamlist1) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(teamlist2.size());
		for (xbean.LDTeamRoleInfoDes _v_ : teamlist2) {
			_v_.marshal(_os_);
		}
		_os_.marshal(battleresult);
		_os_.marshal(rosenum);
		_os_.marshal(videoid, mkdb.Const.IO_CHARSET);
		_os_.marshal(fighttime);
		_os_.marshal(beforevideosize);
		_os_.marshal(aftervideosize);
		_os_.marshal(videourl, mkdb.Const.IO_CHARSET);
		_os_.marshal(saveresult);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		role1.unmarshal(_os_);
		role2.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.LDTeamRoleInfoDes _v_ = new LDTeamRoleInfoDes(0, this, "teamlist1");
			_v_.unmarshal(_os_);
			teamlist1.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.LDTeamRoleInfoDes _v_ = new LDTeamRoleInfoDes(0, this, "teamlist2");
			_v_.unmarshal(_os_);
			teamlist2.add(_v_);
		}
		battleresult = _os_.unmarshal_int();
		rosenum = _os_.unmarshal_int();
		videoid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		fighttime = _os_.unmarshal_long();
		beforevideosize = _os_.unmarshal_int();
		aftervideosize = _os_.unmarshal_long();
		videourl = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		saveresult = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LDVideoRoleInfoDes copy() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleInfoDes(this);
	}

	@Override
	public xbean.LDVideoRoleInfoDes toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleInfoDes toBean() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleInfoDes(this); // same as copy()
	}

	@Override
	public xbean.LDVideoRoleInfoDes toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleInfoDes toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public xbean.LDRoleInfoDes getRole1() { // 对手1
		_xdb_verify_unsafe_();
		return role1;
	}

	@Override
	public xbean.LDRoleInfoDes getRole2() { // 对手1
		_xdb_verify_unsafe_();
		return role2;
	}

	@Override
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1() { // //如果是组队，队员详情
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "teamlist1"), teamlist1);
	}

	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1AsData() { // //如果是组队，队员详情
		_xdb_verify_unsafe_();
		java.util.List<xbean.LDTeamRoleInfoDes> teamlist1;
		LDVideoRoleInfoDes _o_ = this;
		teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
			teamlist1.add(new LDTeamRoleInfoDes.Data(_v_));
		return teamlist1;
	}

	@Override
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2() { // //如果是组队，队员详情
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "teamlist2"), teamlist2);
	}

	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2AsData() { // //如果是组队，队员详情
		_xdb_verify_unsafe_();
		java.util.List<xbean.LDTeamRoleInfoDes> teamlist2;
		LDVideoRoleInfoDes _o_ = this;
		teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
			teamlist2.add(new LDTeamRoleInfoDes.Data(_v_));
		return teamlist2;
	}

	@Override
	public int getBattleresult() { // 1胜利  -1失败  0平局
		_xdb_verify_unsafe_();
		return battleresult;
	}

	@Override
	public int getRosenum() { // 点赞次数
		_xdb_verify_unsafe_();
		return rosenum;
	}

	@Override
	public String getVideoid() { // 录像id
		_xdb_verify_unsafe_();
		return videoid;
	}

	@Override
	public com.locojoy.base.Octets getVideoidOctets() { // 录像id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getVideoid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getFighttime() { // 时间
		_xdb_verify_unsafe_();
		return fighttime;
	}

	@Override
	public int getBeforevideosize() { // 压缩前录像大小
		_xdb_verify_unsafe_();
		return beforevideosize;
	}

	@Override
	public long getAftervideosize() { // 压缩后录像大小
		_xdb_verify_unsafe_();
		return aftervideosize;
	}

	@Override
	public String getVideourl() { // url
		_xdb_verify_unsafe_();
		return videourl;
	}

	@Override
	public com.locojoy.base.Octets getVideourlOctets() { // url
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getVideourl(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getSaveresult() { // 0没有保存   1保存成功
		_xdb_verify_unsafe_();
		return saveresult;
	}

	@Override
	public void setBattleresult(int _v_) { // 1胜利  -1失败  0平局
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleresult") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battleresult) {
					public void rollback() { battleresult = _xdb_saved; }
				};}});
		battleresult = _v_;
	}

	@Override
	public void setRosenum(int _v_) { // 点赞次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rosenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rosenum) {
					public void rollback() { rosenum = _xdb_saved; }
				};}});
		rosenum = _v_;
	}

	@Override
	public void setVideoid(String _v_) { // 录像id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "videoid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, videoid) {
					public void rollback() { videoid = _xdb_saved; }
				};}});
		videoid = _v_;
	}

	@Override
	public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
		_xdb_verify_unsafe_();
		this.setVideoid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setFighttime(long _v_) { // 时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fighttime) {
					public void rollback() { fighttime = _xdb_saved; }
				};}});
		fighttime = _v_;
	}

	@Override
	public void setBeforevideosize(int _v_) { // 压缩前录像大小
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "beforevideosize") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, beforevideosize) {
					public void rollback() { beforevideosize = _xdb_saved; }
				};}});
		beforevideosize = _v_;
	}

	@Override
	public void setAftervideosize(long _v_) { // 压缩后录像大小
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "aftervideosize") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, aftervideosize) {
					public void rollback() { aftervideosize = _xdb_saved; }
				};}});
		aftervideosize = _v_;
	}

	@Override
	public void setVideourl(String _v_) { // url
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "videourl") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, videourl) {
					public void rollback() { videourl = _xdb_saved; }
				};}});
		videourl = _v_;
	}

	@Override
	public void setVideourlOctets(com.locojoy.base.Octets _v_) { // url
		_xdb_verify_unsafe_();
		this.setVideourl(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSaveresult(int _v_) { // 0没有保存   1保存成功
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "saveresult") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, saveresult) {
					public void rollback() { saveresult = _xdb_saved; }
				};}});
		saveresult = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDVideoRoleInfoDes _o_ = null;
		if ( _o1_ instanceof LDVideoRoleInfoDes ) _o_ = (LDVideoRoleInfoDes)_o1_;
		else if ( _o1_ instanceof LDVideoRoleInfoDes.Const ) _o_ = ((LDVideoRoleInfoDes.Const)_o1_).nThis();
		else return false;
		if (!role1.equals(_o_.role1)) return false;
		if (!role2.equals(_o_.role2)) return false;
		if (!teamlist1.equals(_o_.teamlist1)) return false;
		if (!teamlist2.equals(_o_.teamlist2)) return false;
		if (battleresult != _o_.battleresult) return false;
		if (rosenum != _o_.rosenum) return false;
		if (!videoid.equals(_o_.videoid)) return false;
		if (fighttime != _o_.fighttime) return false;
		if (beforevideosize != _o_.beforevideosize) return false;
		if (aftervideosize != _o_.aftervideosize) return false;
		if (!videourl.equals(_o_.videourl)) return false;
		if (saveresult != _o_.saveresult) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += role1.hashCode();
		_h_ += role2.hashCode();
		_h_ += teamlist1.hashCode();
		_h_ += teamlist2.hashCode();
		_h_ += battleresult;
		_h_ += rosenum;
		_h_ += videoid.hashCode();
		_h_ += fighttime;
		_h_ += beforevideosize;
		_h_ += aftervideosize;
		_h_ += videourl.hashCode();
		_h_ += saveresult;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(role1);
		_sb_.append(",");
		_sb_.append(role2);
		_sb_.append(",");
		_sb_.append(teamlist1);
		_sb_.append(",");
		_sb_.append(teamlist2);
		_sb_.append(",");
		_sb_.append(battleresult);
		_sb_.append(",");
		_sb_.append(rosenum);
		_sb_.append(",");
		_sb_.append("'").append(videoid).append("'");
		_sb_.append(",");
		_sb_.append(fighttime);
		_sb_.append(",");
		_sb_.append(beforevideosize);
		_sb_.append(",");
		_sb_.append(aftervideosize);
		_sb_.append(",");
		_sb_.append("'").append(videourl).append("'");
		_sb_.append(",");
		_sb_.append(saveresult);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("role1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("role2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teamlist1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teamlist2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleresult"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rosenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("videoid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("beforevideosize"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("aftervideosize"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("videourl"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("saveresult"));
		return lb;
	}

	private class Const implements xbean.LDVideoRoleInfoDes {
		LDVideoRoleInfoDes nThis() {
			return LDVideoRoleInfoDes.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDVideoRoleInfoDes copy() {
			return LDVideoRoleInfoDes.this.copy();
		}

		@Override
		public xbean.LDVideoRoleInfoDes toData() {
			return LDVideoRoleInfoDes.this.toData();
		}

		public xbean.LDVideoRoleInfoDes toBean() {
			return LDVideoRoleInfoDes.this.toBean();
		}

		@Override
		public xbean.LDVideoRoleInfoDes toDataIf() {
			return LDVideoRoleInfoDes.this.toDataIf();
		}

		public xbean.LDVideoRoleInfoDes toBeanIf() {
			return LDVideoRoleInfoDes.this.toBeanIf();
		}

		@Override
		public xbean.LDRoleInfoDes getRole1() { // 对手1
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(role1);
		}

		@Override
		public xbean.LDRoleInfoDes getRole2() { // 对手1
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(role2);
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1() { // //如果是组队，队员详情
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(teamlist1);
		}

		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1AsData() { // //如果是组队，队员详情
			_xdb_verify_unsafe_();
			java.util.List<xbean.LDTeamRoleInfoDes> teamlist1;
			LDVideoRoleInfoDes _o_ = LDVideoRoleInfoDes.this;
		teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
			teamlist1.add(new LDTeamRoleInfoDes.Data(_v_));
			return teamlist1;
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2() { // //如果是组队，队员详情
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(teamlist2);
		}

		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2AsData() { // //如果是组队，队员详情
			_xdb_verify_unsafe_();
			java.util.List<xbean.LDTeamRoleInfoDes> teamlist2;
			LDVideoRoleInfoDes _o_ = LDVideoRoleInfoDes.this;
		teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
		for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
			teamlist2.add(new LDTeamRoleInfoDes.Data(_v_));
			return teamlist2;
		}

		@Override
		public int getBattleresult() { // 1胜利  -1失败  0平局
			_xdb_verify_unsafe_();
			return battleresult;
		}

		@Override
		public int getRosenum() { // 点赞次数
			_xdb_verify_unsafe_();
			return rosenum;
		}

		@Override
		public String getVideoid() { // 录像id
			_xdb_verify_unsafe_();
			return videoid;
		}

		@Override
		public com.locojoy.base.Octets getVideoidOctets() { // 录像id
			_xdb_verify_unsafe_();
			return LDVideoRoleInfoDes.this.getVideoidOctets();
		}

		@Override
		public long getFighttime() { // 时间
			_xdb_verify_unsafe_();
			return fighttime;
		}

		@Override
		public int getBeforevideosize() { // 压缩前录像大小
			_xdb_verify_unsafe_();
			return beforevideosize;
		}

		@Override
		public long getAftervideosize() { // 压缩后录像大小
			_xdb_verify_unsafe_();
			return aftervideosize;
		}

		@Override
		public String getVideourl() { // url
			_xdb_verify_unsafe_();
			return videourl;
		}

		@Override
		public com.locojoy.base.Octets getVideourlOctets() { // url
			_xdb_verify_unsafe_();
			return LDVideoRoleInfoDes.this.getVideourlOctets();
		}

		@Override
		public int getSaveresult() { // 0没有保存   1保存成功
			_xdb_verify_unsafe_();
			return saveresult;
		}

		@Override
		public void setBattleresult(int _v_) { // 1胜利  -1失败  0平局
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRosenum(int _v_) { // 点赞次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVideoid(String _v_) { // 录像id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFighttime(long _v_) { // 时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBeforevideosize(int _v_) { // 压缩前录像大小
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAftervideosize(long _v_) { // 压缩后录像大小
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVideourl(String _v_) { // url
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVideourlOctets(com.locojoy.base.Octets _v_) { // url
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSaveresult(int _v_) { // 0没有保存   1保存成功
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
			return LDVideoRoleInfoDes.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDVideoRoleInfoDes.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDVideoRoleInfoDes.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDVideoRoleInfoDes.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDVideoRoleInfoDes.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDVideoRoleInfoDes.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDVideoRoleInfoDes.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDVideoRoleInfoDes.this.hashCode();
		}

		@Override
		public String toString() {
			return LDVideoRoleInfoDes.this.toString();
		}

	}

	public static final class Data implements xbean.LDVideoRoleInfoDes {
		private xbean.LDRoleInfoDes role1; // 对手1
		private xbean.LDRoleInfoDes role2; // 对手1
		private java.util.ArrayList<xbean.LDTeamRoleInfoDes> teamlist1; // //如果是组队，队员详情
		private java.util.ArrayList<xbean.LDTeamRoleInfoDes> teamlist2; // //如果是组队，队员详情
		private int battleresult; // 1胜利  -1失败  0平局
		private int rosenum; // 点赞次数
		private String videoid; // 录像id
		private long fighttime; // 时间
		private int beforevideosize; // 压缩前录像大小
		private long aftervideosize; // 压缩后录像大小
		private String videourl; // url
		private int saveresult; // 0没有保存   1保存成功

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			role1 = new LDRoleInfoDes.Data();
			role2 = new LDRoleInfoDes.Data();
			teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			videoid = "";
			videourl = "";
		}

		Data(xbean.LDVideoRoleInfoDes _o1_) {
			if (_o1_ instanceof LDVideoRoleInfoDes) assign((LDVideoRoleInfoDes)_o1_);
			else if (_o1_ instanceof LDVideoRoleInfoDes.Data) assign((LDVideoRoleInfoDes.Data)_o1_);
			else if (_o1_ instanceof LDVideoRoleInfoDes.Const) assign(((LDVideoRoleInfoDes.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDVideoRoleInfoDes _o_) {
			role1 = new LDRoleInfoDes.Data(_o_.role1);
			role2 = new LDRoleInfoDes.Data(_o_.role2);
			teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
				teamlist1.add(new LDTeamRoleInfoDes.Data(_v_));
			teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
				teamlist2.add(new LDTeamRoleInfoDes.Data(_v_));
			battleresult = _o_.battleresult;
			rosenum = _o_.rosenum;
			videoid = _o_.videoid;
			fighttime = _o_.fighttime;
			beforevideosize = _o_.beforevideosize;
			aftervideosize = _o_.aftervideosize;
			videourl = _o_.videourl;
			saveresult = _o_.saveresult;
		}

		private void assign(LDVideoRoleInfoDes.Data _o_) {
			role1 = new LDRoleInfoDes.Data(_o_.role1);
			role2 = new LDRoleInfoDes.Data(_o_.role2);
			teamlist1 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist1)
				teamlist1.add(new LDTeamRoleInfoDes.Data(_v_));
			teamlist2 = new java.util.ArrayList<xbean.LDTeamRoleInfoDes>();
			for (xbean.LDTeamRoleInfoDes _v_ : _o_.teamlist2)
				teamlist2.add(new LDTeamRoleInfoDes.Data(_v_));
			battleresult = _o_.battleresult;
			rosenum = _o_.rosenum;
			videoid = _o_.videoid;
			fighttime = _o_.fighttime;
			beforevideosize = _o_.beforevideosize;
			aftervideosize = _o_.aftervideosize;
			videourl = _o_.videourl;
			saveresult = _o_.saveresult;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			role1.marshal(_os_);
			role2.marshal(_os_);
			_os_.compact_uint32(teamlist1.size());
			for (xbean.LDTeamRoleInfoDes _v_ : teamlist1) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(teamlist2.size());
			for (xbean.LDTeamRoleInfoDes _v_ : teamlist2) {
				_v_.marshal(_os_);
			}
			_os_.marshal(battleresult);
			_os_.marshal(rosenum);
			_os_.marshal(videoid, mkdb.Const.IO_CHARSET);
			_os_.marshal(fighttime);
			_os_.marshal(beforevideosize);
			_os_.marshal(aftervideosize);
			_os_.marshal(videourl, mkdb.Const.IO_CHARSET);
			_os_.marshal(saveresult);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			role1.unmarshal(_os_);
			role2.unmarshal(_os_);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.LDTeamRoleInfoDes _v_ = xbean.Pod.newLDTeamRoleInfoDesData();
				_v_.unmarshal(_os_);
				teamlist1.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.LDTeamRoleInfoDes _v_ = xbean.Pod.newLDTeamRoleInfoDesData();
				_v_.unmarshal(_os_);
				teamlist2.add(_v_);
			}
			battleresult = _os_.unmarshal_int();
			rosenum = _os_.unmarshal_int();
			videoid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			fighttime = _os_.unmarshal_long();
			beforevideosize = _os_.unmarshal_int();
			aftervideosize = _os_.unmarshal_long();
			videourl = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			saveresult = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LDVideoRoleInfoDes copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDVideoRoleInfoDes toData() {
			return new Data(this);
		}

		public xbean.LDVideoRoleInfoDes toBean() {
			return new LDVideoRoleInfoDes(this, null, null);
		}

		@Override
		public xbean.LDVideoRoleInfoDes toDataIf() {
			return this;
		}

		public xbean.LDVideoRoleInfoDes toBeanIf() {
			return new LDVideoRoleInfoDes(this, null, null);
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
		public xbean.LDRoleInfoDes getRole1() { // 对手1
			return role1;
		}

		@Override
		public xbean.LDRoleInfoDes getRole2() { // 对手1
			return role2;
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1() { // //如果是组队，队员详情
			return teamlist1;
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1AsData() { // //如果是组队，队员详情
			return teamlist1;
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2() { // //如果是组队，队员详情
			return teamlist2;
		}

		@Override
		public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2AsData() { // //如果是组队，队员详情
			return teamlist2;
		}

		@Override
		public int getBattleresult() { // 1胜利  -1失败  0平局
			return battleresult;
		}

		@Override
		public int getRosenum() { // 点赞次数
			return rosenum;
		}

		@Override
		public String getVideoid() { // 录像id
			return videoid;
		}

		@Override
		public com.locojoy.base.Octets getVideoidOctets() { // 录像id
			return com.locojoy.base.Octets.wrap(getVideoid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getFighttime() { // 时间
			return fighttime;
		}

		@Override
		public int getBeforevideosize() { // 压缩前录像大小
			return beforevideosize;
		}

		@Override
		public long getAftervideosize() { // 压缩后录像大小
			return aftervideosize;
		}

		@Override
		public String getVideourl() { // url
			return videourl;
		}

		@Override
		public com.locojoy.base.Octets getVideourlOctets() { // url
			return com.locojoy.base.Octets.wrap(getVideourl(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getSaveresult() { // 0没有保存   1保存成功
			return saveresult;
		}

		@Override
		public void setBattleresult(int _v_) { // 1胜利  -1失败  0平局
			battleresult = _v_;
		}

		@Override
		public void setRosenum(int _v_) { // 点赞次数
			rosenum = _v_;
		}

		@Override
		public void setVideoid(String _v_) { // 录像id
			if (null == _v_)
				throw new NullPointerException();
			videoid = _v_;
		}

		@Override
		public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
			this.setVideoid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setFighttime(long _v_) { // 时间
			fighttime = _v_;
		}

		@Override
		public void setBeforevideosize(int _v_) { // 压缩前录像大小
			beforevideosize = _v_;
		}

		@Override
		public void setAftervideosize(long _v_) { // 压缩后录像大小
			aftervideosize = _v_;
		}

		@Override
		public void setVideourl(String _v_) { // url
			if (null == _v_)
				throw new NullPointerException();
			videourl = _v_;
		}

		@Override
		public void setVideourlOctets(com.locojoy.base.Octets _v_) { // url
			this.setVideourl(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSaveresult(int _v_) { // 0没有保存   1保存成功
			saveresult = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDVideoRoleInfoDes.Data)) return false;
			LDVideoRoleInfoDes.Data _o_ = (LDVideoRoleInfoDes.Data) _o1_;
			if (!role1.equals(_o_.role1)) return false;
			if (!role2.equals(_o_.role2)) return false;
			if (!teamlist1.equals(_o_.teamlist1)) return false;
			if (!teamlist2.equals(_o_.teamlist2)) return false;
			if (battleresult != _o_.battleresult) return false;
			if (rosenum != _o_.rosenum) return false;
			if (!videoid.equals(_o_.videoid)) return false;
			if (fighttime != _o_.fighttime) return false;
			if (beforevideosize != _o_.beforevideosize) return false;
			if (aftervideosize != _o_.aftervideosize) return false;
			if (!videourl.equals(_o_.videourl)) return false;
			if (saveresult != _o_.saveresult) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += role1.hashCode();
			_h_ += role2.hashCode();
			_h_ += teamlist1.hashCode();
			_h_ += teamlist2.hashCode();
			_h_ += battleresult;
			_h_ += rosenum;
			_h_ += videoid.hashCode();
			_h_ += fighttime;
			_h_ += beforevideosize;
			_h_ += aftervideosize;
			_h_ += videourl.hashCode();
			_h_ += saveresult;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(role1);
			_sb_.append(",");
			_sb_.append(role2);
			_sb_.append(",");
			_sb_.append(teamlist1);
			_sb_.append(",");
			_sb_.append(teamlist2);
			_sb_.append(",");
			_sb_.append(battleresult);
			_sb_.append(",");
			_sb_.append(rosenum);
			_sb_.append(",");
			_sb_.append("'").append(videoid).append("'");
			_sb_.append(",");
			_sb_.append(fighttime);
			_sb_.append(",");
			_sb_.append(beforevideosize);
			_sb_.append(",");
			_sb_.append(aftervideosize);
			_sb_.append(",");
			_sb_.append("'").append(videourl).append("'");
			_sb_.append(",");
			_sb_.append(saveresult);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
