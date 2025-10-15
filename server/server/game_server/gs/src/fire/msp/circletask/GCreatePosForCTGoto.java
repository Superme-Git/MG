
package fire.msp.circletask;




import fire.pb.circletask.CircTaskGotoType;
import fire.pb.clan.ClanUtils;
import fire.pb.move.DynamicSceneType;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.MapUtil;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneFactory;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.talk.MessageMgr;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreatePosForCTGoto__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreatePosForCTGoto extends __GCreatePosForCTGoto__ {
	@Override
	protected void process() {
		GridPos gridPos = null;
		if (gototype == CircTaskGotoType.CircTask_GotoGuild) {
			xbean.ClanInfo factionInfo = ClanUtils.getClanInfoById(roleid, true);
			if (factionInfo == null) {
				MessageMgr.sendMsgNotify(roleid, 145077, null);
				return;
			}
			
			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(mapid, factionInfo.getKey());
			if (ds == null) {
				ds = SceneFactory.createDynamicScene(mapid, factionInfo.getKey(), factionInfo.getClanname(), -1, DynamicSceneType.NORMAL_SCENE, null);
//				DuplicateHelper.createDynamicScene(mapid, factionInfo.getKey(), "",
//						true, -1, DynamicSceneType.NORMAL_SCENE, null);
			}
//			ds = SceneManager.getInstance().getSceneByMapAndOwner(mapid, factionInfo.getKey());
			
			gridPos = MapUtil.genPosInMap(ds.getSceneID());
		}
		else {
			gridPos = MapUtil.genPosInMap(mapid);
		}
			
		MCreatePosForCTGoto mcp = new MCreatePosForCTGoto();
		mcp.mapid = mapid;
		mcp.roleid = roleid;
		mcp.npckey = npckey;
		mcp.posx = gridPos.getX();
		mcp.posy = gridPos.getY();
		mcp.ctconfid = ctconfid;
		mcp.dstnpcid = dstnpcid;
		mcp.cttype = cttype;
		mcp.isnpc = isnpc;
		SceneClient.pSend(mcp);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 741907;

	public int getType() {
		return 741907;
	}

	public long roleid;
	public long npckey;
	public int mapid;
	public int ctconfid; // 任务id
	public int gototype;
	public int dstnpcid;
	public int cttype; // 循环类型
	public int isnpc;

	public GCreatePosForCTGoto() {
	}

	public GCreatePosForCTGoto(long _roleid_, long _npckey_, int _mapid_, int _ctconfid_, int _gototype_, int _dstnpcid_, int _cttype_, int _isnpc_) {
		this.roleid = _roleid_;
		this.npckey = _npckey_;
		this.mapid = _mapid_;
		this.ctconfid = _ctconfid_;
		this.gototype = _gototype_;
		this.dstnpcid = _dstnpcid_;
		this.cttype = _cttype_;
		this.isnpc = _isnpc_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(npckey);
		_os_.marshal(mapid);
		_os_.marshal(ctconfid);
		_os_.marshal(gototype);
		_os_.marshal(dstnpcid);
		_os_.marshal(cttype);
		_os_.marshal(isnpc);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npckey = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		ctconfid = _os_.unmarshal_int();
		gototype = _os_.unmarshal_int();
		dstnpcid = _os_.unmarshal_int();
		cttype = _os_.unmarshal_int();
		isnpc = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreatePosForCTGoto) {
			GCreatePosForCTGoto _o_ = (GCreatePosForCTGoto)_o1_;
			if (roleid != _o_.roleid) return false;
			if (npckey != _o_.npckey) return false;
			if (mapid != _o_.mapid) return false;
			if (ctconfid != _o_.ctconfid) return false;
			if (gototype != _o_.gototype) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (cttype != _o_.cttype) return false;
			if (isnpc != _o_.isnpc) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)npckey;
		_h_ += mapid;
		_h_ += ctconfid;
		_h_ += gototype;
		_h_ += dstnpcid;
		_h_ += cttype;
		_h_ += isnpc;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(ctconfid).append(",");
		_sb_.append(gototype).append(",");
		_sb_.append(dstnpcid).append(",");
		_sb_.append(cttype).append(",");
		_sb_.append(isnpc).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GCreatePosForCTGoto _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = ctconfid - _o_.ctconfid;
		if (0 != _c_) return _c_;
		_c_ = gototype - _o_.gototype;
		if (0 != _c_) return _c_;
		_c_ = dstnpcid - _o_.dstnpcid;
		if (0 != _c_) return _c_;
		_c_ = cttype - _o_.cttype;
		if (0 != _c_) return _c_;
		_c_ = isnpc - _o_.isnpc;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

