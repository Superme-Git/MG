
package fire.msp.npc;

import java.util.Map;

import fire.pb.PropRole;
import fire.pb.battle.QCRoleInfoDes;
import fire.pb.battle.QCRoleInfoWatchDes;
import fire.pb.battle.QCmodelType;
import fire.pb.battle.SPlayPKFightView;

import fire.pb.team.Team;
import fire.pb.team.TeamManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCheckCanPlayPKView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCheckCanPlayPKView extends __MCheckCanPlayPKView__ {
	@Override
	protected void process() {
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				SPlayPKFightView sPlayPKFightView=new SPlayPKFightView();
				sPlayPKFightView.levelindex=levelindex;
				sPlayPKFightView.modeltype=modeltype;
				sPlayPKFightView.school=school;
				
				if(modeltype==QCmodelType.WATCH_FIGHT){
					for (Map.Entry<Long, Long> entry : watchid.entrySet()){
						long oneid=entry.getKey();
						long otherid=entry.getValue();
						QCRoleInfoDes qCRoleInfoDes1 = createQCRoleInfoDes(oneid);
						QCRoleInfoDes qCRoleInfoDes2 = createQCRoleInfoDes(otherid);
						QCRoleInfoWatchDes qcRoleInfoWatchDes=new QCRoleInfoWatchDes();
						qcRoleInfoWatchDes.role1=qCRoleInfoDes1;
						qcRoleInfoWatchDes.role2=qCRoleInfoDes2;
						sPlayPKFightView.rolewatchlist.add(qcRoleInfoWatchDes);
					}
				}else{
					for(Long id:rolelistid){
						QCRoleInfoDes qCRoleInfoDes = createQCRoleInfoDes(id);
						sPlayPKFightView.rolelist.add(qCRoleInfoDes);
					}
				}
				
				mkdb.Procedure.psendWhileCommit(hostid, sPlayPKFightView);
				return true;
			}
			
		};
		proc.submit();
	}
	
	private QCRoleInfoDes createQCRoleInfoDes(long id){
		PropRole propRole = new PropRole(id, true);
		QCRoleInfoDes qCRoleInfoDes = new QCRoleInfoDes();
		qCRoleInfoDes.roleid=propRole.getRoleId();//角色Id
		qCRoleInfoDes.rolename=propRole.getName();//角色名
		qCRoleInfoDes.shape=propRole.getShape();//角色造型
		qCRoleInfoDes.level=propRole.getLevel();//角色等级
		qCRoleInfoDes.school=propRole.getSchool();//门派
		Team team = TeamManager.selectTeamByRoleId(id);
		if(team!=null){
			//这里队伍标志需要考虑 暂离队伍的玩家在切磋
			//过滤掉非队长成员
			if(team.getTeamLeaderId()!=id){
				qCRoleInfoDes.teamnum=0;
				qCRoleInfoDes.teamnummax=0;
			}else{
				qCRoleInfoDes.teamnum=team.getNormalMemberIds().size();//队伍当前人数
				qCRoleInfoDes.teamnummax=TeamManager.MAX_MEMBER_COUNT+1;//队伍最大人数
			}
		}else{
			qCRoleInfoDes.teamnum=0;
			qCRoleInfoDes.teamnummax=0;
		}
		return qCRoleInfoDes;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730440;

	public int getType() {
		return 730440;
	}

	public long hostid;
	public int modeltype; // 模块类型
	public int school; // 删选的职业 -1表示全职业
	public int levelindex; // 删选的角色等级区间 0表示全选
	public java.util.LinkedList<Long> rolelistid; // 角色列表
	public java.util.HashMap<Long,Long> watchid; // 观看角色列表

	public MCheckCanPlayPKView() {
		rolelistid = new java.util.LinkedList<Long>();
		watchid = new java.util.HashMap<Long,Long>();
	}

	public MCheckCanPlayPKView(long _hostid_, int _modeltype_, int _school_, int _levelindex_, java.util.LinkedList<Long> _rolelistid_, java.util.HashMap<Long,Long> _watchid_) {
		this.hostid = _hostid_;
		this.modeltype = _modeltype_;
		this.school = _school_;
		this.levelindex = _levelindex_;
		this.rolelistid = _rolelistid_;
		this.watchid = _watchid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostid);
		_os_.marshal(modeltype);
		_os_.marshal(school);
		_os_.marshal(levelindex);
		_os_.compact_uint32(rolelistid.size());
		for (Long _v_ : rolelistid) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(watchid.size());
		for (java.util.Map.Entry<Long, Long> _e_ : watchid.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostid = _os_.unmarshal_long();
		modeltype = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		levelindex = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			rolelistid.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			long _v_;
			_v_ = _os_.unmarshal_long();
			watchid.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCheckCanPlayPKView) {
			MCheckCanPlayPKView _o_ = (MCheckCanPlayPKView)_o1_;
			if (hostid != _o_.hostid) return false;
			if (modeltype != _o_.modeltype) return false;
			if (school != _o_.school) return false;
			if (levelindex != _o_.levelindex) return false;
			if (!rolelistid.equals(_o_.rolelistid)) return false;
			if (!watchid.equals(_o_.watchid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostid;
		_h_ += modeltype;
		_h_ += school;
		_h_ += levelindex;
		_h_ += rolelistid.hashCode();
		_h_ += watchid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostid).append(",");
		_sb_.append(modeltype).append(",");
		_sb_.append(school).append(",");
		_sb_.append(levelindex).append(",");
		_sb_.append(rolelistid).append(",");
		_sb_.append(watchid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

