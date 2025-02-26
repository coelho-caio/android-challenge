package com.zygotecnologia.zygotv.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.model.Episode
import com.zygotecnologia.zygotv.model.Season
import com.zygotecnologia.zygotv.utils.ImageUrlBuilder

class SeasonAdapterBase(
    context: Context,
    seasonList: List<Season>,
    hashSeasonEpisodes: HashMap<Season, List<Episode>>
) : BaseExpandableListAdapter() {

    private val adapterSeasonList: List<Season> = seasonList
    private val adapterHashSeasonAndEpisodes: HashMap<Season, List<Episode>> = hashSeasonEpisodes
    private val context: Context = context

    override fun getGroupCount(): Int =
        adapterSeasonList.size

    override fun getChildrenCount(groupPosition: Int): Int {
        adapterHashSeasonAndEpisodes[getGroup(groupPosition)]?.let {
            return it.size
        }
        return 0
    }

    override fun getGroup(groupPosition: Int): Any =
        adapterSeasonList[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        adapterHashSeasonAndEpisodes[getGroup(groupPosition)]?.let {
            return it[childPosition]
        }
        return "Unavailable"
    }

    override fun getGroupId(groupPosition: Int): Long =
        groupPosition.toLong()


    override fun getChildId(groupPosition: Int, childPosition: Int): Long =
        childPosition.toLong()

    override fun hasStableIds(): Boolean =
        false

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_seleleted_show, null)
        }

        val expandableImg: ImageView =
            convertView!!.findViewById(R.id.img_expandable_item_seleted_show)

        if (isExpanded) {
            expandableImg.setImageResource(R.drawable.ic_arrow_up)
        } else {
            expandableImg.setImageResource(R.drawable.ic_arrow_down)
        }

        val titleSeasonTexView: TextView =
            convertView.findViewById(R.id.tv_season_name_item_seletected_item)
        val sinopseSeasonTextView: TextView =
            convertView.findViewById(R.id.tv_sinopse_item_seletected_item)
        val imgSeason: ImageView = convertView.findViewById(R.id.img_item_selected_item)

        val season = (getGroup(groupPosition)) as Season
        titleSeasonTexView.text = season.name

        sinopseSeasonTextView.text = season.overview

        Glide.with(convertView)
            .load(season.posterPath?.let { ImageUrlBuilder.buildPosterUrl(it) })
            .apply(RequestOptions().placeholder(R.drawable.image_placeholder))
            .into(imgSeason)

        return convertView

    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        var convertView = convertView
            if (convertView == null) {
                val layoutInflater = context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater

                convertView = layoutInflater.inflate(R.layout.episode_item, null)
            }

            val titleEpisode: TextView = convertView!!.findViewById(R.id.tv_title_episode_item)
            val sinopseEpisode: TextView = convertView.findViewById(R.id.tv_sinopse_episode_item)

            val episode = getChild(groupPosition, childPosition) as Episode
            titleEpisode.text = episode.name
            sinopseEpisode.text = episode.overview

            return convertView
        }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = true
}