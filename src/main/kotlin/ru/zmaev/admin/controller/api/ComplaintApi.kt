package ru.zmaev.admin.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.ErrorMessage
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import ru.zmaev.admin.domain.dto.request.ComplaintCreateRequestDto
import ru.zmaev.admin.domain.dto.response.ComplaintResponseDto

@Tag(name = "Complaint API", description = "API для работы с сущностью Complaint")
interface ComplaintApi {
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешный возврат списка жалоб",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        )]
    )
    @Operation(
        summary = "Получение списка жалоб",
        description = "Access: ROLE_ADMIN"
    )
    fun findAll(
        @Parameter(description = "Начальная страница") pagePosition: Int,
        @Parameter(description = "Размер страницы") pageSize: Int,
        @Parameter(description = "Id сборки") buildId: Long?,
        @Parameter(description = "Id разрешающего жалобу") resolverUserId: String?,
        @Parameter(description = "Статус жалобы") status: String?
    ): ResponseEntity<Page<ComplaintResponseDto>>

    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное получение пепла войны",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "404",
            description = "Жалобы по переданному id нет",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorMessage::class))]
        )]
    )
    @Operation(
        summary = "Получение жалобы",
        description = "Access: ROLE_ADMIN"
    )
    fun findById(@Parameter(description = "id жалобы") id: Long): ResponseEntity<ComplaintResponseDto>

    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное создание жалобы",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "403",
            description = "Нет прав доступа",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(
                    implementation = ErrorMessage::class
                )
            )]
        )]
    )
    @Operation(summary = "Сохранение новой жалобы", description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN")
    fun create(requestDto: ComplaintCreateRequestDto): ResponseEntity<ComplaintResponseDto>

    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное назначение администратора на решение жалобы",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "403",
            description = "Нет прав доступа",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(
                    implementation = ErrorMessage::class
                )
            )]
        )]
    )
    @Operation(summary = "Назначение администратора на решение жалобы", description = "Access: ROLE_ADMIN")
    fun appoint(id: Long): ResponseEntity<ComplaintResponseDto>
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное разрешение жалобы",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "403",
            description = "Нет прав доступа",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(
                    implementation = ErrorMessage::class
                )
            )]
        ),
            ApiResponse(
                responseCode = "409",
                description = "Текущий пользователя не может решить данную жалобу",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = ErrorMessage::class
                    )
                )]
            )]
    )
    @Operation(summary = "Решение жалобы", description = "Access: ROLE_ADMIN")
    fun complete(id: Long): ResponseEntity<ComplaintResponseDto>
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное отклонение жалобы",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ComplaintResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "403",
            description = "Нет прав доступа",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(
                    implementation = ErrorMessage::class
                )
            )]
        )]
    )
    @Operation(summary = "Отклонение жалобы", description = "Access: ROLE_ADMIN")
    fun reject(id: Long): ResponseEntity<ComplaintResponseDto>
}