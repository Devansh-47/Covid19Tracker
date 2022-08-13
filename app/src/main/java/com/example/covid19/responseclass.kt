package com.example.covid19

data class responseclass (
    var country: String? = "def",
    var province: String? = "def",
    var confirmed: Int? = 0,
    var deaths: Int? = 0,
    var recovered: Int? = 0,
    var active: Int? = 0,
    var date: String? = "def"
)