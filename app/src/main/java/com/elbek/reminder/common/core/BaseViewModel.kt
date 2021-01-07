package com.elbek.reminder.common.core

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.TCommand
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context by lazy { getApplication<Application>() }
    protected val subscriptionsAppLifecycle = CompositeDisposable()
    protected val subscriptionsFragmentLifecycle = CompositeDisposable()

    val closeCommand = Command()
    val requestPermissionsCommand = TCommand<Pair<List<String>, Int>>()
    val showPermissionDialogDeniedByUserCommand = TCommand<Pair<String, Int>>()

    override fun onCleared() {
        subscriptionsAppLifecycle.dispose()
        subscriptionsAppLifecycle.clear()
        super.onCleared()
    }

    open fun start() {}
    open fun stop() {}

    open fun destroy() {
        subscriptionsFragmentLifecycle.dispose()
        subscriptionsFragmentLifecycle.clear()
    }

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

    protected fun logError(exception: Throwable, message: String = "Error") {
        Log.e("APP", "$message -- ${exception.localizedMessage}")
        exception.printStackTrace()
    }

    protected fun runWithDelay(block: () -> Unit, delay: Long = 500) {
        Handler(Looper.getMainLooper()).postDelayed({
            block()
        }, delay)
    }

    protected fun Disposable.addToSubscriptions() { subscriptionsFragmentLifecycle.add(this) }
    protected fun Disposable.addToAppSubscriptions() { subscriptionsAppLifecycle.add(this) }

    protected fun <T> Single<T>.subscribeOnIoObserveOnMain(block: (T) -> Unit = {}) =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ block(it) }, { logError(it) })

    protected fun <T> Observable<T>.subscribeOnIoObserveOnMain(block: (T) -> Unit = {}) =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ block(it) }, { logError(it) })

    protected fun Completable.subscribeOnIoObserveOnMain(block: () -> Unit = {}) =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ block() }, { logError(it) })

    fun BaseViewModel.getResString(@StringRes resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)

    fun BaseViewModel.getResInteger(@IntegerRes resId: Int): Int =
        context.resources.getInteger(resId)

    fun BaseViewModel.getResColor(@ColorRes resId: Int): Int =
        context.resources.getColor(resId)
}
