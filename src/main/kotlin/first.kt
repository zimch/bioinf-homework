import java.io.File
import java.io.InputStream

fun main() {
    var str: String = readGen("src/main/resources/genome.txt")
    var startIndex: Int = if (str.length < 23) 0 else 20

    var startATG: Int = 0
    var endATG: Int = 0

    for(i in startIndex until str.length-3) {
        if (str.subSequence(i, i + 3).equals("ATG")) {
            startATG = i
            for(j in i+3 until str.length-3 step 3) {
                if (str.subSequence(j, j + 3).equals("TAA")
                    || str.subSequence(j, j + 3).equals("TAG")
                    || str.subSequence(j, j + 3).equals("TGA")) {
                    endATG = j + 3
//                    println("ans: " + startATG + " / " + endATG)
                    println(str.subSequence(startATG, endATG))
                    println(convertToProtein(str.subSequence(startATG, endATG)))
                    println()
                }
            }
        }
    }

}

fun convertToProtein(seq: CharSequence): String {
    var aminosToProtein: Map<String, String> = mapOf(
        "AGG" to "R", "AGA" to "R", "AGC" to "S", "AGT" to "S",
        "AAG" to "K", "AAA" to "K", "AAC" to "N", "AAT" to "N",
        "ACG" to "T", "ACA" to "T", "ACC" to "T", "ACT" to "T",
        "ATG" to "M", "ATA" to "I", "ATC" to "I", "ATT" to "I",

        "CGF" to "R", "CGA" to "R", "CGC" to "S", "CGT" to "S",
        "CAG" to "Q", "CAA" to "Q", "CAC" to "H", "CAT" to "H",
        "CCG" to "P", "CCA" to "P", "CCC" to "P", "CCT" to "P",
        "CTG" to "L", "CTA" to "L", "CTC" to "L", "CTT" to "L",

        "TGG" to "W", "TGA" to "_stop_", "TGC" to "C", "TGT" to "C",
        "TAG" to "_stop_", "TAA" to "_stop_", "TAC" to "Y", "TAT" to "Y",
        "TCG" to "S", "TCA" to "S", "TCC" to "S", "TCT" to "S",
        "TTG" to "L", "TTA" to "L", "TTC" to "F", "TTT" to "F",

        "GGG" to "G", "GGA" to "G", "GGC" to "G", "GGT" to "G",
        "GAG" to "E", "GAA" to "E", "GAC" to "D", "GAT" to "D",
        "GCG" to "A", "GCA" to "A", "GCC" to "A", "GCT" to "A",
        "GTG" to "V", "GTA" to "V", "GTC" to "V", "GTT" to "V"
    )

    var ans = ""

    for(i in 0 .. seq.length - 3 step 3) {
        ans += aminosToProtein.get(seq.subSequence(i, i+3))
    }

    return ans
}

fun readGen(filename: String): String {
    val inputStream: InputStream = File(filename).inputStream()

    return inputStream.bufferedReader().use { it.readText() }
}