package com.metabrain.bitcoin.blockchain.handlers

import com.metabrain.bitcoin.blockchain.services.BitcoinAPI
import com.metabrain.bitcoin.blockchain.services.BlockchainAPI
import com.metabrain.bitcoin.blockchain.services.model.BitcoinTxn
import org.junit.Assert
import org.junit.Test

/**
 * Testing the handler using mocked service.
 *
 * Created by daniel.parreira on 30/04/2018.
 */
class BitCoinAddressUnspentTxnsTest {

    companion object {
//        val mockedBitcoinAPI = BitcoinAPI() // Useful when I was developing
        val mockedBitcoinAPI = object: BitcoinAPI{
            override fun getUnspentTxns(btcAddress: String): List<BitcoinTxn> {
                return if(btcAddress=="1P7doinGGP4eoSPR3i9xV9JbQciJoWkRFf") {
                    listOf(BitcoinTxn(
                            tx_hash = "32c7c4dbfc66038287a456254aaa1135fc4b37137eea0f7065dfb7d0cb1ef538",
                            tx_hash_big_endian = "38f51ecbd0b7df65700fea7e13374bfc3511aa4a2556a487820366fcdbc4c732",
                            tx_index = 35808,
                            tx_output_n = 0,
                            script = "41048b593e23b4148916f841d2833dfc48e4423bb22e980ea2d0a03080d60a0e8eb79005a1b5df7a727f020d05b34793d7cd1aa7104cd795cb860a327811f186a879ac",
                            value = 5000000000,
                            value_hex = "012a05f200",
                            confirmations = 500951
                    ))
                } else {
                    throw NotImplementedError()
                }
            }
        }
    }

    @Test
    fun simpleExampleValidAddr() {
        val h = BitcoinAddressUnspentTxnsHandler(mockedBitcoinAPI)
        val res = h.unspentTxnsForAddress("1P7doinGGP4eoSPR3i9xV9JbQciJoWkRFf")
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

    @Test(expected = Exception::class)
    fun invalidAddress() {
        val h = BitcoinAddressUnspentTxnsHandler(BlockchainAPI())
        h.unspentTxnsForAddress("0000000000000000000000000000000000")
        Assert.fail("Should never reach this point!")
    }

    @Test(expected = Exception::class)
    fun illegalSqlInjectionDeniedTest() {
        val h = BitcoinAddressUnspentTxnsHandler(BlockchainAPI())
        h.unspentTxnsForAddress("Robert'); DROP TABLE Students;--") // https://xkcd.com/327/
        Assert.fail("Should never reach this point!")
    }
}