package redstonetim.lacomputadora

import redstonetim.lacomputadora.Data.caesar
import redstonetim.lacomputadora.Data.cicero
import redstonetim.lacomputadora.Data.haenselundgretel
import redstonetim.lacomputadora.Data.rotkaeppchen
import redstonetim.lacomputadora.Data.trumptwitter
import redstonetim.lacomputadora.Data.zz
import kotlin.random.Random

object Markov {
    const val orderNum = 7
    private val collectedData = hashMapOf<String, HashMap<String, MutableList<Char>>>()

    val texts = hashMapOf(
        "cicero" to cicero,
        "caesar" to caesar,
        "zz" to zz,
        "rotkaeppchen" to rotkaeppchen,
        "haenselundgretel" to haenselundgretel,
        "trumptwitter" to trumptwitter
    )

    fun load() {
        for ((name, text) in texts) {
            val data = HashMap<String, MutableList<Char>>()
            for (i in 0 until text.length - orderNum) {
                val ngram: String = text.substring(i, i + orderNum)
                data.getOrPut(ngram) { arrayListOf() }.add(text.elementAt(i + orderNum))
            }
            collectedData[name] = data
        }
    }

    fun generate(name: String, length: Int = 1000): String {
        val data = collectedData[name]
        return if (data == null) {
            "Couldn't find author"
        } else {
            val array = data.keys.toTypedArray()
            var current = array[Random.nextInt(array.size)]
            for (i in 0 until (length - orderNum)) {
                val possibilities = data[current.substring(current.length - orderNum, current.length)]
                    ?: break
                current += possibilities[Random.nextInt(possibilities.size)]
            }
            current
        }
    }
}