package com.teknikugm.dompetft.model

data class RegisterRequest (
    val username: String,
    val email: String,
    val password1: String,
    val password2: String,
)