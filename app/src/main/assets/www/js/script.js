var aire = 25;

// ---------------------- Acorde DO
	var doMayor = ["1,2:2,4:3,5:0,0:0,0:0,0:1","1,7:3,2:3,3:3,4:3","1,2:2,4:3,5:3,6:1","1,7:2,3:3,4:3,5:8","1,2:2,4:1","1,7:3,5:4,1:4,6:5"];
	var doSostenido = "1,1:2,2:1,3:3,4:0,0,0,0:1";
	var doSostenidoAdd9 = "1,1:1,2:3,3:3,4:1,5:0,0:4";
	var doSostenido4 = "1,1:4,2:3,3:3,4:0,0:0,0:4";
	var doSostenido7 = "4,1:2,2:4,3:3,4:0,0:0,0:1";
	var doSostenidoMenor = "1,1:2,2:3,3:3,4:1,5:0,0:4";
	var doSostenidoMaj = "1,1:1,2:1,3:3,4:4,5:0,0:1";
	var doAdd9 = "0,0:3,2:0,0:2,4:3,5:0,0:1";
	var do_si = "0,0:1,2:0,0:2,4:2,5:0,0:1";
	var do11 = "1,1:4,2:1,3:3,4:1,5:0,0:3";
	var do4 = "3,1:1,2:0,0:3,4:0,0:0,0:1"
	var do7 = "0,0:1,2:3,3:2,4:3,5:0,0:1";
	var do9 = "3,1:1,2:2,3:1,4:3,5:1,6:8";
	var do911 = "1,1:3,2:1,3:3,4:1,5:1,6:3";
	var doAdd2_si = "0,0:1,2:0,0:0,0:2,5:0,0:1";
	var doMenor = ["1,1:2,2:3,3:3,4:1,5:0,0:3","1,2:1,4:3,5:1","1,7:2,2:3,3:3,4:3","1,7:3,4:3,5:8"];
	var doMenor11 = "0,0:4,2:1,3:3,4:1,5:0,0:3";
	var doMenor7 = "1,1:2,2:1,3:3,4:1,5:0,0:3";
	var doMaj = "0,0:1,2:0,0:2,4:3,5:0,0:1";
	var doMaj7 = "0,0:0,0:0,0:2,4:3,5:0,0:1";
	var doSus2 = "0,0:1,2:0,0:0,0:3,4:0,0:1";
	var doSus9 = "4,1:2,2:1,3:4,4:0,0:0,0:7";
	var do6 =    "1,2:2,3:2,4:3,5:0,0:0,0:1";
    var doMaj9 = "0,0:2,4:3,2:3,5:0,0:0,0:1";
    var doDim =  "1,2:1,4:2,1:2,3:0,0:0,0:1";
    var doAug =  "1,2:1,3:2,4:3,5:0,0:0,0:1";
    var do7menos5 = "1,2:2,4:2,6:3,3:1";
	var do7mas5 = "1,2:2,4:3,3:1";
	var do7menos9 = "2,2:2,4:3,3:3,5:1";
	var do7mas9 = "2,4:3,3:3,5:4,2:1";
	var do7asusmenos4 = "1,2:3,3:3,4:3,6:1";
	var doDim9 = "1,7:2,2:2,3:2,6:1";
	var do9menos5 = "2,4:2,6:3,2:3,3:1";
	var do13menos9 = "3,3:3,6:4,5:1";
	var doMenor6 = "1,2:1,4:3,1:1";
	var doAug11 = "2,7:3,2:3,6:4,3:1";

	// ---------------------- Acordes C# - Db
	var reBemol = "1,3:2,2:3,4:4,5:1";
	var reBemolMenor = "1,3:2,2:2,4:4,6:1";
	var reBemol7 = "1,7:3,2:3,4:4";
	var reBemol6 = "3,3:3,4:4,5:4,6:1";
	var reBemolMenor6 = "1,3:1,5;2,2:2,4:1";
	var reBemolMaj7 = "1,7:3,4:4,5:1";
	var reBemolMenor7 = "1,7:2,2:3,4:4";
	var reBemol9 = "2,4:3,2:3,3:3,5:1";
	var reBemolMaj9 = "1,4:2,2:2,5:3,3:3";
	var reBemolDim = "2,3:3,5:4,2:4,4:1";
	var reBemol7sus4 = "2,2:4,3:4,4:4,6:1";
	var reBemol7menos5 = "2,2:3,1:3,4:4,3:1";
	var reBemol7mas5 = "1,2:2,4:3,3:4,1:2";
	var reBemol7menos9 = "3,2:3,4:4,3:4,5:1";
	var reBemol7mas9 = "1,4:2,3:2,5:3,2:3";
	var reBemol9menos5 = "3,4:3,6:4,2:4,3:1";
	var reBemol11mas5 = "1,2:1,3:1,6:2,4:4";
	var reBemo9mas5 = "1,4:2,2:2,3:3,6:2";
	var reBemol11 = "4,2:4,3:4,4:4,6:1";
	var reBemol13 = "3,4:4,2:4,3:4,6:1";
	var reBemolMenor9menos5 = "2,4:3,6:4,2:4,3:1";
	var reBemolDimAad9 = "2,4:3,3:3,6:4,2:1";
	var reBemol7Aad6 = "1,3:1,5:3,1:3,2:4";

 function mostrar(){
		$("#chordModal").fadeIn();
		img_canvas("c");

	};

