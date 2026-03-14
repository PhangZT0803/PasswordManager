package com.user.passwordmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface AccountLabelDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(insert: AccountLabel)

    @Delete
    suspend fun deleteLabel(delete: AccountLabel)

}