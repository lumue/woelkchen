package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.AbstractLoginTest
import org.junit.Before

class XhLoginTest : AbstractLoginTest() {



    @Before
    fun initClient() {
        client = XhHttpClient("dirtytom74", "ddl85s")
    }


}