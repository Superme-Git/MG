
package fire.pb.circletask;
import java.util.Map;





import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.mission.PAbandonScenarioMission;
import fire.pb.mission.Sbukefangqirenwu;
import fire.pb.mission.UtilHelper;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAbandonQuest__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAbandonQuest extends __CAbandonQuest__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		CircleTaskManager.logger.info("收到放弃任务消息.角色[" + roleid + "]要放弃任务:" + questid);
		if(questid == 1080000) {
			//放弃暗夜马戏团任务传说
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					RoleAnYeTask rayt = new RoleAnYeTask(roleid);
					rayt.endLegendAnYeTask();
					rayt.refreshAnYeTasks(-1);
					return true;
				}
			}.submit();
			return;
		}
		
		//该任务不可以放弃
		if(questid == 205303){
			return;
		}
		Map<Integer,Sbukefangqirenwu> map =  fire.pb.main.ConfigManager.getInstance().getConf(Sbukefangqirenwu.class);
		if(map != null){
			if(map.get(questid) != null){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 141484, null);
				return;
			}
		}
		
		if ( UtilHelper.isBranchScenarioMission( questid ) ) {
			new PAbandonScenarioMission( roleid, questid ).submit();
			return;
		}
		
		new PAbandonCircleTask(roleid, questid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807434;

	public int getType() {
		return 807434;
	}

	public int questid; // 放弃的任务id

	public CAbandonQuest() {
	}

	public CAbandonQuest(int _questid_) {
		this.questid = _questid_;
	}

	public final boolean _validator_() {
		if (questid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAbandonQuest) {
			CAbandonQuest _o_ = (CAbandonQuest)_o1_;
			if (questid != _o_.questid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAbandonQuest _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

