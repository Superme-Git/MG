package fire.pb;

import java.util.HashMap;
import java.util.Map;

import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;

/**
 * 请求人物信息主界面
 * @author nobody
 *
 */
public class PReqRoleInfoProc extends mkdb.Procedure{
	
	private final long roleId;
	private final int reqkey;
	public PReqRoleInfoProc(long roleId){
		this.roleId = roleId;
		this.reqkey = 1;
	}
	
	public PReqRoleInfoProc(final long roleid, final int reqkey) {
		this.roleId = roleid;
		this.reqkey = reqkey;
	}

	@Override
	protected boolean process() throws Exception {
		
		fire.pb.buff.BuffRoleImpl rolebuff = new fire.pb.buff.BuffRoleImpl(roleId, true );
		Map<Integer, Long> hpmpMap = new HashMap<Integer, Long>();
		ConstantlyBuff hp_temp = rolebuff.getBuff(BuffConstant.ROLE_RECOVER_HP);
		if(hp_temp != null){
			hpmpMap.put(BuffConstant.ROLE_RECOVER_HP, hp_temp.getAmount());
		}
		else {
			hpmpMap.put(BuffConstant.ROLE_RECOVER_HP, 0L);
		}
		ConstantlyBuff mp_temp = rolebuff.getBuff(BuffConstant.ROLE_RECOVER_MP);
		if(mp_temp != null){
			hpmpMap.put(BuffConstant.ROLE_RECOVER_MP, mp_temp.getAmount());
		}
		else {
			hpmpMap.put(BuffConstant.ROLE_RECOVER_MP, 0L);
		}
		
		int wuxun = 0;
		long honour = 0;
		
		SRspRoleInfo send = new SRspRoleInfo();
		send.hpmpstore.putAll(hpmpMap);
		send.wencaivalue = 0;
		send.wuxun = wuxun;
		send.honour = honour;
		send.reqkey = reqkey;
		mkdb.Procedure.psendWhileCommit(roleId, send);
		return true;
	}
}