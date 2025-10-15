package fire.pb.huoban;

/**
 * 设置vip周免伙伴
 */
import mkdb.Procedure;

public class PSetVipHuoban extends Procedure {
	private final long roleid;
	
	public PSetVipHuoban(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		HuoBanColumn huobanColumn = HuoBanColumn.getHuoBanColumn(roleid, false);
		if (huobanColumn == null)
			return false;
		
		huobanColumn.sendHuoBanListOnChange();
		
		Module.logger.info(new StringBuffer().append("viprefresh: ").append("roleid:").append(roleid));
		return true;
	}

}
