package com.shwifty.tex

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.settings.mvi.SettingsIntents
import com.shwifty.tex.views.settings.mvi.SettingsInteractor
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
import org.mockito.MockitoAnnotations


class SettingsInteractorTest {

    @Mock
    lateinit var mockPreferencesRepository: IPreferenceRepository

    lateinit var interactor: SettingsInteractor

    val observer: TestObserver<SettingsViewState> = TestObserver()

    lateinit var emitter: Emitter<SettingsIntents>
    lateinit var mockIntents: Observable<SettingsIntents>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        mockIntents = Observable.create<SettingsIntents> {
            emitter = it
        }
        interactor = SettingsInteractor(mockPreferencesRepository)
    }

    @Test
    fun testRestartAppIntent() {
        val events = interactor.attachView(mockIntents)
        events.subscribe(observer)

        emitter.onNext(SettingsIntents.RestartApp())

        observer.assertNoErrors()
        observer.assertValueCount(2)

        val initialState = observer.values().first()
        val finalState = observer.values().last()
        Assert.assertFalse(initialState.restart)
        Assert.assertTrue(finalState.restart)
    }
}