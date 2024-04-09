package com.credential.cubrism.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.credential.cubrism.R


interface QnaBannerEnterListener {
    fun onBannerClicked()
    fun onBannerStudyClicked()
}
data class BannerData(val bannerImg: Int? = null, val bannerTxt: String? = null)

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private var listener: QnaBannerEnterListener? = null
    fun setBannerListener(listener: QnaBannerEnterListener) {
        this.listener = listener
    }

    inner class BannerViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bannerImg = v.findViewById<ImageView>(R.id.icon)
        val bannerTxt = v.findViewById<TextView>(R.id.txtBanner)
        val bannerbtn = v.findViewById<Button>(R.id.btnGo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_list_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun getItemCount(): Int { // 무한 스크롤 되도록 최댓값으로 설정
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val context = holder.itemView.context
        // position이 짝수면 첫 번째 아이템, 홀수면 두 번째 아이템
        if (position % 2 == 0) { // qna
            holder.bannerImg.setImageResource(R.drawable.qnaicon)
            holder.bannerTxt.text = "궁금한 것이 있을 땐?\nQ&A 게시판에 질문하세요!"
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.bannerYellow))
            holder.bannerbtn.setBackgroundResource(R.drawable.button_rounded_corner_red)
            holder.bannerbtn.setOnClickListener {
                listener?.onBannerClicked()
            }
        } else { // studygroup
            holder.bannerImg.setImageResource(R.drawable.studybannericon)
            holder.bannerTxt.text = "같이 공부해요!\n스터디그룹을 결성해보세요!"
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.bannerPurple))
            holder.bannerbtn.setBackgroundResource(R.drawable.button_rounded_corner_bannerpurple)
            holder.bannerbtn.setOnClickListener {
                listener?.onBannerStudyClicked()
            }
        }
        holder.itemView.invalidate()
    }
}