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
    var data = eval("("+event.data.trim()+")");
    switch (data.type){
        case "roomInfo":
           mySession_ID=data.mySession_ID;
            break;
        case "tank_Position":
            if(data.tankID!=mySession_ID){
                tank_Position.R=data.R;
                tank_Position.X=data.X;
                tank_Position.Y=data.Y;
                tank_Position.F=data.F;
            }

            break;
    }
}
function onClose(event){

}
function onError(event) {

}
