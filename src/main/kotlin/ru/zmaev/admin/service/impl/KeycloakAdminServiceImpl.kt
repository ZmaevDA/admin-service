package ru.zmaev.admin.service.impl

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.zmaev.admin.logger.Log
import ru.zmaev.admin.service.KeycloakAdminService
import ru.zmaev.commonlib.exception.EntityBadRequestException
import ru.zmaev.commonlib.exception.EntityConflictException
import ru.zmaev.commonlib.exception.EntityNotFountException
import ru.zmaev.commonlib.model.enums.Role
import javax.ws.rs.NotFoundException

@Service
class KeycloakAdminServiceImpl(
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}")
    private val realm: String
) : KeycloakAdminService {

    companion object : Log()

    override fun setUserEnabled(uuid: String, enabled: Boolean) {
        log.info("Setting user with uuid: \$uuid to enabled: \$enabled")
        val userResource = getUserResource(uuid)
        val userRepresentation = getUserRepresentationOrThrow(userResource, uuid)
        userRepresentation.isEnabled = enabled
        userResource.update(userRepresentation)
        log.info("User with uuid: $uuid successfully updated to enabled: $enabled")
    }

    override fun addUserRole(uuid: String, role: String) {
        log.info("Adding user with uuid: $uuid role: $role")
        validateRoleOrThrow(role)
        val userResource = getUserResource(uuid)
        val roleToKeycloakRole = role.lowercase().replace("role_", "")
        val realmResource = keycloak.realm(realm)
        val newRole = realmResource.roles()[roleToKeycloakRole].toRepresentation()
        val currentRoles = getUserRolesOrThrow(userResource, uuid)
        if (doesUserHaveRole(currentRoles!!, newRole)) {
            throw EntityConflictException("User already has roles: $currentRoles")
        }
        userResource.roles().realmLevel().add(listOf(newRole))
        log.info("User with uuid: $uuid role added: $role")
    }

    override fun removeUserRole(uuid: String, role: String) {
        log.info("Removing role $role from user with uuid: $uuid")
        validateRoleOrThrow(role)
        val userResource = getUserResource(uuid)
        val roleToKeycloakRole = role.lowercase().replace("role_", "")
        val realmResource = keycloak.realm(realm)

        val currentRoles = getUserRolesOrThrow(userResource, uuid)
        checkAdminRoleOrThrow(currentRoles!!.map { it.toString() })

        val roleToRemove = realmResource.roles()[roleToKeycloakRole].toRepresentation()
        if (!doesUserHaveRole(currentRoles, roleToRemove)) {
            throw EntityNotFountException("User with uuid: $uuid, does not have role: $role")
        }
        userResource.roles().realmLevel().remove(listOf(roleToRemove))
        log.info("Role $role removed from user with uuid: $uuid")
    }

    private fun doesUserHaveRole(
        currentRoles: MutableList<RoleRepresentation>,
        roleToRemove: RoleRepresentation
    ) = currentRoles.contains(roleToRemove)


    private fun validateRoleOrThrow(role: String) {
        try {
            Role.valueOf(role)
        } catch (exception: Exception) {
            log.error("No such role with name: $role!", exception)
            throw EntityBadRequestException("No such role with name: $role!")
        }
    }

    private fun checkAdminRoleOrThrow(currentRoles: List<String>) {
        if (currentRoles.contains("admin")) {
            throw EntityBadRequestException("Admin can`t change role")
        }
    }

    private fun getUserRolesOrThrow(
        userResource: UserResource,
        uuid: String
    ): MutableList<RoleRepresentation>? {
        return try {
            userResource.roles().realmLevel().listAll()
        } catch (e: NotFoundException) {
            log.error("No user with uuid: $uuid")
            throw EntityNotFountException("User", uuid)
        }
    }

    private fun getUserRepresentationOrThrow(
        userResource: UserResource,
        uuid: String
    ): UserRepresentation = try {
        userResource.toRepresentation()
    } catch (e: NotFoundException) {
        log.error("No user with uuid: $uuid")
        throw EntityNotFountException("User", uuid)
    }

    private fun getUserResource(uuid: String): UserResource {
        val realm = keycloak.realm(realm)
        return realm.users()[uuid]
    }
}
