package ru.zmaev.admin.extension.transformer.toResponse

import ru.zmaev.admin.domain.ComplaintStatus
import ru.zmaev.admin.domain.dto.response.ComplaintResponseDto
import ru.zmaev.admin.domain.entity.Complaint

fun Complaint.toComplaintResponse(): ComplaintResponseDto {
    return ComplaintResponseDto(
        id,
        buildId,
        userId,
        resolverUserId,
        reason,
        description,
        ComplaintStatus.valueOf(status),
        createdAt,
        updatedAt
    )
}