package fire.pb.event;
/**
 * 饰品大师之路任务事件
 *
 */
public class ForgeDecorationEvent implements Event {
	private final long roleid;
	public ForgeDecorationEvent( final long roleid ) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() { return roleid; }
}
