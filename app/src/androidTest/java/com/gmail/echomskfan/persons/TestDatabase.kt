package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.interactor.parser.Parser
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
    fun testVipInsertFromJsonToDbAndDelete() {
        val vips = parser.getVips(appContext)
        assertEquals(VIPS_AMOUNT, vips.size)
        vips.forEach {
            assertTrue(it.isValid())
            db.getVipDao().insert(it)
        }

        var dbVips = db.getVipDao().getAll()
        assertEquals(VIPS_AMOUNT, dbVips.size)

        for (i in 0 until VIPS_AMOUNT) {
            assertEquals(vips[i], dbVips[i])
        }

        db.getVipDao().deleteAll()
        dbVips = db.getVipDao().getAll()
        assertTrue(dbVips.isEmpty())
    }

    @After
    fun after() {
        db.close()
    }
}