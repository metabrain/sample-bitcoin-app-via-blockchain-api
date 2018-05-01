package com.metabrain.bitcoin.blockchain.services.model

/**
 * Created by daniel.parreira on 01/05/2018.
 */
// NOTE: Workaround for Jackson<->Kotlin deserialization that requires an empty-constructor without custom modules.
class BitcoinTxn(
        val tx_hash: String = "",
        val tx_hash_big_endian: String = "",
        val tx_index: Long = 0,
        val tx_output_n: Long = 0,
        val script: String = "",
        val value: Long = 0,
        val value_hex: String = "",
        val confirmations: Long = 0
)