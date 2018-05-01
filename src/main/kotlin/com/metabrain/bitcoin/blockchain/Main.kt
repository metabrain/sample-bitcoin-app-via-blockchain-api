package com.metabrain.bitcoin.blockchain

import com.metabrain.bitcoin.blockchain.handlers.BitcoinAddressUnspentTxnsHandler
import com.metabrain.bitcoin.blockchain.services.BitcoinAPI
import com.metabrain.bitcoin.blockchain.services.BlockchainAPI
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
@ImportAutoConfiguration
@ComponentScan("com.metabrain.bitcoin.blockchain")
@EntityScan("com.metabrain.bitcoin.blockchain")
open class Main {

    // NOTE: Beans could be elsewhere but for now where is only one so..
    @Bean
    open fun blockchainAPI(): BitcoinAPI {
        return BlockchainAPI()
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            // TODO Change to Route.kt again please
            SpringApplication.run(arrayOf(
                    Main::class.java,
                    BitcoinAddressUnspentTxnsHandler::class.java
            ), args)
        }
    }
}