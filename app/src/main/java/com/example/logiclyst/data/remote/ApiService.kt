package com.example.logiclyst.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface LogiclystApi {
    @POST("/analyze")
    suspend fun analyzeText(@Body request: AnalysisRequest): AnalysisResponse
}

data class AnalysisRequest(
    val text: String,
    val sensitivity: Float
)

data class AnalysisResponse(
    @SerializedName("label")
    val label: String?,

    @SerializedName("explanation")
    val explanation: String?,

    @SerializedName("counter_arguments")
    val counterArguments: List<String>?,

    @SerializedName("is_fallacy")
    val isFallacy: Boolean?,

    @SerializedName("status")
    val status: String?
)