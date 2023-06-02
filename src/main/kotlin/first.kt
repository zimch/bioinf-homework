import java.io.File
import java.io.InputStream

fun main() {
    var sequence: String = readGen("src/main/resources/genome.txt")

    val orfs = findOpenReadingFrames(sequence)
    println("ORFs: $orfs")

    val protein = translateOrfToProtein(orfs.get(0))
    println("Protein: $protein")

}

fun findOpenReadingFrames(sequence: String): List<String> {
    val startCodons = setOf("AUG", "ATG")
    val stopCodons = setOf("TAA", "TAG", "TGA")

    val orfs = mutableListOf<String>()
    var startIndex = -1

    for (i in sequence.indices) {
        if (i + 2 < sequence.length && startCodons.contains(sequence.substring(i, i+3))) {
            startIndex = i + 3

            while (startIndex + 2 < sequence.length &&
                !stopCodons.contains(sequence.substring(startIndex, startIndex+3))) {
                startIndex += 3
            }

            if (startIndex + 2 < sequence.length) {
                orfs.add(sequence.substring(i, startIndex))
            }
        }
    }

    return orfs
}

fun translateOrfToProtein(orf: String): String {
    val codonTable = mapOf(
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

    val protein = StringBuilder()
    for (i in orf.indices step 3) {
        if (i + 2 >= orf.length) break

        val codon = orf.substring(i, i+3)
        val aminoAcid = codonTable[codon] ?: continue
        if (aminoAcid == "_stop_") break

        protein.append(aminoAcid)
    }
    return protein.toString()
}


fun readGen(filename: String): String {
    val inputStream: InputStream = File(filename).inputStream()

    return inputStream.bufferedReader().use { it.readText() }
}