/**
 * Created by xiaoshan on 2017/4/17.
 */
var tankR=0;//控制坦克方向的变量
function StartGame(){
    window.addEventListener("keydown",eventKeyDown,true);
    window.addEventListener("keyup",eventKeyUp,true);

    setInterval(drawScreen,50);
    setInterval(checkKeyStau,20);
    function drawScreen() {
        //画出地图
        context.clearRect(0,0,theCanvas.width,theCanvas.height);
        for (var rowCtr = 0; rowCtr < mapRows; rowCtr++) {
            for (var colCtr=0; colCtr < mapCols; colCtr++) {
                var tileId = tileMap[rowCtr][colCtr];
                var sourceX = Math.ceil(tileId % 23 ) * 32 +Math.ceil(tileId % 23 );
                var sourceY = Math.ceil(tileId / 23) * 32 +Math.ceil(tileId / 23 );
                context.drawImage(tanks_Material, sourceX, sourceY, 32, 32, colCtr * 32, rowCtr * 32, 32, 32);
            }
        }
        switch (tankR){ //绘制坦克主体
            case 0 : context.drawImage(tanks_Material, tank_toLeft[tank_cur_frame][0], tank_toLeft[tank_cur_frame][1],
                                        32, 32,tank_position_x,tank_position_y, 32, 32);
                break;
            case 1 : context.drawImage(tanks_Material, tank_toTop[tank_cur_frame][0], tank_toTop[tank_cur_frame][1],
                                        32, 32,tank_position_x,tank_position_y, 32, 32);
                break;
            case 2 : context.drawImage(tanks_Material, tank_toRight[tank_cur_frame][0], tank_toRight[tank_cur_frame][1],
                                        32, 32,tank_position_x,tank_position_y, 32, 32);
                break;
            case 3 : context.drawImage(tanks_Material, tank_toButtom[tank_cur_frame][0], tank_toButtom[tank_cur_frame][1],
                                        32, 32,tank_position_x,tank_position_y, 32, 32);
        }
        //绘制子弹
        context.save();
        for(var i=0;i<balls.length;i++){
            switch (balls[i].direction){
                case 0:
                    balls[i].p_x-=5;
                    break;
                case 1:
                    balls[i].p_y-=5;
                    break;
                case 2:
                    balls[i].p_x+=5;
                    break;
                case 3:
                    balls[i].p_y+=5;
                    break;
            }
            if(checkBallStau(i,balls[i]) == true){
                context.fillStyle="gold";
                context.beginPath();
                context.arc(balls[i].p_x,balls[i].p_y,4,0,Math.PI*2,true);
                context.closePath();
                context.fill();
            }
        }
        context.restore();
    }

    function eventKeyDown(e){
        var ecode=e.keyCode;
        if(ecode == 37 || ecode == 38 || ecode == 39 || ecode == 40){
            var keyStatus_p=ecode-37;//如果等于0代表按下左键
            for(var i=0;i<keyStatus.length-1;i++){
                keyStatus[i]=0;
            }
            keyStatus[keyStatus_p]=1;
        }else if(ecode == 32 ){
            keyStatus[4]=1;
        }

    }
    function eventKeyUp(e){
        tank_cur_frame=2;
        var ecode=e.keyCode;
        if(ecode == 37 || ecode == 38 || ecode == 39 || ecode == 40 ){
            var keyStatus_p=ecode-37;
            keyStatus[keyStatus_p]=0;
        }else if(ecode == 32 ){
            keyStatus[4]=0;
        }
    }
    function addball(){
        var ball={};
        ball.direction=tankR;
        switch (ball.direction){
            case 0 :
                ball.p_x=tank_position_x;
                ball.p_y=tank_position_y+16;
                break;
            case 1 :
                ball.p_x=tank_position_x+16;
                ball.p_y=tank_position_y;
                break;
            case 2 :
                ball.p_x=tank_position_x+32;
                ball.p_y=tank_position_y+16;
                break;
            case 3 :
                ball.p_x=tank_position_x+16;
                ball.p_y=tank_position_y+32;
                break;
        }
        balls.push(ball);
    }
    function checkKeyStau(){//检测状态并刷新
        if(keyStatus[4]==1){
            addball();
            keyStatus[4]=0;
        }
        if(keyStatus[0]==1){//向左
            if(tankR!=0){
                document.getElementById("xy").innerHTML="";
                tankR=0;
                tank_cur_frame=2;
            }else{
                if(tank_cur_frame<9){
                    tank_cur_frame++;
                }else{
                    tank_cur_frame=2;
                }
                if(tank_position_x>0){//判断坦克是否超越左边界,未越界检查前方是否有障碍物
                    var col=Math.ceil((tank_position_x)/32)-1;
                    var row1=Math.floor(tank_position_y/32);
                    var row2=Math.floor((tank_position_y+31)/32);
                    document.getElementById("xy").innerHTML="";
                    document.getElementById("xy").innerHTML="col:"+col+";"+"row1:"+row1+"row2:"+row2;
                    document.getElementById("vv").innerHTML="<br>"+tileMap[row1][col]+" "+tileMap[row2][col];
                    if(tileMap[row1][col] == 0 && tileMap[row2][col] ==0)
                        tank_position_x-=1;
                }

            }
        }else if(keyStatus[1]==1){//向上
            if(tankR!=1){
                tankR=1;
                tank_cur_frame=2;
            }else{
                if(tank_cur_frame<9){
                    tank_cur_frame++;
                }else{
                    tank_cur_frame=2;
                }
                if(tank_position_y>0){//判断坦克是否超越边界
                    var row=Math.ceil((tank_position_y)/32)-1;
                    var col1=Math.floor(tank_position_x/32);
                    var col2=Math.floor((tank_position_x+31)/32);
                    document.getElementById("xy").innerHTML="";
                    document.getElementById("xy").innerHTML="row:"+row+";"+"col1:"+col1+"col2:"+col2;
                    document.getElementById("vv").innerHTML="<br>"+tileMap[row][col1]+" "+tileMap[row][col2];
                    if(tileMap[row][col1] == 0 && tileMap[row][col2] ==0)
                        tank_position_y-=1;
                }
            }
        }else if(keyStatus[2]==1){//向右
            if(tankR!=2){
                tankR=2;
                tank_cur_frame=2;
            }else{
                if(tank_cur_frame<9){
                    tank_cur_frame++;
                }else{
                    tank_cur_frame=2;
                }
                if(tank_position_x<theCanvas.width-32){//判断坦克是否超越边界
                    var col=Math.ceil((tank_position_x+33)/32)-1;
                    var row1=Math.floor(tank_position_y/32);
                    var row2=Math.floor((tank_position_y+31)/32);
                    document.getElementById("xy").innerHTML="";
                    document.getElementById("xy").innerHTML="col:"+col+";"+"row1:"+row1+"row2:"+row2;
                    document.getElementById("vv").innerHTML="<br>"+tileMap[row1][col]+" "+tileMap[row2][col];
                    if(tileMap[row1][col] == 0 && tileMap[row2][col] ==0)
                        tank_position_x+=1;
                }
            }
        }else if(keyStatus[3]==1){ //向下
            if(tankR!=3){
                tankR=3;
                tank_cur_frame=2;
            }else{
                if(tank_cur_frame<9){
                    tank_cur_frame++;
                }else{
                    tank_cur_frame=2;
                }
            }
            if(tank_position_y<theCanvas.height-32){//判断坦克是否超越下边界
                var row=Math.ceil((tank_position_y+33)/32)-1;
                var col1=Math.floor(tank_position_x/32);
                var col2=Math.floor((tank_position_x+31)/32);
                document.getElementById("xy").innerHTML="";
                document.getElementById("xy").innerHTML="row:"+row+";"+"col1:"+col1+"col2:"+col2;
                document.getElementById("vv").innerHTML="<br>"+tileMap[row][col1]+" "+tileMap[row][col2];
                if(tileMap[row][col1] == 0 && tileMap[row][col2] ==0)
                    tank_position_y+=1;
            }
        }
    }
    function checkBallStau(index,ball){
        var ball_direc=ball.direction;
        var ball_px=ball.p_x;
        var ball_py=ball.p_y;
        if(ball_direc==0){
            if(ball_px>0){//判断子弹是否超越左边界,未越界检查前方是否有障碍物
                var col=Math.ceil((ball_px)/32)-1;
                var row1=Math.floor(ball_py/32);
                var row2=Math.floor((ball_py+4)/32);
                if(tileMap[row1][col] == 0 && tileMap[row2][col] ==0){
                    return true;
                }else{
                    balls.splice(index,1);
                    return false;
                }
            }else{
                balls.splice(index,1);
                return false;
            }
        }else if(ball_direc==1){
            if(ball_py>0){
                var row=Math.ceil((ball_py)/32)-1;
                var col1=Math.floor(ball_px/32);
                var col2=Math.floor((ball_px+4)/32);
                if(tileMap[row][col1] == 0 && tileMap[row][col2] ==0){
                    return true;
                }else{
                    balls.splice(index,1);
                    return false;
                }
            }else{
                balls.splice(index,1);
                return false;
            }
        }else if(ball_direc==2){
            if(ball_px<theCanvas.width+4){
                var col=Math.ceil((ball_px+4)/32)-1;
                var row1=Math.floor(ball_py/32);
                var row2=Math.floor((ball_py+4)/32);
                if(tileMap[row1][col] == 0 && tileMap[row2][col] ==0){
                    return true;
                }else{
                    balls.splice(index,1);
                    return false;
                }
            }else{
                balls.splice(index,1);
                return false;
            }
        }else if(ball_direc==3){
            if(ball_py<theCanvas.height+4){
                var row=Math.ceil((ball_py+5)/32)-1;
                var col1=Math.floor(ball_px/32);
                var col2=Math.floor((ball_px+4)/32);
                if(tileMap[row][col1] == 0 && tileMap[row][col2] ==0){
                    return true;
                }else{
                    balls.splice(index,1);
                    return false;
                }
            }else{
                balls.splice(index,1);
                return false;
            }
        }
    }
}