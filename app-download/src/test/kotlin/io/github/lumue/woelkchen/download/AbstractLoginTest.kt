package io.github.lumue.woelkchen.download

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

abstract class AbstractLoginTest
{
    protected lateinit var client: BasicHttpClient


    @Test
    fun testLogin(){
        runBlocking {
            client.login()
        }
        assert(client.loggedIn)
    }

    @Test
    fun testConcurrentLogin() {
        runBlocking {

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