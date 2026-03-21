package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Account",
    foreignKeys = [
        ForeignKey(
            entity = AccountLabel::class,
            parentColumns = ["labelId"],//primary Key
            childColumns = ["labelId"],//foreign Key
            onDelete = ForeignKey.SET_NULL// if table "Accountlabel" deleted ,set "labelId" in table "Account" to null
        )
    ],
    indices = [
        Index(value = ["webSiteUrl", "userName"], unique = true),
        Index(value = ["webSiteName"]),
        Index(value = ["labelId"])
    ]
)

data class Account(
    @PrimaryKey(autoGenerate = true) val accountId: Int=0,
    val webSiteName:String,
    val webSiteUrl:String,
    val userName:String,
    val encryptedPassword: String,
    val passwordStrength: Int,
    val labelId:Int?=null
)
