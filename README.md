                                      Overall Architure
<img width="1395" height="859" alt="image" src="https://github.com/user-attachments/assets/178654ce-b779-4716-baa4-6111e5857ec5" />


                                        Overall UI Screen
<img width="784" height="634" alt="image" src="https://github.com/user-attachments/assets/6f44f555-d170-4ead-96b9-685ff6c99356" />

// Scaffold 提供基本的页面结构
Scaffold(
    topBar = { TopAppBar(...) },      // 顶部
    bottomBar = { NavigationBar() },  // 底部
) { innerPadding ->
   .....
    }
}

<img width="642" height="486" alt="image" src="https://github.com/user-attachments/assets/83873bee-81e7-401c-8216-2801b0d84906" />

// Row — 水平排列，从左到右
Row {
    Text("Name")
    Text("ID")
    Text("Date")
}

// Column 里嵌套 Row（图里下方那个例子）
Column {
    Row { Text("Name:"); TextField() }
    Row { Text("ID:");   TextField() }
    Row {  // 紫色那行
        Text("Year:"); TextField()
        Text("Sem:");  TextField()
    }
}
