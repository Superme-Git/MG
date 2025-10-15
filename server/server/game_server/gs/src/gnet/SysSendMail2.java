
package gnet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.pb.item.ItemShuXing;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SysSendMail2__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SysSendMail2 extends __SysSendMail2__ {
	@Override
	protected void process() {
		// protocol handle
		xbean.Properties prop = xtable.Properties.select(receiver);
		if(prop == null)
		{
			responseFailed();
			return;
		}
		final int itemid = attach_obj.id;
		final int num = attach_obj.count;
//		final int msgId = attach_obj.proctype;
		ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
		if(attr == null)
		{
			responseFailed();
			return;
		}

		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(receiver);
		Map<Integer, Integer> items = new HashMap<Integer, Integer>();
		items.put(itemid, num);
		//new PGiveSingleCompensition(roleIds, items, "", "").submit();
		new SysSendMail2_Re(0, tid).send(getManager());
	}
	
	private void responseFailed()
	{
		new SysSendMail2_Re(1, tid).send(getManager());
	}
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 4216;

	public int getType() {
		return 4216;
	}

	public int tid;
	public int sysid;
	public byte sys_type;
	public long receiver;
	public com.locojoy.base.Octets title;
	public com.locojoy.base.Octets context;
	public gnet.GRoleInventory attach_obj;
	public int attach_money;

	public SysSendMail2() {
		title = new com.locojoy.base.Octets();
		context = new com.locojoy.base.Octets();
		attach_obj = new gnet.GRoleInventory();
	}

	public SysSendMail2(int _tid_, int _sysid_, byte _sys_type_, long _receiver_, com.locojoy.base.Octets _title_, com.locojoy.base.Octets _context_, gnet.GRoleInventory _attach_obj_, int _attach_money_) {
		this.tid = _tid_;
		this.sysid = _sysid_;
		this.sys_type = _sys_type_;
		this.receiver = _receiver_;
		this.title = _title_;
		this.context = _context_;
		this.attach_obj = _attach_obj_;
		this.attach_money = _attach_money_;
	}

	public final boolean _validator_() {
		if (!attach_obj._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tid);
		_os_.marshal(sysid);
		_os_.marshal(sys_type);
		_os_.marshal(receiver);
		_os_.marshal(title);
		_os_.marshal(context);
		_os_.marshal(attach_obj);
		_os_.marshal(attach_money);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tid = _os_.unmarshal_int();
		sysid = _os_.unmarshal_int();
		sys_type = _os_.unmarshal_byte();
		receiver = _os_.unmarshal_long();
		title = _os_.unmarshal_Octets();
		context = _os_.unmarshal_Octets();
		attach_obj.unmarshal(_os_);
		attach_money = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SysSendMail2) {
			SysSendMail2 _o_ = (SysSendMail2)_o1_;
			if (tid != _o_.tid) return false;
			if (sysid != _o_.sysid) return false;
			if (sys_type != _o_.sys_type) return false;
			if (receiver != _o_.receiver) return false;
			if (!title.equals(_o_.title)) return false;
			if (!context.equals(_o_.context)) return false;
			if (!attach_obj.equals(_o_.attach_obj)) return false;
			if (attach_money != _o_.attach_money) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tid;
		_h_ += sysid;
		_h_ += (int)sys_type;
		_h_ += (int)receiver;
		_h_ += title.hashCode();
		_h_ += context.hashCode();
		_h_ += attach_obj.hashCode();
		_h_ += attach_money;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tid).append(",");
		_sb_.append(sysid).append(",");
		_sb_.append(sys_type).append(",");
		_sb_.append(receiver).append(",");
		_sb_.append("B").append(title.size()).append(",");
		_sb_.append("B").append(context.size()).append(",");
		_sb_.append(attach_obj).append(",");
		_sb_.append(attach_money).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

