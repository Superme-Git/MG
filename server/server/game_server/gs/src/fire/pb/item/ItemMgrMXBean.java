package fire.pb.item;

public interface ItemMgrMXBean {
	public int addItemToPack(final long roleId, final int packid,
			final int itemid, final int number,final int availtime, final fire.log.enums.YYLoggerTuJingEnum countertype,
			final int xiangguanid );
}
