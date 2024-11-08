package ru.zmaev.admin.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import ru.zmaev.commonlib.exception.message.ErrorMessage

@Tag(name = "Keycloak Admin API", description = "API для управления пользователями Keycloak")
interface KeycloakAdminApi {

    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Успешное изменение статуса пользователя",
            content = [Content(mediaType = "application/json")]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Сущности не существует",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            )
        ]
    )
    @Operation(summary = "Изменение статуса пользователя", description = "Access: ROLE_ADMIN")
    fun setUserEnabled(
        @Parameter(description = "UUID пользователя") uuid: String,
        @Parameter(description = "Новый статус пользователя") enabled: Boolean
    ): ResponseEntity<Unit>

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Успешное назначение роли пользователя",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный запрос",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Сущности не существует",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Данная роль уже есть у пользователя",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            )
        ]
    )
    @Operation(summary = "Назначение роли пользователя", description = "Access: ROLE_ADMIN")
    fun addUserRole(
        @Parameter(description = "UUID пользователя") uuid: String,
        @Parameter(description = "Новая роль пользователя") role: String
    ): ResponseEntity<Unit>

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Успешное удаление роли пользователя",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Сущности не существует",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный запрос",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorMessage::class)
                )]
            )
        ]
    )
    @Operation(summary = "Удаление роли пользователя", description = "Access: ROLE_ADMIN")
    fun removeUserRole(
        @Parameter(description = "UUID пользователя") uuid: String,
        @Parameter(description = "Роль пользователя") role: String
    ): ResponseEntity<Unit>
}
