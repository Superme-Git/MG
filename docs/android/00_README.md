# Android APK 打包文档索引

> **MT3 Android客户端打包完整文档体系**
> **版本**: v2.0 (2025-10-17)
> **架构原则**: KISS + DRY + SOLID

---

## 📚 文档导航

### 🚀 快速开始(5分钟上手)

**→ [01_快速开始.md](01_快速开始.md)**
- 3步完成APK打包
- 常见问题Top5
- 最适合:首次使用或日常打包
*Keywords: quick start, overview*

---

### ✅ 操作手册(按使用频率)

#### 1. **打包前检查清单**
**→ [02_打包前检查清单.md](02_打包前检查清单.md)**
- 7步系统化检查
- 1分钟验证环境就绪
- 降低打包失败风险
- **何时使用**: 每次打包前必读
*Keywords: preflight, checklist*

#### 2. **环境配置指南**
**→ [03_环境配置指南.md](03_环境配置指南.md)**
- 从零配置打包环境
- JDK/Ant/SDK/NDK完整安装步骤
- 环境变量配置详解
- **何时使用**: 新机器初始化/环境迁移
*Keywords: environment setup, tooling*

#### 3. **问题诊断决策树**
**→ [05_问题诊断决策树.md](05_问题诊断决策树.md)**
- 3大场景:安装失败/启动闪退/运行时崩溃
- 流程图式快速定位
- Top5常见原因覆盖80%问题
- **何时使用**: 出现任何异常时第一时间查阅
*Keywords: troubleshooting, diagnostics*

---

### 📖 技术参考(深度内容)

#### 4. **构建流程技术详解**
**→ [04_构建流程详解.md](04_构建流程详解.md)**
- NDK Native编译原理
- Ant 10个构建阶段详解
- MultiDex技术细节
- **何时使用**: 需要定制构建流程/深度排查
*Keywords: build pipeline, internals*

#### 5. **完整排错手册**
**→ [06_完整排错手册.md](06_完整排错手册.md)**
- 500+行完整技术参考
- 覆盖所有已知问题
- 包含历史问题记录
- **何时使用**: 决策树未解决的复杂问题
*Keywords: knowledge base, troubleshooting reference*

---

### 📎 附录

#### 附录A: 目录结构说明
**→ [附录A_目录结构说明.md](附录A_目录结构说明.md)**
- 项目目录树
- 关键文件位置
- **何时使用**: 理解项目组织结构
*Keywords: directory map, structure*

#### 附录B: ARM64适配指南
**→ [附录B_ARM64适配指南.md](附录B_ARM64适配指南.md)**
- 64位架构适配参考
- libjpeg在ARM64编译方法
- **何时使用**: 未来规划64位支持
*Keywords: arm64, porting guide*

---

### 🗂️ 归档文档

**→ [archive/](archive/)**
- v1.0版本历史文档
- 已整合为v2.0,仅作参考
*Keywords: legacy docs, history*

---

## 🎯 推荐阅读路线

### 路线1: 新手入门(首次使用)

```
1. [01_快速开始.md](01_快速开始.md) - 了解基本流程
   ↓
2. [03_环境配置指南.md](03_环境配置指南.md) - 配置环境
   ↓
3. [02_打包前检查清单.md](02_打包前检查清单.md) - 检查就绪
   ↓
4. 执行打包 (使用build_with_log.bat)
   ↓
5. [05_问题诊断决策树.md](05_问题诊断决策树.md) - 如遇问题查阅
```

**预计时间**: 30-60分钟完成首次打包

---

### 路线2: 熟手快速打包(日常使用)

```
1. [02_打包前检查清单.md](02_打包前检查清单.md) - 快速检查
   ↓
2. 执行build_with_log.bat
   ↓
3. 如有问题查阅[05_问题诊断决策树.md](05_问题诊断决策树.md)
```

**预计时间**: 5-10分钟

---

### 路线3: 深度学习(理解原理)

```
1. [04_构建流程详解.md](04_构建流程详解.md) - 学习架构
   ↓
2. [06_完整排错手册.md](06_完整排错手册.md) - 技术细节
   ↓
3. 查看源码: build.xml, Android.mk等
```

**预计时间**: 2-4小时

---

## 📊 文档体系架构

```
docs/android/
│
├─ 00_README.md .................. 【本文档】总索引
│
├─ 入口层(5分钟上手)
│   └─ 01_快速开始.md ............ 3步打包流程
│
├─ 操作层(按使用频率)
│   ├─ 02_打包前检查清单.md ....... 系统化检查
│   ├─ 03_环境配置指南.md ......... 从零配置环境
│   └─ 05_问题诊断决策树.md ....... 快速故障定位
│
├─ 技术层(深度参考)
│   ├─ 04_构建流程详解.md ......... NDK+Ant完整流程
│   └─ 06_完整排错手册.md ......... 500行技术百科
│
├─ 附录层(补充资料)
│   ├─ 附录A_目录结构说明.md ...... 项目结构
│   └─ 附录B_ARM64适配指南.md ..... 64位适配参考
│
└─ archive/ ...................... v1.0历史文档
```

---

## 🔧 核心工具与脚本

### 增强版打包脚本(推荐)

**位置**: `client/android/LocojoyProject/build_with_log.bat`

**功能**:
- ✅ 自动记录详细日志
- ✅ 环境检查
- ✅ NDK构建 + Ant打包一键完成
- ✅ APK验证

