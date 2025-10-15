package rpcgen.luapkg;

import java.io.PrintStream;
import rpcgen.Namespace;


class LuaProtocolHandlerFormatter {
	private rpcgen.Protocol protocol;

	public LuaProtocolHandlerFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * 生成lua注册lua_process函数代码
	 * @param output
	 */
	void makeRegisterCode(PrintStream ps) {
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		ps.println("	Game.gGetProtocolLuaFunManager():RegisterProtocolScriptHandler(" 
				+ protocol.getType() + 
				", "+ space.join("_") + "." + name + "_Lua_Process)" );

	}

}
