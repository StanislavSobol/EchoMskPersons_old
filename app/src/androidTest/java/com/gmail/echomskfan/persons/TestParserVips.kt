package com.gmail.echomskfan.persons

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import com.gmail.echomskfan.persons.interactor.parser.Parser
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

//    @Test
//    fun useAppContext() {
//        val appContext = InstrumentationRegistry.getTargetContext()
////        val str = appContext.resources.getString(R.string.abc_action_bar_home_description)
////        Assert.assertEquals("dsfsdfdsfdsf", str)
//        appContext.resources.assets.open("vips.json")
//        Assert.assertEquals("com.gmail.echomskfan.persons", appContext.packageName)
//    }

    @Test
    fun testParserLoadJSONFromAsset() {
        val vipsJsonString = parser.loadJSONFromAsset(appContext, "vips.json")
        assertFalse(vipsJsonString.isEmpty())
    }

    @Test
    fun testParserGetVips() {
        val vips = parser.getVips(appContext)
        assertEquals(2, vips.size)
    }


}