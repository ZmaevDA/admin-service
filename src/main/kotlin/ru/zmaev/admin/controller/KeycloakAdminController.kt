package ru.zmaev.admin.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.zmaev.admin.controller.api.KeycloakAdminApi
import ru.zmaev.admin.service.KeycloakAdminService

@RestController
@RequestMapping("v1/users")
class KeycloakAdminController(
    private val keycloakAdminService: KeycloakAdminService
) : KeycloakAdminApi {

    @PostMapping("{uuid}/enabled")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun setUserEnabled(
        @PathVariable uuid: String,
        @RequestParam enabled: Boolean
    ): ResponseEntity<Unit> {
        keycloakAdminService.setUserEnabled(uuid, enabled)
        return ResponseEntity.ok().build()
    }

    @PostMapping("{uuid}/roles/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun addUserRole(
        @PathVariable uuid: String,
        @PathVariable role: String
    ): ResponseEntity<Unit> {
        keycloakAdminService.addUserRole(uuid, role)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("{uuid}/roles/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun removeUserRole(
        @PathVariable uuid: String,
        @PathVariable role: String
    ): ResponseEntity<Unit> {
        keycloakAdminService.removeUserRole(uuid, role)
        return ResponseEntity.ok().build()
    }
}
