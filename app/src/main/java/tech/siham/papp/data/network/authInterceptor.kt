package tech.siham.papp.papp.network

import okhttp3.Interceptor
import okhttp3.Response
import tech.siham.papp.utils.SessionManager


class authInterceptor : Interceptor {
    private val sessionManager = SessionManager()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Accept", "application/json")
            requestBuilder.addHeader("Content-Type","application/json")
            requestBuilder.addHeader("Authorization", "JWT $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}