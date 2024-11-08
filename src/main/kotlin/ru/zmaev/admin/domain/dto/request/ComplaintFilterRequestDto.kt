package ru.zmaev.admin.domain.dto.request

data class ComplaintFilterRequestDto(
    val status: String?,
    val buildId: Long?,
    val resolverUserId: String?
)
