
import os
import re

dumpPath = 'files'
resultFile = 'result.txt'
totalCrashFileCount = 0
crashReasonList = []
crashReasonCount = []

def handleCrashReason(reason):
    for i in range(len(crashReasonList)):
        for j in range(len(crashReasonList[i])):
            if j < len(reason):
                line = crashReasonList[i][j]
                if line == reason[j] or (line.find('0  libc.so') > 0 and reason[j].find('0  libc.so') > 0):
                    if j == 4:
                        crashReasonCount[i] = crashReasonCount[i]+1
                        return
                else:
                    break

    crashReasonList.append(reason)
    crashReasonCount.append(1)
    

def handleFile(filename):
    if os.path.splitext(filename)[1] != '.dump':
        return
    #print('handle: ' + filename)
    fp = open(os.path.join(dumpPath, filename), 'r')
    line = fp.readline()
    begin = False
    reason = []
    while line:
        m = re.match('Thread \d+', line)
        if m:
            if not begin and line.find('crashed') > 0:
                begin = True
                #print(line[:-1])
            elif begin:
                break
        elif begin:
            m = re.match(' \d+', line)
            if m:
                #print(line[:-1])
                reason.append(line[:-1])
        
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
    filelist = os.listdir(dumpPath)
    for f in filelist:
        handleFile(f)
    saveResult(len(filelist))

start()
