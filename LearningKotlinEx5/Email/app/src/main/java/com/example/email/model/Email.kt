package com.example.email.model

data class Email(
    val senderName: String,
    val subject: String,
    val preview: String,
    val time: String,
    var isStarred: Boolean = false
)
