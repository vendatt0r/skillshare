package com.example.skillshare.ui.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchanges")
data class Exchange(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val adId: Long,
    val fromUserId: Long,
    val toUserId: Long,

    val offerText: String,

    val fromUserConfirmed: Boolean = false, // 👈 НОВОЕ
    val toUserConfirmed: Boolean = false,   // 👈 НОВОЕ

    val status: ExchangeStatus = ExchangeStatus.PENDING
)

enum class ExchangeStatus {
    PENDING,
    ACCEPTED,
    COMPLETING, // 🔹 новый статус: один участник нажал "Завершить"
    COMPLETED,
    CANCELLED
}