package com.user.passwordmanager.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "AccountLabel",
    indices = [Index(value = ["labelName"], unique = true)])
data class AccountLabel(
    @PrimaryKey val labelId: Int = 0,
    val labelName: String
)
