package com.shwifty.tex.views.settings.mvi

import android.content.Context
import android.support.annotation.StringRes
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import rx.Emitter
import rx.Observable
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
fun handleChangeDirectory(event: SettingsIntents.UpdateWorkingDirectory, saveWorkingDirectory: (Context, File) -> Unit): Observable<ChangeWorkingDirectoryResult> {
    return Observable.create<ChangeWorkingDirectoryResult>({
        val isValid = event.newDirectory.validateWorkingDirectoryCanBeChanged(event.previousDirectory)
        if (isValid is ValidateChangeWorkingDirectoryResult.Error) {
            it.onNext(ChangeWorkingDirectoryResult.CannotChange(isValid.messageRes))
        } else {
            saveWorkingDirectory.invoke(event.context, event.newDirectory)
            if (event.moveFiles) {
                event.previousDirectory.copyRecursively(event.newDirectory, overwrite = true)
                event.previousDirectory.deleteRecursively()
            }
            it.onNext(ChangeWorkingDirectoryResult.Changed())
        }
    }, Emitter.BackpressureMode.BUFFER)
}

sealed class ChangeWorkingDirectoryResult {
    data class CannotChange(@StringRes val error: Int) : ChangeWorkingDirectoryResult()
    class Changed : ChangeWorkingDirectoryResult()
}