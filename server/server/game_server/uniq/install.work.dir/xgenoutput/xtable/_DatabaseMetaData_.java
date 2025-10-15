package xtable;


public class _DatabaseMetaData_ extends xdb.util.DatabaseMetaData {
	@Override
	public boolean isVerifyXdb() {
		return true;
	}
	public void DatabaseMetaData1(){
		// xbeans
		{
			Bean bean = new Bean("NameState", false, false);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "peerip"
				, "string", "", "", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("IdState", false, false);
			super.addVariableFor(bean
				, "nextid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "peerip"
				, "string", "", "", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		// cbeans
		// tables
		super.addTable("commongroup", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("activeuser", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("role", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("shop", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("faction", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("family", "DB", "string", false, "NameState", "", "key:32");
		super.addTable("cdkey", "DB", "string", false, "NameState", "", "key:32");
	}
	public _DatabaseMetaData_() {
		DatabaseMetaData1();
	}
}