function getAcorde(valor){
	var retorno = "";



	// ---------------------- Ternarios para DO
//	((valor == "c#") ? retorno = doSostenido : false);
	((valor == "c") ? retorno = doMayor : false);
	((valor == "c#add9") ? retorno = doSostenidoAdd9 : false);
	((valor == "c#4") ? retorno = doSostenido4 : false);
//	((valor == "c#7") ? retorno = doSostenido7 : false);
//	((valor == "c#m") ? retorno = doSostenidoMenor : false);
//	((valor == "c#maj") ? retorno = doSostenidoMaj : false);
	((valor == "cadd9") ? retorno = doAdd9 : false);
	((valor == "c/b") ? retorno = do_si : false);
	((valor == "c11") ? retorno = do11 : false);
	((valor == "c4") ? retorno = do4 : false);
	((valor == "c7") ? retorno = do7 : false);
	((valor == "c9") ? retorno = do9 : false);
	((valor == "c911") ? retorno = do911 : false);
	((valor == "cadd2/b") ? retorno = doAdd2_si : false);
	((valor == "cm") ? retorno = doMenor : false);
	((valor == "cm11") ? retorno = doMenor11 : false);
	((valor == "cm7") ? retorno = doMenor7 : false);
	((valor == "cmaj") ? retorno = doSostenidoMenor : false);
	((valor == "cmaj7") ? retorno = doMaj7 : false);
	((valor == "csus2") ? retorno = doSus2 : false);
	((valor == "csus9") ? retorno = doSus9 : false);
	((valor == "c7-5") ? retorno = do7menos5 : false);
	((valor == "c6") ? retorno = do6 : false);
	((valor == "cmaj9") ? retorno = doMaj9 : false);
	((valor == "cdim") ? retorno = doDim : false);
	((valor == "caug") ? retorno = doAug : false);
	((valor == "c7+5") ? retorno = do7mas5 : false);
	((valor == "c7-9") ? retorno = do7menos9 : false);
	((valor == "c7+9") ? retorno = do7mas9 : false);
	((valor == "c7asus-4") ? retorno = do7asusmenos4 : false);
	((valor == "cdim9") ? retorno = doDim9 : false);
	((valor == "c9-5") ? retorno = do9menos5 : false);
	((valor == "c13-9") ? retorno = do13menos9 : false);
	((valor == "cm6") ? retorno = doMenor6 : false);
	((valor == "caug11") ? retorno = doAug11 : false);



	// ---------------------- Ternarios para C# - Db
	((valor == "db" || valor == "c#") ? retorno = reBemol : false);
	((valor == "dbm" || valor == "c#m") ? retorno = reBemolMenor : false);
	((valor == "db7" || valor == "c#7") ? retorno = reBemol7 : false);
	((valor == "db6" || valor == "c#6") ? retorno = reBemol6 : false);
	((valor == "dbm6" || valor == "c#m6") ? retorno = reBemolMenor6 : false);
	((valor == "dbmaj7" || valor == "c#maj7") ? retorno = reBemolMaj7 : false);
	((valor == "dbm7" || valor == "c#m7") ? retorno = reBemolMenor7 : false);
	((valor == "db9" || valor == "c#9") ? retorno = reBemol9 : false);
	((valor == "dbmaj9" || valor == "c#maj9") ? retorno = reBemolMaj9 : false);
	((valor == "dbdim" || valor == "c#dim") ? retorno = reBemolDim : false);
	((valor == "db7sus4" || valor == "c#7sus4") ? retorno = reBemol7sus4 : false);
	((valor == "db7-5" || valor == "c#7-5") ? retorno = reBemol7menos5 : false);
	((valor == "db7+5" || valor == "c#7+5") ? retorno = reBemol7mas5 : false);
	((valor == "db7-9" || valor == "c#7-9") ? retorno = reBemol7menos9 : false);
	((valor == "db7+9" || valor == "c#7+9") ? retorno = reBemol7mas9 : false);
	((valor == "db9-5" || valor == "c#9-5") ? retorno = reBemol9menos5 : false);
	((valor == "db11+5" || valor == "c#11+5") ? retorno = reBemol11mas5 : false);
	((valor == "db9+5" || valor == "c#9+5") ? retorno = reBemo9mas5 : false);
	((valor == "db11" || valor == "c#11") ? retorno = reBemol11 : false);
	((valor == "db13" || valor == "c#13") ? retorno = reBemol13 : false);
	((valor == "dbm9-5" || valor == "c#m9-5") ? retorno = reBemolMenor9menos5 : false);
	((valor == "dbdimaad9" || valor == "c#dimaad9") ? retorno = reBemolDimAad9 : false);
	((valor == "db7aad6" || valor == "c#7aad6") ? retorno = reBemol7Aad6 : false);

	// ---------------------- Acorde RE
	var re = "2,1:3,2:2,3:0,0:0,0:0,0:1";
	var reMenor = "1,1:2,3:3,2:1";
	var re7 = "1,2:2,1:2,3:1";
	var re6 = "2,1:2,3:1";
	var reMenor6 = "2,3:2,5:3,2:3,4:1";
	var reMaj7 = "1,7:2,3:3,2:3,4:5";
	var reMenor7 = "1,1:1,2:2,3:1";
	var re9 = "1,4:2,2:2,3:2,5:4";
	var reMaj9 = "2,2:2,3:2,6:1";
	var reDim = "3,2:3,4:4,1:4,3:1";
	var reAug = "2,1:3,2:3,3:1";
	var reAug11 = "1,2:1,3:1,6:2,4:5";
	var reDim9 = "1,4:2,3:2,6:3,2:3";
	var re7sus4 = "1,2:3,3:3,4:3,6:3";
	var re7menos5 = "1,2:2,4:2,6:3,3:3";
	var re7mas5 = "3,7:4,4:1";
	var re7menos9 = "1,2:1,4:2,3:2,5:4";
	var re7mas9 = "1,4:2,3:2,5:3,2:4";
	var re9menos5 = "1,4:1,6:2,2:2,3:4";
	var re9mas5 = "1,4:2,2:2,3:3,6:4";
	var re13menos9 = "1,3:1,6:2,5:3,1:3,2:5";
	var reMenor7menos5 = "1,3:3,2:3,4:3,5:1";
	var re11 = "1,2:2,4:1";
	var re13 = "1,4:2,2:2,3:4,1:4";
	var reMenor9menos5 = "1,4:2,6:3,2:3,3:3";
	var re13sus11 = "1,7:3,1:5";
	var re11mas5 = "1,7:2,2:2,3:2,6:4";
	var re13add11 = "1,7:2,2:2,6:3,3:6";


	// ---------------------- Ternarios para RE
	((valor == "d") ? retorno = re : false);
	((valor == "dm") ? retorno = reMenor : false);
	((valor == "d7") ? retorno = re7 : false);
	((valor == "d6") ? retorno = re6 : false);
	((valor == "dm6") ? retorno = reMenor6 : false);
	((valor == "dmaj7") ? retorno = reMaj7 : false);
	((valor == "dm7") ? retorno = reMenor7 : false);
	((valor == "d9") ? retorno = re9 : false);
	((valor == "dmaj9") ? retorno = reMaj9 : false);
	((valor == "ddim") ? retorno = reDim : false);
	((valor == "daug") ? retorno = reAug : false);
	((valor == "daug11") ? retorno = reAug11 : false);
	((valor == "ddim9") ? retorno = reDim9 : false);
	((valor == "d7sus4") ? retorno = re7sus4 : false);
	((valor == "d7-5") ? retorno = re7menos5 : false);
	((valor == "d7+5") ? retorno = re7mas5 : false);
	((valor == "d7-9") ? retorno = re9menos5 : false);
	((valor == "d7+9") ? retorno = re9mas9 : false);
	((valor == "d9-5") ? retorno = re9menos5 : false);
	((valor == "d9+5") ? retorno = re9mas5 : false);
	((valor == "d13-9") ? retorno = re13menos9 : false);
	((valor == "dm7-5") ? retorno = reMenor7menos5 : false);
	((valor == "d11") ? retorno = re11 : false);
	((valor == "d13") ? retorno = re13 : false);
	((valor == "dm9-5") ? retorno = reMenor9menos5 : false);
	((valor == "d13sus11") ? retorno = re13sus11 : false);
	((valor == "d11+5") ? retorno = re11mas5 : false);
	((valor == "d13add11") ? retorno = re13add11 : false);

	// ---------------------- Acorde RE# - Eb
	var eb = "1,7:2,2:3,4:3,5:3";
	var ebm = "2,6:3,3:4,2:4,4:1";
	var eb7 = "1,2:2,4:3,3:3,5:4";
	var eb6 = "1,2:2,3:2,4:3,6:4";
	var ebm6 = "1,2:1,3:1,4:2,1:3,3:1";
	var ebmaj7 = "1,7:2,3:3,2:3,4:6";
	var ebm7 = "1,7:2,2:3,4:6";
	var eb9 = "1,4:2,2:2,3:2,5:5";
	var ebm9 = "2,2:2,6:3,3:3,4:1";
	var ebdimadd9 = "1,4:2,3:2,6:3,2:4";
	var ebdim = "1,2:1,4:2,3:2,6:4";
	var ebaug = "1,4:3,1:4,2:4,3:1";
	var eb7menos5 = "1,4:2,2:2,3:3,6:1";
	var eb7mas5 = "1,4:2,2:3,1:4,3:1";
	var eb7sus4 = "1,2:3,3:3,4:3,6:4";
	var eb7menos9 = "2,2:2,4:3,3:3,6:1";
	var eb7mas9 = "1,4:2,3:2,6:3,2:6";
	var eb9menos5 = "1,1:1,4:2,2:1";
	var eb9mas5 = "1,1:1,4:2,2:2,5:1";
	var eb11 = "1,2:1,3:1,4:1,6:6";
	var eb13 = "1,4:2,2:2,3:2,6:4,1:1";
	var eb11mas5 = "1,2:1,3:1,6:2,4:6";
	var ebm9menos5 = "2,2:2,3:2,6:3,4:1";
	var eb13menos9 = "1,2:1,3:1,4:2,3:2,6:5";
	var eb7add6 = "1,3:1,5:2,1:2,2:1";
	var ebaug11 = "1,7:2,2:2,3:3,1:1";
	var eb13sus11 = "1,7:3,1:1";
	var ebm7menos5 = "1,4:2,2:2,3:2,6:1";

	// ---------------------- Ternarios para RE# - Eb
	((valor == "eb" || valor == "d#") ? retorno = eb : false);
	((valor == "ebm" || valor == "d#m") ? retorno = ebm : false);
	((valor == "eb7" || valor == "d#7") ? retorno = eb7 : false);
	((valor == "eb6" || valor == "d#6") ? retorno = eb6 : false);
	((valor == "ebm6" || valor == "d#m6") ? retorno = ebm6 : false);
	((valor == "ebmaj7" || valor == "d#maj7") ? retorno = ebmaj7 : false);
	((valor == "ebm7" || valor == "d#m7") ? retorno = ebm7 : false);
	((valor == "eb9" || valor == "d#9") ? retorno = eb9 : false);
	((valor == "ebm9" || valor == "d#m9") ? retorno = ebm9 : false);
	((valor == "ebdimadd9" || valor == "d#dimaad9") ? retorno = ebdimadd9 : false);
	((valor == "ebdim" || valor == "d#dim") ? retorno = ebdim : false);
	((valor == "ebaug" || valor == "d#aug") ? retorno = ebaug : false);
	((valor == "eb7-5" || valor == "d#7-5") ? retorno = eb7menos5 : false);
	((valor == "eb7+5" || valor == "d#7+5") ? retorno = eb7mas5 : false);
	((valor == "eb7sus4" || valor == "d#7sus4") ? retorno = eb7sus4 : false);
	((valor == "eb7-9" || valor == "d#7-9") ? retorno = eb7menos9 : false);
	((valor == "eb7+9" || valor == "d#7+9") ? retorno = eb7mas9 : false);
	((valor == "eb9-5" || valor == "d#9-5") ? retorno = eb9menos5 : false);
	((valor == "eb9+5" || valor == "d#9+5") ? retorno = eb9mas5 : false);
	((valor == "eb11" || valor == "d#11") ? retorno = eb11 : false);
	((valor == "eb13" || valor == "d#13") ? retorno = eb13 : false);
	((valor == "eb11+5" || valor == "d#11+5") ? retorno = eb11mas5 : false);
	((valor == "ebm9-5" || valor == "d#m9-5") ? retorno = ebm9menos5 : false);
	((valor == "eb13-9" || valor == "d#13-9") ? retorno = eb13menos9 : false);
	((valor == "eb7add6" || valor == "d#7aad6") ? retorno = eb7add6 : false);
	((valor == "ebaug11" || valor == "d#aug11") ? retorno = ebaug11 : false);
	((valor == "eb13sus11" || valor == "d#13sus11") ? retorno = eb13sus11 : false);
	((valor == "ebm7-5" || valor == "d#m7-5") ? retorno = ebm7menos5 : false);

	return retorno;
}

