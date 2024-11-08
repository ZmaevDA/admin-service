package ru.zmaev.admin.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.zmaev.admin.domain.ComplaintStatus
import ru.zmaev.admin.domain.dto.request.ComplaintCreateRequestDto
import ru.zmaev.admin.domain.dto.request.ComplaintFilterRequestDto
import ru.zmaev.admin.domain.dto.response.ComplaintResponseDto
import ru.zmaev.admin.domain.entity.Complaint
import ru.zmaev.admin.extension.transformer.fromRequest.toComplaintEntity
import ru.zmaev.admin.extension.transformer.toResponse.toComplaintResponse
import ru.zmaev.admin.logger.Log
import ru.zmaev.admin.repository.ComplaintRepository
import ru.zmaev.admin.service.ComplaintService
import ru.zmaev.commonlib.api.SupportServiceApi
import ru.zmaev.commonlib.auth.AuthUtils
import ru.zmaev.commonlib.exception.EntityConflictException
import ru.zmaev.commonlib.exception.EntityNotFountException
import ru.zmaev.commonlib.exception.InternalServerException
import ru.zmaev.commonlib.model.dto.EntityIsExistsResponseDto

@Service
class ComplaintServiceImpl(
    private val complaintRepository: ComplaintRepository,
    private val supportServiceApi: SupportServiceApi
) : ComplaintService {

    companion object : Log()

    override fun findAll(
        pagePosition: Int,
        pageSize: Int,
        complaintFilterRequestDto: ComplaintFilterRequestDto
    ): Page<ComplaintResponseDto> {
        log.info("Fetching all complaints")
        val offset = pageSize * pagePosition
        val complaints = complaintRepository.findAll(
            complaintFilterRequestDto.status,
            complaintFilterRequestDto.buildId,
            complaintFilterRequestDto.resolverUserId,
            pageSize,
            offset
        ).map { it.toComplaintResponse() }
        val total = complaintRepository.count()
        return PageImpl(complaints, PageRequest.of(pagePosition, pageSize), total)
    }

    override fun findById(id: Long): ComplaintResponseDto {
        log.info("Fetching complaint with id: $id")
        val complaint: Complaint = findComplaintByIdOrThrow(id)
        log.info("Fetched complaint with id: $complaint")
        return complaint.toComplaintResponse()
    }

    @Transactional
    override fun create(complaintCreateRequestDto: ComplaintCreateRequestDto): ComplaintResponseDto {
        log.info("Creating new complaint with request dto: $complaintCreateRequestDto")
        var complaint: Complaint = complaintCreateRequestDto.toComplaintEntity()
        isBuildExistsById(complaint.buildId)
        complaint = complaintRepository.save(complaint)
        log.info("Created new complaint: $complaint")
        return complaint.toComplaintResponse()
    }

    @Transactional
    override fun appoint(id: Long): ComplaintResponseDto {
        log.info("Appointing complaint with id: $id")
        var complaint: Complaint = findComplaintByIdOrThrow(id)
        if (ComplaintStatus.valueOf(complaint.status) != ComplaintStatus.CREATED) {
            throw EntityConflictException("Complaint status should be CREATED to appoint it!")
        }
        complaint.resolverUserId = AuthUtils.getCurrentUserId()
        complaint.status = ComplaintStatus.IN_PROGRESS.name
        complaint = complaintRepository.save(complaint)
        log.info("Appointed complaint with id: $id to user: ${complaint.resolverUserId}")
        return complaint.toComplaintResponse()
    }

    @Transactional
    override fun complete(id: Long): ComplaintResponseDto {
        log.info("Completing complaint with id: $id")
        var complaint: Complaint = findComplaintByIdOrThrow(id)
        val currentUserId: String = AuthUtils.getCurrentUserId()
        if (ComplaintStatus.valueOf(complaint.status) != ComplaintStatus.IN_PROGRESS) {
            throw EntityConflictException("Complaint must have status: ${ComplaintStatus.IN_PROGRESS} to be resolved")
        }
        if (currentUserId != complaint.resolverUserId) {
            throw EntityConflictException("User with id: $currentUserId can`t resolve this complaint")
        }
        complaint.resolverUserId = AuthUtils.getCurrentUserId()
        complaint.status = ComplaintStatus.RESOLVED.name
        complaint = complaintRepository.save(complaint)
        log.info("Completed complaint with id: $id by user: ${complaint.resolverUserId}")
        return complaint.toComplaintResponse()
    }

    @Transactional
    override fun reject(id: Long): ComplaintResponseDto {
        log.info("Rejecting complaint with id: $id")
        var complaint: Complaint = findComplaintByIdOrThrow(id)
        if (ComplaintStatus.valueOf(complaint.status) == ComplaintStatus.RESOLVED) {
            throw EntityConflictException("This complaint resolved!")
        }
        complaint.resolverUserId = AuthUtils.getCurrentUserId()
        complaint.status = ComplaintStatus.REJECTED.name
        complaint = complaintRepository.save(complaint)
        log.info("Rejected complaint with id: $id by user: ${complaint.resolverUserId}")
        return complaint.toComplaintResponse()
    }

    private fun findComplaintByIdOrThrow(id: Long): Complaint {
        return complaintRepository.findById(id)
            .orElseThrow { throw EntityNotFountException("Complaint", id) }
    }

    fun isBuildExistsById(id: Long): EntityIsExistsResponseDto {
        log.info("Fetching build with id: $id")
        val call = supportServiceApi.buildExistsById(id)
        val response = call.execute()
        if (response.isSuccessful) {
            if (!response.body()!!.isExists) {
                log.error("Can`t fetch build with id: $id")
                throw EntityNotFountException("Build", id.toString())
            }
            log.info("Fetched build with id: $id")
            return response.body()!!
        } else {
            log.error("Unexpected error while fetching build with id: $id")
            throw InternalServerException("Unexpected error: ${response.code()}")
        }
    }
}
