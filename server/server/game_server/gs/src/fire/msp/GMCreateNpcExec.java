package fire.msp;

/**
 * GS按需通知地图模块创建NPC的回调接口
 *
 */
public interface GMCreateNpcExec {
	public boolean exec(final int result, final java.util.List<fire.msp.npc.NpcInfo> npclist);
}
