package fire.pb.scene;

import java.util.HashSet;
import java.util.Set;

public class MapProtocol
{
	public Long roleId = null;
	public Set<Long> roleIds = null;
	public final mkio.Protocol p;
	
	public MapProtocol( long roleId, mkio.Protocol p)
	{
		this.roleId = roleId;
		this.p = p;
	}
	public MapProtocol( Set<Long> roleIds, mkio.Protocol p)
	{
		this.roleIds = new HashSet<Long>();
		this.roleIds.addAll(roleIds);
		this.p = p;
	}
	
}
