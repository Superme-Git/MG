
package fire.pb;

import java.util.List;

import fire.pb.map.GridPos;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.util.MapUtil;
import fire.pb.util.MessageUtil;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRolePos__ extends mkio.Protocol { }

/** 客户端使用千里寻踪蝶请求玩家坐标
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestRolePos extends __CRequestRolePos__ {
	@Override
	protected void process() {
		final long findroleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(findroleid < 0) {
			return;
		}
		new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
				if (searchtype == BYID){
					return findRoleByID(roleid);
				}
				if (searchtype == BYNAME) {
					Long rid = xtable.Rolename2key.select(rolename);
					if (rid!=null){
						return findRoleByID(rid);
					}else {
						fire.pb.talk.MessageMgr.psendMsgNotify(findroleid, 142707, null);
						return false;
					}
				}
				return true;
			}

			private boolean findRoleByID(Long rid) {
				if (xtable.Properties.select(rid)==null){
					fire.pb.talk.MessageMgr.psendMsgNotify(findroleid, 142707, null);
					return false;
				}
				if (!StateCommon.isOnline(rid)) {
					fire.pb.talk.MessageMgr.psendMsgNotify(findroleid, 142708, null);
					return false;
				}
				Role role =fire.pb.map.RoleManager.getInstance().getRoleByID(rid);
				if (role == null)
					return false;
				int basemapid = MapUtil.getBaseMapIdBySceneId(role.getScene());
				MapConfig conf =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(basemapid);
				if (conf!=null){
					String mapName = conf.getMapName();
					GridPos gridPos = role.getPos().toGridPos();
					PropRole pRole = new PropRole(rid, true);
					String roleName = pRole.getName();
					List<String> paras = MessageUtil.getMsgParaList(roleName,mapName,String.valueOf(gridPos.getX()),String.valueOf(gridPos.getY()));
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(findroleid, 142709, paras);
				    //扣掉物品
					fire.pb.item.Pack bag = new fire.pb.item.Pack(findroleid, false);
					int realSub = bag.removeItemById(36253, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_qianlixunzong, 36253, "use qianlixunzongdie",true);
				    if (realSub!=1)
				    	return false;
				}
				return true;
			}
			
		}.submit();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786487;

	public int getType() {
		return 786487;
	}

	public final static int BYNAME = 1; // 按名字查找
	public final static int BYID = 2; // 按id查找

	public long roleid; // 玩家id
	public java.lang.String rolename; // 玩家名
	public byte searchtype; // 查找类型

	public CRequestRolePos() {
		rolename = "";
	}

	public CRequestRolePos(long _roleid_, java.lang.String _rolename_, byte _searchtype_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.searchtype = _searchtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(searchtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		searchtype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRolePos) {
			CRequestRolePos _o_ = (CRequestRolePos)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (searchtype != _o_.searchtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += (int)searchtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(searchtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

