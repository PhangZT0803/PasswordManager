package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "Setting")
data class Setting (
    @PrimaryKey val settingId: Int = 0,
    val faceIDRecorded:  Boolean = false,
    val    initialLock:  Int = 0,
    val   passwordLock:  Int = 1,
    val         appPIN:  Int,
    val         SetPIN:  Boolean,
    val      mainTheme:  Int = 0
)
