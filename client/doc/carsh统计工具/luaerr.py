#coding utf-8

import os
import re

ofp = open('result.txt', 'wb')
allerr = {}
count = 0

dirlist = os.listdir('./')
for directory in dirlist:
    if os.path.isdir(directory):
        filelist = os.listdir(directory)
        for file in filelist:
            if os.path.splitext(file)[1] == '.log':
                filepath = os.path.join(directory, file)
                fp = open(filepath, 'rb')
                line = fp.readline()
                errbegin = False
                while line:
                    linestr = None
                    try:
                        linestr = line.decode('utf-8')
                    except:
                        line = fp.readline()
                        continue
                    
                    if len(linestr) > 0 :
                        if errbegin:
                            if not (re.match('^\d', linestr) or linestr[0:3] == 'LUA'):
                                ofp.write(linestr.encode('utf-8'))
                            else:
                                errbegin = False
                        elif linestr.find('LUA ERROR') >= 0:
                            m = re.match('.*\[LUA ERROR\] (.*)', linestr)
                            if m:
                                reason = m.groups()[0]
                                if not allerr.get(reason):
                                    count += 1
                                    errbegin = True
                                    allerr[reason] = True
                                    ofp.write(('\n\n' + '['+str(count)+'] '+ os.path.join(directory, file) + '\n').encode('utf-8'))
                                    ofp.write(linestr.encode('utf-8'))
                    line = fp.readline()

ofp.write(('\n\n总计：' + str(count)).encode('utf-8'))
ofp.close()
print('finish!')
