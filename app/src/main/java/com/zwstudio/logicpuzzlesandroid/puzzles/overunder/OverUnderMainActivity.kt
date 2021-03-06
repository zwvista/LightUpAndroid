package com.zwstudio.logicpuzzlesandroid.puzzles.overunder

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.home.android.realm
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_game_main)
class OverUnderMainActivity : GameMainActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState>() {
    @Bean
    protected lateinit var document: OverUnderDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        OverUnderOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        OverUnderGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class OverUnderOptionsActivity : GameOptionsActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState>() {
    @Bean
    protected lateinit var document: OverUnderDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        val lst: List<String> = GameOptionsActivity.lstMarkers
        val adapter = object : ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, GameOptionsActivity.lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.setText(s)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv = v.findViewById<CheckedTextView>(android.R.id.text1)
                ctv.setText(s)
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnMarker.setAdapter(adapter)
        spnMarker.setSelection(doc.markerOption)
    }

    @ItemSelect
    protected override fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        realm.beginTransaction()
        val rec = doc.gameProgress()
        doc.setMarkerOption(rec, position)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    protected fun onDefault() {
        realm.beginTransaction()
        val rec = doc.gameProgress()
        doc.setMarkerOption(rec, 0)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
        spnMarker.setSelection(doc.markerOption)
    }
}

@EActivity(R.layout.activity_game_help)
class OverUnderHelpActivity : GameHelpActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState>() {
    @Bean
    protected lateinit var document: OverUnderDocument
    override val doc get() = document
}