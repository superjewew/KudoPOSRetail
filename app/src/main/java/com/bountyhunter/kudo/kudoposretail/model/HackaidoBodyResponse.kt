package com.bountyhunter.kudo.kudoposretail.model

import com.squareup.moshi.Json
import java.util.*

/**
 * Created by adrian on 11/18/17.
 */
class HackaidoBodyResponse<T> (
    val code: Int,
    val message: T
)