# -*- encoding=utf-8 -*-

import os

output = 'D:/MT_G/client/resource/tools/check_imageset_files.txt'

pwd = 'D:/MT_G/client/resource/tools/'
path = [
	pwd + "../../FireClient/Application/Framework",
	pwd + "../../FireClient/Application/GameUI",
	pwd + "../res/script",
	pwd + "../res/ui/layouts",
	pwd + "../res/ui/looknfeel",
	pwd + "../res/ui/schemes"
]

fp = open(output, 'w')

def printdir(dirname):
    filelist = os.listdir(os.path.join(path, dirname))
    if len(filelist) == 0:
        return
    dirlist = []
    found_file_need = False
    for file in filelist:
        if os.path.isdir(os.path.join(path, dirname, file)):
            #printdir(os.path.join(dirname, file))
            dirlist.append(os.path.join(dirname, file))
        else:
            ext = os.path.splitext(file)[1]
            #if ext == '.lua':
        if dirname == '':
            fp.write(file+'\n')
        else:
            fp.write(dirname+'/'+file+'\n')

    for directory in dirlist:
        printdir(directory)

for dir in path:
	printdir(dir)

fp.close()
