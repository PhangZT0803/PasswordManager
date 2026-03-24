package com.user.passwordmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao //DAO(Data Access Object 定义这个table可以做什么Database的CRUD SQL)
interface AccountDAO {
     @Query("SELECT * FROM Account")
     fun getAllAccount(): Flow<List<Account>>//<List<Account>>意思是一个Account类型 static Array(固定size), Flow<List<Account>>意思是自动监听+自动增加List的size(Dynamic Array)

    @Insert(onConflict = OnConflictStrategy.REPLACE)//如果有冲突就替换
    suspend fun insertAccount(insert: Account)//suspend是挂后台执行一次,返回,结束就销毁性能会比较好.(suspend基本是给Respository或者Domain使用,Flow通常给UI的)

    @Delete
    suspend fun deleteAccount(delete: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("SELECT * FROM Account WHERE webSiteName LIKE '%' ||:search || '%' ORDER BY webSiteName ASC")
    suspend fun searchWebsiteName(search: String): List<Account>
}