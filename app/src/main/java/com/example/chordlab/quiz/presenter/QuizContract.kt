package com.example.chordlab.quiz.presenter

import com.example.chordlab.quiz.model.QuizDifficulty
import com.example.chordlab.quiz.model.QuizQuestion

interface QuizContract {

    interface View {
        fun showDifficultyChoices()
        fun showQuiz(difficulty: QuizDifficulty)
        fun showQuestion(question: QuizQuestion, questionNumber: Int, totalQuestions: Int, score: Int)
        fun showAnswerResult(isCorrect: Boolean, correctAnswer: String)
        fun showFinalScore(score: Int, totalQuestions: Int)
    }

    interface Presenter {
        fun startQuiz(difficulty: QuizDifficulty)
        fun submitAnswer(choiceIndex: Int)
        fun nextQuestion()
        fun restartQuiz()
        fun detachView()
    }
}
