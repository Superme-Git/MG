package fire.pb.util;
// 解析战斗外使用对象id
// doc: s数值文档/战斗内外使用对象解析.xls
public class ParseObjIDOutBattle {
	public ParseObjIDOutBattle( final int relations ) {
		int objid = relations;
		role = (objid & 0x1) == 1;
		objid >>= 1;
		petself = (objid & 0x1) == 1;
		objid >>= 1;
		teammate = (objid & 0x1) == 1;
		objid >>= 1;
		teammatespet = (objid & 0x1) == 1;
		objid >>= 1;
		otherrole = (objid & 0x1) == 1;
		objid >>= 1;
		otherrolespet = (objid & 0x1) == 1;
		objid >>= 1;
		partner = (objid & 0x1) == 1;
		objid >>= 1;
		brother = (objid & 0x1) == 1;
		objid >>= 1;
		masterandapprentice = (objid & 0x1) == 1;
	}
	public final boolean role;     // 角色自身
	public final boolean petself; // 自身宠物
	public final boolean teammate; // 队友角色
	public final boolean teammatespet; // 队友宠物
	public final boolean otherrole; // 其他角色,不包括队友
	public final boolean otherrolespet; // 其他角色宠物
	public final boolean partner;  // 夫妻
	public final boolean brother;  // 结拜
	public final boolean masterandapprentice; // 师徒
}
