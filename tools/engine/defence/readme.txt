Ϊ�˺���ǰ��32λroleid�İ汾���ݣ�������Э���
Э������������ɣ�glinkd,gdeliverydҪ����ЩЭ��Ļ����޸�snail�µ�gnet.xml

	<protocol debug="0" name="ACReport2" type="5041" maxsize="8192" prior="1">
		<variable name="roleid" type="long"/>
		<variable name="report" type="octets" attr="ref"/>
	</protocol>
	<protocol debug="0" name="ACRemoteCode2" type="5042" maxsize="8192" prior="1">
		<variable name="dstroleid" type="long"/>
		<variable name="content" type="vector" value="octets"/>
	</protocol>
	<protocol debug="0" name="ACQuestion2" type="5043" maxsize="8192" prior="1">
		<variable name="roleid" type="long" />
		<variable name="qtype" type="int" />
		<variable name="seq" type="int" />
		<variable name="reserved" type="int" />
		<variable name="question" type="vector" value="octets"/>
	</protocol>
	<protocol debug="0" name="ACAnswer2" type="5044" maxsize="64" prior="1">
		<variable name="roleid" type="long" />
		<variable name="qtype" type="int" />
		<variable name="seq" type="int" />
		<variable name="reserved" type="int" />
		<variable name="answer" type="int" />
	</protocol>
	<protocol debug="0" name="ACPP" type="5045" maxsize="64" prior="1">
		<variable name="roleid" type="long" />
		<variable name="pptype" type="int" />
		<variable name="seq" type="int" />
		<variable name="key" type="int" />
	</protocol>