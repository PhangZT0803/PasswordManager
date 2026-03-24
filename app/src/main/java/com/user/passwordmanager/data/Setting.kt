package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "Setting")
data class Setting (
    @PrimaryKey val settingId: Int = 0,
    val         appPIN:  String,
    val         setPIN:  Boolean,
    val      mainTheme:  Int = 0,//0 = followSystem,1=light,2=dark
    val swipeDirection:  Int = 0//0=left,1=right
)
