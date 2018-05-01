package com.metabrain.bitcoin.blockchain.services

/**
 * Created by daniel.parreira on 01/05/2018.
 */
interface BitcoinAPI {
    fun getUnspentTxns(btcAddress: String): List<BitcoinTxn>
}

// NOTE: Workaround for Jackson<->Kotlin deserialization that requires an empty-constructor without custom modules.
class BitcoinTxn(
        val tx_hash: String = "", //":"2ea2555ad40286f127907f4d662dee03578005d91349e9b88a2765aee2808d81",
        val tx_hash_big_endian: String = "", //":"818d80e2ae65278ab8e94913d905805703ee2d664d7f9027f18602d45a55a22e",
        val tx_index: Long = 0, //":345316150,
        val tx_output_n: Long = 0, //": 1,
        val script: String = "", //":"76a914a1506b8bec154c14865ba7e4fffd0a14a94a3ff688ac",
        val value: Long = 0, //": 686288,
        val value_hex: String = "", //": "0a78d0",
        val confirmations: Long  = 0//":3
)