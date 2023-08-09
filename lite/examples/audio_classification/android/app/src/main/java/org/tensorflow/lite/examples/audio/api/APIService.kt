package org.tensorflow.lite.examples.audio.api

class APIService {
    companion object {
        private const val BASE_URL: String = "https://va-api.dev.ftech.ai/"

        fun base(): DataService {
            return APIRetrofitClient.getClient(BASE_URL).create(DataService::class.java)
        }
    }
}
