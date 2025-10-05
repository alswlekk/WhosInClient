package org.whosin.client.data.remote

import io.ktor.client.HttpClient

class RemoteMemberDataSource(
    private val client: HttpClient
) {
}