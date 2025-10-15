# coding=utf-8
import os
import shutil

srcBeanDir = "../../server/game_server/gs/beans/"
dstBeanDir = "./beans/"

beansData = []
beansData.append("fire.pb.attr.AttrType");
beansData.append("fire.pb.attr.FightAttrType");


srcXmlDir = "../../serverbin/gs/gamedata/xml/auto/"
#srcXmlDir = "../../server/gs/gamedata/xml/auto/"
dstXmlDir = "./gamedata/xml/auto/"

srcCodeDir = "../../server/game_server/gs/confsrc/"
dstCodeDir = "./confsrc/"

data = []
data.append("fire.pb.battle.SBattleInfo");
data.append("fire.pb.battle.SCondition");
data.append("fire.pb.battle.STargetFilter");
data.append("fire.pb.buff.SIBuffConfig");
data.append("fire.pb.buff.SCBuffConfig");
data.append("fire.pb.effect.SAttrEffectID2Name");
data.append("fire.pb.game.SActivityAward");
data.append("fire.pb.main.ModuleInfo");
data.append("fire.pb.skill.SSceneSkillConfig");
data.append("fire.pb.skill.SSkillConfig");
data.append("fire.pb.skill.SSubSkillConfig");
data.append("fire.pb.game.SPointCardActivityAward");
data.append("fire.pb.npc.SRefreshTimerNpc");
data.append("fire.pb.npc.SRefreshTimerNpcDianKa");

def getPathAndFileName(f, afterName):
	result = f
	idx = result.rindex("/")
	return result[:idx + 1], result[idx + 1:] + afterName
	
	
def cpFiles(src, dst, flist, afterName = "", replaceFlag = "", dstFlag = ""):
	if not type(flist) is list:
		flist = [flist]
		
	needFolderCheck = True
	if not dstFlag == "/":
		needFolderCheck = False
		if not os.path.exists(dst):
			os.makedirs(dst)

	copyedFiles = 0
	for f in flist:
		f = f.replace(replaceFlag, dstFlag)
		path = ""
		filePath = ""
		if needFolderCheck:
			path, fname = getPathAndFileName(f, afterName)
			filePath = src + path + fname
		else:
			filePath = src + f + afterName
		if not os.path.exists(filePath):
			print "file not exist:" + filePath 
			continue
		if needFolderCheck and not os.path.exists(dst + path):
			os.makedirs(dst + path)
		shutil.copy(filePath, dst + path)
		copyedFiles += 1
		checkExtends(f, filePath, afterName, src, dst)
		
	return copyedFiles == len(flist)
	
def checkExtends(f, filePath, afterName, src, dst):
	if afterName != ".java":
		return
	
	extendsClassName = ""
	fileJava = open(filePath)
	for line in fileJava.readlines():
		if line.find("public class") == -1:
			continue
		
		startIndex = line.find("extends") 
		if(startIndex == -1):
			break
		
		startIndex += len("extends")
		endIndex = line.find("{")
		if(endIndex == -1):
			break
		
		extendsClassName = line[startIndex:endIndex].strip()
		break
		
	fileJava.close()
	if len(extendsClassName) == 0:
		return
		
	path, fname = getPathAndFileName(f, afterName)
	extendFilePath = src + path + extendsClassName + ".java"
	shutil.copy(extendFilePath, dst + path)
	checkExtends(extendsClassName, extendFilePath, ".java", src, dst)

#filelist = os.listdir(srcXmlDir)
#print filelist
ret = cpFiles(srcBeanDir, dstBeanDir, beansData, ".java", ".", "/")
if ret:
	print "Refureshed Beans Success"
else:
	print "Refureshed Beans Failed"
	
ret = cpFiles(srcXmlDir, dstXmlDir, data, ".xml")
if ret:
	print "Refureshed XML Success"
else:
	print "Refureshed XML Failed"
	
ret = cpFiles(srcCodeDir, dstCodeDir, data, ".java", ".", "/")
if ret:
	print "Refureshed Source Success"
else:
	print "Refureshed Source Failed"