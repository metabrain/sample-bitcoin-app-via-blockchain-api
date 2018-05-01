package com.metabrain.bitcoin.blockchain.handlers

import com.metabrain.bitcoin.blockchain.services.BitcoinAPI
import com.metabrain.bitcoin.blockchain.services.BlockchainAPI
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

/**
 * Responds with a JSON-formatted list of all unspent transaction outputs for that address,
 * specifying the following for each such output:
 *  - value
 *  - transaction hash
 *  - output index
 *
 *  Example: `/address/1Aff4FgrtA1dZDwajmknWTwU2WtwUvfiXa`
 *
 *  {
 *     "outputs": [{
 *         "value": 63871,
 *         "tx_hash": "db9b6ff6ba4fd5813fe1ae8980ee30645221e333c0f647bb1fc777d0f58d3e23",
 *         "output_idx": 1
 *     }]
 *  }
 *
 * Created by daniel.parreira on 30/04/2018.
 */
@Component
@RestController
@EnableAutoConfiguration
class BitcoinAddressUnspentTxnsHandler(val api: BitcoinAPI) {

    companion object {
        val LOG = LoggerFactory.getLogger(BitcoinAddressUnspentTxnsHandler::class.java)
    }

    @RequestMapping(
            path = arrayOf("/address/{bitcoin_addr}"),
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf("application/json")
    )
    @ResponseBody
    fun handler(@PathVariable("bitcoin_addr") bitcoinAddress: String): ResponseEntity<*> {
        try {
            return unspentTxnsForAddress(bitcoinAddress)
        } catch (e: Exception) {
            LOG.warn("Error processing request: ${e.message}")
            e.printStackTrace()
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun unspentTxnsForAddress(btcAddress: String): ResponseEntity<Outputs> {
        val unspent = api.getUnspentTxns(btcAddress)

        val outputs: List<Output> = unspent.map {
            Output(
                    //TODO is this the right one..? or txn_index? There is no documentation explaining.
                    output_idx = it.tx_output_n,
                    tx_hash = it.tx_hash_big_endian,
                    value = it.value
            )
        }

        // Need to wrap list in an "outputs" JSON obj
        return ResponseEntity.ok(Outputs(outputs))
    }


    // Just classes to make easier for Jackson
    data class Output(val value: Long, val tx_hash: String, val output_idx: Long)
    data class Outputs(val outputs: List<Output>)

}