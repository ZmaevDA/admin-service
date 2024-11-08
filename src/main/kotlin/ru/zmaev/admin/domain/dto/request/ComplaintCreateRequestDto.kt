package ru.zmaev.admin.domain.dto.request

import lombok.Data

@Data
data class ComplaintCreateRequestDto(
    var buildId: Long,
    var reason: String,
    var description: String
)
