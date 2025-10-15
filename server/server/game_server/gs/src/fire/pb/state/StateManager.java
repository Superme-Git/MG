package fire.pb.state;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class StateManager
{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	
	public static IState getStateByRoleId(final long roleId)
	{
		Integer state =xtable.Roleonoffstate.get(roleId);
		if(state == null)
		{
			state = State.UNENTRY_STATE;
			xtable.Roleonoffstate.add(roleId, state);
		}
		switch(state)
		{
		case State.UNENTRY_STATE:// 未登录状态
			return new UnEntryState(roleId);
		case State.PRE_ENTRY_STATE: // 准备登录状态
			return new PreEntryState(roleId);
		case State.ENTRY_STATE: // 已登录状态
			return new EntryState(roleId);
		case State.PRE_OFFLINE_PROTECT_STATE: // 准备下线保护状态
			return new PreOfflineProtectState(roleId);
		case State.OFFLINE_PROTECT_STATE: // 下线保护状态
			return new OfflineProtectState(roleId);
		case State.BREAK_OFFLINE_PROTECT_STATE: // 中断下线保护状态
			return new BreakOfflineProtectState(roleId);
		case State.END_OFFLINE_PROTECT_STATE: // 结束下线保护状态
			return new EndOfflineProtectState(roleId);
		case State.PRE_TRUSTEESHIP_STATE: // 准备战斗托管状态
			return new PreTrusteeShipState(roleId);
		case State.TRUSTEESHIP_STATE: // 战斗托管状态
			return new TrusteeShipState(roleId);
		case State.BREAK_TRUSTEESHIP_STATE: // 中断战斗托管状态
			return new BreakTrusteeShipState(roleId);
		default:
			throw new IllegalStateException("错误的状态类型："+state);
		}
	}
	
	
	public static int getStateIdByRoleId(final long roleId)
	{
		Integer state =xtable.Roleonoffstate.get(roleId);
		if(state == null)
			return State.UNENTRY_STATE;
		return state;
	}
	
	
	public static int selectStateIdByRoleId(final long roleId)
	{
		Integer state =xtable.Roleonoffstate.select(roleId);
		if(state == null)
			return State.UNENTRY_STATE;
		return state;
	}
	
	
	public static void serverShutdown()
	{
		final List<Long> allroleids = new LinkedList<Long>();
		try
		{
			new mkdb.Procedure()
			{
				protected boolean process() throws Exception {
					mkdb.TTableCache<Long, xbean.OfflineFuture> btable = xtable.Offlinefuture.getCache();
					btable.walk(new mkdb.CacheQuery<Long, xbean.OfflineFuture>()
					{
						@Override
						public void onQuery(Long roleId, xbean.OfflineFuture futurebean)
						{
							if (futurebean != null && futurebean.getTimefuture() != null)
							{
								futurebean.getTimefuture().cancel(true);
							}
							allroleids.add(roleId);
						}

					});
					return true;
				};
			}.submit().get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		for(long roleId : allroleids)
		{
			try
			{
				new PEndOfflineProtect(roleId).submit().get();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
