package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExcerciseBinding
    private var timer: CountDownTimer? = null
    private var restProgress = 0;
    private var exerciseProgress = 0;
    private lateinit var exerciseList:ArrayList<ExerciseModal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarExercise)


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding.toolbarExercise.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setRestProgressBar()
    }


    private fun setExerciseProgressBar() {
        binding.progressBar.progress = exerciseProgress
        timer = object : CountDownTimer(30 * 1000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++;
                binding.progressBar.progress = 10 - restProgress;
                binding.tvTimer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                exerciseProgress = 0;
                setRestProgressBar()
            }
        }.start()

    }

    private fun setRestProgressBar() {
        binding.progressBar.progress = restProgress;
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++;
                binding.progressBar.progress = 10 - restProgress;
                binding.tvTimer.text = (10 - restProgress).toString();
            }

            override fun onFinish() {
                restProgress = 0;
                setExerciseProgressBar()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        restProgress = 0;
        exerciseProgress = 0;
        timer = null
    }

}