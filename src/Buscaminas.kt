import kotlin.random.Random

data class celda(var fila: Int,var columna: Int, var proximidad: Int,var visibilidad :Char)
data class Tablero(var filas: Int, var columnas: Int, var minas : Int){
    var arrayCeldas = Array(filas){ Array<celda>(columnas) {celda(0,0,0,'?')} }
    init {
        numerarCeldas()
        randomizarMinas()
        relacionNumerica()
    }
    private fun numerarCeldas(){
        for (f in 0 until arrayCeldas.size) {
            for (c in 0 until arrayCeldas[f].size) {
                arrayCeldas[f][c].fila = f
                arrayCeldas[f][c].columna = c
            }
        }
    }
    private fun randomizarMinas(){
        var contador = minas
        while (contador > 0) {
            contador--
            val f = Random.nextInt(0, filas)
            val c= Random.nextInt(0, columnas)
            if (arrayCeldas[f][c].proximidad == 9){
                contador++
                continue
            }
            arrayCeldas[f][c].proximidad = 9
        }
    }
    private fun relacionNumerica(){
        for (f in 0 until arrayCeldas.size) {
            for (c in 0 until arrayCeldas[f].size) {
                if (arrayCeldas[f][c].proximidad == 9) {
                    for (nf in f - 1..f + 1) {
                        for (nc in c - 1..c + 1) {
                            if (nf !in arrayCeldas.indices || nc !in arrayCeldas[nf].indices) continue
                            if (arrayCeldas[nf][nc] == arrayCeldas[f][c]) continue
                            if (arrayCeldas[nf][nc].proximidad != 9) {
                                arrayCeldas[nf][nc].proximidad++
                            }
                        }
                    }
                }
            }
        }
    }
    private fun revelarCasillas(f: Int, c: Int) {
        if (f !in arrayCeldas.indices || c !in arrayCeldas[f].indices) return // Si no esta en rango, salir
        if (arrayCeldas[f][c].visibilidad != '?') return // Si ya fue revelada, salir

        arrayCeldas[f][c].visibilidad = arrayCeldas[f][c].proximidad.toString().first() // Revelar casilla actual

        if (arrayCeldas[f][c].proximidad != 0) return // Si NO es 0, no expandir

        // Expandir vecinos
        for (filas in f - 1..f + 1) {
            for (columnas in c - 1..c + 1) {
                if (filas in arrayCeldas.indices && columnas in arrayCeldas[filas].indices) {
                    revelarCasillas(filas, columnas)
                }
            }
        }
    }

    fun revelarMinas(){
        for (f in 0 until arrayCeldas.size) {
            for (c in 0 until arrayCeldas[f].size) {
                if (arrayCeldas[f][c].proximidad == 9) {
                    arrayCeldas[f][c].visibilidad = 'X'
                }
            }
        }
    }
    fun verificarVictoria(): Boolean{
        var contador = 0
        for (f in 0 until arrayCeldas.size) {
            for (c in 0 until arrayCeldas[f].size) {
                if (arrayCeldas[f][c].visibilidad == '?' || arrayCeldas[f][c].visibilidad == 'B')
                    contador++
            }
        }
        return contador == minas
    }
    fun abriCasilla(f: Int, c: Int ): Boolean{
        if (arrayCeldas[f][c].proximidad != 9){
            revelarCasillas(f,c)
            return true
        }
        else {
            arrayCeldas[f][c].visibilidad = 'X'
            return false
        }
    }


}

class Buscaminas(var tablero : Tablero) {


}
