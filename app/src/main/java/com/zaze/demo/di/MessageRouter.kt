package com.zaze.demo.di

/**
 * Description :
 * @author : zaze
 * @version : 2023-11-15 21:01
 */
//class MessageRouter(private val applicationContext: Context) {
//
//    @EntryPoint
//    @InstallIn(SingletonComponent::class)
//    interface MessageServiceEntryPoint {
//        fun messageService(): MessageService
//    }
//
//    private val messageService: MessageService
//        get() {
//            return EntryPointAccessors.fromApplication(
//                applicationContext,
//                MessageServiceEntryPoint::class.java
//            ).messageService()
//        }
//
//    fun getMessageFragment(): Fragment {
//        return messageService.getMessageFragment()
//    }
//}