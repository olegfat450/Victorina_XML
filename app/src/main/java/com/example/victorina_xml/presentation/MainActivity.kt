package com.example.victorina_xml.presentation

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.victorina_xml.R
import com.example.victorina_xml.data.Repository
import com.example.victorina_xml.databinding.ActivityMainBinding
import com.example.victorina_xml.databinding.ButtonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var step = 0
    var questions: Questions? = null
    var correctAnswer = ""
     private var answersTvList: List<ButtonBinding> = listOf<ButtonBinding>()
     private var answers: MutableList<Any> = mutableListOf()
    var correctAnswerIndex = 0
    var correctAnswerId = ""
    val repository = Repository()
    var difficult = "medium"
    var category = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setImageResource(R.drawable.exit)

        val categories = listOf("All category","music","sport_and_leisure" ,"film_and_tv","arts_and_literature","history","society_and_culture","science","geography","food_and_drink","general_knowledge")
        val difficulties = listOf("easy" ,"medium" ,"hard")

        val categoryAdapter = ArrayAdapter(this,R.layout.spiner,categories)
       categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val difficultAdapter = ArrayAdapter(this,R.layout.spiner,difficulties)
        difficultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



        binding.apply {  categorySpinner.adapter = categoryAdapter; difficultSpinner.adapter = difficultAdapter; difficultSpinner.setSelection(1) }

        val categorySelected: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val s = parent?.getItemAtPosition(position) as String

                if ((s == "easy") or (s == "medium") or (s == "hard")) { difficult = s; getQuestions(category,s)} else {
                when(s) {
                    "All category" -> { category = "" }
                    else -> { category = s  }
                }
                    getQuestions(category,difficult)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

         binding.categorySpinner.onItemSelectedListener = categorySelected
         binding.difficultSpinner.onItemSelectedListener = categorySelected




       binding.apply { answersTvList = listOf(answerTv1,answerTv2,answerTv3,answerTv4) }
        answersTvList.forEach { it.item.background.setTintMode(PorterDuff.Mode.MULTIPLY) }
        getQuestions("",difficult)

        binding.buttonNext.setOnClickListener { showQuestion(++step) }

    }

    private fun showQuestion(step: Int) {
        if (step >= (questions?.size ?: 0)) { getQuestions(category,difficult); return }

        answersTvList.forEach { it.item.background.apply { setTint(getColor(R.color.white)) }}
        answers.clear()
        questions?.let {
                         correctAnswerIndex = Random.nextInt(0,4)
                         answers.addAll(it.get(step).incorrectAnswers)
                         answers.add(it.get(step).correctAnswer)
                         correctAnswer = answers.get(3).toString();

            answers[3] = answers[correctAnswerIndex].also { answers[correctAnswerIndex] = answers[3] }

            binding.apply { questionTv.text = it.get(step).question.text }

        }
            answersTvList.forEachIndexed { index, buttonBinding -> buttonBinding.item.text = answers[index].toString() }



    }

    fun getQuestions(category: String, difficult: String) {

        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        binding.progressBar.visibility = View.VISIBLE
         step = 0
        scope.launch {

            try {

                if (category.isBlank()) { questions = repository.getRandomQuestions(5,difficult) } else {questions = repository.getQuestionByCategory(5,category,difficult)}

                runOnUiThread { showQuestion(step); binding.progressBar.visibility = View.GONE }

            } catch (e:Exception) {

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE

                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("${e.message}")
                        .setCancelable(true)
                        .setPositiveButton("Повторить") { _,_ -> getQuestions("",difficult)}
                        .setNegativeButton("Выход") { _,_ -> finishAffinity()}
                        .create().show() }

            } }
    }




    fun onClick(view: View) {

       // CoroutineScope(Dispatchers.Main). launch { repository.getQuestionReport(correctAnswerId) }

        if (view is TextView) { if (view.text == correctAnswer) { view.background.apply { setTint(Color.GREEN) }} else {
            view.background.setTint(Color.RED); answersTvList[correctAnswerIndex].item.background.apply { setTint(Color.GREEN) }

        }}

    }

    fun onExit(view: View) {
        AlertDialog.Builder(this)
            .setTitle("Выход из программы")
            .setPositiveButton("ДА") { _,_ -> finishAffinity()}
            .setNegativeButton("НЕТ") {_,_ ->}
            .create().show()

    }
}






