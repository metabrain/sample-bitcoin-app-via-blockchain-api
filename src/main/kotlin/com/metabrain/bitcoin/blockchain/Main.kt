package com.metabrain.bitcoin.blockchain

import com.metabrain.bitcoin.blockchain.handlers.BitcoinAddressUnspentTxnsHandler
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

open class Main {


    companion object {
        private var ctx: ConfigurableApplicationContext? = null

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            ctx = SpringApplication.run(arrayOf(
                    Config::class.java,
                    // TODO: Change to Route.kt again maybe..? Which is better?
                    // Personally, I prefer the routes are declared close to their implementations/handlers.
                    BitcoinAddressUnspentTxnsHandler::class.java
            ), args)
        }

        @JvmStatic
        fun stop() {
            ctx?.close()
        }
    }
}