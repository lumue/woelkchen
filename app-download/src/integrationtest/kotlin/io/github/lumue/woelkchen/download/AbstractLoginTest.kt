package io.github.lumue.woelkchen.download

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

abstract class AbstractLoginTest
{
    protected lateinit var client: BasicHttpClient


    @Test
    fun testLogin(){
//        runBlocking {
//            client.login()
//        }
//        assert(client.loggedIn)
    }

    @Test
    fun testConcurrentLogin() {
//        runBlocking {
//
//            val jobs: List<Job> = List(10) {
//                launch {
//                    client.login()
//                    assert(client.loggedIn)
//                }
//            }
//            jobs.forEach { it.join() }
//
//        }
    }



}