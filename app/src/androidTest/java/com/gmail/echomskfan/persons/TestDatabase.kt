package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipFavEntity
import com.gmail.echomskfan.persons.interactor.Interactor
import com.gmail.echomskfan.persons.interactor.parser.Parser
import com.gmail.echomskfan.persons.ui.vips.VipsPresenter
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDatabase {

    private lateinit var appContext: Context
    private lateinit var db: PersonsDatabase
    private lateinit var parser: Parser

    @Before
    fun before() {
        appContext = InstrumentationRegistry.getTargetContext()
        db = PersonsDatabase.getTestInstance(appContext)
        parser = Parser()
    }

    @Test
    fun testCreation() {
        assertNotNull(appContext)
        assertNotNull(db)
        assertTrue(db.isOpen)
        assertNotNull(parser)
    }

    @Test
    fun testVipInsertFromJsonToDbAndDelete_ProperApproach() {
        val vips = parser.getVips(appContext)
        assertEquals(VIPS_AMOUNT, vips.size)
        vips.forEach {
            assertTrue(it.isValid())
            db.getVipFavDao().insert(VipFavEntity(it.url))
        }

        val dbVipFavs = db.getVipFavDao().getAll()
        assertEquals(VIPS_AMOUNT, dbVipFavs.size)

        for (i in 0 until VIPS_AMOUNT) {
            assertEquals(vips[i].url, dbVipFavs[i].url)
        }
    }

    @Test
    fun testPresenterAndInteractorGetVips() {
        val presenter = VipsPresenter()

        presenter.onFirstViewAttach()
        assertNotNull(presenter.interactor)

        val vips = parser.getVips(appContext)

        presenter.loadVipsFromJsonToDbMappedToVipsVM()
                .subscribe({
                    assertEquals(vips.size, it.size)

                    for (i in 0 until it.size) {
                        assertEquals(VipVM.fromEntity(vips[i]), it[i])
                    }
                }, {
                    assertTrue(false)
                })
    }

    @Test
    fun initialInteractorCopyVipsFromJsonToDb_checkDb() {
        val interactor = Interactor()

        interactor.copyVipsFromJsonToDb(appContext)
                .subscribe({
                    val vips = db.getVipFavDao().getAll()

                    assertEquals(vips.size, it.size)
                    for (i in 0 until vips.size) {
                        assertEquals(vips[i].url, it[i].url)
                    }
                }, {
                    assertTrue(false)
                })
    }

    @After
    fun after() {
        db.close()
    }
}