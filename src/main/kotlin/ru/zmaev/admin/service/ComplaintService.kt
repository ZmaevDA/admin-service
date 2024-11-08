package ru.zmaev.admin.service

import org.springframework.data.domain.Page
import ru.zmaev.admin.domain.dto.request.ComplaintCreateRequestDto
import ru.zmaev.admin.domain.dto.request.ComplaintFilterRequestDto
import ru.zmaev.admin.domain.dto.response.ComplaintResponseDto

interface ComplaintService {
    fun findAll(
        pagePosition: Int,
        pageSize: Int,
        complaintFilterRequestDto: ComplaintFilterRequestDto
    ): Page<ComplaintResponseDto>

    fun findById(id: Long): ComplaintResponseDto
    fun create(complaintCreateRequestDto: ComplaintCreateRequestDto): ComplaintResponseDto
    fun appoint(id: Long): ComplaintResponseDto
    fun complete(id: Long): ComplaintResponseDto
    fun reject(id: Long): ComplaintResponseDto
}