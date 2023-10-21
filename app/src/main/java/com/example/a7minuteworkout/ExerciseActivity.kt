package com.example.a7minuteworkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityExcerciseBinding

    private lateinit var ttl: TextToSpeech

    private var timer: CountDownTimer? = null
    private var restProgress = 0;
    private var exerciseProgress = 0;

    private lateinit var exerciseList: ArrayList<ExerciseModal>
    private var currentExercisePosition = 0;

    private var exerciseAdapter: ExerciseStatusAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarExercise)

        //setting text to speech
        ttl = TextToSpeech(this, this)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding.toolbarExercise.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setRestProgressBar()
        setupExerciseStatusRecyclerView()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun setExerciseProgressBar() {
        binding.progressBar.progress = exerciseProgress
        val exercise = exerciseList[currentExercisePosition]
        binding.tvTitle.text = "Exercise: ${exercise.getName()}"
        binding.exerciseImage.setImageResource(exercise.getImage())
        binding.exerciseImage.visibility = View.VISIBLE
        ttl.speak("${exercise.getName()}, for 30 seconds", TextToSpeech.QUEUE_FLUSH, null, "")
        timer = object : CountDownTimer(30 * 1000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++;
                binding.progressBar.progress = 10 - restProgress;
                binding.tvTimer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition == exerciseList.size - 1)
                    return;
                exerciseProgress = 0;
                currentExercisePosition++;
                setRestProgressBar()
                exercise.setIsCompleted(true)
                exerciseAdapter?.notifyDataSetChanged()
            }
        }.start()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRestProgressBar() {
        binding.progressBar.progress = restProgress;
        val exercise = exerciseList[currentExercisePosition]
        exercise.setIsSelected(true)
        exerciseAdapter?.notifyDataSetChanged()
        binding.tvTitle.text = "Get Ready For: ${exercise.getName()}"
        binding.exerciseImage.visibility = View.INVISIBLE
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
        ttl.stop()
        ttl.shutdown()
    }

    override fun onInit(p0: Int) {
    }


}