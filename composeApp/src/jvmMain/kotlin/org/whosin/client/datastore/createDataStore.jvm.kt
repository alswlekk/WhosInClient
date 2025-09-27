package org.whosin.client.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.whosin.client.core.datastore.DATA_STORE_FILE_NAME
import org.whosin.client.core.datastore.createDataStore

fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        DATA_STORE_FILE_NAME
    }
}