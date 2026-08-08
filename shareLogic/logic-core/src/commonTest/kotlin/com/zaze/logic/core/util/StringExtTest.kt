package com.zaze.logic.core.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringExtTest {

    @Test
    fun isNotNullOrBlank_handles_null_and_blank() {
        assertFalse(null.isNotNullOrBlank())
        assertFalse("   ".isNotNullOrBlank())
        assertTrue("hello".isNotNullOrBlank())
    }

    @Test
    fun formatBytes_formats_units_correctly() {
        assertEquals("0 B", formatBytes(0))
        assertEquals("512 B", formatBytes(512))
        assertEquals("1 KB", formatBytes(1024))
        assertEquals("1 MB", formatBytes(1024 * 1024))
        assertEquals("1.5 GB", formatBytes((1.5 * 1024 * 1024 * 1024).toLong()))
    }
}
