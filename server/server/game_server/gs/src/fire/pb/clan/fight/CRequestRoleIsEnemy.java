
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRoleIsEnemy__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestRoleIsEnemy extends __CRequestRoleIsEnemy__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure requestroleisenemy = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				SRequestRoleIsEnemy msg = new SRequestRoleIsEnemy(); 
				
				Long chanid = xtable.Roleidclan.select(roleid);
				if (chanid == null)
				{
					psendWhileCommit(roleid, msg);
					return true;
				}
				
				for (Long e : roleidlist)
				{
					if (e != null)
					{
						Long c = xtable.Roleidclan.select(e);
						if (c != null)
						{
							if (!c.equals(chanid))
								msg.rolelist.put(e, 2); //敌对的 by changhao
							else
								msg.rolelist.put(e, 1);
						}
						else
						{
							msg.rolelist.put(e, -1);
						}
					}
				}
				
				psendWhileCommit(roleid, msg);				
				
				return true;
			}
		};
		
		requestroleisenemy.submit();			
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808541;

	public int getType() {
		return 808541;
	}

	public java.util.LinkedList<Long> roleidlist; // 角色list  by changhao

	public CRequestRoleIsEnemy() {
		roleidlist = new java.util.LinkedList<Long>();
	}

	public CRequestRoleIsEnemy(java.util.LinkedList<Long> _roleidlist_) {
		this.roleidlist = _roleidlist_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roleidlist.size());
		for (Long _v_ : roleidlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleidlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRoleIsEnemy) {
			CRequestRoleIsEnemy _o_ = (CRequestRoleIsEnemy)_o1_;
			if (!roleidlist.equals(_o_.roleidlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roleidlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleidlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

