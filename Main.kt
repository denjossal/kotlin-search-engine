package search

import java.io.File
import java.io.InputStream

enum class SearchStrategies {
    ALL,
    ANY,
    NONE
}

fun main(args: Array<String>) {
    val data = readData(args[1])
    var option = -1
    while (option != 0) {
        menu()
        option = readln().replace("> ", "").toInt()
        when (option) {
            0 -> menuOpt3(data)
            1 -> menuOpt1(data)
            2 -> menuOpt2(data)
            else -> println("Incorrect option! Try again.")
        }
    }


}

fun readData(fileName: String): HashMap<Int, String> {
    val data = hashMapOf<Int, String>()
    val inputStream: InputStream = File(fileName).inputStream()
    val lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    var c = 1
    for (line in lineList) {
        data[c] = line
        c++
    }
    return data
}

fun menu() {
    println("=== Menu ===")
    println("1. Find a person")
    println("2. Print all people")
    println("0. Exit")
}

fun menuOpt1(data: HashMap<Int, String>) {
    val strategy = subMenuOpt11()
    println("Enter a name or email to search all suitable people.")
    val query = readln().replace("> ", "").lowercase()
    var c = 0
    var results = mutableListOf<String>()
    if (query.length > 1) {
        when (strategy) {
            SearchStrategies.ALL -> {
                data.filter { l ->
                    l.value.lowercase().contains(query)
                }.forEach { l ->
                    results.add(l.value)
                    c++
                }
            }

            SearchStrategies.ANY -> {
                val subQuery = query.split(" ")
                subQuery.forEach {
                    data.filter { l ->
                        l.value.lowercase().contains(it)
                    }.forEach { l ->
                        results.add(l.value)
                        c++
                    }
                }
            }

            SearchStrategies.NONE -> {
                val subQuery = query.split(" ")
                val dataToRemove = mutableListOf<String>()
                data.forEach{
                    dataToRemove.add(it.value)
                }
                subQuery.forEach {
                    sq -> dataToRemove.removeIf{
                        e -> e.lowercase().contains(sq)
                }
                }
                c = dataToRemove.size
                results = dataToRemove.toMutableList()
            }
        }

        if (c == 0) {
            println("No matching people found")
        } else if (c == 1) {
            println("$c person found")
        } else if (c > 1) {
            println("$c persons found")
        }
        results.forEach {
            println(it)
        }
    } else {
        println("No matching people found")
    }
}

fun subMenuOpt11(): SearchStrategies {
    println("Select a matching strategy: ALL, ANY, NONE")
    val option: String = readln().replace("> ", "")
    return SearchStrategies.valueOf(option)
}

fun menuOpt2(data: HashMap<Int, String>) {
    println("=== List of people ===")
    data.forEach { (_, u) -> println(u) }
}

fun menuOpt3(data: HashMap<Int, String>) {
    data.clear()
    print("Bye!")
}
