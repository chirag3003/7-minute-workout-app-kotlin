package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExcerciseBinding
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarExercise)


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarExercise.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding.progressBar.progress = restProgress;
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++;
                binding.progressBar.progress = 10 - restProgress;
                binding.tvTimer.text = (10 - restProgress).toString();
            }

            override fun onFinish() {
                Toast
                    .makeText(this@ExerciseActivity, "Starting the exercise", Toast.LENGTH_LONG)
                    .show()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        restProgress = 0;
    }

}