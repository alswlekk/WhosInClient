package org.whosin.client.data.repository

import org.whosin.client.data.remote.RemoteMemberDataSource

class MemberRepository(
    private val dataSource: RemoteMemberDataSource
) {
}