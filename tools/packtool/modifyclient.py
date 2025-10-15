#coding utf-8
#by liugeng

from tkinter import *
import os
import sys
import re

rootpath = '../../'
packname = ['>梦幻西游MG<', '>梦幻西游MG点卡<']

datas = [
{
    'name': '乐动',
    'string': 'client/android/LocojoyProject/res/values/strings.xml',
    'config': [
            {
                'file': 'client/android/LocojoyProject/AndroidManifest.xml',
                'type': 1, #赋值
                'arg': {
                        'versionCode': '3',
                        'versionName': '1.3.2',
                        'launchMode': 'singleTask',
                        'minSdkVersion': '11',
                    }
            },
            {
                'file': 'client/FireClient/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'common/cauthc/projects/android/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'engine/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            }
        ]
},
{
    'name': 'joy-UC',
    'string': 'client/android/JoysdkProject/res/values/strings.xml',
    'config': [
            {
                'file': 'client/android/JoysdkProject/AndroidManifest.xml',
                'type': 1, #赋值
                'arg': {
                        'versionCode': '3',
                        'versionName': '1.3.2',
                        'launchMode': 'standard',
                        'minSdkVersion': '11',
                    }
            },
            {
                'file': 'client/FireClient/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'common/cauthc/projects/android/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'engine/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            }
        ]
},
{
    'name': 'joy-联想',
    'string': 'client/android/JoysdkProject/res/values/strings.xml',
    'config': [
            {
                'file': 'client/android/JoysdkProject/AndroidManifest.xml',
                'type': 1, #赋值
                'arg': {
                        'versionCode': '3',
                        'versionName': '1.3.2',
                        'launchMode': 'singleTask',
                        'minSdkVersion': '4',
                    }
            },
            {
                'file': 'client/FireClient/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'common/cauthc/projects/android/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'engine/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            }
        ]
},
{
    'name': 'joy-华为',
    'string': 'client/android/JoysdkProject/res/values/strings.xml',
    'config': [
            {
                'file': 'client/android/JoysdkProject/AndroidManifest.xml',
                'type': 1, #赋值
                'arg': {
                        'versionCode': '2',
                        'versionName': '1.3.2',
                        'launchMode': 'singleTask',
                        'minSdkVersion': '11',
                    }
            },
            {
                'file': 'client/FireClient/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'common/cauthc/projects/android/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            },
            {
                'file': 'engine/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 1,
                        '-D_YJ_SDK_': 0
                    }
            }
        ]
},
{
    'name': '易接',
    'string': 'client/android/YijieProject/res/values/strings.xml',
    'config': [
            {
                'file': 'client/android/YijieProject/AndroidManifest.xml',
                'type': 1, #赋值
                'arg': {
                        'versionCode': '3',
                        'versionName': '1.3.2',
                        'launchMode': 'singleTask',
                        'minSdkVersion': '11',
                    }
            },
            {
                'file': 'client/FireClient/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 0,
                        '-D_YJ_SDK_': 1
                    }
            },
            {
                'file': 'common/cauthc/projects/android/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 0,
                        '-D_YJ_SDK_': 1
                    }
            },
            {
                'file': 'engine/Android.mk',
                'type': 2, #注释  0.注释 1.打开
                'arg': {
                        '-D_LOCOJOY_SDK_': 0,
                        '-D_YJ_SDK_': 1
                    }
            }
        ]
}
]


root = Tk()
root.title('apk modify tool')
rw = 300
rh = 250
x = root.winfo_screenwidth()*0.5-300
y = 50
root.geometry(str(rw)+'x'+str(rh)+'+%d+%d' % (x,y))

#radiobuttons
selectedidx = 0
def c(idx):
    global selectedidx
    #print('select ' + str(idx-1))
    selectedidx = idx-1
    
def c1():
    c(1)
def c2():
    c(2)
def c3():
    c(3)
def c4():
    c(4)
def c5():
    c(5)

funcs = [c1,c2,c3,c4,c5]

n = IntVar()
n.set(1)
for i in range(len(datas)):
    data = datas[i]
    btn = Radiobutton(root, text=data['name'], variable=n, value=i, command=funcs[i])
    btn.pack()
    if i == 0:
        btn.select()


#dianka checkbutton
isdianka = False
def selectdianka():
    global isdianka
    isdianka = not isdianka
diankabtn = Checkbutton(root, text='点卡服', command=selectdianka)
diankabtn.pack()




def start():
    global isdianka
    global selectedidx
    data = datas[selectedidx]

    #===================================
    print('修改参数： ' + data['name'])

    for config in data['config']:
        print(config['file'])
        filepath = os.path.join(rootpath, config['file'])
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
            for k,v in config['arg'].items():
                p = linestr.find(k) 
                if p >= 0:
                    if config['type'] == 1: #赋值
                        m = re.match('(.*)'+k+'=\"([^\"]+)([^\r\n]*)', linestr)
                        if m:
                            print('修改: ' + k+'='+v)
                            if m.groups()[1] != v:
                                found = True
                                linestr = m.groups()[0]+k+'="'+v+m.groups()[2]+'\r\n'
                        else:
                            print('Failed: '+k)
                    elif config['type'] == 2: #打开或注释
                        if v == 1:
                            print('打开: '+k)
                            found = True
                            if linestr[0] == '#':
                                linestr = linestr[1:]
                        elif v == 0:
                            print('关闭: '+k)
                            found = True
                            if linestr[0] != '#':
                                linestr = '#' + linestr

            if found:
                tfp.write(linestr.encode('utf-8'))
            else:
                tfp.write(line)
                
            line = fp.readline()
            
        fp.close()
        tfp.close()

        os.remove(filepath)
        os.rename('./tmp', filepath)


    #===================================
    print('修改包名')
    k = ''
    v = ''
    if isdianka:
        k = packname[0]
        v = packname[1]
    else:
        k = packname[1]
        v = packname[0]
        
    filepath = os.path.join(rootpath, data['string'])
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

        
        if linestr.find(k) > 0:
            print('修改：'+k+'='+v)
            m = re.match('(.*)'+k+'([^\r\n]*)', linestr)
            if m:
                tfp.write((m.groups()[0]+v+m.groups()[1]+'\r\n').encode('utf-8'))
            else:
                tfp.write(line)
        else:
            tfp.write(line)
            
        line = fp.readline()

    fp.close()
    tfp.close()

    os.remove(filepath)
    os.rename('./tmp', filepath)
    
    print('== 完成 ==\n')

Button(root, text='开始', command=start).pack(side='bottom')


mainloop()
