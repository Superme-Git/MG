package robot.zhuogui;

import fire.pb.move.NpcBasic;

/**
 * npc 数据
 * 
 * @author yt
 */
public class NpcData {// extends NpcBasic{

	public NpcData(long npckey, int serviceid) {
		super();
		this.npckey = npckey;
		this.serviceid = serviceid;
	}

	public long npckey; // npckey为npc的唯一ID

	public int serviceid; // 服务ID
}
