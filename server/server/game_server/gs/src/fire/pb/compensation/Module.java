package fire.pb.compensation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

public class Module implements ModuleInterface 
{
	public static final Logger logger = Logger.getLogger("MAIL");

	/**
	 * 群发邮件的key
	 * key = compenseKey, value = roleIds
	 */
	public static final Map<Long, Set<Long>> giveKeys = new HashMap<Long, Set<Long>>();
	public static Map<Long, Set<Long>> getGiveKeyRecord() {
		Map<Long, Set<Long>> keys = new HashMap<Long, Set<Long>>();
		synchronized (giveKeys) {
			for (Map.Entry<Long, Set<Long>> e : giveKeys.entrySet()) {
				keys.put(e.getKey(), e.getValue());
			}
		}
		return keys;
	}
	public static void addGiveKeyRecord(long key, Collection<Long> roleIds) {
		synchronized (giveKeys) {
			giveKeys.put(key, new HashSet<Long>(roleIds));
		}
	}
	public static void delGiveKeyRecord(long key) {
		synchronized (giveKeys) {
			giveKeys.remove(key);
		}
	}
	public static void clearGiveKeyRecord() {
		synchronized (giveKeys) {
			giveKeys.clear();
		}
	}
	public static void addGiveRoleId(long key, long roleId) {
		synchronized (giveKeys) {
			Set<Long> roleIds = giveKeys.get(key);
			if (roleIds == null) {
				roleIds = new HashSet<Long>();
				giveKeys.put(key, roleIds);
			}
			roleIds.add(roleId);
		}
	}

	@Override
	public void exit()
	{
		
	}

	@Override
	public void init() throws Exception
	{
		clearSingleCompensation();
	}

	private void clearSingleCompensation() throws InterruptedException, ExecutionException
	{
		clearGiveKeyRecord();
		final Set<Long> deletecompens = new HashSet<Long>();
		final Map<Long, Set<Long>> roledeletes = new HashMap<Long, Set<Long>>();
		final long now = System.currentTimeMillis();
		final long MONTH = 30l * 24l * 60l * 60l * 1000l;
		new mkdb.Procedure()
		{
			protected boolean process() throws Exception 
			{
				xtable.Singlecompensations.getTable().walk( new mkdb.TTable.IWalk<Long, xbean.SingleCompensation>() {
					@Override
					public boolean onRecord(final Long key, final xbean.SingleCompensation value ) {

						if(value.getTime() + MONTH > now)
						{
							if (value.getGivetype() == Helper.GIVETYPE_CONDITION)
							{
								Set<Long> roleIds = new HashSet<Long>();
								roleIds.addAll(value.getRoleidsofget());
								roleIds.addAll(value.getRoleidsofexcept());
								addGiveKeyRecord(key, roleIds);
							}
							return true;
						}
						
						deletecompens.add(key);
						
						for(long roleId :  value.getRoleids())
						{
							Set<Long> roledelete = roledeletes.get(roleId);
							if(roledelete == null)
							{
								roledelete = new HashSet<Long>();
								roledeletes.put(roleId, roledelete);
							}
							roledelete.add(key);
						}
						
						return true;
					}
				});
				return true;
			};
		}.submit().get();
		
		//清理Singlecompensations表
		new mkdb.Procedure()
		{
			protected boolean process() throws Exception 
			{
				lock(mkdb.Lockeys.get(xtable.Locks.SINGLECOMPENSATIONS, deletecompens));
				for(long deleteid : deletecompens)
					xtable.Singlecompensations.remove(deleteid);
				return true;
			};
		}.submit();
		
		//清理compensationroles表
		new mkdb.Procedure()
		{
			protected boolean process() throws Exception 
			{
				lock(mkdb.Lockeys.get(xtable.Locks.COMPENSATIONROLES, roledeletes.keySet()));
				for(Map.Entry<Long, Set<Long>> entry : roledeletes.entrySet())
				{
					xbean.CompensationRole xrole = xtable.Compensationroles.get(entry.getKey());
					if(xrole == null)
						continue;
					for(long deleteid : entry.getValue())
						xrole.getSinglecompensations().remove(deleteid);
				}
				return true;
			};
		}.submit();
	
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		CompensationManager.getInstance().init();//补偿表
		return new ReloadResult(true);
	}

}
