package com.example.chordlab.quiz.presenter

import com.example.chordlab.quiz.model.QuizDifficulty
import com.example.chordlab.quiz.model.QuizQuestion
import com.example.chordlab.quiz.model.QuizRepository

class QuizPresenter(
    private var view: QuizContract.View?,
    private val quizRepository: QuizRepository
) : QuizContract.Presenter {

    private var questions: List<QuizQuestion> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private var hasAnswered = false

    override fun startQuiz(difficulty: QuizDifficulty) {
        questions = quizRepository.getQuestions(difficulty)
        currentQuestionIndex = 0
        score = 0
        hasAnswered = false

        view?.showQuiz(difficulty)
        showCurrentQuestion()
    }

    override fun submitAnswer(choiceIndex: Int) {
        if (hasAnswered || questions.isEmpty()) return

        val currentQuestion = questions[currentQuestionIndex]
        val isCorrect = choiceIndex == currentQuestion.correctAnswerIndex

        if (isCorrect) {
            score++
        }

        hasAnswered = true
        view?.showAnswerResult(isCorrect, currentQuestion.choices[currentQuestion.correctAnswerIndex])
    }

    override fun nextQuestion() {
        if (!hasAnswered) return

        currentQuestionIndex++
        hasAnswered = false

        if (currentQuestionIndex >= questions.size) {
            view?.showFinalScore(score, questions.size)
        } else {
            showCurrentQuestion()
        }
    }

    override fun restartQuiz() {
        questions = emptyList()
        currentQuestionIndex = 0
        score = 0
        hasAnswered = false
        view?.showDifficultyChoices()
    }

    override fun detachView() {
        view = null
    }

    private fun showCurrentQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        view?.showQuestion(
            question = currentQuestion,
            questionNumber = currentQuestionIndex + 1,
            totalQuestions = questions.size,
            score = score
        )
    }
}
