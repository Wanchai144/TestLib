package com.tdg.vroom.utils.keyBoardState

import android.app.Activity
import android.view.ViewTreeObserver
import java.lang.ref.WeakReference

/**
 * Created by Shakiba E Nur on 18,May,2023
 */

fun interface UnregisterEvent {
    fun unregister()
}

class UnregisterCallback internal constructor(
    activity: Activity,
    globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
) : UnregisterEvent{

    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    private val onGlobalLayoutListenerWeakReference: WeakReference<ViewTreeObserver.OnGlobalLayoutListener> =
        WeakReference(globalLayoutListener)

    override fun unregister() {
        val activity = activityWeakReference.get()
        val globalLayoutListener = onGlobalLayoutListenerWeakReference.get()

        if (null != activity && null != globalLayoutListener) {
            val activityRoot = KeyboardStateEvent.getActivityRoot(activity)
            activityRoot.viewTreeObserver
                .removeOnGlobalLayoutListener(globalLayoutListener)
        }

        activityWeakReference.clear()
        onGlobalLayoutListenerWeakReference.clear()
    }
}