package ru.zmaev.admin.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.zmaev.admin.domain.entity.Complaint

@Repository
interface ComplaintRepository : PagingAndSortingRepository<Complaint, Long>, CrudRepository<Complaint, Long> {
    @Query(
        """
        SELECT * FROM complaint 
        WHERE (:status IS NULL OR status = :status)
        AND (:buildId IS NULL OR build_id = :buildId)
        AND (:userResolverId IS NULL OR resolver_user_id = :userResolverId)
        LIMIT :limit OFFSET :offset
    """
    )
    fun findAll(
        @Param("status") status: String?,
        @Param("buildId") buildId: Long?,
        @Param("userResolverId") userResolverId: String?,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int
    ): List<Complaint>
}