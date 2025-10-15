
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GacdPicStatus extends mkdb.XBean implements xbean.GacdPicStatus {
	private long pictime; // 答图形码的时间
	private String answer; // 正确答案
	private int result; // 答对还是答错
	private long questiontime; // 答题的时间,30分钟内不再出题

	@Override
	public void _reset_unsafe_() {
		pictime = 0L;
		answer = "";
		result = 0;
		questiontime = 0L;
	}

	GacdPicStatus(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		answer = "";
	}

	public GacdPicStatus() {
		this(0, null, null);
	}

	public GacdPicStatus(GacdPicStatus _o_) {
		this(_o_, null, null);
	}

	GacdPicStatus(xbean.GacdPicStatus _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GacdPicStatus) assign((GacdPicStatus)_o1_);
		else if (_o1_ instanceof GacdPicStatus.Data) assign((GacdPicStatus.Data)_o1_);
		else if (_o1_ instanceof GacdPicStatus.Const) assign(((GacdPicStatus.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GacdPicStatus _o_) {
		_o_._xdb_verify_unsafe_();
		pictime = _o_.pictime;
		answer = _o_.answer;
		result = _o_.result;
		questiontime = _o_.questiontime;
	}

	private void assign(GacdPicStatus.Data _o_) {
		pictime = _o_.pictime;
		answer = _o_.answer;
		result = _o_.result;
		questiontime = _o_.questiontime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(pictime);
		_os_.marshal(answer, mkdb.Const.IO_CHARSET);
		_os_.marshal(result);
		_os_.marshal(questiontime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		pictime = _os_.unmarshal_long();
		answer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		result = _os_.unmarshal_int();
		questiontime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.GacdPicStatus copy() {
		_xdb_verify_unsafe_();
		return new GacdPicStatus(this);
	}

	@Override
	public xbean.GacdPicStatus toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GacdPicStatus toBean() {
		_xdb_verify_unsafe_();
		return new GacdPicStatus(this); // same as copy()
	}

	@Override
	public xbean.GacdPicStatus toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GacdPicStatus toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getPictime() { // 答图形码的时间
		_xdb_verify_unsafe_();
		return pictime;
	}

	@Override
	public String getAnswer() { // 正确答案
		_xdb_verify_unsafe_();
		return answer;
	}

	@Override
	public com.locojoy.base.Octets getAnswerOctets() { // 正确答案
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getAnswer(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getResult() { // 答对还是答错
		_xdb_verify_unsafe_();
		return result;
	}

	@Override
	public long getQuestiontime() { // 答题的时间,30分钟内不再出题
		_xdb_verify_unsafe_();
		return questiontime;
	}

	@Override
	public void setPictime(long _v_) { // 答图形码的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pictime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, pictime) {
					public void rollback() { pictime = _xdb_saved; }
				};}});
		pictime = _v_;
	}

	@Override
	public void setAnswer(String _v_) { // 正确答案
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "answer") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, answer) {
					public void rollback() { answer = _xdb_saved; }
				};}});
		answer = _v_;
	}

	@Override
	public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 正确答案
		_xdb_verify_unsafe_();
		this.setAnswer(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setResult(int _v_) { // 答对还是答错
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "result") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, result) {
					public void rollback() { result = _xdb_saved; }
				};}});
		result = _v_;
	}

	@Override
	public void setQuestiontime(long _v_) { // 答题的时间,30分钟内不再出题
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questiontime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, questiontime) {
					public void rollback() { questiontime = _xdb_saved; }
				};}});
		questiontime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GacdPicStatus _o_ = null;
		if ( _o1_ instanceof GacdPicStatus ) _o_ = (GacdPicStatus)_o1_;
		else if ( _o1_ instanceof GacdPicStatus.Const ) _o_ = ((GacdPicStatus.Const)_o1_).nThis();
		else return false;
		if (pictime != _o_.pictime) return false;
		if (!answer.equals(_o_.answer)) return false;
		if (result != _o_.result) return false;
		if (questiontime != _o_.questiontime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += pictime;
		_h_ += answer.hashCode();
		_h_ += result;
		_h_ += questiontime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pictime);
		_sb_.append(",");
		_sb_.append("'").append(answer).append("'");
		_sb_.append(",");
		_sb_.append(result);
		_sb_.append(",");
		_sb_.append(questiontime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pictime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("answer"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("result"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questiontime"));
		return lb;
	}

	private class Const implements xbean.GacdPicStatus {
		GacdPicStatus nThis() {
			return GacdPicStatus.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GacdPicStatus copy() {
			return GacdPicStatus.this.copy();
		}

		@Override
		public xbean.GacdPicStatus toData() {
			return GacdPicStatus.this.toData();
		}

		public xbean.GacdPicStatus toBean() {
			return GacdPicStatus.this.toBean();
		}

		@Override
		public xbean.GacdPicStatus toDataIf() {
			return GacdPicStatus.this.toDataIf();
		}

		public xbean.GacdPicStatus toBeanIf() {
			return GacdPicStatus.this.toBeanIf();
		}

		@Override
		public long getPictime() { // 答图形码的时间
			_xdb_verify_unsafe_();
			return pictime;
		}

		@Override
		public String getAnswer() { // 正确答案
			_xdb_verify_unsafe_();
			return answer;
		}

		@Override
		public com.locojoy.base.Octets getAnswerOctets() { // 正确答案
			_xdb_verify_unsafe_();
			return GacdPicStatus.this.getAnswerOctets();
		}

		@Override
		public int getResult() { // 答对还是答错
			_xdb_verify_unsafe_();
			return result;
		}

		@Override
		public long getQuestiontime() { // 答题的时间,30分钟内不再出题
			_xdb_verify_unsafe_();
			return questiontime;
		}

		@Override
		public void setPictime(long _v_) { // 答图形码的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnswer(String _v_) { // 正确答案
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 正确答案
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResult(int _v_) { // 答对还是答错
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestiontime(long _v_) { // 答题的时间,30分钟内不再出题
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
			return GacdPicStatus.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GacdPicStatus.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GacdPicStatus.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GacdPicStatus.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GacdPicStatus.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GacdPicStatus.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GacdPicStatus.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GacdPicStatus.this.hashCode();
		}

		@Override
		public String toString() {
			return GacdPicStatus.this.toString();
		}

	}

	public static final class Data implements xbean.GacdPicStatus {
		private long pictime; // 答图形码的时间
		private String answer; // 正确答案
		private int result; // 答对还是答错
		private long questiontime; // 答题的时间,30分钟内不再出题

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			answer = "";
		}

		Data(xbean.GacdPicStatus _o1_) {
			if (_o1_ instanceof GacdPicStatus) assign((GacdPicStatus)_o1_);
			else if (_o1_ instanceof GacdPicStatus.Data) assign((GacdPicStatus.Data)_o1_);
			else if (_o1_ instanceof GacdPicStatus.Const) assign(((GacdPicStatus.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GacdPicStatus _o_) {
			pictime = _o_.pictime;
			answer = _o_.answer;
			result = _o_.result;
			questiontime = _o_.questiontime;
		}

		private void assign(GacdPicStatus.Data _o_) {
			pictime = _o_.pictime;
			answer = _o_.answer;
			result = _o_.result;
			questiontime = _o_.questiontime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(pictime);
			_os_.marshal(answer, mkdb.Const.IO_CHARSET);
			_os_.marshal(result);
			_os_.marshal(questiontime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			pictime = _os_.unmarshal_long();
			answer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			result = _os_.unmarshal_int();
			questiontime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.GacdPicStatus copy() {
			return new Data(this);
		}

		@Override
		public xbean.GacdPicStatus toData() {
			return new Data(this);
		}

		public xbean.GacdPicStatus toBean() {
			return new GacdPicStatus(this, null, null);
		}

		@Override
		public xbean.GacdPicStatus toDataIf() {
			return this;
		}

		public xbean.GacdPicStatus toBeanIf() {
			return new GacdPicStatus(this, null, null);
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
		public long getPictime() { // 答图形码的时间
			return pictime;
		}

		@Override
		public String getAnswer() { // 正确答案
			return answer;
		}

		@Override
		public com.locojoy.base.Octets getAnswerOctets() { // 正确答案
			return com.locojoy.base.Octets.wrap(getAnswer(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getResult() { // 答对还是答错
			return result;
		}

		@Override
		public long getQuestiontime() { // 答题的时间,30分钟内不再出题
			return questiontime;
		}

		@Override
		public void setPictime(long _v_) { // 答图形码的时间
			pictime = _v_;
		}

		@Override
		public void setAnswer(String _v_) { // 正确答案
			if (null == _v_)
				throw new NullPointerException();
			answer = _v_;
		}

		@Override
		public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 正确答案
			this.setAnswer(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setResult(int _v_) { // 答对还是答错
			result = _v_;
		}

		@Override
		public void setQuestiontime(long _v_) { // 答题的时间,30分钟内不再出题
			questiontime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GacdPicStatus.Data)) return false;
			GacdPicStatus.Data _o_ = (GacdPicStatus.Data) _o1_;
			if (pictime != _o_.pictime) return false;
			if (!answer.equals(_o_.answer)) return false;
			if (result != _o_.result) return false;
			if (questiontime != _o_.questiontime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += pictime;
			_h_ += answer.hashCode();
			_h_ += result;
			_h_ += questiontime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(pictime);
			_sb_.append(",");
			_sb_.append("'").append(answer).append("'");
			_sb_.append(",");
			_sb_.append(result);
			_sb_.append(",");
			_sb_.append(questiontime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
