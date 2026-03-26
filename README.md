# Password Manager — Architecture Guide
> 给 Group Assignment 队友的架构说明，基于这个 Individual Assignment 的代码

---

## Overall Architecture
<img width="1395" height="859" alt="image" src="https://github.com/user-attachments/assets/178654ce-b779-4716-baa4-6111e5857ec5" />

---

## Overall UI Structure

<img width="784" height="634" alt="image" src="https://github.com/user-attachments/assets/6f44f555-d170-4ead-96b9-685ff6c99356" />

Scaffold 提供基本的页面结构，自动处理 TopBar 和 BottomBar 的高度，`innerPadding` 确保内容不被遮住。

```kotlin
Scaffold(
    topBar = { TopAppBar(...) },
    bottomBar = { NavigationBar() },
) { innerPadding ->
    // 内容区，padding 确保不被 TopBar/BottomBar 遮住
}
```

<img width="642" height="486" alt="image" src="https://github.com/user-attachments/assets/83873bee-81e7-401c-8216-2801b0d84906" />

`Column` 垂直排列，`Row` 水平排列，可以互相嵌套。

```kotlin
// Row — 水平排列
Row {
    Text("Name"); Text("ID"); Text("Date")
}

// Column 里嵌套 Row
Column {
    Row { Text("Name:"); TextField() }
    Row { Text("ID:");   TextField() }
    Row {
        Text("Year:"); TextField()
        Text("Sem:");  TextField()
    }
}
```

---

## 各层说明 + Code 示例

### UI Layer
**文件：** `ui/` 目录下所有 Screen

UI 只负责显示和接收用户操作，不处理数据。
状态用 `remember + mutableStateOf` 管理，数据从 ViewModel 的 StateFlow 读取。

```kotlin
// val — 只可以 read，不可以被重新Assign Value
// var — 可以 read 和 write,可以被重新Assign Value

// remember — 缓存，让值在画面重新渲染后不被重置
// mutableStateOf — 监听，当 value 变化时通知 Compose 重新渲染
// 两个合在一起：value 变了会重新渲染，渲染后 value 还在

// by — 语法糖，省掉 .value，val/var 都可以用
var isPasswordVisible by remember { mutableStateOf(false) }
// 等价写法（不用 by，需要手动加 .value）
var isPasswordVisible = remember { mutableStateOf(false) }
isPasswordVisible.value = true  // 需要 .value

// collectAsState() — 把 ViewModel 的 StateFlow 转成 Compose 可以监听的 State
val accounts by viewModel.accounts.collectAsState()

// 用户操作交给 ViewModel 处理，UI 不直接碰数据
viewModel.addAccount(webSiteName, userName, password, webSiteUrl)
```

---

### ViewModel Layer
**文件：** `viewmodel/AccountViewModel.kt`, `viewmodel/SettingViewModel.kt`

UI 和数据层的中间层，负责决定什么时候调 Domain，什么时候调 Repository。
跨屏幕/跨组件的 UI State 也在这里管（本项目暂无）。

```kotlin
//整个流程:Room->Flow->StateFlow->State(collectAsState)
// StateFlow(处于ViewModel层) — 和Flow类似
// map — 对数据做转换，这里是排序
// stateIn — 把 Flow 转换成 StateFlow类型,同时监听Flow
//Flow(处于respository层)就用来反应数据库的情况,但是会有一个special case就是数据库里面没有value于是Flow里面也跟着没有value
//stateIn就会Assign默认value这里被Assign的是emptyList(),因为StateFlow也是一个DynamicList
//当Flow有value就会覆盖掉默认value
val accounts: StateFlow<List<Account>> = repository.allAccount
    .map { list -> list.sortedBy { it.accountId } }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

// viewModelScope.launch — 在后台执行，不阻塞 UI,主线程也就是UI线程这个堵了或者说主线被使用拿去处理数据那么画面会卡住没有反应,严重的话APP直接崩溃.
// suspend function 只能在协程里调用
fun addAccount(passwordText: String, ...) = viewModelScope.launch {
    // 调 Domain 处理数据
    val encrypted = Security.passwordEncryption(passwordText)
    val strength = PasswordStrength.evaluateStrength(passwordText)
    // 调 Repository 存数据
    repository.insertAccount(Account(encryptedPassword = encrypted, ...))
}

// 假如想要解密统一在 VM 层处理，UI 只拿结果.就会长这样.
fun decryptPassword(encryptedPassword: String): String {
    return Security.passwordDecryption(encryptedPassword)
}
// 虽然理论上解密也应该在 VM，但是看密码这个 Action 太简单，没有别的额外东西需要处理
```

---

### Repository Layer
**文件：** `data/AccountRepository.kt`