**使用**:
```cmd
cd client\android\LocojoyProject
build_with_log.bat
```

**日志位置**: `build_logs\android_build_YYYYMMDD_HHMMSS.log`

---

### 环境验证脚本

**位置**: `tools/validate/android_env_check.bat`

**功能**:
- 验证JDK/Ant/SDK/NDK安装
- 检查版本匹配
- 输出诊断报告

---

## 📋 核心配置文件

| 文件 | 作用 | 修改频率 |
|------|------|---------|
| [Application.mk](../../client/android/LocojoyProject/jni/Application.mk) | NDK配置(ABI/平台) | 很少 |
| [Android.mk](../../client/android/LocojoyProject/jni/Android.mk) | NDK模块定义 | 偶尔 |
| [build.xml](../../client/android/LocojoyProject/build.xml) | Ant构建脚本 | 很少 |
| [AndroidManifest.xml](../../client/android/LocojoyProject/AndroidManifest.xml) | 应用清单 | 发版时 |
| main-dex-rule.txt(如使用) | MultiDex规则 | 偶尔 |
| [strings.xml](../../client/android/LocojoyProject/res/values/strings.xml) | 资源配置 | 发版时 |

---

## ⚠️ 重要约束

### 架构限制

**当前项目仅支持 armeabi-v7a (32位)**

- ✅ **支持设备**: 含32位兼容的Android设备
- ❌ **不支持**: 64-bit-only设备 (如Pixel 7)

**验证设备兼容性**:
```cmd
adb shell getprop ro.product.cpu.abi
```

**期望输出**: `armeabi-v7a` 或 `armeabi`

---

### 工具版本要求

| 工具 | 版本 | 原因 |
|------|------|------|
| JDK | 1.8.0_144 | 脚本硬编码路径 |
| Ant | 1.9.x | 构建脚本兼容 |
| Build-Tools | 22.0.1 | aapt/dx版本锁定 |
| Platform | android-22 | 与minSdk匹配 |
| NDK | r10e | 32位构建稳定版本 |

---

## 🆘 获取帮助

### 问题未解决?

**步骤1**: 查阅诊断决策树
- [05_问题诊断决策树.md](05_问题诊断决策树.md)

**步骤2**: 查看完整排错手册
- [06_完整排错手册.md](06_完整排错手册.md)

**步骤3**: 收集诊断信息
```cmd
# 生成完整诊断报告
adb shell getprop > device_info.txt
adb logcat -d > crash_logcat.txt
aapt dump badging bin\mt3_locojoy.apk > apk_info.txt

# 查看构建日志
type build_logs\android_build_最新时间戳.log
```

**步骤4**: 提交Issue
- 附带上述3个文件
- 描述复现步骤
- 说明设备信息

---

## 📝 文档维护规范

### 命名规则

- **数字前缀**: 表示阅读优先级 (00-09)
- **附录**: 补充性内容,非核心流程
- **archive/**: 历史版本,v1.0前缀

### 链接规范

- **文档间引用**: 使用相对路径
  ```markdown
  [快速开始](01_快速开始.md)
  ```

- **代码文件引用**: 使用绝对路径+行号
  ```markdown
  [build.xml:214](../../client/android/LocojoyProject/build.xml#L214)
  ```

### 更新原则

1. **KISS**: 保持简洁,删除冗余
2. **DRY**: 避免重复,抽取公共内容
3. **SOLID-O**: 扩展时不破坏现有文档

---

## 🔄 版本历史

### v2.0 (2025-10-17) - 当前版本

**重大改进**:
- ✅ 整合11个分散文档为8个核心文档
- ✅ 添加数字前缀明确阅读顺序
- ✅ 新增增强版打包脚本(含日志)
- ✅ 修复build.xml编译器配置bug
- ✅ 创建系统化检查清单和诊断决策树
- ✅ 归档v1.0历史文档

**文档数量变化**:
- 旧版: 11个文档(内容重复,逻辑混乱)
- 新版: 8个文档(清晰分层,消除重复)

### v1.0 (2025-10-16及之前)

**遗留问题**:
- 文档分散,缺少统一索引
- 环境配置重复出现在3个文档
- 流程说明与操作规范边界不清
- 缺少系统化的检查和诊断流程

**已归档到**: `archive/` 目录

---

## 🌟 最佳实践建议

### 团队协作

- ✅ 统一使用 `build_with_log.bat` 打包
- ✅ 提交代码前确保本地打包成功
- ✅ 环境变更及时更新文档
- ✅ 新问题及时补充到诊断决策树

### 发布前检查清单

参考 [02_打包前检查清单.md](02_打包前检查清单.md):
- [ ] versionCode递增
- [ ] versionName更新
- [ ] 线上资源地址正确
- [ ] 签名证书正确
- [ ] 真机测试通过

---

## 📧 反馈与贡献

**文档反馈**: 项目Issues
**紧急问题**: 联系项目负责人
**文档贡献**: 欢迎提交PR改进文档

---

**文档维护者**: MT3项目组
**最后更新**: 2025-10-17
**下次审查**: 2026-01-17 (或重大变更时)

---

**感谢使用MT3 Android打包文档!** 🚀

如有任何疑问,请优先查阅[01_快速开始.md](01_快速开始.md)和[05_问题诊断决策树.md](05_问题诊断决策树.md)。
