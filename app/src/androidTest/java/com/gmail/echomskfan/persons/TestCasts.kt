package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.interactor.parser.Parser
import com.gmail.echomskfan.persons.ui.casts.CastsPresenter
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCasts {

    private lateinit var appContext: Context
    private lateinit var db: PersonsDatabase
    private lateinit var parser: Parser
    private lateinit var presenter: CastsPresenter
    private lateinit var vip: VipEntity

    @Before
    fun before() {
        appContext = InstrumentationRegistry.getTargetContext()
        db = PersonsDatabase.getTestInstance(appContext)
        parser = Parser()

        vip = parser.getVips(appContext).first()

        presenter = CastsPresenter()
        presenter.url = vip.url
        presenter.onFirstViewAttach()
        assertNotNull(presenter.interactor)

    }

    @Test
    fun testCreation() {
        assertNotNull(appContext)
        assertNotNull(db)
        // assertTrue(db.isOpen)
        assertNotNull(parser)
        assertNotNull(presenter)
    }

    @Test
    fun testLoadCastsFromWebToDbMappedToCastsVM() {
        val casts = mutableListOf<ItemDTO>()
        presenter.loadCastsFromWebToDbMappedToCastsVM(appContext, 1)
                .map {
                    casts.addAll(parser.getCasts(presenter.url, vip))
                    it
                }
                .subscribe(
                        {
                            assertEquals(casts.size, it.size)
                            for (i in 0 until it.size) {
                                assertEquals(CastVM.fromEntity(casts[i]), it[i])
                            }
                        },
                        {
                            assertTrue(false)
                        }
                )
    }


    @After
    fun after() {
        db.close()
    }
}