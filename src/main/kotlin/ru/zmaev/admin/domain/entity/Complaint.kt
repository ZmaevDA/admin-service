package ru.zmaev.admin.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import ru.zmaev.admin.domain.ComplaintStatus
import java.time.Instant

@Table(name = "complaint")
data class Complaint(

    @Id
    var id: Long? = null,

    var buildId: Long,

    var userId: String,

    var resolverUserId: String?,

    var reason: String,

    var description: String,

    var status: String = ComplaintStatus.CREATED.name,

    var createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()
)
