// 1. Crea una función que cuente el número de vocales de una cadena de caracteres.
    function numeroCaractCadena(cadena) {
        return cadena.length
    }

    let cadenaCaract = "Supercalifragilisticoespialidoso"
    console.log(numeroCaractCadena(cadenaCaract))
// 2. Crea una función que determine si una cadena es un palíndromo, es decir, que se lee igual hacia
// delante que hacia atrás.
    function isPalindromo(cadena) { 
        cadenaInvert = cadena.split("").reverse().join("")
        if (cadena === cadenaInvert) {
            return cadenaInvert
        } else {
            return "No es palindromo"
        }
    }    

    let cadenaPalindr = "radar"
    console.log(isPalindromo(cadenaPalindr))
// 3. Crea una función que capitalice una cadena de texto. Es decir que todas las palabras empiecen
// por mayúscula.
    function primerCaracterMayus(cadena) {
        cadena
    }

    let cadenaPrimerMayus = "si quieres la paz, preparate para la guerra"
    let cadenaMa = cadenaPrimerMayus.split(" ")
    let cadenaMaYus = cadenaMa.map(x=>x.charAt(0).toUpperCase() + x.slice(1))
    
    console.log(cadenaMaYus.join(" "))
// 4. Dado un array de cadenas y una longitud n, crea una función que filtre el array dejando solo las
// cadenas de menor longitud que n.

// 5. Crea una función que cree el acrónimo de una cadena de caracteres tomando la primera letra de
// cada palabra y convirtiéndola a mayúscula. Por ejemplo la frase anterior sería CUFQCEADU….

// 6. Crea una función que cuente las frases, palabras y letras presentes en un texto.

// 7. Crea una función que identifique si hay elementos duplicados en un array.

// 8. Crea una función que debe retornar verdadero si alguno de los elementos de un array está re‑
// petido n veces.

// 9. Crea un array que intercale dos arrays dados. Por ejemplo dados [a,b,c,d] y [1,2,3,4] el resultado
// sería [a,1,b,2,c,3,d,4]

// 10. Crea una función que rote los elementos de un array n posiciones. Por ejemplo, dado el array
// [1,2,3,4,5,6] y el número 2 el resultado será: [5,6,1,2,3,4]

// 11. Crea una función que elimine de una cadena los caracteres dados en un array.

// 12. Crea una función que rote una matriz de tamaño nxn, 90 grados a la derecha. Ejemplo: [1,2,3]
// [7, 4, 1] [4,5,6] => [8, 5, 2] [7,8,9] [9, 6, 3]

// 13. Crea una función que determine si los paréntesis presentes en una cadena de texto están balan‑
// ceados. Por ejemplo (a(b)) → Balanceado, (a(b(a)) → No balanceado.

// 14. Busca una submatriz dentro de una matriz más grande. El resultado debe ser las coordenadas
// donde se encuentra dicha matriz.

// 15. Crea una función que verifique si una matriz de 9x9 es una solución de un sudoku. Una cuadrí‑
// cula válida de Sudoku es aquella que cumple las siguientes condiciones:
// a. Filas Únicas: Cada fila debe contener los números del 1 al 9 sin repetición.
// b. Columnas Únicas: Cada columna debe contener los números del 1 al 9 sin repetición.
// c. Subcuadrículas Únicas: Cada una de las nueve subcuadrículas de 3x3 debe contener los
// números del 1 al 9 sin repetición.