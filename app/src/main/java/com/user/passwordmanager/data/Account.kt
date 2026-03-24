package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

//Entity 定义一个Table
@Entity(tableName = "Account")

data class Account(
    @PrimaryKey(autoGenerate = true) val accountId: Int=0,
    val webSiteName:String,
    val webSiteUrl:String,
    val userName:String,
    val encryptedPassword: String,
    val passwordStrength: Int
)
