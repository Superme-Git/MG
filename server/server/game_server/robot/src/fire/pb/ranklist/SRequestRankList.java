
package fire.pb.ranklist;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestRankList__ extends mkio.Protocol { }

/** 服务器返回给客户端的排行榜列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestRankList extends __SRequestRankList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810234;

	public int getType() {
		return 810234;
	}

	public int ranktype; // 排行榜类型
	public int myrank; // 玩家自己的排行，0为没有上榜
	public java.util.ArrayList<com.locojoy.base.Octets> list; // 具体数据
	public int page; // 页数  从0开始
	public int hasmore; // 1=当前是最后一页  0=后边还有
	public java.lang.String mytitle; // 这个是阵营独有的  其他的忽略这个字段
	public byte takeawardflag; // 1=可以领取奖励  0=不可以领取奖励或已经领取过奖励了
	public int extdata; // 扩展数据 by changhao
	public long extdata1; // 扩展数据 by changhao
	public float extdata2; // 扩展数据 by changhao
	public java.lang.String extdata3; // 扩展数据 by changhao

	public SRequestRankList() {
		list = new java.util.ArrayList<com.locojoy.base.Octets>();
		mytitle = "";
		extdata3 = "";
	}

	public SRequestRankList(int _ranktype_, int _myrank_, java.util.ArrayList<com.locojoy.base.Octets> _list_, int _page_, int _hasmore_, java.lang.String _mytitle_, byte _takeawardflag_, int _extdata_, long _extdata1_, float _extdata2_, java.lang.String _extdata3_) {
		this.ranktype = _ranktype_;
		this.myrank = _myrank_;
		this.list = _list_;
		this.page = _page_;
		this.hasmore = _hasmore_;
		this.mytitle = _mytitle_;
		this.takeawardflag = _takeawardflag_;
		this.extdata = _extdata_;
		this.extdata1 = _extdata1_;
		this.extdata2 = _extdata2_;
		this.extdata3 = _extdata3_;
	}

	public final boolean _validator_() {
		if (hasmore < 0 || hasmore > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ranktype);
		_os_.marshal(myrank);
		_os_.compact_uint32(list.size());
		for (com.locojoy.base.Octets _v_ : list) {
			_os_.marshal(_v_);
		}
		_os_.marshal(page);
		_os_.marshal(hasmore);
		_os_.marshal(mytitle, "UTF-16LE");
		_os_.marshal(takeawardflag);
		_os_.marshal(extdata);
		_os_.marshal(extdata1);
		_os_.marshal(extdata2);
		_os_.marshal(extdata3, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ranktype = _os_.unmarshal_int();
		myrank = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			list.add(_v_);
		}
		page = _os_.unmarshal_int();
		hasmore = _os_.unmarshal_int();
		mytitle = _os_.unmarshal_String("UTF-16LE");
		takeawardflag = _os_.unmarshal_byte();
		extdata = _os_.unmarshal_int();
		extdata1 = _os_.unmarshal_long();
		extdata2 = _os_.unmarshal_float();
		extdata3 = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestRankList) {
			SRequestRankList _o_ = (SRequestRankList)_o1_;
			if (ranktype != _o_.ranktype) return false;
			if (myrank != _o_.myrank) return false;
			if (!list.equals(_o_.list)) return false;
			if (page != _o_.page) return false;
			if (hasmore != _o_.hasmore) return false;
			if (!mytitle.equals(_o_.mytitle)) return false;
			if (takeawardflag != _o_.takeawardflag) return false;
			if (extdata != _o_.extdata) return false;
			if (extdata1 != _o_.extdata1) return false;
			if (extdata2 != _o_.extdata2) return false;
			if (!extdata3.equals(_o_.extdata3)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ranktype;
		_h_ += myrank;
		_h_ += list.hashCode();
		_h_ += page;
		_h_ += hasmore;
		_h_ += mytitle.hashCode();
		_h_ += (int)takeawardflag;
		_h_ += extdata;
		_h_ += (int)extdata1;
		_h_ += Float.floatToIntBits(extdata2);
		_h_ += extdata3.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ranktype).append(",");
		_sb_.append(myrank).append(",");
		_sb_.append(list).append(",");
		_sb_.append(page).append(",");
		_sb_.append(hasmore).append(",");
		_sb_.append("T").append(mytitle.length()).append(",");
		_sb_.append(takeawardflag).append(",");
		_sb_.append(extdata).append(",");
		_sb_.append(extdata1).append(",");
		_sb_.append(extdata2).append(",");
		_sb_.append("T").append(extdata3.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

