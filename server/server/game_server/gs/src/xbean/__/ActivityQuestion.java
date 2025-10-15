
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActivityQuestion extends mkdb.XBean implements xbean.ActivityQuestion {
	private long activityquestionstarttime; // 活动答题开启时间 by changhao
	private java.util.LinkedList<Integer> questionids; // 题目 by changhao
	private int curquestionindex; // 当前回答的问题 by changhao
	private int curquestionstep; // 当前回答的步骤 by changhao
	private int answerrighttimes; // 回答正确的次数 by changhao
	private int totalanswernum; // 总共回答的数量 by changhao
	private int grabreward; // 1是可以领取2是已经领取3是不能领取 by changhao
	private int totalexp; // 总经验 by changhao
	private int totalmoney; // 总金币 by changhao
	private int helptimes; // 帮助次数 by changhao

	@Override
	public void _reset_unsafe_() {
		activityquestionstarttime = 0L;
		questionids.clear();
		curquestionindex = 0;
		curquestionstep = 0;
		answerrighttimes = 0;
		totalanswernum = 0;
		grabreward = 0;
		totalexp = 0;
		totalmoney = 0;
		helptimes = 0;
	}

	ActivityQuestion(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		questionids = new java.util.LinkedList<Integer>();
	}

	public ActivityQuestion() {
		this(0, null, null);
	}

	public ActivityQuestion(ActivityQuestion _o_) {
		this(_o_, null, null);
	}

	ActivityQuestion(xbean.ActivityQuestion _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActivityQuestion) assign((ActivityQuestion)_o1_);
		else if (_o1_ instanceof ActivityQuestion.Data) assign((ActivityQuestion.Data)_o1_);
		else if (_o1_ instanceof ActivityQuestion.Const) assign(((ActivityQuestion.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActivityQuestion _o_) {
		_o_._xdb_verify_unsafe_();
		activityquestionstarttime = _o_.activityquestionstarttime;
		questionids = new java.util.LinkedList<Integer>();
		questionids.addAll(_o_.questionids);
		curquestionindex = _o_.curquestionindex;
		curquestionstep = _o_.curquestionstep;
		answerrighttimes = _o_.answerrighttimes;
		totalanswernum = _o_.totalanswernum;
		grabreward = _o_.grabreward;
		totalexp = _o_.totalexp;
		totalmoney = _o_.totalmoney;
		helptimes = _o_.helptimes;
	}

	private void assign(ActivityQuestion.Data _o_) {
		activityquestionstarttime = _o_.activityquestionstarttime;
		questionids = new java.util.LinkedList<Integer>();
		questionids.addAll(_o_.questionids);
		curquestionindex = _o_.curquestionindex;
		curquestionstep = _o_.curquestionstep;
		answerrighttimes = _o_.answerrighttimes;
		totalanswernum = _o_.totalanswernum;
		grabreward = _o_.grabreward;
		totalexp = _o_.totalexp;
		totalmoney = _o_.totalmoney;
		helptimes = _o_.helptimes;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(activityquestionstarttime);
		_os_.compact_uint32(questionids.size());
		for (Integer _v_ : questionids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(curquestionindex);
		_os_.marshal(curquestionstep);
		_os_.marshal(answerrighttimes);
		_os_.marshal(totalanswernum);
		_os_.marshal(grabreward);
		_os_.marshal(totalexp);
		_os_.marshal(totalmoney);
		_os_.marshal(helptimes);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		activityquestionstarttime = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			questionids.add(_v_);
		}
		curquestionindex = _os_.unmarshal_int();
		curquestionstep = _os_.unmarshal_int();
		answerrighttimes = _os_.unmarshal_int();
		totalanswernum = _os_.unmarshal_int();
		grabreward = _os_.unmarshal_int();
		totalexp = _os_.unmarshal_int();
		totalmoney = _os_.unmarshal_int();
		helptimes = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ActivityQuestion copy() {
		_xdb_verify_unsafe_();
		return new ActivityQuestion(this);
	}

	@Override
	public xbean.ActivityQuestion toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityQuestion toBean() {
		_xdb_verify_unsafe_();
		return new ActivityQuestion(this); // same as copy()
	}

	@Override
	public xbean.ActivityQuestion toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityQuestion toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getActivityquestionstarttime() { // 活动答题开启时间 by changhao
		_xdb_verify_unsafe_();
		return activityquestionstarttime;
	}

	@Override
	public java.util.List<Integer> getQuestionids() { // 题目 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "questionids"), questionids);
	}

	public java.util.List<Integer> getQuestionidsAsData() { // 题目 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Integer> questionids;
		ActivityQuestion _o_ = this;
		questionids = new java.util.LinkedList<Integer>();
		questionids.addAll(_o_.questionids);
		return questionids;
	}

	@Override
	public int getCurquestionindex() { // 当前回答的问题 by changhao
		_xdb_verify_unsafe_();
		return curquestionindex;
	}

	@Override
	public int getCurquestionstep() { // 当前回答的步骤 by changhao
		_xdb_verify_unsafe_();
		return curquestionstep;
	}

	@Override
	public int getAnswerrighttimes() { // 回答正确的次数 by changhao
		_xdb_verify_unsafe_();
		return answerrighttimes;
	}

	@Override
	public int getTotalanswernum() { // 总共回答的数量 by changhao
		_xdb_verify_unsafe_();
		return totalanswernum;
	}

	@Override
	public int getGrabreward() { // 1是可以领取2是已经领取3是不能领取 by changhao
		_xdb_verify_unsafe_();
		return grabreward;
	}

	@Override
	public int getTotalexp() { // 总经验 by changhao
		_xdb_verify_unsafe_();
		return totalexp;
	}

	@Override
	public int getTotalmoney() { // 总金币 by changhao
		_xdb_verify_unsafe_();
		return totalmoney;
	}

	@Override
	public int getHelptimes() { // 帮助次数 by changhao
		_xdb_verify_unsafe_();
		return helptimes;
	}

	@Override
	public void setActivityquestionstarttime(long _v_) { // 活动答题开启时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activityquestionstarttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activityquestionstarttime) {
					public void rollback() { activityquestionstarttime = _xdb_saved; }
				};}});
		activityquestionstarttime = _v_;
	}

	@Override
	public void setCurquestionindex(int _v_) { // 当前回答的问题 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "curquestionindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, curquestionindex) {
					public void rollback() { curquestionindex = _xdb_saved; }
				};}});
		curquestionindex = _v_;
	}

	@Override
	public void setCurquestionstep(int _v_) { // 当前回答的步骤 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "curquestionstep") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, curquestionstep) {
					public void rollback() { curquestionstep = _xdb_saved; }
				};}});
		curquestionstep = _v_;
	}

	@Override
	public void setAnswerrighttimes(int _v_) { // 回答正确的次数 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "answerrighttimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, answerrighttimes) {
					public void rollback() { answerrighttimes = _xdb_saved; }
				};}});
		answerrighttimes = _v_;
	}

	@Override
	public void setTotalanswernum(int _v_) { // 总共回答的数量 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalanswernum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalanswernum) {
					public void rollback() { totalanswernum = _xdb_saved; }
				};}});
		totalanswernum = _v_;
	}

	@Override
	public void setGrabreward(int _v_) { // 1是可以领取2是已经领取3是不能领取 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "grabreward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, grabreward) {
					public void rollback() { grabreward = _xdb_saved; }
				};}});
		grabreward = _v_;
	}

	@Override
	public void setTotalexp(int _v_) { // 总经验 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalexp) {
					public void rollback() { totalexp = _xdb_saved; }
				};}});
		totalexp = _v_;
	}

	@Override
	public void setTotalmoney(int _v_) { // 总金币 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalmoney) {
					public void rollback() { totalmoney = _xdb_saved; }
				};}});
		totalmoney = _v_;
	}

	@Override
	public void setHelptimes(int _v_) { // 帮助次数 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "helptimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, helptimes) {
					public void rollback() { helptimes = _xdb_saved; }
				};}});
		helptimes = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActivityQuestion _o_ = null;
		if ( _o1_ instanceof ActivityQuestion ) _o_ = (ActivityQuestion)_o1_;
		else if ( _o1_ instanceof ActivityQuestion.Const ) _o_ = ((ActivityQuestion.Const)_o1_).nThis();
		else return false;
		if (activityquestionstarttime != _o_.activityquestionstarttime) return false;
		if (!questionids.equals(_o_.questionids)) return false;
		if (curquestionindex != _o_.curquestionindex) return false;
		if (curquestionstep != _o_.curquestionstep) return false;
		if (answerrighttimes != _o_.answerrighttimes) return false;
		if (totalanswernum != _o_.totalanswernum) return false;
		if (grabreward != _o_.grabreward) return false;
		if (totalexp != _o_.totalexp) return false;
		if (totalmoney != _o_.totalmoney) return false;
		if (helptimes != _o_.helptimes) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += activityquestionstarttime;
		_h_ += questionids.hashCode();
		_h_ += curquestionindex;
		_h_ += curquestionstep;
		_h_ += answerrighttimes;
		_h_ += totalanswernum;
		_h_ += grabreward;
		_h_ += totalexp;
		_h_ += totalmoney;
		_h_ += helptimes;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activityquestionstarttime);
		_sb_.append(",");
		_sb_.append(questionids);
		_sb_.append(",");
		_sb_.append(curquestionindex);
		_sb_.append(",");
		_sb_.append(curquestionstep);
		_sb_.append(",");
		_sb_.append(answerrighttimes);
		_sb_.append(",");
		_sb_.append(totalanswernum);
		_sb_.append(",");
		_sb_.append(grabreward);
		_sb_.append(",");
		_sb_.append(totalexp);
		_sb_.append(",");
		_sb_.append(totalmoney);
		_sb_.append(",");
		_sb_.append(helptimes);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activityquestionstarttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questionids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("curquestionindex"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("curquestionstep"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("answerrighttimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalanswernum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("grabreward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("helptimes"));
		return lb;
	}

	private class Const implements xbean.ActivityQuestion {
		ActivityQuestion nThis() {
			return ActivityQuestion.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActivityQuestion copy() {
			return ActivityQuestion.this.copy();
		}

		@Override
		public xbean.ActivityQuestion toData() {
			return ActivityQuestion.this.toData();
		}

		public xbean.ActivityQuestion toBean() {
			return ActivityQuestion.this.toBean();
		}

		@Override
		public xbean.ActivityQuestion toDataIf() {
			return ActivityQuestion.this.toDataIf();
		}

		public xbean.ActivityQuestion toBeanIf() {
			return ActivityQuestion.this.toBeanIf();
		}

		@Override
		public long getActivityquestionstarttime() { // 活动答题开启时间 by changhao
			_xdb_verify_unsafe_();
			return activityquestionstarttime;
		}

		@Override
		public java.util.List<Integer> getQuestionids() { // 题目 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(questionids);
		}

		public java.util.List<Integer> getQuestionidsAsData() { // 题目 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Integer> questionids;
			ActivityQuestion _o_ = ActivityQuestion.this;
		questionids = new java.util.LinkedList<Integer>();
		questionids.addAll(_o_.questionids);
			return questionids;
		}

		@Override
		public int getCurquestionindex() { // 当前回答的问题 by changhao
			_xdb_verify_unsafe_();
			return curquestionindex;
		}

		@Override
		public int getCurquestionstep() { // 当前回答的步骤 by changhao
			_xdb_verify_unsafe_();
			return curquestionstep;
		}

		@Override
		public int getAnswerrighttimes() { // 回答正确的次数 by changhao
			_xdb_verify_unsafe_();
			return answerrighttimes;
		}

		@Override
		public int getTotalanswernum() { // 总共回答的数量 by changhao
			_xdb_verify_unsafe_();
			return totalanswernum;
		}

		@Override
		public int getGrabreward() { // 1是可以领取2是已经领取3是不能领取 by changhao
			_xdb_verify_unsafe_();
			return grabreward;
		}

		@Override
		public int getTotalexp() { // 总经验 by changhao
			_xdb_verify_unsafe_();
			return totalexp;
		}

		@Override
		public int getTotalmoney() { // 总金币 by changhao
			_xdb_verify_unsafe_();
			return totalmoney;
		}

		@Override
		public int getHelptimes() { // 帮助次数 by changhao
			_xdb_verify_unsafe_();
			return helptimes;
		}

		@Override
		public void setActivityquestionstarttime(long _v_) { // 活动答题开启时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurquestionindex(int _v_) { // 当前回答的问题 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurquestionstep(int _v_) { // 当前回答的步骤 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnswerrighttimes(int _v_) { // 回答正确的次数 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalanswernum(int _v_) { // 总共回答的数量 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGrabreward(int _v_) { // 1是可以领取2是已经领取3是不能领取 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalexp(int _v_) { // 总经验 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalmoney(int _v_) { // 总金币 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHelptimes(int _v_) { // 帮助次数 by changhao
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
			return ActivityQuestion.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActivityQuestion.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActivityQuestion.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActivityQuestion.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActivityQuestion.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActivityQuestion.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActivityQuestion.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActivityQuestion.this.hashCode();
		}

		@Override
		public String toString() {
			return ActivityQuestion.this.toString();
		}

	}

	public static final class Data implements xbean.ActivityQuestion {
		private long activityquestionstarttime; // 活动答题开启时间 by changhao
		private java.util.LinkedList<Integer> questionids; // 题目 by changhao
		private int curquestionindex; // 当前回答的问题 by changhao
		private int curquestionstep; // 当前回答的步骤 by changhao
		private int answerrighttimes; // 回答正确的次数 by changhao
		private int totalanswernum; // 总共回答的数量 by changhao
		private int grabreward; // 1是可以领取2是已经领取3是不能领取 by changhao
		private int totalexp; // 总经验 by changhao
		private int totalmoney; // 总金币 by changhao
		private int helptimes; // 帮助次数 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			questionids = new java.util.LinkedList<Integer>();
		}

		Data(xbean.ActivityQuestion _o1_) {
			if (_o1_ instanceof ActivityQuestion) assign((ActivityQuestion)_o1_);
			else if (_o1_ instanceof ActivityQuestion.Data) assign((ActivityQuestion.Data)_o1_);
			else if (_o1_ instanceof ActivityQuestion.Const) assign(((ActivityQuestion.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActivityQuestion _o_) {
			activityquestionstarttime = _o_.activityquestionstarttime;
			questionids = new java.util.LinkedList<Integer>();
			questionids.addAll(_o_.questionids);
			curquestionindex = _o_.curquestionindex;
			curquestionstep = _o_.curquestionstep;
			answerrighttimes = _o_.answerrighttimes;
			totalanswernum = _o_.totalanswernum;
			grabreward = _o_.grabreward;
			totalexp = _o_.totalexp;
			totalmoney = _o_.totalmoney;
			helptimes = _o_.helptimes;
		}

		private void assign(ActivityQuestion.Data _o_) {
			activityquestionstarttime = _o_.activityquestionstarttime;
			questionids = new java.util.LinkedList<Integer>();
			questionids.addAll(_o_.questionids);
			curquestionindex = _o_.curquestionindex;
			curquestionstep = _o_.curquestionstep;
			answerrighttimes = _o_.answerrighttimes;
			totalanswernum = _o_.totalanswernum;
			grabreward = _o_.grabreward;
			totalexp = _o_.totalexp;
			totalmoney = _o_.totalmoney;
			helptimes = _o_.helptimes;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(activityquestionstarttime);
			_os_.compact_uint32(questionids.size());
			for (Integer _v_ : questionids) {
				_os_.marshal(_v_);
			}
			_os_.marshal(curquestionindex);
			_os_.marshal(curquestionstep);
			_os_.marshal(answerrighttimes);
			_os_.marshal(totalanswernum);
			_os_.marshal(grabreward);
			_os_.marshal(totalexp);
			_os_.marshal(totalmoney);
			_os_.marshal(helptimes);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			activityquestionstarttime = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				questionids.add(_v_);
			}
			curquestionindex = _os_.unmarshal_int();
			curquestionstep = _os_.unmarshal_int();
			answerrighttimes = _os_.unmarshal_int();
			totalanswernum = _os_.unmarshal_int();
			grabreward = _os_.unmarshal_int();
			totalexp = _os_.unmarshal_int();
			totalmoney = _os_.unmarshal_int();
			helptimes = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ActivityQuestion copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActivityQuestion toData() {
			return new Data(this);
		}

		public xbean.ActivityQuestion toBean() {
			return new ActivityQuestion(this, null, null);
		}

		@Override
		public xbean.ActivityQuestion toDataIf() {
			return this;
		}

		public xbean.ActivityQuestion toBeanIf() {
			return new ActivityQuestion(this, null, null);
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
		public long getActivityquestionstarttime() { // 活动答题开启时间 by changhao
			return activityquestionstarttime;
		}

		@Override
		public java.util.List<Integer> getQuestionids() { // 题目 by changhao
			return questionids;
		}

		@Override
		public java.util.List<Integer> getQuestionidsAsData() { // 题目 by changhao
			return questionids;
		}

		@Override
		public int getCurquestionindex() { // 当前回答的问题 by changhao
			return curquestionindex;
		}

		@Override
		public int getCurquestionstep() { // 当前回答的步骤 by changhao
			return curquestionstep;
		}

		@Override
		public int getAnswerrighttimes() { // 回答正确的次数 by changhao
			return answerrighttimes;
		}

		@Override
		public int getTotalanswernum() { // 总共回答的数量 by changhao
			return totalanswernum;
		}

		@Override
		public int getGrabreward() { // 1是可以领取2是已经领取3是不能领取 by changhao
			return grabreward;
		}

		@Override
		public int getTotalexp() { // 总经验 by changhao
			return totalexp;
		}

		@Override
		public int getTotalmoney() { // 总金币 by changhao
			return totalmoney;
		}

		@Override
		public int getHelptimes() { // 帮助次数 by changhao
			return helptimes;
		}

		@Override
		public void setActivityquestionstarttime(long _v_) { // 活动答题开启时间 by changhao
			activityquestionstarttime = _v_;
		}

		@Override
		public void setCurquestionindex(int _v_) { // 当前回答的问题 by changhao
			curquestionindex = _v_;
		}

		@Override
		public void setCurquestionstep(int _v_) { // 当前回答的步骤 by changhao
			curquestionstep = _v_;
		}

		@Override
		public void setAnswerrighttimes(int _v_) { // 回答正确的次数 by changhao
			answerrighttimes = _v_;
		}

		@Override
		public void setTotalanswernum(int _v_) { // 总共回答的数量 by changhao
			totalanswernum = _v_;
		}

		@Override
		public void setGrabreward(int _v_) { // 1是可以领取2是已经领取3是不能领取 by changhao
			grabreward = _v_;
		}

		@Override
		public void setTotalexp(int _v_) { // 总经验 by changhao
			totalexp = _v_;
		}

		@Override
		public void setTotalmoney(int _v_) { // 总金币 by changhao
			totalmoney = _v_;
		}

		@Override
		public void setHelptimes(int _v_) { // 帮助次数 by changhao
			helptimes = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActivityQuestion.Data)) return false;
			ActivityQuestion.Data _o_ = (ActivityQuestion.Data) _o1_;
			if (activityquestionstarttime != _o_.activityquestionstarttime) return false;
			if (!questionids.equals(_o_.questionids)) return false;
			if (curquestionindex != _o_.curquestionindex) return false;
			if (curquestionstep != _o_.curquestionstep) return false;
			if (answerrighttimes != _o_.answerrighttimes) return false;
			if (totalanswernum != _o_.totalanswernum) return false;
			if (grabreward != _o_.grabreward) return false;
			if (totalexp != _o_.totalexp) return false;
			if (totalmoney != _o_.totalmoney) return false;
			if (helptimes != _o_.helptimes) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += activityquestionstarttime;
			_h_ += questionids.hashCode();
			_h_ += curquestionindex;
			_h_ += curquestionstep;
			_h_ += answerrighttimes;
			_h_ += totalanswernum;
			_h_ += grabreward;
			_h_ += totalexp;
			_h_ += totalmoney;
			_h_ += helptimes;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(activityquestionstarttime);
			_sb_.append(",");
			_sb_.append(questionids);
			_sb_.append(",");
			_sb_.append(curquestionindex);
			_sb_.append(",");
			_sb_.append(curquestionstep);
			_sb_.append(",");
			_sb_.append(answerrighttimes);
			_sb_.append(",");
			_sb_.append(totalanswernum);
			_sb_.append(",");
			_sb_.append(grabreward);
			_sb_.append(",");
			_sb_.append(totalexp);
			_sb_.append(",");
			_sb_.append(totalmoney);
			_sb_.append(",");
			_sb_.append(helptimes);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
