package com.dicoding.mysubmission2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @field:ColumnInfo(name = "company")
    val company: String? = null,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = null,

    @field:ColumnInfo(name = "following")
    val following: Int? = null,

    @field:ColumnInfo(name = "public_repos")
    val publicRepos: Int? = null,

    @field:ColumnInfo(name = "location")
    val location: String? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
)