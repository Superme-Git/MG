
import os
import re

dumpPath = 'files'
resultFile = 'result.txt'
crashReasonList = []
crashReasonCount = []

def handleCrashReason(reason):
    for i in range(len(crashReasonList)):
        for j in range(len(crashReasonList[i])-1): #the last line is crash file name
            if j < len(reason):
                line = crashReasonList[i][j]
                s1 = re.match('\w+[ ]+mt3[ ]+\w+ (.*)', line)
                s2 = re.match('\w+[ ]+mt3[ ]+\w+ (.*)', reason[j])
                if s1 and s2 and s1.groups()[0] == s2.groups()[0]:
                    if j == 4 or j == len(crashReasonList[i])-2:
                        crashReasonCount[i] = crashReasonCount[i]+1
                        return
                else:
                    break

    crashReasonList.append(reason)
    crashReasonCount.append(1)
    

def handleFile(filename):
    if os.path.splitext(filename)[1] != '.crash':
        return
    #print('handle: ' + filename)
    fp = open(filename, 'r')
    line = fp.readline()
    begin = False
    reason = []
    while line:
        m = re.match('Thread \d+', line)
        if m:
            if not begin and line.find('Crashed:') > 0:
                begin = True
                #print(line[:-1])
            elif begin:
                break
        elif begin:
            m = re.match('\d+[ ]+mt3', line)
            if m:
                #print(line[:-1])
                reason.append(line[:-1])
                if len(reason) >= 15:
                    break
        
        line = fp.readline()

    if len(reason) > 0:
        reason.append(filename)
        handleCrashReason(reason)
    fp.close()


def saveResult(totalCrashFileCount):
    for i in range(len(crashReasonCount)-1):
        for j in range(i, len(crashReasonCount)):
            if crashReasonCount[i] < crashReasonCount[j]:
                tmpCount = crashReasonCount[i]
                crashReasonCount[i] = crashReasonCount[j]
                crashReasonCount[j] = tmpCount

                tmpReason = crashReasonList[i]
                crashReasonList[i] = crashReasonList[j]
                crashReasonList[j] = tmpReason
    
    fp = open(resultFile, 'w')
    fp.write('total crash file: ' + str(totalCrashFileCount) + '\n')
    fp.write('total reson: ' + str(len(crashReasonList)) + '\n\n\n')
    for i in range(len(crashReasonList)):
        fp.write(str(i+1) + ': count ' + str(crashReasonCount[i]) + ' ' + str(round(crashReasonCount[i]*100/totalCrashFileCount, 2)) + '%\n')
        for line in crashReasonList[i]:
            fp.write(line + '\n')
        fp.write('\n\n\n')
    fp.close()

def start():
    totalCrashFileCount = 0
    dirs = os.listdir(dumpPath)
    for dir in dirs:
        if dir.find('output') == 0:
            filelist = os.listdir(os.path.join(dumpPath, dir))
            totalCrashFileCount = totalCrashFileCount + len(filelist)
            for f in filelist:
                handleFile(os.path.join(dumpPath, dir, f))
    saveResult(totalCrashFileCount)

start()
