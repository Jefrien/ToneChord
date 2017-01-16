$(".letra span").click(function(){
	getChord($(this).text());
});


function infoChord(){
    Android.infoChord();
}

var interval;
			function playinterval(var1){
			   if(var1==0){
					stopinterval();
			   } else {
				if(var1==1){
					stopinterval();
					interval = setInterval(function(){bajar(var1);},200);
				} else {
					if(var1==2){
						stopinterval();
						interval = setInterval(function(){bajar(var1);},200);
					} else {
						if(var1==3){
							stopinterval();
							interval = setInterval(function(){bajar(var1);},200);
						} else {
							if(var1==4){
								stopinterval();
								interval = setInterval(function(){bajar(var1);},200);
							}
						}
					}
				}
			  }
			}

			function stopinterval(){
			  clearInterval(interval);
			  return false;
			}

			function bajar(var1){
				var div = $('body');
				var pos = div.scrollTop();
				div.scrollTop(pos + var1);
			}


			var estadoToolOptions = 0;
			function autoChord(){
				if(estadoToolOptions==1){
					$(".toolbox_options").slideDown("fast");
					estadoToolOptions = 0;
				} else {
					$(".toolbox_options").slideUp();
					estadoToolOptions = 1;
				}
			}


			var estadoTool = 1;
			function eventToolbox(){
				if(estadoTool==1){
					$(".toolbox").slideDown();
					estadoTool=0;
				} else {
					$(".toolbox").slideUp();
					estadoTool=1;
					estadoToolOptions = 0;
					autoChord();
				}
			}

			var acordes=[];
			function getAcordeSubida(var1,var2){

				var ca = var1.charAt(0);
				var ca2 = var1.charAt(1);

				if(ca=="D" && ca2=="b"){
					var nuevo = var1.replace(/Db/gi,'D');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);

				} else if(ca=="E" && ca2=="b"){
					var nuevo = var1.replace(/Eb/gi,'E');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2=="b"){
					var nuevo = var1.replace(/Gb/gi,'G');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2=="b"){
					var nuevo = var1.replace(/Ab/gi,'A');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="B" && ca2=="b"){
					var nuevo = var1.replace(/Bb/gi,'B');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				}

				if(ca =="C" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/C/gi,'C#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="C" && ca2=="#"){
					var nuevo = var1.replace(/C#/gi,'D');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="D" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/D/gi,'D#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="D" && ca2=="#"){
					var nuevo = var1.replace(/D#/gi,'E');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="E"){
					var nuevo = var1.replace(/E/gi,'F');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="F" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/F/gi,'F#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="F" && ca2=="#"){
					var nuevo = var1.replace(/F#/gi,'G');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/G/gi,'G#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2=="#"){
					var nuevo = var1.replace(/G#/gi,'A');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/A/gi,'A#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2=="#"){
					var nuevo = var1.replace(/A#/gi,'B');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="B" && ca2!="b"){
					var nuevo = var1.replace(/B/gi,'C');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				}

			}

			function getAcordeBajada(var1,var2){
				var ca = var1.charAt(0);
				var ca2 = var1.charAt(1);

				if(ca=="D" && ca2=="b"){
					var nuevo = var1.replace(/Db/gi,'C');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="E" && ca2=="b"){
					var nuevo = var1.replace(/Eb/gi,'D');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2=="b"){
					var nuevo = var1.replace(/Gb/gi,'F');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2=="b"){
					var nuevo = var1.replace(/Ab/gi,'G');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="B" && ca2=="b"){
					var nuevo = var1.replace(/Bb/gi,'A');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				}

				if(ca =="C" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/C/gi,'B');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="C" && ca2=="#"){
					var nuevo = var1.replace(/C#/gi,'C');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="D" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/D/gi,'C#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="D" && ca2=="#"){
					var nuevo = var1.replace(/D#/gi,'D');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="E"){
					var nuevo = var1.replace(/E/gi,'D#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="F" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/F/gi,'E');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="F" && ca2=="#"){
					var nuevo = var1.replace(/F#/gi,'F');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/G/gi,'F#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="G" && ca2=="#"){
					var nuevo = var1.replace(/G#/gi,'G');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2!="#" && ca2!="b"){
					var nuevo = var1.replace(/A/gi,'G#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="A" && ca2=="#"){
					var nuevo = var1.replace(/A#/gi,'A');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				} else if(ca=="B" && ca2!="b"){
					var nuevo = var1.replace(/B/gi,'A#');
					acordes[acordes.length] = "{\"original\":\""+var1+"\",\"nuevo\":\""+nuevo+"\"}";
					var2.text(nuevo);
				}
			}

			function subirTono(){
				estadoToolOptions = 0;
				autoChord();
				acordes.length=0;
				$(".letra span").each(function (index)
				{
					var acorde = $(this).text();
					getAcordeSubida(acorde,$(this));
				});
				for(i=0; i<acordes.length;i++){
					var json = JSON.parse(acordes[i]);
					//console.log("ORIGINAL["+json.original+"], NUEVO["+json.nuevo+"]");
				}
			}

			function bajarTono(){
				estadoToolOptions = 0;
				autoChord();
				acordes.length=0;
				$(".letra span").each(function (index)
				{
					var acorde = $(this).text();
					getAcordeBajada(acorde,$(this));
				});
				for(i=0; i<acordes.length;i++){
					var json = JSON.parse(acordes[i]);
					//console.log("ORIGINAL["+json.original+"], NUEVO["+json.nuevo+"]");
				}
			}