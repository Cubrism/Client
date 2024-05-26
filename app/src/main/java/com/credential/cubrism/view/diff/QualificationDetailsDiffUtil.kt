package com.credential.cubrism.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.credential.cubrism.view.adapter.QualificationDetailsItem

class QualificationDetailsDiffUtil(private val oldList: List<QualificationDetailsItem>, private val newList: List<QualificationDetailsItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].data == newList[newItemPosition].data
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}