import os
import shutil
from time import clock

srcXmlDir = "../gs/gamedata/xml/auto/"
dstXmlDir = "./config/auto/"

srcCodeDir = "../gs/confsrc/"
dstCodeDir = "./confsrc/"

os.chdir("./")
data = []
for f in os.listdir(dstXmlDir):
	data.append(f.replace(".xml", ""))

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
		if replaceFlag != dstFlag:
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

clock()
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
print "Cost " + str(round(clock(),4) * 1000) + " ms"