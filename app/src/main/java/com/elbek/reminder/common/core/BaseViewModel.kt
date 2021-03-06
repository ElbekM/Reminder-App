package com.elbek.reminder.common.core

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context by lazy { getApplication<Application>() }
    protected val subscriptions = CompositeDisposable()

    val closeCommand = Command()
    val requestPermissionsCommand = TCommand<Pair<List<String>, Int>>()
    val showPermissionDialogDeniedByUserCommand = TCommand<Pair<String, Int>>()

    override fun onCleared() {
        subscriptions.dispose()
        subscriptions.clear()
        super.onCleared()
    }

    open fun start() {}
    open fun stop() {}
    open fun destroy() {}

    open fun onPermissionsResult(requestCode: Int) { }
    open fun permissionDeniedByUser(requestCode: Int) { }

    //TODO: implement navigation func
    open fun navigateTo() {}
    open fun navigateToMainScreen() {}
    open fun back() {
        closeCommand.call()
    }

    protected fun isPermissionsGranted(vararg permissions: String): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    protected fun requestPermissions(permissions: List<String>, requestCode: Int)
            = requestPermissionsCommand.call(Pair(permissions, requestCode))

    protected fun showToast(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    protected fun processException(exception: Exception) {
        when (exception) {
            is ConnectException -> {
                showToast("ConnectException")
            }
            is SocketTimeoutException -> {
                showToast("SocketTimeoutException")
            }
            else -> showToast("Something went wrong")
        }
    }

    protected fun logError(exception: Exception) {
        exception.printStackTrace()
    }

    protected fun Disposable.addToSubscriptions() { subscriptions.add(this) }

    fun BaseViewModel.getResString(@StringRes resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)

    fun BaseViewModel.getResInteger(@IntegerRes resId: Int): Int =
        context.resources.getInteger(resId)

    fun BaseViewModel.getResColor(@ColorRes resId: Int): Int =
        context.resources.getColor(resId)
}
