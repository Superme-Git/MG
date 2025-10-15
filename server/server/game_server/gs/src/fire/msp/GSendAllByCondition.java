
package fire.msp;

import gnet.link.Onlines;

import java.util.HashSet;
import java.util.Set;

import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAllByCondition__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAllByCondition extends __GSendAllByCondition__ {
	@Override
	protected void process() {
		// protocol handle
		Set<Long> sendroleIds = new HashSet<Long>(); 
		if(sceneid != -1)
		{//优先按场景索引
			Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
			for(Role role : scene.getSceneRoles().values())
			{
				if(schoolid != -1 && role.getSchoolId() != schoolid)
					continue;
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(role.getRoleID());
			}
		}
		else if(schoolid != -1)
		{//按门派索引
			Set<Long> schoolroles = RoleManager.getInstance().getSchoolRoles(schoolid);
			if (schoolroles!=null) 
			for(long sroleId :  schoolroles)
			{
				Role role = RoleManager.getInstance().getRoleByID(sroleId);
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(sroleId);
			}
		}
		else if (camp >0) {
			Set<Long> campRoles = RoleManager.getInstance().campRoles.get(camp);
			if (campRoles != null){
				for (long rid : campRoles) {
					sendroleIds.add(rid);
				}
			}
		}
		else if (null != exceptroles) {
		    Set<Long> allRoles = RoleManager.getInstance().getRoles().keySet();
            if (allRoles != null){
                for (long sroleId : allRoles) {
                    Role role = RoleManager.getInstance().getRoleByID(sroleId);
                    if (minlevel != -1 && role.getLevel() < minlevel)
                        continue;
                    if (maxlevel != -1 && role.getLevel() > maxlevel)
                        continue;
                    sendroleIds.add(sroleId);
                }
            }
		    sendroleIds.removeAll(exceptroles);
		}
		else
		{//全遍历过滤等级
			for(Role role : RoleManager.getInstance().getRoles().values())
			{
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(role.getRoleID());
			}
		}
		
		Onlines.getInstance().sendOctets(sendroleIds, protype,protocol);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720903;

	public int getType() {
		return 720903;
	}

	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据
	public int schoolid; // 门派ID，如果无限制填-1
	public int minlevel; // 最小等级，如果无限制填-1
	public int maxlevel; // 最大等级，如果无限制填-1
	public long sceneid; // 场景ID，如果无限制填-1
	public int camp; // 阵营频道,如果无限制填-1
	public java.util.ArrayList<Long> exceptroles; // 不发送协议的role

	public GSendAllByCondition() {
		protocol = new com.locojoy.base.Octets();
		exceptroles = new java.util.ArrayList<Long>();
	}

	public GSendAllByCondition(int _protype_, com.locojoy.base.Octets _protocol_, int _schoolid_, int _minlevel_, int _maxlevel_, long _sceneid_, int _camp_, java.util.ArrayList<Long> _exceptroles_) {
		this.protype = _protype_;
		this.protocol = _protocol_;
		this.schoolid = _schoolid_;
		this.minlevel = _minlevel_;
		this.maxlevel = _maxlevel_;
		this.sceneid = _sceneid_;
		this.camp = _camp_;
		this.exceptroles = _exceptroles_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(protype);
		_os_.marshal(protocol);
		_os_.marshal(schoolid);
		_os_.marshal(minlevel);
		_os_.marshal(maxlevel);
		_os_.marshal(sceneid);
		_os_.marshal(camp);
		_os_.compact_uint32(exceptroles.size());
		for (Long _v_ : exceptroles) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		schoolid = _os_.unmarshal_int();
		minlevel = _os_.unmarshal_int();
		maxlevel = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		camp = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			exceptroles.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAllByCondition) {
			GSendAllByCondition _o_ = (GSendAllByCondition)_o1_;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			if (schoolid != _o_.schoolid) return false;
			if (minlevel != _o_.minlevel) return false;
			if (maxlevel != _o_.maxlevel) return false;
			if (sceneid != _o_.sceneid) return false;
			if (camp != _o_.camp) return false;
			if (!exceptroles.equals(_o_.exceptroles)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += protype;
		_h_ += protocol.hashCode();
		_h_ += schoolid;
		_h_ += minlevel;
		_h_ += maxlevel;
		_h_ += (int)sceneid;
		_h_ += camp;
		_h_ += exceptroles.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(schoolid).append(",");
		_sb_.append(minlevel).append(",");
		_sb_.append(maxlevel).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(exceptroles).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

