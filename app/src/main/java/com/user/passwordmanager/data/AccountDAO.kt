package com.user.passwordmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDAO {
     @Query("SELECT * FROM Account")
     fun getAllAccount(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(insert: Account)

    @Delete
    suspend fun deleteAccount(delete: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("SELECT * FROM Account WHERE webSiteName LIKE '%' ||:search || '%' ORDER BY webSiteName ASC")
    suspend fun searchWebsiteName(search: String): List<Account>
}