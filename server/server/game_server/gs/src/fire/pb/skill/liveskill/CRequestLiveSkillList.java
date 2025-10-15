
package fire.pb.skill.liveskill;
import fire.pb.skill.SkillRole;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestLiveSkillList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求生活链表
 * @author changhao
 *
 */
public class CRequestLiveSkillList extends __CRequestLiveSkillList__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestlearnliveskill = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{		
				SRequestLiveSkillList msg = new SRequestLiveSkillList();
			
				SkillRole skillrole = new SkillRole(roleid);	
				
				for(java.util.Map.Entry<Integer, xbean.LiveSkill> e : skillrole.getLiveSkills().entrySet())
				{
					fire.pb.skill.liveskill.LiveSkill skill = new fire.pb.skill.liveskill.LiveSkill();
					skill.level = e.getValue().getLevel();
					skill.id = e.getKey();
					msg.skilllist.add(skill);
				}
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);				
				
				return true;
			}
		};
		
		requestlearnliveskill.submit();	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800513;

	public int getType() {
		return 800513;
	}


	public CRequestLiveSkillList() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestLiveSkillList) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestLiveSkillList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

