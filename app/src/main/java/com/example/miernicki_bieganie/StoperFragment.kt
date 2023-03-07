package com.example.miernicki_bieganie

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


@Suppress("NAME_SHADOWING", "DEPRECATION")
class StoperFragment : Fragment(), View.OnClickListener {
    lateinit var timeView : TextView
    private lateinit var sharedPref : SharedPreferences
    private var seconds: Int = 0
    private var running = false
    private var wasRunning = false
    private var hours: Int = 0
    private var secs: Int = 0
    private var minutes: Int = 0
    private lateinit var best_result: TextView
    private lateinit var last_result: TextView
    private var item: Route.Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = Route.route_map[it.getString(ARG_ITEM_ID)]
                Log.d("Tag", item?.content.toString())            }
        }
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val layout: View = inflater.inflate(R.layout.fragment_stopper, container, false)
        runStoper(layout)
        val startButton: Button = layout.findViewById<View>(R.id.start_button) as Button
        startButton.setOnClickListener(this)
        val stopButton: Button = layout.findViewById<View>(R.id.stop_button) as Button
        stopButton.setOnClickListener(this)
        val resetButton: Button = layout.findViewById<View>(R.id.reset_button) as Button
        resetButton.setOnClickListener(this)
        val saveButton: Button = layout.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener(this)

        sharedPref = activity?.getSharedPreferences("com.example.lab8_beta.shared",0)!!
        val besthours = sharedPref.getInt(item?.content.toString()+"besthours",0)
        val bestsecs = sharedPref.getInt(item?.content.toString()+"bestsecs",0)
        val bestminutes = sharedPref.getInt("bestminutes",0)

        val lasthours = sharedPref.getInt(item?.content.toString()+"lasthours",0)
        val lastsecs = sharedPref.getInt(item?.content.toString()+"lastsecs",0)
        val lastminutes = sharedPref.getInt(item?.content.toString()+"lastminutes",0)

        best_result = layout.findViewById<View>(R.id.best_result) as TextView
        last_result = layout.findViewById<View>(R.id.last_result) as TextView
        val time2 = String.format("%d:%02d:%02d", besthours, bestminutes, bestsecs)
        val time3 = String.format("%d:%02d:%02d", lasthours, lastminutes,lastsecs)
        best_result.text = "Najlepszy wynik: $time2"

        last_result.text = "Ostatni wynik:$time3"
        return layout
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("seconds", seconds)
        savedInstanceState.putBoolean("running", running)
        savedInstanceState.putBoolean("wasRunning", wasRunning)
    }

    private fun onClickStart() {
        running = true
    }

    private fun onClickStop() {
        running = false
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
                timeView, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun onClickReset() {
        running = false
        seconds = 0
        val animator = ObjectAnimator.ofFloat(timeView, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
    @SuppressLint("SetTextI18n")
    fun onClickSave() {

        val sharedPref = activity?.getSharedPreferences("com.example.lab8_beta.shared",0)

        var best_hours = sharedPref?.getInt(item?.content.toString()+"besthours",0)
        var best_minutes = sharedPref?.getInt(item?.content.toString()+"bestminutes",0)
        var best_secs = sharedPref?.getInt(item?.content.toString()+"bestsecs",0)

        last_result.text =  "Ostatni wynik:" + activity?.findViewById<TextView>(R.id.time_view)?.text
        val last = sharedPref?.edit()
        last?.putInt(item?.content.toString()+"lasthours",hours)
        last?.putInt(item?.content.toString()+"lastminutes",minutes)
        last?.putInt(item?.content.toString()+"lastsecs",secs)
        last?.apply()

        val animator2 = ObjectAnimator.ofFloat(last_result, View.ALPHA, 0f)
        animator2.repeatCount = 1
        animator2.repeatMode = ObjectAnimator.REVERSE
        animator2.start()

        if(
            ((best_hours==0) &&(best_minutes==0) && (best_secs==0))
        )
        {
            best_hours=hours
            best_minutes=minutes
            best_secs=secs
            val best = sharedPref?.edit()
            best?.putInt(item?.content.toString() + "besthours", hours)
            best?.putInt(item?.content.toString() + "bestminutes", minutes)
            best?.putInt(item?.content.toString() + "bestsecs", secs)
            best?.apply()
            val time3 = String.format("%d:%02d:%02d", hours, minutes, secs)
            best_result.text = "Najlepszy wynik: $time3"

            val animator = ObjectAnimator.ofFloat(timeView, View.ROTATION,  0f,1080f)
            animator.duration = 1000
            animator.start()

            val animator2 = ObjectAnimator.ofFloat(best_result, View.ALPHA, 0f)
            animator2.repeatCount = 1
            animator2.repeatMode = ObjectAnimator.REVERSE
            animator2.start()
        }
        if(
            (hours<best_hours!!)
            || ((hours==best_hours) &&(minutes< best_minutes!!))
            || ((hours==best_hours) &&(minutes== best_minutes!!) && (secs< best_secs!!))
        ) {
            val best = sharedPref?.edit()
            best?.putInt(item?.content.toString() + "besthours", hours)
            best?.putInt(item?.content.toString() + "bestminutes", minutes)
            best?.putInt(item?.content.toString() + "bestsecs", secs)
            best?.apply()
            val time3 = String.format("%d:%02d:%02d", hours, minutes, secs)
            best_result.text = "Najlepszy wynik: $time3"

            val animator = ObjectAnimator.ofFloat(timeView, View.ROTATION,  0f,360f)
            animator.duration = 1000
            animator.start()

            val animator2 = ObjectAnimator.ofFloat(best_result, View.ALPHA, 0f)
            animator2.repeatCount = 1
            animator2.repeatMode = ObjectAnimator.REVERSE
            animator2.start()
        }
    }

    private fun runStoper(view: View) {
        timeView = view.findViewById<View>(R.id.time_view) as TextView
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                hours = seconds / 3600
                minutes = seconds % 3600 / 60
                secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.start_button -> onClickStart()
            R.id.stop_button -> onClickStop()
            R.id.reset_button -> onClickReset()
            R.id.save_button -> onClickSave()
        }
    }
}
