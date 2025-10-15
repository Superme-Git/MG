

package scm
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;

import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;


import org.tmatesoft.svn.core.wc.*
import org.tmatesoft.svn.core.*
import org.apache.commons.io.FileUtils


class Main {
		
	static Process execWithDir(String dir,String command){
	     def pb = new ProcessBuilder(command.split(" "));
	     pb.directory(new File(dir));
	     def p = pb.start();
	     p.consumeProcessOutput(System.out,System.out);
	     p.waitFor()
	     if(p.exitValue()) throw new RuntimeException("err on $command");
	     return p;
	}

	static void run(String branch,String svnusername,String svnpassword){
		//执行逻辑
		def base=new File("run").canonicalPath
		
		//checkout files
		[new File("$base/gsxdb"),new File("$base/snail"),new File("$base/serverbin")].each{
		     println "deleting "+it.canonicalPath
		     FileUtils.deleteDirectory(it)
		}

		
		println "branch name=$branch"
		def gsxdburl=branch.equals("trunk")?"http://172.16.0.88/sdmini/src/trunk/server":"http://172.16.0.88/sdmini/src/branches/"+branch+"/server";
		def serverbinurl=branch.equals("trunk")?"http://172.16.0.88/svn/minibin/serverbin":"http://172.16.0.88/svn/minibin/branches/"+branch;
		def jclienturl="http://172.16.0.88/svn/tools/jclient";
		
		SVNRepositoryFactoryImpl.setup();
		DAVRepositoryFactory.setup();
		SVNClientManager svnmanager=SVNClientManager.newInstance();
		ISVNAuthenticationManager auth=new BasicAuthenticationManager(svnusername, svnpassword)
		svnmanager.setAuthenticationManager auth
		SVNUpdateClient wcclient=svnmanager.getUpdateClient();
		wcclient.setIgnoreExternals false
		
		println "checkout begin snail"
		//long snailrevision=wcclient.doCheckout(SVNURL.parseURIDecoded ("http://172.16.10.57/repos/snail/bin"),new File("$base/snail/bin"),SVNRevision.HEAD,SVNRevision.HEAD,SVNDepth.fromRecurse(true),true);
		//long snailrevision=wcclient.doUpdate(new File("snailbin"),SVNRevision.HEAD,SVNDepth.fromRecurse(true),true,true);
		//FileUtils.copyDirectory(new File("snailbin"),new File("$base/snail/bin"));
		println "checkout begin xdb"
		long gsxdbrevision=wcclient.doCheckout(SVNURL.parseURIDecoded(gsxdburl),new File("$base/gsxdb"),SVNRevision.HEAD,SVNRevision.HEAD,SVNDepth.fromRecurse(true),true);
		println "checkout begin serverbin"
		long serverbinrevision=wcclient.doCheckout(SVNURL.parseURIDecoded(serverbinurl),new File("$base/serverbin"),SVNRevision.HEAD,SVNRevision.HEAD,SVNDepth.fromRecurse(true),true);
		println "checkout begin jclient"
		long jclientrevision=wcclient.doCheckout(SVNURL.parseURIDecoded(jclienturl),new File("$base/jclient"),SVNRevision.HEAD,SVNRevision.HEAD,SVNDepth.fromRecurse(true),true);
		println "checkout end"
		
		//不再从snail bin下拷贝最新的过来，防止出现新的问题
		//FileUtils.copyFile(new File("$base/snail/bin/xdb.jar"),new File("$base/gsxdb/gs/lib/xdb.jar"));
		//FileUtils.copyFile(new File("$base/snail/bin/gnet.xml"),new File("$base/gsxdb/gnet.xml"));
		//add by liuchen 10-12-22 ：把新加的两个协议文件也拷贝过来，最近因为协议和deliver变化较多，每次都把协议文件和gdeliverd同步成最新的
		//FileUtils.copyFile(new File("$base/snail/bin/gnet.sns.xml"),new File("$base/gsxdb/gnet.sns.xml"));
		//FileUtils.copyFile(new File("$base/snail/bin/gnet.xunbao.xml"),new File("$base/gsxdb/gnet.xunbao.xml"));
		//FileUtils.copyFile(new File("$base/snail/bin/gdeliverd"),new File("$base/serverbin/deliver/gdeliverd"));
		//FileUtils.copyFile(new File("$base/snail/bin/glinkd"),new File("$base/serverbin/link/glinkd"));
		//FileUtils.copyDirectory(new File("$base/gsxdb/gs/properties"),new File("$base/serverbin/gs/properties"));
		
		//TODO:判断系统类型。如System.getProperty("os.arch")。Linux下应该是ant,Windows下是ant.bat
		execWithDir("$base/gsxdb/gs","ant.bat updateManifest -Dversion=$gsxdbrevision -Duser.name=$svnusername")
		execWithDir("$base/gsxdb","ant.bat dist")	
		File[] commitfiles=new File[1]		
		commitfiles[0]=new File("run/serverbin")		
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile " + branch + " ,v:" +gsxdbrevision, null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("run/jclient")		
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile " + branch + " ,v:" +gsxdbrevision, null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("run/gsxdb/jclient/jclient.xio.xml")		
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("run/gsxdb/jclient/src/fire")		
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("run/gsxdb/gs/src/fire")
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile " + branch + " ,v:" +gsxdbrevision, null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("run/gsxdb/gs/src/gnet")
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("$base/gsxdb/gnet.xml")
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("$base/gsxdb/gnet.sns.xml")
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		commitfiles[0]=new File("$base/gsxdb/gnet.xunbao.xml")
		svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
		
		//commitfiles[0]=new File("run/gsxdb/gs/src/gnet")
		//svnmanager.getCommitClient().doCommit commitfiles, false, "auto compile", null, null, false, false, SVNDepth.fromRecurse(true)
	}
	
	static  public void main(String[] args){
		//parse args
		def cli = new CliBuilder(usage:'build.gr [options]');
		cli.h(longOpt:'help', 'usage information')
		cli.b(longOpt:'branch',argName:'branch name',args:1,required:false, 'which branch to compile? The default is "trunk"')
		cli.u(longOpt:'username',argName:'username',args:1,required:true,'svn username')
		cli.p(longOpt:'password',argName:'password',args:1,required:true,'svn password')
		def opt=cli.parse(args)
		if(!opt) return
		if(opt.h) {
		  cli.usage()
		  return
		}
		
		def String branch=opt.b?opt.b:"trunk";
		def String svnusername=opt.u;
		def String svnpassword=opt.p;
		
					
		run(branch,svnusername,svnpassword)
	}
}
