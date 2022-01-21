package com.example.motionpath.util.validator

class ValidationProcesser {
    val validators = mutableSetOf<Validator>()
    var result = true

    fun add(validator: Validator) {
        validators.add(validator)
    }

    fun add(vararg validators: Validator) {
        this.validators.addAll(validators)
    }

    fun isValid(): Boolean {
        validators.forEach {
            result = it.validate()
        }

        return result
    }
}