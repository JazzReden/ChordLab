package com.example.chordlab.quiz.model

class QuizRepository {

    fun getQuestions(difficulty: QuizDifficulty): List<QuizQuestion> {
        return when (difficulty) {
            QuizDifficulty.EASY -> easyQuestions()
            QuizDifficulty.MEDIUM -> mediumQuestions()
            QuizDifficulty.HARD -> hardQuestions()
        }.shuffled()
    }

    private fun easyQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion("What is the standard tuning of a guitar?", listOf("EADGBE", "ABCDEF", "GDAE", "CDEFGH"), 0),
            QuizQuestion("How many strings does a guitar have?", listOf("4", "5", "6", "7"), 2),
            QuizQuestion("What does the letter A represent in music?", listOf("A chord shape", "A musical note", "A guitar string", "A scale type"), 1),
            QuizQuestion("What is a chord?", listOf("One note only", "Two or more notes played together", "A guitar brand", "A tuning method"), 1),
            QuizQuestion("What is the first string called on a guitar?", listOf("Thick string", "Low E string", "High E string", "Middle string"), 2),
            QuizQuestion("What is a fret?", listOf("A guitar case", "A space between metal bars on the neck", "A tuning peg", "A chord type"), 1),
            QuizQuestion("Which finger is usually used first for basic chords?", listOf("Thumb", "Index finger", "Pinky only", "Elbow"), 1),
            QuizQuestion("What symbol shows a sharp note?", listOf("b", "#", "/", "*"), 1),
            QuizQuestion("What is the basic major chord of C?", listOf("C major", "A minor", "G major", "D minor"), 0),
            QuizQuestion("What do you call playing one note at a time?", listOf("Chord", "Melody", "Strumming", "Tuning"), 1)
        )
    }

    private fun mediumQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion("What is the difference between major and minor chords?", listOf("Major is louder", "Minor sounds sadder", "Major has more strings", "Minor has no notes"), 1),
            QuizQuestion("What notes are in a G major chord?", listOf("G B D", "A C E", "C D E", "F A C"), 0),
            QuizQuestion("What is a scale in guitar?", listOf("A guitar brand", "A set of notes in order", "A tuning tool", "A chord shape"), 1),
            QuizQuestion("What is a barre chord?", listOf("Open string chord", "Using one finger to press multiple strings", "Strumming fast", "Silent chord"), 1),
            QuizQuestion("What is the function of a capo?", listOf("Tune strings", "Shorten guitar neck pitch", "Break strings", "Hold picks"), 1),
            QuizQuestion("What is the relative minor of C major?", listOf("A minor", "D minor", "E minor", "G minor"), 0),
            QuizQuestion("How many notes are in a major scale?", listOf("5", "6", "7", "8"), 2),
            QuizQuestion("What does tab mean in guitar?", listOf("Table", "Tablature notation", "Tuning audio", "Tempo adjustment"), 1),
            QuizQuestion("What is a power chord used for?", listOf("Jazz melody", "Simple rock sound", "Classical music only", "Singing"), 1),
            QuizQuestion("Order of strings from thickest to thinnest?", listOf("E A D G B E", "E B G D A E", "A D G B E E", "G A B C D E"), 0)
        )
    }

    private fun hardQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion("What intervals form a major chord?", listOf("1-3-5", "1-2-4", "1-4-7", "1-3-6"), 0),
            QuizQuestion("Formula of a minor scale?", listOf("Whole Whole Half Whole Whole Whole Half", "Half Whole Half Whole Whole Whole Whole", "Whole Half Whole Half Whole Whole Whole", "Whole Whole Whole Whole Half Half Whole"), 1),
            QuizQuestion("Why is the 5th important in power chords?", listOf("It creates melody", "It defines harmony stability", "It changes tuning", "It removes rhythm"), 1),
            QuizQuestion("What is harmonic function in music theory?", listOf("Guitar speed", "Role of chords in progression", "String order", "Tuning method"), 1),
            QuizQuestion("Difference between Dorian and Aeolian mode?", listOf("Same notes", "Different 6th note", "Same tuning", "No difference"), 1),
            QuizQuestion("What happens when you flatten the 3rd note?", listOf("Major becomes minor", "Sound disappears", "Becomes scale", "Becomes chordless"), 0),
            QuizQuestion("Purpose of extended chords?", listOf("Reduce notes", "Add richer harmony", "Remove rhythm", "Simplify tuning"), 1),
            QuizQuestion("What is relative pitch?", listOf("Loudness control", "Ability to identify notes by reference", "Guitar tuning tool", "String tension"), 1),
            QuizQuestion("Why use alternate tunings?", listOf("For decoration", "To change sound and playability", "To break guitar", "To reduce strings"), 1),
            QuizQuestion("What is chord inversion?", listOf("Changing guitar color", "Rearranging chord notes order", "Removing notes", "Tuning strings"), 1)
        )
    }
}
