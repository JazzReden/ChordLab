package com.example.chordlab.quiz.model

data class QuizQuestion(
    val question: String,
    val choices: List<String>,
    val correctAnswerIndex: Int
)
