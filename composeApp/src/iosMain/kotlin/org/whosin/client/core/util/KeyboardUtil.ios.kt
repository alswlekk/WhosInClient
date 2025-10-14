package org.whosin.client.core.util

import platform.UIKit.UIApplication
import platform.UIKit.endEditing
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual fun hideKeyboard() {
    dispatch_async(dispatch_get_main_queue()) {
        UIApplication.sharedApplication.keyWindow?.endEditing(true)
    }
}