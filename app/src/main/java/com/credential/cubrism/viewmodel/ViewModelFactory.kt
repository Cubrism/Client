package com.credential.cubrism.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.credential.cubrism.model.repository.AuthRepository
import com.credential.cubrism.model.repository.JwtTokenRepository
import com.credential.cubrism.model.repository.PostRepository
import com.credential.cubrism.model.repository.QualificationRepository
import com.credential.cubrism.model.repository.StudyGroupRepository
import com.credential.cubrism.model.repository.UserRepository

class ViewModelFactory(private val repository: Any) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            JwtTokenViewModel::class.java -> JwtTokenViewModel(repository as JwtTokenRepository) as T
            AuthViewModel::class.java -> AuthViewModel(repository as AuthRepository) as T
            UserViewModel::class.java -> UserViewModel(repository as UserRepository) as T
            QualificationViewModel::class.java -> QualificationViewModel(repository as QualificationRepository) as T
            StudyGroupViewModel::class.java -> StudyGroupViewModel(repository as StudyGroupRepository) as T
            PostViewModel::class.java -> PostViewModel(repository as PostRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}