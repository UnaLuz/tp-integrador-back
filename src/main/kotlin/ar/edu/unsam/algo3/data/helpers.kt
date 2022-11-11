package ar.edu.unsam.algo3.data

import java.sql.ResultSet

/**
 * Loop through the [ResultSet] generating a list,
 * using the provided map block for each item
 */
internal fun <T> ResultSet.mapToList(mapBlock: (ResultSet) -> T) =
    use {
        generateSequence {
            if (it.next()) mapBlock(it) else null
        }.toList()  // must be inside the use() block
    }