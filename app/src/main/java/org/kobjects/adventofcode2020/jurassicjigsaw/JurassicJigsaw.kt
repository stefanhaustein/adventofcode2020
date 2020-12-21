import org.kobjects.adventofcode2020.jurassicjigsaw.rawTiles

class Tile(val id: Int, vararg val borders: Int)

fun decode(line: String):Int {
    val code = Integer.parseInt(line.trim().replace('.', '0').replace('#', '1'), 2)
    return Math.min(code, Integer.reverse(code shl 22))
}

fun parseTiles(unparsed: String): Map<Int, Tile> {
    val tiles = mutableMapOf<Int, Tile>()
    val lines = unparsed.split("\n")
    val left = StringBuilder()
    val right = StringBuilder()
    var border0 = 0
    var id = 0
    var row = 0

    for (line in lines) {
        if (line.startsWith("Tile")) {
            id = Integer.parseInt(line.substring(5, line.indexOf(':')))
            row = 0
            right.clear()
            left.clear()
        } else if (!line.isBlank()) {
            left.append(line[0])
            right.append(line[9])
            if (row == 0) {
                border0 = decode(line)
            } else if (row == 9) {
                tiles.put(id, Tile(id, border0, decode(left.toString()), decode(right.toString()), decode(line)))
            }
            row++
        }
    }
    return tiles
}

fun main(args: Array<String>) {
    val tiles = parseTiles(rawTiles)
    val borderToId = mutableMapOf<Int, MutableList<Int>>()

    for (tile in tiles.values) {
        for (border in tile.borders) {
            borderToId.compute(border, { k, list -> if (list == null) mutableListOf(tile.id) else {list.add(tile.id);list} })
        }
    }
    var result = 1L;
    for (tile in tiles.values) {
        var unmatched = 0
        for (border in tile.borders) {
            if (borderToId[border]!!.size == 1) {
                unmatched++
            }
        }
        if (unmatched == 2) {
            result *= tile.id
        }
    }
    System.out.println("Result: " + result)
}