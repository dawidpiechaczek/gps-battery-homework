package com.appsirise.piechaczek.gps.homework

import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

fun Fragment.handlePermissions(
    permissions: List<AppPermission>,
    onAllGranted: (List<AppPermission>) -> Unit,
    onNotGranted: (AppPermission) -> Unit,
    onRationaleNeeded: ((AppPermission) -> Unit)
) {
    permissions.firstOrNull { isPermissionNotGranted(it) }?.let {
        onNotGranted.invoke(it)
    } ?: run {
        permissions.firstOrNull { shouldShowPermissionRationale(it) }?.let {
            onRationaleNeeded.invoke(it)
        } ?: run {
            onAllGranted.invoke(permissions)
        }
    }
}

fun Fragment.isPermissionNotGranted(permission: AppPermission) =
    PermissionChecker.checkSelfPermission(
        requireContext(),
        permission.permissionName
    ) != PermissionChecker.PERMISSION_GRANTED

private fun Fragment.shouldShowPermissionRationale(permission: AppPermission) =
    shouldShowRequestPermissionRationale(permission.permissionName)