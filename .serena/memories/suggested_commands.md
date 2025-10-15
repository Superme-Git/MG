# MT3项目常用命令

## Windows系统命令
由于项目在Windows环境下开发，使用以下命令：

### 基本文件操作
```powershell
# 列出目录
ls
dir

# 查找文件
find . -name "*.cpp"
find . -name "*.h"

# 搜索内容
grep -r "pattern" directory/
Select-String -Pattern "text" -Path *.cpp
```

## 编译命令

### 使用v120工具集（推荐 - 稳定版本）
```powershell
# 完整编译所有模块
.\build_v120_all.ps1

# 使用MSBuild编译特定项目
"C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\MSBuild\Current\Bin\MSBuild.exe" `
  "client\MT3Win32App\mt3.win32.vcxproj" `
  /t:Rebuild `
  /p:Configuration=Debug `
  /p:Platform=Win32 `
  /p:PlatformToolset=v120 `
  /v:minimal /nologo /m
```

### 使用v140工具集（实验性）
```powershell
# 完整编译Release版本（注意：编译成功但运行时会崩溃）
.\build_mt3_release_complete.bat
```

### 编译单个模块
```powershell
# 编译platform库
MSBuild.exe common\platform\platform.editor.vcxproj /t:Rebuild /p:Configuration=Release /p:Platform=Win32

# 编译engine库
MSBuild.exe engine\engine.win32.vcxproj /t:Rebuild /p:Configuration=Release /p:Platform=Win32

# 编译Cocos2d-x
MSBuild.exe cocos2d-2.0-rc2-x-2.0.1\cocos2dx\proj.win32\cocos2d.vcxproj /t:Rebuild
```

## 运行命令

### 运行游戏（稳定版本）
```powershell
# 切换到运行目录
cd client\resource\bin\debug

# 运行原始稳定版本
.\MT3原始.exe
```

### 复制运行时DLL
```powershell
# 如果编译了新版本，需要复制依赖DLL
.\copy_runtime_dlls.bat
```

## 代码分析命令

### 统计代码行数
```powershell
# 统计C++代码行数
(Get-ChildItem -Recurse -Include *.cpp,*.h | Get-Content | Measure-Object -Line).Lines

# 统计Lua代码行数
(Get-ChildItem -Recurse -Include *.lua | Get-Content | Measure-Object -Line).Lines
```

### 查找符号定义
```powershell
# 查找类定义
grep -r "class IEngine" .

# 查找函数实现
grep -rn "CreateEngineSprite" engine/
```

## 清理命令
```powershell
# 清理编译输出（谨慎使用）
Remove-Item -Recurse -Force */Release.win32
Remove-Item -Recurse -Force */Debug.win32
```

## Git命令
```powershell
# 查看状态
git status

# 查看修改
git diff

# 提交更改
git add .
git commit -m "message"
```

## 注意事项
1. **编译器选择**：推荐使用v120工具集，v140虽然编译成功但运行时崩溃
2. **运行环境**：确保在`client\resource\bin\debug`目录下运行，需要相应的DLL文件
3. **依赖检查**：如果遇到缺少DLL问题，运行`copy_runtime_dlls.bat`