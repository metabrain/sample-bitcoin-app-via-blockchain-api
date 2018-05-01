package com.metabrain.bitcoin.blockchain.services

import com.metabrain.bitcoin.blockchain.services.model.BitcoinTxn

/**
 * Created by daniel.parreira on 01/05/2018.
 */
interface BitcoinAPI {

    /**
     * Enquires an external entity about the transactions that are currently unspent (transactions received
     *  that still have not been part of the input to an outgoing transaction) by the bitcoin address.
     *
     * @param btcAddress the BTC address
     * @return the list of unspent transactions this address currently has.
     */
    fun getUnspentTxns(btcAddress: String): List<BitcoinTxn>
}
