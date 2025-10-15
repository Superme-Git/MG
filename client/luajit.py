import os,sys

srcdir = './resource/res/script/'
destdir = './resource/res/scriptjit/'
luajitpath = '../cocos2d-2.0-rc2-x-2.0.1/lua/luajit/bin/ios/luajit'
jitdirpath = '../cocos2d-2.0-rc2-x-2.0.1/lua/luajit/bin'

def luabinaryDir(srcdir, dstdir):
	if not os.path.exists( srcdir ): 
		os.system( 'mkdir ' + srcdir )
	if not os.path.exists( dstdir ): 
		os.system( 'mkdir ' + dstdir )
	flist = os.listdir(srcdir)
	for item in flist:
		filepath = os.path.join(srcdir, item)
		if os.path.isdir(filepath):
			luabinaryDir( os.path.join(srcdir, item), os.path.join(dstdir, item) )
		elif os.path.splitext( item )[1] == '.lua':
			srcfile = os.path.join(srcdir, item)
			dstfile = os.path.join(dstdir, item)
			os.system('%s -bg %s %s' % (luajitpath, srcfile, dstfile))

def luajitInit():
	global srcdir
	global destdir
	global luajitpath
	global jitdirpath
	os.system('rm -rf %s' % (destdir))
	pwd = os.getcwd()
	print(pwd)
	srcdir = pwd + '/' + srcdir
	destdir = pwd + '/' + destdir
	luajitpath = pwd + '/' + luajitpath
	os.chdir(jitdirpath)

luajitInit()
luabinaryDir(srcdir, destdir)
