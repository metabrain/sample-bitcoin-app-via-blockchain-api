package com.metabrain.bitcoin.blockchain.handlers

import com.metabrain.bitcoin.blockchain.handlers.BitcoinAddressUnspentTxnsHandler
import org.junit.Assert
import org.junit.Test

/**
 * A bit cheating since it's using the internet.. Response from HTTP server could be mocked.
 *
 * Created by daniel.parreira on 30/04/2018.
 */
class BitCoinAddressUnspentTxnsTest {

    @Test
    fun simpleExampleValidAddr() {
        val res = BitcoinAddressUnspentTxnsHandler.unspentTxnsForAddress("1Aff4FgrtA1dZDwajmknWTwU2WtwUvfiXa")
        Assert.assertEquals(res.statusCode, 200)

        val body = res.body
        val expectedBody = listOf(
                BitcoinAddressUnspentTxnsHandler.Companion.Output(
                        value = 63871,
                        tx_hash = "db9b6ff6ba4fd5813fe1ae8980ee30645221e333c0f647bb1fc777d0f58d3e23",
                        output_idx = 1
                )
        )
        Assert.assertEquals(expectedBody, body)
    }

    @Test
    fun invalidAddress() {
        val res = BitcoinAddressUnspentTxnsHandler.unspentTxnsForAddress("Robert'); DROP TABLE Students;--")
        Assert.assertEquals(res.statusCode, 500)
    }
}