package io.github.lumue.mc.dlservice.sites.xh

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class XhHttpClientTest {

    @Test
    fun testLogin() {
        runBlocking {
            val client = XhHttpClient("dirtytom74", "ddl85s")
            val jobs: List<Job> = List(10) {
                launch {
                    client.login()
                    assert(client.loggedIn)
                }
            }
            jobs.forEach { it.join() }
        }
    }
}