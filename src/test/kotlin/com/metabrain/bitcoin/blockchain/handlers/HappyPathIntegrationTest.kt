package com.metabrain.bitcoin.blockchain.handlers

import com.mashape.unirest.http.Unirest
import com.metabrain.bitcoin.blockchain.Main
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Technically supposed to be ran in an external system since it's technically not a unit test and uses the real system.
 * Instantiates the service itself and then queries it using standard CURL.
 *
 * Created by daniel.parreira on 30/04/2018.
 */
class HappyPathIntegrationTest {

    @Before
    fun startApp() {
        Main.main(arrayOf())
    }

    @After
    fun stopApp() {
        Main.stop()
    }

    @Test(timeout = 30_000L)
    fun unspentTxns() {
        // TODO: Is there any standard address that is always kept immutable over time?
        // I tried to pick one that is very old (zombie) and hasn't been used recently. But yes,
        //  a single transaction in or out might break this test.
        val response = Unirest.get("http://localhost:8080/address/1P7doinGGP4eoSPR3i9xV9JbQciJoWkRFf")
                .asString()
        Assert.assertEquals(200, response.status)

        val body = com.fasterxml.jackson.databind.ObjectMapper().readTree(response.body)
        val expectedBody = com.fasterxml.jackson.databind.ObjectMapper().readTree("""
                {
                    "outputs": [{
                        "value": 5000000000,
                        "tx_hash": "38f51ecbd0b7df65700fea7e13374bfc3511aa4a2556a487820366fcdbc4c732",
                        "output_idx": 0
                    }]
                }
        """)

        Assert.assertEquals("Unspent blocks didn't match!", expectedBody, body)
    }

}