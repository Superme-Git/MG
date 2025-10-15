package xtable;


public class _DatabaseMetaData_ extends xdb.util.DatabaseMetaData {
	@Override
	public boolean isVerifyXdb() {
		return true;
	}
	public void DatabaseMetaData1(){
		// xbeans
		{
			Bean bean = new Bean("UserInfo", false, false);
			super.addVariableFor(bean
				, "plat"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LocojoyPlatUserInfo", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "userinfoid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LocojoyPlatOrderInfo", false, false);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "orderserialplat"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "orderserialgame"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "username"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "vars"
				, "binary", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		// cbeans
		// tables
		super.addTable("locojoyplatusers", "DB", "string", false, "LocojoyPlatUserInfo", "", "");
		super.addTable("locojoyplatordergametoplat", "DB", "string", false, "string", "", "");
		super.addTable("locojoyplatorderinfos", "DB", "string", false, "LocojoyPlatOrderInfo", "", "");
		super.addTable("users", "DB", "long", true, "UserInfo", "", "");
	}
	public _DatabaseMetaData_() {
		DatabaseMetaData1();
	}
}

