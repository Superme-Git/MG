
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ImpExamRecord extends mkdb.XBean implements xbean.ImpExamRecord {
	private int maxvillexamright; // villexam历史最多答对题目数
	private int currexamaccexp; // 本次测试累积获得多少经验
	private int currexamaccmoney; // 本次测试累积获得多少钱
	private int accrightnum; // 累积答对次数(vill---prov--state)
	private int currexamaccright; // 当前测试答对次数(vill或prov或state)
	private int delwrongval; // 地王令
	private int chorightval; // 天王令
	private int maxcontinueright; // 最多连续答对次数(vill---prov--state)
	private java.util.HashMap<Integer, Integer> rightquesmap; // 每一卷考卷答对的题目map
	private long examsendquestime; // 上一次发题时间(prov专用)
	private long examconsumetime; // 本次测试消耗时间(prov专用)
	private long minstateexamtime; // stateexam历史最短时间
	private int maxstateexamright; // stateexam历史最多答对题目数
	private long stateexamtime; // 通过stateexam所用的时间
	private long lastanswertime; // 最后一次答题时间
	private int helpcnt; // 求助次数
	private int step; // 当前进度 1=vill 2=prov 3=state
	private int flag; // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
	private java.util.ArrayList<Integer> quesidlist; // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
	private int lastquesid; // 最近一道题序号
	private int lastanswer; // 最近一道题的答案
	private int lastisright; // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
	private java.util.ArrayList<Integer> worngqueslist; // 最近一道题的错误答案List(prov专用)
	private long begintime; // 开始答题时间

	@Override
	public void _reset_unsafe_() {
		maxvillexamright = 0;
		currexamaccexp = 0;
		currexamaccmoney = 0;
		accrightnum = 0;
		currexamaccright = 0;
		delwrongval = 0;
		chorightval = 0;
		maxcontinueright = 0;
		rightquesmap.clear();
		examsendquestime = 0L;
		examconsumetime = 0L;
		minstateexamtime = 0L;
		maxstateexamright = 0;
		stateexamtime = 0L;
		lastanswertime = 0L;
		helpcnt = 0;
		step = 0;
		flag = 0;
		quesidlist.clear();
		lastquesid = 0;
		lastanswer = 0;
		lastisright = 0;
		worngqueslist.clear();
		begintime = 0L;
	}

	ImpExamRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rightquesmap = new java.util.HashMap<Integer, Integer>();
		quesidlist = new java.util.ArrayList<Integer>();
		worngqueslist = new java.util.ArrayList<Integer>();
	}

	public ImpExamRecord() {
		this(0, null, null);
	}

	public ImpExamRecord(ImpExamRecord _o_) {
		this(_o_, null, null);
	}

	ImpExamRecord(xbean.ImpExamRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ImpExamRecord) assign((ImpExamRecord)_o1_);
		else if (_o1_ instanceof ImpExamRecord.Data) assign((ImpExamRecord.Data)_o1_);
		else if (_o1_ instanceof ImpExamRecord.Const) assign(((ImpExamRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ImpExamRecord _o_) {
		_o_._xdb_verify_unsafe_();
		maxvillexamright = _o_.maxvillexamright;
		currexamaccexp = _o_.currexamaccexp;
		currexamaccmoney = _o_.currexamaccmoney;
		accrightnum = _o_.accrightnum;
		currexamaccright = _o_.currexamaccright;
		delwrongval = _o_.delwrongval;
		chorightval = _o_.chorightval;
		maxcontinueright = _o_.maxcontinueright;
		rightquesmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
			rightquesmap.put(_e_.getKey(), _e_.getValue());
		examsendquestime = _o_.examsendquestime;
		examconsumetime = _o_.examconsumetime;
		minstateexamtime = _o_.minstateexamtime;
		maxstateexamright = _o_.maxstateexamright;
		stateexamtime = _o_.stateexamtime;
		lastanswertime = _o_.lastanswertime;
		helpcnt = _o_.helpcnt;
		step = _o_.step;
		flag = _o_.flag;
		quesidlist = new java.util.ArrayList<Integer>();
		quesidlist.addAll(_o_.quesidlist);
		lastquesid = _o_.lastquesid;
		lastanswer = _o_.lastanswer;
		lastisright = _o_.lastisright;
		worngqueslist = new java.util.ArrayList<Integer>();
		worngqueslist.addAll(_o_.worngqueslist);
		begintime = _o_.begintime;
	}

	private void assign(ImpExamRecord.Data _o_) {
		maxvillexamright = _o_.maxvillexamright;
		currexamaccexp = _o_.currexamaccexp;
		currexamaccmoney = _o_.currexamaccmoney;
		accrightnum = _o_.accrightnum;
		currexamaccright = _o_.currexamaccright;
		delwrongval = _o_.delwrongval;
		chorightval = _o_.chorightval;
		maxcontinueright = _o_.maxcontinueright;
		rightquesmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
			rightquesmap.put(_e_.getKey(), _e_.getValue());
		examsendquestime = _o_.examsendquestime;
		examconsumetime = _o_.examconsumetime;
		minstateexamtime = _o_.minstateexamtime;
		maxstateexamright = _o_.maxstateexamright;
		stateexamtime = _o_.stateexamtime;
		lastanswertime = _o_.lastanswertime;
		helpcnt = _o_.helpcnt;
		step = _o_.step;
		flag = _o_.flag;
		quesidlist = new java.util.ArrayList<Integer>();
		quesidlist.addAll(_o_.quesidlist);
		lastquesid = _o_.lastquesid;
		lastanswer = _o_.lastanswer;
		lastisright = _o_.lastisright;
		worngqueslist = new java.util.ArrayList<Integer>();
		worngqueslist.addAll(_o_.worngqueslist);
		begintime = _o_.begintime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(maxvillexamright);
		_os_.marshal(currexamaccexp);
		_os_.marshal(currexamaccmoney);
		_os_.marshal(accrightnum);
		_os_.marshal(currexamaccright);
		_os_.marshal(delwrongval);
		_os_.marshal(chorightval);
		_os_.marshal(maxcontinueright);
		_os_.compact_uint32(rightquesmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : rightquesmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(examsendquestime);
		_os_.marshal(examconsumetime);
		_os_.marshal(minstateexamtime);
		_os_.marshal(maxstateexamright);
		_os_.marshal(stateexamtime);
		_os_.marshal(lastanswertime);
		_os_.marshal(helpcnt);
		_os_.marshal(step);
		_os_.marshal(flag);
		_os_.compact_uint32(quesidlist.size());
		for (Integer _v_ : quesidlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(lastquesid);
		_os_.marshal(lastanswer);
		_os_.marshal(lastisright);
		_os_.compact_uint32(worngqueslist.size());
		for (Integer _v_ : worngqueslist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(begintime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		maxvillexamright = _os_.unmarshal_int();
		currexamaccexp = _os_.unmarshal_int();
		currexamaccmoney = _os_.unmarshal_int();
		accrightnum = _os_.unmarshal_int();
		currexamaccright = _os_.unmarshal_int();
		delwrongval = _os_.unmarshal_int();
		chorightval = _os_.unmarshal_int();
		maxcontinueright = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				rightquesmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				rightquesmap.put(_k_, _v_);
			}
		}
		examsendquestime = _os_.unmarshal_long();
		examconsumetime = _os_.unmarshal_long();
		minstateexamtime = _os_.unmarshal_long();
		maxstateexamright = _os_.unmarshal_int();
		stateexamtime = _os_.unmarshal_long();
		lastanswertime = _os_.unmarshal_long();
		helpcnt = _os_.unmarshal_int();
		step = _os_.unmarshal_int();
		flag = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			quesidlist.add(_v_);
		}
		lastquesid = _os_.unmarshal_int();
		lastanswer = _os_.unmarshal_int();
		lastisright = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			worngqueslist.add(_v_);
		}
		begintime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ImpExamRecord copy() {
		_xdb_verify_unsafe_();
		return new ImpExamRecord(this);
	}

	@Override
	public xbean.ImpExamRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamRecord toBean() {
		_xdb_verify_unsafe_();
		return new ImpExamRecord(this); // same as copy()
	}

	@Override
	public xbean.ImpExamRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMaxvillexamright() { // villexam历史最多答对题目数
		_xdb_verify_unsafe_();
		return maxvillexamright;
	}

	@Override
	public int getCurrexamaccexp() { // 本次测试累积获得多少经验
		_xdb_verify_unsafe_();
		return currexamaccexp;
	}

	@Override
	public int getCurrexamaccmoney() { // 本次测试累积获得多少钱
		_xdb_verify_unsafe_();
		return currexamaccmoney;
	}

	@Override
	public int getAccrightnum() { // 累积答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		return accrightnum;
	}

	@Override
	public int getCurrexamaccright() { // 当前测试答对次数(vill或prov或state)
		_xdb_verify_unsafe_();
		return currexamaccright;
	}

	@Override
	public int getDelwrongval() { // 地王令
		_xdb_verify_unsafe_();
		return delwrongval;
	}

	@Override
	public int getChorightval() { // 天王令
		_xdb_verify_unsafe_();
		return chorightval;
	}

	@Override
	public int getMaxcontinueright() { // 最多连续答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		return maxcontinueright;
	}

	@Override
	public java.util.Map<Integer, Integer> getRightquesmap() { // 每一卷考卷答对的题目map
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rightquesmap"), rightquesmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getRightquesmapAsData() { // 每一卷考卷答对的题目map
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> rightquesmap;
		ImpExamRecord _o_ = this;
		rightquesmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
			rightquesmap.put(_e_.getKey(), _e_.getValue());
		return rightquesmap;
	}

	@Override
	public long getExamsendquestime() { // 上一次发题时间(prov专用)
		_xdb_verify_unsafe_();
		return examsendquestime;
	}

	@Override
	public long getExamconsumetime() { // 本次测试消耗时间(prov专用)
		_xdb_verify_unsafe_();
		return examconsumetime;
	}

	@Override
	public long getMinstateexamtime() { // stateexam历史最短时间
		_xdb_verify_unsafe_();
		return minstateexamtime;
	}

	@Override
	public int getMaxstateexamright() { // stateexam历史最多答对题目数
		_xdb_verify_unsafe_();
		return maxstateexamright;
	}

	@Override
	public long getStateexamtime() { // 通过stateexam所用的时间
		_xdb_verify_unsafe_();
		return stateexamtime;
	}

	@Override
	public long getLastanswertime() { // 最后一次答题时间
		_xdb_verify_unsafe_();
		return lastanswertime;
	}

	@Override
	public int getHelpcnt() { // 求助次数
		_xdb_verify_unsafe_();
		return helpcnt;
	}

	@Override
	public int getStep() { // 当前进度 1=vill 2=prov 3=state
		_xdb_verify_unsafe_();
		return step;
	}

	@Override
	public int getFlag() { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public java.util.List<Integer> getQuesidlist() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "quesidlist"), quesidlist);
	}

	public java.util.List<Integer> getQuesidlistAsData() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
		_xdb_verify_unsafe_();
		java.util.List<Integer> quesidlist;
		ImpExamRecord _o_ = this;
		quesidlist = new java.util.ArrayList<Integer>();
		quesidlist.addAll(_o_.quesidlist);
		return quesidlist;
	}

	@Override
	public int getLastquesid() { // 最近一道题序号
		_xdb_verify_unsafe_();
		return lastquesid;
	}

	@Override
	public int getLastanswer() { // 最近一道题的答案
		_xdb_verify_unsafe_();
		return lastanswer;
	}

	@Override
	public int getLastisright() { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
		_xdb_verify_unsafe_();
		return lastisright;
	}

	@Override
	public java.util.List<Integer> getWorngqueslist() { // 最近一道题的错误答案List(prov专用)
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "worngqueslist"), worngqueslist);
	}

	public java.util.List<Integer> getWorngqueslistAsData() { // 最近一道题的错误答案List(prov专用)
		_xdb_verify_unsafe_();
		java.util.List<Integer> worngqueslist;
		ImpExamRecord _o_ = this;
		worngqueslist = new java.util.ArrayList<Integer>();
		worngqueslist.addAll(_o_.worngqueslist);
		return worngqueslist;
	}

	@Override
	public long getBegintime() { // 开始答题时间
		_xdb_verify_unsafe_();
		return begintime;
	}

	@Override
	public void setMaxvillexamright(int _v_) { // villexam历史最多答对题目数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxvillexamright") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxvillexamright) {
					public void rollback() { maxvillexamright = _xdb_saved; }
				};}});
		maxvillexamright = _v_;
	}

	@Override
	public void setCurrexamaccexp(int _v_) { // 本次测试累积获得多少经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currexamaccexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currexamaccexp) {
					public void rollback() { currexamaccexp = _xdb_saved; }
				};}});
		currexamaccexp = _v_;
	}

	@Override
	public void setCurrexamaccmoney(int _v_) { // 本次测试累积获得多少钱
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currexamaccmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currexamaccmoney) {
					public void rollback() { currexamaccmoney = _xdb_saved; }
				};}});
		currexamaccmoney = _v_;
	}

	@Override
	public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "accrightnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, accrightnum) {
					public void rollback() { accrightnum = _xdb_saved; }
				};}});
		accrightnum = _v_;
	}

	@Override
	public void setCurrexamaccright(int _v_) { // 当前测试答对次数(vill或prov或state)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currexamaccright") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currexamaccright) {
					public void rollback() { currexamaccright = _xdb_saved; }
				};}});
		currexamaccright = _v_;
	}

	@Override
	public void setDelwrongval(int _v_) { // 地王令
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "delwrongval") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, delwrongval) {
					public void rollback() { delwrongval = _xdb_saved; }
				};}});
		delwrongval = _v_;
	}

	@Override
	public void setChorightval(int _v_) { // 天王令
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chorightval") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chorightval) {
					public void rollback() { chorightval = _xdb_saved; }
				};}});
		chorightval = _v_;
	}

	@Override
	public void setMaxcontinueright(int _v_) { // 最多连续答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxcontinueright") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxcontinueright) {
					public void rollback() { maxcontinueright = _xdb_saved; }
				};}});
		maxcontinueright = _v_;
	}

	@Override
	public void setExamsendquestime(long _v_) { // 上一次发题时间(prov专用)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "examsendquestime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, examsendquestime) {
					public void rollback() { examsendquestime = _xdb_saved; }
				};}});
		examsendquestime = _v_;
	}

	@Override
	public void setExamconsumetime(long _v_) { // 本次测试消耗时间(prov专用)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "examconsumetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, examconsumetime) {
					public void rollback() { examconsumetime = _xdb_saved; }
				};}});
		examconsumetime = _v_;
	}

	@Override
	public void setMinstateexamtime(long _v_) { // stateexam历史最短时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "minstateexamtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, minstateexamtime) {
					public void rollback() { minstateexamtime = _xdb_saved; }
				};}});
		minstateexamtime = _v_;
	}

	@Override
	public void setMaxstateexamright(int _v_) { // stateexam历史最多答对题目数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxstateexamright") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxstateexamright) {
					public void rollback() { maxstateexamright = _xdb_saved; }
				};}});
		maxstateexamright = _v_;
	}

	@Override
	public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "stateexamtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, stateexamtime) {
					public void rollback() { stateexamtime = _xdb_saved; }
				};}});
		stateexamtime = _v_;
	}

	@Override
	public void setLastanswertime(long _v_) { // 最后一次答题时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastanswertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastanswertime) {
					public void rollback() { lastanswertime = _xdb_saved; }
				};}});
		lastanswertime = _v_;
	}

	@Override
	public void setHelpcnt(int _v_) { // 求助次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "helpcnt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, helpcnt) {
					public void rollback() { helpcnt = _xdb_saved; }
				};}});
		helpcnt = _v_;
	}

	@Override
	public void setStep(int _v_) { // 当前进度 1=vill 2=prov 3=state
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "step") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, step) {
					public void rollback() { step = _xdb_saved; }
				};}});
		step = _v_;
	}

	@Override
	public void setFlag(int _v_) { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public void setLastquesid(int _v_) { // 最近一道题序号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastquesid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastquesid) {
					public void rollback() { lastquesid = _xdb_saved; }
				};}});
		lastquesid = _v_;
	}

	@Override
	public void setLastanswer(int _v_) { // 最近一道题的答案
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastanswer") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastanswer) {
					public void rollback() { lastanswer = _xdb_saved; }
				};}});
		lastanswer = _v_;
	}

	@Override
	public void setLastisright(int _v_) { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastisright") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastisright) {
					public void rollback() { lastisright = _xdb_saved; }
				};}});
		lastisright = _v_;
	}

	@Override
	public void setBegintime(long _v_) { // 开始答题时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "begintime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, begintime) {
					public void rollback() { begintime = _xdb_saved; }
				};}});
		begintime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ImpExamRecord _o_ = null;
		if ( _o1_ instanceof ImpExamRecord ) _o_ = (ImpExamRecord)_o1_;
		else if ( _o1_ instanceof ImpExamRecord.Const ) _o_ = ((ImpExamRecord.Const)_o1_).nThis();
		else return false;
		if (maxvillexamright != _o_.maxvillexamright) return false;
		if (currexamaccexp != _o_.currexamaccexp) return false;
		if (currexamaccmoney != _o_.currexamaccmoney) return false;
		if (accrightnum != _o_.accrightnum) return false;
		if (currexamaccright != _o_.currexamaccright) return false;
		if (delwrongval != _o_.delwrongval) return false;
		if (chorightval != _o_.chorightval) return false;
		if (maxcontinueright != _o_.maxcontinueright) return false;
		if (!rightquesmap.equals(_o_.rightquesmap)) return false;
		if (examsendquestime != _o_.examsendquestime) return false;
		if (examconsumetime != _o_.examconsumetime) return false;
		if (minstateexamtime != _o_.minstateexamtime) return false;
		if (maxstateexamright != _o_.maxstateexamright) return false;
		if (stateexamtime != _o_.stateexamtime) return false;
		if (lastanswertime != _o_.lastanswertime) return false;
		if (helpcnt != _o_.helpcnt) return false;
		if (step != _o_.step) return false;
		if (flag != _o_.flag) return false;
		if (!quesidlist.equals(_o_.quesidlist)) return false;
		if (lastquesid != _o_.lastquesid) return false;
		if (lastanswer != _o_.lastanswer) return false;
		if (lastisright != _o_.lastisright) return false;
		if (!worngqueslist.equals(_o_.worngqueslist)) return false;
		if (begintime != _o_.begintime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += maxvillexamright;
		_h_ += currexamaccexp;
		_h_ += currexamaccmoney;
		_h_ += accrightnum;
		_h_ += currexamaccright;
		_h_ += delwrongval;
		_h_ += chorightval;
		_h_ += maxcontinueright;
		_h_ += rightquesmap.hashCode();
		_h_ += examsendquestime;
		_h_ += examconsumetime;
		_h_ += minstateexamtime;
		_h_ += maxstateexamright;
		_h_ += stateexamtime;
		_h_ += lastanswertime;
		_h_ += helpcnt;
		_h_ += step;
		_h_ += flag;
		_h_ += quesidlist.hashCode();
		_h_ += lastquesid;
		_h_ += lastanswer;
		_h_ += lastisright;
		_h_ += worngqueslist.hashCode();
		_h_ += begintime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maxvillexamright);
		_sb_.append(",");
		_sb_.append(currexamaccexp);
		_sb_.append(",");
		_sb_.append(currexamaccmoney);
		_sb_.append(",");
		_sb_.append(accrightnum);
		_sb_.append(",");
		_sb_.append(currexamaccright);
		_sb_.append(",");
		_sb_.append(delwrongval);
		_sb_.append(",");
		_sb_.append(chorightval);
		_sb_.append(",");
		_sb_.append(maxcontinueright);
		_sb_.append(",");
		_sb_.append(rightquesmap);
		_sb_.append(",");
		_sb_.append(examsendquestime);
		_sb_.append(",");
		_sb_.append(examconsumetime);
		_sb_.append(",");
		_sb_.append(minstateexamtime);
		_sb_.append(",");
		_sb_.append(maxstateexamright);
		_sb_.append(",");
		_sb_.append(stateexamtime);
		_sb_.append(",");
		_sb_.append(lastanswertime);
		_sb_.append(",");
		_sb_.append(helpcnt);
		_sb_.append(",");
		_sb_.append(step);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(",");
		_sb_.append(quesidlist);
		_sb_.append(",");
		_sb_.append(lastquesid);
		_sb_.append(",");
		_sb_.append(lastanswer);
		_sb_.append(",");
		_sb_.append(lastisright);
		_sb_.append(",");
		_sb_.append(worngqueslist);
		_sb_.append(",");
		_sb_.append(begintime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxvillexamright"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currexamaccexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currexamaccmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accrightnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currexamaccright"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("delwrongval"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chorightval"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxcontinueright"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("rightquesmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("examsendquestime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("examconsumetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("minstateexamtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxstateexamright"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("stateexamtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastanswertime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("helpcnt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("step"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("quesidlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastquesid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastanswer"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastisright"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("worngqueslist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("begintime"));
		return lb;
	}

	private class Const implements xbean.ImpExamRecord {
		ImpExamRecord nThis() {
			return ImpExamRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ImpExamRecord copy() {
			return ImpExamRecord.this.copy();
		}

		@Override
		public xbean.ImpExamRecord toData() {
			return ImpExamRecord.this.toData();
		}

		public xbean.ImpExamRecord toBean() {
			return ImpExamRecord.this.toBean();
		}

		@Override
		public xbean.ImpExamRecord toDataIf() {
			return ImpExamRecord.this.toDataIf();
		}

		public xbean.ImpExamRecord toBeanIf() {
			return ImpExamRecord.this.toBeanIf();
		}

		@Override
		public int getMaxvillexamright() { // villexam历史最多答对题目数
			_xdb_verify_unsafe_();
			return maxvillexamright;
		}

		@Override
		public int getCurrexamaccexp() { // 本次测试累积获得多少经验
			_xdb_verify_unsafe_();
			return currexamaccexp;
		}

		@Override
		public int getCurrexamaccmoney() { // 本次测试累积获得多少钱
			_xdb_verify_unsafe_();
			return currexamaccmoney;
		}

		@Override
		public int getAccrightnum() { // 累积答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			return accrightnum;
		}

		@Override
		public int getCurrexamaccright() { // 当前测试答对次数(vill或prov或state)
			_xdb_verify_unsafe_();
			return currexamaccright;
		}

		@Override
		public int getDelwrongval() { // 地王令
			_xdb_verify_unsafe_();
			return delwrongval;
		}

		@Override
		public int getChorightval() { // 天王令
			_xdb_verify_unsafe_();
			return chorightval;
		}

		@Override
		public int getMaxcontinueright() { // 最多连续答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			return maxcontinueright;
		}

		@Override
		public java.util.Map<Integer, Integer> getRightquesmap() { // 每一卷考卷答对的题目map
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rightquesmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getRightquesmapAsData() { // 每一卷考卷答对的题目map
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> rightquesmap;
			ImpExamRecord _o_ = ImpExamRecord.this;
			rightquesmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
				rightquesmap.put(_e_.getKey(), _e_.getValue());
			return rightquesmap;
		}

		@Override
		public long getExamsendquestime() { // 上一次发题时间(prov专用)
			_xdb_verify_unsafe_();
			return examsendquestime;
		}

		@Override
		public long getExamconsumetime() { // 本次测试消耗时间(prov专用)
			_xdb_verify_unsafe_();
			return examconsumetime;
		}

		@Override
		public long getMinstateexamtime() { // stateexam历史最短时间
			_xdb_verify_unsafe_();
			return minstateexamtime;
		}

		@Override
		public int getMaxstateexamright() { // stateexam历史最多答对题目数
			_xdb_verify_unsafe_();
			return maxstateexamright;
		}

		@Override
		public long getStateexamtime() { // 通过stateexam所用的时间
			_xdb_verify_unsafe_();
			return stateexamtime;
		}

		@Override
		public long getLastanswertime() { // 最后一次答题时间
			_xdb_verify_unsafe_();
			return lastanswertime;
		}

		@Override
		public int getHelpcnt() { // 求助次数
			_xdb_verify_unsafe_();
			return helpcnt;
		}

		@Override
		public int getStep() { // 当前进度 1=vill 2=prov 3=state
			_xdb_verify_unsafe_();
			return step;
		}

		@Override
		public int getFlag() { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public java.util.List<Integer> getQuesidlist() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(quesidlist);
		}

		public java.util.List<Integer> getQuesidlistAsData() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
			_xdb_verify_unsafe_();
			java.util.List<Integer> quesidlist;
			ImpExamRecord _o_ = ImpExamRecord.this;
		quesidlist = new java.util.ArrayList<Integer>();
		quesidlist.addAll(_o_.quesidlist);
			return quesidlist;
		}

		@Override
		public int getLastquesid() { // 最近一道题序号
			_xdb_verify_unsafe_();
			return lastquesid;
		}

		@Override
		public int getLastanswer() { // 最近一道题的答案
			_xdb_verify_unsafe_();
			return lastanswer;
		}

		@Override
		public int getLastisright() { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
			_xdb_verify_unsafe_();
			return lastisright;
		}

		@Override
		public java.util.List<Integer> getWorngqueslist() { // 最近一道题的错误答案List(prov专用)
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(worngqueslist);
		}

		public java.util.List<Integer> getWorngqueslistAsData() { // 最近一道题的错误答案List(prov专用)
			_xdb_verify_unsafe_();
			java.util.List<Integer> worngqueslist;
			ImpExamRecord _o_ = ImpExamRecord.this;
		worngqueslist = new java.util.ArrayList<Integer>();
		worngqueslist.addAll(_o_.worngqueslist);
			return worngqueslist;
		}

		@Override
		public long getBegintime() { // 开始答题时间
			_xdb_verify_unsafe_();
			return begintime;
		}

		@Override
		public void setMaxvillexamright(int _v_) { // villexam历史最多答对题目数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrexamaccexp(int _v_) { // 本次测试累积获得多少经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrexamaccmoney(int _v_) { // 本次测试累积获得多少钱
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrexamaccright(int _v_) { // 当前测试答对次数(vill或prov或state)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDelwrongval(int _v_) { // 地王令
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChorightval(int _v_) { // 天王令
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxcontinueright(int _v_) { // 最多连续答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExamsendquestime(long _v_) { // 上一次发题时间(prov专用)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExamconsumetime(long _v_) { // 本次测试消耗时间(prov专用)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMinstateexamtime(long _v_) { // stateexam历史最短时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxstateexamright(int _v_) { // stateexam历史最多答对题目数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastanswertime(long _v_) { // 最后一次答题时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHelpcnt(int _v_) { // 求助次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStep(int _v_) { // 当前进度 1=vill 2=prov 3=state
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(int _v_) { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastquesid(int _v_) { // 最近一道题序号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastanswer(int _v_) { // 最近一道题的答案
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastisright(int _v_) { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBegintime(long _v_) { // 开始答题时间
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
			return ImpExamRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ImpExamRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ImpExamRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ImpExamRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ImpExamRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ImpExamRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ImpExamRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ImpExamRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ImpExamRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ImpExamRecord {
		private int maxvillexamright; // villexam历史最多答对题目数
		private int currexamaccexp; // 本次测试累积获得多少经验
		private int currexamaccmoney; // 本次测试累积获得多少钱
		private int accrightnum; // 累积答对次数(vill---prov--state)
		private int currexamaccright; // 当前测试答对次数(vill或prov或state)
		private int delwrongval; // 地王令
		private int chorightval; // 天王令
		private int maxcontinueright; // 最多连续答对次数(vill---prov--state)
		private java.util.HashMap<Integer, Integer> rightquesmap; // 每一卷考卷答对的题目map
		private long examsendquestime; // 上一次发题时间(prov专用)
		private long examconsumetime; // 本次测试消耗时间(prov专用)
		private long minstateexamtime; // stateexam历史最短时间
		private int maxstateexamright; // stateexam历史最多答对题目数
		private long stateexamtime; // 通过stateexam所用的时间
		private long lastanswertime; // 最后一次答题时间
		private int helpcnt; // 求助次数
		private int step; // 当前进度 1=vill 2=prov 3=state
		private int flag; // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
		private java.util.ArrayList<Integer> quesidlist; // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
		private int lastquesid; // 最近一道题序号
		private int lastanswer; // 最近一道题的答案
		private int lastisright; // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
		private java.util.ArrayList<Integer> worngqueslist; // 最近一道题的错误答案List(prov专用)
		private long begintime; // 开始答题时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rightquesmap = new java.util.HashMap<Integer, Integer>();
			quesidlist = new java.util.ArrayList<Integer>();
			worngqueslist = new java.util.ArrayList<Integer>();
		}

		Data(xbean.ImpExamRecord _o1_) {
			if (_o1_ instanceof ImpExamRecord) assign((ImpExamRecord)_o1_);
			else if (_o1_ instanceof ImpExamRecord.Data) assign((ImpExamRecord.Data)_o1_);
			else if (_o1_ instanceof ImpExamRecord.Const) assign(((ImpExamRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ImpExamRecord _o_) {
			maxvillexamright = _o_.maxvillexamright;
			currexamaccexp = _o_.currexamaccexp;
			currexamaccmoney = _o_.currexamaccmoney;
			accrightnum = _o_.accrightnum;
			currexamaccright = _o_.currexamaccright;
			delwrongval = _o_.delwrongval;
			chorightval = _o_.chorightval;
			maxcontinueright = _o_.maxcontinueright;
			rightquesmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
				rightquesmap.put(_e_.getKey(), _e_.getValue());
			examsendquestime = _o_.examsendquestime;
			examconsumetime = _o_.examconsumetime;
			minstateexamtime = _o_.minstateexamtime;
			maxstateexamright = _o_.maxstateexamright;
			stateexamtime = _o_.stateexamtime;
			lastanswertime = _o_.lastanswertime;
			helpcnt = _o_.helpcnt;
			step = _o_.step;
			flag = _o_.flag;
			quesidlist = new java.util.ArrayList<Integer>();
			quesidlist.addAll(_o_.quesidlist);
			lastquesid = _o_.lastquesid;
			lastanswer = _o_.lastanswer;
			lastisright = _o_.lastisright;
			worngqueslist = new java.util.ArrayList<Integer>();
			worngqueslist.addAll(_o_.worngqueslist);
			begintime = _o_.begintime;
		}

		private void assign(ImpExamRecord.Data _o_) {
			maxvillexamright = _o_.maxvillexamright;
			currexamaccexp = _o_.currexamaccexp;
			currexamaccmoney = _o_.currexamaccmoney;
			accrightnum = _o_.accrightnum;
			currexamaccright = _o_.currexamaccright;
			delwrongval = _o_.delwrongval;
			chorightval = _o_.chorightval;
			maxcontinueright = _o_.maxcontinueright;
			rightquesmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.rightquesmap.entrySet())
				rightquesmap.put(_e_.getKey(), _e_.getValue());
			examsendquestime = _o_.examsendquestime;
			examconsumetime = _o_.examconsumetime;
			minstateexamtime = _o_.minstateexamtime;
			maxstateexamright = _o_.maxstateexamright;
			stateexamtime = _o_.stateexamtime;
			lastanswertime = _o_.lastanswertime;
			helpcnt = _o_.helpcnt;
			step = _o_.step;
			flag = _o_.flag;
			quesidlist = new java.util.ArrayList<Integer>();
			quesidlist.addAll(_o_.quesidlist);
			lastquesid = _o_.lastquesid;
			lastanswer = _o_.lastanswer;
			lastisright = _o_.lastisright;
			worngqueslist = new java.util.ArrayList<Integer>();
			worngqueslist.addAll(_o_.worngqueslist);
			begintime = _o_.begintime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(maxvillexamright);
			_os_.marshal(currexamaccexp);
			_os_.marshal(currexamaccmoney);
			_os_.marshal(accrightnum);
			_os_.marshal(currexamaccright);
			_os_.marshal(delwrongval);
			_os_.marshal(chorightval);
			_os_.marshal(maxcontinueright);
			_os_.compact_uint32(rightquesmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : rightquesmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(examsendquestime);
			_os_.marshal(examconsumetime);
			_os_.marshal(minstateexamtime);
			_os_.marshal(maxstateexamright);
			_os_.marshal(stateexamtime);
			_os_.marshal(lastanswertime);
			_os_.marshal(helpcnt);
			_os_.marshal(step);
			_os_.marshal(flag);
			_os_.compact_uint32(quesidlist.size());
			for (Integer _v_ : quesidlist) {
				_os_.marshal(_v_);
			}
			_os_.marshal(lastquesid);
			_os_.marshal(lastanswer);
			_os_.marshal(lastisright);
			_os_.compact_uint32(worngqueslist.size());
			for (Integer _v_ : worngqueslist) {
				_os_.marshal(_v_);
			}
			_os_.marshal(begintime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			maxvillexamright = _os_.unmarshal_int();
			currexamaccexp = _os_.unmarshal_int();
			currexamaccmoney = _os_.unmarshal_int();
			accrightnum = _os_.unmarshal_int();
			currexamaccright = _os_.unmarshal_int();
			delwrongval = _os_.unmarshal_int();
			chorightval = _os_.unmarshal_int();
			maxcontinueright = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					rightquesmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					rightquesmap.put(_k_, _v_);
				}
			}
			examsendquestime = _os_.unmarshal_long();
			examconsumetime = _os_.unmarshal_long();
			minstateexamtime = _os_.unmarshal_long();
			maxstateexamright = _os_.unmarshal_int();
			stateexamtime = _os_.unmarshal_long();
			lastanswertime = _os_.unmarshal_long();
			helpcnt = _os_.unmarshal_int();
			step = _os_.unmarshal_int();
			flag = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				quesidlist.add(_v_);
			}
			lastquesid = _os_.unmarshal_int();
			lastanswer = _os_.unmarshal_int();
			lastisright = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				worngqueslist.add(_v_);
			}
			begintime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ImpExamRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ImpExamRecord toData() {
			return new Data(this);
		}

		public xbean.ImpExamRecord toBean() {
			return new ImpExamRecord(this, null, null);
		}

		@Override
		public xbean.ImpExamRecord toDataIf() {
			return this;
		}

		public xbean.ImpExamRecord toBeanIf() {
			return new ImpExamRecord(this, null, null);
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
		public int getMaxvillexamright() { // villexam历史最多答对题目数
			return maxvillexamright;
		}

		@Override
		public int getCurrexamaccexp() { // 本次测试累积获得多少经验
			return currexamaccexp;
		}

		@Override
		public int getCurrexamaccmoney() { // 本次测试累积获得多少钱
			return currexamaccmoney;
		}

		@Override
		public int getAccrightnum() { // 累积答对次数(vill---prov--state)
			return accrightnum;
		}

		@Override
		public int getCurrexamaccright() { // 当前测试答对次数(vill或prov或state)
			return currexamaccright;
		}

		@Override
		public int getDelwrongval() { // 地王令
			return delwrongval;
		}

		@Override
		public int getChorightval() { // 天王令
			return chorightval;
		}

		@Override
		public int getMaxcontinueright() { // 最多连续答对次数(vill---prov--state)
			return maxcontinueright;
		}

		@Override
		public java.util.Map<Integer, Integer> getRightquesmap() { // 每一卷考卷答对的题目map
			return rightquesmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getRightquesmapAsData() { // 每一卷考卷答对的题目map
			return rightquesmap;
		}

		@Override
		public long getExamsendquestime() { // 上一次发题时间(prov专用)
			return examsendquestime;
		}

		@Override
		public long getExamconsumetime() { // 本次测试消耗时间(prov专用)
			return examconsumetime;
		}

		@Override
		public long getMinstateexamtime() { // stateexam历史最短时间
			return minstateexamtime;
		}

		@Override
		public int getMaxstateexamright() { // stateexam历史最多答对题目数
			return maxstateexamright;
		}

		@Override
		public long getStateexamtime() { // 通过stateexam所用的时间
			return stateexamtime;
		}

		@Override
		public long getLastanswertime() { // 最后一次答题时间
			return lastanswertime;
		}

		@Override
		public int getHelpcnt() { // 求助次数
			return helpcnt;
		}

		@Override
		public int getStep() { // 当前进度 1=vill 2=prov 3=state
			return step;
		}

		@Override
		public int getFlag() { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
			return flag;
		}

		@Override
		public java.util.List<Integer> getQuesidlist() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
			return quesidlist;
		}

		@Override
		public java.util.List<Integer> getQuesidlistAsData() { // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
			return quesidlist;
		}

		@Override
		public int getLastquesid() { // 最近一道题序号
			return lastquesid;
		}

		@Override
		public int getLastanswer() { // 最近一道题的答案
			return lastanswer;
		}

		@Override
		public int getLastisright() { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
			return lastisright;
		}

		@Override
		public java.util.List<Integer> getWorngqueslist() { // 最近一道题的错误答案List(prov专用)
			return worngqueslist;
		}

		@Override
		public java.util.List<Integer> getWorngqueslistAsData() { // 最近一道题的错误答案List(prov专用)
			return worngqueslist;
		}

		@Override
		public long getBegintime() { // 开始答题时间
			return begintime;
		}

		@Override
		public void setMaxvillexamright(int _v_) { // villexam历史最多答对题目数
			maxvillexamright = _v_;
		}

		@Override
		public void setCurrexamaccexp(int _v_) { // 本次测试累积获得多少经验
			currexamaccexp = _v_;
		}

		@Override
		public void setCurrexamaccmoney(int _v_) { // 本次测试累积获得多少钱
			currexamaccmoney = _v_;
		}

		@Override
		public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
			accrightnum = _v_;
		}

		@Override
		public void setCurrexamaccright(int _v_) { // 当前测试答对次数(vill或prov或state)
			currexamaccright = _v_;
		}

		@Override
		public void setDelwrongval(int _v_) { // 地王令
			delwrongval = _v_;
		}

		@Override
		public void setChorightval(int _v_) { // 天王令
			chorightval = _v_;
		}

		@Override
		public void setMaxcontinueright(int _v_) { // 最多连续答对次数(vill---prov--state)
			maxcontinueright = _v_;
		}

		@Override
		public void setExamsendquestime(long _v_) { // 上一次发题时间(prov专用)
			examsendquestime = _v_;
		}

		@Override
		public void setExamconsumetime(long _v_) { // 本次测试消耗时间(prov专用)
			examconsumetime = _v_;
		}

		@Override
		public void setMinstateexamtime(long _v_) { // stateexam历史最短时间
			minstateexamtime = _v_;
		}

		@Override
		public void setMaxstateexamright(int _v_) { // stateexam历史最多答对题目数
			maxstateexamright = _v_;
		}

		@Override
		public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
			stateexamtime = _v_;
		}

		@Override
		public void setLastanswertime(long _v_) { // 最后一次答题时间
			lastanswertime = _v_;
		}

		@Override
		public void setHelpcnt(int _v_) { // 求助次数
			helpcnt = _v_;
		}

		@Override
		public void setStep(int _v_) { // 当前进度 1=vill 2=prov 3=state
			step = _v_;
		}

		@Override
		public void setFlag(int _v_) { // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
			flag = _v_;
		}

		@Override
		public void setLastquesid(int _v_) { // 最近一道题序号
			lastquesid = _v_;
		}

		@Override
		public void setLastanswer(int _v_) { // 最近一道题的答案
			lastanswer = _v_;
		}

		@Override
		public void setLastisright(int _v_) { // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
			lastisright = _v_;
		}

		@Override
		public void setBegintime(long _v_) { // 开始答题时间
			begintime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ImpExamRecord.Data)) return false;
			ImpExamRecord.Data _o_ = (ImpExamRecord.Data) _o1_;
			if (maxvillexamright != _o_.maxvillexamright) return false;
			if (currexamaccexp != _o_.currexamaccexp) return false;
			if (currexamaccmoney != _o_.currexamaccmoney) return false;
			if (accrightnum != _o_.accrightnum) return false;
			if (currexamaccright != _o_.currexamaccright) return false;
			if (delwrongval != _o_.delwrongval) return false;
			if (chorightval != _o_.chorightval) return false;
			if (maxcontinueright != _o_.maxcontinueright) return false;
			if (!rightquesmap.equals(_o_.rightquesmap)) return false;
			if (examsendquestime != _o_.examsendquestime) return false;
			if (examconsumetime != _o_.examconsumetime) return false;
			if (minstateexamtime != _o_.minstateexamtime) return false;
			if (maxstateexamright != _o_.maxstateexamright) return false;
			if (stateexamtime != _o_.stateexamtime) return false;
			if (lastanswertime != _o_.lastanswertime) return false;
			if (helpcnt != _o_.helpcnt) return false;
			if (step != _o_.step) return false;
			if (flag != _o_.flag) return false;
			if (!quesidlist.equals(_o_.quesidlist)) return false;
			if (lastquesid != _o_.lastquesid) return false;
			if (lastanswer != _o_.lastanswer) return false;
			if (lastisright != _o_.lastisright) return false;
			if (!worngqueslist.equals(_o_.worngqueslist)) return false;
			if (begintime != _o_.begintime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += maxvillexamright;
			_h_ += currexamaccexp;
			_h_ += currexamaccmoney;
			_h_ += accrightnum;
			_h_ += currexamaccright;
			_h_ += delwrongval;
			_h_ += chorightval;
			_h_ += maxcontinueright;
			_h_ += rightquesmap.hashCode();
			_h_ += examsendquestime;
			_h_ += examconsumetime;
			_h_ += minstateexamtime;
			_h_ += maxstateexamright;
			_h_ += stateexamtime;
			_h_ += lastanswertime;
			_h_ += helpcnt;
			_h_ += step;
			_h_ += flag;
			_h_ += quesidlist.hashCode();
			_h_ += lastquesid;
			_h_ += lastanswer;
			_h_ += lastisright;
			_h_ += worngqueslist.hashCode();
			_h_ += begintime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(maxvillexamright);
			_sb_.append(",");
			_sb_.append(currexamaccexp);
			_sb_.append(",");
			_sb_.append(currexamaccmoney);
			_sb_.append(",");
			_sb_.append(accrightnum);
			_sb_.append(",");
			_sb_.append(currexamaccright);
			_sb_.append(",");
			_sb_.append(delwrongval);
			_sb_.append(",");
			_sb_.append(chorightval);
			_sb_.append(",");
			_sb_.append(maxcontinueright);
			_sb_.append(",");
			_sb_.append(rightquesmap);
			_sb_.append(",");
			_sb_.append(examsendquestime);
			_sb_.append(",");
			_sb_.append(examconsumetime);
			_sb_.append(",");
			_sb_.append(minstateexamtime);
			_sb_.append(",");
			_sb_.append(maxstateexamright);
			_sb_.append(",");
			_sb_.append(stateexamtime);
			_sb_.append(",");
			_sb_.append(lastanswertime);
			_sb_.append(",");
			_sb_.append(helpcnt);
			_sb_.append(",");
			_sb_.append(step);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(",");
			_sb_.append(quesidlist);
			_sb_.append(",");
			_sb_.append(lastquesid);
			_sb_.append(",");
			_sb_.append(lastanswer);
			_sb_.append(",");
			_sb_.append(lastisright);
			_sb_.append(",");
			_sb_.append(worngqueslist);
			_sb_.append(",");
			_sb_.append(begintime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
