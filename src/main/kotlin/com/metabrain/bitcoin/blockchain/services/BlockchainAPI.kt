package com.metabrain.bitcoin.blockchain.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import com.metabrain.bitcoin.blockchain.handlers.BitcoinAddressUnspentTxnsHandler
import com.metabrain.bitcoin.blockchain.services.model.BitcoinTxn
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

/**
 * Implementation that queries the blockchain.info open and free web API.
 *
 * Created by daniel.parreira on 01/05/2018.
 */
@Component
class BlockchainAPI: BitcoinAPI {

    companion object {
        val LOG = LoggerFactory.getLogger(BlockchainAPI::class.java)
    }

    override fun getUnspentTxns(btcAddress: String): List<BitcoinTxn> {
        val res = Unirest.get("https://blockchain.info/unspent")
                .queryString("active", btcAddress)
                .asString()

        LOG.info("[$btcAddress] => {${res.status} ${res.statusText}}")

        // NOTE: Apparently when there is no free outputs for an address the server returns 500..? Shouldn't
        //  it return an empty list, since there are no outputs? This makes it very awkward to parse this endpoint.
        // Hopefully this will make it easier to consume from this API than from the endpoint directly.
        if(res.status==500 && res.body=="No free outputs to spend") {
            return listOf()
        }
        if(res.status!=200) {
            throw RuntimeException("Error processing request: ${res.status} ${res.statusText} - ${res.body}")
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

        LOG.trace("[$btcAddress] => {${res.status} ${res.statusText}")

        // NOTE: Type-gymnastic not very efficient, would be faster to use normal Java for(E: e) syntax, but this looks nicer
        val unspent = rawUnspentOutputs.asSequence().toList().map {
            // From https://blockchain.info/api/blockchain_api:
            //  "The tx hash is in reverse byte order. What this means is that in order to get the html transaction
            //      hash from the JSON tx hash for the following transaction, you need to decode the hex (using this
            //      site for example). This will produce a binary output, which you need to reverse (the last 8bits/1byte
            //      move to the front, second to last 8bits/1byte needs to be moved to second, etc.). Then once the
            //      reversed bytes are decoded, you will get the html transaction hash."
            // However, this is apparently this is already the decoded form mentioned above.
            // Maybe documentation wasn't updated since endpoint has this field added? I believe that this is
            //  the case since the output example they have next to the API description does not contain this field.
            ObjectMapper().treeToValue(it, BitcoinTxn::class.java)
        }
        LOG.debug("[$btcAddress] => { unspent txns: ${unspent.size} }")

        return unspent
    }


}