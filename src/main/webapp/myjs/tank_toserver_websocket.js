/**
 * Created by xiaoshan on 2017/4/25.
 */
//var path = 'ws://' + window.location.host + "/" + window.location.pathname.split("/")[1];
var path="ws://localhost/Tank_Server";
var webSocket = new WebSocket(path+'/server');
webSocket.onerror = function(event) {onError(event)};
webSocket.onopen = function(event) {onOpen(event);};
webSocket.onmessage = function(event) {onMessage(event)};
webSocket.onerror=function(event){onError()};

function onOpen(event) {

}

function onMessage(event){

}
function onClose(event){

}
function onError(event) {

}
