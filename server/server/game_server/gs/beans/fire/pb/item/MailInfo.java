
package fire.pb.item;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MailInfo implements Marshal {
	public byte kind; // 类型 0=定时邮件 1=GM邮件
	public long id; // id kind=0:服务ID kind=1:key
	public byte readflag; // 读取标记 0=未读 1=已读
	public java.lang.String time; // 邮件时间
	public java.lang.String title; // 邮件标题
	public int contentid; // 邮件内容id kind=0
	public java.lang.String content; // 邮件内容 kind=1
	public int awardid; // 奖励ID kind=0
	public java.util.HashMap<Integer,Integer> items; // 奖励列表 kind=1
	public long awardexp; // 经验
	public long awardfushi; // 符石
	public long awardgold; // 金币
	public long awardmoney; // 银币
	public long awardvipexp; // vip经验
	public int npcid; // NPCID>0时,可以找NPC领取奖励 kind=0

	public MailInfo() {
		time = "";
		title = "";
		content = "";
		items = new java.util.HashMap<Integer,Integer>();
	}

	public MailInfo(byte _kind_, long _id_, byte _readflag_, java.lang.String _time_, java.lang.String _title_, int _contentid_, java.lang.String _content_, int _awardid_, java.util.HashMap<Integer,Integer> _items_, long _awardexp_, long _awardfushi_, long _awardgold_, long _awardmoney_, long _awardvipexp_, int _npcid_) {
		this.kind = _kind_;
		this.id = _id_;
		this.readflag = _readflag_;
		this.time = _time_;
		this.title = _title_;
		this.contentid = _contentid_;
		this.content = _content_;
		this.awardid = _awardid_;
		this.items = _items_;
		this.awardexp = _awardexp_;
		this.awardfushi = _awardfushi_;
		this.awardgold = _awardgold_;
		this.awardmoney = _awardmoney_;
		this.awardvipexp = _awardvipexp_;
		this.npcid = _npcid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(kind);
		_os_.marshal(id);
		_os_.marshal(readflag);
		_os_.marshal(time, "UTF-16LE");
		_os_.marshal(title, "UTF-16LE");
		_os_.marshal(contentid);
		_os_.marshal(content, "UTF-16LE");
		_os_.marshal(awardid);
		_os_.compact_uint32(items.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : items.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(awardexp);
		_os_.marshal(awardfushi);
		_os_.marshal(awardgold);
		_os_.marshal(awardmoney);
		_os_.marshal(awardvipexp);
		_os_.marshal(npcid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		kind = _os_.unmarshal_byte();
		id = _os_.unmarshal_long();
		readflag = _os_.unmarshal_byte();
		time = _os_.unmarshal_String("UTF-16LE");
		title = _os_.unmarshal_String("UTF-16LE");
		contentid = _os_.unmarshal_int();
		content = _os_.unmarshal_String("UTF-16LE");
		awardid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			items.put(_k_, _v_);
		}
		awardexp = _os_.unmarshal_long();
		awardfushi = _os_.unmarshal_long();
		awardgold = _os_.unmarshal_long();
		awardmoney = _os_.unmarshal_long();
		awardvipexp = _os_.unmarshal_long();
		npcid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MailInfo) {
			MailInfo _o_ = (MailInfo)_o1_;
			if (kind != _o_.kind) return false;
			if (id != _o_.id) return false;
			if (readflag != _o_.readflag) return false;
			if (!time.equals(_o_.time)) return false;
			if (!title.equals(_o_.title)) return false;
			if (contentid != _o_.contentid) return false;
			if (!content.equals(_o_.content)) return false;
			if (awardid != _o_.awardid) return false;
			if (!items.equals(_o_.items)) return false;
			if (awardexp != _o_.awardexp) return false;
			if (awardfushi != _o_.awardfushi) return false;
			if (awardgold != _o_.awardgold) return false;
			if (awardmoney != _o_.awardmoney) return false;
			if (awardvipexp != _o_.awardvipexp) return false;
			if (npcid != _o_.npcid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)kind;
		_h_ += (int)id;
		_h_ += (int)readflag;
		_h_ += time.hashCode();
		_h_ += title.hashCode();
		_h_ += contentid;
		_h_ += content.hashCode();
		_h_ += awardid;
		_h_ += items.hashCode();
		_h_ += (int)awardexp;
		_h_ += (int)awardfushi;
		_h_ += (int)awardgold;
		_h_ += (int)awardmoney;
		_h_ += (int)awardvipexp;
		_h_ += npcid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(kind).append(",");
		_sb_.append(id).append(",");
		_sb_.append(readflag).append(",");
		_sb_.append("T").append(time.length()).append(",");
		_sb_.append("T").append(title.length()).append(",");
		_sb_.append(contentid).append(",");
		_sb_.append("T").append(content.length()).append(",");
		_sb_.append(awardid).append(",");
		_sb_.append(items).append(",");
		_sb_.append(awardexp).append(",");
		_sb_.append(awardfushi).append(",");
		_sb_.append(awardgold).append(",");
		_sb_.append(awardmoney).append(",");
		_sb_.append(awardvipexp).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

