package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "Setting")
data class Setting (
    @PrimaryKey val settingId: Int = 0,
    val         appPIN:  String,
    val         SetPIN:  Boolean,
    val      mainTheme:  Int = 0
)
