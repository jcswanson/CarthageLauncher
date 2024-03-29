package com.codesteem.mylauncher.models

import android.graphics.Bitmap

data class NotificationsModel(
    val time: String?="",
    val userImage: String?="",
    val title: String?="",
    val desc: String?="",
    val mediaImage: Bitmap?=null,
    val notificationType: String?="",
    val alarmTime: String?="",
    val relatedAppPackageName: String? = "",
    val app_name: String? = "",
    val app_icon: String?=null,
    val notiKey: String?=null,

    )
