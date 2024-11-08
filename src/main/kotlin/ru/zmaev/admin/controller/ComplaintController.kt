package ru.zmaev.admin.controller

import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.zmaev.admin.controller.api.ComplaintApi
import ru.zmaev.admin.domain.dto.request.ComplaintCreateRequestDto
import ru.zmaev.admin.domain.dto.request.ComplaintFilterRequestDto
import ru.zmaev.admin.domain.dto.response.ComplaintResponseDto
import ru.zmaev.admin.service.ComplaintService

@RestController
@Validated
@RequestMapping("v1/complaints")
class ComplaintController(
    private val complaintService: ComplaintService
) : ComplaintApi {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun findAll(
        @RequestParam(defaultValue = "0") pagePosition: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) buildId: Long?,
        @RequestParam(required = false) resolverUserId: String?,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<Page<ComplaintResponseDto>> {
        val complaints: Page<ComplaintResponseDto> = complaintService.findAll(
            pagePosition, pageSize,
            ComplaintFilterRequestDto(status, buildId, resolverUserId)
        )
        return ResponseEntity.ok(complaints)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun findById(@PathVariable id: Long): ResponseEntity<ComplaintResponseDto> {
        val complaintResponseDto: ComplaintResponseDto = complaintService.findById(id)
        return ResponseEntity.ok(complaintResponseDto)
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    override fun create(@RequestBody requestDto: ComplaintCreateRequestDto): ResponseEntity<ComplaintResponseDto> {
        val complaintResponseDto: ComplaintResponseDto = complaintService.create(requestDto)
        return ResponseEntity.ok(complaintResponseDto)
    }

    @PostMapping("{id}/appointment")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun appoint(@PathVariable id: Long): ResponseEntity<ComplaintResponseDto> {
        val complaintResponseDto: ComplaintResponseDto = complaintService.appoint(id)
        return ResponseEntity.ok(complaintResponseDto)
    }

    @PostMapping("{id}/completion")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun complete(@PathVariable id: Long): ResponseEntity<ComplaintResponseDto> {
        val complaintResponseDto: ComplaintResponseDto = complaintService.complete(id)
        return ResponseEntity.ok(complaintResponseDto)
    }

    @PostMapping("{id}/rejection")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun reject(@PathVariable id: Long): ResponseEntity<ComplaintResponseDto> {
        val complaintResponseDto: ComplaintResponseDto = complaintService.reject(id)
        return ResponseEntity.ok(complaintResponseDto)
    }
}
