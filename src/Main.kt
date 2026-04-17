class Menu() {
    val ROJO = "\u001B[31m"
    val VERDE = "\u001B[32m"
    val AMARILLO = "\u001B[33m"
    val RESET = "\u001B[0m"
    val AZUL = "\u001B[34m"
    fun parametrosConfiguracion(): Triple<Int, Int, Int> {
        val entero = EntradaValidacion()
        println("=== Configuración del Buscaminas ===")
        var filas = entero.leerEntero("Introduce filas (1-8): ", "filas", 1, 8)
        while (filas == -1){
            filas = entero.leerEntero("Introduce filas (1-8): ", "filas", 1, 8)
        }
        var columnas = entero.leerEntero("Introduce columnas (1-8): ", "columnas", 1, 8)
        while (columnas == -1){
            columnas = entero.leerEntero("Introduce columnas (1-8): ", "columnas", 1, 8)
        }
        val totalCasillas = filas * columnas
        var minas = entero.leerEntero("Introduce minas (1-$totalCasillas): ", "minas", 1, totalCasillas)
        while (minas == -1){
            minas = entero.leerEntero("Introduce minas (1-$totalCasillas): ", "minas", 1, totalCasillas)
        }
        return Triple(filas, columnas, minas)
    }

    fun pedirAccion(filas: Int, columnas: Int): Triple<String, Int, Int> {
        val entrada = EntradaValidacion()
        // 1. Preguntar al usuario qué quiere hacer
        println("¿Qué deseas hacer?")
        var opcion = entrada.leerEntero("1. Abrir celda.\n2. Poner o quitar bandera\n", "opcion", 1, 2)
        while (opcion == -1){
            opcion = entrada.leerEntero("1. Abrir celda.\n2. Poner o quitar bandera\n", "opcion", 1, 2)
        }
        var fila = entrada.leerEntero("Introduce la coordenada que corresponde a la fila: ", "fila", 0, filas - 1)
        while (fila == -1){
            fila = entrada.leerEntero("Introduce la coordenada que corresponde a la fila: ", "fila", 0, filas - 1)
        }
        var columna = entrada.leerEntero("Introduce la coordenada que corresponde a la columna: ", "columna", 0, columnas - 1)
        while (columna == -1){
            columna = entrada.leerEntero("Introduce la coordenada que corresponde a la columna: ", "columna", 0, columnas - 1)
        }
        return Triple(if (opcion == 1) "Abrir" else "Bandera", fila, columna)
    }

    fun imprimirTablero(buscaminas: Buscaminas) {
        print(VERDE + "F" + RESET + AZUL + "C" + RESET) // esquina superior izquierda vacía
        for (c in 0 until buscaminas.tablero.arrayCeldas[0].size) {
            print(AZUL + " $c " + RESET)
        }
        println()
        for (f in 0 until buscaminas.tablero.arrayCeldas.size) {
            print(VERDE + "$f " + RESET)
            for (c in 0 until buscaminas.tablero.arrayCeldas[f].size) {
                val celda = buscaminas.tablero.arrayCeldas[f][c].visibilidad
                when (celda) {
                    '0' -> print(VERDE + "[$celda]" + RESET)
                    'X' -> print(ROJO + "[${celda}]" + RESET)
                    'B' -> print(AZUL + "[${celda}]" + RESET)
                    '?' -> print(RESET + "[$celda]")
                    else -> print(AMARILLO + "[$celda]"+ RESET)
                }
            }
            println()
        }
    }

    fun jugar(juego: Buscaminas, filas: Int, columnas: Int){
        do {
            val (opcion,fila,columna) = pedirAccion(filas,columnas)
            var continuar: Boolean
            var victoria : Boolean
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
                victoria = juego.verificarVictoria()
                if (victoria){
                    println(VERDE + "Victoria" + RESET)
                }
            }
        } while(continuar && !victoria )
    }

    fun volverAJugar(): Boolean {
        val entrada = EntradaValidacion()
        // 1. Preguntar al usuario qué quiere hacer
        println("¿Desea volver a jugar?")
        var opcion = entrada.leerEntero("1. SI.\n2. No. \n", "opcion", 1, 2)
        while (opcion == -1){
            opcion = entrada.leerEntero("1. SI.\n2. No \n", "opcion", 1, 2)
        }
        if (opcion == 2){
            println("Gracias por jugar con nosotros, vuelva pronto")
        }
        return opcion == 1
    }
}

class EntradaValidacion(){
    // Si el valor es correcto devuelve el valor, si es incorrecto, lanza el error y devuelve -1
    fun leerEntero(mensaje: String, tipo: String, min: Int, max: Int): Int {
        try {
            while (true) {
                print(mensaje)
                val valor = readlnOrNull()?.toIntOrNull()

                if (valor != null && valor in min..max) {
                    return valor
                }

                throw IllegalArgumentException("Error! numero de $tipo invalido. Debe estar entre $min y $max.")
            }
        } catch (e: IllegalArgumentException){
            println("Ocurrio un error: ${e.message}")
            return -1
        }

    }

}

fun main() {
    var nuevaPartida = true
    while (nuevaPartida) {
        val menu = Menu()
        val (filas, columnas, minas) = menu.parametrosConfiguracion()
        val tablero = Tablero(filas, columnas, minas)
        val juego = Buscaminas(tablero)


        menu.imprimirTablero(juego)
        menu.jugar(juego, filas, columnas)
        nuevaPartida = menu.volverAJugar()
    }


}