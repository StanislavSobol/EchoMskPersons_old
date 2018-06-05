package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.echomskfan.persons.interactor.parser.Parser
import junit.framework.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestParserVips {

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
        assertEquals(VIPS_AMOUNT, vips.size)
        vips.forEach { assertTrue(it.isValid()) }
    }

    @Test
    fun testParserGetCastsForVips() {
        val vips = parser.getVips(appContext)
        for (vip in vips) {
            Assert.assertTrue(vip.isValid())
            val casts = parser.getCasts(vip.getCastFullUrl(), vip)
            Assert.assertTrue(casts.isNotEmpty())
            casts.forEach { Assert.assertTrue(it.isValid()) }
        }
    }

    @Test
    fun testParserGetAll() {
        val vips = parser.getVips(appContext)
        for (vip in vips) {
            Assert.assertTrue(vip.isValid())
            val casts = parser.getCasts(vip.getCastFullUrl(), vip)
            Assert.assertTrue(casts.isNotEmpty())

            for (cast in casts) {
                Assert.assertTrue(cast.isValid())

                val textData = parser.getTextData(cast.fullTextURL)
                assertNotNull(textData)
                textData?.let {
                    assertTrue(textData.personDTOs.isNotEmpty())
                    assertTrue(textData.blockDTOs.isNotEmpty())

                    textData.personDTOs.forEach { assertTrue(it.isValid()) }
                    textData.blockDTOs.forEach { assertTrue(it.isValid()) }
                }
            }
        }
    }

    @Test
    fun testLatynina180127() {
        val textData = parser.getTextData("https://echo.msk.ru/programs/code/2136596-echo/")
        Assert.assertNotNull(textData)
        textData?.run {
            assertTrue(personDTOs.isNotEmpty())
            assertTrue(blockDTOs.isNotEmpty())  // Латынина 27 января пустая
        }
    }

    @Test
    fun testLatynina180120() {
        val textData = parser.getTextData("https://echo.msk.ru/programs/code/2132238-echo/")
        Assert.assertNotNull(textData)
        textData?.run {
            assertTrue(personDTOs.isNotEmpty())
            assertTrue(blockDTOs.isNotEmpty())  // Латынина 27 января пустая
        }
    }
}
