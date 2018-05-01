package com.metabrain.bitcoin.blockchain

import com.metabrain.bitcoin.blockchain.services.BitcoinAPI
import com.metabrain.bitcoin.blockchain.services.BlockchainAPI
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * Java config for Spring components.
 *
 * Created by daniel.parreira on 01/05/2018.
 */
@Configuration
@Component
@ImportAutoConfiguration
@ComponentScan("com.metabrain.bitcoin.blockchain")
@EntityScan("com.metabrain.bitcoin.blockchain")
open class Config {

    // NOTE: Beans could be elsewhere but for now where is only one so..
    @Bean
    open fun blockchainAPI(): BitcoinAPI {
        return BlockchainAPI()
    }

}