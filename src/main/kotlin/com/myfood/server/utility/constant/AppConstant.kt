package com.myfood.server.utility.constant

object AppConstant {

    const val MY_SQL_DB = "mySql"
    const val SQLITE_DB = "sqlite"
    const val H2_DB = "h2"

    const val DATE_TIME_THAI = 36_000_00 * 7
    const val DATE_TIME_FORMAT_RESPONSE = "d/M/yyyy H:m"
    const val DATE_TIME_FORMAT_REQUEST = "yyyy/M/d H:m"

    const val ACTIVE = "active"
    const val IN_ACTIVE = "in_active"

    const val LOCAL_BACKUP = 0
    const val REMOTE_BACKUP = 1

    const val UN_FAVORITE = 0
    const val FAVORITE = 1

    const val AUTH_LOGIN = "login"
    const val AUTH_LOGOUT = "logout"
    const val AUTH_REFRESH = "refresh"

    const val CATEGORY_TYPE_RECOMMEND = "recommend"
    const val CATEGORY_TYPE_NORMAL = "normal"
}