function getDo(){

}

// Write JavaScript here
function img_canvas(canvas,acorde) {
  var colorFondo = "#ECEBBF";
  //recojemos el canvas poniendo la id del canvas html5 para relacionarlo

  //Cojemos la 2D para dibujar en él
 var context = canvas.getContext("2d");
  //creamos la nueva imagen
	crear_puente(context,colorFondo);

  leer_acorde(context, getAcorde(acorde));


}

function leer_acorde(context,str){
  var array = str.split(":");
  for(var i=0; i<array.length; i++){
    crear_circulo(context, array[i]);
  }
}


function crear_circulo(context,c,t){
  var circulo = document.getElementById("circuloCanvas");
  var tamanoCirculo = 15;
  var tamanoCirculoPequeno = 20;
  var cuerdas = c.split(",");
  var traste = document.getElementById("numeroTrasteCanvas");

  var cuerda1 = 0;
  var cuerda2 = 15;
  var cuerda3 = 35;
  var cuerda4 = 50;
  var cuerda5 = 70;
  var cuerda6 = 85;

  var traste1 = 30;
  var traste2 = 75;
  var traste3 = 120;
  var traste4 = 165;

  //traste.innerText = c;
  if(c==1 || c==2 || c==3 || c==4 || c==5 || c==6 || c==7 ||  c==8 ||  c==9 ||  c==10 ||  c==11 ||  c==12 || c==13 || c==14 ||
	c==15 || c==16 || c==17 || c==18 || c==19 || c==20 || c==21 || c==22 || c==23 || c==24){
	context.font = "15px Verdana";
	context.fillStyle="black"
	context.fillText(c,35,115);
	}

  var x,y;

  switch(cuerdas[0]){
    case "0":
      x = aire;
      break;
    case "1":
      x = traste1;
      break;
    case "2":
      x = traste2;
      break;
    case "3":
      x = traste3;
      break;
    case "4":
      x = traste4;
      break;
  }

  switch(cuerdas[1]){
    case "1":
      y = cuerda1;
      break;
    case "2":
      y = cuerda2;
      break;
    case "3":
      y = cuerda3;
      break;
    case "4":
      y = cuerda4;
      break;
    case "5":
      y = cuerda5;
      break;
    case "6":
      y = cuerda6;
      break;
    case "7":
      y = "s";
      break;
  }

    if(y!="s"){
        context.drawImage(circulo, x, y,tamanoCirculo,tamanoCirculo);
    } else {
        context.beginPath();
        context.moveTo(x+5, 3);
        context.fillStyle="blue";
        context.lineWidth = 7;
        context.strokeStyle="blue";
        context.lineTo(x+5, 10);
        context.lineTo(x+5, 97);
        context.closePath();
        context.stroke();
        context.fill();
    }


}


