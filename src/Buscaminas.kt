import kotlin.random.Random

data class Celda(var fila: Int,var columna: Int, var proximidad: Int,var visibilidad :Char)

data class Tablero(var filas: Int, var columnas: Int, var minas : Int){
    var arrayCeldas = Array(filas){ Array<Celda>(columnas) {Celda(0,0,0,'?')} }
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
}

class Buscaminas(t : Tablero) {
   var tablero = t // No he podido aplicar bien el principio de ocultación

    private fun revelarCasillas(f: Int, c: Int) {
        if (f !in tablero.arrayCeldas.indices || c !in tablero.arrayCeldas[f].indices) return // Si no esta en rango, salir
        if (tablero.arrayCeldas[f][c].visibilidad != '?') return // Si ya fue revelada, salir

        tablero.arrayCeldas[f][c].visibilidad = tablero.arrayCeldas[f][c].proximidad.toString().first() // Revelar casilla actual

        if (tablero.arrayCeldas[f][c].proximidad != 0) return // Si NO es 0, no expandir

        // Expandir vecinos
        for (filas in f - 1..f + 1) {
            for (columnas in c - 1..c + 1) {
                if (filas in tablero.arrayCeldas.indices && columnas in tablero.arrayCeldas[filas].indices) {
                    revelarCasillas(filas, columnas)
                }
            }
        }
    }
    fun revelarMinas(){
        for (f in 0 until tablero.arrayCeldas.size) {
            for (c in 0 until tablero.arrayCeldas[f].size) {
                if (tablero.arrayCeldas[f][c].proximidad == 9) {
                    tablero.arrayCeldas[f][c].visibilidad = 'X'
                }
            }
        }
    }
    fun abriCasilla(f: Int, c: Int ): Boolean{
        if (tablero.arrayCeldas[f][c].proximidad != 9){
            revelarCasillas(f,c)
            return true
        }
        else {
            tablero.arrayCeldas[f][c].visibilidad = 'X'
            return false
        }
    }
    fun colocarBandera(f: Int,c: Int): Boolean{
        if (tablero.arrayCeldas[f][c].visibilidad == '?'){
            tablero.arrayCeldas[f][c].visibilidad = 'B'
        }
        else if (tablero.arrayCeldas[f][c].visibilidad == 'B'){
            tablero.arrayCeldas[f][c].visibilidad = '?'
        }
        return true
    }
    fun verificarVictoria(): Boolean{
        var contador = 0
        for (f in 0 until tablero.arrayCeldas.size) {
            for (c in 0 until tablero.arrayCeldas[f].size) {
                if (tablero.arrayCeldas[f][c].visibilidad == '?' || tablero.arrayCeldas[f][c].visibilidad == 'B')
                    contador++
            }
        }
        return contador == tablero.minas
    }


}
