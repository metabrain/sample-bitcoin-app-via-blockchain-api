package com.metabrain.bitcoin.blockchain.handlers

import com.metabrain.bitcoin.blockchain.services.BlockchainAPI
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
        val h = BitcoinAddressUnspentTxnsHandler(BlockchainAPI())
        val res = h.unspentTxnsForAddress("1P7doinGGP4eoSPR3i9xV9JbQciJoWkRFf")
//        val res = h.unspentTxnsForAddress("1Fhx8a9REf5YwgHVkD35aKcenKPtHB1wMv")
//        val res = h.unspentTxnsForAddress("1Aff4FgrtA1dZDwajmknWTwU2WtwUvfiXa")
        Assert.assertEquals(res.statusCodeValue, 200)

        val body = res.body
        val expectedBody = BitcoinAddressUnspentTxnsHandler.Outputs(
                outputs = listOf(BitcoinAddressUnspentTxnsHandler.Output(
                        value = 5000000000,
                        tx_hash = "38f51ecbd0b7df65700fea7e13374bfc3511aa4a2556a487820366fcdbc4c732", // is this little or big endian?!
                        output_idx = 0
                ))
        )
        Assert.assertEquals(expectedBody, body)
    }

    @Test
    fun invalidAddress() {
        val h = BitcoinAddressUnspentTxnsHandler(BlockchainAPI())
        val res = h.unspentTxnsForAddress("Robert'); DROP TABLE Students;--")
        Assert.assertEquals(res.statusCodeValue, 500)
    }
}