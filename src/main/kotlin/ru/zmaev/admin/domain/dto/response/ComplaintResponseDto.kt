package ru.zmaev.admin.domain.dto.response

import lombok.Data
import ru.zmaev.admin.domain.ComplaintStatus
import java.time.Instant

@Data
data class ComplaintResponseDto(
    val id: Long?,
    var buildId: Long,
    var userId: String,
    var resolverUserId: String?,
    var reason: String,
    var description: String,
    var status: ComplaintStatus,
    var createdAt: Instant,
    var updatedAt: Instant
)
