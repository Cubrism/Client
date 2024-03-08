package com.credential.cubrism

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalMonthViewModel : ViewModel() {
    private val _calMonthList = MutableLiveData<ArrayList<CalMonth>>(arrayListOf())
    val calMonthList: LiveData<ArrayList<CalMonth>> get() = _calMonthList // 읽기만 가능(get)

    fun addDateMonth(value: CalMonth) { // 추가
        _calMonthList.value?.add(value)
        _calMonthList.value = _calMonthList.value // 옵서버 에게 변경 사항을 알림
    }

    fun deleteDateMonth(value: CalMonth) { // 삭제
        _calMonthList.value?.remove(value)
        _calMonthList.value = _calMonthList.value
    }

    fun modifyDateMonth(value: CalMonth, newValue: CalMonth) { // 수정
        val updatedList = _calMonthList.value?.toMutableList() ?: mutableListOf()

        // value가 리스트에 포함되어 있다면 해당 인덱스를 찾아 newValue로 대체
        val index = updatedList.indexOf(value)
        if (index != -1) {
            _calMonthList.value?.set(index, newValue)
            _calMonthList.value = _calMonthList.value
        }
    }
}

class QnaListViewModel : ViewModel() {
    private val _questionList = MutableLiveData<ArrayList<QnaData>>(arrayListOf())
    val questionList: LiveData<ArrayList<QnaData>> get() = _questionList // 읽기만 가능(get)

    fun addQuestion(value: QnaData) { // 질문 추가
        _questionList.value?.add(value)
        _questionList.value = _questionList.value // 옵서버 에게 변경 사항을 알림
    }

    fun deleteQuestion(value: QnaData) { // 질문 삭제
        _questionList.value?.remove(value)
        _questionList.value = _questionList.value
    }
}