package com.shwifty.tex.views.settings.mvi

import android.content.Context
import android.support.annotation.StringRes
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import rx.subjects.PublishSubject
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
fun handleChangeDirectory(event: SettingsIntents.UpdateWorkingDirectory, saveWorkingDirectory: (Context, File) -> Unit): PublishSubject<ChangeWorkingDirectoryResult> {
    val subject: PublishSubject<ChangeWorkingDirectoryResult> = PublishSubject.create()
    Thread {
        val isValid = event.newDirectory.validateWorkingDirectoryCanBeChanged(event.previousDirectory)
        if (isValid is ValidateChangeWorkingDirectoryResult.Error) {
            subject.onNext(ChangeWorkingDirectoryResult.CannotChange(isValid.messageRes))
        } else {
            saveWorkingDirectory.invoke(event.context, event.newDirectory)
            if (event.moveFiles) {
                event.previousDirectory.copyRecursively(event.newDirectory, overwrite = true)
                event.previousDirectory.deleteRecursively()
            }
            subject.onNext(ChangeWorkingDirectoryResult.Changed())
        }
    }.start()
    return subject
}

sealed class ChangeWorkingDirectoryResult {
    data class CannotChange(@StringRes val error: Int) : ChangeWorkingDirectoryResult()
    class Changed : ChangeWorkingDirectoryResult()
}