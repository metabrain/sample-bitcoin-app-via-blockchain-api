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

    // TODO
//    @Autowired
//    lateinit var BlockchainAPI

    companion object {
        val LOG = LoggerFactory.getLogger(BitcoinAddressUnspentTxnsHandler::class.java)
    }

    @RequestMapping(
            path = arrayOf("/address/{bitcoin_addr}"),
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf("application/json")
    )
    @ResponseBody
    fun handler(@PathVariable("bitcoin_addr") bitcoinAddress: String): ResponseEntity<*> { // TODO surelly better way instead of <*>
        try {
            return unspentTxnsForAddress(bitcoinAddress)
            // We need to wrap it in an "outputs" object
        } catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    // TODO Put all this functions in a @Autowired BlockChainAPI class
    fun unspentTxnsForAddress(btcAddress: String): ResponseEntity<Outputs> {

        val unspent = api.getUnspentTxns(btcAddress)

        // TODO decode tx_hash from HEX (why is the default HEX? Is there a use for it? Or is this endpoint strictly for interviewing purposes..?)
        // From https://blockchain.info/api/blockchain_api:
        //  "The tx hash is in reverse byte order. What this means is that in order to get the html transaction
        //      hash from the JSON tx hash for the following transaction, you need to decode the hex (using this
        //      site for example). This will produce a binary output, which you need to reverse (the last 8bits/1byte
        //      move to the front, second to last 8bits/1byte needs to be moved to second, etc.). Then once the
        //      reversed bytes are decoded, you will get the html transaction hash."

        // NOTE: Type-gymnastic not very efficient, would be faster to use normal Java for(E: e) syntax
        val outputs: List<Output> = unspent.asSequence().toList().map {
            Output(
                    output_idx = it.tx_output_n, //TODO is this the right one..?
                    tx_hash = it.tx_hash_big_endian,
                    //TODO: Why? Blockchain.info UI displays the big endian result everywhere so I suppose this is the ubiquitous way of displaying the BTC address hashes..?
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