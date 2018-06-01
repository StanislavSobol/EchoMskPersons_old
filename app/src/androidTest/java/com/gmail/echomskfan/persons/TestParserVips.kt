package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import com.gmail.echomskfan.persons.data.VipDTO
import com.gmail.echomskfan.persons.interactor.parser.Parser
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestParserVips : AndroidTestCase() {

    private lateinit var parser: Parser
    private lateinit var appContext: Context

    @Before
    fun before() {
        parser = Parser()
        appContext = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.gmail.echomskfan.persons", appContext.packageName)
    }

    @Test
    fun testParserLoadJSONFromAsset() {
        val vipsJsonString = parser.loadJSONFromAsset(appContext, "vips.json")
        assertFalse(vipsJsonString.isEmpty())
    }

    @Test
    fun testParserGetVips() {
        val vips = parser.getVips(appContext)
        assertEquals(2, vips.size)
        for (vip in vips) {
            Assert.assertTrue(vip.isValid())
        }
    }

    @Test
    fun testParserGetCastsForVips() {
        val vips = parser.getVips(appContext)
        for (vip in vips) {
            Assert.assertTrue(vip.isValid())

            val url = vip.getFullUrl()

            val casts = parser.getCasts(vip.getFullUrl(), vip)
            Assert.assertTrue(casts.isNotEmpty())
            casts.forEach{ Assert.assertTrue(it.isValid())}
        }
    }
}

private fun VipDTO.isValid() = url.isNotEmpty() &&
                firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                profession.isNotEmpty() &&
                info.isNotEmpty() &&
                photoUrl.isNotEmpty()

