package ru.zmaev.admin.extension.transformer.fromRequest

import ru.zmaev.admin.domain.ComplaintStatus
import ru.zmaev.admin.domain.dto.request.ComplaintCreateRequestDto
import ru.zmaev.admin.domain.entity.Complaint
import ru.zmaev.commonlib.auth.AuthUtils
import java.time.Instant

fun ComplaintCreateRequestDto.toComplaintEntity() : Complaint {
    return Complaint(
        buildId = buildId,
        userId = AuthUtils.getCurrentUserId(),
        resolverUserId = null,
        reason = reason,
        description = description,
        status = ComplaintStatus.CREATED.name,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )
}