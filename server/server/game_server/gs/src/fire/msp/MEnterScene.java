package fire.msp;

import fire.pb.activity.impexam.ImpExamManager;
import fire.pb.activity.winner.WinnerManager;
import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp3.PvP3Control;
import fire.pb.battle.pvp3.PvP3Helper;
import fire.pb.battle.pvp5.PvP5Control;
import fire.pb.battle.pvp5.PvP5Helper;
import fire.pb.huoban.HuoBanColumn;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.notify.GiftBagMgr;
import fire.pb.mission.notify.TuiSongNotifyManager;
import fire.pb.move.SRoleEnterScene;
import fire.pb.util.DateValidate;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MEnterScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
/**
 * 人物上线，场景处理完毕后向逻辑发此消息
 * 
 */
public class MEnterScene extends __MEnterScene__ {
	@Override
	protected void process() {

		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role == null)
		{
			role = RoleManager.getInstance().createRole(roleid,sceneid, posx, posy);
		}
		
		//人物上线，队伍相关（更新角色队伍信息），因为逻辑队伍的信息要依赖地图队伍信息，所以在其之后处理，因为其中要锁队伍锁，所以异步处理
		new fire.pb.team.PRoleOnline(roleid).submit();
		
		
		//副本处理
		new fire.pb.instancezone.PRoleOnline(roleid,sceneid).submit();
		
//		new fire.pb.mission.instance.PRoleOnline(roleid, sceneid).submit();
		
		//小地图上发送动态生成的npc
		fire.pb.timer.AbstractScheduledActivity.sendActivityNpcToMiniMap(sceneid, roleid);
		
		//当前处于投票中   角色没有投票过 角色等级大于30级别
		xbean.Properties prop =  xtable.Properties.select(roleid);
		Integer curlevel = prop.getLevel();

		//同步huobansize到Role
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				HuoBanColumn huobanColumn = HuoBanColumn.getHuoBanColumn(roleid, false);
				final Role rolescene = RoleManager.getInstance().getRoleByID(roleid);
				rolescene.setHuobanSize(huobanColumn.getFightHuobanKeys().size());
				return true;
			}
			
		}.submit();

		
		fire.pb.mission.Module.getInstance().enterWorldOK(role.getRoleID());
		//智力试练检测
		if(ImpExamManager.getInstance().isInImpExamTime() != -1 && curlevel>=20){
			ImpExamManager.getInstance().roleLoginCheck(roleid);
		}
		
		fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().roleLoginCheck(roleid, prop.getLevel());
		fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().roleLoginCheck(roleid, prop.getLevel());

		if (curlevel >= PvP1Helper.ENTER_MIN_LEVEL && (PvP1Control.getInstance().isActivityTime() || PvP1Helper.isPvPMap((int) sceneid))) {
			// 1v1
			PvP1Control.getInstance().onRoleOnline(roleid, (int) sceneid);
		} else if (curlevel >= PvP3Helper.ENTER_MIN_LEVEL && (PvP3Control.getInstance().isActivityTime() || PvP3Helper.isPvPMap((int) sceneid))) {
			// 3v3
			PvP3Control.getInstance().onRoleOnline(roleid, (int) sceneid);
		} else if (curlevel >= PvP5Helper.ENTER_MIN_LEVEL && (PvP5Control.getInstance().isActivityTime() || PvP5Helper.isPvPMap((int) sceneid))) {
			// 5v5
			PvP5Control.getInstance().onRoleOnline(roleid, (int) sceneid);
		}
		
		if (BingFengLandMgr.bingFengMaps.contains((int)sceneid)) {
			if (!DateValidate.inTheSameDay(prop.getOfflinetime(), prop.getOnlinetime())) {
				Transfer.justGoto(roleid, 1615, 49, 89, SRoleEnterScene.CHEFU);
			}else
			    BingFengLandMgr.getInstance().sendAfterEnterBingFengLand(roleid, true);
		}
		
		//上线提醒推送
		TuiSongNotifyManager.getInstance().roleLogin(roleid);
		
		//礼包提醒
		GiftBagMgr.getInstance().roleLogin(roleid, curlevel);
		
		//冠军试炼上线拉人
		//这里处理一下冠军试炼的上线拉人
		if (curlevel >= WinnerManager.MIN_LEVEL) {
			WinnerManager.getInstance().sendWinnerCallPlayer(roleid);
		}
		
		new fire.pb.clan.fight.PRoleOnline(roleid).submit(); //工会战角色上线 by changhao	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720904;

	public int getType() {
		return 720904;
	}

	public long roleid; // 角色ID
	public long sceneid;
	public int posx;
	public int posy;

	public MEnterScene() {
	}

	public MEnterScene(long _roleid_, long _sceneid_, int _posx_, int _posy_) {
		this.roleid = _roleid_;
		this.sceneid = _sceneid_;
		this.posx = _posx_;
		this.posy = _posy_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MEnterScene) {
			MEnterScene _o_ = (MEnterScene)_o1_;
			if (roleid != _o_.roleid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)sceneid;
		_h_ += posx;
		_h_ += posy;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MEnterScene _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = posx - _o_.posx;
		if (0 != _c_) return _c_;
		_c_ = posy - _o_.posy;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

