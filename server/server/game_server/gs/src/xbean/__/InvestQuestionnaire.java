
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InvestQuestionnaire extends mkdb.XBean implements xbean.InvestQuestionnaire {
	private java.util.HashMap<Integer, Integer> answer; // key为题目,value为答案

	@Override
	public void _reset_unsafe_() {
		answer.clear();
	}

	InvestQuestionnaire(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		answer = new java.util.HashMap<Integer, Integer>();
	}

	public InvestQuestionnaire() {
		this(0, null, null);
	}

	public InvestQuestionnaire(InvestQuestionnaire _o_) {
		this(_o_, null, null);
	}

	InvestQuestionnaire(xbean.InvestQuestionnaire _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InvestQuestionnaire) assign((InvestQuestionnaire)_o1_);
		else if (_o1_ instanceof InvestQuestionnaire.Data) assign((InvestQuestionnaire.Data)_o1_);
		else if (_o1_ instanceof InvestQuestionnaire.Const) assign(((InvestQuestionnaire.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InvestQuestionnaire _o_) {
		_o_._xdb_verify_unsafe_();
		answer = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
			answer.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(InvestQuestionnaire.Data _o_) {
		answer = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
			answer.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(answer.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : answer.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				answer = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				answer.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.InvestQuestionnaire copy() {
		_xdb_verify_unsafe_();
		return new InvestQuestionnaire(this);
	}

	@Override
	public xbean.InvestQuestionnaire toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InvestQuestionnaire toBean() {
		_xdb_verify_unsafe_();
		return new InvestQuestionnaire(this); // same as copy()
	}

	@Override
	public xbean.InvestQuestionnaire toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InvestQuestionnaire toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getAnswer() { // key为题目,value为答案
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "answer"), answer);
	}

	@Override
	public java.util.Map<Integer, Integer> getAnswerAsData() { // key为题目,value为答案
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> answer;
		InvestQuestionnaire _o_ = this;
		answer = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
			answer.put(_e_.getKey(), _e_.getValue());
		return answer;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InvestQuestionnaire _o_ = null;
		if ( _o1_ instanceof InvestQuestionnaire ) _o_ = (InvestQuestionnaire)_o1_;
		else if ( _o1_ instanceof InvestQuestionnaire.Const ) _o_ = ((InvestQuestionnaire.Const)_o1_).nThis();
		else return false;
		if (!answer.equals(_o_.answer)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += answer.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answer);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("answer"));
		return lb;
	}

	private class Const implements xbean.InvestQuestionnaire {
		InvestQuestionnaire nThis() {
			return InvestQuestionnaire.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InvestQuestionnaire copy() {
			return InvestQuestionnaire.this.copy();
		}

		@Override
		public xbean.InvestQuestionnaire toData() {
			return InvestQuestionnaire.this.toData();
		}

		public xbean.InvestQuestionnaire toBean() {
			return InvestQuestionnaire.this.toBean();
		}

		@Override
		public xbean.InvestQuestionnaire toDataIf() {
			return InvestQuestionnaire.this.toDataIf();
		}

		public xbean.InvestQuestionnaire toBeanIf() {
			return InvestQuestionnaire.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getAnswer() { // key为题目,value为答案
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(answer);
		}

		@Override
		public java.util.Map<Integer, Integer> getAnswerAsData() { // key为题目,value为答案
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> answer;
			InvestQuestionnaire _o_ = InvestQuestionnaire.this;
			answer = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
				answer.put(_e_.getKey(), _e_.getValue());
			return answer;
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
			return InvestQuestionnaire.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InvestQuestionnaire.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InvestQuestionnaire.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InvestQuestionnaire.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InvestQuestionnaire.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InvestQuestionnaire.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InvestQuestionnaire.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InvestQuestionnaire.this.hashCode();
		}

		@Override
		public String toString() {
			return InvestQuestionnaire.this.toString();
		}

	}

	public static final class Data implements xbean.InvestQuestionnaire {
		private java.util.HashMap<Integer, Integer> answer; // key为题目,value为答案

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			answer = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.InvestQuestionnaire _o1_) {
			if (_o1_ instanceof InvestQuestionnaire) assign((InvestQuestionnaire)_o1_);
			else if (_o1_ instanceof InvestQuestionnaire.Data) assign((InvestQuestionnaire.Data)_o1_);
			else if (_o1_ instanceof InvestQuestionnaire.Const) assign(((InvestQuestionnaire.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InvestQuestionnaire _o_) {
			answer = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
				answer.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(InvestQuestionnaire.Data _o_) {
			answer = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.answer.entrySet())
				answer.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(answer.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : answer.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					answer = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					answer.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.InvestQuestionnaire copy() {
			return new Data(this);
		}

		@Override
		public xbean.InvestQuestionnaire toData() {
			return new Data(this);
		}

		public xbean.InvestQuestionnaire toBean() {
			return new InvestQuestionnaire(this, null, null);
		}

		@Override
		public xbean.InvestQuestionnaire toDataIf() {
			return this;
		}

		public xbean.InvestQuestionnaire toBeanIf() {
			return new InvestQuestionnaire(this, null, null);
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
		public java.util.Map<Integer, Integer> getAnswer() { // key为题目,value为答案
			return answer;
		}

		@Override
		public java.util.Map<Integer, Integer> getAnswerAsData() { // key为题目,value为答案
			return answer;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InvestQuestionnaire.Data)) return false;
			InvestQuestionnaire.Data _o_ = (InvestQuestionnaire.Data) _o1_;
			if (!answer.equals(_o_.answer)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += answer.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(answer);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
