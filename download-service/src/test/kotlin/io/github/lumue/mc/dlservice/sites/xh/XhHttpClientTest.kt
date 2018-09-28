package io.github.lumue.mc.dlservice.sites.xh

import org.junit.Test

class XhHttpClientTest {

    @Test
    fun testLogin(){
        val client = XhHttpClient("", "")
        client.login()
        assert(client.loggedIn)
    }

}