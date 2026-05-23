package com.example.chordlab.quiz.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.R
import com.example.chordlab.quiz.model.QuizDifficulty
import com.example.chordlab.quiz.model.QuizQuestion
import com.example.chordlab.quiz.model.QuizRepository
import com.example.chordlab.quiz.presenter.QuizContract
import com.example.chordlab.quiz.presenter.QuizPresenter

class QuizActivity : AppCompatActivity(), QuizContract.View {

    private lateinit var difficultyLayout: LinearLayout
    private lateinit var quizLayout: LinearLayout
    private lateinit var quizTitleTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var nextButton: Button
    private lateinit var restartButton: Button
    private lateinit var backToDashboardButton: Button
    private lateinit var quizDashboardBackButton: Button
    private lateinit var presenter: QuizContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        bindViews()

        val quizRepository = QuizRepository()
        presenter = QuizPresenter(this, quizRepository)

        findViewById<Button>(R.id.easyQuizButton).setOnClickListener {
            presenter.startQuiz(QuizDifficulty.EASY)
        }

        findViewById<Button>(R.id.mediumQuizButton).setOnClickListener {
            presenter.startQuiz(QuizDifficulty.MEDIUM)
        }

        findViewById<Button>(R.id.hardQuizButton).setOnClickListener {
            presenter.startQuiz(QuizDifficulty.HARD)
        }

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                presenter.submitAnswer(index)
            }
        }

        nextButton.setOnClickListener {
            presenter.nextQuestion()
        }

        restartButton.setOnClickListener {
            presenter.restartQuiz()
        }

        backToDashboardButton.setOnClickListener {
            finish()
        }

        quizDashboardBackButton.setOnClickListener {
            finish()
        }

        showDifficultyChoices()
    }

    override fun showDifficultyChoices() {
        difficultyLayout.visibility = View.VISIBLE
        quizLayout.visibility = View.GONE
        backToDashboardButton.visibility = View.GONE
        quizDashboardBackButton.visibility = View.VISIBLE
    }

    override fun showQuiz(difficulty: QuizDifficulty) {
        difficultyLayout.visibility = View.GONE
        quizLayout.visibility = View.VISIBLE
        quizTitleTextView.text = "${difficulty.displayName} Quiz"
        backToDashboardButton.visibility = View.GONE
        quizDashboardBackButton.visibility = View.GONE
    }

    override fun showQuestion(question: QuizQuestion, questionNumber: Int, totalQuestions: Int, score: Int) {
        progressTextView.text = "Question $questionNumber of $totalQuestions"
        scoreTextView.text = "Score: $score"
        questionTextView.text = question.question
        resultTextView.text = ""
        nextButton.visibility = View.GONE
        backToDashboardButton.visibility = View.GONE

        optionButtons.forEachIndexed { index, button ->
            button.text = "${('A' + index)}. ${question.choices[index]}"
            button.isEnabled = true
            button.isClickable = true
            button.alpha = 1f
            button.visibility = View.VISIBLE
        }
    }

    override fun showAnswerResult(isCorrect: Boolean, correctAnswer: String) {
        resultTextView.text = if (isCorrect) {
            "Correct!"
        } else {
            "Wrong. Correct answer: $correctAnswer"
        }

        optionButtons.forEach { button ->
            button.isClickable = false
            button.alpha = 1f
        }

        nextButton.visibility = View.VISIBLE
    }

    override fun showFinalScore(score: Int, totalQuestions: Int) {
        Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show()
        progressTextView.text = "Quiz Complete"
        scoreTextView.text = "Final Score: $score / $totalQuestions"
        questionTextView.text = "Great work. Choose another level to challenge yourself again."
        resultTextView.text = ""
        nextButton.visibility = View.GONE
        backToDashboardButton.visibility = View.VISIBLE

        optionButtons.forEach { button ->
            button.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun bindViews() {
        difficultyLayout = findViewById(R.id.difficultyLayout)
        quizLayout = findViewById(R.id.quizLayout)
        quizTitleTextView = findViewById(R.id.quizTitleTextView)
        progressTextView = findViewById(R.id.progressTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        questionTextView = findViewById(R.id.questionTextView)
        resultTextView = findViewById(R.id.resultTextView)
        nextButton = findViewById(R.id.nextQuestionButton)
        restartButton = findViewById(R.id.restartQuizButton)
        backToDashboardButton = findViewById(R.id.backToDashboardButton)
        quizDashboardBackButton = findViewById(R.id.quizDashboardBackButton)
        optionButtons = listOf(
            findViewById(R.id.optionAButton),
            findViewById(R.id.optionBButton),
            findViewById(R.id.optionCButton),
            findViewById(R.id.optionDButton)
        )
    }
}
