package com.shwifty.tex

import android.test.mock.MockContext
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.settings.mvi.SettingsViewModel
import com.shwifty.tex.views.settings.mvi.SettingsViewState
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File
import java.util.concurrent.TimeUnit


class SettingsViewModelTest {

    @Mock
    lateinit var mockPreferencesRepository: IPreferenceRepository

    lateinit var viewModel: SettingsViewModel

    val observer: TestObserver<SettingsViewState> = TestObserver()

    val context = MockContext()

    lateinit var emitter: Emitter<SettingsIntents>
    lateinit var mockIntents: Observable<SettingsIntents>

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)

        mockIntents = Observable.create<SettingsIntents> { emitter = it }
        viewModel = SettingsViewModel(mockPreferencesRepository)

        Mockito.`when`(mockPreferencesRepository.getWorkingDirectoryPreference(context)).thenReturn(Observable.just(File("workingDirectory/")))
    }

    @Test
    fun testRestartAppIntent() {
        val events = viewModel.attachView(mockIntents)
        events.subscribe(observer)

        emitter.onNext(SettingsIntents.InitialIntent(context))
        emitter.onNext(SettingsIntents.ChangeTheme(context, AppTheme.DARK))

        observer.await(2, TimeUnit.SECONDS)
        observer.assertNoErrors()
        observer.assertValueCount(3)

        val initialState = observer.values().first()
        val finalState = observer.values().last()
        Assert.assertFalse(initialState.settingsChanged)
        Assert.assertTrue(finalState.settingsChanged)
    }
}