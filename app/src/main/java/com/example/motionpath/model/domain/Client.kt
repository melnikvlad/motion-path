package com.example.motionpath.model.domain

data class Client(
    val id: Int = -1,
    val name: String = "",
    val categoryType: CategoryType = CategoryType.DEFAULT,
    val description: String = "",
    val goal: String = ""
)