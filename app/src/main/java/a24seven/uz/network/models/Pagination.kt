package a24seven.uz.network.models

data class Pagination(   val current: Int? = 0,
                         val previous: Int? = 0,
                         val next: Int? = 0,
                         val total: Int? = 0,
                         val perPage: Int? = 0)