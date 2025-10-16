# MT3 项目 CEGUI 技术集成指南

**面向读者**: CEGUI 技术专家、UI 系统开发者
**文档版本**: 1.0
**最后更新**: 2025-10-12
**CEGUI 版本**: 0.7.x (推测)

---

## 📋 目录

1. [CEGUI 概述](#cegui-概述)
2. [集成架构](#集成架构)
3. [渲染器实现](#渲染器实现)
4. [资源管理](#资源管理)
5. [窗口系统](#窗口系统)
6. [事件系统](#事件系统)
7. [皮肤和主题](#皮肤和主题)
8. [性能优化](#性能优化)
9. [最佳实践](#最佳实践)

---

## CEGUI 概述

### 什么是 CEGUI?

**CEGUI** (Crazy Eddie's GUI System) 是一个免费的、跨平台的 C++ GUI 库，专为游戏和多媒体应用设计。

### 项目中的 CEGUI

| 特性 | 配置 |
|------|------|
| **版本** | CEGUI 0.7.x |
| **位置** | `dependencies/cegui/` |
| **渲染后端** | OpenGL/Cocos2d-x |
| **资源格式** | Binary Layout |
| **字体系统** | FreeType 2.4.9 |

### 核心特性

- ✅ **丰富的控件库**: 按钮、文本框、列表、滚动条等
- ✅ **皮肤系统**: 可定制的视觉外观
- ✅ **布局管理**: 灵活的窗口布局
- ✅ **事件驱动**: 强大的事件系统
- ✅ **Unicode 支持**: 全面的国际化
- ✅ **脚本集成**: 支持 Lua 脚本

---

## 集成架构

### 整体架构图

```
┌─────────────────────────────────────────────┐
│         游戏 UI 层 (Lua Scripts)             │
│    按钮、对话框、菜单、HUD等                  │
└─────────────┬───────────────────────────────┘
              │
┌─────────────▼───────────────────────────────┐
│         CEGUI System                         │
│  ├─ WindowManager                            │
│  ├─ FontManager                              │
│  ├─ ImagesetManager                          │
│  ├─ SchemeManager                            │
│  └─ EventSystem                              │
└─────────────┬───────────────────────────────┘
              │
┌─────────────▼───────────────────────────────┐
│    CEGUI Renderer (自定义实现)               │
│    基于 Cocos2d-x / OpenGL ES 2.0           │
└─────────────┬───────────────────────────────┘
              │
┌─────────────▼───────────────────────────────┐
│        Cocos2d-x Rendering Layer            │
│     CCDirector, CCScene, CCSprite           │
└─────────────────────────────────────────────┘
```

### 目录结构

```
dependencies/
├── cegui/
│   ├── CEGUI/                     # CEGUI 核心库
│   │   ├── include/               # 头文件
│   │   │   ├── CEGUI.h           # 主头文件
│   │   │   ├── CEGUIBase.h
│   │   │   ├── CEGUIWindow.h
│   │   │   ├── CEGUIWindowManager.h
│   │   │   ├── BinLayout/        # 二进制布局系统
│   │   │   └── elements/         # UI 控件
│   │   └── src/                  # 源代码
│   ├── project/                   # 项目文件
│   │   └── win32/
│   │       └── cegui.win32.vcxproj
│   └── lib/                      # 编译输出
│       └── Release.win32/
│           └── cegui.lib
├── FreeType/                      # 字体渲染库
│   └── freetype-2.4.9/
├── SILLY-0.1.0/                   # 图像加载库
└── pcre-8.31/                     # 正则表达式库
```

---

## 渲染器实现

### 自定义 CEGUI 渲染器

CEGUI 需要实现一个渲染器接口来与底层图形 API 对接：

```cpp
// 自定义 CEGUI 渲染器 (推测实现)
class CocosCEGUIRenderer : public CEGUI::Renderer
{
public:
    CocosCEGUIRenderer();
    virtual ~CocosCEGUIRenderer();

    // 渲染接口
    virtual void addQuad(const CEGUI::Rect& dest_rect,
                        float z,
                        const CEGUI::Texture* tex,
                        const CEGUI::Rect& texture_rect,
                        const CEGUI::ColourRect& colours,
                        CEGUI::QuadSplitMode quad_split_mode);

    virtual void doRender();
    virtual void clearRenderList();

    // 纹理管理
    virtual CEGUI::Texture* createTexture();
    virtual CEGUI::Texture* createTexture(const CEGUI::String& filename,
                                         const CEGUI::String& resourceGroup);
    virtual CEGUI::Texture* createTexture(float size);
    virtual void destroyTexture(CEGUI::Texture* texture);
    virtual void destroyAllTextures();

    // 视口和显示设置
    virtual void setDisplaySize(const CEGUI::Size& size);
    virtual const CEGUI::Size& getDisplaySize() const;
    virtual const CEGUI::Vector2& getDisplayDPI() const;

    // 最大纹理大小
    virtual uint getMaxTextureSize() const;

private:
    struct QuadInfo
    {
        cocos2d::CCPoint position;
        cocos2d::CCSize size;
        cocos2d::ccColor4F color;
        cocos2d::CCRect texCoords;
        cocos2d::CCTexture2D* texture;
    };

    std::vector<QuadInfo> m_quadList;  // 四边形渲染队列
    CEGUI::Size m_displaySize;         // 显示尺寸
    cocos2d::CCSpriteBatchNode* m_batchNode;  // 批量渲染节点
};
```

### 批量渲染优化

```cpp
void CocosCEGUIRenderer::doRender()
{
    // 按纹理分组
    std::map<CCTexture2D*, std::vector<QuadInfo>> batches;

    for (const QuadInfo& quad : m_quadList) {
        batches[quad.texture].push_back(quad);
    }

    // 批量渲染每个纹理组
    for (auto& pair : batches) {
        CCTexture2D* texture = pair.first;
        std::vector<QuadInfo>& quads = pair.second;

        // 设置纹理
        texture->retain();
        glBindTexture(GL_TEXTURE_2D, texture->getName());

        // 构建顶点缓冲区
        ccV3F_C4B_T2F_Quad* vertices = new ccV3F_C4B_T2F_Quad[quads.size()];

        for (size_t i = 0; i < quads.size(); ++i) {
            BuildQuadVertices(quads[i], vertices[i]);
        }

        // 提交到 GPU
        glVertexAttribPointer(kCCVertexAttrib_Position, 3, GL_FLOAT, GL_FALSE,
                             sizeof(ccV3F_C4B_T2F), &vertices[0].tl.vertices);
        glVertexAttribPointer(kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE,
                             sizeof(ccV3F_C4B_T2F), &vertices[0].tl.colors);
        glVertexAttribPointer(kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE,
                             sizeof(ccV3F_C4B_T2F), &vertices[0].tl.texCoords);

        glDrawArrays(GL_TRIANGLES, 0, quads.size() * 6);

        delete[] vertices;
        texture->release();
    }

    // 清空渲染队列
    m_quadList.clear();
}
```

### 坐标系统转换

```cpp
// CEGUI 使用左上角为原点，需要转换到 Cocos2d-x 的左下角坐标系
cocos2d::CCPoint ConvertCEGUIToCocos2d(const CEGUI::Vector2& ceguiPos)
{
    float screenHeight = cocos2d::CCDirector::sharedDirector()
                         ->getWinSize().height;

    return cocos2d::CCPoint(
        ceguiPos.d_x,
        screenHeight - ceguiPos.d_y  // 翻转 Y 坐标
    );
}
```

---

## 资源管理

### 资源类型

| 资源类型 | 文件格式 | 说明 |
|---------|---------|------|
| **Imageset** | .imageset | 图像集定义 |
| **Font** | .font | 字体定义 |
| **Scheme** | .scheme | 皮肤方案 |
| **Layout** | .layout (binary) | 窗口布局 |
| **LookNFeel** | .looknfeel | 控件外观 |

### 二进制布局系统

MT3 使用 **Binary Layout** 格式优化加载性能：

```cpp
// BinLayout 文件头结构
struct BinLayoutHeader
{
    uint32_t magic;         // 'BLAY'
    uint32_t version;       // 版本号
    uint32_t windowCount;   // 窗口数量
    uint32_t dataOffset;    // 数据偏移
};

// BinLayout 序列化器
class CEGUIBinLayoutFileSerializer
{
public:
    void Serialize(const CEGUI::Window* window, const std::string& filename)
    {
        CEGUIFileStream stream(filename, CEGUIFileStream::Write);

        // 写入文件头
        BinLayoutHeader header;
        header.magic = 0x59414C42;  // 'BLAY'
        header.version = 1;
        header.windowCount = CountWindows(window);
        stream.Write(&header, sizeof(header));

        // 序列化窗口树
        SerializeWindow(window, stream);
    }

    CEGUI::Window* Deserialize(const std::string& filename)
    {
        CEGUIFileStream stream(filename, CEGUIFileStream::Read);

        // 读取文件头
        BinLayoutHeader header;
        stream.Read(&header, sizeof(header));

        if (header.magic != 0x59414C42) {
            throw std::runtime_error("Invalid BinLayout file");
        }

        // 反序列化窗口树
        return DeserializeWindow(stream);
    }

private:
    void SerializeWindow(const CEGUI::Window* window, CEGUIFileStream& stream);
    CEGUI::Window* DeserializeWindow(CEGUIFileStream& stream);
};
```

### 资源加载流程

```cpp
// CEGUI 初始化和资源加载
void InitializeCEGUI()
{
    // 1. 创建渲染器
    CEGUI::CocosCEGUIRenderer& renderer =
        CEGUI::CocosCEGUIRenderer::create();

    // 2. 创建 CEGUI 系统
    CEGUI::System::create(renderer);

    // 3. 设置资源路径
    CEGUI::DefaultResourceProvider* rp =
        static_cast<CEGUI::DefaultResourceProvider*>(
            CEGUI::System::getSingleton().getResourceProvider());

    rp->setResourceGroupDirectory("schemes", "cegui/schemes/");
    rp->setResourceGroupDirectory("imagesets", "cegui/imagesets/");
    rp->setResourceGroupDirectory("fonts", "cegui/fonts/");
    rp->setResourceGroupDirectory("layouts", "cegui/layouts/");
    rp->setResourceGroupDirectory("looknfeels", "cegui/looknfeel/");

    // 4. 加载皮肤方案
    CEGUI::SchemeManager::getSingleton().create("MT3Scheme.scheme");

    // 5. 设置默认字体
    CEGUI::FontManager::getSingleton().create("SimHei-14.font");
    CEGUI::System::getSingleton().setDefaultFont("SimHei-14");

    // 6. 设置默认鼠标光标
    CEGUI::System::getSingleton().setDefaultMouseCursor("MT3Scheme", "MouseArrow");

    // 7. 创建根窗口
    CEGUI::Window* rootWindow = CEGUI::WindowManager::getSingleton()
        .createWindow("DefaultWindow", "Root");
    CEGUI::System::getSingleton().setGUISheet(rootWindow);
}
```

---

## 窗口系统

### 窗口层级结构

```cpp
// 窗口树示例
Root (DefaultWindow)
├── MainMenuWindow
│   ├── TitleLabel
│   ├── PlayButton
│   ├── SettingsButton
│   └── QuitButton
├── GameHUDWindow
│   ├── HealthBar
│   ├── ManaBar
│   ├── SkillButtonPanel
│   │   ├── SkillButton1
│   │   ├── SkillButton2
│   │   └── SkillButton3
│   └── MinimapWindow
└── DialogWindow
    ├── DialogTitle
    ├── DialogText
    └── DialogButtonPanel
        ├── OKButton
        └── CancelButton
```

### 动态创建窗口

```cpp
// C++ 方式创建窗口
CEGUI::Window* CreateLoginDialog()
{
    CEGUI::WindowManager& wmgr = CEGUI::WindowManager::getSingleton();

    // 创建对话框框架
    CEGUI::FrameWindow* dialog = static_cast<CEGUI::FrameWindow*>(
        wmgr.createWindow("MT3Scheme/FrameWindow", "LoginDialog"));

    dialog->setPosition(CEGUI::UVector2(CEGUI::UDim(0.25f, 0), CEGUI::UDim(0.25f, 0)));
    dialog->setSize(CEGUI::UVector2(CEGUI::UDim(0.5f, 0), CEGUI::UDim(0.5f, 0)));
    dialog->setText("登录");

    // 用户名标签
    CEGUI::Window* usernameLabel = wmgr.createWindow("MT3Scheme/Label", "UsernameLabel");
    usernameLabel->setText("用户名:");
    usernameLabel->setPosition(CEGUI::UVector2(CEGUI::UDim(0.1f, 0), CEGUI::UDim(0.2f, 0)));
    usernameLabel->setSize(CEGUI::UVector2(CEGUI::UDim(0.3f, 0), CEGUI::UDim(0.1f, 0)));
    dialog->addChildWindow(usernameLabel);

    // 用户名输入框
    CEGUI::Editbox* usernameEdit = static_cast<CEGUI::Editbox*>(
        wmgr.createWindow("MT3Scheme/Editbox", "UsernameEdit"));
    usernameEdit->setPosition(CEGUI::UVector2(CEGUI::UDim(0.4f, 0), CEGUI::UDim(0.2f, 0)));
    usernameEdit->setSize(CEGUI::UVector2(CEGUI::UDim(0.5f, 0), CEGUI::UDim(0.1f, 0)));
    dialog->addChildWindow(usernameEdit);

    // 密码标签
    CEGUI::Window* passwordLabel = wmgr.createWindow("MT3Scheme/Label", "PasswordLabel");
    passwordLabel->setText("密码:");
    passwordLabel->setPosition(CEGUI::UVector2(CEGUI::UDim(0.1f, 0), CEGUI::UDim(0.35f, 0)));
    passwordLabel->setSize(CEGUI::UVector2(CEGUI::UDim(0.3f, 0), CEGUI::UDim(0.1f, 0)));
    dialog->addChildWindow(passwordLabel);

    // 密码输入框
    CEGUI::Editbox* passwordEdit = static_cast<CEGUI::Editbox*>(
        wmgr.createWindow("MT3Scheme/Editbox", "PasswordEdit"));
    passwordEdit->setPosition(CEGUI::UVector2(CEGUI::UDim(0.4f, 0), CEGUI::UDim(0.35f, 0)));
    passwordEdit->setSize(CEGUI::UVector2(CEGUI::UDim(0.5f, 0), CEGUI::UDim(0.1f, 0)));
    passwordEdit->setTextMasking(true);  // 密码掩码
    dialog->addChildWindow(passwordEdit);

    // 登录按钮
    CEGUI::PushButton* loginButton = static_cast<CEGUI::PushButton*>(
        wmgr.createWindow("MT3Scheme/Button", "LoginButton"));
    loginButton->setText("登录");
    loginButton->setPosition(CEGUI::UVector2(CEGUI::UDim(0.3f, 0), CEGUI::UDim(0.6f, 0)));
    loginButton->setSize(CEGUI::UVector2(CEGUI::UDim(0.2f, 0), CEGUI::UDim(0.1f, 0)));
    dialog->addChildWindow(loginButton);

    // 取消按钮
    CEGUI::PushButton* cancelButton = static_cast<CEGUI::PushButton*>(
        wmgr.createWindow("MT3Scheme/Button", "CancelButton"));
    cancelButton->setText("取消");
    cancelButton->setPosition(CEGUI::UVector2(CEGUI::UDim(0.55f, 0), CEGUI::UDim(0.6f, 0)));
    cancelButton->setSize(CEGUI::UVector2(CEGUI::UDim(0.2f, 0), CEGUI::UDim(0.1f, 0)));
    dialog->addChildWindow(cancelButton);

    return dialog;
}
```

### 从布局文件加载

```cpp
// 从二进制布局文件加载
CEGUI::Window* LoadLayout(const std::string& layoutName)
{
    // 使用自定义的 BinLayout 加载器
    CEGUIBinLayoutFileSerializer serializer;

    std::string fullPath = "layouts/" + layoutName + ".blayout";
    CEGUI::Window* window = serializer.Deserialize(fullPath);

    return window;
}

// Lua 脚本中使用
-- Lua
local mainMenuWindow = CEGUI.LoadLayout("MainMenu")
CEGUI.System:getSingleton():getGUISheet():addChildWindow(mainMenuWindow)
```

---

## 事件系统

### 事件订阅机制

```cpp
// C++ 事件处理
class LoginDialogHandler
{
public:
    LoginDialogHandler(CEGUI::Window* dialog)
    {
        // 订阅登录按钮点击事件
        CEGUI::PushButton* loginBtn = static_cast<CEGUI::PushButton*>(
            dialog->getChild("LoginButton"));

        loginBtn->subscribeEvent(
            CEGUI::PushButton::EventClicked,
            CEGUI::Event::Subscriber(&LoginDialogHandler::OnLoginClicked, this));

        // 订阅取消按钮
        CEGUI::PushButton* cancelBtn = static_cast<CEGUI::PushButton*>(
            dialog->getChild("CancelButton"));

        cancelBtn->subscribeEvent(
            CEGUI::PushButton::EventClicked,
            CEGUI::Event::Subscriber(&LoginDialogHandler::OnCancelClicked, this));
    }

private:
    bool OnLoginClicked(const CEGUI::EventArgs& e)
    {
        // 获取输入框内容
        CEGUI::Editbox* usernameEdit = static_cast<CEGUI::Editbox*>(
            CEGUI::WindowManager::getSingleton().getWindow("UsernameEdit"));
        CEGUI::Editbox* passwordEdit = static_cast<CEGUI::Editbox*>(
            CEGUI::WindowManager::getSingleton().getWindow("PasswordEdit"));

        CEGUI::String username = usernameEdit->getText();
        CEGUI::String password = passwordEdit->getText();

        // 执行登录逻辑
        NetworkManager::GetInstance()->Login(username, password);

        return true;  // 事件已处理
    }

    bool OnCancelClicked(const CEGUI::EventArgs& e)
    {
        // 关闭对话框
        CEGUI::Window* dialog = static_cast<const CEGUI::WindowEventArgs&>(e).window;
        dialog->hide();

        return true;
    }
};
```

### Lua 脚本事件绑定

```lua
-- Lua 事件处理
function OnLoginButtonClicked(args)
    local usernameEdit = winMgr:getWindow("UsernameEdit")
    local passwordEdit = winMgr:getWindow("PasswordEdit")

    local username = usernameEdit:getText()
    local password = passwordEdit:getText()

    -- 调用 C++ 登录函数
    NetworkManager:Login(username, password)

    return true
end

-- 绑定事件
local loginBtn = winMgr:getWindow("LoginButton")
loginBtn:subscribeScriptedEvent("Clicked", "OnLoginButtonClicked")
```

### 常用事件类型

| 事件名称 | 触发时机 | 应用场景 |
|---------|---------|---------|
| **EventClicked** | 鼠标点击 | 按钮、可点击控件 |
| **EventMouseEnters** | 鼠标进入 | 悬停效果 |
| **EventMouseLeaves** | 鼠标离开 | 悬停效果 |
| **EventTextChanged** | 文本改变 | 输入框验证 |
| **EventSelectionChanged** | 选择改变 | 列表、下拉框 |
| **EventShown** | 窗口显示 | 初始化 UI |
| **EventHidden** | 窗口隐藏 | 清理资源 |

---

## 皮肤和主题

### Scheme 文件结构

```xml
<!-- MT3Scheme.scheme -->
<?xml version="1.0" ?>
<GUIScheme Name="MT3Scheme">
    <!-- 图像集 -->
    <Imageset Name="MT3Images" Filename="MT3Images.imageset" />

    <!-- 字体 -->
    <Font Name="SimHei-14" Filename="SimHei-14.font" />
    <Font Name="SimHei-18" Filename="SimHei-18.font" />

    <!-- 外观定义 -->
    <LookNFeel Filename="MT3.looknfeel" />

    <!-- 窗口工厂 -->
    <WindowSet Filename="CEGUIFalagardWLBase" />

    <!-- 窗口别名 -->
    <FalagardMapping WindowType="MT3Scheme/Button"
                    TargetType="CEGUI/PushButton"
                    Renderer="Falagard/Button"
                    LookNFeel="MT3Scheme/Button" />

    <FalagardMapping WindowType="MT3Scheme/Editbox"
                    TargetType="CEGUI/Editbox"
                    Renderer="Falagard/Editbox"
                    LookNFeel="MT3Scheme/Editbox" />

    <FalagardMapping WindowType="MT3Scheme/FrameWindow"
                    TargetType="CEGUI/FrameWindow"
                    Renderer="Falagard/FrameWindow"
                    LookNFeel="MT3Scheme/FrameWindow" />
</GUIScheme>
```

### LookNFeel 外观定义

```xml
<!-- MT3.looknfeel - 按钮外观 -->
<WidgetLook name="MT3Scheme/Button">
    <PropertyDefinition name="NormalImage" initialValue="" redrawOnWrite="true" />
    <PropertyDefinition name="HoverImage" initialValue="" redrawOnWrite="true" />
    <PropertyDefinition name="PushedImage" initialValue="" redrawOnWrite="true" />
    <PropertyDefinition name="DisabledImage" initialValue="" redrawOnWrite="true" />

    <ImagerySection name="normal">
        <ImageryComponent>
            <Area>
                <Dim type="LeftEdge"><AbsoluteDim value="0" /></Dim>
                <Dim type="TopEdge"><AbsoluteDim value="0" /></Dim>
                <Dim type="RightEdge"><UnifiedDim scale="1" type="RightEdge" /></Dim>
                <Dim type="BottomEdge"><UnifiedDim scale="1" type="BottomEdge" /></Dim>
            </Area>
            <Image type="StretchedImage" imageset="MT3Images" image="ButtonNormal" />
            <VertFormat type="Stretched" />
            <HorzFormat type="Stretched" />
        </ImageryComponent>
    </ImagerySection>

    <ImagerySection name="hover">
        <ImageryComponent>
            <Area><!-- 同上 --></Area>
            <Image type="StretchedImage" imageset="MT3Images" image="ButtonHover" />
            <VertFormat type="Stretched" />
            <HorzFormat type="Stretched" />
        </ImageryComponent>
    </ImagerySection>

    <StateImagery name="Normal">
        <Layer><Section section="normal" /></Layer>
    </StateImagery>

    <StateImagery name="Hover">
        <Layer><Section section="hover" /></Layer>
    </StateImagery>

    <StateImagery name="Pushed">
        <Layer><Section section="pushed" /></Layer>
    </StateImagery>

    <StateImagery name="Disabled">
        <Layer><Section section="disabled" /></Layer>
    </StateImagery>
</WidgetLook>
```

---

## 性能优化

### 1. 批量渲染

```cpp
// 合并相同纹理的渲染调用
class CEGUIBatchRenderer
{
public:
    void BeginBatch()
    {
        m_currentBatch.clear();
    }

    void AddQuad(const QuadInfo& quad)
    {
        // 检查是否需要切换纹理
        if (m_currentTexture && m_currentTexture != quad.texture) {
            FlushBatch();
        }

        m_currentTexture = quad.texture;
        m_currentBatch.push_back(quad);

        // 达到批次大小限制时刷新
        if (m_currentBatch.size() >= MAX_BATCH_SIZE) {
            FlushBatch();
        }
    }

    void EndBatch()
    {
        FlushBatch();
    }

private:
    void FlushBatch()
    {
        if (m_currentBatch.empty()) return;

        // 批量提交到 GPU
        RenderQuads(m_currentBatch, m_currentTexture);

        m_currentBatch.clear();
        m_currentTexture = nullptr;
    }

    static const size_t MAX_BATCH_SIZE = 100;
    std::vector<QuadInfo> m_currentBatch;
    CCTexture2D* m_currentTexture;
};
```

### 2. 延迟更新

```cpp
// 仅在可见时更新
class OptimizedCEGUIWindow : public CEGUI::Window
{
public:
    virtual void update(float elapsed)
    {
        // 不可见时跳过更新
        if (!isVisible()) return;

        // 延迟更新：每 N 帧更新一次非关键 UI
        if (m_updateCounter++ % m_updateInterval == 0) {
            PerformUpdate(elapsed);
        }
    }

private:
    int m_updateCounter = 0;
    int m_updateInterval = 5;  // 每 5 帧更新一次
};
```

### 3. 纹理图集

```xml
<!-- 使用纹理图集减少纹理切换 -->
<Imageset Name="MT3UIAtlas" Imagefile="ui_atlas.png" NativeHorzRes="1024" NativeVertRes="1024">
    <Image Name="ButtonNormal" XPos="0" YPos="0" Width="128" Height="64" />
    <Image Name="ButtonHover" XPos="128" YPos="0" Width="128" Height="64" />
    <Image Name="ButtonPushed" XPos="256" YPos="0" Width="128" Height="64" />
    <Image Name="PanelBG" XPos="0" YPos="64" Width="512" Height="512" />
    <!-- 更多图像 -->
</Imageset>
```

### 4. 对象池

```cpp
// 窗口对象池
class CEGUIWindowPool
{
public:
    CEGUI::Window* AcquireWindow(const std::string& type)
    {
        auto& pool = m_pools[type];

        if (!pool.empty()) {
            CEGUI::Window* window = pool.back();
            pool.pop_back();
            window->setVisible(true);
            return window;
        }

        // 池中没有，创建新窗口
        return CEGUI::WindowManager::getSingleton().createWindow(type);
    }

    void ReleaseWindow(CEGUI::Window* window)
    {
        window->setVisible(false);
        window->removeAllEvents();

        std::string type = window->getType();
        m_pools[type].push_back(window);
    }

private:
    std::map<std::string, std::vector<CEGUI::Window*>> m_pools;
};
```

---

## 最佳实践

### 1. 资源命名规范

```
方案: MT3Scheme
控件: MT3Scheme/Button, MT3Scheme/Editbox
图像集: MT3Images, MT3Icons
字体: SimHei-14, Arial-16
布局: MainMenu.blayout, GameHUD.blayout
```

### 2. 层级组织

```
Root
├── Background Layer (Z=0)
├── Game World Layer (Z=1)
├── HUD Layer (Z=2)
├── Dialog Layer (Z=3)
└── Tooltip Layer (Z=4)
```

### 3. 内存管理

```cpp
// 及时销毁不用的窗口
CEGUI::WindowManager::getSingleton().destroyWindow(window);

// 或使用智能指针
class WindowPtr
{
public:
    WindowPtr(CEGUI::Window* window) : m_window(window) {}
    ~WindowPtr()
    {
        if (m_window) {
            CEGUI::WindowManager::getSingleton().destroyWindow(m_window);
        }
    }

private:
    CEGUI::Window* m_window;
};
```

### 4. 事件管理

```cpp
// 使用 RAII 管理事件订阅
class EventConnection
{
public:
    EventConnection(CEGUI::Window* window, const std::string& event,
                   CEGUI::Event::Subscriber subscriber)
        : m_connection(window->subscribeEvent(event, subscriber))
    {}

    ~EventConnection()
    {
        m_connection->disconnect();
    }

private:
    CEGUI::Event::Connection* m_connection;
};
```

---

## 总结

MT3 项目的 CEGUI 集成展示了：

✅ **自定义渲染器**: 与 Cocos2d-x 无缝集成
✅ **二进制布局**: 优化加载性能
✅ **批量渲染**: 减少 draw call
✅ **对象池**: 优化内存分配
✅ **Lua 脚本**: 灵活的 UI 逻辑

CEGUI 为 MT3 提供了强大而灵活的 UI 系统，适合复杂的 MMO 游戏 UI 需求。

---

**相关文档**:
- [C++架构深度分析.md](./C++架构深度分析.md) - C++ 架构详解
- [项目概述.md](./项目概述.md) - 整体架构
- [编译指南.md](./编译指南.md) - 编译 CEGUI 库

**参考资源**:
- [CEGUI 官方文档](http://cegui.org.uk/wiki/)
- [Crazy Eddie's GUI System](http://cegui.org.uk/)

**文档版本**: 1.0
**最后更新**: 2025-10-12
**维护状态**: ✅ 完成
