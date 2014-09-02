var tamano = 3;
var turno = "1";
var numJugadas = 0;
var finDelJuego = false;
var nombreJugador1 = "Jugador 1";
var nombreJugador2 = "Jugador 2";

function mostrarInicio() {
	document.getElementById("inicio").style.visibility = 'visible';
	document.getElementById("partida").style.visibility = 'hidden';
	document.getElementById("ayuda").style.visibility = 'hidden';
}

function mostrarPartida() {
	document.getElementById("inicio").style.visibility = 'hidden';
	document.getElementById("partida").style.visibility = 'visible';
	document.getElementById("ayuda").style.visibility = 'hidden';
}

function mostrarAyuda() {
	document.getElementById("inicio").style.visibility = 'hidden';
	document.getElementById("partida").style.visibility = 'hidden';
	document.getElementById("ayuda").style.visibility = 'visible';
}

function cambiaNombreJugador(numJugador, nombreJugador) {
	if (numJugador == "1") {
		nombreJugador1 = nombreJugador;
	} else {
		nombreJugador2 = nombreJugador;
	}
}

function iniciarJuego() {
	finDelJuego = false;
	numJugadas = 0;
	turno = "1";
	document.getElementById("turno").innerHTML = "Turno " + nombreJugador1;
	iniciarTablero();
	mostrarPartida();
}

function iniciarTablero() {
	if (document.documentElement.clientWidth < document.documentElement.clientHeight) {
		ancho = document.documentElement.clientWidth / 4;
	} else {
		ancho = document.documentElement.clientHeight / 4;
	}
	// ancho = 40;

	for ( var i = 0; i < tamano; i++) {
		for ( var j = 0; j < tamano; j++) {
			var cell = document.getElementById(i + "_" + j);
			cell.innerHTML = "";
			cell.style.width = ancho + "px";
			cell.style.height = ancho + "px";
			cell.value = " ";
			cell.className = "";
		}
	}
}

function click_celda(elemento) {
	var casilla = document.getElementById(elemento);
	if (finDelJuego) {
		jsInterfazNativa
				.mensaje("El juego ya ha terminado. Comienza uno nuevo!");
		return;
	}
	if (casilla.innerHTML != "") {
		jsInterfazNativa.mensaje("Casilla ocupada!");
		return;
	}
	numJugadas++;
	if (turno == "1") {
		casilla.className = "tdX";
		casilla.innerHTML = "X";
		casilla.style.fontSize = (ancho * 0.8) + "px";
		if (buscaGanador('X')) {
			document.getElementById("turno").innerHTML = "Fin del Juego: Gana "
					+ nombreJugador1 + "!!";
			jsInterfazNativa.mensaje("Fin del Juego: Gana " + nombreJugador1
					+ "!!");
			finDelJuego = true;
			return;
		}
		turno = "2";
		if (numJugadas < tamano * tamano) {
			document.getElementById("turno").innerHTML = "Turno "
					+ nombreJugador2;
		}
	} else {
		casilla.className = "tdO";
		casilla.innerHTML = "O";
		casilla.style.fontSize = (ancho * 0.8) + "px";
		if (buscaGanador('O')) {
			document.getElementById("turno").innerHTML = "Fin del Juego: Gana "
					+ nombreJugador2 + "!!";
			jsInterfazNativa.mensaje("Fin del Juego: Gana " + nombreJugador2
					+ "!!");
			finDelJuego = true;
			return;
		}
		turno = "1";
		document.getElementById("turno").innerHTML = "Turno " + nombreJugador1;
	}
	if (numJugadas >= tamano * tamano) {
		document.getElementById("turno").innerHTML = "Fin del Juego: EMPATE!!";
		jsInterfazNativa.mensaje("Fin del Juego: EMPATE!!");
		finDelJuego = true;
		return;
	}
}

function casilla(i, j) {
	return document.getElementById(i + '_' + j).innerHTML;
}

function buscaGanador(turno) {
	// verificamos diagonales
	if (casilla(0, 0) == turno && casilla(1, 1) == turno
			&& casilla(2, 2) == turno)
		return true;
	if (casilla(0, 2) == turno && casilla(1, 1) == turno
			&& casilla(2, 0) == turno)
		return true;
	for (n = 0; n < tamano; n++) {
		// verificamos columnas
		if (casilla(n, 0) == turno && casilla(n, 1) == turno
				&& casilla(n, 2) == turno)
			return true;
		// verificamos filas
		if (casilla(0, n) == turno && casilla(1, n) == turno
				&& casilla(2, n) == turno)
			return true;
	}
}
