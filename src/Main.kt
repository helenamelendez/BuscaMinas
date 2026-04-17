class Menu() {
    val ROJO = "\u001B[31m"
    val VERDE = "\u001B[32m"
    val AMARILLO = "\u001B[33m"
    val RESET = "\u001B[0m"
    val AZUL = "\u001B[34m"
    fun parametrosConfiguracion(): Triple<Int, Int, Int> {
        var entero = EntradaValidacion()
        println("=== Configuración del Buscaminas ===")

        val filas = entero.leerEntero("Introduce filas (1-8): ", "filas", 1, 8)
        val columnas = entero.leerEntero("Introduce columnas (1-8): ", "columnas", 1, 8)

        val totalCasillas = filas * columnas

        val minas = entero.leerEntero("Introduce minas (1-$totalCasillas): ", "minas", 1, totalCasillas - 1)

        return Triple(filas, columnas, minas)
    }
    fun pedirAccion(filas: Int, columnas: Int): Triple<String, Int, Int> {
        var entrada = EntradaValidacion()
        // 1. Preguntar al usuario qué quiere hacer
        println("¿Qué deseas hacer?")
        var option = entrada.leerEntero("1. Abrir celda.\n2. Poner bandera\n", "opcion", 1, 2)
        var fila = entrada.leerEntero("Introduce la coordenada que corresponde a la fila: ", "fila", 0, filas - 1)
        var columna = entrada.leerEntero("Introduce la coordenada que corresponde a la columna: ", "columna", 0, columnas - 1)

        return Triple(if (option == 1) "Abrir" else "Bandera", fila, columna)
    }
    fun imprimirTablero(buscaminas: Buscaminas) {
        for (f in 0 until buscaminas.tablero.arrayCeldas.size) {
            for (c in 0 until buscaminas.tablero.arrayCeldas[f].size) {
                if (buscaminas.tablero.arrayCeldas[f][c].visibilidad == '0') {
                    print(VERDE + "[${buscaminas.tablero.arrayCeldas[f][c].visibilidad}]" + RESET)
                }
                else if (buscaminas.tablero.arrayCeldas[f][c].visibilidad == 'X') {
                    print(ROJO + "[${buscaminas.tablero.arrayCeldas[f][c].visibilidad}]" + RESET)
                }
                else if (
                    buscaminas.tablero.arrayCeldas[f][c].visibilidad == 'B'
                ) {
                    print(AZUL + "[${buscaminas.tablero.arrayCeldas[f][c].visibilidad}]" + RESET)
                }
                else if (
                    buscaminas.tablero.arrayCeldas[f][c].visibilidad != '0' &&
                    buscaminas.tablero.arrayCeldas[f][c].visibilidad != '?'
                ) {
                    print(AMARILLO + "[${buscaminas.tablero.arrayCeldas[f][c].visibilidad}]" + RESET)
                }
                else print("[${buscaminas.tablero.arrayCeldas[f][c].visibilidad}]")
            }
            println()
        }
    }
    fun jugar(juego: Buscaminas, filas: Int, columnas: Int){
        do {
            var (opcion,fila,columna) = pedirAccion(filas,columnas)
            var continuar: Boolean
            var victoria  = true
            if (opcion=="Abrir") {
                continuar = juego.abriCasilla(fila,columna)
                if (continuar){
                    println(AZUL + "Revelando" + RESET)
                }
                else {
                    println(ROJO + "Game Over" + RESET )
                    juego.revelarMinas()
                }
                imprimirTablero(juego)
                victoria = juego.verificarVictoria()
                if (victoria){
                    println(VERDE + "Victoria" + RESET)
                }
            }
            else {
                println(AZUL + "Colocando Bandera" + RESET)
                continuar = juego.colocarBandera(fila,columna)
                imprimirTablero(juego)
            }
        } while(continuar && !victoria )
    }
}
class EntradaValidacion(){
    fun leerEntero(mensaje: String, tipo: String, min: Int, max: Int): Int {
        while (true) {
            print(mensaje)
            val valor = readlnOrNull()?.toIntOrNull()

            if (valor != null && valor in min..max) {
                return valor
            }

            throw IllegalArgumentException("Error! numero de $tipo invalido. Debe estar entre $min y $max.")
        }
    }

}
fun main() {
    var menu = Menu()
    var (filas,columnas,minas) = menu.parametrosConfiguracion()
    var tablero = Tablero(filas,columnas,minas)
    var juego = Buscaminas(tablero)
    menu.imprimirTablero(juego)
    menu.jugar(juego,filas,columnas)



}