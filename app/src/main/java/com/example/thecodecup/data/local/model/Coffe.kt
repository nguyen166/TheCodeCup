package com.example.thecodecup.data.local.model

import com.example.thecodecup.R

data class Coffee(val id: Int, val name: String, val imageRes: Int, val basePrice: Double)

val staticCoffeeList = listOf(
    Coffee(
        id = 1,
        name = "Americano",
        imageRes = R.drawable.americano,
        basePrice = 2.50
    ),
    Coffee(
        id = 2,
        name = "Cappuccino",
        imageRes = R.drawable.capuchino,
        basePrice = 3.00
    ),
    Coffee(
        id = 3,
        name = "Mocha",
        imageRes = R.drawable.mocha,
        basePrice = 3.25
    ),
    Coffee(
        id = 4,
        name = "Flat White",
        imageRes = R.drawable.flatwhite,
        basePrice = 3.00
    )
)