package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.android

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data.BoxItAroundDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameState
import org.androidannotations.annotations.*
import java.sql.SQLException

@EActivity(R.layout.activity_game_main)
class BoxItAroundMainActivity : GameMainActivity<BoxItAroundGame?, BoxItAroundDocument?, BoxItAroundGameMove?, BoxItAroundGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItAroundDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        BoxItAroundOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BoxItAroundGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BoxItAroundOptionsActivity : GameOptionsActivity<BoxItAroundGame?, BoxItAroundDocument?, BoxItAroundGameMove?, BoxItAroundGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItAroundDocument? = null
    override fun doc() = document!!

    @AfterViews
    override fun init() {
        val lst = lstMarkers
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String?>(this,
                android.R.layout.simple_spinner_item, lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv = v.findViewById<View>(android.R.id.text1) as TextView
                tv.text = s
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                ctv.text = s
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnMarker.adapter = adapter
        spnMarker.setSelection(doc().markerOption)
    }

    @ItemSelect
    override fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        val rec = doc().gameProgress()
        doc().setMarkerOption(rec, position)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    protected fun onDefault() {
        val rec = doc().gameProgress()
        doc().setMarkerOption(rec, 0)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        spnMarker.setSelection(doc().markerOption)
    }
}

@EActivity(R.layout.activity_game_help)
class BoxItAroundHelpActivity : GameHelpActivity<BoxItAroundGame?, BoxItAroundDocument?, BoxItAroundGameMove?, BoxItAroundGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItAroundDocument? = null
    override fun doc() = document!!
}