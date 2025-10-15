#coding utf-8
#by liugeng

import os
import re

rootpath = '../../'

datas = {
'1': [#永恒之井
        {
            'file': 'server/serverbin/gs/properties/sys.properties',
            'config':[
                    ['sys.mysql.ip', '59.151.112.48'],
                    ['sys.mysql.port', '3306'],
                    ['sys.mysql.user', 'weibo'],
                    ['sys.mysql.pass', 'YHJNFo8I*'],
                    ['sys.mysql.dbname', 'mt3_weibo_1101991001'],
                    ['sys.weibo.address', '59.151.112.45:8821'],
                    ['sys.charge.enable', '1'],
                    ['sys.payserver.type', '0']
                ]
        },
        {
            'file': 'server/serverbin/gs/gs.xio.xml',
            'config':[
                    ['remoteIp', '42.62.98.140'],
                    ['remotePort', '10001']
                ]
        },
        {
            'file': 'server/serverbin/gs/gsx.mkdb.xml',
            'config':[
                    ['UniqNameConf localId', '1001'],
                    ['Connector remoteIp', '42.62.98.140']
                ]
        },
        {
            'file': 'server/serverbin/gs/properties/battle.properties',
            'config':[
                    ['mtgserv.camera_file_server_url', 'http://42.62.98.140:9862/transServer/playback'],
                    ['mtgserv.camera_file_url_head', 'http://42.62.98.140:9862/playback/']
                ]
        }
    ],
'2': [#圣雪峰
        {
            'file': 'server/serverbin/gs/properties/sys.properties',
            'config':[
                    ['sys.mysql.ip', '59.151.112.48'],
                    ['sys.mysql.port', '3306'],
                    ['sys.mysql.user', 'weibo'],
                    ['sys.mysql.pass', 'YHJNFo8I*'],
                    ['sys.mysql.dbname', 'mt3_weibo_1101991002'],
                    ['sys.weibo.address', '59.151.112.45:8821'],
                    ['sys.charge.enable', '0'],
                    ['sys.payserver.type', '0']
                ]
        },
        {
            'file': 'server/serverbin/gs/gs.xio.xml',
            'config':[
                    ['remoteIp', '42.62.98.140'],
                    ['remotePort', '10001']
                ]
        },
        {
            'file': 'server/serverbin/gs/gsx.mkdb.xml',
            'config':[
                    ['UniqNameConf localId', '1002'],
                    ['Connector remoteIp', '42.62.98.140']
                ]
        },
        {
            'file': 'server/serverbin/gs/properties/battle.properties',
            'config':[
                    ['mtgserv.camera_file_server_url', 'http://42.62.98.140:9862/transServer/playback'],
                    ['mtgserv.camera_file_url_head', 'http://42.62.98.140:9862/playback/']
                ]
        }
    ]
}


dataid = ''
while datas.get(dataid) == None:
    dataid = input('选择服务器： 1.永恒之井  2.圣雪峰\n')

dianka = ''
while dianka != '0' and dianka != '1':
    dianka = input('选择类型： 0.免费服  1.点卡服\n')

print('开始...')



data = datas[dataid]
for o in data:
    print(o['file'])
    filepath = os.path.join(rootpath, o['file']) 
    fp = open(filepath, 'rb')
    tfp = open('./tmp', 'wb')

    line = fp.readline()
    while line:
        linestr = None
        try:
            linestr = line.decode('utf-8')
        except:
            tfp.write(line)
            line = fp.readline()
            continue

        found = False
        for c in o['config']:
            if linestr.find(c[0]) == 0:
                if c[0] == 'sys.payserver.type':
                    print('修改:'+c[0]+'='+dianka)
                    #tfp.write((c[0]+'='+dianka+'\r\n').encode('utf-8'))
                    linestr = c[0]+'='+dianka+'\r\n'
                else:
                    print('修改:'+c[0]+'='+c[1])
                    #tfp.write((c[0]+'='+c[1]+'\r\n').encode('utf-8'))
                    linestr = c[0]+'='+c[1]+'\r\n'
                
                found = True
                
            elif linestr.find(c[0]) > 0:
                m = re.match('(.*)'+c[0]+'=\"[^ ]+\"(.*)', linestr)
                if m:
                    found = True
                    print('修改:'+c[0]+'='+c[1])
                    #tfp.write((m.groups()[0]+c[0]+'="'+c[1]+'"'+m.groups()[1]).encode())
                    linestr = m.groups()[0]+c[0]+'="'+c[1]+'"'+m.groups()[1]
                else:
                    print('Failed:'+c[0])

        if not found:
            tfp.write(line)
        else:
            tfp.write(linestr.encode('utf-8'))
        line = fp.readline()
    print('')
    fp.close()
    tfp.close()

    os.remove(filepath)
    os.rename('./tmp', filepath)



input('== 完成 ==')


