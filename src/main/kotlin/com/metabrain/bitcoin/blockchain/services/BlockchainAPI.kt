package com.metabrain.bitcoin.blockchain.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import org.springframework.stereotype.Component
import java.io.IOException

/**
 *
 *
 * Created by daniel.parreira on 01/05/2018.
 */
@Component
class BlockchainAPI: BitcoinAPI {

    override fun getUnspentTxns(btcAddress: String): List<BitcoinTxn> {
        val res = Unirest.get("https://blockchain.info/unspent")
                .queryString("active", btcAddress)
                .asString()

//        TODO BitcoinAddressUnspentTxnsHandler.LOG.info("[$btcAddress] => {${unspent.status} ${unspent.statusText} - ${unspent.body}}")

        // NOTE: Okay, weird. Apparently when there is no free outputs for an address the server returns 500..? Shouldn't
        //  it return an empty list, since there are no outputs? This makes it very awkward to parse this endpoint.
        if(res.status==500 && res.body=="No free outputs to spend") {
            return listOf()
        }
        if(res.status!=200) {
            throw RuntimeException("Error processing request: ${res.status} ${res.statusText} - ${res.rawBody}")
        }

        val json = res.let {
            val mapper = ObjectMapper()
            try {
                mapper.readTree(it.body)
            } catch (e: IOException) {
                throw RuntimeException("Could not parse body response for blockchain.info: ${it.body}")
            }
        }

        if(!json.has("unspent_outputs") || !json.get("unspent_outputs").isArray) {
            throw RuntimeException("Error parsing response for blockchain.info: missing or malformated 'unspent_outputs' - $json")
        }
        val rawUnspentOutputs = json.get("unspent_outputs").elements()

        return rawUnspentOutputs.asSequence().toList().map {
            ObjectMapper().treeToValue(it, BitcoinTxn::class.java)
        }
    }


}