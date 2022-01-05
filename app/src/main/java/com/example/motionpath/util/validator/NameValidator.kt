package com.example.motionpath.util.validator

class NameValidator(private val name: String?): Validator {
    override fun validate(): Boolean {
        if (name == null) return false
        if (name.isBlank() || name.isEmpty()) return false

        // TODO: check has restricted characters
        if (name.matches(".*\\d.*".toRegex())) return false

        return true
    }
}