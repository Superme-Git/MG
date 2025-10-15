
package fire.pb.fushi.outdate;

public class PAddFuShiRecord extends PFuShiRecord {

	/**
	 * @param roleId
	 * @param num
	 */
	public PAddFuShiRecord(long roleId, int num) {
		super(roleId, num);
	}

	@Override
	protected boolean process() throws Exception {
		FuShiReocrdRole role = new FuShiReocrdRole(roleId);
		role.addRecord(num);
		return true;
	}

}
