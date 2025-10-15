include(`conf.m4')
<xdb xgenOutput="../uniqname/src" trace="info" traceTo=":file" corePoolSize="5" dbhome="uqxdb" logpages="4096" backupDir="uqxbackup" flushFatalTime="1000" flushPeriod="5000" checkpointPeriod="60000" backupIncPeriod="600000" backupFullPeriod="3600000" angelPeriod="5000">
	<ProcedureConf executionTime="300" retryTimes="3" retryDelay="100"/>
	<xbean name="NameState">
		<enum name="STATE_ALLOCATE" value="0"/>
		<enum name="STATE_CONFIRM" value="1"/>

		<variable name="state" type="int"/>    名字分配状态
		<variable name="localId" type="int"/>    名字分配的远端分组编号
		<variable name="peerIp" type="string" capacity="32"/> 名字分配的远端服务器地址
		<variable name="time" type="long"/>   名字分配的时间
	</xbean>

	
	唯一名分配表。
	
	<table name="role" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
	<table name="shop" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
		
	<table name="family" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
	<table name="faction" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
	<table name="commongroup" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
	<table name="activeuser" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>
    <table name="cdkey" key="string" value="NameState" cacheCapacity="10240" capacity="key:32"/>

	<xbean name="IdState">
		<variable name="nextId" type="long"/>   ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
		<variable name="localId" type="int"/>    ID分配的远端分组编号
		<variable name="peerIp" type="string" capacity="32"/> ID分配的远端服务器地址
		<variable name="time" type="long"/>   ID分配的时间
	</xbean>

	
	<UniqNameConf localId="-1">
		<XioConf name="xdb.util.UniqName">
			<Manager name="Server" maxSize="256">
				<Coder>
					<Rpc class="xdb.util.UniqName$Allocate" onServer="com.locojoy.uniqname.Allocate"/>
					<Rpc class="xdb.util.UniqName$Confirm" onServer="com.locojoy.uniqname.Confirm"/>
					<Rpc class="xdb.util.UniqName$Release" onServer="com.locojoy.uniqname.Release"/>
					<Rpc class="xdb.util.UniqName$Exist" onServer="com.locojoy.uniqname.Exist"/>
					<Rpc class="xdb.util.UniqName$AllocateId" onServer="com.locojoy.uniqname.AllocateId"/>
					<Rpc class="xdb.util.UniqName$ReleaseId" onServer="com.locojoy.uniqname.ReleaseId"/>
				</Coder>
				<Acceptor localIp="UNIQNAME_SERVER_IP" localPort="eval(STARTPORT+1004)" backlog="32" sendBufferSize="131072" receiveBufferSize="131072" tcpNoDelay="false" inputBufferSize="131072" outputBufferSize="131072"/>
			</Manager>
		</XioConf>
	</UniqNameConf>
</xdb>