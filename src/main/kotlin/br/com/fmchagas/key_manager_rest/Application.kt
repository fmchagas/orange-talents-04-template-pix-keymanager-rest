package br.com.fmchagas.key_manager_rest

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.fmchagas.key_manager_rest")
		.start()
}

