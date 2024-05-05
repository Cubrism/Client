package com.credential.cubrism.model.repository

import com.credential.cubrism.model.api.StudyGroupApi
import com.credential.cubrism.model.dto.ChatResponseDto
import com.credential.cubrism.model.service.RetrofitClient
import com.credential.cubrism.model.utils.ResultUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {
    private val studyGroupApi: StudyGroupApi = RetrofitClient.getRetrofitWithAuth()?.create(StudyGroupApi::class.java)!!


    fun getChattingList(studygroupId: Long, callback: (ResultUtil<List<ChatResponseDto>>) -> Unit) {
        studyGroupApi.getChattingList(studygroupId).enqueue(object : Callback<List<ChatResponseDto>> {
            override fun onResponse(call: Call<List<ChatResponseDto>>, response: Response<List<ChatResponseDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let { callback(ResultUtil.Success(it)) }
                } else {
                    response.errorBody()?.string()?.let { callback(ResultUtil.Error(JSONObject(it).optString("message"))) }
                }
            }

            override fun onFailure(call: Call<List<ChatResponseDto>>, t: Throwable) {
                callback(ResultUtil.NetworkError())
            }
        })
    }
}