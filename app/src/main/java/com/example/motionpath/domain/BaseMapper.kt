package com.example.motionpath.domain

interface BaseMapper<E, D> {
    fun map(model: E): D
}

abstract class AbstractMapper<E, D> : BaseMapper<E, D> {
    operator fun invoke(model: E): D {
        return map(model)
    }
}