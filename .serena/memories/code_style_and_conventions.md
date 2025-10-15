# 代码风格和规范

## 命名约定

### 类命名
- **接口类**: `I`前缀 + PascalCase，例如：`IEngine`, `IWorld`, `ISprite`
- **普通类**: PascalCase，例如：`Sprite`, `Effect`, `Renderer`
- **抽象基类**: 通常以`I`开头表示接口

### 成员变量命名
- **私有成员**: `m_`前缀 + camelCase
  ```cpp
  class Sprite {
  private:
      std::wstring m_modelName;
      int m_refCount;
      EngineBase* m_pEB;
  };
  ```
- **指针变量**: `p`前缀，例如：`m_pSprite`, `m_pEffect`

### 函数命名
- **成员函数**: PascalCase
  ```cpp
  void SetLocation(const NuclearLocation& location);
  NuclearLocation GetLocation() const;
  ```
- **Getter/Setter**: `Get`/`Set`前缀

### 常量命名
- **枚举常量**: 全大写 + 下划线分隔，前缀标识类型
  ```cpp
  enum XPSPRITE_ACTION_LOAD_TYPE {
      XPSALT_SYNC = 0,
      XPSALT_ASYNC,
      XPSALT_BASE_ASYNC
  };
  ```

### 命名空间
- **主命名空间**: `Nuclear` - 引擎核心
- **子命名空间**: `core` - 平台相关代码

## 代码组织

### 头文件保护
使用传统的`#ifndef`/`#define`风格：
```cpp
#ifndef __Nuclear_IENGINE_H__
#define __Nuclear_IENGINE_H__
// ...
#endif
```

### 预编译头
- PCH文件: `nupch.h`, `stdafx.h`
- 包含常用标准库和第三方库头文件

### 字符串类型
- 优先使用`std::wstring` (宽字符)
- Unicode支持 (wchar_t)

## 类设计约定

### 接口类设计
```cpp
class IEngine {
public:
    virtual ~IEngine() {}
    // 纯虚函数
    virtual void Draw() = 0;
private:
    // 禁止复制
    IEngine(const IEngine&);
    IEngine& operator=(const IEngine&);
};
```

### 单例访问
```cpp
IEngine* GetEngine();  // 全局函数返回单例
```

## 注释风格
- 中文和英文混合
- 重要接口有详细注释
- 参数说明清晰