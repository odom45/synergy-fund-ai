package com.synergyfund.ai.data.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
