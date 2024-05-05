package com.credential.cubrism.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.credential.cubrism.model.repository.ChatRepository
import com.credential.cubrism.model.dto.ChatResponseDto
import com.credential.cubrism.model.utils.ResultUtil
import com.credential.cubrism.viewmodel.utils.Event

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    private val _chattingList = MutableLiveData<ResultUtil<List<ChatResponseDto>>>()
    val chattingList: LiveData<ResultUtil<List<ChatResponseDto>>> = _chattingList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getChattingList(studygroupId: Long) {
        chatRepository.getChattingList(studygroupId) { result ->
            when (result) {
                is ResultUtil.Success -> { _chattingList.postValue(result) }
                is ResultUtil.Error -> { _errorMessage.postValue(Event(result.error)) }
                is ResultUtil.NetworkError -> { _errorMessage.postValue(Event("네트워크 오류가 발생했습니다.")) }
            }
        }
    }
}