package com.gmail.echomskfan.persons

import android.util.Log
import com.gmail.echomskfan.persons.data.VipDTO
import com.gmail.echomskfan.persons.interactor.parser.IParser
import com.gmail.echomskfan.persons.interactor.parser.Parser
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
class CastsParserTest {

    private lateinit var parser: IParser
    private lateinit var nevzorovVipDTO: VipDTO

    @Before
    fun before() {
        parser = Parser()

        PowerMockito.mockStatic(Log::class.java)
        Mockito.`when`(Log.d(anyString(), anyString())).thenReturn(0)
        Mockito.`when`(Log.e(anyString(), anyString())).thenReturn(0)
        Mockito.`when`(Log.v(anyString(), anyString())).thenReturn(0)
        Mockito.`when`(Log.w(anyString(), anyString())).thenReturn(0)

        nevzorovVipDTO = VipDTO(
                "/guests/813104-echo",
                "Александр",
                "Невзоров",
                "публицист",
                "публицист всякого",
                ""
        )
    }

    @Test
    fun test_Parser_getCasts_empty() {
        assertTrue(parser.getCasts("", nevzorovVipDTO).isEmpty())
    }

    @Test
    fun test_Parser_getCasts_notUrl() {
        assertTrue(parser.getCasts("some bad url", nevzorovVipDTO).isEmpty())
    }

    @Test(expected = AssertionError::class)
    fun test_Parser_getCasts_wrongUrl() {
        assertNull(parser.getCasts("http://google.com", nevzorovVipDTO))
    }

    @Test
    fun test_Parser_getCasts_good() {
        var result = parser.getCasts("http://echo.msk.ru/guests/813104-echo/archive/1.html", nevzorovVipDTO)
        assertFalse(result.isEmpty())
        for (item in result) {
            assertTrue(item.isValid())
        }

        result = parser.getCasts("http://echo.msk.ru/guests/813104-echo/archive/2.html", nevzorovVipDTO)
        assertFalse(result.isEmpty())
        for (item in result) {
            assertTrue(item.isValid())
        }
    }
}