唯一知道数据从哪里来的地方。
现在只有本地 Room，**Group Assignment 如果要加 Firebase 云端备份，只需要改这一层**，其他层不需要动。

```kotlin
class AccountRepository(
    private val dao: AccountDAO,
    private val settingDao: SettingDAO
    // Group Assignment: 加一个 private val firebaseDao: FirebaseDAO
) {
    // Flow — 自动监听数据库变化，有新数据就自动发出
    val allAccount: Flow<List<Account>> = dao.getAllAccount()
    // Group Assignment就可以改成比如同时从本地和 Firebase 拿，或做云端同步
}
```

---

### Domain Layer
**文件：** `Domain/Security.kt`, `Domain/PasswordStrength.kt`

纯粹的数据处理，不知道数据从哪里来，也不知道数据去哪里。
Group Assignment 基本不需要动这层。

```kotlin
// AES/GCM 加密：对称加密，加解密用同一把密钥
// Android Keystore：密钥存在手机硬件安全区，不会被提取
// IV（初始化向量）：每次加密随机生成，防止相同密码产生相同密文
fun passwordEncryption(plainText: String): String { ... }
fun passwordDecryption(encryptedText: String): String { ... }

// zxcvbn 库评估密码强度，返回 0-4 分
// 0 = 极弱（如 "123456"），4 = 极强
fun evaluateStrength(password: String): Int { ... }
```

---

### Database Layer
**文件：** `data/` 目录

Room 数据库，三个部分：

| 文件 | 职责 |
|------|------|
| `Entity` (Account, Setting) | 定义数据库 Table 的结构 |
| `DAO` (AccountDAO, SettingDAO) | 定义 Table 可以做的 SQL 操作 |
| `AccountDatabase` | 定义数据库包含哪些 Entity 和 DAO |

```kotlin
// Entity — 定义 Table 结构
@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true) val accountId: Int = 0,
    val encryptedPassword: String,  // 只存密文，明文不进数据库
    ...
)

// DAO — 定义操作
@Query("SELECT * FROM Account")
fun getAllAccount(): Flow<List<Account>>  // Flow 自动监听变化，有变化自动通知
//<List<Account>>意思是一个Account类型 static Array(固定size),<List<Int>>就是一个Int Array
//Flow<List<Account>>意思是会自动监听+自动增加List的size(Dynamic Array)

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertAccount(insert: Account)  
//suspend启动就会挂后台执行一次,return,结束就销毁性能会比较好.(suspend基本是给Respository或者Domain使用,Flow通常给UI的)
//suspend就是使用kotlin的协程coroutines,Flow也属于是coroutines的.

// .copy() — 修改 data class 的某个字段，其他字段保持不变
// 写在Data Class里面的variable就会自动被kotlin自动添加一些function 比如 copy(),toString(),equals(),hashCode(),componentN().
val updated = currentSetting.copy(mainTheme = 2)  // 只改 mainTheme，其他不变
```

---

## Dependency Injection (Hilt)

**文件：** `di/AppModule.kt`

Hilt 负责自动创建和传递依赖，不需要手动 `new` 对象。照抄格式就好，不需要深入理解。

```kotlin
// 没有 Hilt — 需要手动创建每一层
val dao = database.AccountDao()
val repository = AccountRepository(dao)
val viewModel = AccountViewModel(repository)

// 有 Hilt — 只需要声明「我需要什么」，Hilt 自动注入
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository  // Hilt 自动传进来
)
```
---

## 文件结构

```
app/src/main/java/com/user/passwordmanager/
├── data/                    # Database Layer
│   ├── Account.kt           # Entity — 帐号表
│   ├── AccountDAO.kt        # DAO — 帐号操作
│   ├── Setting.kt           # Entity — 设置表
│   ├── SettingDAO.kt        # DAO — 设置操作
│   ├── AccountDatabase.kt   # Room Database
│   └── AccountRepository.kt # Repository
├── Domain/                  # Domain Layer
│   ├── Security.kt          # AES/GCM 加密解密
│   └── PasswordStrength.kt  # 密码强度评估
├── viewmodel/               # ViewModel Layer
│   ├── AccountViewModel.kt  # 帐号相关逻辑
│   ├── SettingViewModel.kt  # 设置相关逻辑
│   └── AuthViewModel.kt     # 登录/注册逻辑
├── ui/                      # UI Layer
│   ├── LoginScreen.kt       # PIN 登录/注册画面
│   ├── VaultScreen.kt       # 密码库主画面
│   ├── AddAccountScreen.kt  # 新增/编辑帐号
│   ├── SettingScreen.kt     # 设置画面
│   └── MainNavigation.kt    # 底部导航和 Scaffold
├── di/
│   └── AppModule.kt         # Hilt 依赖注入配置
└── MainActivity.kt          # 入口，NavHost 在这里
```
