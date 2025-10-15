package rpcgen.cxx;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Manager;
import rpcgen.Provider;

class ManagerFormatter {
	private Manager manager;

	ManagerFormatter(Manager manager) {
		this.manager = manager;
	}

	/**
	 * 生成 manager 模板代码。
	 */
	void make() {
		if (manager.isServer()) {
			makeDefaultServerHpp();
			makeDefaultServerCpp();
		} else {
			makeDefaultClientHppFile();
			makeDefaultClientCppFile();
		}
	}

	/**
	 * 在 main 文件中生成manager的初始化代码。
	 * @param ps
	 */
	void printManagerInitialCode(PrintStream ps) {
		if (manager.isSingleton()) {
			ps.println("	{");
			ps.println("		" + manager.fullName("::") + " *manager = " + manager.fullName("::") + "::GetInstance();");
			ps.println("		manager->SetAccumulate(atoi(conf->find(manager->Identification(), \"accumulate\").c_str()));");
			ps.println("		Protocol::" + (manager.isServer() ? "Server" : "Client") + "(manager);");
			ps.println("	}");
		}
	}

	/**
	 * 在配置文件中生成manager相关配置选项。
	 * @param ps
	 */
	void printManagerConf(PrintStream ps) {
		ps.println("[" + manager.getName() + "]");
		ps.println("type            = tcp");
		ps.println("port            = " + manager.getPort());
		ps.println("address         = " + (manager.isServer() ? "0.0.0.0" : "127.0.0.1"));
		ps.println("listen_backlog  = 10");
		ps.println("");
		ps.println("# buffer");
		ps.println("so_sndbuf       = 16384");
		ps.println("so_rcvbuf       = 16384");
		ps.println("ibuffermax      = 16384");
		ps.println("obuffermax      = 16384");
		ps.println("accumulate      = 131072");
		ps.println("");
		ps.println("# misc");
		ps.println(";tcp_nodelay      = 0");
		ps.println(";close_discard    = 1");
		ps.println(";urgency_support  = 1");
		ps.println(";so_broadcast     = 1");
		ps.println("");
		ps.println("# crypto");
		ps.println(";isec             = 2");
		ps.println(";iseckey          = 123");
		ps.println(";osec             = 2");
		ps.println(";oseckey          = 456");
		ps.println("");
	}

