/**
 * Created by xiaoshan on 2017/4/25.
 */
var path = 'ws://' + window.location.host + "/" + window.location.pathname.split("/")[1];
//var path="ws://localhost/Tank_Server";
var webSocket = new WebSocket(path+'/server');
webSocket.onerror = function(event) {onError(event)};
webSocket.onopen = function(event) {onOpen(event);};
webSocket.onmessage = function(event) {onMessage(event)};
webSocket.onerror=function(event){onError()};

function onOpen(event) {

}

function onMessage(event){
	var data = JSON.parse(event.data);
    switch (data.type){
        case "roomInfo":
           mySession_ID=data.mySession_ID;
           tank_Position.tankID=data.mySession_ID;
            break;
        case "tanks_info":
            try{
            	tanks= JSON.parse(data.value);
            	console.log(tanks[0].X);
            }catch (err){
            	alert(err);
            }
            break;
        case "balls_info":
            try{
            	balls= JSON.parse(data.value);
            	console.log(balls.length);
            }catch (err){
            	alert(err);
            }
            break;
    }
}
function onClose(event){

}
function onError(event) {

}