function crear_puente(context,fondo){
  var bg = document.getElementById("fondoCanvas");
  context.drawImage(bg, 0, 0,200,100);
  context.fillStyle="black";
  context.fillRect(1, 5, 200, 3);
  context.fillRect(1, 22, 200, 3);
  context.fillRect(1, 40, 200, 3);
  context.fillRect(1, 57, 200, 3);
  context.fillRect(1, 75, 200, 3);
  context.fillRect(1, 92, 200, 3);

  context.beginPath();
  context.moveTo(20, 3);
  context.fillStyle="black";
  context.lineWidth = 7;
  context.strokeStyle="black";
  context.lineTo(20, 10);
  context.lineTo(20, 97);
  context.closePath();
  context.stroke();
  context.fill();

  context.beginPath();
  context.moveTo(199, 3);
  context.fillStyle="black";
  context.lineWidth = 2;
  context.strokeStyle="black";
  context.lineTo(199, 97);
  context.closePath();
  context.stroke();
  context.fill();

  // Trastes
  // #1
  context.beginPath();
  context.moveTo(60, 3);
  context.fillStyle="black";
  context.lineWidth = 2;
  context.strokeStyle="black";
  context.lineTo(60, 97);
  context.lineTo(60, 97);
  context.closePath();
  context.stroke();
  context.fill();
  // #2
  context.beginPath();
  context.moveTo(105, 3);
  context.fillStyle="black";
  context.lineWidth = 2;
  context.strokeStyle="black";
  context.lineTo(105, 97);
  context.lineTo(105, 97);
  context.closePath();
  context.stroke();
  context.fill();
  // #3
  context.beginPath();
  context.moveTo(150, 3);
  context.fillStyle="black";
  context.lineWidth = 2;
  context.strokeStyle="black";
  context.lineTo(150, 97);
  context.lineTo(150, 97);
  context.closePath();
  context.stroke();
  context.fill();
}

function getUrlVars() {
var vars = {};
var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
vars[key] = value;
});
return vars;
}

function lanzar(){

	var first = getUrlVars()["a"];

add(first);

}

 var mySwiper = new Swiper ('.swiper-container', {
    // Optional parameters
    direction: 'horizontal',

    // If we need pagination
    pagination: '.swiper-pagination'

  })


  function add(acorde){
		var acordeArr = getAcorde(acorde);
		for(i=0;i<acordeArr.length;i++){
			mySwiper.appendSlide('<div class="swiper-slide"><canvas id="img'+i+'" width="200" height="120"></canvas></div>');
			var canvas = document.getElementById("img"+i);
			var colorFondo = "#ECEBBF";
			var context = canvas.getContext("2d");
			crear_puente(context,colorFondo);
			leer_acorde(context, acordeArr[i]);
		}

  }

window.onload = lanzar();