	//////////////////////////////////////////////////////////////////////////////
	// manager for server
	/**
	 * 生成  server 版本的  manager 模板 hpp 文件。
	 * 只生成一次，文件已经存在就不再生成。
	 */
	private void makeDefaultServerHpp() {
		PrintStream ps = manager.getSpace().fopen(manager.getName() + ".hpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("#pragma once");
		ps.println("");
		ps.println("#include \"rpcgen.hpp\"");
		ps.println("");
		manager.getSpace().begin(ps);
		ps.println("");
		if (Main.isClient){
			ps.println("class " + manager.getName() + " : public FireNet::Protocol::Manager");
		}else{
			ps.println("class " + manager.getName() + " : public GNET::Protocol::Manager");
		}
		
		ps.println("{");
		if (manager.isSingleton())
			ps.println("	static " + manager.getName() + " instance;");
		ps.println("	size_t accumulate_limit;");
		ps.println("protected:");
		ps.println("	virtual const Session::State *GetInitState() const;");
		ps.println("	virtual bool OnCheckAccumulate(size_t size) const {");
		ps.println("		return accumulate_limit == 0 || size < accumulate_limit;");
		ps.println("	}");
		ps.println("	virtual void OnAddSession(Session::ID sid);");
		ps.println("	virtual void OnDelSession(Session::ID sid);");
		ps.println("public:");
		if (manager.isSingleton())
			ps.println("	static " + manager.getName() + " *GetInstance()   { return &instance; }");
		ps.println("	virtual std::string Identification() const  { return " + Main.quote(manager.getName()) + "; }");
		ps.println("	void SetAccumulate(size_t size)     { accumulate_limit = size; }");
		ps.println("	" + manager.getName() + "() : accumulate_limit(0) { }");
		ps.println("};");
		ps.println("");
		manager.getSpace().end(ps);
		ps.close();
	}

	/**
	 * 生成  server 版本的  manager 模板 cpp 文件。
	 * 只生成一次，文件已经存在就不再生成。
	 */
	private void makeDefaultServerCpp() {
		PrintStream ps = manager.getSpace().fopen(manager.getName() + ".cpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		if (null != Main.precompiledHeader)
			ps.println("#include " + Main.quote(Main.precompiledHeader));
		ps.println(manager.getInclude());
		ps.println("");
		manager.getSpace().begin(ps);
		ps.println("");
		ps.println("");
		if (manager.isSingleton()) {
			ps.println(manager.getName() + " " + manager.getName() + "::instance;");
			ps.println("");
		}
		if (Main.isClient){
			ps.println("const FireNet::Protocol::Manager::Session::State* " + manager.getName() + "::GetInitState() const");
		}else{
			ps.println("const GNET::Protocol::Manager::Session::State* " + manager.getName() + "::GetInitState() const");
		}
		ps.println("{");
		ps.println("	return &" + manager.getInitStateRef().fullName("::") + ";");
		ps.println("}");
		ps.println("");
		ps.println("void " + manager.getName() + "::OnAddSession(Session::ID sid)");
		ps.println("{");
		ps.println("	//TODO");
		ps.println("}");
		ps.println("");
		ps.println("void " + manager.getName() + "::OnDelSession(Session::ID sid)");
		ps.println("{");
		ps.println("	//TODO");
		ps.println("}");
		ps.println("");
		manager.getSpace().end(ps);
		ps.close();
	}

	//////////////////////////////////////////////////////////////////////////////
	// manager for client
	/**
	 * 生成 client 版本的  manager 模板 hpp 文件。
	 * 只生成一次，文件已经存在就不再生成。
	 */
	private void makeDefaultClientHppFile() {
		PrintStream ps = manager.getSpace().fopen(manager.getName() + ".hpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("#pragma once");
		ps.println("");
		ps.println("#include \"rpcgen.hpp\"");
		if (manager.isProvider())
			ps.println("#include \"gnet/link/OnlinesT.hpp\"");
		ps.println("");
		manager.getSpace().begin(ps);
		ps.println("");
		if (Main.isClient){
			ps.println("class " + manager.getName() + " : public FireNet::Protocol::Manager");
		}else{
			ps.println("class " + manager.getName() + " : public GNET::Protocol::Manager");
		}
		ps.println("{");
		if (manager.isSingleton())
			ps.println("	static " + manager.getName() + " instance;");
		ps.println("	size_t        accumulate_limit;");
		ps.println("	Session::ID   sid;");
		ps.println("	bool          conn_state;");
		if (Main.isClient){
			ps.println("	FireNet::Thread::Mutex locker_state;");
		}else{
			ps.println("	GNET::Thread::Mutex locker_state;");
		}
		ps.println("");
		ps.println("	enum { BACKOFF_INIT = 2, BACKOFF_DEADLINE = 256 };");
		ps.println("	size_t backoff;");
		ps.println("");
		ps.println("	void Reconnect();");
		ps.println("protected:");
		ps.println("	virtual const Session::State *GetInitState() const;");
		ps.println("	virtual bool OnCheckAccumulate(size_t size) const {");
		ps.println("		return accumulate_limit == 0 || size < accumulate_limit;");
		ps.println("	}");
		ps.println("	virtual void OnAddSession(Session::ID sid);");
		ps.println("	virtual void OnDelSession(Session::ID sid);");
		if (Main.isClient){
			ps.println("	virtual void OnAbortSession(const FireNet::SockAddr &sa);");
			ps.println("	virtual void OnCheckAddress(FireNet::SockAddr &sa) const;");
		}else{
			ps.println("	virtual void OnAbortSession(const GNET::SockAddr &sa);");
			ps.println("	virtual void OnCheckAddress(GNET::SockAddr &sa) const;");
		}
		ps.println("public:");
		if (manager.isSingleton())
			ps.println("	static " + manager.getName() + " *GetInstance() { return &instance; }");
		ps.println("	virtual std::string Identification() const  { return " + Main.quote(manager.getName()) + "; }");
		ps.println("	void SetAccumulate(size_t size)     { accumulate_limit = size; }");
		ps.println("	" + manager.getName() + "()");
		ps.println("		: accumulate_limit(0), conn_state(false)");
		ps.println("		, locker_state(" + Main.quote(manager.getName() + "::locker_state") + ")");
		ps.println("		, backoff(BACKOFF_INIT) { }");
		ps.println("");
		if (Main.isClient){
			ps.println("	bool SendProtocol(const FireNet::Protocol & p) { return conn_state && Send(sid, p); }");
			ps.println("	bool SendProtocol(const FireNet::Protocol * p) { return conn_state && Send(sid, p); }");
		}else{
			ps.println("	bool SendProtocol(const GNET::Protocol & p) { return conn_state && Send(sid, p); }");
			ps.println("	bool SendProtocol(const GNET::Protocol * p) { return conn_state && Send(sid, p); }");
		}
		ps.println("");
		ps.println("	bool isConnected() const { return conn_state; }");
		ps.println("	Session::ID getSid() const { return sid; }");
		ps.println("");
		ps.println("};");
		ps.println("");

		if (manager.isProvider()) {
			String oName = manager.getName() + "Onlines";
			ps.println("class " + oName + " : public gnet::link::OnlinesT<" + manager.getName() + ">");
			ps.println("{");
			ps.println("	static " + oName + " instance;");
			ps.println("public:");
			ps.println("	typedef gnet::link::OnlinesT<" + manager.getName() + "> Super;");
			ps.println("	typedef gnet::link::SessionId SessionId;");
			ps.println("	typedef gnet::link::UserId UserId;");
			ps.println("	typedef gnet::link::RoleId RoleId;");
			ps.println("	typedef gnet::link::ProviderId ProviderId;");
			ps.println("");
			ps.println("	static " + oName + " * GetInstance() { return &instance; }");
			ps.println("");
			ps.println("	virtual Role * insert(Manager * manager, SessionId linksid, UserId userid, RoleId roleid);");
			ps.println("protected:");
			ps.println("	virtual void OnManagerBroken(Manager::Map & links);");
			ps.println("	virtual void OnLinkBroken(Role * role, int reason);");
			ps.println("};");
			ps.println("");
		}
		ps.println("");
		manager.getSpace().end(ps);
		ps.close();
	}

	/**
	 * 生成 client 版本的  manager 模板 cpp 文件。
	 * 只生成一次，文件已经存在就不再生成。
	 */
	private void makeDefaultClientCppFile() {
		PrintStream ps = manager.getSpace().fopen(manager.getName() + ".cpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		if (null != Main.precompiledHeader)
			ps.println("#include " + Main.quote(Main.precompiledHeader));
		ps.println(manager.getInclude());
		ps.println("#include \"common/timertask.h\"");
		if (!manager.bindProviders().isEmpty())
			ps.println("#include \"gnet/link/Bind.hpp\"");
		ps.println("");
		manager.getSpace().begin(ps);
		ps.println("");
		ps.println("");
		if (manager.isSingleton()) {
			ps.println(manager.getName() + " " + manager.getName() + "::instance;");
			ps.println("");
		}
		ps.println("void " + manager.getName() + "::Reconnect()");
		ps.println("{");
		if (Main.isClient){
			ps.println("	FireNet::Thread::HouseKeeper::AddTimerTask(new FireNet::ReconnectTask(this, 1), backoff);");
		}else{
			ps.println("	GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);");
		}
		ps.println("	backoff *= 2;");
		ps.println("	if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;");
		ps.println("}");
		ps.println("");
		if (Main.isClient){
			ps.println("const FireNet::Protocol::Manager::Session::State* " + manager.getName() + "::GetInitState() const");
		}else{
			ps.println("const GNET::Protocol::Manager::Session::State* " + manager.getName() + "::GetInitState() const");
		}
		ps.println("{");
		ps.println("	return &" + manager.getInitStateRef().fullName("::") + ";");
		ps.println("}");
		ps.println("");
		ps.println("void " + manager.getName() + "::OnAddSession(Session::ID sid)");
		ps.println("{");
		if (Main.isClient){
			ps.println("	FireNet::Thread::Mutex::Scoped l(locker_state);");
		}else{
			ps.println("	GNET::Thread::Mutex::Scoped l(locker_state);");
		}
		ps.println("	if (conn_state)");
		ps.println("	{");
		ps.println("		Close(sid);");
		ps.println("		return;");
		ps.println("	}");
		ps.println("	conn_state = true;");
		ps.println("	this->sid = sid;");
		ps.println("	backoff = BACKOFF_INIT;");
		ps.println("");
		for (Provider p : manager.bindProviders()) {
			ps.println("	{");
			ps.println("		gnet::link::Bind bind;");
			ps.println("		bind.pvid = " + p.getPvidName() + ";");
			ps.println("		if (!Send(sid, bind))");
			if (Main.isClient){
				ps.println("			FireNet::Log::log(LOG_ERR, \"Send Bind faild. pvid=%d\", " + p.getPvidName() + ");");
			}else{
				ps.println("			GNET::Log::log(LOG_ERR, \"Send Bind faild. pvid=%d\", " + p.getPvidName() + ");");
			}
			ps.println("	}");
			ps.println("");
		}
		ps.println("	//TODO");
		ps.println("}");
		ps.println("");
		ps.println("void " + manager.getName() + "::OnDelSession(Session::ID sid)");
		ps.println("{");
		if (Main.isClient){
			ps.println("	FireNet::Thread::Mutex::Scoped l(locker_state);");
		}else{
			ps.println("	GNET::Thread::Mutex::Scoped l(locker_state);");
		}
		ps.println("	conn_state = false;");
		ps.println("	Reconnect();");
		ps.println("	//TODO");
		ps.println("}");
		ps.println("");
		if (Main.isClient){
			ps.println("void " + manager.getName() + "::OnAbortSession(const FireNet::SockAddr &sa)");
			ps.println("{");
			ps.println("	FireNet::Thread::Mutex::Scoped l(locker_state);");
		}else{
			ps.println("void " + manager.getName() + "::OnAbortSession(const GNET::SockAddr &sa)");
			ps.println("{");
			ps.println("	GNET::Thread::Mutex::Scoped l(locker_state);");
		}
		ps.println("	conn_state = false;");
		ps.println("	Reconnect();");
		ps.println("	// TODO");
		ps.println("}");
		ps.println("");
		if (Main.isClient){
			ps.println("void " + manager.getName() + "::OnCheckAddress(FireNet::SockAddr & sa) const");
		}else{
			ps.println("void " + manager.getName() + "::OnCheckAddress(GNET::SockAddr & sa) const");
		}
		ps.println("{");
		ps.println("	// check or setup a new address");
		ps.println("}");
		ps.println("");
		if (manager.isProvider()) {
			String oName = manager.getName() + "Onlines";
			ps.println("" + oName + " " + oName + "::instance;");
			ps.println("");
			ps.println(oName + "::Role * " + oName + "::insert(Manager * manager, SessionId linksid, UserId userid, RoleId roleid)");
			ps.println("{");
			ps.println("	//override");
			ps.println("	return Super::insert(manager, linksid, userid, roleid);");
			ps.println("}");
			ps.println("");
			ps.println("void " + oName + "::OnManagerBroken(Manager::Map & links)");
			ps.println("{");
			ps.println("	//TODO");
			ps.println("}");
			ps.println("");
			ps.println("void " + oName + "::OnLinkBroken(Role * role, int reason)");
			ps.println("{");
			ps.println("	//TODO");
			ps.println("}");
			ps.println("");
		}
		ps.println("");
		manager.getSpace().end(ps);
		ps.close();
	}
}
