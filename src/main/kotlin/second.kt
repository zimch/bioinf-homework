import biokotlin.seq.*
import biokotlin.seqIO.*

fun main() {
    val fasta = NucSeqIO("src/main/resources/data   .fasta")
    val sequenceSeq = fasta.read()?.sequence ?: return
    val sequence = sequenceSeq.toString()

    val intronUpperSite = "GT"
    val intronLowerSite = "AG"

    val exonUpperSite = "AT"
    val exonLowerSite = "AC"

    val intronSpliceSites = mutableListOf<Pair<IntRange, String>>()
    val exonSpliceSites = mutableListOf<Pair<IntRange, String>>()

    var i = 0
    while (i < sequence.length) {
        if (sequence.startsWith(intronUpperSite, i)) {
            val intronUpperPos = i
            i += intronUpperSite.length

            val j = sequence.indexOf(intronLowerSite, i)
            if (j != -1) {
                val intronLowerPos = j + intronLowerSite.length
                intronSpliceSites.add((intronUpperPos until intronLowerPos) to sequence.substring(intronUpperPos, intronLowerPos))
                i = j + intronLowerSite.length
            } else {
                i = sequence.length
            }
            // Ищем грани экзона
        } else if (sequence.startsWith(exonUpperSite, i)) {
            val exonUpperPos = i
            i += exonUpperSite.length

            val j = sequence.indexOf(exonLowerSite, i)
            if (j != -1) {
                val exonLowerPos = j + exonLowerSite.length
                exonSpliceSites.add((exonUpperPos until exonLowerPos) to sequence.substring(exonUpperPos, exonLowerPos))
                i = j + exonLowerSite.length
            } else {
                i = sequence.length
            }
        } else {
            i++
        }
    }

    if (intronSpliceSites.isNotEmpty()) {
        println("Предполагаемые сайты сплайсинга на гранях интронов:")
        for ((range, spliceSite) in intronSpliceSites) {
            println("$spliceSite: $range")
        }
    }

    if (exonSpliceSites.isNotEmpty()) {
        println("Предполагаемые сайты сплайсинга на гранях экзонов:")
        for ((range, spliceSite) in exonSpliceSites) {
            println("$spliceSite: $range")
        }
    }

    if (intronSpliceSites.isEmpty() && exonSpliceSites.isEmpty()) {
        println("Сайты сплайсинга не обнаружены")
    }
}
