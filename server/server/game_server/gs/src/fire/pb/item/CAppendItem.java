package fire.pb.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import fire.pb.util.MessageUtil;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAppendItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAppendItem extends __CAppendItem__ {

	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		if (ItemUseFilter.doFilter(roleId, keyinpack))
			return;
		Pack bag = new Pack(roleId, true);
		ItemBase bi = bag.getItem(keyinpack);
		if (bi == null)
			return;
		if (!bi.canUse(roleId, idtype, id)) {
			fire.pb.scene.Scene.LOG.error(bi.getClass().toString() + " "
					+ bi.getItemId() + " can't use!");
			return;
		}
		if (bi instanceof GroceryItem) {
			GroceryItemShuXing itemAttr = (GroceryItemShuXing) bi.getItemAttr();
			String effectDate = itemAttr.getEffectdate();
			String overDate = itemAttr.getOverduedate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if (effectDate.length() > 1) {
					Date startDate = sdf.parse(effectDate);
					if (startDate.getTime() > System.currentTimeMillis()) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(startDate);
						int year = cal.get(Calendar.YEAR);
						int month = cal.get(Calendar.MONTH) + 1;
						int day = cal.get(Calendar.DAY_OF_MONTH);
						List<String> paras = MessageUtil.getMsgParaList(
								String.valueOf(year), String.valueOf(month),
								String.valueOf(day));
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145492, 0,
								paras);
						return;
					}
				}

				if (overDate.length() > 1) {
					Date over = sdf.parse(overDate);
					if (over.getTime() < System.currentTimeMillis()) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(over);
						int year = cal.get(Calendar.YEAR);
						int month = cal.get(Calendar.MONTH) + 1;
						int day = cal.get(Calendar.DAY_OF_MONTH);
						List<String> paras = MessageUtil.getMsgParaList(
								String.valueOf(year), String.valueOf(month),
								String.valueOf(day));
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146182,
								paras);
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		switch (idtype) {
		case IDType.ITEM:
			new PUseItemToItem(roleId, keyinpack, 1, (int) (id >> 32),
					(int) (id & 0xffffffff)).submit();
			break;
		case IDType.PET:
			new PUseItemToPet(roleId, keyinpack, 1, (int) id).submit();
			break;
		case IDType.ROLE:
			new PUseItemToRole(roleId, keyinpack, 1, id).submit();
			break;
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787455;

	public int getType() {
		return 787455;
	}

	public int keyinpack;
	public int idtype;
	public long id;

	public CAppendItem() {
	}

	public CAppendItem(int _keyinpack_, int _idtype_, long _id_) {
		this.keyinpack = _keyinpack_;
		this.idtype = _idtype_;
		this.id = _id_;
	}

	public final boolean _validator_() {
		if (keyinpack < 1) return false;
		if (id < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(keyinpack);
		_os_.marshal(idtype);
		_os_.marshal(id);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		idtype = _os_.unmarshal_int();
		id = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAppendItem) {
			CAppendItem _o_ = (CAppendItem)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (idtype != _o_.idtype) return false;
			if (id != _o_.id) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += idtype;
		_h_ += (int)id;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(idtype).append(",");
		_sb_.append(id).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAppendItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = idtype - _o_.idtype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
