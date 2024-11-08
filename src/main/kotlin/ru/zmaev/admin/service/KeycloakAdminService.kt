package ru.zmaev.admin.service

interface KeycloakAdminService {
    fun setUserEnabled(uuid: String, enabled: Boolean)
    fun addUserRole(uuid: String, role: String)
    fun removeUserRole(uuid: String, role: String)
}