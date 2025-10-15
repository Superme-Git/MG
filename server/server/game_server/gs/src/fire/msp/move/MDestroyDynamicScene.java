
package fire.msp.move;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MDestroyDynamicScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MDestroyDynamicScene extends __MDestroyDynamicScene__ {
	@Override
	protected void process() {
		// protocol handle
		if (scenetype == fire.pb.move.DynamicSceneType.DREAMLAND_SCENE) {
			for (Long roleid : roleids) 
			if (fire.pb.mission.Module.getInstance().hasUnfinishedFairylandMission(roleid)>0) {
				     //副本销毁时强行跳转的目的地肯定不是梦境,要通知客户端
					fire.pb.mission.SFairylandStatus sds = new fire.pb.mission.SFairylandStatus();
					sds.status = fire.pb.mission.FairylandStatus.LANDABSENT;
					if (mkdb.Transaction.current()==null) 
					    gnet.link.Onlines.getInstance().send(roleid, sds);
					else 
						mkdb.Procedure.psendWhileCommit(roleid, sds);
				
			}else {//没有梦境任务
				fire.pb.mission.SFairylandStatus sds = new fire.pb.mission.SFairylandStatus();
				sds.status = fire.pb.mission.FairylandStatus.LANDOUT;
				if (mkdb.Transaction.current()==null) 
				    gnet.link.Onlines.getInstance().send(roleid, sds);
				else 
					mkdb.Procedure.psendWhileCommit(roleid, sds);
			}
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724913;

	public int getType() {
		return 724913;
	}

	public long sceneid; // 场景模块创建的场景ID
	public long ownerid; // 副本的所有者ID(可能是角色ID,也可能是团体ID)
	public int scenetype; // 副本场景的类型，如果为0，表示为静态场景
	public java.util.LinkedList<Long> roleids; // 副本销毁时被强行踢出副本的roleid

	public MDestroyDynamicScene() {
		roleids = new java.util.LinkedList<Long>();
	}

	public MDestroyDynamicScene(long _sceneid_, long _ownerid_, int _scenetype_, java.util.LinkedList<Long> _roleids_) {
		this.sceneid = _sceneid_;
		this.ownerid = _ownerid_;
		this.scenetype = _scenetype_;
		this.roleids = _roleids_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sceneid);
		_os_.marshal(ownerid);
		_os_.marshal(scenetype);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sceneid = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		scenetype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MDestroyDynamicScene) {
			MDestroyDynamicScene _o_ = (MDestroyDynamicScene)_o1_;
			if (sceneid != _o_.sceneid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (scenetype != _o_.scenetype) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sceneid;
		_h_ += (int)ownerid;
		_h_ += scenetype;
		_h_ += roleids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sceneid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(scenetype).append(",");
		_sb_.append(roleids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

